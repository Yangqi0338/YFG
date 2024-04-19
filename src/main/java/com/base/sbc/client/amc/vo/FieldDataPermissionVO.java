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
     * 自定义sql隔离
     */
    private String sqlField;
    /**
     * 字段值
     */
    private List<String> fieldValues;
    /**
     * 字段值名称
     */
    private List<String> fieldValueName;

    /**
     * 条件类型：in.包含、=.等于
     */
    private String conditionType;

    /**
     * 字段查询类型：and.且、or.或
     */
    private String selectType;


    private String groupSelectType;

    private String groupIdx;
}
