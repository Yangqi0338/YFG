package com.base.sbc.config.aspect;

import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.client.message.service.MessagesService;
import com.base.sbc.config.annotation.MessageTrigger;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.exception.RestExceptionHandler;
import com.base.sbc.config.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

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
        processResult(result, annotation.value(),annotation.code());
    }

    @Async
    public void processResult(Object result, String value, String code) {
        if (Objects.isNull(result) || StringUtils.isEmpty(value)){
            return;
        }
        ApiResult apiResult = (ApiResult) result;
        ModelMessage modelMessage = new ModelMessage();
        modelMessage.setModelCode(value);
        List<Map<String, String>> messageObjects = apiResult.getMessageObjects();

        if (CollUtil.isEmpty(messageObjects)){
            Map<String, String> map = Maps.newHashMap();
            if (StringUtils.isNotEmpty(code)){
                map.put("triggerActionCode",code);
            }
            modelMessage.setParams(map);
            if (StringUtils.isEmpty(map.get("triggerActionCode"))){
                return;
            }
            messagesService.tokenNoticeOrMessageByModel(modelMessage);
        }else {
            for (Map<String, String> messageObject : messageObjects) {
                if (StringUtils.isNotEmpty(code)){
                    messageObject.put("triggerActionCode",code);
                }
                ModelMessage modelMessage1 = BeanUtil.copyProperties(modelMessage, ModelMessage.class);
                if (StringUtils.isEmpty(messageObject.get("triggerActionCode"))){
                    continue;
                }
                modelMessage1.setParams(messageObject);
                messagesService.tokenNoticeOrMessageByModel(modelMessage1);
            }
        }
    }

}
