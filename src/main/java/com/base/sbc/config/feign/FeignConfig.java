package com.base.sbc.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 防止Feign请求 头部token丢失
 * @author xiong
 *
 */
@Configuration
public class FeignConfig {
	
	@Bean
    public RequestInterceptor headerInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
                // 不是前端请求过来会没有ServletRequestAttributes对象，如定时任务
                if(attributes!=null){
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        while (headerNames.hasMoreElements()) {
                            String name = headerNames.nextElement();
                            // springboot2.x+ 增加了参数校验,会检查content-length
                            if (name.equals("content-length")){
                                continue;
                            }
                            if(!"Content-type".equalsIgnoreCase(name)){
                                String values = request.getHeader(name);
                                requestTemplate.header(name, values);
                            }else{
                                requestTemplate.header("Content-type", "application/json");
                            }
                        }
                    }
                }

            }
        };
    }
}
