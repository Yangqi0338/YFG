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
import com.base.sbc.module.fabric.dto.FabricPlanningItemSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPlanningItem;
import com.base.sbc.module.fabric.mapper.FabricPlanningItemMapper;
import com.base.sbc.module.fabric.service.FabricPlanningItemService;
import com.base.sbc.module.fabric.vo.FabricPlanningItemVO;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：面料企划明细 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPlanningItemService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:55
 */
@Service
public class FabricPlanningItemServiceImpl extends BaseServiceImpl<FabricPlanningItemMapper, FabricPlanningItem> implements FabricPlanningItemService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void saveItem(List<FabricPlanningItemSaveDTO> dto, String fabricPlanningId) {
        if (CollectionUtils.isEmpty(dto)) {
            LambdaQueryWrapper<FabricPlanningItem> queryWrapper = new QueryWrapper<FabricPlanningItem>()
                    .lambda()
                    .eq(FabricPlanningItem::getFabricPlanningId, fabricPlanningId)
                    .eq(FabricPlanningItem::getDelFlag, "0");
            super.getBaseMapper().delete(queryWrapper);
            return;
        }
        List<String> ids = this.getIdByFabricPlanningId(fabricPlanningId);
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        List<FabricPlanningItem> fabricPlanningItems = dto.stream()
                .map(e -> {
                    FabricPlanningItem fabricPlanningItem = CopyUtil.copy(e, FabricPlanningItem.class);
                    if (StringUtils.isEmpty(fabricPlanningItem.getId())) {
                        fabricPlanningItem.setId(idGen.nextIdStr());
                        fabricPlanningItem.setCompanyCode(companyCode);
                        fabricPlanningItem.insertInit();
                    } else {
                        fabricPlanningItem.updateInit();
                    }
                    fabricPlanningItem.setFabricPlanningId(fabricPlanningId);
                    ids.remove(fabricPlanningItem.getId());
                    return fabricPlanningItem;
                }).collect(Collectors.toList());

        super.saveOrUpdateBatch(fabricPlanningItems);
        if (CollectionUtils.isNotEmpty(ids)) {
            super.getBaseMapper().deleteBatchIds(ids);
        }
    }

    @Override
    public List<FabricPlanningItemVO> getByFabricPlanningId(String fabricPlanningId, String materialFlag) {
        return super.getBaseMapper().getByFabricPlanningId(fabricPlanningId, materialFlag);
    }

    private List<String> getIdByFabricPlanningId(String fabricPlanningId) {
        LambdaQueryWrapper<FabricPlanningItem> qw = new QueryWrapper<FabricPlanningItem>().lambda()
                .eq(FabricPlanningItem::getFabricPlanningId, fabricPlanningId)
                .eq(FabricPlanningItem::getDelFlag, "0")
                .select(FabricPlanningItem::getId);
        List<FabricPlanningItem> list = super.list(qw);
        return CollectionUtils.isEmpty(list) ? Lists.newArrayList() : list.stream()
                .map(FabricPlanningItem::getId)
                .collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}
