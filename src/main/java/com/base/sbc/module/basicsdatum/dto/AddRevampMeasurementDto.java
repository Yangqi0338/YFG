package com.base.sbc.module.basicsdatum.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 新增修改测量点Dto类
 * */
@Data
public class AddRevampMeasurementDto {

    private String id;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    @ApiModelProperty(value = "测量点"  )
    private String measurement;
    /** POM类型 */
    @ApiModelProperty(value = "POM类型"  )
    private String pdmType;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String description;
    /** 描述(Alt) */
    @ApiModelProperty(value = "描述(Alt)"  )
    private String descriptionAlt;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String image;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

}
