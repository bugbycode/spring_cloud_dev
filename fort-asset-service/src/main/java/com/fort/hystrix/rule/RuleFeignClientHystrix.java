package com.fort.hystrix.rule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fort.feign.rule.RuleFeignClient;
import com.fort.module.rule.Rule;
import com.netflix.hystrix.exception.HystrixBadRequestException;

@Component
public class RuleFeignClientHystrix implements RuleFeignClient {
	
	private final Logger logger = LoggerFactory.getLogger(RuleFeignClientHystrix.class);

	@Override
	public int updateById(Rule r) {
		logger.info("更新授权信息时发生异常");
		return -1;
	}

	@Override
	public List<Rule> findRuleInfo(long assetId, String account, int protocol, String username) {
		logger.info("查询授权信息时发生异常");
		return new ArrayList<Rule>();
	}

	@Override
	public int deleteById(long id) {
		logger.info("删除授权信息时发生异常");
		return -1;
	}

}
