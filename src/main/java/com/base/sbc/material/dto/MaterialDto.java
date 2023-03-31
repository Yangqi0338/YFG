package com.base.sbc.material.dto;

import com.base.sbc.material.entity.Material;
import com.base.sbc.material.entity.MaterialDetails;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
public class MaterialDto {
    private Material material;
    private MaterialDetails materialDetails;
}
