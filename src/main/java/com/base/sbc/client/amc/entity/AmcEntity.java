package com.base.sbc.client.amc.entity;
/**
 *  amc 实体类
 * @author lilele
 * @data 创建时间:2021/12/24
 */
public class AmcEntity {
    /**名称*/
    private String name;
    /**数据值*/
    private String value;
    /**类型*/
    private String type;
    /** id */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
