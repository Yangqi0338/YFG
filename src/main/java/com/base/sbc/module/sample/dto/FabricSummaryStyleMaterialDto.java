package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FabricSummaryStyleMaterialDto extends Page {
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;

    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;

    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;

    @ApiModelProperty(value = "设计款号"  )
    private String designNo;


}
