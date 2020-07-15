package com.fort.webapp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.util.RoleProperties;

@Configuration
public class AuthResourceConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private ResourceServerTokenServices checkTokenService;
	
	@Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(checkTokenService);
    }
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**")
			.hasRole(RoleProperties.ROLE_ADMIN); //任何接口都需要CALL_API权限
	}
}
