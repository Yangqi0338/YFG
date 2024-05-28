package com.base.sbc.module.formtype.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品主数据动态字段
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
public class GoodsDynamicFieldVo {
    /**
     * fieldName = patternClass,
     * fieldType = String,
     * fieldTypeName =单选,
     * fieldTypeCoding = SELECT
     * fieldExplain =围度大类,
     * optionDictKey = pattern,
     * val = T3,
     * valName =宽松,
     */
    @ApiModelProperty(value = "字段编码")
    private String fieldName;
    @ApiModelProperty(value = "字段类型")
    private String fieldType;
    @ApiModelProperty(value = "字段类型名")
    private String fieldTypeName;
    @ApiModelProperty(value = "字段类型编码")
    private String fieldTypeCoding;
    @ApiModelProperty(value = "字典key")
    private String optionDictKey;
    @ApiModelProperty(value = "字段名称")
    private String fieldExplain;
    @ApiModelProperty(value = "字段值")
    private String val;
    @ApiModelProperty(value = "字段值名称")
    private String valName;
}
