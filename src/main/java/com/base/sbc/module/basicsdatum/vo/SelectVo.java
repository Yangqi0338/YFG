package com.base.sbc.module.basicsdatum.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SelectVo {
	/**
	 * 供应商名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 简称
	 */
	private String supplierAbbreviation;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private String createDate;
}
