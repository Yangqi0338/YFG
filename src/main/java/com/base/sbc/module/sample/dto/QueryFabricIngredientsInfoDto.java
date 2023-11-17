package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryFabricIngredientsInfoDto extends Page {

    @ApiModelProperty(value = "发起")
    private String originate;

    @ApiModelProperty(value = "品类")
    private String categoryId;

    @ApiModelProperty(value = "品类")
    private String categoryName;

    /** 开发类型名称 */
    @ApiModelProperty(value = "开发类型名称"  )
    private String devTypeName;

    @ApiModelProperty(value = "开发情况"  )
    private String  devCondition;

    @ApiModelProperty(value = "厂家编码"  )
    private String  manufacturerNumber;

    @ApiModelProperty(value = "厂家"  )
    private String   manufacturer;

    @ApiModelProperty(value = "调样设计师"  )
    private String   atactiformStylist;

    private String practicalAtactiformDate;

    @ApiModelProperty(value = "创建人"  )
    private String createName;

    @ApiModelProperty(value = "创建时间"  )
    private String createDate;

    @ApiModelProperty(value = "完成状态"  )
    private String completionStatus;

    @ApiModelProperty(value = "导出标记"  )
    private String   deriveFlag;

}
