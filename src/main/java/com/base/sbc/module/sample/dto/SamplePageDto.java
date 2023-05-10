package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 类描述： 样衣分页查询
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SamplePageDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 18:03
 */
@Data
@ApiModel("样衣分页查询 SampleDto")
public class SamplePageDto extends Page {

    /**
     * 年份
     */
    @ApiModelProperty(value = "年份", example = "2022")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节", example = "S")
    private String season;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份", example = "1")
    private String month;

}
