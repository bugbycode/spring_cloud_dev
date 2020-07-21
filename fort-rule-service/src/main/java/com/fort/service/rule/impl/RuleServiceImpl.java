package com.fort.service.rule.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fort.mapper.rule.RuleMapper;
import com.fort.module.rule.Rule;
import com.fort.service.rule.RuleService;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {

	@Autowired
	private RuleMapper ruleMapper;
	
	@Override
	public SearchResult<Rule> query(String keyword, int offset, int limit) {
		SearchResult<Rule> sr = new SearchResult<Rule>();
		
		int totalCount = ruleMapper.count(keyword);
		List<Rule> list = ruleMapper.query(keyword, offset, limit);
		Page page = new Page(limit,offset);
		page.setTotalCount(totalCount);
		
		sr.setList(list);
		sr.setPage(page);
		
		return sr;
	}

	@Override
	public int deleteById(long id) {
		return ruleMapper.deleteById(id);
	}

	@Override
	public List<Rule> findRuleInfo(long assetId, String account, int protocol, String username) {
		return ruleMapper.findRuleInfo(assetId, account, protocol, username);
	}

	@Override
	public int updateById(Rule r) {
		return ruleMapper.updateById(r);
	}

	@Override
	public int insert(Rule r) {
		return ruleMapper.insert(r);
	}

	@Override
	public int insert(List<Rule> ruleList) {
		int rows = 0;
		if(!CollectionUtils.isEmpty(ruleList)) {
			for(Rule r : ruleList) {
				List<Rule> dbRule = findRuleInfo(r.getAssetId(), r.getAccount(), r.getProtocol(), r.getUsername());
				if(CollectionUtils.isEmpty(dbRule)) {
					rows += insert(r);
				}
			}
		}
		return rows;
	}

}
