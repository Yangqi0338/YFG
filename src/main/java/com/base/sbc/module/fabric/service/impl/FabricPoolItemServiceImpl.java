/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricPoolItemSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPoolItem;
import com.base.sbc.module.fabric.mapper.FabricPoolItemMapper;
import com.base.sbc.module.fabric.service.FabricPoolItemService;
import com.base.sbc.module.fabric.vo.FabricPoolItemVO;
import com.beust.jcommander.internal.Lists;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：面料池明细 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPoolItemService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:45
 */
@Service
public class FabricPoolItemServiceImpl extends BaseServiceImpl<FabricPoolItemMapper, FabricPoolItem> implements FabricPoolItemService {
    // 自定义方法区 不替换的区域【other_start】

    @Override
    public void saveItem(List<FabricPoolItemSaveDTO> dto, String fabricPoolId) {
        if (CollUtil.isEmpty(dto)) {
            LambdaQueryWrapper<FabricPoolItem> queryWrapper = new QueryWrapper<FabricPoolItem>()
                    .lambda()
                    .eq(FabricPoolItem::getFabricPoolId, fabricPoolId)
                    .eq(FabricPoolItem::getDelFlag, "0");
            super.getBaseMapper().delete(queryWrapper);
            return;
        }
        List<String> ids = this.getIdByFabricPoolId(fabricPoolId);
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        List<FabricPoolItem> fabricPoolItems = dto.stream()
                .map(e -> {
                    FabricPoolItem fabricPoolItem = CopyUtil.copy(e, FabricPoolItem.class);
                    if (StringUtils.isEmpty(fabricPoolItem.getId())) {
                        fabricPoolItem.setId(idGen.nextIdStr());
                        fabricPoolItem.setCompanyCode(companyCode);
                        fabricPoolItem.insertInit();
                    } else {
                        fabricPoolItem.updateInit();
                    }
                    fabricPoolItem.setFabricPoolId(fabricPoolId);
                    ids.remove(fabricPoolItem.getId());
                    return fabricPoolItem;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(fabricPoolItems);
        if (CollUtil.isNotEmpty(ids)) {
            super.getBaseMapper().deleteBatchIds(ids);
        }
    }

    @Override
    public List<FabricPoolItemVO> getByFabricPoolId(String fabricPoolId) {
        return super.getBaseMapper().getByFabricPoolId(fabricPoolId);
    }

    @Override
    public Map<String, List<String>> getSourceIdByFabricPlanningId(String fabricPlanningId) {
        List<FabricPoolItem> fabricPoolItems = super.getBaseMapper().getSourceIdByFabricPlanningId(fabricPlanningId);
        return CollUtil.isEmpty(fabricPoolItems) ?
                null :
                fabricPoolItems.stream()
                        .collect(Collectors.groupingBy(FabricPoolItem::getFabricPoolId, Collectors.mapping(FabricPoolItem::getSourceId, Collectors.toList())));
    }


    private List<String> getIdByFabricPoolId(String fabricPoolId) {
        LambdaQueryWrapper<FabricPoolItem> qw = new QueryWrapper<FabricPoolItem>().lambda()
                .eq(FabricPoolItem::getFabricPoolId, fabricPoolId)
                .eq(FabricPoolItem::getDelFlag, "0")
                .select(FabricPoolItem::getId);
        List<FabricPoolItem> list = super.list(qw);
        return CollUtil.isEmpty(list) ? Lists.newArrayList() : list.stream()
                .map(FabricPoolItem::getId)
                .collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}

