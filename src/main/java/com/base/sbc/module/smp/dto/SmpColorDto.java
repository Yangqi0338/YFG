package com.base.sbc.module.smp.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/10 13:21:37
 * @mail 247967116@qq.com
 * 企业色库
 */
@Data
public class SmpColorDto extends SmpBaseDto {
    /**颜色名称*/
    private String colorName;
    /**色号*/
    private String colorCode;
    /**色系*/
    private String colorType;
    /**色系名称*/
    private String colorTypeName;
    /**色度id*/
    private String colorChromaId;
    /**色度名称*/
    private String colorChroma;
    /**生效范围（款式/材料）*/
    private String range;
}
