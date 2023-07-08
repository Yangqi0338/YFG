/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-品类测量组 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumCategoryMeasure
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 19:08:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_category_measure")
@ApiModel("基础资料-品类测量组 BasicsdatumCategoryMeasure")
public class BasicsdatumCategoryMeasure extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 品类测量组 */
    @ApiModelProperty(value = "品类测量组"  )
    private String name;
    /** 档差名 */
    @ApiModelProperty(value = "档差名"  )
    private String rangeDifferenceName;
    /** 档差id */
    @ApiModelProperty(value = "档差id"  )
    private String rangeDifferenceId;
    /** 测量点 */
    @ApiModelProperty(value = "测量点"  )
    private String measurement;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

