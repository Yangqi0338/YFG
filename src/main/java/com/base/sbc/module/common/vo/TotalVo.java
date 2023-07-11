package com.base.sbc.module.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：统计vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.vo.TotalVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-10 15:41
 */
@Data
@ApiModel("统计Vo ")
public class TotalVo {
    @ApiModelProperty(value = "项目", example = "包装费")
    private String label;
    @ApiModelProperty(value = "值", example = "32.54")
    private BigDecimal total;

}
