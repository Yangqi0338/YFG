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
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.ColorPlanningSaveDTO;
import com.base.sbc.module.planning.dto.ColorPlanningSearchDTO;
import com.base.sbc.module.planning.entity.ColorPlanning;
import com.base.sbc.module.planning.mapper.ColorPlanningMapper;
import com.base.sbc.module.planning.service.ColorPlanningItemService;
import com.base.sbc.module.planning.service.ColorPlanningService;
import com.base.sbc.module.planning.vo.ColorPlanningListVO;
import com.base.sbc.module.planning.vo.ColorPlanningVO;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：颜色企划 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ColorPlanningService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:50
 */
@Service
public class ColorPlanningServiceImpl extends BaseServiceImpl<ColorPlanningMapper, ColorPlanning> implements ColorPlanningService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(ColorPlanningService.class);

    @Autowired
    private ColorPlanningItemService colorPlanningItemService;

    @Override
    public PageInfo<ColorPlanningListVO> getColorPlanningList(ColorPlanningSearchDTO colorPlanningSearchDTO) {
        PageHelper.startPage(colorPlanningSearchDTO.getPageNum(), colorPlanningSearchDTO.getPageSize());
        colorPlanningSearchDTO.setCompanyCode(super.getCompanyCode());
        List<ColorPlanningListVO> colorPlanningList = super.getBaseMapper().getColorPlanningList(colorPlanningSearchDTO);
        return new PageInfo<>(colorPlanningList);
    }

    @Override
    @Transactional
    public String colorPlanningSave(ColorPlanningSaveDTO colorPlanningSaveDTO) {
        logger.info("ColorPlanningService#colorPlanningSave 保存 colorPlanningSaveDTO:{}", JSON.toJSONString(colorPlanningSaveDTO));
        ColorPlanning colorPlanning = CopyUtil.copy(colorPlanningSaveDTO, ColorPlanning.class);
        if (StringUtils.isEmpty(colorPlanning.getId())) {
            colorPlanning.setId(new IdGen().nextIdStr());
            colorPlanning.setCompanyCode(super.getCompanyCode());
            colorPlanning.insertInit();
        } else {
            colorPlanning.updateInit();
        }
        super.saveOrUpdate(colorPlanning);
        colorPlanningItemService.colorPlanningItemSave(colorPlanningSaveDTO.getColorPlanningItemSaves(), colorPlanning.getId());
        return colorPlanning.getId();

    }

    @Override
    public ColorPlanningVO getDetailById(String id) {
        ColorPlanningVO colorPlanningVO = super.getBaseMapper().getDetailById(id);
        if (Objects.isNull(colorPlanningVO)) {
            throw new OtherException("数据不存在");
        }
        colorPlanningVO.setColorPlanningItems(colorPlanningItemService.getBYColorPlanningId(id));
        return colorPlanningVO;
    }

    @Override
    public Long getColorPlanningCount(String planningSeasonId) {
        if (StringUtils.isEmpty(planningSeasonId)) {
            return 0L;
        }
        LambdaQueryWrapper<ColorPlanning> qw = new QueryWrapper<ColorPlanning>().lambda()
                .eq(ColorPlanning::getPlanningSeasonId, planningSeasonId)
                .eq(ColorPlanning::getDelFlag, "0");
        return super.count(qw);
    }

    @Override
    public List<ColorPlanningListVO> getListByPlanningSeasonId(String planningSeasonId) {
        if (StringUtils.isEmpty(planningSeasonId)) {
            return Lists.newArrayList();
        }
        ColorPlanningSearchDTO colorPlanningSearchDTO = new ColorPlanningSearchDTO();
        colorPlanningSearchDTO.setCompanyCode(super.getCompanyCode());
        colorPlanningSearchDTO.setPlanningSeasonId(planningSeasonId);
        return super.getBaseMapper().getColorPlanningList(colorPlanningSearchDTO);
    }


// 自定义方法区 不替换的区域【other_end】

}

