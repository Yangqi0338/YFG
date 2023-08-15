package com.base.sbc.module.planning.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("颜色企划")
public class ColorPlanningListVO {
    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 颜色企划名称
     */
    @ApiModelProperty(value = "颜色企划名称")
    private String colorPlanningName;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;

    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;

    /**
     * 产品季
     */
    @ApiModelProperty(value = "产品季")
    private String planningSeason;
    /**
     * 审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回）
     */
    @ApiModelProperty(value = "审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回）")
    private String confirmStatus;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;
    @ApiModelProperty(value = "创建人")
    private String createName;
}
