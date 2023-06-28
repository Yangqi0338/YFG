package com.base.sbc.listener;

import com.base.sbc.client.event.sample.SampleEvent;
import com.base.sbc.module.process.service.ProcessBusinessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleEventListener {
    @Autowired
    private ProcessBusinessInstanceService processBusinessInstanceService;


    @EventListener(classes = {SampleEvent.class})
    @Async
   public void  sampleClick(SampleEvent sampleEvent){
        log.info("getBusinessDataId:" + sampleEvent.getBusinessDataId());
        processBusinessInstanceService.complete( sampleEvent.getBusinessDataId(), sampleEvent.getActionCode(),sampleEvent.getObject());

   }

}

