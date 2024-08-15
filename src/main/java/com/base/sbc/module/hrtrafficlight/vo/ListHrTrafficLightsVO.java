package com.base.sbc.module.hrtrafficlight.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 人事红绿灯列表查询返回值
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯列表查询返回值", description = "人事红绿灯列表查询返回值")
public class ListHrTrafficLightsVO extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

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
