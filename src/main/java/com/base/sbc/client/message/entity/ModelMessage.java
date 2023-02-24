package com.base.sbc.client.message.entity;

import java.util.Map;
/**
 * @author Youkehai
 * @data 创建时间:2021/6/28
 */
public class ModelMessage {

    /**
     * 模板编码
     */
    private String modelCode;

    /**
     * 键值对模板参数
     */
    private Map<String,String> params;
    /**
     * 要发送给的用户IDs
     */
    private String userIds;


    /**
     * 空参构造器
     */
    public ModelMessage() {
    }

    /**
     * 提供一个构造器 方便直接传入参数
     * @param params
     * @param userIds
     */
    public ModelMessage(Map<String, String> params, String userIds) {
        this.params = params;
        this.userIds = userIds;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

}
