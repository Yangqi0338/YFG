package com.base.sbc.module.hrtrafficlight.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 人事红绿灯列表查询入参
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯列表查询入参", description = "人事红绿灯列表查询入参")
public class ListHrTrafficLightsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 人事红绿灯年份名称（字典）
     */
    @ApiModelProperty("人事红绿灯年份名称（字典）")
    private String hrYearCode;

    /**
     * 禁用状态（0-否 1-是 默认 0）
     */
    @ApiModelProperty("禁用状态（0-否 1-是 默认 0）")
    private Integer disableFlag;

}
