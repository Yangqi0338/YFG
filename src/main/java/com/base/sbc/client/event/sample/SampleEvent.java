package com.base.sbc.client.event.sample;

import org.springframework.context.ApplicationEvent;


public class SampleEvent extends ApplicationEvent {

    /*数据*/
    private final Object object;
    /*业务数据id*/
    private final String businessDataId;
    /*动作编号*/
    private final String actionCode;


    public SampleEvent(Object source, Object object, String businessDataId, String actionCode) {
        super(source);
        this.object = object;
        this.businessDataId = businessDataId;
        this.actionCode = actionCode;
    }
    
    public Object getObject(){
        return object;
    }

    public String getBusinessDataId(){
        return businessDataId;
    }

    public String getActionCode(){
        return actionCode;
    }

}

