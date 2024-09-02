package com.base.sbc.config.adviceadapter;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.httplog.service.HttpLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

@RequiredArgsConstructor
@ControllerAdvice
public class RequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final HttpLogService httpLogService;

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
        //如果是文件上传则不写入请求体
        HttpLog tempHttpLog;
        if (inputMessage.getHeaders().getContentType().toString().contains("multipart/form-data")) {
            tempHttpLog = httpLog;
        } else {
            tempHttpLog = BeanUtil.copyProperties(httpLog, HttpLog.class);
            tempHttpLog.setReqBody(JSON.toJSONString(body));
        }
        httpLogService.save(tempHttpLog);
        httpLog.setId(tempHttpLog.getId());
        return body;
    }
}
