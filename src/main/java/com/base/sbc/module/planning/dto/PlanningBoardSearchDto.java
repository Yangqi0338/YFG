package com.base.sbc.module.planning.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 类描述：企划看板筛选条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.PlanningBoardSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 09:25
 */
@Data
@ApiModel("企划看板筛选条件 PlanningBoardSearchDto")
public class PlanningBoardSearchDto {


    @ApiModelProperty(value = "关键字筛选", required = true, example = "1667076468196474882")
    @NotBlank(message = "产品季不能为空")
    private String planningSeasonId;

    @ApiModelProperty(value = "关键字筛选", required = false, example = "5CA232731")
    private String search;

    @ApiModelProperty(value = "月份", required = false, example = "01")
    private List<String> month;

    @ApiModelProperty(value = "波段", required = false, example = "1A")
    private List<String> band;

    @ApiModelProperty(value = "品类", required = false, example = "700655279267643450")
    private List<String> category;

}
