package com.base.sbc.config.security;

import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
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
        http.
                csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Bearer realm=\"webrealm\""))
                .and()
                .authorizeRequests()
                //开放文档地址不用权限    开放/api/open/下所有的请求
                .mvcMatchers("/v2/api-docs","/api/open/**","/**.html","/**.js","/**.css","/swagger*/**","/null/swagger*/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
