package com.base.sbc.module.sample.vo;

import com.base.sbc.module.sample.entity.SampleCirculate;
import com.base.sbc.module.sample.entity.SampleCirculateItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣借还 vo
 * @address com.base.sbc.module.sample.vo.SampleCirulateVo
 */
@Data
@ApiModel("样衣及明细保存修改 SampleCirculateVo")
public class SampleCirculateVo extends SampleCirculate {

    @ApiModelProperty(value = "关联的明细")
    private List<SampleCirculateItem> itemList;
}
