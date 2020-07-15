package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 微服务配置中心 管理微服务通用配置信息 如数据源配置
 * @author zhigongzhang
 *
 */
@SpringBootApplication
@EnableConfigServer
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
public class FortConfigApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FortConfigApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FortConfigApplication.class, args);
	}
}
