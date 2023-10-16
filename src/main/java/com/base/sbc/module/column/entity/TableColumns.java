package com.base.sbc.module.column.entity;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/10/13 19:27:47
 * @mail 247967116@qq.com
 */
@Data
public class TableColumns extends BaseDataEntity<String> {
    /**
     * 字段名称
     */
    private String title;
}
