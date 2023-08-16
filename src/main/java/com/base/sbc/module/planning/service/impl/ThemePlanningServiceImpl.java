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
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.ThemePlanningSaveDTO;
import com.base.sbc.module.planning.dto.ThemePlanningSearchDTO;
import com.base.sbc.module.planning.entity.ThemePlanning;
import com.base.sbc.module.planning.mapper.ThemePlanningMapper;
import com.base.sbc.module.planning.service.ThemePlanningImageService;
import com.base.sbc.module.planning.service.ThemePlanningMaterialService;
import com.base.sbc.module.planning.service.ThemePlanningService;
import com.base.sbc.module.planning.vo.ThemePlanningListVO;
import com.base.sbc.module.planning.vo.ThemePlanningVO;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：主题企划 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ThemePlanningService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:35
 */
@Service
public class ThemePlanningServiceImpl extends BaseServiceImpl<ThemePlanningMapper, ThemePlanning> implements ThemePlanningService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private ThemePlanningMaterialService themePlanningMaterialService;
    @Autowired
    private ThemePlanningImageService themePlanningImageService;

    @Override
    public PageInfo<ThemePlanningListVO> getThemePlanningList(ThemePlanningSearchDTO dto) {
        dto.setCompanyCode(super.getCompanyCode());
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<ThemePlanningListVO> themePlanningList = super.getBaseMapper().getThemePlanningList(dto);
        return new PageInfo<>(themePlanningList);
    }

    @Override
    public ThemePlanningVO getThemePlanningById(String id) {
        ThemePlanningVO themePlanningVO = super.getBaseMapper().getThemePlanningById(id);
        if (Objects.isNull(themePlanningVO)) {
            throw new OtherException("主题企划数据不存在");
        }
        themePlanningVO.setThemePlanningMaterials(themePlanningMaterialService.getByThemePlanningId(id));
        themePlanningVO.setImages(themePlanningImageService.getByThemePlanningId(id));
        return themePlanningVO;
    }

    @Override
    @Transactional
    public String themePlanningSave(ThemePlanningSaveDTO dto) {
        ThemePlanning themePlanning = CopyUtil.copy(dto, ThemePlanning.class);
        String companyCode = super.getCompanyCode();
        if (StringUtils.isEmpty(themePlanning.getId())) {
            themePlanning.setId(new IdGen().nextIdStr());
            themePlanning.setCompanyCode(companyCode);
            themePlanning.insertInit();
        } else {
            themePlanning.updateInit();
        }
        super.saveOrUpdate(themePlanning);
        themePlanningMaterialService.save(dto.getThemePlanningMaterials(), themePlanning.getId());
        themePlanningImageService.save(dto.getImages(), themePlanning.getId());
        return themePlanning.getId();
    }

    @Override
    public Long getThemePlanningCount(String planningSeasonId) {
        if (StringUtils.isEmpty(planningSeasonId)) {
            return 0L;
        }
        LambdaQueryWrapper<ThemePlanning> qw = new QueryWrapper<ThemePlanning>().lambda()
                .eq(ThemePlanning::getPlanningSeasonId, planningSeasonId)
                .eq(ThemePlanning::getDelFlag, "0");
        return super.count(qw);
    }

    @Override
    public List<ThemePlanningListVO> getThemeListByPlanningSeasonId(String planningSeasonId) {
        if (StringUtils.isEmpty(planningSeasonId)) {
            return Lists.newArrayList();
        }
        ThemePlanningSearchDTO themePlanningSearchDTO = new ThemePlanningSearchDTO();
        themePlanningSearchDTO.setCompanyCode(super.getCompanyCode());
        themePlanningSearchDTO.setPlanningSeasonId(planningSeasonId);
        return super.getBaseMapper().getThemePlanningList(themePlanningSearchDTO);
    }

// 自定义方法区 不替换的区域【other_end】

}

