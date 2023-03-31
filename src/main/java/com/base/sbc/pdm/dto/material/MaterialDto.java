package com.base.sbc.pdm.dto.material;

import com.base.sbc.pdm.entity.material.Material;
import com.base.sbc.pdm.entity.material.MaterialDetails;
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
