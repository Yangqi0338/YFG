/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.entity;

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
 * 类描述：打版管理 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.entity.PatternMaking
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-16 17:52:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_making")
@ApiModel("打版管理 PatternMaking")
public class PatternMaking extends BaseDataEntity<String> {

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
     * 样板号
     */
    @ApiModelProperty(value = "样板号")
    private String patternNo;
    /**
     * 当前状态
     */
    @ApiModelProperty(value = "当前状态")
    private String status;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 款式设计id
     */
    @ApiModelProperty(value = "款式设计id")
    private String styleId;
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
     * 打版类型
     */
    @ApiModelProperty(value = "打版类型")
    private String sampleType;
    /**
     * 打版类型名称
     */
    @ApiModelProperty(value = "打版类型名称")
    private String sampleTypeName;
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
     * 打样顺序
     */
    @ApiModelProperty(value = "打样顺序")
    private String patSeq;
    /**
     * 打样顺序名称
     */
    @ApiModelProperty(value = "打样顺序名称")
    private String patSeqName;
    /**
     * 紧急程度
     */
    @ApiModelProperty(value = "紧急程度")
    private String urgency;
    /**
     * 紧急程度名称
     */
    @ApiModelProperty(value = "紧急程度名称")
    private String urgencyName;
    /**
     * 工艺员确认齐套
     */
    @ApiModelProperty(value = "工艺员确认齐套")
    private String technicianKitting;
    /**
     * 工艺员确认齐套时间
     */
    @ApiModelProperty(value = "工艺员确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technicianKittingDate;
    /**
     * 样衣组长确认齐套
     */
    @ApiModelProperty(value = "样衣组长确认齐套")
    private String sglKitting;
    /**
     * 样衣组长确认齐套时间
     */
    @ApiModelProperty(value = "样衣组长确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sglKittingDate;
    /**
     * 纸样需求完成日期
     */
    @ApiModelProperty(value = "纸样需求完成日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date patternReqDate;
    /**
     * 缺料备注
     */
    @ApiModelProperty(value = "缺料备注")
    private String shortageRemarks;
    /**
     * 需求数
     */
    @ApiModelProperty(value = "需求数")
    private BigDecimal requirementNum;
    /**
     * 改版原因
     */
    @ApiModelProperty(value = "改版原因")
    private String revisionReason;
    /**
     * 改版意见
     */
    @ApiModelProperty(value = "改版意见")
    private String revisionComments;
    /**
     * 版师名称
     */
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    private String size;
    /**
     * 中断样衣(0正常，1中断)
     */
    @ApiModelProperty(value = "中断样衣(0正常，1中断)")
    private String breakOffSample;
    /**
     * 中断打版(0正常，1中断)
     */
    @ApiModelProperty(value = "中断打版(0正常，1中断)")
    private String breakOffPattern;
    /**
     * 版房主管下发状态:(0未下发，1已下发)
     */
    @ApiModelProperty(value = "版房主管下发状态:(0未下发，1已下发)")
    private String prmSendStatus;
    /**
     * 版房主管下发时间
     */
    @ApiModelProperty(value = "版房主管下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date prmSendDate;
    /**
     * 设计下发时间
     */
    @ApiModelProperty(value = "设计下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designSendDate;
    /**
     * 设计下发状态:(0未下发，1已下发)
     */
    @ApiModelProperty(value = "设计下发状态:(0未下发，1已下发)")
    private String designSendStatus;
    /**
     * 纸样完成数量
     */
    @ApiModelProperty(value = "纸样完成数量")
    private BigDecimal patternFinishNum;
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
     * 裁剪完成数量
     */
    @ApiModelProperty(value = "裁剪完成数量")
    private BigDecimal cutterFinishNum;
    /**
     * 样衣制作评分
     */
    @ApiModelProperty(value = "样衣制作评分")
    private BigDecimal sampleMakingScore;
    /**
     * 打版质量评分
     */
    @ApiModelProperty(value = "打版质量评分")
    private BigDecimal patternMakingScore;
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
     * 二次加工(0否，1是)
     */
    @ApiModelProperty(value = "二次加工(0否，1是)")
    private String secondProcessing;
    /**
     * 当前节点
     */
    @ApiModelProperty(value = "当前节点")
    private String node;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private BigDecimal sort;
    /**
     * 挂起(1挂起)
     */
    @ApiModelProperty(value = "挂起(1挂起)")
    private String suspend;
    /**
     * 挂起状态(“打胚样”“打净样”“做纸样”)
     */
    @ApiModelProperty(value = "挂起状态(“打胚样”“打净样”“做纸样”)")
    private String suspendStatus;
    /**
     * 挂起备注
     */
    @ApiModelProperty(value = "挂起备注")
    private String suspendRemarks;
    /**
     * 确认收到样衣时间
     */
    @ApiModelProperty(value = "确认收到样衣时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveSampleDate;
    /**
     * 确认收到样衣(0否,1是)
     */
    @ApiModelProperty(value = "确认收到样衣(0否,1是)")
    private String receiveSample;
    /**
     * 是否需要外辅(0否,1是)
     */
    @ApiModelProperty(value = "是否需要外辅(0否,1是)")
    private String extAuxiliary;
    /**
     * 外辅发出时间
     */
    @ApiModelProperty(value = "外辅发出时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extAuxiliaryDispatchDate;
    /**
     * 外辅接收时间
     */
    @ApiModelProperty(value = "外辅接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extAuxiliaryReceiveDate;
    /**
     * 样衣结束标识:(0:未结束,1结束)
     */
    @ApiModelProperty(value = "样衣结束标识:(0:未结束,1结束)")
    private String endFlg;
    /**
     * 样衣编号
     */
    @ApiModelProperty(value = "样衣编号")
    private String sampleNo;
    /**
     * SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开
     */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开")
    private String scmSendFlag;
    /**
     * 样衣完成状态:(0未完成,1完成)
     */
    @ApiModelProperty(value = "样衣完成状态:(0未完成,1完成)")
    private String sampleCompleteFlag;
    /**
     * 打版状态:0待接收,1已接收,2进行中,3完成
     */
    @ApiModelProperty(value = "打版状态:0待接收,1已接收,2进行中,3完成")
    private String patternStatus;
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
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

