package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasicsdatumDimensionalityVo extends BasicsdatumDimensionality {

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
