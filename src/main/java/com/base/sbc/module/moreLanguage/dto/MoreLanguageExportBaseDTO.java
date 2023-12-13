package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Data
public class MoreLanguageExportBaseDTO {

    @Excel(name = "翻译内容", width = 80.0)
    private String content;

    @Excel(name = "翻译语言", width = 20.0, mergeVertical = true)
    private String languageName;

    @Excel(name = "标准列Code", isColumnHidden = true)
    private String standardColumnCode;

    @Excel(name = "关键key的码", isColumnHidden = true)
    private String key;

    @Excel(name = "关键key的名字", isColumnHidden = true)
    private String keyName;

    @Excel(name = "map关联", isColumnHidden = true)
    private String mapping;
}

