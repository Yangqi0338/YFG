package com.base.sbc.module.planning.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：年份品牌vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.YearBrandVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-21 14:22
 */
@Data
@ApiModel("年份品牌Vo YearBrandVo")
public class YearBrandVo {

    @ApiModelProperty(value = "年份", example = "2023")
    private String yearName;

    @ApiModelProperty(value = "品牌", example = "MM")
    private String brandName;

    @ApiModelProperty(value = "数量", example = "100")
    private int total;

    public String getLabel() {
        return this.yearName + this.brandName + "(" + total + ")";
    }

    @ApiModelProperty(value = "产品季")
    List<PlanningSeasonTreeVo> children;
}
