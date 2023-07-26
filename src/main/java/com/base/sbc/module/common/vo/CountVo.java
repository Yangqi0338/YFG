package com.base.sbc.module.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：CountVo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.vo.CountVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-25 18:23
 */
@Data
@ApiModel("统计Vo CountVo")
public class CountVo {
    @ApiModelProperty(value = "项目", example = "上衣")
    private String label;
    @ApiModelProperty(value = "值", example = "1")
    private long count;

}
