package com.fort.feign.oauth;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fort.hystrix.oauth.OauthHystrixFallBack;
import com.fort.webapp.interceptor.OauthFeignInterceptor;

@FeignClient(name="oauth2",fallback=OauthHystrixFallBack.class,configuration=OauthFeignInterceptor.class)
public interface OauthFeign {

	@PostMapping("/oauth/check_token")
	public Map<String,Object> checkToken(@RequestParam("token") String token);
	
	@PostMapping("/oauth/token")
	public Map<String,Object> checkClientToken(@RequestParam("grant_type") String grant_type,
			@RequestParam("client_id") String clientId,@RequestParam("client_secret") String clientSecret,
			@RequestParam("scope") String scope);
}
