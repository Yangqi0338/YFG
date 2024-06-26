package com.base.sbc.config.security;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
		BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
		basicAuthenticationEntryPoint.setRealmName("webrealm");
		http.
                csrf().disable()
				.exceptionHandling()
				.authenticationEntryPoint(basicAuthenticationEntryPoint).and()
                .authorizeRequests()
                //开放文档地址不用权限    开放/api/open/下所有的请求
				.antMatchers("/v2/api-docs", "/api/open/**", "/ureport/**", "/**.html", "/**.js", "/**.css",
						"/swagger*/**", "/null/swagger*/**", "/excel/**", "/druid/**",  "/webjars/**")
				.permitAll().anyRequest().authenticated()
                .and()
				.httpBasic().disable();
    }
}
