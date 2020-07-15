package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 系统身份授权服务 提供api认证 用户身份认证
 * @author zhigongzhang
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthorizationServer
public class Oauth2Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Oauth2Application.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Oauth2Application.class, args);
	}
}
