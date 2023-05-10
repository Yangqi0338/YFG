package com.base.sbc.module.smp.entity;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/10 10:21:20
 * @mail 247967116@qq.com
 */
@Data
public class SmpColor {
    /**颜色编码*/
    private String colorCode;
    /**颜色名称*/
    private String colorName;
    /**颜色是否启用*/
    private String active;
    /**供应商物料颜色*/
    private String supplierMatColor;
}
