package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：关联样板号
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackInfoSetPatternNoDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-04 15:41
 */
@Data
@ApiModel("资料包-关联样板号 PackInfoSetPatternNoDto")
public class PackInfoSetPatternNoDto {


    @ApiModelProperty(value = "资料包id")
    @NotBlank(message = "资料包id不能为空")
    private String packId;
    @ApiModelProperty(value = "打版指令id")
    @NotBlank(message = "打版指令id不能为空")
    private String patternMakingId;

    @ApiModelProperty(value = "样板号")
    @NotBlank(message = "样板号不能为空")
    private String patternNo;

}
