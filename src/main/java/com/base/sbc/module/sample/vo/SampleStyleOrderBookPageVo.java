package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 订货本列表返回数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月10日
 */
@Data
public class SampleStyleOrderBookPageVo {
	@ApiModelProperty(value = "id")
	private String id;
	@ApiModelProperty(value = "明细Id")
	private String itemId;
	@ApiModelProperty(value = "订货本编号")
	private String orderBookCode;
	@ApiModelProperty(value = "款号")
	private String styleNo;
	@ApiModelProperty(value = "款式图片")
	private String sampleDesignPic;
	@ApiModelProperty(value = "品牌编码")
	private String bandCode;
	@ApiModelProperty(value = "颜色名称")
	private String colorName;
	@ApiModelProperty(value = "是否搭配")
	private String groupFlag;
	@ApiModelProperty(value = "搭配编码")
	private String groupCode;
	@ApiModelProperty(value = "订货本图片")
	private String imageUrl;
	@ApiModelProperty(value = "上会状态（0下会 1上会）")
	private String meetFlag;
	@ApiModelProperty(value = "锁定状态（0未锁 1锁定）")
	private String lockFlag;
}
