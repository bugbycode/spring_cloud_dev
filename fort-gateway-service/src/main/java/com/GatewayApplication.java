package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.fort.webapp.filter.Oauth2ZuulFilter;
import com.netflix.zuul.ZuulFilter;

@EnableFeignClients
@SpringBootApplication
@EnableZuulProxy
@EnableCircuitBreaker
@ServletComponentScan
public class GatewayApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GatewayApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	@Bean
	public ZuulFilter oauth2ZuulFilter() {
		return new Oauth2ZuulFilter();
	}
}
