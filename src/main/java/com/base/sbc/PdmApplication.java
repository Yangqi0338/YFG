package com.base.sbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableHystrix
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PdmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdmApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// sessionid 多次访问一致
		corsConfiguration.setAllowCredentials(true);
		// 允许任何域名使用
		corsConfiguration.addAllowedOrigin("*");
		// 允许任何头
		corsConfiguration.addAllowedHeader("*");
		// 允许任何方法（post、get等）
		corsConfiguration.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 对接口配置跨域设置
		source.registerCorsConfiguration("/**", corsConfiguration);
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
		// 设置 Filter 的优先级为最高优先级(如果有多个过滤器这些过滤器会有一个先后顺序的问题)
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegistrationBean;

	}
}
