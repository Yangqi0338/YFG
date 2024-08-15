package com.base.sbc.module.hrtrafficlight.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 人事红绿灯详情入参
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯详情入参", description = "人事红绿灯详情入参")
public class HrTrafficLightsDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 一级字段名称
     */
    @ApiModelProperty("一级字段名称")
    private String columnNameOne;

    /**
     * 二级字段名称
     */
    @ApiModelProperty("二级字段名称")
    private String columnNameTwo;

    /**
     * 字段值
     */
    @ApiModelProperty("字段值")
    private String columnValue;

    /**
     * 行号
     */
    @ApiModelProperty("行号")
    private Integer rowIdx;

}
