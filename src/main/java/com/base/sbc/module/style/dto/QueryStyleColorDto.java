package com.base.sbc.module.style.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/*查询款式配色*/
public class QueryStyleColorDto extends Page {
    /**
     * 样衣id
     */
    @ApiModelProperty(value = "样衣id")
    private String styleId;

    @ApiModelProperty(value = "大货编号多个使用，分割"  )
    private String styleNo;

    @ApiModelProperty(value = "ids"  )
    private  String ids;

    @ApiModelProperty(value = "颜色规格"  )
    private String  colorSpecification;

    @ApiModelProperty(value = "细分"  )
    private String subdivide;

    @ApiModelProperty(value = "是否款式"  )
    private String isTrim;

    @ApiModelProperty(value = "是否上会"  )
    private String meetFlag;

}
