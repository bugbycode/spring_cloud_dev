package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableCircuitBreaker
@EnableFeignClients
@EnableResourceServer
@SpringBootApplication(scanBasePackages = { "com.fort" , "com.fort.webapp.controller.global"})
public class RuleApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RuleApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RuleApplication.class, args);
	}
}
