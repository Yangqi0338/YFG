package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
@ApiModel("颜色企划保存")
public class ColorPlanningSaveDTO {
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 颜色企划名称
     */
    @ApiModelProperty(value = "颜色企划名称")
    @NotBlank(message = "颜色企划名称不可为空")
    private String colorPlanningName;
    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
    @NotBlank(message = "品牌不可为空")
    private String brandCode;
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
     * 年份编码
     */
    @ApiModelProperty(value = "年份编码")
    @NotBlank(message = "年份不可为空")
    private String yearCode;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节编码
     */
    @ApiModelProperty(value = "季节编码")
    private String seasonCode;
    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    @NotBlank(message = "产品季不可为空")
    private String planningSeasonId;
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
    @ApiModelProperty(value = "颜色列表")
    private List<ColorPlanningItemSaveDTO> colorPlanningItemSaves;
}
