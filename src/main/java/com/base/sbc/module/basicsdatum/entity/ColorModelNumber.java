package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

/**
 * 色号和色型
 */
@Data
@TableName("t_color_model_number")
public class ColorModelNumber extends BaseDataEntity<String> {
    /**状态*/
    private String status;

    /**编码*/
    private String code;

    /**名称*/
    private String name;

    private String fileName;

    /**备注*/
    private String remarks;

    /**材料种类id*/
    private String mat2ndCategoryId;

    /**材料种类名称*/
    private String mat2ndCategoryName;
}
