package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

@Data
public class BasicsdatumMaterialPageAndStyleDto extends Page {
    /**
     * 物料清单id
     */
    private String packBomId;
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
    /**
     * 图片标识
     */
    private String imgFlag;
}
