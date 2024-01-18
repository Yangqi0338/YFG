package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Data
public class MoreLanguageExportBaseDTO {

    @Excel(name = "标准列Code（勿动）", isColumnHidden = true)
    private String standardColumnCode;

    @Excel(name = "关键key的码（勿动）", isColumnHidden = true)
    private String key;

    @Excel(name = "关键key的名字（勿动）", isColumnHidden = true)
    private String keyName;

    @Excel(name = "表格关键Key（勿动）", isColumnHidden = true)
    private String excelCode;

    @Excel(name = "map关联（勿动）", isColumnHidden = true)
    private String mapping;
}

