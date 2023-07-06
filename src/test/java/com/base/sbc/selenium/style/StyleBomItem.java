package com.base.sbc.selenium.style;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class StyleBomItem {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "款式Bom编码" })
	private String bomCode;
	@ExcelProperty(value = { "大货款号" })
	private String orderStyle;
	@ExcelProperty(value = { "设计款号" })
	private String code;
	@ExcelProperty(value = { "大货生产/样品" })
	private String type;
	@ExcelProperty(value = { "配色" })
	private String color;

	@ExcelProperty(value = { "发送状态" })
	private String sendStatus;
	@ExcelProperty(value = { "搭配" })
	private String group;
	@ExcelProperty(value = { "主材料" })
	private String mainFabric;
	@ExcelProperty(value = { "材料" })
	private String fabric;
	@ExcelProperty(value = { "材料路径" })
	private String fabricUrl;
	@ExcelProperty(value = { "材料名称" })
	private String fabricName;
	@ExcelProperty(value = { "厂家成分" })
	private String factoryComposition;
	@ExcelProperty(value = { "成分" })
	private String composition;

	@ExcelProperty(value = { "供应商报价" })
	private String supplierPrice;
	@ExcelProperty(value = { "供应商报价路径" })
	private String supplierPriceUrl;
	@ExcelProperty(value = { "单价" })
	private String unitPrice;
	@ExcelProperty(value = { "使用部位" })
	private String part;
	@ExcelProperty(value = { "单位" })
	private String unit;
	@ExcelProperty(value = { "辅料材质" })
	private String auxiliaryMaterial;
	@ExcelProperty(value = { "颜色(通用)" })
	private String colorAll;
	@ExcelProperty(value = { "厂家有效门幅/规格(通用)" })
	private String sizeAll;

	@ExcelProperty(value = { "单件用量" })
	private String itemUsage;
	@ExcelProperty(value = { "损耗%" })
	private String loss;
	@ExcelProperty(value = { "成本" })
	private String cost;
	@ExcelProperty(value = { "颜色名" })
	private String colorName;
	@ExcelProperty(value = { "颜色值" })
	private String colorValue;
	@ExcelProperty(value = { "规格名" })
	private String sizeName;
	@ExcelProperty(value = { "规格值" })
	private String sizeValue;

}
