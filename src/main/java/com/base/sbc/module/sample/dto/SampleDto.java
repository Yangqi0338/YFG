package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：样衣dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:41
 */
@Data
@ApiModel("样衣 SampleDto")
public class SampleDto {
    @ApiModelProperty(value = "样衣id")
    private String id;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 波段企划id
     */
    @ApiModelProperty(value = "波段企划id")
    private String planningBandId;
    /**
     * 品类信息id
     */
    @ApiModelProperty(value = "品类信息id")
    private String planningCategoryId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;
    /**
     * 品类名称路径:(大类/品类/中类/小类)
     */
    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
    private String categoryName;
    /**
     * 品类id路径:(大类/品类/中类/小类)
     */
    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)")
    private String categoryIds;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 旧设计款号
     */
    @ApiModelProperty(value = "旧设计款号")
    private String hisDesignNo;
    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    /**
     * 款式类型
     */
    @ApiModelProperty(value = "款式类型")
    private String styleType;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 主题
     */
    @ApiModelProperty(value = "主题")
    private String subject;
    /**
     * 版型
     */
    @ApiModelProperty(value = "版型")
    private String plateType;
    /**
     * 号型类型
     */
    @ApiModelProperty(value = "号型类型")
    private String sizeRange;
    /**
     * 开发分类
     */
    @ApiModelProperty(value = "开发分类")
    private String devClass;
    /**
     * 生产类型
     */
    @ApiModelProperty(value = "生产类型")
    private String devtType;
    /**
     * Default尺码
     */
    @ApiModelProperty(value = "Default尺码")
    private String defaultSize;
    /**
     * Default颜色
     */
    @ApiModelProperty(value = "Default颜色")
    private String defaultColor;
    /**
     * 波段(编码)
     */
    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;
    /**
     * 目标成本
     */
    @ApiModelProperty(value = "目标成本")
    private BigDecimal productCost;
    /**
     * 主材料
     */
    @ApiModelProperty(value = "主材料")
    private String mainMaterials;
    /**
     * 研发材料
     */
    @ApiModelProperty(value = "研发材料")
    private String rdMat;
    /**
     * 廓形
     */
    @ApiModelProperty(value = "廓形")
    private String silhouette;
    /**
     * 打板难度
     */
    @ApiModelProperty(value = "打板难度")
    private String patDiff;
    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    private String productSizes;

    @ApiModelProperty(value = "设计师名称"  )
    private String designer;
    /** 设计师id */
    @ApiModelProperty(value = "设计师id"  )
    private String designerId;

    /**
     * 状态:0未开款，1已开款，2已下发打板
     */
    @ApiModelProperty(value = "状态:0未开款，1已开款，2已下发打板")
    private String status;

    @ApiModelProperty(value = "工艺信息")
    private TechnologyDto technology;

}
