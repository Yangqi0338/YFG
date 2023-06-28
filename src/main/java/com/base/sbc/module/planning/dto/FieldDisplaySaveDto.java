package com.base.sbc.module.planning.dto;


import com.base.sbc.module.planning.vo.FieldDisplayVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：字段显示隐藏-保存dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.FieldDisplayConfigDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-28 15:18
 */
@Data
@ApiModel("字段显示隐藏-保存dto FieldDisplayConfigDto")
public class FieldDisplaySaveDto {

    /**
     * 类型(款式看板/企划看板)
     */
    @ApiModelProperty(value = "类型(planningBoard企划看板,styleBoard款式看板)", example = "planningBoard")
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 配置
     */
    @ApiModelProperty(value = "配置")
    @NotNull(message = "配置不能为空")
    private List<FieldDisplayVo> config;
}
