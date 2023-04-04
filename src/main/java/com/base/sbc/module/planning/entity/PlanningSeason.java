/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
/**
 * 类描述：企划-产品季 实体类
 * @address com.base.sbc.module.planning.entity.PlanningSeason
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-1 11:13:38
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanningSeason extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 企划名称 */
    @ApiModelProperty(value = "企划名称"  )
    private String name;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 月份 */
    @ApiModelProperty(value = "月份"  )
    private String month;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*******************************************getset方法区************************************/

}

