/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.entity.StyleMainAccessories;
import com.base.sbc.module.style.mapper.StyleMainAccessoriesMapper;
import com.base.sbc.module.style.service.StyleMainAccessoriesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：款式-款式主款配饰表 service类
 * @address com.base.sbc.module.style.service.StyleMainAccessoriesService
 * @author mengfanjiang
 * @email xx@qq.com
 * @date 创建时间：2023-10-17 11:06:10
 * @version 1.0  
 */
@Service
public class StyleMainAccessoriesServiceImpl extends BaseServiceImpl<StyleMainAccessoriesMapper, StyleMainAccessories> implements StyleMainAccessoriesService {
    /**
     * 获取配饰下的主款配饰
     *
     * @param styleColorId
     * @param isTrim
     * @return
     */
    @Override
    public List<StyleMainAccessories> styleMainAccessoriesList(String styleColorId, String isTrim) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_color_id", styleColorId);
        queryWrapper.eq(StrUtil.isNotBlank(isTrim), "is_trim", isTrim);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取配饰下的主款配饰
     *
     * @param styleColorIds
     * @param isTrim
     * @return
     */
    @Override
    public List<StyleMainAccessories> styleMainAccessoriesListBatch(List<String> styleColorIds, String isTrim) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("style_color_id", styleColorIds);
        queryWrapper.eq(StrUtil.isNotBlank(isTrim), "is_trim", isTrim);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 清除主款或配饰
     *
     * @param styleColorId
     * @param isTrim
     * @return
     */
    @Override
    public Boolean delMainAccessories(String styleColorId, String isTrim) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_color_id", styleColorId);
        queryWrapper.eq("is_trim", isTrim);
        baseMapper.delete(queryWrapper);
        return true;
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
