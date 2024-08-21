package com.base.sbc.client.message.service;

import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.config.common.ApiResult;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

import javax.annotation.Resource;

@Component
public class MessagesFeignService {

    @Resource
    private MessagesService messagesService;


    public String noticeOrMessageByModel( ModelMessage message){
        return messagesService.noticeOrMessageByModel(message);
    }

    public boolean timingEndTime( String code){
        ApiResult<Boolean> apiResult = messagesService.timingEndTime(code);
        if (0 != apiResult.getStatus()){
            return false;
        }
        if (Objects.isNull(apiResult.getData())){
            return false;
        }
        return apiResult.getData();
    }

}
