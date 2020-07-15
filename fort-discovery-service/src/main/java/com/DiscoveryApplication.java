package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心也称之为服务发现 负责管理微服务调度
 * @author zhigongzhang
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(DiscoveryApplication.class);
	}
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(DiscoveryApplication.class).run(args);
	}
}
