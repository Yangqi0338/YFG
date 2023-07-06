package com.base.sbc.selenium.style;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 存储尺寸的数据
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月5日
 */
@Data
public class StyleSizeItem {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "尺寸表编码" })
	private String sizeCode;
	@ExcelProperty(value = { "大货款号" })
	private String orderStyle;
	@ExcelProperty(value = { "设计款号" })
	private String code;
	@ExcelProperty(value = { "尺码" })
	private String size;

	@ExcelProperty(value = { "部位" })
	private String part;
	@ExcelProperty(value = { "描述" })
	private String say;
	@ExcelProperty(value = { "尺寸" })
	private String sizeItem;
	@ExcelProperty(value = { "收缩" })
	private String shrink;
	@ExcelProperty(value = { "公差(-)" })
	private String toleranceReduction;
	@ExcelProperty(value = { "公差(+)" })
	private String tolerancePlus;

}
