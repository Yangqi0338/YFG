/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialOld;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialPriceDetailMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPriceDetail;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：基础资料-物料档案-供应商报价- service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-27 17:53:42
 * @version 1.0
 */
@Service
public class BasicsdatumMaterialPriceDetailServiceImpl extends ServiceImpl<BasicsdatumMaterialPriceDetailMapper, BasicsdatumMaterialPriceDetail> implements BasicsdatumMaterialPriceDetailService {

    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void copyByMaterialCode(String materialCode, String newMaterialCode) {
        QueryWrapper<BasicsdatumMaterialPriceDetail> qw = new QueryWrapper<>();
        qw.eq("material_code", materialCode);
        List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialOlds = super.list(qw);
        if (CollectionUtils.isNotEmpty(basicsdatumMaterialOlds)) {
            basicsdatumMaterialOlds.forEach(e -> {
                e.setId(null);
                e.setMaterialCode(newMaterialCode);
            });
            super.saveBatch(basicsdatumMaterialOlds);
        }
    }


// 自定义方法区 不替换的区域【other_end】

}

