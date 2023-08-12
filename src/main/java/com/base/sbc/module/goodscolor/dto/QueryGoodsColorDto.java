package com.base.sbc.module.goodscolor.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*查询物料颜色*/
@Data
public class QueryGoodsColorDto  extends Page {

    @ApiModelProperty(value = "颜色"  )
    private String color;

    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;

    @ApiModelProperty(value = "状态"  )
    private String status;
}
