/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.entity.ThemePlanningImage;
import com.base.sbc.module.planning.mapper.ThemePlanningImageMapper;
import com.base.sbc.module.planning.service.ThemePlanningImageService;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：主题企划图片 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ThemePlanningImageService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:40
 */
@Service
public class ThemePlanningImageServiceImpl extends BaseServiceImpl<ThemePlanningImageMapper, ThemePlanningImage> implements ThemePlanningImageService {
    private static final Logger logger = LoggerFactory.getLogger(ThemePlanningImageService.class);

    @Override
    public List<String> getByThemePlanningId(String themePlanningId) {
        LambdaQueryWrapper<ThemePlanningImage> qw = new QueryWrapper<ThemePlanningImage>().lambda()
                .eq(ThemePlanningImage::getThemePlanningId, themePlanningId)
                .eq(ThemePlanningImage::getDelFlag, "0")
                .select(ThemePlanningImage::getImageUrl);
        List<ThemePlanningImage> list = super.list(qw);
        return CollectionUtils.isEmpty(list) ? Lists.newArrayList() : list.stream()
                .map(ThemePlanningImage::getImageUrl)
                .collect(Collectors.toList());
    }

    @Override
    public void save(List<String> images, String themePlanningId) {
        logger.info("ThemePlanningImageService#save 保存 images：{},themePlanningId:{}", images, themePlanningId);
        LambdaQueryWrapper<ThemePlanningImage> qw = new QueryWrapper<ThemePlanningImage>()
                .lambda()
                .eq(ThemePlanningImage::getThemePlanningId, themePlanningId)
                .eq(ThemePlanningImage::getDelFlag, "0");
        super.getBaseMapper().delete(qw);
        if (CollectionUtils.isEmpty(images)) {
            return;
        }
        IdGen idGen = new IdGen();
        List<ThemePlanningImage> themePlanningImages = images.stream()
                .map(e -> {
                    ThemePlanningImage themePlanningImage = new ThemePlanningImage();
                    themePlanningImage.setImageUrl(e);
                    themePlanningImage.insertInit();
                    themePlanningImage.setId(idGen.nextIdStr());
                    themePlanningImage.setThemePlanningId(themePlanningId);
                    return themePlanningImage;
                }).collect(Collectors.toList());
        super.saveBatch(themePlanningImages);
    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}

