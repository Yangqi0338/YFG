package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("打印配置查询")
public class QueryRevampConfigPrintDto extends Page {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 单据类别 */
	@ApiModelProperty(value = "单据类别")
	private String billType;
	/** 编码 */
	@ApiModelProperty(value = "编码")
	private String code;
	/** 名称 */
	@ApiModelProperty(value = "名称")
	private String name;
	/** 类型(ureport) */
//	@ApiModelProperty(value = "类型(ureport)")
//	private String printType;
	/** 是否默认选中(0否,1是) */
//	@ApiModelProperty(value = "是否默认选中(0否,1是)")
//	private String selectFlag;
	/** 是否停用(0否,1是) */
	@ApiModelProperty(value = "是否停用(0否,1是)")
	private String stopFlag;
//    /** 创建日期 */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @ApiModelProperty(value = "创建时间"  )
//    private String[] createDate;
//    /**  创建者名称 */
//    @ApiModelProperty(value = "创建人"  )
//    private String createName;
	/** 备注 */
	@ApiModelProperty(value = "备注")
	private String remarks;

}
