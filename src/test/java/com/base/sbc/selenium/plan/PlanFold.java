package com.base.sbc.selenium.plan;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 企划文件夹 第一级
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年5月22日
 */
@Data
public class PlanFold {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "品牌" })
	private String brand;
	@ExcelProperty(value = { "版本" })
	private String version;
	@ExcelProperty(value = { "文件夹" })
	private String fold;
	@ExcelProperty(value = { "文件夹路径" })
	private String foldUrl;
	@ExcelProperty(value = { "产品类型" })
	private String productType;
	@ExcelProperty(value = { "款式大类" })
	private String bigType;
	@ExcelProperty(value = { "品类" })
	private String type;
	@ExcelProperty(value = { "品类规划SKU数" })
	private String skuNum;
}
