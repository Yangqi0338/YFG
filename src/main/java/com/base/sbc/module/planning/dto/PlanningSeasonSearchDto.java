package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：产品季查询
 * @address com.base.sbc.module.planning.dto.PlanningSeasonSearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-25 17:17
 * @version 1.0
 */
@Data
@ApiModel("商品企划-产品季查询 PlanningSeasonSaveDto")
public class PlanningSeasonSearchDto extends Page {

    @ApiModelProperty(value = "年份" ,required = false,example = "2023")
    private String year;

    @ApiModelProperty(value = "季节编码" ,example = "冬")
    private String season;
}
