package com.fort.hystrix.oauth;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fort.feign.oauth.OauthFeign;

@Component
public class OauthHystrixFallBack implements OauthFeign {

	private final Logger logger = LoggerFactory.getLogger(OauthHystrixFallBack.class);
	
	@Override
	public Map<String, Object> checkToken(String token) {
		logger.info("使用Feign检查token信息时发生异常");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("error", "check_token_error");
		map.put("error_description", "使用Feign检查token信息时发生异常");
		return map;
	}

	@Override
	public Map<String, Object> checkClientToken(String grant_type, String clientId, String clientSecret, String scope) {
		logger.info("使用Feign获取token信息时发生异常");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("error", "check_client_token_error");
		map.put("error_description", "使用Feign获取token信息时发生异常");
		return map;
	}

}
