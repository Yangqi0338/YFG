/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.pack.entity.PackSizeDetail;
import com.base.sbc.module.pack.mapper.PackSizeDetailMapper;
import com.base.sbc.module.pack.service.PackSizeDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-尺寸表-明细 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeDetailService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-18 10:56:19
 */
@Service
public class PackSizeDetailServiceImpl extends AbstractPackBaseServiceImpl<PackSizeDetailMapper, PackSizeDetail> implements PackSizeDetailService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveSizeDetail(List<PackSizeDetail> sizeDetails) {
        if (CollUtil.isNotEmpty(sizeDetails)) {
            List<String> packSizeIds = sizeDetails.stream().map(PackSizeDetail::getPackSizeId).collect(Collectors.toList());
            QueryWrapper<PackSizeDetail> qw = new QueryWrapper<>();
            qw.lambda().in(PackSizeDetail::getPackSizeId, packSizeIds);
            remove(qw);
            saveBatch(sizeDetails);
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delBypackSizeIds(String id) {
        List<String> packSizeIds = StrUtil.split(id, CharUtil.COMMA);
        QueryWrapper<PackSizeDetail> qw = new QueryWrapper<>();
        qw.lambda().in(PackSizeDetail::getPackSizeId, packSizeIds);
        remove(qw);
    }

    @Override
    String getModeName() {
        return "尺码明细";
    }

// 自定义方法区 不替换的区域【other_end】

}
