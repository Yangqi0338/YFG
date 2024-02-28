package com.base.sbc.module.smp.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/9 17:50:56
 * @mail 247967116@qq.com
 */
@Data
public class  SmpSize {
    /**尺寸(XS、S)*/
    private String size;
    /**尺寸编号(5、1)*/
    private String sizeNumber;
    /**尺寸描述*/
    private String sizeDescription;
    /**排序code*/
    private String code;
    /**吊牌显示*/
    private String productSizeName;
    /**基码标识*/
    private Boolean baseSize;
    /**充绒量/克重*/
    private String skuFiller;
    /**特殊规格*/
    private String specialSpec;
    /**合作方条形码*/
    private String outsideBarcode;
    /**
     * 外部尺码code
     */
    @ApiModelProperty(value = "外部尺码code")
    private String outsideSizeCode;
}
