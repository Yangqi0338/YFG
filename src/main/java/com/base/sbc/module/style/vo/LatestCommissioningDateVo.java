/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * 类描述：下单最晚投产日期管理Vo 实体类
 * @address com.base.sbc.module.style.vo.LatestCommissioningDateVo
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-11 19:05:30
 * @version 1.0
 */
@Data
@ApiModel("下单最晚投产日期管理 LatestCommissioningDateVo")
public class LatestCommissioningDateVo extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称,多选 */
    @ApiModelProperty(value = "品牌名称,多选"  )
    private String brandName;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份名称 */
    @ApiModelProperty(value = "年份名称"  )
    private String yearName;
    /** 波段(编码) */
    @ApiModelProperty(value = "波段(编码)"  )
    private String bandCode;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /** 最晚投产日期 */
    @ApiModelProperty(value = "最晚投产日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date latestCommissioningDate;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
