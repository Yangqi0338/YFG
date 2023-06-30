package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.SampleSale;
import com.base.sbc.module.sample.entity.SampleSaleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣销售 dto
 * @address com.base.sbc.module.sample.dto.SampleSaleSaveDto
 */
@Data
@ApiModel("样衣销售保存修改 SampleSaleSaveDto")
public class SampleSaleSaveDto extends SampleSale {

    @ApiModelProperty(value = "关联的明细")
    private List<SampleSaleItem> sampleItemList;
}
