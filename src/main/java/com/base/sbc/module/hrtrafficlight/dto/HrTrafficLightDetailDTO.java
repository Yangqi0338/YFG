package com.base.sbc.module.hrtrafficlight.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 人事红绿灯列表查询入参
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯列表查询入参", description = "人事红绿灯列表查询入参")
public class HrTrafficLightDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人事红绿灯版本 ID
     */
    @ApiModelProperty("人事红绿灯版本 ID")
    private String hrTrafficLightVersionId;

    /**
     * 字段搜索
     */
    @ApiModelProperty("字段搜索")
    private String search;

    /**
     * 工号
     */
    @ApiModelProperty("工号")
    private String username;

}
