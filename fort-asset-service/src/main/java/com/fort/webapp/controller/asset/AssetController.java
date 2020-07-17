package com.fort.webapp.controller.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fort.module.asset.Asset;
import com.fort.service.asset.AssetService;
import com.util.page.SearchResult;

@RestController
@RequestMapping("/asset")
public class AssetController {

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
	
	@PostMapping("/updateById")
	public int updateById(@RequestBody Asset asset) {
		return assetService.updateById(asset);
	}
	
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
