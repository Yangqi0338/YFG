package com.base.sbc.config.adviceadapter;

import com.alibaba.fastjson2.JSON;
import com.base.sbc.module.httplog.entity.HttpLog;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

@ControllerAdvice
public class RequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 对所有请求体都进行处理
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpLog httpLog = companyUserInfo.get().getHttpLog();
        httpLog.setReqBody(JSON.toJSONString(body));
        return body;
    }
}
