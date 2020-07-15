package com.fort.webapp.interceptor;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OauthFeignInterceptor implements RequestInterceptor {

	private final Log logger = LogFactory.getLog(OauthFeignInterceptor.class);
	
	@Value("${spring.oauth.clientId}")
	private String clientId;
	
	@Value("${spring.oauth.clientSecret}")
	private String clientSecret;
	
	@Override
	public void apply(RequestTemplate template) {
		logger.debug("Feign check token......");
		if(template.url().startsWith("/oauth/check_token")) {
			template.header("Authorization", getAuthorizationHeader(clientId,clientSecret));
		}
	}

	private String getAuthorizationHeader(String clientId, String clientSecret) {

		if(clientId == null || clientSecret == null) {
			logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
		}

		String creds = String.format("%s:%s", clientId, clientSecret);
		try {
			return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Could not convert String");
		}
	}
}
