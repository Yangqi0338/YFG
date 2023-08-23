/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料开发申请 实体类
 * @address com.base.sbc.module.fabric.entity.FabricDevApply
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-17 9:57:28
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_dev_apply")
@ApiModel("面料开发申请 FabricDevApply")
public class FabricDevApply extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 开发申请单号 */
    @ApiModelProperty(value = "开发申请单号"  )
    private String devApplyCode;
    /** 分配状态:1.待分配、2.进行中、3.已完成 */
    @ApiModelProperty(value = "分配状态:1.待分配、2.进行中、3.已完成"  )
    private String allocationStatus;
    /** 物料id */
    @ApiModelProperty(value = "物料id"  )
    private String materialId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
