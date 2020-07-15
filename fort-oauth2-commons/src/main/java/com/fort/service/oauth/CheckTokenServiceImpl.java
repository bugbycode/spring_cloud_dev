package com.fort.service.oauth;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;

import com.fort.feign.oauth.OauthFeign;

@Service("checkTokenService")
public class CheckTokenServiceImpl implements ResourceServerTokenServices {

	private final Log logger = LogFactory.getLog(CheckTokenServiceImpl.class);
	
	private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
	
	@Autowired
	private OauthFeign oauthFeign;
	
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		Map<String,Object> map = oauthFeign.checkToken(accessToken);
		
		if (map.containsKey("error")) {
			if (logger.isDebugEnabled()) {
				logger.debug("check_token returned error: " + map.get("error"));
			}
			throw new InvalidTokenException(accessToken);
		}

		// gh-838
		if (!Boolean.TRUE.equals(map.get("active"))) {
			logger.debug("check_token returned active attribute: " + map.get("active"));
			throw new InvalidTokenException(accessToken);
		}
		
		logger.debug("Check token successfully.");
		
		return tokenConverter.extractAuthentication(map);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}
}
