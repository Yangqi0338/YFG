/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sampleFlow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：样衣流程 实体类
 * @address com.base.sbc.module.sampleFlow.entity.SampleFlow
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-9 10:26:37
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_flow")
public class SampleFlow extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 样衣Id */
    @ApiModelProperty(value = "样衣Id"  )
    private String sampleId;
    /** 流程名称 */
    @ApiModelProperty(value = "流程名称"  )
    private String flowName;
    /** 流程开始时间 */
    @ApiModelProperty(value = "流程开始时间"  )
    private Date flowStartDate;
    /** 流程结束时间 */
    @ApiModelProperty(value = "流程结束时间"  )
    private Date flowEndDate;
    /** 审核人id */
    @ApiModelProperty(value = "审核人id"  )
    private String auditUserId;
    /** 审核人名称 */
    @ApiModelProperty(value = "审核人名称"  )
    private String auditUserName;
    /** 流程状态(0未完成,1完成) */
    @ApiModelProperty(value = "流程状态(0未完成,1完成)"  )
    private String flowStatus;
    /** 节点顺序 */
    @ApiModelProperty(value = "节点顺序"  )
    private Integer flowOrder;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
