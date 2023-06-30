package com.base.sbc.module.sample.vo;

import com.base.sbc.module.sample.entity.SampleWarehouse;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：样衣仓库 vo
 * @address com.base.sbc.module.sample.vo.SampleWarehouseVo
 */
@Data
@ApiModel("样衣及明细保存修改 SampleWarehouseVo")
public class SampleWarehouseVo extends SampleWarehouse {
}
