package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;

import lombok.Data;

/**
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月4日
 */
@Data
public class SampleStyleOrderBookQueryDto extends Page {

	private static final long serialVersionUID = 1L;

	private String companyCode;
}
