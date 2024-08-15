package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.basicsdatum.entity.BasicProcessGallery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 卞康
 * @date 2024-03-11 13:43:32
 * @mail 247967116@qq.com
 */
@Data
public class BasicProcessGallerySaveDto extends BasicProcessGallery {
    /** 品牌id */
    @ApiModelProperty(value = "品牌id")
    @NotBlank(message = "至少选择一个品牌")
    private String brandId;

    private String ids;
}
