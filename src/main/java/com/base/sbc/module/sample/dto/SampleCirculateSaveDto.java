package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.SampleCirculate;
import com.base.sbc.module.sample.entity.SampleCirculateItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("样衣借还保存 SampleDto")
public class SampleCirculateSaveDto extends SampleCirculate {

    @ApiModelProperty(value = "样衣借还明细列表", example = "")
    private List<SampleCirculateItem> itemList;
}
