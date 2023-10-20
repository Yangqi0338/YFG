package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于款式上传返回款式年品牌季节
 */
@Data
public class StyleUploadVo {

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String styleNo;

    @ApiModelProperty(value = "设计款图")
    private String designNo;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 年份名称
     */
    @ApiModelProperty(value = "年份名称")
    private String yearName;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节名称
     */
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
}
