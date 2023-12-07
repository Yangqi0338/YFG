package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/26 15:39
 * @mail 247967116@qq.com
 */
@Data
public class ColorModelNumberExcelDto {
    @Excel(name = "编码")
    private String code;

    @Excel(name = "字段名称")
    private String name;

    @Excel(name = "翻译内容")
    private String content;

    @Excel(name = "依赖于")
    private String mat2ndCategoryName;
}
