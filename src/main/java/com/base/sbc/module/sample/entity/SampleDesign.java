/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：样衣设计 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleDesign
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-28 10:30:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_design")
@ApiModel("样衣设计 SampleDesign")
public class SampleDesign extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 状态:0未开款，1已开款，2已下发打板(完成)
     */
    @ApiModelProperty(value = "状态:0未开款，1已开款，2已下发打板(完成)")
    private String status;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;
    /**
     * 大类code
     */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类code
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 小类code
     */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 关联历史款号
     */
    @ApiModelProperty(value = "关联历史款号")
    private String hisDesignNo;
    /**
     * 旧设计款号
     */
    @ApiModelProperty(value = "旧设计款号")
    private String oldDesignNo;
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
     * 款式类型名称
     */
    @ApiModelProperty(value = "款式类型名称")
    private String styleTypeName;
    /**
     * 款式图id(主图)
     */
    @ApiModelProperty(value = "款式图id(主图)")
    private String stylePic;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 年份名称
     */
    @ApiModelProperty(value = "年份名称")
    private String yearName;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节名称
     */
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;
    /**
     * 月份名称
     */
    @ApiModelProperty(value = "月份名称")
    private String monthName;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;
    /**
     * 性别名称
     */
    @ApiModelProperty(value = "性别名称")
    private String sexName;
    /**
     * 波段(编码)
     */
    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;
    /**
     * 波段名称
     */
    @ApiModelProperty(value = "波段名称")
    private String bandName;
    /**
     * 生产模式
     */
    @ApiModelProperty(value = "生产模式")
    private String devtType;
    /**
     * 生产模式名称
     */
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;
    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channel;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
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
     * 版型名称
     */
    @ApiModelProperty(value = "版型名称")
    private String plateTypeName;
    /**
     * 号型编码
     */
    @ApiModelProperty(value = "号型编码")
    private String sizeRange;
    /**
     * 号型名称
     */
    @ApiModelProperty(value = "号型名称")
    private String sizeRangeName;
    /**
     * 开发分类
     */
    @ApiModelProperty(value = "开发分类")
    private String devClass;
    /**
     * 开发分类名称
     */
    @ApiModelProperty(value = "开发分类名称")
    private String devClassName;
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
     * Default颜色编码
     */
    @ApiModelProperty(value = "Default颜色编码")
    private String defaultColorCode;
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
     * 廓形名称
     */
    @ApiModelProperty(value = "廓形名称")
    private String silhouetteName;
    /**
     * 打板难度
     */
    @ApiModelProperty(value = "打板难度")
    private String patDiff;
    /**
     * 打版难度名称
     */
    @ApiModelProperty(value = "打版难度名称")
    private String patDiffName;
    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    private String productSizes;
    /**
     * 尺码ids
     */
    @ApiModelProperty(value = "尺码ids")
    private String sizeIds;
    /**
     * 尺码codes
     */
    @ApiModelProperty(value = "尺码codes")
    private String sizeCodes;
    /**
     * 设计师名称
     */
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;
    /**
     * 价格带
     */
    @ApiModelProperty(value = "价格带")
    private String price;
    /**
     * 款式风格
     */
    @ApiModelProperty(value = "款式风格")
    private String styleFlavour;
    /**
     * 款式风格名称
     */
    @ApiModelProperty(value = "款式风格名称")
    private String styleFlavourName;
    /**
     * 款式定位
     */
    @ApiModelProperty(value = "款式定位")
    private String positioning;
    /**
     * 款式定位名称
     */
    @ApiModelProperty(value = "款式定位名称")
    private String positioningName;
    /**
     * 关联的素材库数量
     */
    @ApiModelProperty(value = "关联的素材库数量")
    private BigDecimal materialCount;
    /**
     * 任务等级
     */
    @ApiModelProperty(value = "任务等级")
    private String taskLevel;
    /**
     * 任务等级名称
     */
    @ApiModelProperty(value = "任务等级名称")
    private String taskLevelName;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 设计工艺员id
     */
    @ApiModelProperty(value = "设计工艺员id")
    private String technicianId;
    /**
     * 设计工艺员名称
     */
    @ApiModelProperty(value = "设计工艺员名称")
    private String technicianName;
    /**
     * 材料专员id
     */
    @ApiModelProperty(value = "材料专员id")
    private String fabDevelopeId;
    /**
     * 材料专员名称
     */
    @ApiModelProperty(value = "材料专员名称")
    private String fabDevelopeName;
    /**
     * 实际出稿时间
     */
    @ApiModelProperty(value = "实际出稿时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualPublicationDate;
    /**
     * 下发人
     */
    @ApiModelProperty(value = "下发人")
    private String sender;
    /**
     * 是否齐套:0未齐套，1已齐套
     */
    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套")
    private String kitting;
    /**
     * 模板部件
     */
    @ApiModelProperty(value = "模板部件")
    private String patternParts;
    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    /**
     * 版师名称
     */
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    /**
     * 跟款设计师Id
     */
    @ApiModelProperty(value = "跟款设计师Id")
    private String merchDesignId;
    /**
     * 跟款设计师
     */
    @ApiModelProperty(value = "跟款设计师")
    private String merchDesignName;
    /**
     * 审版设计师id
     */
    @ApiModelProperty(value = "审版设计师id")
    private String reviewedDesignId;
    /**
     * 审版设计师
     */
    @ApiModelProperty(value = "审版设计师")
    private String reviewedDesignName;
    /**
     * 改版设计师id
     */
    @ApiModelProperty(value = "改版设计师id")
    private String revisedDesignId;
    /**
     * 改版设计师
     */
    @ApiModelProperty(value = "改版设计师")
    private String revisedDesignName;
    /**
     * 应季节
     */
    @ApiModelProperty(value = "应季节")
    private String seasonal;
    /**
     * 延续点
     */
    @ApiModelProperty(value = "延续点")
    private String continuationPoint;
    /**
     * 款式单位编码
     */
    @ApiModelProperty(value = "款式单位编码")
    private String styleUnit;
    /**
     * 款式单位名称
     */
    @ApiModelProperty(value = "款式单位名称")
    private String styleUnitCode;
    /**
     * SCM下发状态:0未下发,1已下发
     */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;
    /**
     * 计划完成时间
     */
    @ApiModelProperty(value = "计划完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planningFinishDate;
    /**
     * 特别需求:(1是,0否)
     */
    @ApiModelProperty(value = "特别需求:(1是,0否)")
    private String specialNeedsFlag;
    /**
     * 审核状态：草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "审核状态：草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)")
    private String confirmStatus;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

