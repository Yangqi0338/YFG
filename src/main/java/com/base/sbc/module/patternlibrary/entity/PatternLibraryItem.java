package com.base.sbc.module.patternlibrary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 版型库-子表
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_library_item")
@ApiModel(value = "PatternLibraryItem对象", description = "版型库-子表")
public class PatternLibraryItem extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部件库 ID（t_pattern_library id）
     */
    @ApiModelProperty("部件库 ID")
    private String patternLibraryId;

    /**
     * 系数名称/部位/部件类别
     */
    @ApiModelProperty("系数名称/部位/部件类别")
    private String name;

    /**
     * 系数编码/部位编码/部件编码
     */
    @ApiModelProperty("系数编码/部位编码/部件编码")
    private String code;

    /**
     * 实际纸样尺寸
     */
    @ApiModelProperty("实际纸样尺寸")
    private String patternSize;

    /**
     * 结构管理 Key（围度和长度使用）
     */
    @ApiModelProperty("结构管理 Key（围度和长度使用）")
    private String structureKey;

    /**
     * 结构管理 Value（围度和长度使用）
     */
    @ApiModelProperty("结构管理 Value（围度和长度使用）")
    private String structureValue;

    /**
     * 测量点说明/描述
     */
    @ApiModelProperty("测量点说明/描述")
    private String description;

    /**
     * 品类 code
     */
    @ApiModelProperty("品类 code")
    private String prodCategory;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String prodCategoryName;

    /**
     * 工艺项目
     */
    @ApiModelProperty("工艺项目")
    private String processName;

    /**
     * 类型（1-围度信息 2-长度信息 3-部位尺寸 4-涉及部件）
     */
    @ApiModelProperty("类型（1-围度信息 2-长度信息 3-部位尺寸 4-涉及部件）")
    private Integer type;

}
