package com.base.sbc.module.patternlibrary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 版型库-模板表
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_library_template")
@ApiModel(value = "TPatternLibraryTemplate对象", description = "版型库-模板表")
public class PatternLibraryTemplate extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编码
     */
    @ApiModelProperty("模板编码")
    private String code;

    /**
     * 唯一 ID 系统自动生成
     */
    @ApiModelProperty("唯一 ID 系统自动生成")
    private String soleId;

    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    private String name;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    private Integer sort;

    /**
     * 不可修改 JSON 数据（取数据字典「版型类型-PatternType」）
     */
    @ApiModelProperty("不可修改 JSON 数据（取数据字典「版型类型-PatternType」）")
    private String unmodifiable;

    /**
     * 可修改 JSON 数据（取数据字典「版型类型-PatternType」）
     */
    @ApiModelProperty("可修改 JSON 数据（取数据字典「版型类型-PatternType」）")
    private String modifiable;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;

}
