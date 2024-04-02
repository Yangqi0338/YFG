package com.base.sbc.module.sample.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
public class FabricSummaryStyleSaveDto {

    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;

    @ApiModelProperty(value = "汇总Id"  )
    @NotBlank(message = "汇总Id不能为空")
    private String fabricSummaryId;

    @ApiModelProperty(value = "款式(大货款号) 列表"  )
    private List<String>  styleNos;
}
