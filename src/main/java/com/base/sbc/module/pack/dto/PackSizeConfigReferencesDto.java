package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackSizeConfigReferencesDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-04 15:19
 */
@Data
@ApiModel("资料包-尺寸表引用 PackSizeConfigReferencesDto")
public class PackSizeConfigReferencesDto {

    String sourceForeignId;
    String sourcePackType;
    String targetForeignId;
    String targetPackType;

}
