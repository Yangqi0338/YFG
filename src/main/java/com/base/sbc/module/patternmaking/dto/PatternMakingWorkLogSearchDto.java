package com.base.sbc.module.patternmaking.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：打版管理-工作记录查询条件dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 19:49
 */
@Data
@ApiModel("打版管理-工作记录查询条件dto PatternMakingWorkLogSearchDto ")
public class PatternMakingWorkLogSearchDto {

    @ApiModelProperty(value = "打板id")
    @NotBlank(message = "打板id不能为空")
    private String patternMakingId;
    /**
     * 用户类型:裁剪工、车缝工
     */
    @ApiModelProperty(value = "用户类型:裁剪工、车缝工")
    @NotBlank(message = "用户类型不能为空")
    private String userType;
}
