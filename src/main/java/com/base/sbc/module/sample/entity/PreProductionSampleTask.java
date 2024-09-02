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
 * 类描述：产前样-任务 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.PreProductionSampleTask
 * @email your email
 * @date 创建时间：2023-10-11 10:12:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pre_production_sample_task")
@ApiModel("产前样-任务 PreProductionSampleTask")
public class PreProductionSampleTask extends BaseDataEntity<String> {

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
     * 款式主数据id:款式主数据id(t_style_master_data)
     */
    @ApiModelProperty(value = "款式主数据id:款式主数据id(t_style_master_data)")
    private String styleMasterDataId;
    /**
     * 资料包id
     */
    @ApiModelProperty(value = "资料包id")
    private String packInfoId;

    /**
     * 样衣图
     */
    @ApiModelProperty(value = "样衣图")
    private String samplePic;
    /**
     * 工艺师id
     */
    @ApiModelProperty(value = "工艺师id")
    private String technologistId;
    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
    private String technologistName;
    /**
     * 当前状态
     */
    @ApiModelProperty(value = "当前状态")
    private String status;
    /**
     * 放码师id
     */
    @ApiModelProperty(value = "放码师id")
    private String gradingId;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    private String gradingName;
    /**
     * 放码日期
     */
    @ApiModelProperty(value = "放码日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gradingDate;
    /**
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    private String cutterName;
    /**
     * 裁剪工id
     */
    @ApiModelProperty(value = "裁剪工id")
    private String cutterId;
    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    private String stitcher;
    /**
     * 车缝工id
     */
    @ApiModelProperty(value = "车缝工id")
    private String stitcherId;
    /**
     * 样衣质量评分
     */
    @ApiModelProperty(value = "样衣质量评分")
    private BigDecimal sampleQualityScore;
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
     * 打样顺序
     */
    @ApiModelProperty(value = "打样顺序")
    private String patSeq;
    /**
     * 需求数
     */
    @ApiModelProperty(value = "需求数")
    private BigDecimal requirementNum;
    /**
     * 当前节点
     */
    @ApiModelProperty(value = "当前节点")
    private String node;
    /**
     * 紧急程度
     */
    @ApiModelProperty(value = "紧急程度")
    private String urgency;
    /**
     * 是否齐套:0未齐套，1已齐套
     */
    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套")
    private String kitting;
    /**
     * 齐套时间
     */
    @ApiModelProperty(value = "齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date kittingTime;
    /**
     * 后技术备注说明
     */
    @ApiModelProperty(value = "后技术备注说明")
    private String techRemarks;
    /**
     * 正确样是否接收:(0未接收,1已接收)
     */
    @ApiModelProperty(value = "正确样是否接收:(0未接收,1已接收)")
    private String correctSampleReceivedFlag;
    /**
     * 样衣是否接收:(0未接收,1已接收)
     */
    @ApiModelProperty(value = "样衣是否接收:(0未接收,1已接收)")
    private String receiveSample;
    /**
     * 设计下明细单时间
     */
    @ApiModelProperty(value = "设计下明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailTime;
    /**
     * 计控接收明细单时间
     */
    @ApiModelProperty(value = "计控接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planningReceiveTime;
    /**
     * 工艺接收明细单时间
     */
    @ApiModelProperty(value = "工艺接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techReceiveTime;
    /**
     * 大货接收明细单时间
     */
    @ApiModelProperty(value = "大货接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionReceiveTime;
    /**
     * 品控接收明细单时间
     */
    @ApiModelProperty(value = "品控接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qcReceiveTime;
    /**
     * 面辅料信息
     */
    @ApiModelProperty(value = "面辅料信息")
    private String materialInfo;
    /**
     * 裁剪状态:0待接收,1已接收,2进行中,3完成
     */
    @ApiModelProperty(value = "裁剪状态:0待接收,1已接收,2进行中,3完成")
    private String cuttingStatus;
    /**
     * 车缝状态:0待接收,1已接收,2进行中,3完成
     */
    @ApiModelProperty(value = "车缝状态:0待接收,1已接收,2进行中,3完成")
    private String sewingStatus;
    /**
     * 样衣工作量评分
     */
    @ApiModelProperty(value = "样衣工作量评分")
    private BigDecimal sampleMakingScore;
    /**
     * 流程完成状态:(0未完成,1已完成)
     */
    @ApiModelProperty(value = "流程完成状态:(0未完成,1已完成)")
    private String finishFlag;
    /**
     * SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开
     */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开")
    private String scmSendFlag;
    /**
     * 工艺部接收正确样时间
     */
    @ApiModelProperty(value = "工艺部接收正确样时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techReceiveDate;
    /**
     * 查版日期
     */
    @ApiModelProperty(value = "查版日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sampleChaBanData;
    /**
     * 面料检测时间
     */
    @ApiModelProperty(value = "面料检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date materialCheckDate;
    /**
     * 技术下工艺部正确样
     */
    @ApiModelProperty(value = "技术下工艺部正确样")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processDepartmentDate;
    /**
     * 打样顺序名称
     */
    @ApiModelProperty(value = "打样顺序名称")
    private String patSeqName;
    /**
     * 紧急程度名称
     */
    @ApiModelProperty(value = "紧急程度名称")
    private String urgencyName;
    /**
     * 打版难度
     */
    @ApiModelProperty(value = "打版难度")
    private String patDiff;
    /**
     * 打版难度名称
     */
    @ApiModelProperty(value = "打版难度名称")
    private String patDiffName;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private BigDecimal sort;

    /**
     * 是否为旧数据
     */
    @ApiModelProperty(value = "是否为旧数据")
    private String historicalData;

    /** 创建部门 */
    @ApiModelProperty(value = "创建部门"  )
    private String createDeptId;

    /**
     * 评分详情id
     */
    @ApiModelProperty(value = "评分详情id")
    private String workloadRatingId;

    /**
     * 二次加工
     */
    @ApiModelProperty(value = "二次加工")
    private String secondProcessing;

    /**
     * 工作量基础分
     */
    @ApiModelProperty(value = "工作量基础分")
    private BigDecimal workloadRatingBase;

    /**
     * 工作量面料分
     */
    @ApiModelProperty(value = "工作量面料分")
    private BigDecimal workloadRatingRate;

    /**
     * 工作量附加分
     */
    @ApiModelProperty(value = "工作量附加分")
    private BigDecimal workloadRatingAppend;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

