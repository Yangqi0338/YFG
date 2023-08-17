/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：评审会-员工参与记录 实体类
 * @address com.base.sbc.module.review.entity.ReviewMeetingPeople
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-17 14:53:06
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_review_meeting_people")
@ApiModel("评审会-员工参与记录 ReviewMeetingPeople")
public class ReviewMeetingPeople extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 评审会id */
    @ApiModelProperty(value = "评审会id"  )
    private String meetingId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
