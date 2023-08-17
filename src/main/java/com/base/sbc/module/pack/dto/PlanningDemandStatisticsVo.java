package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PlanningDemandStatisticsVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-17 11:32
 */
@Data
@ApiModel("资料包-企划维度统计返回明细Vo PlanningDemandStatisticsVo")
public class PlanningDemandStatisticsVo {

    @ApiModelProperty(value = "字段id")
    private String fieldId;
    @ApiModelProperty(value = "维度id")
    private String demandId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "总数")
    private String total;
    @ApiModelProperty(value = "当前数")
    private String quantity;
}
