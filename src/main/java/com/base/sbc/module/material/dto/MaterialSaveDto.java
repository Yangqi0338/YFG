package com.base.sbc.module.material.dto;

import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialColor;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.MaterialSize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/3 13:27:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialSaveDto extends Material {

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

    private boolean save;

    @ApiModelProperty("是否管理人员：1为管理人员")
    private String  materialManagerStaff;
}
