package com.fort.service.asset;

import com.fort.module.asset.Asset;
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
}
