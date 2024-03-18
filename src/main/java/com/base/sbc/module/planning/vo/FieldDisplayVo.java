package com.base.sbc.module.planning.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：字段显示配置Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.FieldDisplayVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-28 16:07
 */
@Data
@ApiModel("字段显示配置Vo FieldDisplayVo")
public class FieldDisplayVo {

    @ApiModelProperty(value = "属性", example = "bandCode")
    private String field;

    @ApiModelProperty(value = "属性名称", example = "波段")
    private String name;

    @ApiModelProperty(value = "是否显示", example = "true")
    private boolean display;

    @ApiModelProperty(value = "排序", example = "0")
    private String sort;

    @ApiModelProperty(value = "是否动态字段", example = "true")
    private boolean dynamicFields;

}
