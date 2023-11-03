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
@ApiModel("评分Dto  ScoreDto")
public class ScoreDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "score", required = true, example = "1")
    @Range(min = 0, max = 100, message = "评分0-100")
    private BigDecimal score;
}
