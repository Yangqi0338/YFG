/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 面料开发申请分配
 */
@Data
@ApiModel("面料开发申请分配")
public class FabricDevApplyAllocationDTO {
    @NotBlank(message = "开发申请id不可为空")
    @ApiModelProperty("开发申请id")
    private String id;
    @NotNull(message = "供应商不可为空")
    @ApiModelProperty("供应商id")
    private String supplerIds;
    @ApiModelProperty("供应商名称")
    private String suppler;
    @ApiModelProperty("预计开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expectStartDate;
    @ApiModelProperty("预计结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expectEndDate;
    @ApiModelProperty("开发信息")
    private List<String> devConfigIds;


}
