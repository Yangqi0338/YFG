package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/27 17:26
 * @mail 247967116@qq.com
 */
@Data
public class SecondIngredientSyncDto {

    private String id;
    private String kindName;
    private String kindCode;
    private String status;
}
