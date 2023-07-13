package com.base.sbc.client.amc.vo;

import lombok.Data;

import java.util.List;

@Data
public class FieldDataPermissionVO {
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段值
     */
    private List<String> fieldValues;
    /**
     * 字段值id
     */
    private List<String> fieldValueIds;
}
