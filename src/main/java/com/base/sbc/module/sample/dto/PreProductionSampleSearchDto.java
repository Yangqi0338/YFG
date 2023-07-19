package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：产前样查询Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.PreProductionSampleSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:51
 */
@Data
@ApiModel("产前样查询Dto PreProductionSampleSearchDto")
public class PreProductionSampleSearchDto extends Page {


    @ApiModelProperty(value = "年份", example = "2022")
    private String year;

    @ApiModelProperty(value = "季节", example = "S")
    private String season;

    @ApiModelProperty(value = "月份", example = "1")
    private String month;

    @ApiModelProperty(value = "版师id", example = "1223333122223333333")
    private String patternDesignId;

}
