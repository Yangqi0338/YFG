package com.base.sbc.selenium.style;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class Style {
	@ExcelProperty(value = { "设计款号" })
	private String code;
	@ExcelProperty(value = { "搜索数量" })
	private Integer searchNum;
	@ExcelProperty(value = { "路径" })
	private String url;
	@ExcelProperty(value = { "BOM数量" })
	private Integer bomNum;
	@ExcelProperty(value = { "尺寸数量" })
	private Integer sizeNum;
}
