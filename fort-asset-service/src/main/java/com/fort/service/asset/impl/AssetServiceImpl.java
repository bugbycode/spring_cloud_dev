package com.fort.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fort.mapper.asset.AssetMapper;
import com.fort.module.asset.Asset;
import com.fort.module.asset.AssetStatus;
import com.fort.service.asset.AssetService;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("assetService")
public class AssetServiceImpl implements AssetService {

	@Autowired
	private AssetMapper assetMapper;
	
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

}
