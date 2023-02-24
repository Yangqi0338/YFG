package com.base.sbc.client.ccm.entity;
/**
 * @author lile
 * @data 创建时间:2022/07/08
 */
public class CcmWashSay {
    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /**
     * 标签名
     */
    private String name;
    /**
     * 数据值
     */
    private String code;
    /**
     * 图片
     */
    private String picture;
    /**
     * 状态(0启用 1停用)
     */
    private String status;

    /*******************************************getset方法区************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
