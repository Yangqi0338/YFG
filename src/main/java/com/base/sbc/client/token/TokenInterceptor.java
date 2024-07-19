package com.base.sbc.client.token;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;


@Component
public class TokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String tokenValue = TokenHolder.get();
        if (tokenValue != null) {
            template.header("Authorization",  tokenValue);
        }
    }
}
