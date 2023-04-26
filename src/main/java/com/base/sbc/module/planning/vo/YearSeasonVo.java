package com.base.sbc.module.planning.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述： 年份季节筛选
 * @address com.base.sbc.module.planning.vo.YearSeasonVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-25 16:48
 * @version 1.0
 */
@Data
public class YearSeasonVo {
    @ApiModelProperty(value = "年份编码" ,example = "2023")
    private String year;
    @ApiModelProperty(value = "年份" ,example = "2023")
    private String yearDesc;
    @ApiModelProperty(value = "季节编码" ,example = "冬")
    private String season;
    @ApiModelProperty(value = "季节" ,example = "d")
    private String seasonDesc;

    public String getLabel(){
        return this.yearDesc+this.seasonDesc;
    }
}
