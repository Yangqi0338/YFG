package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/*新增删除需求维度*/
@Data
@ApiModel("企划需求-新增删除需求 SaveDelDemandDto")
public class SaveDelDemandDto extends CheckMutexDto{

    /*产品季id*/
    @ApiModelProperty(value = "产品季id", required = true, example = "111")
    @NotBlank(message = "产品季id不能为空")
    private String planningSeasonId;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "渠道id")
    private String planningChannelId;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    /**
     * 大类
     */
    @ApiModelProperty(value = "大类")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "需求占比", required = true, example = "111")
    @NotBlank(message = "需求名称")
    private String demandName;

    @ApiModelProperty(value = "formTypeId", required = true, example = "122222")
    @NotNull(message = "表单id不能为空")
    private String formTypeId;

    /**
     * 字段id
     */
    @ApiModelProperty(value = "字段id"  )
    private String fieldId;

}
