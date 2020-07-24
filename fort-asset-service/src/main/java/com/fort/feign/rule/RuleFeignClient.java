package com.fort.feign.rule;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fort.hystrix.rule.RuleFeignClientHystrix;
import com.fort.module.rule.Rule;
import com.fort.webapp.interceptor.FeignHeadersInterceptor;

@FeignClient(name = "rule",contextId = "ruleFeignClient",fallback = RuleFeignClientHystrix.class, configuration = FeignHeadersInterceptor.class)
public interface RuleFeignClient {

	@PostMapping("/rule/updateById")
	public int updateById(@RequestBody Rule r);
	
	@GetMapping("/rule/findRuleInfo")
	public List<Rule> findRuleInfo(@RequestParam(name = "assetId",defaultValue = "0") long assetId, 
			@RequestParam(name = "account",defaultValue = "") 
			String account, 
			@RequestParam(name = "protocol",defaultValue = "") 
			int protocol, 
			@RequestParam(name = "username",defaultValue = "") 
			String username);
	
	@PostMapping("/rule/deleteById")
	public int deleteById(@RequestParam("id") long id);
}
