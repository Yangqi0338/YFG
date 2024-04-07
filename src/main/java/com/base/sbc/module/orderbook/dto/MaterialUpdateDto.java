package com.base.sbc.module.orderbook.dto;

import com.base.sbc.module.pack.dto.MaterialSupplierInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class MaterialUpdateDto {

    @ApiModelProperty(value = "foreignId")
    @NotEmpty(message = "foreignId不能为空")
    private String foreignId;

    @ApiModelProperty(value = "版本id")
    @NotEmpty(message = "版本id不能为空")
    private String bomVersionId;

    @ApiModelProperty(value = "cmt面料编码")
    @NotEmpty(message = "cmt面料编码不能为空")
    private String fabricCode;

    @ApiModelProperty(value = "cmt面料厂家名称")
    @NotEmpty(message = "cmt面料不能为空")
    private String fabricFactoryName;

    List<MaterialSupplierInfo> materialSupplierInfos;;




}
