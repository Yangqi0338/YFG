/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：款式研发进度模板 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-2 17:47:24
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_research_process_template")
@ApiModel("款式研发进度模板 BasicsdatumResearchProcessTemplate")
public class BasicsdatumResearchProcessTemplate extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    private String templateName;
    /** 品牌code */
    @ApiModelProperty(value = "品牌code"  )
    private String brandCode;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brandName;
    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private String productType;
    @ApiModelProperty(value = "推送方式 0:代表正推 ， 1 代表倒推"  )
    private Integer type;
    @ApiModelProperty(value = " 0:启用 ， 1:停用"  )
    private Integer enableFlag;
    /** 生产类型名称 */
    @ApiModelProperty(value = "生产类型名称"  )
    private String productName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
