package com.base.sbc.module.style.dto;

import com.base.sbc.module.formtype.entity.FieldVal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SaveCoefficientDto {

    @ApiModelProperty(value = "配色id")
    private String styleId;


    @ApiModelProperty(value = "")
    private List<FieldVal> fieldValList;


}
