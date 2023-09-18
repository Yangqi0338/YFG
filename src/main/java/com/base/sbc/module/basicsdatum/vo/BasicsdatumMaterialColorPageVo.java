package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 物料规格列表数据
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料规格列表查询")
public class BasicsdatumMaterialColorPageVo extends BaseDataEntity<String> {
	private static final long serialVersionUID = 1L;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 颜色代码 */
	@ApiModelProperty(value = "颜色代码")
	private String colorCode;
	/** 颜色代码 */
	@ApiModelProperty(value = "颜色")
	private String color;
	/** 颜色hex */
	@ApiModelProperty(value = "颜色hex")
	private String colorHex;
	/** 供应商色号 */
	@ApiModelProperty(value = "供应商色号")
	private String supplierColorCode;
	/** 颜色图片 */
	@ApiModelProperty(value = "颜色图片")
	private String picture;
	/**是否在报价引用*/
	private String quote;
}
