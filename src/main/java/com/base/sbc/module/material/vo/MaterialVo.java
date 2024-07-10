package com.base.sbc.module.material.vo;

import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialColor;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.MaterialSize;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * 创建者工号
     */
    private String userName;

    @ApiModelProperty(value = "是否收藏")
    private boolean collect;

    /**
     * 收藏数量
     */
    private String collectNum;

    /**
     * 引用数量
     */
    private String quoteNum;

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

    @ApiModelProperty("引用标志，ture:已引用，其他为未引用")
    private Boolean citeFlag;

}
