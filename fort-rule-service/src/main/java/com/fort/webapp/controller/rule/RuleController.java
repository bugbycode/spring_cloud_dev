package com.fort.webapp.controller.rule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fort.module.rule.Rule;
import com.fort.service.rule.RuleService;
import com.util.page.SearchResult;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;

@RestController
@RequestMapping("/rule")
public class RuleController {
	
	private final Logger logger = LoggerFactory.getLogger(RuleController.class);
	
	@Autowired
	private RuleService ruleService;
	
	@GetMapping("/query")
	public SearchResult<Rule> query(@RequestParam(name = "keyword",defaultValue = "") String keyword,
			@RequestParam(name = "offset",defaultValue = "0") int offset, 
			@RequestParam(name = "limit",defaultValue = "20") int limit) {
		return ruleService.query(keyword, offset, limit);
	}
	
	@GlobalTransactional
	@PostMapping("/deleteById")
	public int deleteById(@RequestParam("id") long id) {
		return ruleService.deleteById(id);
	}
	
	@GetMapping("/findRuleInfo")
	public List<Rule> findRuleInfo(@RequestParam(name = "assetId",defaultValue = "0") long assetId, 
			@RequestParam(name = "account",defaultValue = "") 
			String account, 
			@RequestParam(name = "protocol",defaultValue = "") 
			int protocol, 
			@RequestParam(name = "username",defaultValue = "") 
			String username) {
		String xid = RootContext.getXID();
		logger.info("My xid3 as : " + xid);
		return ruleService.findRuleInfo(assetId, account, protocol, username);
	}
	
	@GlobalTransactional
	@PostMapping("/updateById")
	public int updateById(@RequestBody Rule r) {
		return ruleService.updateById(r);
	}
	
	@GlobalTransactional
	@PostMapping("/insert")
	public int insert(@RequestBody List<Rule> ruleList) {
		return ruleService.insert(ruleList);
	}
}
