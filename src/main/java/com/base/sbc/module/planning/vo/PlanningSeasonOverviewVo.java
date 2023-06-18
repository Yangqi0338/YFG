package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.sample.vo.SampleUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：产品季总览 列表vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-18 14:53
 */

@Data
@ApiModel("产品季总览-列表vo PlanningSeasonOverviewVo")
public class PlanningSeasonOverviewVo extends PlanningCategoryItem {
    @ApiModelProperty(value = "产品季名称")
    private String name;

    @ApiModelProperty(value = "月份")
    private String month;
    @ApiModelProperty(value = "波段")
    private String bandCode;
    @ApiModelProperty(value = "设计师")
    public List<SampleUserVo> designers;
}
