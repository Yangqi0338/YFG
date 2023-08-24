/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.ColorPlanningItemSaveDTO;
import com.base.sbc.module.planning.entity.ColorPlanningItem;
import com.base.sbc.module.planning.mapper.ColorPlanningItemMapper;
import com.base.sbc.module.planning.service.ColorPlanningItemService;
import com.base.sbc.module.planning.vo.ColorPlanningItemVO;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：颜色企划明细 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ColorPlanningItemService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:55
 */
@Service
public class ColorPlanningItemServiceImpl extends BaseServiceImpl<ColorPlanningItemMapper, ColorPlanningItem> implements ColorPlanningItemService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(ColorPlanningItemService.class);

    @Override
    public void colorPlanningItemSave(List<ColorPlanningItemSaveDTO> colorPlanningItemSaves, String colorPlanningId) {
        logger.info("ColorPlanningItemService#colorPlanningItemSave 保存 colorPlanningItemSaves:{}, colorPlanningId:{}",
                JSON.toJSONString(colorPlanningItemSaves), colorPlanningId);

        if (CollectionUtils.isEmpty(colorPlanningItemSaves)) {
            LambdaQueryWrapper<ColorPlanningItem> queryWrapper = new QueryWrapper<ColorPlanningItem>()
                    .lambda()
                    .eq(ColorPlanningItem::getColorPlanningId, colorPlanningId)
                    .eq(ColorPlanningItem::getDelFlag, "0");
            super.getBaseMapper().delete(queryWrapper);
            return;
        }

        List<String> ids = this.getIdByColorPlanningId(colorPlanningId);
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        List<ColorPlanningItem> colorPlanningItems = colorPlanningItemSaves.stream()
                .map(e -> {
                    ColorPlanningItem colorPlanningItem = CopyUtil.copy(e, ColorPlanningItem.class);
                    if (StringUtils.isEmpty(colorPlanningItem.getId())) {
                        colorPlanningItem.setId(idGen.nextIdStr());
                        colorPlanningItem.setCompanyCode(companyCode);
                        colorPlanningItem.insertInit();
                    } else {
                        colorPlanningItem.updateInit();
                    }
                    colorPlanningItem.setColorPlanningId(colorPlanningId);
                    ids.remove(colorPlanningItem.getId());
                    return colorPlanningItem;
                }).collect(Collectors.toList());

        super.saveOrUpdateBatch(colorPlanningItems);
        if (CollectionUtils.isNotEmpty(ids)) {
            super.getBaseMapper().deleteBatchIds(ids);
        }
    }

    @Override
    public List<ColorPlanningItemVO> getBYColorPlanningId(String colorPlanningId) {
        return super.getBaseMapper().getBYColorPlanningId(colorPlanningId);
    }

    private List<String> getIdByColorPlanningId(String colorPlanningId) {
        LambdaQueryWrapper<ColorPlanningItem> queryWrapper = new QueryWrapper<ColorPlanningItem>()
                .lambda()
                .eq(ColorPlanningItem::getColorPlanningId, colorPlanningId)
                .eq(ColorPlanningItem::getDelFlag, "0")
                .select(ColorPlanningItem::getId);
        List<ColorPlanningItem> colorPlanningItems = super.getBaseMapper().selectList(queryWrapper);
        return CollectionUtils.isEmpty(colorPlanningItems) ? Lists.newArrayList() : colorPlanningItems.stream()
                .map(ColorPlanningItem::getId)
                .collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}

