package com.base.sbc.config.resttemplate;

import com.base.sbc.module.pushrecords.service.PushRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Autowired
    @Lazy
    private PushRecordsService pushRecordsService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String s = new String(body);
        LOGGER.info("Request URL: {}", request.getURI());
//        pushRecordsService.prePushRecordSave(jsonObject.toJSONString(), "scm", "订货本一键投产");
        // 可以在此处记录其他请求相关的信息，如请求方法、请求头等
        ClientHttpResponse execute = execution.execute(request, body);
        int rawStatusCode = execute.getRawStatusCode();
        // 继续执行请求
        return execute;
    }
}
