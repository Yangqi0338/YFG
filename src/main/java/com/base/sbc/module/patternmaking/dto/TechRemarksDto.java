package com.base.sbc.module.patternmaking.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

/**
 * 类描述：评分
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.ScoreDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-15 15:34
 */
@Data
@ApiModel("后技术备注说明Dto")
public class TechRemarksDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "后技术备注说明")
    private String remark;
}
