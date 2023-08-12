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

    @ApiModelProperty(value = "品类")
    private String categoryName;

    @ApiModelProperty(value = "紧急状态")
    private String taskLevelName;

    @ApiModelProperty(value = "生产类型")
    private String  devtTypeName;

    @ApiModelProperty(value = "设计师")
    private String designerId;

    @ApiModelProperty(value = "工艺员")
    private String  technicianId;

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

    @ApiModelProperty(value = "产品季id"  )
    private String  planningSeasonId;

    @ApiModelProperty(value = "大类"  )
    private String prodCategory1st;

    @ApiModelProperty(value = "品类"  )
    private String prodCategory;

    @ApiModelProperty(value = "款式状态"  )
    private String  styleStatus;

    @ApiModelProperty(value = "配色"  )
    private String  colorName;

    @ApiModelProperty(value = "款号"  )
    private String   designNo;
}
