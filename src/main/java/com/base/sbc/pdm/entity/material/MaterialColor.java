package com.base.sbc.pdm.entity.material;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/3/31 16:04:02
 */
@Data
@TableName("t_material_color")
public class MaterialColor {
    private String id;
    private String materialId;
    private String colorId;
    private String colorName;
}
