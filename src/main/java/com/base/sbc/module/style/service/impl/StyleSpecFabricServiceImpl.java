/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.dto.StyleSpecFabricDto;
import com.base.sbc.module.style.entity.StyleSpecFabric;
import com.base.sbc.module.style.mapper.StyleSpecFabricMapper;
import com.base.sbc.module.style.service.StyleSpecFabricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：款式BOM指定面料表 service类
 * @address com.base.sbc.module.style.service.StyleSpecFabricService
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-25 16:04:37
 * @version 1.0  
 */
@Service
public class StyleSpecFabricServiceImpl extends BaseServiceImpl<StyleSpecFabricMapper, StyleSpecFabric> implements StyleSpecFabricService {

    @Autowired
    @Lazy
    private SmpService smpService;

    @Transactional
    @Override
    public Boolean batchSaveAndClearHistoryData(List<StyleSpecFabricDto> styleSpecFabricDtoList) {
        String styleColorId = null;
        if (CollUtil.isNotEmpty(styleSpecFabricDtoList)) {
            List<StyleSpecFabric> styleSpecFabricList = BeanUtil.copyToList(styleSpecFabricDtoList, StyleSpecFabric.class);
            QueryWrapper<StyleSpecFabric> removeQueryWrapper = new QueryWrapper<>();
            styleColorId = styleSpecFabricDtoList.get(0).getStyleColorId();
            removeQueryWrapper.eq("style_color_id", styleColorId);
            //批量删除
            super.physicalDeleteQWrap(removeQueryWrapper);
            //批量保存
            super.saveBatch(styleSpecFabricList);
        }

        //下发配色到下游
        if (StrUtil.isNotEmpty(styleColorId)) {
            try {
                smpService.goods(new String[]{styleColorId});
            }catch (Exception e){

            }
        }
        return true;
    }
}

