package com.fort.webapp.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.fort.module.user.User;
import com.fort.service.oauth.UserDetailsServiceImpl;

public class Oauth2ResourceOwnerPasswordTokenGranter extends ResourceOwnerPasswordTokenGranter {

	private UserDetailsService userDetailsService;

	public Oauth2ResourceOwnerPasswordTokenGranter(
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory,UserDetailsService userDetailsService) {
		super(null, tokenServices, clientDetailsService, requestFactory);
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
		String username = parameters.get("username");
		//String imgCode = parameters.get("imgCode");
		String password = parameters.get("password");
		// Protect from downstream leaks of password
		parameters.remove("password");
		
		User user = ((UserDetailsServiceImpl)userDetailsService).login(username, password);
		
		//UserDetails userDetails = ((UserDetailsServiceImpl)userDetailsService).loadUserByUsername(username);
		
		if(user == null) {
			throw new BadCredentialsException("用户名密码错误");
		}
		user.setPassword("");
		Authentication userAuth = new UsernamePasswordAuthenticationToken(username, "", user.getAuthorities());

		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
	}

}
