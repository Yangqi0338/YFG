package com.base.sbc.module.sample.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：保存工艺信息 dto
 * @address com.base.sbc.module.sample.vo.TechnologyInfoDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-23 10:40
 * @version 1.0
 */
@ApiModel("工艺说明保存 TechnologyInfoDto")
@Data
public class TechnologyInfoDto {

    @ApiModelProperty(value = "表单类型主键"  )
    private String fieldName;

    @ApiModelProperty(value = "值"  )
    private String val;
}
