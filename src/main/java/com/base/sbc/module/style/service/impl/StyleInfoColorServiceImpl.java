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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.dto.PackInfoDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackProcessPrice;
import com.base.sbc.module.pack.service.PackBomColorService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackProcessPriceVo;
import com.base.sbc.module.style.dto.StyleInfoColorDto;
import com.base.sbc.module.style.dto.StyleSaveDto;
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
import java.util.ArrayList;
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

    @Resource
    private PackBomColorService packBomColorService;

    @Resource
    private PackInfoService packInfoService;

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
        if (StrUtil.isNotBlank(styleInfoColorDto.getPackType())) {
            qw.like("pack_type" , styleInfoColorDto.getPackType());
        }
        Page<StyleInfoColorVo> page = PageHelper.startPage(styleInfoColorDto);
        list(qw);
        PageInfo<StyleInfoColorVo> pageInfo = page.toPageInfo();
        return pageInfo;
    }

    /**
     * 根据颜色code删除款式设计详情颜色
     * @param styleInfoColorDto 式设计详情颜色DTO
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public void delStyleInfoColorById(StyleInfoColorDto styleInfoColorDto) {
        List<String> codeList = StringUtils.convertList(styleInfoColorDto.getCodes());
        // 查找数据是否存在
        List<StyleInfoColor> styleInfoColors = baseMapper.selectList(new QueryWrapper<StyleInfoColor>().in("color_code", codeList)
                .eq("company_code", styleInfoColorDto.getCompanyCode()).eq("foreign_id", styleInfoColorDto.getForeignId()));
        if (CollectionUtil.isEmpty(styleInfoColors)) {
            throw new OtherException(styleInfoColorDto.getCodes() + "未找到数据，删除失败");
        }
        if (null != styleInfoColorDto.getPackType() && PackUtils.PACK_TYPE_BIG_GOODS.equals(styleInfoColorDto.getPackType())) {
            // 删除颜色对应的SKU数据
            styleInfoColors.forEach( styleInfoColor -> {
                if (StringUtils.isNotEmpty(styleInfoColor.getColorCode()) && StringUtils.isNotEmpty(styleInfoColor.getForeignId())) {
                    QueryWrapper<StyleInfoSku> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("color_code", styleInfoColor.getColorCode());
                    queryWrapper.eq("company_code", styleInfoColorDto.getCompanyCode());
                    queryWrapper.eq("foreign_id", styleInfoColor.getForeignId());
                    styleInfoSkuService.remove(queryWrapper);
                }
            });
        }
        // 查找未删除的颜色数据，拼接数据修改款式设计里的颜色code、颜色名称集合
        QueryWrapper<StyleInfoColor> qwColor = new QueryWrapper<>();
        qwColor.notIn("color_code", codeList);
        qwColor.eq("company_code", styleInfoColorDto.getCompanyCode());
        qwColor.eq("foreign_id", styleInfoColorDto.getForeignId());
        qwColor.select("id","color_code","color_name", "foreign_id");
        List<StyleInfoColor> styleInfoColorList = baseMapper.selectList(qwColor);
        Style style = new Style();
        style.setId(styleInfoColorDto.getStyleId());
        if (CollectionUtil.isNotEmpty(styleInfoColorList)) {
            // 颜色code集合逗号分隔
            String colorCodes = styleInfoColorList.stream().map(StyleInfoColor::getColorCode).collect(Collectors.joining(BaseGlobal.D));
            // 颜色名称集合逗号分隔
            String productColors = styleInfoColorList.stream().map(StyleInfoColor::getColorName).collect(Collectors.joining(BaseGlobal.D));
            style.setColorCodes(colorCodes);
            style.setProductColors(productColors);
            styleService.updateById(style);
        } else {
            // 删除所有数据 把款式设计里的颜色清空
            LambdaUpdateWrapper<Style> lambdaUpdate = Wrappers.lambdaUpdate();
            lambdaUpdate.eq(Style::getId, styleInfoColorDto.getStyleId());
            lambdaUpdate.set(Style::getColorCodes, null);
            lambdaUpdate.set(Style::getProductColors, null);
            style.updateInit();
            styleService.update(style,lambdaUpdate);
        }

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


    /**
     * 添加款式设计详情颜色
     * @param packInfoDto 资料包dto
     */
    @Override
    public void insertStyleInfoColorList(PackInfoDto packInfoDto) {
        if(null == packInfoDto || null == packInfoDto.getId()){
            return;
        }
        // 如果来源资料包类型是：款式设计 - packStyle， 款式设计详情颜色里主ID 存的是款式表id
        String styleId = StrUtil.equals(packInfoDto.getSourcePackType(), PackUtils.PACK_TYPE_STYLE)
                ? packInfoDto.getId() : BaseGlobal.H;
        // 如果不是来源资料包类型不是款式设计 - packStyle，则查出资料包信息，取出款式id
        if (!StrUtil.equals(packInfoDto.getSourcePackType(), PackUtils.PACK_TYPE_STYLE)) {
            PackInfo packInfo = packInfoService.getById(packInfoDto.getId());
            if (null == packInfo) {
                throw new OtherException(packInfoDto.getId() + "资料包信息未找到!!!");
            }
            styleId = packInfo.getForeignId();
        }
        Style styleDto = styleService.getById(styleId);
        if (null == styleDto) {
            throw new OtherException(packInfoDto.getForeignId() + "款式信息未找到!!!");
        }
        QueryWrapper delWrapper = new QueryWrapper<>();
        delWrapper.eq("company_code", this.getCompanyCode());
        delWrapper.eq("foreign_id", packInfoDto.getTargetForeignId());
        delWrapper.eq("pack_type", packInfoDto.getTargetPackType());
        // 删除款式设计详情颜色
        this.remove(delWrapper);
        // 删除款式设计详情SKU
        styleInfoSkuService.remove(delWrapper);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", this.getCompanyCode());
        queryWrapper.eq("foreign_id", packInfoDto.getId());
        queryWrapper.eq("pack_type", packInfoDto.getSourcePackType());
        List<StyleInfoColor> styleInfoColorList = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(styleInfoColorList)) {
            return;
        }
        for (StyleInfoColor styleInfoColor : styleInfoColorList) {
            styleInfoColor.setId(null);
            styleInfoColor.setForeignId(packInfoDto.getTargetForeignId());
            styleInfoColor.setPackType(packInfoDto.getTargetPackType());
            styleInfoColor.insertInit();
        }
        // 保存款式设计详情颜色
        boolean flg = this.saveBatch(styleInfoColorList);
        if (!flg) {
            throw new OtherException("新增大货款式设计详情颜色失败，请联系管理员");
        }
        if (null != styleDto.getSizeCodes() && null != styleDto.getProductSizes()) {
            // 保存款式设计SKU
            List<StyleInfoSku> styleInfoSkuList = new ArrayList<>();
            // 拼接款式SKU数据： 颜色多个尺码
            styleInfoColorList.forEach(styleInfoColor -> {
                // 取款式尺码编码
                List<String> sizeCodeList = com.base.sbc.config.utils.StringUtils.convertList(styleDto.getSizeCodes());
                // 取除款式尺码
                List<String> productSizeList = com.base.sbc.config.utils.StringUtils.convertList(styleDto.getProductSizes());
                // 拼接款式SKU ：颜色code + 尺码code
                for (int i = 0; i < sizeCodeList.size(); i++) {
                    // 尺码code
                    String sizeCode = sizeCodeList.get(i);
                    // 尺码名称
                    String sizeName =  null != productSizeList.get(i) ? productSizeList.get(i) : "";
                    // SKU ：颜色code + - + 尺码code
                    String skuCode = styleDto.getDesignNo() + styleInfoColor.getColorCode() + sizeCode;
                    StyleInfoSku styleInfoSku = new StyleInfoSku();
                    styleInfoSku.setForeignId(styleInfoColor.getForeignId());
                    styleInfoSku.setPackType(styleInfoColor.getPackType());
                    styleInfoSku.setSkuCode(skuCode);
                    styleInfoSku.setColorCode(styleInfoColor.getColorCode());
                    styleInfoSku.setColorName(styleInfoColor.getColorName());
                    styleInfoSku.setSizeCode(sizeCode);
                    styleInfoSku.setSizeName(sizeName);
                    styleInfoSku.insertInit();
                    styleInfoSkuList.add(styleInfoSku);
                }
            });
            // 保存款式SKU数据
            boolean skuFlg = styleInfoSkuService.saveBatch(styleInfoSkuList);
            if (!skuFlg) {
                throw new OtherException("新增款式SKU失败，请联系管理员");
            }
        }

    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
