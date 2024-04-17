package com.base.sbc.config.resttemplate;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.smp.dto.HttpReq;
import com.base.sbc.module.smp.dto.HttpResp;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Autowired
    @Lazy
    private PushRecordsService pushRecordsService;

    private final List<HttpMessageConverter<?>> messageConverters;
    private final HttpMessageConverterExtractor<String> extractor;

    RequestLoggingInterceptor(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
        this.extractor = new HttpMessageConverterExtractor<>(String.class, this.messageConverters);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String s = new String(body);
        LOGGER.info("Request URL: {}", request.getURI());
        HttpHeaders headers = request.getHeaders();
        HttpReq httpReq = new HttpReq(request.getURI().toString());
        String moduleName = headers.getFirst("moduleName");
        httpReq.setModuleName(moduleName);
        String functionName = headers.getFirst("functionName");
        httpReq.setFunctionName(functionName);
        httpReq.setData(s);
        pushRecordsService.prePushRecordSave(httpReq);
        // 可以在此处记录其他请求相关的信息，如请求方法、请求头等
        ClientHttpResponse execute = execution.execute(request, body);
        InputStream inputStream = execute.getBody();
        inputStream.mark(0);
        String responseData = extractor.extractData(execute);
        inputStream.reset();
        HttpResp httpResp = RestTemplateService.buildHttpResp(responseData);
        BeanUtil.copyProperties(httpReq, httpResp);
        pushRecordsService.pushRecordSave(httpResp, s, moduleName, functionName);
        // 继续执行请求
        return execute;
    }
}
