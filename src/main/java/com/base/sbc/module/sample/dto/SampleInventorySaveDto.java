package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.SampleInventory;
import com.base.sbc.module.sample.entity.SampleInventoryItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣盘点 dto
 * @address com.base.sbc.module.sample.dto.SampleInventorySaveDto
 */
@Data
@ApiModel("样衣样衣盘点保存 SampleInventorySaveDto")
public class SampleInventorySaveDto extends SampleInventory {

    @ApiModelProperty(value = "关联的明细")
    private List<SampleInventoryItem> sampleItemList;
}
