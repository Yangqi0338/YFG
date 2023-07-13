/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackInfoStatusMapper;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：资料包-状态 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoStatusService
 * @email your email
 * @date 创建时间：2023-7-13 9:17:47
 */
@Service
public class PackInfoStatusServiceImpl extends PackBaseServiceImpl<PackInfoStatusMapper, PackInfoStatus> implements PackInfoStatusService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackInfoStatus newStatus(String foreignId, String packType) {
        PackInfoStatus pack = new PackInfoStatus();
        pack.setForeignId(foreignId);
        pack.setPackType(packType);
        pack.setBomStatus(BasicNumber.ZERO.getNumber());
        save(pack);
        return pack;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackInfoStatus get(String foreignId, String packType) {
        QueryWrapper<PackInfoStatus> qw = new QueryWrapper<>();
        qw.eq("foreign_id", foreignId);
        qw.eq("pack_type", packType);
        qw.last("limit 1");
        PackInfoStatus one = getOne(qw);
        if (one != null) {
            return one;
        }
        return newStatus(foreignId, packType);
    }

    @Override
    String getModeName() {
        return "资料包";
    }

// 自定义方法区 不替换的区域【other_end】

}

