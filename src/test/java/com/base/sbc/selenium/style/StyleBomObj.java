package com.base.sbc.selenium.style;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class StyleBomObj {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "款式Bom路径" })
	private String bomUrl;
	@ExcelProperty(value = { "款式Bom编码" })
	private String bomCode;
	@ExcelProperty(value = { "描述" })
	private String say;
	@ExcelProperty(value = { "配色" })
	private String color;
	@ExcelProperty(value = { "配色路径" })
	private String colorUrl;
	@ExcelProperty(value = { "大货款号" })
	private String orderStyle;
	@ExcelProperty(value = { "设计款号" })
	private String code;
	@ExcelProperty(value = { "品名" })
	private String productName;
	@ExcelProperty(value = { "阶段" })
	private String stage;
	@ExcelProperty(value = { "设计编号(改版)" })
	private String codeOld;
	@ExcelProperty(value = { "唛类信息" })
	private String markingInformation;
	@ExcelProperty(value = { "特别注意	" })
	private String particularAttention;
	@ExcelProperty(value = { "特殊工艺备注" })
	private String specialProcess;

}
