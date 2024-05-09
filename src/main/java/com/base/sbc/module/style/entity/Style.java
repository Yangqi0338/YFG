/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：款式设计 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.entity.Style
 * @email your email
 * @date 创建时间：2023-10-25 19:08:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style")
@ApiModel("款式设计 Style")
public class Style extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    public SmpSampleDto toSmpSampleDto() {
        SmpSampleDto smpSampleDto = new SmpSampleDto();


        smpSampleDto.setSampleReceivedDate(getCreateDate());


        try {
            smpSampleDto.setMajorCategoriesName(this.prodCategory1stName);
            smpSampleDto.setMajorCategories(this.prodCategory1st);
            smpSampleDto.setCategoryName(this.prodCategoryName);
            smpSampleDto.setCategory(this.prodCategory);
            smpSampleDto.setMiddleClassName(this.prodCategory2ndName);
            smpSampleDto.setMiddleClassId(this.prodCategory2nd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        smpSampleDto.setQuarterCode(season);
        smpSampleDto.setPatternMaker(patternDesignName);
        smpSampleDto.setPatternMakerId(patternDesignId);
        smpSampleDto.setStyleCode(designNo);
        return smpSampleDto;

    }

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 启用状态(0启用，1停用)
     */
    @ApiModelProperty(value = "启用状态(0启用，1停用)")
    private String enableStatus;
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
     * 旧产品季节id
     */
    @ApiModelProperty(value = "旧产品季节id")
    private String  oldPlanningSeasonId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;
    /**
     * 企划渠道id
     */
    @ApiModelProperty(value = "企划渠道id")
    private String planningChannelId;
    /** 大类code */
    @ApiModelProperty(value = "大类code"  )
    private String prodCategory1st;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 品类code */
    @ApiModelProperty(value = "品类code"  )
    private String prodCategory;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2nd;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 小类code */
    @ApiModelProperty(value = "小类code"  )
    private String prodCategory3rd;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String prodCategory3rdName;
    /** 设计品类code */
    @ApiModelProperty(value = "设计品类code"  )
    private String designCategoryCode;
    /** 设计品类名称 */
    @ApiModelProperty(value = "设计品类名称"  )
    private String designCategoryName;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String styleNo;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 关联历史款号 */
    @ApiModelProperty(value = "关联历史款号"  )
    private String hisDesignNo;
    /** 旧设计款号 */
    @ApiModelProperty(value = "旧设计款号"  )
    private String oldDesignNo;
    /** 款式名称 */
    @ApiModelProperty(value = "款式名称"  )
    private String styleName;
    /** 款式类型 */
    @ApiModelProperty(value = "款式类型"  )
    private String styleType;
    /** 款式性别类型 */
    @ApiModelProperty(value = "产品季性别类型"  )
    private String genderType;
    /** 款式性别名称 */
    @ApiModelProperty(value = "产品季性别名称"  )
    private String genderName;
    /** 款式类型名称 */
    @ApiModelProperty(value = "款式类型名称")
    private String styleTypeName;
    /** 款式图id(主图) */
    @ApiModelProperty(value = "款式图id(主图)"  )
    private String stylePic;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份名称 */
    @ApiModelProperty(value = "年份名称"  )
    private String yearName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 月份 */
    @ApiModelProperty(value = "月份"  )
    private String month;
    /** 月份名称 */
    @ApiModelProperty(value = "月份名称"  )
    private String monthName;
    /** 性别 */
    @ApiModelProperty(value = "性别"  )
    private String sex;
    /** 性别名称 */
    @ApiModelProperty(value = "性别名称"  )
    private String sexName;
    /** 波段(编码) */
    @ApiModelProperty(value = "波段(编码)"  )
    private String bandCode;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /** 生产模式 */
    @ApiModelProperty(value = "生产模式"  )
    private String devtType;
    /** 生产模式名称 */
    @ApiModelProperty(value = "生产模式名称"  )
    private String devtTypeName;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
    /** 主题 */
    @ApiModelProperty(value = "主题"  )
    private String subject;
    /** 版型 */
    @ApiModelProperty(value = "版型"  )
    private String plateType;
    /** 版型名称 */
    @ApiModelProperty(value = "版型名称"  )
    private String plateTypeName;
    /** 版型 */
    @ApiModelProperty(value = "版型"  )
    private String platePositioning;
    /** 版型名称 */
    @ApiModelProperty(value = "版型名称"  )
    private String platePositioningName;
    /** 号型编码 */
    @ApiModelProperty(value = "号型编码"  )
    private String sizeRange;
    /** 号型名称 */
    @ApiModelProperty(value = "号型名称"  )
    private String sizeRangeName;
    /** 开发分类 */
    @ApiModelProperty(value = "开发分类"  )
    private String devClass;
    /** 开发分类名称 */
    @ApiModelProperty(value = "开发分类名称"  )
    private String devClassName;
    /** Default尺码 */
    @ApiModelProperty(value = "Default尺码"  )
    private String defaultSize;
    /** Default颜色 */
    @ApiModelProperty(value = "Default颜色"  )
    private String defaultColor;
    /** Default颜色编码 */
    @ApiModelProperty(value = "Default颜色编码"  )
    private String defaultColorCode;
    /** 目标成本 */
    @ApiModelProperty(value = "目标成本"  )
    private BigDecimal productCost;
    /** 主材料 */
    @ApiModelProperty(value = "主材料"  )
    private String mainMaterials;
    /** 研发材料 */
    @ApiModelProperty(value = "研发材料"  )
    private String rdMat;
    /** 廓形 */
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
     * 产品线等级
     */
    @ApiModelProperty(value = "产品线等级")
    private String productLineGrade;
    /**
     * 产品线等级名称
     */
    @ApiModelProperty(value = "产品线等级名称")
    private String productLineGradeName;
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
    /** 尺码codes */
    @ApiModelProperty(value = "尺码codes"  )
    private String sizeCodes;
    /** 尺码真实codes */
    @ApiModelProperty(value = "尺码真实codes"  )
    private String sizeRealCodes;
    /** 设计师名称 */
    @ApiModelProperty(value = "设计师名称"  )
    private String designer;
    /** 设计师id */
    @ApiModelProperty(value = "设计师id"  )
    private String designerId;
    /** 价格带 */
    @ApiModelProperty(value = "价格带"  )
    private String price;
    /** 款式风格 */
    @ApiModelProperty(value = "款式风格"  )
    private String styleFlavour;
    /** 款式风格名称 */
    @ApiModelProperty(value = "款式风格名称"  )
    private String styleFlavourName;
    /** 款式定位 */
    @ApiModelProperty(value = "款式定位"  )
    private String positioning;
    /** 款式定位名称 */
    @ApiModelProperty(value = "款式定位名称"  )
    private String positioningName;
    /** 关联的素材库数量 */
    @ApiModelProperty(value = "关联的素材库数量"  )
    private BigDecimal materialCount;
    /** 任务等级 */
    @ApiModelProperty(value = "任务等级"  )
    private String taskLevel;
    /** 任务等级名称 */
    @ApiModelProperty(value = "任务等级名称"  )
    private String taskLevelName;
    /** 开始时间 */
    @ApiModelProperty(value = "开始时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /** 截止时间 */
    @ApiModelProperty(value = "截止时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /** 设计工艺员id */
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
     * 模板部件图片
     */
    @ApiModelProperty(value = "模板部件图片")
    private String patternPartsPic;
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
    @ApiModelProperty(value = "审版设计师id"  )
    private String reviewedDesignId;
    /** 审版设计师 */
    @ApiModelProperty(value = "审版设计师"  )
    private String reviewedDesignName;
    /** 改版设计师id */
    @ApiModelProperty(value = "改版设计师id"  )
    private String revisedDesignId;
    /** 改版设计师 */
    @ApiModelProperty(value = "改版设计师"  )
    private String revisedDesignName;
    /** 应季节 */
    @ApiModelProperty(value = "应季节"  )
    private String seasonal;
    /** 延续点 */
    @ApiModelProperty(value = "延续点"  )
    private String continuationPoint;
    /** 款式单位名称 */
    @ApiModelProperty(value = "款式单位名称"  )
    private String styleUnit;
    /** 款式单位编码 */
    @ApiModelProperty(value = "款式单位编码"  )
    private String styleUnitCode;
    /** SCM下发状态:0未下发,1已下发 */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发"  )
    private String scmSendFlag;
    /** 计划完成时间 */
    @ApiModelProperty(value = "计划完成时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planningFinishDate;
    /** 样衣需求完成时间 */
    @ApiModelProperty(value = "样衣需求完成时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date demandFinishDate;
    /** 特别需求:(1是,0否) */
    @ApiModelProperty(value = "特别需求:(1是,0否)")
    private String specialNeedsFlag;
    /**
     * 预计上市时间
     */
    @ApiModelProperty(value = "预计上市时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectedLaunchDate;
    /**
     * 目标销价
     */
    @ApiModelProperty(value = "目标销价")
    private BigDecimal targetSalePrice;
    /**
     * 企划目标倍率
     */
    @ApiModelProperty(value = "企划目标倍率")
    private BigDecimal planningTargetRate;
    /**
     * 样衣到仓时间
     */
    @ApiModelProperty(value = "样衣到仓时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sampleDeliveryDate;
    /**
     * 审核状态：草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "审核状态：草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)")
    private String confirmStatus;
    /**
     * 品类标识 0品类 1中类
     */
    @ApiModelProperty(value = "品类标识 0品类 1中类")
    private String categoryFlag;
    /** 状态:1启用,0未启用 */
    @ApiModelProperty(value = "状态:1启用,0未启用"  )
    private String enableFlag;
    /** 款式来源编码 */
    @ApiModelProperty(value = "款式来源编码")
    private String styleOrigin;
    /**
     * 款式来源名称
     */
    @ApiModelProperty(value = "款式来源名称")
    private String styleOriginName;
    /**
     * 颜色codes
     */
    @ApiModelProperty(value = "颜色codes")
    private String colorCodes;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String productColors;
    /**
     * 卖点
     */
    @ApiModelProperty(value = "卖点")
    private String sellingPoint;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 是否授权款(1是，0否)
     */
    @ApiModelProperty(value = "是否授权款(1是，0否)")
    private String isAccredit;
    /**
     * 针梭织id
     */
    @ApiModelProperty(value = "针梭织id")
    private String needleWeavingId;
    /**
     * 针梭织
     */
    @ApiModelProperty(value = "针梭织")
    private String needleWeaving;
    /**
     * 系列id
     */
    @ApiModelProperty(value = "系列id")
    private String seriesId;
    /**
     * 系列
     */
    @ApiModelProperty(value = "系列")
    private String series;
    /**
     * 设计渠道
     */
    @ApiModelProperty(value = "设计渠道")
    private String designChannelId;
    /**
     * 设计渠道
     */
    @ApiModelProperty(value = "设计渠道")
    private String designChannel;
    /**
     * 是否已经生成设计款号(1是，0否)
     */
    @ApiModelProperty(value = "是否已经生成设计款号(1是，0否)")
    private String isGenDesignNo;
    /**
     * 审核开始时间
     */
    @ApiModelProperty(value = "审核开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkStartTime;
    /**
     * 审核结束时间
     */
    @ApiModelProperty(value = "审核结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkEndTime;
    /** 是否是迁移历史数据 */
    @ApiModelProperty(value = "是否是迁移历史数据"  )
    private String historicalData;
    /** 下发打版时间(取最新一条) */
    @ApiModelProperty(value = "下发打版时间(取最新一条)"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendPatternMakingDate;

    /**
     * 是否撞色
     */
    @ApiModelProperty(value = "是否撞色,0否 1是")
    private String colorCrash;

    /**
     * 套版款号
     */
    @ApiModelProperty(value = "套版款号"  )
    private String registeringNo;

    /**
     * RFID标准
     */
    @ApiModelProperty(value = "RFID标准"  )
    private String rfidFlag;

    /**
     * 设计阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "设计阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)"  )
    private String designAuditStatus;
    /**
     * 设计阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)
     */
    @ApiModelProperty(value = "设计阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)"  )
    private String designMarkingStatus;

    /**
     * 销售类型
     */
    @ApiModelProperty(value = "销售类型"  )
    private String salesType;
    /**
     * 销售类型名称
     */
    @ApiModelProperty(value = "销售类型名称"  )
    private String salesTypeName;

    /** 发送部门 */
    @ApiModelProperty(value = "发送部门"  )
    private String sendDeptId;
    /** 接收部门 */
    @ApiModelProperty(value = "接收部门"  )
    private String receiveDeptId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

