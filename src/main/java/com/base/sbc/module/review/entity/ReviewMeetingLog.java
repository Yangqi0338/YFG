/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.entity;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：评审会-会议记录 实体类
 * @address com.base.sbc.module.review.entity.ReviewMeetingLog
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 17:06:39
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_review_meeting_log")
@ApiModel("评审会-会议记录 ReviewMeetingLog")
@JsonIgnoreProperties(value = {"handler"})
public class ReviewMeetingLog extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /** 维度信息 */
    @TableField(exist = false)
    private List<ReviewMeetingLogDetail> meetingLogDetailList;
    /** 附件信息 */
    @TableField(exist = false)
    private ReviewMeetingLogFile reviewMeetingLogFile;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 评审会议id */
    @ApiModelProperty(value = "评审会议id"  )
    private String meetingId;
    /** 评审维度id */
    @ApiModelProperty(value = "评审维度id"  )
    private String reviewDimensionId;
    /** 评审维度 */
    @ApiModelProperty(value = "评审维度"  )
    private String reviewDimension;
    /** 是否通过（0是 1否） */
    @ApiModelProperty(value = "是否通过（0是 1否）"  )
    private String isPass;
    /** 会议纪要 */
    @ApiModelProperty(value = "会议纪要"  )
    private String meetingMinutes;
    /** 跟进负责人id */
    @ApiModelProperty(value = "跟进负责人id"  )
    private String personChargeId;
    /** 跟进负责人 */
    @ApiModelProperty(value = "跟进负责人"  )
    private String personCharge;
    /** 跟进纪要 */
    @ApiModelProperty(value = "跟进纪要"  )
    private String followUpMinutes;
    /** 类型 0会议 1引用 */
    @ApiModelProperty(value = "类型 0会议 1引用"  )
    private String type;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
