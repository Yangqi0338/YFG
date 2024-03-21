/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：吊牌检测报告关系表 实体类
 * @address com.base.sbc.module.hangtag.entity.HangTagInspectCompany
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-22 16:43:18
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_hang_tag_inspect_company")
@ApiModel("吊牌检测报告关系表 HangTagInspectCompany")
public class HangTagInspectCompany extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 检测报告id */
    @ApiModelProperty(value = "检测报告id"  )
    private String inspectCompanyId;
    /** 吊牌id */
    @ApiModelProperty(value = "吊牌id"  )
    private String hangTagId;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
