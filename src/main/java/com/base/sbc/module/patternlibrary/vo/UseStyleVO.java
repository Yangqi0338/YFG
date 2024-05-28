package com.base.sbc.module.patternlibrary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 使用款记录 VO
 *
 * @author XHTE
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "UseStyleVO对象", description = "使用款 VO")
public class UseStyleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 大货款图
     */
    @ApiModelProperty("大货款图")
    private String stylePic;

    /**
     * 设计款号
     */
    @ApiModelProperty("设计款号")
    private String designNo;


    /**
     * 大货款号
     */
    @ApiModelProperty("大货款号")
    private String styleNo;

    /**
     * 波段code
     */
    @ApiModelProperty("波段code")
    private String bandCode;

    /**
     * 波段名称
     */
    @ApiModelProperty("波段名称")
    private String bandName;

    /**
     * 今年销售件数
     */
    @ApiModelProperty("今年销售件数")
    private Integer thisYearSaleNum = 0;

    /**
     * 去年销售件数
     */
    @ApiModelProperty("去年销售件数")
    private Integer lastYearSaleNum = 0;

    /**
     * 前年销售件数
     */
    @ApiModelProperty("前年销售件数")
    private Integer beforeLastYearNum = 0;

    /**
     * 历史销售件数
     */
    @ApiModelProperty("历史销售件数")
    private Integer historySaleNum = 0;

    public void setYearSaleNum(String year, Integer saleNum){
        LocalDateTime now = LocalDateTime.now();
        if (String.valueOf(now.getYear()).equals(year)) {
            this.setThisYearSaleNum(saleNum);
        }else if (String.valueOf(now.minusYears(1).getYear()).equals(year)) {
            this.setLastYearSaleNum(saleNum);
        }else if (String.valueOf(now.minusYears(2).getYear()).equals(year)) {
            this.setBeforeLastYearNum(saleNum);
        }
    }
}
