package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

@Data
public class BasicsdatumMaterialPageAndStyleDto extends Page {
    /**
     * bom阶段
     */
    private String bomPhase;
    /**
     * bom阶段
     */
    private String materialsCode;
    /**
     * 分类id
     */
    private String categoryId;
}
