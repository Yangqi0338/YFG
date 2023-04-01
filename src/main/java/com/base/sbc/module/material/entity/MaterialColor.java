package com.base.sbc.module.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 卞康
 * @date 2023/3/31 16:04:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_material_color")
public class MaterialColor extends BaseDataEntity<String> {
    private String materialId;
    private String colorId;
    private String colorName;
}
