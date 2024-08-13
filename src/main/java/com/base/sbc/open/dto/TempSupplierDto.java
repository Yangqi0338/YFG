/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("SCM传输临时供应商")
public class TempSupplierDto {

    private static final long serialVersionUID = 1L;
    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String supplier;

    /**
     * 供应商类型
     */
    @ApiModelProperty(value = "供应商类型")
    private String supplierType;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contact;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String cellphone;

    /**
     * 二级分类编码
     */
    @ApiModelProperty(value = "二级分类编码")
    private String secondClass;

    /**
     * 二级分类名称
     */
    @ApiModelProperty(value = "二级分类名称")
    private String secondClassName;

}
