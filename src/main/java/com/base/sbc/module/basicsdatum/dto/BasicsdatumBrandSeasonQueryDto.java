/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.base.sbc.config.common.base.Page;
/**
 * 类描述：品牌-季度表QueryDto 实体类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumBrandSeasonQueryDto
 * @author 谭博文
 * @email your email
 * @date 创建时间：2024-4-9 9:42:49
 * @version 1.0
 */
@Data
public class BasicsdatumBrandSeasonQueryDto extends Page {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 月份 */
    @ApiModelProperty(value = "月份"  )
    private String month;
    /** 月份名称 */
    @ApiModelProperty(value = "月份名称"  )
    private String monthName;
    /** 启用状态：0 启用； 1 未启用  */
    @ApiModelProperty(value = "启用状态：0 启用； 1 未启用 "  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
