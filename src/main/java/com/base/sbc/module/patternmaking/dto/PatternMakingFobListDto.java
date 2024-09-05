package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("打版指令查询dto PatternMakingFobListDto ")
public class PatternMakingFobListDto extends QueryFieldDto {

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "供应商id")
    private String patternRoomId;

    @ApiModelProperty(value = "厂家款号"  )
    private String supplierStyleNo;

    @ApiModelProperty(value = "下发状态"  )
    private String prmSendStatus;

    private String bindDesign;

}
