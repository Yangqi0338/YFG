package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xhj
 * @Date 2023/7/1 10:21
 */
@Data
@ApiModel(value = "色号和型号基本信息（下拉组件）")
public class ColorModelNumberBaseSelectVO {
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
}
