package com.base.sbc.pdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/3/31 9:32:05
 * 素材尺码关联
 */
@Data
@TableName("t_material_size")
public class MaterialSize {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String sizeId;
    private String sizeName;
    private String materialId;
}
