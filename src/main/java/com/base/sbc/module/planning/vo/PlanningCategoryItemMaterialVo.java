package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanningCategoryItemMaterialVo {

    @ApiModelProperty(value = "编号" ,example = "689467740238381056")
    private String id;
    /** 素材库类型 */
    @ApiModelProperty(value = "素材库类型"  )
    private String materialType;
    /** 素材库id */
    @ApiModelProperty(value = "素材库id"  )
    private String materialId;
    @ApiModelProperty(value = "素材库id"  )
    private String planningCategoryItemId;
}
