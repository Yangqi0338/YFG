/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialWidthMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：基础资料-物料档案-物料规格 service类
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:19
 */
@Service
public class BasicsdatumMaterialWidthServiceImpl
        extends BaseServiceImpl<BasicsdatumMaterialWidthMapper, BasicsdatumMaterialWidth>
        implements BasicsdatumMaterialWidthService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<BomSelMaterialVo> findDefaultToBomSel(List<String> materialCodeList) {
        QueryWrapper<BasicsdatumMaterialWidth> qw = new QueryWrapper<>();
        qw.in("material_code", materialCodeList);
        qw.eq("company_code", getCompanyCode());
        return getBaseMapper().findDefaultToBomSel(qw);
    }

    @Override
    public void copyByMaterialCode(String materialCode, String newMaterialCode) {
        QueryWrapper<BasicsdatumMaterialWidth> qw = new QueryWrapper<>();
        qw.eq("material_code", materialCode);
        qw.eq("company_code", getCompanyCode());
        List<BasicsdatumMaterialWidth> basicsdatumMaterialOlds = super.list(qw);
        if (CollectionUtils.isNotEmpty(basicsdatumMaterialOlds)) {
            basicsdatumMaterialOlds.forEach(e -> {
                e.insertInit();
                e.setUpdateId(null);
                e.setUpdateName(null);
                e.setUpdateDate(null);
                e.setId(null);
                e.setMaterialCode(newMaterialCode);
            });
            super.saveBatch(basicsdatumMaterialOlds);
        }
    }

    @Override
    public void updateMaterialCode(String oldMaterialCode, String newMaterialCode) {
        LambdaUpdateWrapper<BasicsdatumMaterialWidth> updateWrapper = new UpdateWrapper<BasicsdatumMaterialWidth>()
                .lambda()
                .eq(BasicsdatumMaterialWidth::getMaterialCode, oldMaterialCode)
                .set(BasicsdatumMaterialWidth::getMaterialCode, newMaterialCode);
        super.update(updateWrapper);
    }

// 自定义方法区 不替换的区域【other_end】

}

