package com.base.sbc.config.resttemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {
//    @Bean
//    public RestTemplateCustomizer restTemplateCustomizer() {
//        return restTemplate -> {
//            restTemplate.getInterceptors().add(new RequestLoggingInterceptor());
//            // 可以添加其他拦截器或自定义RestTemplate的配置
//        };
//    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public RequestLoggingInterceptor requestLoggingInterceptor(RestTemplate restTemplate) {
        RequestLoggingInterceptor interceptor = new RequestLoggingInterceptor(restTemplate.getMessageConverters());
        restTemplate.getInterceptors().add(interceptor);
        return interceptor;
    }
}
