package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.SampleAllocate;
import com.base.sbc.module.sample.entity.SampleAllocateItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣调拨 dto
 * @address com.base.sbc.module.sample.dto.SampleAllocateSaveDto
 */
@Data
@ApiModel("样衣调拨保存修改 SampleAllocateSaveDto")
public class SampleAllocateSaveDto extends SampleAllocate {

    @ApiModelProperty(value = "关联的明细")
    private List<SampleAllocateItem> sampleItemList;
}
