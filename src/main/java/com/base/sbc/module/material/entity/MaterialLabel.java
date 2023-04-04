package com.base.sbc.module.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 卞康
 * @date 2023/3/29 16:15:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_material_label")
public class MaterialLabel extends BaseDataEntity<String> {
    private String materialId;
    private String labelId;
    private String labelName;
    private String labelColor;
}
