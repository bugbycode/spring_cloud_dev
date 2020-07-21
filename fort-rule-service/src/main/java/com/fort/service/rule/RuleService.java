package com.fort.service.rule;

import java.util.List;

import com.fort.module.rule.Rule;
import com.util.page.SearchResult;

public interface RuleService {

	public SearchResult<Rule> query(String keyword,int offset,int limit);
	
	public int deleteById(long id);
	
	public List<Rule> findRuleInfo(long assetId,String account,
			int protocol,String username);
	
	public int insert(Rule r);
	
	public int updateById(Rule r);
	
	public int insert(List<Rule> ruleList);
}
