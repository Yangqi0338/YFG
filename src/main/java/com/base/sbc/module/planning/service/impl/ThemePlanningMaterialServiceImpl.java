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
import com.base.sbc.module.planning.dto.ThemePlanningMaterialSaveDTO;
import com.base.sbc.module.planning.entity.ThemePlanningMaterial;
import com.base.sbc.module.planning.mapper.ThemePlanningMaterialMapper;
import com.base.sbc.module.planning.service.ThemePlanningMaterialService;
import com.base.sbc.module.planning.vo.ThemePlanningMaterialVO;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：主题企划素材 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ThemePlanningMaterialService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:45
 */
@Service
public class ThemePlanningMaterialServiceImpl extends BaseServiceImpl<ThemePlanningMaterialMapper, ThemePlanningMaterial> implements ThemePlanningMaterialService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(ThemePlanningMaterialService.class);

    @Override
    public List<ThemePlanningMaterialVO> getByThemePlanningId(String themePlanningId) {
        return super.getBaseMapper().getByThemePlanningId(themePlanningId);
    }

    @Override
    public void save(List<ThemePlanningMaterialSaveDTO> themePlanningMaterialSaves, String themePlanningId) {
        logger.info("ThemePlanningMaterialService#save 保存 themePlanningSaveDTO:{}, themePlanningId:{}",
                JSON.toJSONString(themePlanningMaterialSaves), themePlanningId);

        if (CollectionUtils.isEmpty(themePlanningMaterialSaves)) {
            LambdaQueryWrapper<ThemePlanningMaterial> queryWrapper = new QueryWrapper<ThemePlanningMaterial>()
                    .lambda()
                    .eq(ThemePlanningMaterial::getThemePlanningId, themePlanningId)
                    .eq(ThemePlanningMaterial::getDelFlag, "0")
                    .select(ThemePlanningMaterial::getId);
            super.getBaseMapper().delete(queryWrapper);
            return;
        }

        List<String> ids = this.getIdByThemePlanningId(themePlanningId);
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        List<ThemePlanningMaterial> themePlanningMaterials = themePlanningMaterialSaves.stream()
                .map(e -> {
                    ThemePlanningMaterial themePlanningMaterial = CopyUtil.copy(e, ThemePlanningMaterial.class);
                    if (StringUtils.isEmpty(themePlanningMaterial.getId())) {
                        themePlanningMaterial.setId(idGen.nextIdStr());
                        themePlanningMaterial.setCompanyCode(companyCode);
                        themePlanningMaterial.insertInit();
                    } else {
                        themePlanningMaterial.updateInit();
                    }
                    themePlanningMaterial.setThemePlanningId(themePlanningId);
                    ids.remove(themePlanningMaterial.getId());
                    return themePlanningMaterial;
                }).collect(Collectors.toList());

        super.saveOrUpdateBatch(themePlanningMaterials);
        if (CollectionUtils.isNotEmpty(ids)) {
            super.getBaseMapper().deleteBatchIds(ids);
        }
    }

    private List<String> getIdByThemePlanningId(String themePlanningId) {
        LambdaQueryWrapper<ThemePlanningMaterial> queryWrapper = new QueryWrapper<ThemePlanningMaterial>()
                .lambda()
                .eq(ThemePlanningMaterial::getThemePlanningId, themePlanningId)
                .eq(ThemePlanningMaterial::getDelFlag, "0")
                .select(ThemePlanningMaterial::getId);
        List<ThemePlanningMaterial> themePlanningMaterials = super.getBaseMapper().selectList(queryWrapper);
        return CollectionUtils.isEmpty(themePlanningMaterials) ? Lists.newArrayList() : themePlanningMaterials.stream()
                .map(ThemePlanningMaterial::getId)
                .collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}

