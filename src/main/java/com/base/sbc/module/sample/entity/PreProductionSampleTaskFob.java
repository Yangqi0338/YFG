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

import java.util.Date;

/**
 * 类描述：产前样-任务 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.PreProductionSampleTaskFob
 * @email your email
 * @date 创建时间：2024-7-10 11:25:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pre_production_sample_task_fob")
@ApiModel("产前样-任务 PreProductionSampleTaskFob")
public class PreProductionSampleTaskFob extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 打版编码
     */
    @ApiModelProperty(value = "打版编码")
    private String code;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 款式id
     */
    @ApiModelProperty(value = "款式id")
    private String styleId;
    /**
     * 当前状态
     */
    @ApiModelProperty(value = "当前状态")
    private String status;
    /**
     * 样衣条码
     */
    @ApiModelProperty(value = "样衣条码")
    private String sampleBarCode;
    /**
     * 样衣是否完成:(0否，1是)
     */
    @ApiModelProperty(value = "样衣是否完成:(0否，1是)")
    private String sampleCompleteFlag;
    /**
     * 工艺单完成日期
     */
    @ApiModelProperty(value = "工艺单完成日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processCompletionDate;
    /**
     * 启用状态:1启用,0未启用
     */
    @ApiModelProperty(value = "启用状态:1启用,0未启用")
    private String enableFlag;
    /**
     * 是否是正确产前样：0否,1是
     */
    @ApiModelProperty(value = "是否是正确产前样：0否,1是")
    private String correctSampleFlag;
    /**
     * 版房
     */
    @ApiModelProperty(value = "版房")
    private String patternRoom;
    /**
     * 版房id
     */
    @ApiModelProperty(value = "版房id")
    private String patternRoomId;

    /**
     * 当前节点
     */
    @ApiModelProperty(value = "当前节点")
    private String node;

    /**
     * 后技术备注说明
     */
    @ApiModelProperty(value = "后技术备注说明")
    private String techRemarks;

    /**
     * 设计下明细单时间
     */
    @ApiModelProperty(value = "设计下明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailTime;

    /**
     * 工艺接收明细单时间
     */
    @ApiModelProperty(value = "工艺接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techReceiveTime;
    /**
     * 技术下工艺部正确样
     */
    @ApiModelProperty(value = "技术下工艺部正确样")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processDepartmentDate;
    /**
     * 是否是迁移历史数据
     */
    @ApiModelProperty(value = "是否是迁移历史数据")
    private String historicalData;
    /**
     * 创建部门
     */
    @ApiModelProperty(value = "创建部门")
    private String createDeptId;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号")
    private String orderNo;
    /**
     * 工单状态
     */
    @ApiModelProperty(value = "工单状态")
    private String orderStatus;
    /**
     * 下单类型
     */
    @ApiModelProperty(value = "下单类型")
    private String devtTypeName;
    /**
     * 加工类型
     */
    @ApiModelProperty(value = "加工类型")
    private String processTypeName;
    /**
     * 年份季节品牌
     */
    @ApiModelProperty(value = "年份季节品牌")
    private String outPlanningSeasonName;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String outBrand;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operatorName;
    /**
     * 工单号版次
     */
    @ApiModelProperty(value = "工单号版次")
    private String orderNoBatch;
    /**
     * 样衣卡附件
     */
    @ApiModelProperty(value = "样衣卡附件")
    private String sampleFile;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String yearName;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String seasonName;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brandName;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

