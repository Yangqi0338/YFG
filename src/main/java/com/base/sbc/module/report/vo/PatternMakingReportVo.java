package com.base.sbc.module.report.vo;

import com.base.sbc.config.utils.BigDecimalUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 */
@NoArgsConstructor
@Data
public class PatternMakingReportVo {

    @ApiModelProperty(value = "品类")
    private String prodCategoryName;
    @ApiModelProperty(value = "设计师")
    private String designer;
    @ApiModelProperty(value = "已下稿数")
    private BigDecimal index1;
    @ApiModelProperty(value = "已收样数")
    private BigDecimal index2;
    @ApiModelProperty(value = "收样占比")
    private BigDecimal index3;
    @ApiModelProperty(value = "打版完成")
    private BigDecimal index4;
    @ApiModelProperty(value = "打版未完成")
    private BigDecimal index5;
    @ApiModelProperty(value = "配齐面料")
    private BigDecimal index6;
    @ApiModelProperty(value = "未下发面料")
    private BigDecimal index7;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    public BigDecimal getIndex3() {
        if(BigDecimalUtil.notEqualZero(index1) && BigDecimalUtil.notEqualZero(index2)){
            return index2.divide(index1, 2, RoundingMode.DOWN).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN));
        }
        return BigDecimal.ZERO;
    }
}
