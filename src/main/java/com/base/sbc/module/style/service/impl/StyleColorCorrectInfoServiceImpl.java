/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.dto.QueryStyleColorCorrectDto;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.mapper.StyleColorCorrectInfoMapper;
import com.base.sbc.module.style.service.StyleColorCorrectInfoService;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


        List<StyleColorCorrectInfoVo> infoVoList = baseMapper.findList(queryWrapper);

        /*查询款式图*/
        stylePicUtils.setStylePic(infoVoList, "stylePic");

        return new PageInfo<>(infoVoList);
    }
}
