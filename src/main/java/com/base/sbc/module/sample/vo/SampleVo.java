package com.base.sbc.module.sample.vo;

import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣及明细 vo
 * @address com.base.sbc.module.sample.vo.SampleVo
 */
@Data
@ApiModel("样衣及明细保存修改 SampleVo")
public class SampleVo extends Sample {

    @ApiModelProperty(value = "关联的明细")
    private List<SampleItem> sampleItemList;
}
