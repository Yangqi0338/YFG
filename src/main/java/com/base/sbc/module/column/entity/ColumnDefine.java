/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：系统级列自定义 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.entity.ColumnDefine
 * @email your email
 * @date 创建时间：2023-12-6 17:33:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_column_define")
@ApiModel("系统级列自定义 ColumnDefine")
public class ColumnDefine extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @TableField(exist = false)
    @ApiModelProperty("系统列自定义ID")
    private String sysId;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 表格编码
     */
    @ApiModelProperty(value = "表格编码")
    private String tableCode;
    /**
     * 列编码
     */
    @ApiModelProperty(value = "列编码")
    private String columnCode;
    /**
     * 列名
     */
    @ApiModelProperty(value = "列名")
    private String columnName;
    /**
     * 列名国际化key
     */
    @ApiModelProperty(value = "列名国际化key")
    private String columnNameI18nKey;
    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private String hidden;
    /**
     * 是否必须返回
     */
    @ApiModelProperty(value = "是否必须返回")
    private String requiredReturn;
    /**
     * 对齐方式
     */
    @ApiModelProperty(value = "对齐方式")
    private String alignType;
    /**
     * 固定方式
     */
    @ApiModelProperty(value = "固定方式")
    private String fixType;
    /**
     * 是否超链接
     */
    @ApiModelProperty(value = "是否超链接")
    private String link;
    /**
     * 是否可排序
     */
    @ApiModelProperty(value = "是否可排序")
    private String isSort;
    /**
     * 是否可查询
     */
    @ApiModelProperty(value = "是否可查询")
    private String isQuery;
    /**
     * 是否可编辑
     */
    @ApiModelProperty(value = "是否可编辑")
    private String isEdit;
    /**
     * 是否必填
     */
    @ApiModelProperty(value = "是否必填")
    private String isRequired;
    /**
     * 列字段类型
     */
    @ApiModelProperty(value = "列字段类型")
    private String columnType;
    /**
     * 数据格式
     */
    @ApiModelProperty(value = "数据格式")
    private String dataFormat;
    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    /**
     * 列过滤器
     */
    @ApiModelProperty(value = "列过滤器")
    private String columnFilter;
    /**
     * 列宽
     */
    @ApiModelProperty(value = "列宽")
    private Integer columnWidth;
    /**
     * 默认排序
     */
    @ApiModelProperty(value = "默认排序")
    private Integer sortOrder;
    /**
     * 自定义渲染方法
     */
    @ApiModelProperty(value = "自定义渲染方法")
    private String customRender;
    /**
     * 字段颜色
     */
    @ApiModelProperty(value = "字段颜色")
    private String columnColor;

    /**
     * 导出别名
     */
    @ApiModelProperty(value = "导出别名")
    private String exportAlias;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;

    /**
     * 表字段名
     */
    @ApiModelProperty(value = "表字段名")
    private String sqlCode;

    /**
     * 列头筛选类型
     */
    @ApiModelProperty(value = "列头筛选类型")
    private String property;

    /**
     * 过滤器扩展
     */
    @ApiModelProperty(value = "过滤器扩展")
    private String columnFilterExtent;

    /**
     * 是否省略(1省略,0或空省略)
     */
    @ApiModelProperty(value = "是否省略(1省略,0或空省略)")
    private String columnEllipsis;

    @TableField(exist = false)
    @ApiModelProperty(value = "分组")
    private List<ColumnDefine> children;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
