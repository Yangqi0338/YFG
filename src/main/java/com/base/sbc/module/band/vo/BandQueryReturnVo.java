package com.base.sbc.module.band.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("波段查询返回 BandQueryReturnVo")
public class BandQueryReturnVo extends BaseDataEntity<String> {
    /** 年份 */
    @ApiModelProperty(value = "年份", example = "2023")
    private String particularYear;
    /** 季节 */
    @ApiModelProperty(value = "季节", example = "A")
    private String season;
    private String seasonName;
    /** 月份 */
    @ApiModelProperty(value = "月份", example = "01")
    private String month;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称", example = "波段名称")
    private String bandName;
    /** 编码 */
    @ApiModelProperty(value = "编码", example = "aa")
    private String code;
    /** 排序 */
    @ApiModelProperty(value = "排序", example = "1")
    private String sort;
    /** 是否启用(0启用，1删除) */
    @ApiModelProperty(value = "是否启用(0启用，1删除)", example = "0")
    private String status;
}
