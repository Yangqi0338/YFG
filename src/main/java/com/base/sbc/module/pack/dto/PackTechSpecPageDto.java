package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-工艺说明筛选条件
 *
 * @author lixianglin
 * @version 1.0
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:09
 */
@Data
@ApiModel("资料包-工艺说明筛选条件 PackCommonSearchDto")
public class PackTechSpecPageDto extends PackCommonPageSearchDto {

    @ApiModelProperty(value = "工艺类型:裁剪工艺、基础工艺、小部件、注意事项、整烫包装")
    @NotBlank(message = "工艺类型不能为空")
    private String specType;
}
