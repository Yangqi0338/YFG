package com.base.sbc.module.material.vo;

import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialColor;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.MaterialSize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/3 11:41:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialVo extends Material {
    /**
     * 创建者头像
     */
    private String userAvatar;
    /**
     * 所有标签
     */
    private List<MaterialLabel> labels;

    /**
     * 所有尺码
     */
    private List<MaterialSize> sizes;

    /**
     * 所有颜色
     */
    private List<MaterialColor> colors;
}
