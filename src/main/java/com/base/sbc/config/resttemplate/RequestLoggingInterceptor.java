package com.base.sbc.config.resttemplate;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.smp.dto.HttpReq;
import com.base.sbc.module.smp.dto.HttpResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.util.List;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Autowired
    @Lazy
    private PushRecordsService pushRecordsService;

    private final List<HttpMessageConverter<?>> messageConverters;
    private final HttpMessageConverterExtractor<String> extractor;

    private final static TransmittableThreadLocal<HttpResp> RESPONSE = new TransmittableThreadLocal<>();

    RequestLoggingInterceptor(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
        this.extractor = new HttpMessageConverterExtractor<>(String.class, this.messageConverters);
    }

    public static HttpResp getResponse(){
        return RESPONSE.get();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String s = new String(body);
        LOGGER.info("Request URL: {}", request.getURI());
        HttpHeaders headers = request.getHeaders();
        HttpReq httpReq = new HttpReq(request.getURI().toString());
        try {
            String moduleName = headers.getFirst("moduleName");
            httpReq.setModuleName(moduleName);
            String functionName = headers.getFirst("functionName");
            httpReq.setFunctionName(functionName);
            String code = headers.getFirst("code");
            httpReq.setCode(code);
            String name = headers.getFirst("name");
            httpReq.setName(name);
            httpReq.setData(s);
            httpReq.setUserId(companyUserInfo.get().getUserId());
            pushRecordsService.prePushRecordSave(httpReq);
        }catch (Exception ignored) {}
        // 可以在此处记录其他请求相关的信息，如请求方法、请求头等
        ClientHttpResponse execute = execution.execute(request, body);
        // 不能读取,会导致InputStream读完,导致数据获取不到,除非使用ThreadLocal
        String responseData = extractor.extractData(execute);
        HttpResp httpResp = RestTemplateService.buildHttpResp(responseData);
        httpResp.setStatusCode(httpResp.getCode());
        BeanUtil.copyProperties(httpReq, httpResp, "data");
        pushRecordsService.pushRecordSave(httpResp, s);
        RESPONSE.set(httpResp);
        // 继续执行请求
        return execute;
    }
}
