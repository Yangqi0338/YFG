package com.base.sbc.module.xxljob;

import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.client.message.service.MessagesFeignService;
import com.google.common.collect.Maps;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

@Component
public class SampleXxlJob {

    @Resource
    private MessagesFeignService messagesFeignService;

    private final static Logger log = LoggerFactory.getLogger(SampleXxlJob.class);


    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() {
        String messageCode = "pre_production_sample_board";
        boolean b = messagesFeignService.timingEndTime(messageCode);
        if (b){
            log.info("SampleXxlJob demoJobHandler timingEndTime");
            return;
        }
        Map<String,String> params = Maps.newHashMap();
        params.put("prod_category","品类111");
        params.put("prodCategory_2nd","中类111");
        params.put("prodCategory_3rd","小类111");
        params.put("designer","测试人");
        params.put("设计部","xx部门");
        params.put("triggerActionCode","001");
        ModelMessage modelMessage = new ModelMessage();
        modelMessage.setModelCode(messageCode);
        modelMessage.setParams(params);
        String s = messagesFeignService.noticeOrMessageByModel(modelMessage);
        log.info("demoJobHandler send result={}",s);

    }
}
