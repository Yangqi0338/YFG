package com.base.sbc.module.patternlibrary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 版型库-模板表-子表
 *
 * @author xhte
 * @create 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_library_template_item")
@ApiModel(value = "TPatternLibraryTemplateItem对象", description = "版型库-模板表-子表")
public class PatternLibraryTemplateItem extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型库-模板表 ID（（t_pattern_library_template id ）
     */
    @ApiModelProperty("版型库-模板表 ID")
    private String templateId;

    /**
     * 类型（1-可修改 2-不可修改）
     */
    @ApiModelProperty("类型（1-可修改 2-不可修改）")
    private String type;

    /**
     * 版型类型 code（取 PatternType 字典）
     */
    @ApiModelProperty("版型类型 code（取 PatternType 字典）")
    private String patternType;

    /**
     * 版型类型名称（取 PatternType 字典）
     */
    @ApiModelProperty("版型类型名称（取 PatternType 字典）")
    private Integer patternTypeName;

}
