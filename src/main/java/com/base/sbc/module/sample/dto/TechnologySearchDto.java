package com.base.sbc.module.sample.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：工艺信息查询 dto
 * @address com.base.sbc.module.sample.dto.TechnologySearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:43
 * @version 1.0
 */
@Data
@ApiModel("工艺信息查询   TechnologySearchDto")
public class TechnologySearchDto {

    @ApiModelProperty(value = "样衣设计id", example = "0")
    private String sampleId;
    @ApiModelProperty(value = "产品季id", example = "0")
    @NotBlank(message = "产品季不能为空")
    private String planningSeasonId;
    @ApiModelProperty(value = "品类id", example = "0")
    @NotBlank(message = "品类不能为空")
    private String categoryId;



}
