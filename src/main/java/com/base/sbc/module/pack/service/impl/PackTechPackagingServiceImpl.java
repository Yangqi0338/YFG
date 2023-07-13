/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.pack.entity.PackTechPackaging;
import com.base.sbc.module.pack.mapper.PackTechPackagingMapper;
import com.base.sbc.module.pack.service.PackTechPackagingService;
import org.springframework.stereotype.Service;

/**
 * 类描述：资料包-工艺说明-包装方式和体积重量 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechPackagingService
 * @email your email
 * @date 创建时间：2023-7-13 14:24:40
 */
@Service
public class PackTechPackagingServiceImpl extends PackBaseServiceImpl<PackTechPackagingMapper, PackTechPackaging> implements PackTechPackagingService {


// 自定义方法区 不替换的区域【other_start】


    @Override
    public PackTechPackaging savePackaging(PackTechPackaging packaging) {
        if (StringUtils.isAnyBlank(packaging.getPackType(), packaging.getForeignId())) {
            throw new OtherException("PackType、ForeignId必填");
        }
        return null;
    }

    @Override
    String getModeName() {
        return "包装方式和体积重量";
    }


// 自定义方法区 不替换的区域【other_end】

}
