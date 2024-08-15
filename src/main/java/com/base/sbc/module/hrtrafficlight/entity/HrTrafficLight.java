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
 * 人事红绿灯实体类
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Getter
@Setter
@TableName("t_hr_traffic_light")
@ApiModel(value = "人事红绿灯实体类", description = "人事红绿灯实体类")
public class HrTrafficLight extends BaseDataEntity<String> implements Serializable {

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
