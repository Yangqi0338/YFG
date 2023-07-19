package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.PreProductionSample;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：
 * 产前样 PreProductionSampleDto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:47
 */
@Data
@ApiModel("产前样 PreProductionSampleDto")
public class PreProductionSampleDto extends PreProductionSample {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

}
