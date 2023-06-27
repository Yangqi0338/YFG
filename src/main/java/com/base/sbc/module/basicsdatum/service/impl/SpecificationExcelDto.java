package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/27 11:38
 * @mail 247967116@qq.com
 */
@Data
public class SpecificationExcelDto {
    @Excel(name = "Internal Size")
    private String code;

    @Excel(name = "吊牌显示")
    private String name;

    @Excel(name = "描述")
    private String remarks;

    @Excel(name = "排序")
    private String sort;

    @Excel(name = "Dimension Type")
    private String type;
}
