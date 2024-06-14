package com.base.sbc.module.patternmaking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternUserSearchVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-26 12:15
 */
@Data
@ApiModel("操作人筛选vo PatternUserSearchVo ")
public class PatternUserSearchVo {


    @ApiModelProperty(value = "流程完成状态:(0未完成,1已完成)")
    private String finishFlag;
    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    private String businessType;
}
