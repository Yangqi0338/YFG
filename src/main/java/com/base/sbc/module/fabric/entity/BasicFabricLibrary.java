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
 * 类描述：基础面料库 实体类
 * @address com.base.sbc.module.fabric.entity.BasicFabricLibrary
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-7 11:01:16
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basic_fabric_library")
@ApiModel("基础面料库 BasicFabricLibrary")
public class BasicFabricLibrary extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 开发名称 */
    @ApiModelProperty(value = "开发名称"  )
    private String devName;
    /** 开发编码 */
    @ApiModelProperty(value = "开发编码"  )
    private String devCode;
    /** 开发申请单号 */
    @ApiModelProperty(value = "开发申请单号"  )
    private String devApplyCode;
    /** 要求到料日期 */
    @ApiModelProperty(value = "要求到料日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requiredArrivalDate;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
