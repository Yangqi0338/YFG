package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.module.patternmaking.entity.PatternMaking;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：打版管理dto
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-29 15:24
 * @version 1.0
 */
@Data
@ApiModel("打版管理dto PatternMakingDto ")
public class PatternMakingDto extends PatternMaking {


    @ApiModelProperty(value = "样衣设计id"  )
    @NotBlank(message = "样衣设计id不能为空")
    private String sampleDesignId;
}
