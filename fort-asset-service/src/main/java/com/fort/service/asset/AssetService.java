package com.fort.service.asset;

import java.util.List;

import com.fort.module.asset.Account;
import com.fort.module.asset.Asset;
import com.fort.module.asset.Protocol;
import com.util.page.SearchResult;

public interface AssetService {

	/**
	 * 自定义条件分页查询设备信息
	 * @param keyword 模糊查询条件
	 * @param offset 起始记录数
	 * @param limit 每页显示的条数
	 * @return
	 */
	public SearchResult<Asset> query(String keyword,int offset,int limit);
	
	/**
	 * 根据设备ID修改设备信息
	 * @param asset
	 * @return
	 */
	public int updateById(Asset asset);
	
	/**
	 * 添加设备信息
	 * @param asset
	 * @return
	 */
	public int insert(Asset asset);
	
	/**
	 * 根据设备ID删除设备信息
	 * @param id
	 * @return
	 */
	public int deleteById(long id);
	
	/**
	 * 根据设备ID查询设备信息
	 * @param id
	 * @return
	 */
	public Asset queryById(long id);
	
	/**
	 * 根据设备名称查询设备信息
	 * @param name
	 * @return
	 */
	public Asset queryByName(String name);
	
	public Account findByName(String name,List<Account> accList);
	
	public Protocol findByType(int type,List<Protocol> proList);
}
