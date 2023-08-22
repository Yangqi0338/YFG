package com.base.sbc.module.smp.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/16 13:43:40
 * @mail 247967116@qq.com
 */
@Data
public class PlmStyleSizeParam  {

    /**大货款号*/
    private String styleNo;

    private String code;

    /**尺码组*/

    private String sizeCategory;

    /**尺码数量*/

    private Integer sizeNum;
}
