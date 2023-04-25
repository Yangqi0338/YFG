/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fieldManagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：选项表 实体类
 * @address com.base.sbc.module.fieldManagement.entity.Option
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 18:33:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_option")
public class Option extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 字段管理id */
    @ApiModelProperty(value = "字段管理id"  )
    private String fieldId;
    /** 选项名 */
    @ApiModelProperty(value = "选项名"  )
    private String optionName;

    @ApiModelProperty(value = "选项文件地址"  )
    private String optionUrl;

    @ApiModelProperty(value = "选项数值"  )
    private Long optionNumber;

    /** 选项地址  日期区间，分开 */
    @ApiModelProperty(value = "选项地址  日期区间，分开"  )
    private String optionDate;
    /** 创建时间 */
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
