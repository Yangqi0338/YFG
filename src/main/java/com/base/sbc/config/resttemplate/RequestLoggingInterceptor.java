package com.base.sbc.config.resttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String s = new String(body);
        LOGGER.info("Request URL: {}", request.getURI());
        // 可以在此处记录其他请求相关的信息，如请求方法、请求头等
        ClientHttpResponse execute = execution.execute(request, body);
        int rawStatusCode = execute.getRawStatusCode();
        // 继续执行请求
        return execute;
    }
}
