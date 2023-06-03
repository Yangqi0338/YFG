package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：设置版师dto
 * @address com.base.sbc.module.patternmaking.dto.SetPatternDesignDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-31 16:23
 * @version 1.0
 */

@Data
@ApiModel("制定版师dto  SetPatternDesignDto ")
public class SetPatternDesignDto {


    @ApiModelProperty(value = "打版id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "版师名称", required = true)
    @NotBlank(message = "版师名称不能为空")
    private String patternDesignName;
    /** 版师id */
    @ApiModelProperty(value = "版师id", required = true)
    @NotBlank(message = "版师id不能为空")
    private String patternDesignId;

}
