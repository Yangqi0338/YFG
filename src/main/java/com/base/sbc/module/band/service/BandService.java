/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.band.service;

import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 类描述： service类
 *
 * @author 卞康
 * @date 创建时间：2023-3-17 18:08:53
 */
@Service
public interface BandService extends BaseService<Band> {


    String getNameByCode(String bandCode);

    Map<String, String> getNamesByCodes(String bandCodes);
}
