package com.fort.webapp.controller.asset;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fort.module.asset.Asset;
import com.fort.service.asset.AssetService;
import com.util.exception.FortError;
import com.util.exception.FortException;
import com.util.page.SearchResult;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;

@RestController
@RequestMapping("/asset")
public class AssetController {
	
	private final Logger logger = LoggerFactory.getLogger(AssetController.class);

	@Autowired
	private AssetService assetService;
	
	@GetMapping("/query")
	public SearchResult<Asset> query(
			@RequestParam(name = "queryParam", defaultValue = "")
			String keyword,
			@RequestParam(name = "offset", defaultValue = "0")
			int offset,
			@RequestParam(name = "limit", defaultValue = "20")
			int limit){
		return assetService.query(keyword, offset, limit);
	}
	
	/**
	 * 修改设备信息
	 * 
	 * @param asset
	 * @return
	 */
	@GlobalTransactional
	@PostMapping("/updateById")
	public int updateById(@RequestBody Asset asset) {
		String xid = RootContext.getXID();
		logger.info("My xid as : " + xid);
		
		int rows = assetService.updateById(asset);

		if("mytest".equals(asset.getName())) {
			throw new FortException(FortError.SERVER_ERROR);
		}
		return rows;
	}
	
	@GlobalTransactional
	@PostMapping("/insert")
	public long insert(@RequestBody Asset asset) {
		return assetService.insert(asset);
	}
	
	@GetMapping("/queryById")
	public Asset queryById(long id) {
		return assetService.queryById(id);
	}
	
	@PostMapping("/deleteById")
	public long deleteById(long id) {
		return assetService.deleteById(id);
	}
}
