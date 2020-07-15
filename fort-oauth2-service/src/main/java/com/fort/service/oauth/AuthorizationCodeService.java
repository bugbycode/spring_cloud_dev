package com.fort.service.oauth;

import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Service;

@Service("authorizationCodeService")
public class AuthorizationCodeService implements AuthorizationCodeServices{

	@Override
	public String createAuthorizationCode(OAuth2Authentication authentication) {
		throw new RuntimeException("Unrealized");
	}

	@Override
	public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
		throw new RuntimeException("Unrealized");
	}

}
