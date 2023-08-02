package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年8月2日
 */
@Data
public class BasicsdatumMaterialOldPageVo {

	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 物料名称 */
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 材料 */
	@ApiModelProperty(value = "材料")
	private String materialCodeName;
	/** 大类名称 */
	@ApiModelProperty(value = "大类名称")
	private String category1Name;
	/** 中类名称 */
	@ApiModelProperty(value = "中类名称")
	private String category2Name;
	/** 小类名称 */
	@ApiModelProperty(value = "小类名称")
	private String category3Name;
	/** 类别名称第4级名称 */
	@ApiModelProperty(value = "类别名称第4级名称")
	private String categoryName;
	/** 图片地址 */
	@ApiModelProperty(value = "图片地址")
	private String imageUrl;
	/** 年份名称 */
	@ApiModelProperty(value = "年份名称")
	private String yearName;
	/** 季节名称 */
	@ApiModelProperty(value = "季节名称")
	private String seasonName;
	/** 面料成分 */
	@ApiModelProperty(value = "面料成分")
	private String ingredient;
}
