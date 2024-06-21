/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formtype.dto.FieldBusinessSystemQueryDto;
import com.base.sbc.module.formtype.entity.FieldBusinessSystem;
import com.base.sbc.module.formtype.vo.FieldBusinessSystemVo;

import java.util.List;

/**
 * 类描述：字段对应下游系统关系 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.formtype.service.FieldBusinessSystemService
 * @email your email
 * @date 创建时间：2024-5-31 10:35:28
 */
public interface FieldBusinessSystemService extends BaseService<FieldBusinessSystem> {

    List<FieldBusinessSystemVo> findList(FieldBusinessSystemQueryDto dto);

}