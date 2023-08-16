/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.review.dto.ReviewMeetingStyleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
/**
 * 类描述：评审会 实体类
 * @address com.base.sbc.module.review.entity.ReviewMeeting
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-15 16:43:06
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_review_meeting")
@ApiModel("评审会 ReviewMeeting")
public class ReviewMeeting extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @TableField(exist = false)
    private String meetingTypeName;

    @TableField(exist = false)
    private List<String> dimensionList;

    /** 部门员工信息集合 */
    @TableField(exist = false)
    private List<ReviewMeetingDepartment> meetingDepartmentList;
    /** 引用信息集合 */
    @TableField(exist = false)
    private List<ReviewMeetingLog> quoteLogList;
    /** 会议信息集合 */
    @TableField(exist = false)
    private List<ReviewMeetingLog> meetingLogList;

    /** 设计款号+ 款式BOM 集合 */
    @TableField(exist = false)
    private List<ReviewMeetingStyleDTO> styleList;
	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 草稿(0),待审核(1),审核通过(2),驳回(-1) */
    @ApiModelProperty(value = "草稿(0),待审核(1),审核通过(2),驳回(-1)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 单据编号 */
    @ApiModelProperty(value = "单据编号"  )
    private String code;
    /** 图片url */
    @ApiModelProperty(value = "图片url"  )
    private String pictureUrl;
    /** 会议名称 */
    @ApiModelProperty(value = "会议名称"  )
    private String meetingName;
    /** 主讲人id */
    @ApiModelProperty(value = "主讲人id"  )
    private String speakerId;
    /** 主讲人名称 */
    @ApiModelProperty(value = "主讲人名称"  )
    private String speakerName;
    /** 父级会议类型 */
    @ApiModelProperty(value = "父级会议类型"  )
    private String parentMeetingType;
    /** 会议类型 */
    @ApiModelProperty(value = "会议类型"  )
    private String meetingType;
    /** 客户 */
    @ApiModelProperty(value = "客户"  )
    private String customer;
    /** 客户名称 */
    @ApiModelProperty(value = "客户名称"  )
    private String customerName;
    /** 会议时间 */
    @ApiModelProperty(value = "会议时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date meetingDate;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String styleNo;
    /** 设计款号id */
    @ApiModelProperty(value = "设计款号id"  )
    private String styleId;
    /** 制版单id */
    @ApiModelProperty(value = "制版单id"  )
    private String plateBillId;
    /** 制版号 */
    @ApiModelProperty(value = "制版号"  )
    private String plateBillCode;
    /** 会议地址 */
    @ApiModelProperty(value = "会议地址"  )
    private String meetingAddress;
    /** 会议编号 */
    @ApiModelProperty(value = "会议编号"  )
    private String meetingNo;
    /** 是否通过（0是 1否） */
    @ApiModelProperty(value = "是否通过（0是 1否）"  )
    private String isPass;
    /** 评审结果 */
    @ApiModelProperty(value = "评审结果"  )
    private String reviewResult;
    /** 通知人id */
    @ApiModelProperty(value = "通知人id"  )
    private String notifierId;
    /** 通知人名称 */
    @ApiModelProperty(value = "通知人名称"  )
    private String notifierName;
    /** 会议总结 */
    @ApiModelProperty(value = "会议总结"  )
    private String meetingSummarize;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

