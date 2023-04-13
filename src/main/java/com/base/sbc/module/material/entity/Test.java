package com.base.sbc.module.material.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/4/13 14:31:35
 */
@Data
public class Test {
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("说明")
    private String explain;
}
