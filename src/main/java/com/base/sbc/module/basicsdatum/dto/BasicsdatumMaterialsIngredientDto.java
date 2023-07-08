package com.base.sbc.module.basicsdatum.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/8 11:05:31
 * @mail 247967116@qq.com
 */
@Data
public class BasicsdatumMaterialsIngredientDto extends QueryDto{
    private String material;
    private String ingredient;
    private String code;

}
