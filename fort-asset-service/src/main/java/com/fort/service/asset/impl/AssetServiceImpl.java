package com.fort.service.asset.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fort.feign.rule.RuleFeignClient;
import com.fort.mapper.asset.AccountMapper;
import com.fort.mapper.asset.AssetMapper;
import com.fort.mapper.asset.ProtocolMapper;
import com.fort.module.asset.Account;
import com.fort.module.asset.AccountType;
import com.fort.module.asset.Asset;
import com.fort.module.asset.AssetStatus;
import com.fort.module.asset.Protocol;
import com.fort.module.asset.ProtocolStatus;
import com.fort.module.asset.ProtocolType;
import com.fort.module.rule.Rule;
import com.fort.service.asset.AssetService;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("assetService")
public class AssetServiceImpl implements AssetService {

	@Autowired
	private AssetMapper assetMapper;
	
	@Autowired
	private AccountMapper accountMapper;
	
	@Autowired
	private ProtocolMapper protocolMapper;
	
	@Autowired
	private RuleFeignClient ruleFeignClient;
	
	@Override
	public SearchResult<Asset> query(String keyword, int offset, int limit) {
		SearchResult<Asset> sr = new SearchResult<Asset>();
		
		try {
			int totalCount = assetMapper.count(keyword);
			List<Asset> list = assetMapper.query(keyword, offset, limit);
			Page page = new Page(limit,offset);
			page.setTotalCount(totalCount);
			
			if(!CollectionUtils.isEmpty(list)) {
				for(Asset asset : list) {
					String statusLabel = AssetStatus.ENABLED.getLabel();
					if(asset.getStatus() == AssetStatus.DISABLED.getStatus()) {
						statusLabel = AssetStatus.DISABLED.getLabel();
					}
					asset.setStatusLabel(statusLabel);
				}
			}
			
			sr.setList(list);
			sr.setPage(page);
			return sr;
		} catch (Exception e) {
			throw new RuntimeException("自定义条件分页查询设备信息时出现异常，" + e.getMessage());
		}
	}

	@Override
	public int updateById(Asset asset) {
		long assetId = asset.getId();
		Asset oldAsset = queryById(assetId);
		if(oldAsset == null) {
			throw new AccessDeniedException("该设备已被删除");
		}
		try {

			Asset dbAsset = assetMapper.queryByName(asset.getName());
			
			asset.setUpdateTime(new Date());
			int rows = assetMapper.updateById(asset);
			List<Account> accountList = asset.getAccountList();
			List<Protocol> protocolList = asset.getProtocolList();
			
			List<Account> accountListBack = oldAsset.getAccountList();
			List<Protocol> protocolListBack = oldAsset.getProtocolList();
			
			//新增或修改协议
			for(Protocol pro : protocolList) {
				int type = pro.getType();
				if(pro.getStatus() == ProtocolStatus.ENABLED.getStatus()) {
					pro.setStatus(ProtocolStatus.ENABLED.getStatus());
				} else {
					pro.setStatus(ProtocolStatus.DISABLED.getStatus());
				}
				
				if(!(type == ProtocolType.SSH.getType() || type == ProtocolType.RDP.getType() 
						|| type == ProtocolType.SFTP.getType())) {
					throw new RuntimeException("协议类型错误");
				}
				
				if(pro.getPort() == null || pro.getPort() == 0) {
					if(type == ProtocolType.SSH.getType()) {
						pro.setPort(ProtocolType.SSH.getPort());
					} else if(type == ProtocolType.RDP.getType()) {
						pro.setPort(ProtocolType.RDP.getPort());
					} else if(type == ProtocolType.SFTP.getType()) {
						pro.setPort(ProtocolType.SFTP.getPort());
					}
				}
				pro.setAssetId(assetId);
				Protocol dbPro = findByType(type, protocolListBack);
				if(dbPro == null) {
					protocolMapper.insert(pro);
				} else {
					pro.setId(dbPro.getId());
					int r = protocolMapper.updateById(pro);
					if (r < 0) {
						throw new RuntimeException("修改授权规则发生异常");
					}
				}
			}
			
			//删除协议
			for(Protocol pro : protocolListBack) {
				int type = pro.getType();
				Protocol newPro = findByType(type, protocolList);
				if(newPro == null) {
					protocolMapper.deleteById(pro.getId());
					List<Rule> ruleList = ruleFeignClient.findRuleInfo(assetId, null, type, null);
					if(!CollectionUtils.isEmpty(ruleList)) {
						for(Rule r : ruleList) {
							ruleFeignClient.deleteById(r.getId());
						}
					}
				}
			}
			
			//新增或修改账号
			for(Account acc : accountList) {
				String name = acc.getName();
				if(StringUtils.isEmpty(name)) {
					throw new RuntimeException("账号名称为空");
				}
				acc.setAssetId(assetId);
				if(acc.getType() == AccountType.ROOT.getType()) {
					acc.setType(AccountType.ROOT.getType());
				} else {
					acc.setType(AccountType.GENERAL.getType());
				}
				Account dbAcc = findByName(name, accountListBack);
				acc.setAssetId(assetId);
				if(dbAcc == null) {
					accountMapper.insert(acc);
				} else {
					dbAcc.setPassword(acc.getPassword());
					dbAcc.setType(acc.getType());
					int r = accountMapper.updateById(dbAcc);
					if (r < 0) {
						throw new RuntimeException("修改授权规则发生异常");
					}
				}
			}
			//删除账号
			for(Account acc : accountListBack) {
				String name = acc.getName();
				Account newAcc = findByName(name, accountList);
				if(newAcc == null) {
					accountMapper.deleteById(acc.getId());
					List<Rule> ruleList = ruleFeignClient.findRuleInfo(assetId, acc.getName(), -1, null);
					if(!CollectionUtils.isEmpty(ruleList)) {
						for(Rule r : ruleList) {
							ruleFeignClient.deleteById(r.getId());
						}
					}
				}
			}
			//更新授权规则的设备信息
			List<Rule> ruleList = ruleFeignClient.findRuleInfo(assetId, null, -1, null);
			if(!CollectionUtils.isEmpty(ruleList)) {
				for(Rule r : ruleList) {
					r.setAssetName(asset.getName());
					r.setAssetIp(asset.getIp());
					r.setOsName(asset.getOsName());
					r.setOsVersion(asset.getOsVersion());
					int rw = ruleFeignClient.updateById(r);
					if (rw < 0) {
						throw new RuntimeException("修改授权规则发生异常");
					}
				}
			}
			
			if(dbAsset != null && dbAsset.getId() != oldAsset.getId()) {
				throw new RuntimeException("设备名称重复");
			}
			
			return rows;
		} catch (Exception e) {
			throw new RuntimeException("修改设备信息时出现异常，" + e.getMessage());
		}
	}

	@Override
	public int insert(Asset asset) {
		try {
			asset.setCreateTime(new Date());
			int rows = assetMapper.insert(asset);
			long assetId = asset.getId();
			List<Account> accountList = asset.getAccountList();
			if(!CollectionUtils.isEmpty(accountList)) {
				for(Account account : accountList) {
					String name = account.getName();
					if(StringUtils.isEmpty(name)) {
						throw new RuntimeException("账号名称为空");
					}
					account.setAssetId(assetId);
					if(account.getType() == AccountType.ROOT.getType()) {
						account.setType(AccountType.ROOT.getType());
					} else {
						account.setType(AccountType.GENERAL.getType());
					}
					accountMapper.insert(account);
				}
			}
			List<Protocol> protocolList = asset.getProtocolList();
			if(!CollectionUtils.isEmpty(protocolList)) {
				for(Protocol pro : protocolList) {
					if(pro.getStatus() == ProtocolStatus.ENABLED.getStatus()) {
						pro.setStatus(ProtocolStatus.ENABLED.getStatus());
					} else {
						pro.setStatus(ProtocolStatus.DISABLED.getStatus());
					}
					
					int type = pro.getType();
					if(!(type == ProtocolType.SSH.getType() || type == ProtocolType.RDP.getType() 
							|| type == ProtocolType.SFTP.getType())) {
						throw new RuntimeException("协议类型错误");
					}
					
					if(pro.getPort() == null || pro.getPort() == 0) {
						if(type == ProtocolType.SSH.getType()) {
							pro.setPort(ProtocolType.SSH.getPort());
						} else if(type == ProtocolType.RDP.getType()) {
							pro.setPort(ProtocolType.RDP.getPort());
						} else if(type == ProtocolType.SFTP.getType()) {
							pro.setPort(ProtocolType.SFTP.getPort());
						}
					}
					pro.setAssetId(assetId);
					protocolMapper.insert(pro);
				}
			}
			return rows;
		} catch (Exception e) {
			throw new RuntimeException("添加设备信息时出现异常，" + e.getMessage());
		}
	}

	@Override
	public int deleteById(long id) {
		try {
			return assetMapper.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("根据设备ID删除设备信息时出现异常，" + e.getMessage());
		}
	}

	@Override
	public Asset queryById(long id) {
		try {
			return assetMapper.queryById(id);
		} catch (Exception e) {
			throw new RuntimeException("根据设备ID查询设备信息时出现异常，" + e.getMessage());
		}
	}
	
	private Account findByName(String name,List<Account> accList) {
		for(Account acc : accList) {
			if(acc.getName().equals(name)) {
				return acc;
			}
		}
		return null;
	}
	
	private Protocol findByType(int type,List<Protocol> proList) {
		for(Protocol pro : proList) {
			if(pro.getType() == type) {
				return pro;
			}
		}
		return null;
	}

}
