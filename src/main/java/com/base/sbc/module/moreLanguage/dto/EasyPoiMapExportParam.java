package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import lombok.Data;

import java.util.List;


@Data
public class EasyPoiMapExportParam {

    private ExportParams title;

    private List<ExcelExportEntity> entity;
    private List<?> data;

}
