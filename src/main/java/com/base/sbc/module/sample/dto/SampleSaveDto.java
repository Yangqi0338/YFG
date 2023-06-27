package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣及明细 dto
 * @address com.base.sbc.module.sample.dto.SampleSaveDto
 */
@Data
@ApiModel("样衣及明细保存修改 SampleSaveDto")
public class SampleSaveDto extends Sample {

    @ApiModelProperty(value = "关联的明细")
    private List<SampleItem> itemList;
}
