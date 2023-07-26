package com.base.sbc.module.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：下拉选项vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.vo.SelectOptionsVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-25 14:11
 */
@Data
@ApiModel("下拉选择Vo ")
public class SelectOptionsVo {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "label")
    private String label;
    @ApiModelProperty(value = "value")
    private String value;
}
