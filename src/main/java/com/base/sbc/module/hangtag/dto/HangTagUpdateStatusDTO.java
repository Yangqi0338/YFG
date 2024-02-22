/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.dto;

import com.base.sbc.config.enums.business.HangTagStatusCheckEnum;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：吊牌状态修改
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@Data
@ApiModel(value = "吊牌状态修改")
public class HangTagUpdateStatusDTO {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "吊牌id不可为空")
    @ApiModelProperty(value = "吊牌id")
    private List<String> ids;

    @NotNull(message = "状态不可为空")
    @ApiModelProperty(value = "状态")
    private HangTagStatusEnum status;


    private HangTagStatusCheckEnum checkType;

    /** 国家编码 */
    @ApiModelProperty(value = "国家编码"  )
    private String countryCode;

    private String userCompany;

}

