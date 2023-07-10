/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-品类关系表 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumCompanyRelation
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-10 9:24:27
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_company_relation")
@ApiModel("基础资料-品类关系表 BasicsdatumCompanyRelation")
public class BasicsdatumCompanyRelation extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品类id */
    @ApiModelProperty(value = "品类id"  )
    private String categoryId;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String categoryName;
    /** 数据id */
    @ApiModelProperty(value = "数据id"  )
    private String dataId;
    /** 类型(difference:档差) */
    @ApiModelProperty(value = "类型(difference:档差)"  )
    private String type;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
