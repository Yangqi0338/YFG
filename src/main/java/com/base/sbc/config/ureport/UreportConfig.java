//package com.base.sbc.config.ureport;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportResource;
//
//import com.bstek.ureport.console.UReportServlet;
//
//@Configuration
//@EnableAutoConfiguration
//@ImportResource("classpath:ureport-console-context.xml") // 导致无法注册到nacos, 异常：registry.NacosServiceRegistry : No service
//															// to register for nacos client...
//public class UreportConfig {
//
//	@Bean
//	public ServletRegistrationBean buildUreportServlet() {
//		return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");
//	}
//
//}
