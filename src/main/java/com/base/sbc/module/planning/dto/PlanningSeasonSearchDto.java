package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    @ApiModelProperty(value = "季节编码" ,example = "S")
    private String season;
    @ApiModelProperty(value = "年份" ,required = false,example = "['2023','2022']")
    private List<String> yearList;

    @ApiModelProperty(value = "季节编码" ,example = "['S','W']")
    private List<String> seasonList;
    @ApiModelProperty(value = "品牌" ,example = "['SS','MM']")
    private List<String> brandList;
}
