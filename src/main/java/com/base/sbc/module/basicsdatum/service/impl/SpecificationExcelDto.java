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
    @Excel(name = "编码")
    private String code;

    @Excel(name = "吊牌显示")
    private String hangtags;

    @Excel(name = "名称")
    private String name;

    @Excel(name = "类型")
    private String typeName;

    @Excel(name = "排序")
    private String sort;

    @Excel(name = "描述")
    private String remarks;

    @Excel(name = "状态",replace = {"true_0", "false_1"})
    private String status;
}
