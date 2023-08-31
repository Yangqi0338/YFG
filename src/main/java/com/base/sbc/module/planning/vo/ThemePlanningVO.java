package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("主题企划保存")
@Data
public class ThemePlanningVO {
    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 主题企划
     */
    @ApiModelProperty(value = "主题企划")
    private String themeName;
    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
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
     * 产品季
     */
    @ApiModelProperty(value = "产品季")
    private String planningSeason;
    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;
    /**
     * 主题描述
     */
    @ApiModelProperty(value = "主题描述")
    private String themeDescription;
    /**
     * 参考附件
     */
    @ApiModelProperty(value = "参考附件")
    private String referAttachment;
    /**
     * 审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回）
     */
    @ApiModelProperty(value = "审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回）")
    private String confirmStatus;

    /**
     * 图片信息
     */
    @ApiModelProperty(value = "图片信息")
    private List<String> images;

    /**
     * 素材信息
     */
    @ApiModelProperty(value = "素材信息")
    private List<ThemePlanningMaterialVO> themePlanningMaterials;

}

