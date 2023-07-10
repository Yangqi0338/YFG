package com.base.sbc.module.pack.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 公式计算Dto
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.FormulaDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-10 20:47
 */
@Data
@ApiModel("资料包-公式计算Dto FormulaDto")
public class FormulaDto {
    @ApiModelProperty(value = "公式", example = "物料费+加工费+二次加工费")
    @NotBlank(message = "公式不能为空")
    private String formula;


    @ApiModelProperty(value = "各项的值")
    @NotEmpty(message = "值不能为空")
    Map<String, BigDecimal> itemVal;
}
