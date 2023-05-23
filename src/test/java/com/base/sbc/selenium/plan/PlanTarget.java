package com.base.sbc.selenium.plan;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 企划目标 第二级
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年5月22日
 */
@Data
public class PlanTarget {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "版本" })
	private String version;
	@ExcelProperty(value = { "买手系列" })
	private String series;
	@ExcelProperty(value = { "买手系列url" })
	private String seriesUrl;
	@ExcelProperty(value = { "月份" })
	private String month;
	@ExcelProperty(value = { "品类规划SKU数" })
	private String typeSku;
	@ExcelProperty(value = { "款色数(SKC)" })
	private String colorSkc;
	@ExcelProperty(value = { "每款SKC数" })
	private String modelSkc;
	@ExcelProperty(value = { "企划需求款数" })
	private String requireNum;
	@ExcelProperty(value = { "设计放量" })
	private String designNum;
	@ExcelProperty(value = { "计划开发数" })
	private String developmentNum;

}
