package com.base.sbc.module.planning.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：年份-产品季-波段树Vo YearSeasonBandVo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.YearSeasonBandVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-28 16:22
 */
@Data
@ApiModel("年份-产品季-波段树Vo YearSeasonBandVo")
public class YearSeasonBandVo {

    @ApiModelProperty(value = "年份", example = "2023")
    private String yearName;
    @ApiModelProperty(value = "筛选", example = "2023")
    private String search;


    @ApiModelProperty(value = "产品季名称", example = "2023 S MM")
    private String planningSeasonName;
    @ApiModelProperty(value = "产品季id", example = "1")
    private String planningSeasonId;

    @ApiModelProperty(value = "波段名称", example = "2023")
    private String bandName;

    @ApiModelProperty(value = "品牌", example = "MM")
    private String brandName;

    @ApiModelProperty(value = "品牌编码", example = "MM")
    private String brand;
    @ApiModelProperty(value = "数量", example = "100")
    private long total;
    @ApiModelProperty(value = "级别", example = "0")
    private int level = 0;
    @ApiModelProperty(value = "是否有波段级别", example = "0")
    private String hasBand;

    @ApiModelProperty(value = "波段标记 存在查询波段数量", example = "0")
    private String   bandflag;

    public String getLabel() {
        if (level == 0) {
            return this.yearName + "(" + total + ")";
        } else if (level == 1) {
            return this.planningSeasonName + "(" + total + ")";
        } else if (level == 2) {
            return this.bandName + "(" + total + ")";
        }
        return "";
    }

    @ApiModelProperty(value = "产品季")
    Object children;

    @ApiModelProperty(value = "补充其他的信息")
    private Object supplementInfo;
}
