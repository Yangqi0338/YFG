package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 物料规格分页数据
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料规格分页查询")
public class BasicsdatumMaterialWidthPageVo extends BaseDataEntity<String> {
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
	/** 规格代码 */
	@ApiModelProperty(value = "规格代码")
	private String widthCode;
	/** 规格代码 */
	@ApiModelProperty(value = "规格名称")
	private String name;
	/**是否在报价引用*/
	private String quote;

	/**排序码*/
	private String code;
	/**
	 * 下发状态:0未下发,1已下发
	 */
	@ApiModelProperty(value = "下发状态:0未下发,1已下发")
	private String scmStatus;
}
