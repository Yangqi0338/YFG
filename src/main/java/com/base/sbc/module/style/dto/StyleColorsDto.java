package com.base.sbc.module.style.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StyleColorsDto extends Page {

    private String search;

    @ApiModelProperty(value = "大货编号多个使用，分割"  )
    private String styleNo;

    @ApiModelProperty(value = "款号"  )
    private String   designNo;

    @ApiModelProperty(value = "款号"  )
    private String   seasonId;

    @ApiModelProperty(value = "品类")
    private String categoryName;

    @ApiModelProperty(value = "大类")
    private String prodCategory1st;

    @ApiModelProperty(value = "品类编码")
    private String prodCategory;

    @ApiModelProperty(value = "品类")
    private String prodCategoryName;

    @ApiModelProperty(value = "大类名称"  )
    private String  prodCategory1stName;

    @ApiModelProperty(value = "中类")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "中类"  )
    private String prodCategory2nd;

    @ApiModelProperty(value = "波段")
    private String bandName;

    @ApiModelProperty(value = "波段id"  )
    private String bandCode;

    @ApiModelProperty(value = "维度id"  )
    private String dimensionLabelId;

    @ApiModelProperty(value = "维度val"  )
    private String dimensionLabelVal;

    /**
     * 下单标记（0否 1是）
     */
    @ApiModelProperty(value = "下单标记（0否 1是）")
    private String orderFlag;

    private String dimensionValue;
}
