package com.base.sbc.module.basicsdatum.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 物料导出数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月30日
 */
@Data
@ApiModel("物料BOM档案导出数据")
public class BasicsdatumMaterialStyleExcelVo {

	/**
	 * 物料图片
	 */
	@Excel(name = "物料图片")
	private String materialsImageUrl;

	/**
	 * 物料颜色
	 */
	@Excel(name = "物料颜色")
	private String materialsColor;

	/**
	 * 物料规格
	 */
	@Excel(name = "物料规格")
	private String materialsSpec;

	/**
	 * 厂家简称
	 */
	@Excel(name = "厂家简称")
	private String supperSampleName;

	/**
	 * 款式图
	 */
	@Excel(name = "款式图")
	private String styleImageUrl;

	/**
	 * 产品季
	 */
	@Excel(name = "产品季")
	private String planningSeason;
	/**
	 * 大类
	 */
	@Excel(name = "大类")
	private String prodCategory1stName;
	/**
	 * 品类
	 */
	@Excel(name = "品类")
	private String prodCategoryName;
	/**
	 * 设计款号
	 */
	@Excel(name = "设计款号")
	private String designNo;
	/**
	 * 大货款号
	 */
	@Excel(name = "大货款号")
	private String bulkNo;
	/**
	 * 配色颜色
	 */
	@Excel(name = "配色颜色")
	private String styleColor;

	/**
	 * bom 阶段
	 */
	@Excel(name = "bom阶段")
	private String bomPhase;
}
