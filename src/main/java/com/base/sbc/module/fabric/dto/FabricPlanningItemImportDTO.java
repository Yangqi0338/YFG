package com.base.sbc.module.fabric.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class FabricPlanningItemImportDTO {
    @Excel(name = "物料编码")
    private String materialCode;
}
