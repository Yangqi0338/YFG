package com.base.sbc.config.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import lombok.Data;

@Data
public class ExcelTableCodeVO {

    List<ExcelExportEntity> excelParams;

    Map<String,String> imgColumnMap;

    Set<String> mapColumns;

}
