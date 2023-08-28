/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.PackProcessPrice;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackProcessPriceVo;
import com.base.sbc.module.style.dto.StyleInfoColorDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleInfoSku;
import com.base.sbc.module.style.mapper.StyleInfoColorMapper;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.base.sbc.module.style.service.StyleInfoSkuService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleInfoColorVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：款式设计详情颜色表 service类
 * @address com.base.sbc.module.style.service.StyleInfoColorService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:29
 * @version 1.0  
 */
@Service
public class StyleInfoColorServiceImpl extends BaseServiceImpl<StyleInfoColorMapper, StyleInfoColor> implements StyleInfoColorService {

    @Resource
    private StyleInfoSkuService styleInfoSkuService;
    @Resource
    private StyleService styleService;

    @Override
    public PageInfo<StyleInfoColorVo> pageList(StyleInfoColorDto styleInfoColorDto) {
        QueryWrapper<StyleInfoColor> qw = new QueryWrapper();
        qw.eq("foreign_id", styleInfoColorDto.getForeignId());
        if (StrUtil.isBlank(styleInfoColorDto.getOrderBy())) {
            qw.orderByDesc("id");
        }
        if (StrUtil.isNotBlank(styleInfoColorDto.getColorName())) {
            qw.like("color_name" , styleInfoColorDto.getColorName());
        }
        if (StrUtil.isNotBlank(styleInfoColorDto.getColorCode())) {
            qw.like("color_code" , styleInfoColorDto.getColorCode());
        }
        Page<StyleInfoColor> page = PageHelper.startPage(styleInfoColorDto);
        list(qw);
        PageInfo<StyleInfoColor> pageInfo = page.toPageInfo();
        PageInfo<StyleInfoColorVo> voPageInfo = CopyUtil.copy(pageInfo, StyleInfoColorVo.class);
        return voPageInfo;
    }

    /**
     * 根据id删除款式设计详情颜色
     * @param codes 款式设计详情颜色id
     * @param companyCode 公司编码
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public void delStyleInfoColorById(String codes, String companyCode) {
        List<String> codeList = StringUtils.convertList(codes);
        // 查找数据是否存在
        List<StyleInfoColor> styleInfoColors = baseMapper.selectList(new QueryWrapper<StyleInfoColor>().in("color_code", codeList)
                .eq("company_code", companyCode));
        if (CollectionUtil.isEmpty(styleInfoColors)) {
            throw new OtherException(codes + "未找到数据，删除失败");
        }
        // 删除颜色对应的SKU数据
        styleInfoColors.forEach( styleInfoColor -> {
            if (StringUtils.isNotEmpty(styleInfoColor.getColorCode()) && StringUtils.isNotEmpty(styleInfoColor.getForeignId())) {
                QueryWrapper<StyleInfoSku> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("color_code", styleInfoColor.getColorCode());
                queryWrapper.eq("company_code", companyCode);
                queryWrapper.eq("foreign_id", styleInfoColor.getForeignId());
                styleInfoSkuService.remove(queryWrapper);
            }
        });
        // 查找未删除的颜色数据，拼接数据修改款式设计里的颜色code、颜色名称集合
        QueryWrapper<StyleInfoColor> qwColor = new QueryWrapper<>();
        qwColor.notIn("color_code", codeList);
        qwColor.eq("company_code", companyCode);
        qwColor.eq("foreign_id", styleInfoColors.get(BaseGlobal.ZERO).getForeignId());
        qwColor.select("id","color_code","color_name", "foreign_id");
        List<StyleInfoColor> styleInfoColorList = baseMapper.selectList(qwColor);
        Style style = new Style();
        style.setId(styleInfoColors.get(BaseGlobal.ZERO).getForeignId());
        if (CollectionUtil.isNotEmpty(styleInfoColorList)) {
            // 颜色code集合逗号分隔
            String colorCodes = styleInfoColorList.stream().map(StyleInfoColor::getColorCode).collect(Collectors.joining(BaseGlobal.D));
            // 颜色名称集合逗号分隔
            String productColors = styleInfoColorList.stream().map(StyleInfoColor::getColorName).collect(Collectors.joining(BaseGlobal.D));
            style.setColorCodes(colorCodes);
            style.setProductColors(productColors);
        } else {
            style.setColorCodes(BaseGlobal.H);
            style.setProductColors(BaseGlobal.H);
        }
        styleService.updateById(style);
        // 删除相关数据
        baseMapper.deleteBatchIds(styleInfoColors.stream().map(StyleInfoColor::getId).collect(Collectors.toList()));

    }

    /**
     * 根据id修改款式设计详情颜色
     * @param styleInfoColorDto 款式设计详情颜色DTO
     */
    @Override
    public void updateStyleInfoColorById(StyleInfoColorDto styleInfoColorDto) {
        StyleInfoColor styleInfoColorInfo = baseMapper.selectById(styleInfoColorDto.getId());
        if (null == styleInfoColorInfo) {
            throw new OtherException(styleInfoColorDto.getId() + "删除颜色数据未找到！！！");
        }
        StyleInfoColor styleInfoColor = BeanUtil.copyProperties(styleInfoColorDto, StyleInfoColor.class);
        styleInfoColor.updateInit();
        baseMapper.updateById(styleInfoColor);
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
