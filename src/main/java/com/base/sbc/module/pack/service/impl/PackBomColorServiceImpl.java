/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.mapper.PackBomColorMapper;
import com.base.sbc.module.pack.entity.PackBomColor;
import com.base.sbc.module.pack.service.PackBomColorService;
import org.springframework.stereotype.Service;
/**
 * 类描述：资料包-物料清单-配色 service类
 * @address com.base.sbc.module.pack.service.PackBomColorService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-23 9:44:43
 * @version 1.0
 */
@Service
public class PackBomColorServiceImpl extends PackBaseServiceImpl<PackBomColorMapper, PackBomColor> implements PackBomColorService {
    @Override
    String getModeName() {
        return "物料颜色";
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

}

