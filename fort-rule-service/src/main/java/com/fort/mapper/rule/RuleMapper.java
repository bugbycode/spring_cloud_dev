package com.fort.mapper.rule;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fort.module.rule.Rule;

public interface RuleMapper {

	public List<Rule> query(@Param("keyword") String keyword,@Param("offset") int offset,@Param("limit") int limit);
	
	public int count(@Param("keyword") String keyword);
	
	public int deleteById(long id);
	
	public List<Rule> findRuleInfo(@Param("assetId") long assetId,@Param("account") String account,
			@Param("protocol") int protocol,@Param("username") String username);
	
	public int updateById(Rule r);
	
	public int insert(Rule r);
}
