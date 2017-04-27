package com.liu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * war部署使用，boot测试时注释
 */
@SpringBootApplication
public class EasyDeployWarApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EasyDeployWarApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EasyDeployWarApplication.class, args);
	}
}
