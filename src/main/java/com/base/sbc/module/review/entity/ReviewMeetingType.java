/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：评审会-会议类型表 实体类
 * @address com.base.sbc.module.review.entity.ReviewMeetingType
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 10:11:24
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_review_meeting_type")
@ApiModel("评审会-会议类型表 ReviewMeetingType")
public class ReviewMeetingType extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 禁用 0 启用 1 */
    @ApiModelProperty(value = "禁用 0 启用 1"  )
    private String status;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 名称 */
    @ApiModelProperty(value = "名称"  )
    private String name;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private Integer sort;
    /** 父类ID */
    @ApiModelProperty(value = "父类ID"  )
    private String parentId;
    /** 父类ID集合 */
    @ApiModelProperty(value = "父类ID集合"  )
    private String parentIds;
    /** 层级 */
    @ApiModelProperty(value = "层级"  )
    private String level;
    /** 是否子节点(0表示不是，1表示是） */
    @ApiModelProperty(value = "是否子节点(0表示不是，1表示是）"  )
    private String isLeaf;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
