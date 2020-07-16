package com.fort.webapp.controller.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
