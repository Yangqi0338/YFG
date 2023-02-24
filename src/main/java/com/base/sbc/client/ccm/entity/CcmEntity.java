package com.base.sbc.client.ccm.entity;
/**
 * @author Youkehai
 * @data 创建时间:2021/4/1
 */
public class CcmEntity {
    /**名称*/
    private String name;
    /**数据值*/
    private String value;
    /**类型*/
    private String type;
    /**用户名*/
    private String userName;
    /**编码*/
    private String code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
