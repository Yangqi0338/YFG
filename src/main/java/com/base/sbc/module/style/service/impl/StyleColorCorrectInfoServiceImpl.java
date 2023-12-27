/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskDto;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.style.dto.AddRevampStyleColorDto;
import com.base.sbc.module.style.dto.QueryStyleColorCorrectDto;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.mapper.StyleColorCorrectInfoMapper;
import com.base.sbc.module.style.service.StyleColorCorrectInfoService;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：正确样管理 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.StyleColorCorrectInfoService
 * @email your email
 * @date 创建时间：2023-12-26 10:13:31
 */
@Service
public class StyleColorCorrectInfoServiceImpl extends BaseServiceImpl<StyleColorCorrectInfoMapper, StyleColorCorrectInfo> implements StyleColorCorrectInfoService {

    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private StyleColorService styleColorService;

    @Autowired
    private PreProductionSampleTaskService preProductionSampleTaskService;

    @Override
    public PageInfo<StyleColorCorrectInfoVo> findList(QueryStyleColorCorrectDto page) {
        /*分页*/
        Page<StyleColorCorrectInfoVo> objects = PageHelper.startPage(page);
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.andLike(page.getSearch(), "ts.design_no", "tsc.style_no");
        queryWrapper.notEmptyEq("ts.planning_season_id", page.getPlanningSeasonId());
        queryWrapper.notEmptyEq("ts.prod_category", page.getProdCategory());
        queryWrapper.notEmptyEq("ts.devt_type_name", page.getDevtTypeName());
        queryWrapper.notEmptyEq("ts.designer", page.getDesigner());
        queryWrapper.notEmptyEq("ts.task_level_name", page.getTaskLevelName());
        queryWrapper.notEmptyEq("tsc.id", page.getStyleColorId());
        queryWrapper.notExists("select 1 from t_style_color_correct_info t1 WHERE t1.style_color_id = tsc.id AND t1.del_flag = '1'");
        List<StyleColorCorrectInfoVo> infoVoList = baseMapper.findList(queryWrapper);

        /*查询款式图*/
        stylePicUtils.setStylePic(infoVoList, "stylePic");

        return new PageInfo<>(infoVoList);
    }

    @Override
    @Transactional
    public void saveMain(StyleColorCorrectInfo styleColorCorrectInfo) {
        StyleColorCorrectInfo oldDto = new StyleColorCorrectInfo();
        if(StrUtil.isNotBlank(styleColorCorrectInfo.getId())){
            styleColorCorrectInfo.updateInit();
            oldDto = getById(styleColorCorrectInfo.getId());
        }else{
            styleColorCorrectInfo.insertInit();
        }

        //修改产前样看板的工艺确认时间
        if(StrUtil.isNotBlank(styleColorCorrectInfo.getProductionSampleId())){
            PreProductionSampleTaskDto task = new PreProductionSampleTaskDto();
            task.setId(styleColorCorrectInfo.getProductionSampleId());
            task.setTechReceiveDate(styleColorCorrectInfo.getTechnicsDate());
            preProductionSampleTaskService.saveTechReceiveDate(task);
            //如果没有关联到  则保存在原始表中
            styleColorCorrectInfo.setTechnicsDate(null);
        }

        //修改款式配色的设计 时间
        AddRevampStyleColorDto styleColor = new AddRevampStyleColorDto();
        styleColor.setId(styleColorCorrectInfo.getStyleColorId());
        styleColor.setDesignCorrectDate(styleColorCorrectInfo.getDesignCorrectDate());
        styleColor.setDesignDetailDate(styleColorCorrectInfo.getDesignDetailDate());
        styleColorService.saveDesignDate(styleColor);

        //不保存 字段，分别保存在原始表中，关联查询
        styleColorCorrectInfo.setDesignCorrectDate(null);
        styleColorCorrectInfo.setDesignDetailDate(null);
        saveOrUpdate(styleColorCorrectInfo);
        //修改记录
        /*OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName("物料档案-颜色");
        operaLogEntity.setType(type);
        operaLogEntity.setDocumentId(entity.getId());
        operaLogEntity.setDocumentCode(entity.getColorCode());
        operaLogEntity.setDocumentName(entity.getColorName());
        operaLogEntity.setParentId(dto.getParentId());
        saveOrUpdateOperaLog();*/
    }
}
