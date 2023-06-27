package com.base.sbc.selenium.plan;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 企划计划 第三级
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年5月22日
 */
@Data
public class PlanPlan {
	@ExcelProperty(value = { "浏览器路径" })
	private String url;
	@ExcelProperty(value = { "版本" })
	private String version;
	@ExcelProperty(value = { "产品" })
	private String product;
	@ExcelProperty(value = { "产品url" })
	private String productUrl;
	@ExcelProperty(value = { "品类" })
	private String type;
	@ExcelProperty(value = { "中类" })
	private String centerType;
	@ExcelProperty(value = { "淘宝分类" })
	private String taoBaoType;
	@ExcelProperty(value = { "波段" })
	private String band;
	@ExcelProperty(value = { "款式" })
	private String style;
	@ExcelProperty(value = { "款式url" })
	private String styleUrl;
	@ExcelProperty(value = { "目标销价" })
	private String targetPrice;
	@ExcelProperty(value = { "企划目标倍率" })
	private String targetMultiplier;
	@ExcelProperty(value = { "目标成本" })
	private String targetCost;
	@ExcelProperty(value = { "参考款式" })
	private String refStyle;
	@ExcelProperty(value = { "参考款号" })
	private String refStyleNo;
	@ExcelProperty(value = { "参考款式Images" })
	private String refStyleImg;
	@ExcelProperty(value = { "参考样衣" })
	private String refCloth;
	@ExcelProperty(value = { "图片" })
	private String img;
	@ExcelProperty(value = { "参考材料" })
	private String refMaterial;
	@ExcelProperty(value = { "参考材料Images" })
	private String refMaterialImg;

}
