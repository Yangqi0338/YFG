package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningDimensionality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanningDimensionalityVo extends PlanningDimensionality {



    @ApiModelProperty(value = "分组名称")
    private String groupName;


    @ApiModelProperty(value = "字段名称")
    private String  fieldName;


    @ApiModelProperty(value = "字段说明")
    private String    fieldExplain;

    @ApiModelProperty(value = "编辑")
    private boolean readonly;

    public boolean getReadonly() {
        return true;
    }
}
