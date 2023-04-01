package com.base.sbc.module.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 卞康
 * @date 2023/3/31 9:32:05
 * 素材尺码关联
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_material_size")
public class MaterialSize extends BaseDataEntity<String> {
    private String sizeId;
    private String sizeName;
    private String materialId;
}
