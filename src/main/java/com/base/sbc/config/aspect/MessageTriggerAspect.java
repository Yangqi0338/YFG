package com.base.sbc.config.aspect;

import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.client.message.service.MessagesService;
import com.base.sbc.config.annotation.MessageTrigger;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.exception.RestExceptionHandler;
import com.base.sbc.config.utils.StringUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class MessageTriggerAspect {

    private static final Logger log = LoggerFactory.getLogger(MessageTriggerAspect.class);


    @Autowired
    private MessagesService messagesService;

    @Pointcut(value = "@annotation(com.base.sbc.config.annotation.MessageTrigger)")
    public void messageTriggerPointcut() {}

    @AfterReturning(pointcut = "messageTriggerPointcut()", returning = "result")
    public void handleMessageTrigger(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MessageTrigger annotation = signature.getMethod().getAnnotation(MessageTrigger.class);

        // 根据方法的返回值和注解中的配置进行处理
        processResult(result, annotation.value());
    }

    private void processResult(Object result, String value) {
        if (Objects.isNull(result) || StringUtils.isEmpty(value)){
            return;
        }
        ApiResult apiResult = (ApiResult) result;
        ModelMessage modelMessage = new ModelMessage();
        modelMessage.setModelCode(value);
        modelMessage.setParams(apiResult.getMessageObjects());
        String s = messagesService.tokenNoticeOrMessageByModel(modelMessage);
        log.info("MessageTriggerAspect processResult result = {}",s);
    }

}
