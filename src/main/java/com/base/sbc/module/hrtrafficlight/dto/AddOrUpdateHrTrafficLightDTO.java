package com.base.sbc.module.hrtrafficlight.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 人事红绿灯新增入参
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯新增入参", description = "人事红绿灯新增入参")
public class AddOrUpdateHrTrafficLightDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 人事红绿灯年份 code（字典）
     */
    @ApiModelProperty("人事红绿灯年份 code（字典）")
    private String hrYearName;

    /**
     * 人事红绿灯年份名称（字典）
     */
    @ApiModelProperty("人事红绿灯年份名称（字典）")
    private String hrYearCode;

    /**
     * 排序（正序）
     */
    @ApiModelProperty("排序（正序）")
    private Integer sort;

}
