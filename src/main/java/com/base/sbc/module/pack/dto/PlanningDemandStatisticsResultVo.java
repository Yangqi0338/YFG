package com.base.sbc.module.pack.dto;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PlanningDemandStatisticsResultVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-17 14:10
 */
@Data
@ApiModel("资料包-企划维度统计返回Vo PlanningDemandStatisticsResultVo")
public class PlanningDemandStatisticsResultVo {

    @ApiModelProperty(value = "最大长度")
    private int maxLength;
    @ApiModelProperty(value = "明细")
    private Collection<List<PlanningDemandStatisticsVo>> list;

    public int getCount() {
        return CollUtil.size(list);
    }
}
