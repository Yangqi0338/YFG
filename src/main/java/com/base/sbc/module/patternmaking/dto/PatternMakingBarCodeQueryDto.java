/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;
/**
 * 类描述：QueryDto 实体类
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingBarCodeQueryDto
 * @author your name
 * @email your email
 * @date 创建时间：2024-7-3 15:56:26
 * @version 1.0
 */
@Data
public class PatternMakingBarCodeQueryDto extends Page {

	private static final long serialVersionUID = 1L;

	private String designNo;
	private String sampleTypeName;
	private String yearName;
	private String seasonName;
	private String brandName;
	private String barCode;
}
