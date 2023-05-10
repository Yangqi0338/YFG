package com.base.sbc.module.smp.entity;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/10 11:23:23
 * @mail 247967116@qq.com
 */
@Data
public class SmpSizeQty {
    /**尺码：如XS/S*/
    private String pSizeCode;
    /**大货款号尺码id*/
    private String sizeCode;
    /**门幅规格*/
    private String itemSize;
    /**材料尺码URL*/
    private String matSizeUrl;
    /**单件用量*/
    private String itemQty;
}
