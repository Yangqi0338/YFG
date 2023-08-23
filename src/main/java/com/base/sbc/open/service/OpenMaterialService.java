/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.open.dto.OpenMaterialDto;

import java.util.List;


/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/15 10:06
 */
public interface OpenMaterialService extends BaseService<OpenMaterialDto> {

	List<OpenMaterialDto> getMaterialList(String companyCode);
}

