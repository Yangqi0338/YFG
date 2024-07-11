package com.base.sbc.module.style.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MangoHangTagExeclDto {
    @ApiModelProperty(name = "大货款号")
    @Excel(name = "*大货款号", width = 20)
    private String styleNo;

    @ApiModelProperty(name = "质量等级")
    @Excel(name = "*质量等级", width = 20)
    private String qualityGrade;

    @ApiModelProperty(name = "品名")
    @Excel(name = "*品名", width = 20)
    private String productName;

    @ApiModelProperty(name = "品名翻译")
    @Excel(name = "*品名翻译", width = 20)
    private String productNameTranslate;

    @ApiModelProperty(name = "执行标准")
    @Excel(name = "*执行标准", width = 20)
    private String executeStandard;

    @ApiModelProperty(name = "安全技术类别")
    @Excel(name = "*安全技术类别", width = 20)
    private String saftyType;

    @ApiModelProperty(name = "材料成分")
    @Excel(name = "*材料成分", width = 20)
    private String ingredient;

    @ApiModelProperty(name = "颜色代码翻译")
    @Excel(name = "*颜色代码翻译", width = 20)
    private String colorCodeTranslate;

    @ApiModelProperty(name = "原产地")
    @Excel(name = "*原产地", width = 20)
    private String producer;

    @ApiModelProperty(name = "洗标编码")
    @Excel(name = "*洗标编码", width = 20)
    private String washingCode;

}
