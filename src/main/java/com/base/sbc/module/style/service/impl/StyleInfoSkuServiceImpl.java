/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.dto.StyleInfoSkuDto;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.mapper.StyleInfoSkuMapper;
import com.base.sbc.module.style.entity.StyleInfoSku;
import com.base.sbc.module.style.service.StyleInfoSkuService;
import com.base.sbc.module.style.vo.StyleInfoColorVo;
import com.base.sbc.module.style.vo.StyleInfoSkuVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
/** 
 * 类描述：款式设计SKU表 service类
 * @address com.base.sbc.module.style.service.StyleInfoSkuService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:34
 * @version 1.0  
 */
@Service
public class StyleInfoSkuServiceImpl extends BaseServiceImpl<StyleInfoSkuMapper, StyleInfoSku> implements StyleInfoSkuService {

    /**
     * 根据id修改款式设计SKU
     * @param styleInfoSkuDto 款式设计SKU DTO
     */
    @Override
    public void updateStyleInfoSkuById(StyleInfoSkuDto styleInfoSkuDto) {
        StyleInfoSku styleInfoSkuInfo = baseMapper.selectById(styleInfoSkuDto.getId());
        if (null == styleInfoSkuInfo) {
            throw new OtherException(styleInfoSkuDto.getColorName() + "此数据未找到，请重试");
        }
        StyleInfoSku styleInfoSku = BeanUtil.copyProperties(styleInfoSkuDto, StyleInfoSku.class);
        styleInfoSku.updateInit();
        baseMapper.updateById(styleInfoSku);
    }

    /**
     * 分页查询 款式设计SKU
     * @param styleInfoSkuDto 款式设计SKU DTO
     * @return 分页款式设计SKU
     */
    @Override
    public PageInfo<StyleInfoSkuVo> pageList(StyleInfoSkuDto styleInfoSkuDto) {
        QueryWrapper<StyleInfoSku> qw = new QueryWrapper();
        qw.eq("foreign_id", styleInfoSkuDto.getForeignId());
        if (StrUtil.isBlank(styleInfoSkuDto.getOrderBy())) {
            qw.orderByDesc("id");
        }
        if (StrUtil.isNotBlank(styleInfoSkuDto.getColorName())) {
            qw.like("color_name" , styleInfoSkuDto.getColorName());
        }
        if (StrUtil.isNotBlank(styleInfoSkuDto.getColorCode())) {
            qw.like("color_code" , styleInfoSkuDto.getColorCode());
        }
        Page<StyleInfoSkuVo> page = PageHelper.startPage(styleInfoSkuDto);
        list(qw);
        PageInfo<StyleInfoSkuVo> pageInfo = page.toPageInfo();
        return pageInfo;
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
