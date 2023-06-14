package com.base.sbc.module.sample.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：工艺信息查询 dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.TechnologySearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:43
 */
@Data
@ApiModel("样衣设计维度信息查询条件dto   DimensionLabelsSearchDto")
public class DimensionLabelsSearchDto {

    @ApiModelProperty(value = "样衣设计id", example = "0")
    private String sampleDesignId;
    @ApiModelProperty(value = "产品季id", example = "0")
    @NotBlank(message = "季节")
    private String season;
    @ApiModelProperty(value = "品类id(2级)", example = "0")
    @NotBlank(message = "品类不能为空")
    private String categoryId;


}
