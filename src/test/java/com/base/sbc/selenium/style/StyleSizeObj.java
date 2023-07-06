package com.base.sbc.selenium.style;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class StyleSizeObj {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "尺寸表路径" })
	private String sizeUrl;
	@ExcelProperty(value = { "尺寸表编码" })
	private String sizeCode;
	@ExcelProperty(value = { "配色路径" })
	private String colorUrl;
	@ExcelProperty(value = { "配色名称" })
	private String color;

	@ExcelProperty(value = { "大货款号" })
	private String orderStyle;
	@ExcelProperty(value = { "设计款号" })
	private String code;
	@ExcelProperty(value = { "号型类型" })
	private String sizeType;
	@ExcelProperty(value = { "号型类型路径" })
	private String sizeTypeUrl;

	@ExcelProperty(value = { "尺码" })
	private String size;
	@ExcelProperty(value = { "基础尺码" })
	private String baseSize;

}
