package com.base.sbc.module.tablecolumn.vo;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/21 10:07:41
 * @mail 247967116@qq.com
 */
@Data
public class TableColumnVo {
    /**
     * 列名
     */
    private String title;
    /**
     * colKey
     */
    private String colKey;
    /**
     * key
     */
    private String key;
    /**
     * 列宽
     */
    private Integer width;
    /**
     * 对齐方式
     */
    private String align;
    /**
     * 是否可编辑
     */
    private Boolean editable;
    /**
     * 是否可见
     */
    private Boolean visible;
    /**
     * 是否可排序
     */
    private Boolean sortable;
    /**
     * 是否可拖拽
     */
    private Boolean draggable;
    /**
     * 是否可调整列宽
     */
    private Boolean resizable;
    /**
     * 是否固定列
     */
    private Boolean fixed;
    /**
     * 是否隐藏
     */
    private Boolean hidden;
    /**
     * 是否可换行
     */
    private Boolean ellipsis;
    /**
     * 默认值
     */
    private String defaultVal;
    /**
     * 类型
     */
    private String type;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 是否可编辑
     */
    private Boolean disabled;

    private String num;
}
