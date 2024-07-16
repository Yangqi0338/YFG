/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料汇总-打印日志 实体类
 * @address com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-29 20:05:56
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_summary_print_log")
@ApiModel("面料汇总-打印日志 FabricSummaryPrintLog")
public class FabricSummaryPrintLog extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 面料汇总id */
    @ApiModelProperty(value = "面料汇总id"  )
    private String fabricSummaryId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
