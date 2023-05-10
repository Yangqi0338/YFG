package com.base.sbc.client.smp.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/10 10:26:22
 * @mail 247967116@qq.com
 * 材料尺码
 */
@Data
public class SmpModuleSize {
    /** 尺码 */
    private String sizeCode;
    /** 尺码号型 */
    private String sizeName;
    /** 排序码 */
    private String sortCode;
    /** 尺码排序码 */
    private String code;
    /** 规格是否启用 */
    private String active;
    /** 尺码URL */
    private String sizeURL;
}
