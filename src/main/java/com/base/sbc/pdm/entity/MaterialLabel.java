package com.base.sbc.pdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/3/29 16:15:13
 */
@Data
@TableName("t_material_label")
public class MaterialLabel {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String materialId;
    private String labelId;
    private String labelName;
}
