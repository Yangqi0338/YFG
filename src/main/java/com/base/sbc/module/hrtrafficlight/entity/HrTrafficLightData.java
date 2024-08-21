package com.base.sbc.module.hrtrafficlight.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 人事红绿灯数据实体类
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Getter
@Setter
@TableName("t_hr_traffic_light_data")
@ApiModel(value = "人事红绿灯数据实体类", description = "人事红绿灯数据实体类")
public class HrTrafficLightData extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人事红绿灯主表 ID
     */
    @ApiModelProperty("人事红绿灯主表 ID")
    private String hrTrafficLightVersionId;

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
     * columnValue
     */
    @ApiModelProperty("字段值")
    private String columnValue;

    /**
     * 行号
     */
    @ApiModelProperty("行号")
    private Integer rowIdx;
    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandCode;
    /**
     * 工号
     */
    @ApiModelProperty("工号")
    private String username;

}
