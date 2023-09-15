package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：维度数量统计
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.DimensionTotalVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 10:42
 */
@Data
@ApiModel("维度数量统计Vo DimensionTotalVo")
public class PlanningDimensionTotalVo {

    @ApiModelProperty(value = "字段id", example = "1A")
    private String fieldId;

    @ApiModelProperty(value = "维度名称", example = "1A")
    private String fieldName;
    @ApiModelProperty(value = "字段说明", example = "1A")
    private String fieldExplain;
    @ApiModelProperty(value = "维度值", example = "1A")
    private String val;
    @ApiModelProperty(value = "维度值名称", example = "1A")
    private String valName;

    @ApiModelProperty(value = "企划数量", example = "2")
    private Long planningTotal;

    @ApiModelProperty(value = "总数", example = "2")
    private Long total;

    public Long totalAdd() {
        if (total == null) {
            total = 1L;
        } else {
            total++;
        }
        return total;
    }

    public Long planningTotalAdd() {
        if (planningTotal == null) {
            planningTotal = 1L;
        } else {
            planningTotal++;
        }
        totalAdd();
        return planningTotal;
    }
}
