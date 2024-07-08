/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.dto.PatternMakingBarCodeQueryDto;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import com.base.sbc.module.patternmaking.mapper.PatternMakingBarCodeMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingBarCodeService;
import com.base.sbc.module.patternmaking.vo.PatternMakingBarCodeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述： service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.PatternMakingBarCodeService
 * @email your email
 * @date 创建时间：2024-7-3 15:56:26
 */
@Service
public class PatternMakingBarCodeServiceImpl extends BaseServiceImpl<PatternMakingBarCodeMapper, PatternMakingBarCode> implements PatternMakingBarCodeService {


    private final MinioUtils minioUtils;

    public PatternMakingBarCodeServiceImpl(MinioUtils minioUtils) {
        super();
        this.minioUtils = minioUtils;
    }

    @Override
    public PageInfo<PatternMakingBarCodeVo> findPage(PatternMakingBarCodeQueryDto dto) {
        Page<Object> objects = PageHelper.startPage(dto);
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.andLike(dto.getDesignNo(), "ts.design_no", "tpm.pattern_no");
        qw.notEmptyEq("ts.year_name", dto.getYearName());
        qw.notEmptyEq("ts.season_name", dto.getSeasonName());
        qw.notEmptyEq("ts.brand_name", dto.getBrandName());
        qw.notEmptyEq("tpm.sample_type_name", dto.getSampleTypeName());
        qw.notEmptyEq("tpm.status", dto.getStatus());
        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        minioUtils.setObjectUrlToList(list,"img");
        return new PageInfo<>(list);
    }

    @Override
    public PatternMakingBarCodeVo getByBarCode(String barCode) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.eq("tpmbc.bar_code", barCode);
        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        if (CollUtil.isNotEmpty(list)) {
            PatternMakingBarCodeVo patternMakingBarCodeVo = list.get(0);
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"img");
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"suggestionImg");
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"suggestionVideo");
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"suggestionImg1");
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"suggestionImg2");
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"suggestionImg3");
            minioUtils.setObjectUrlToObject(patternMakingBarCodeVo,"suggestionImg4");
            return patternMakingBarCodeVo;
        }
        return null;
    }

    @Override
    public Boolean removeByBarCode(String barCode) {
        LambdaQueryWrapper<PatternMakingBarCode> qw = new LambdaQueryWrapper<>();
        qw.eq(PatternMakingBarCode::getBarCode, barCode);
        remove(qw);
        return null;
    }

    @Override
    @Transactional
    public void saveMain(PatternMakingBarCode patternMakingBarCode) {
        LambdaQueryWrapper<PatternMakingBarCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatternMakingBarCode::getHeadId, patternMakingBarCode.getHeadId());
        queryWrapper.eq(PatternMakingBarCode::getPitSite, patternMakingBarCode.getPitSite());
        remove(queryWrapper);

        saveOrUpdate(patternMakingBarCode);
    }

    @Override
    public List<PatternMakingBarCode> listByCode(String headId, Integer pitSite) {
        LambdaQueryWrapper<PatternMakingBarCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatternMakingBarCode::getHeadId, headId);
        queryWrapper.eq(PatternMakingBarCode::getPitSite, pitSite);
        return list(queryWrapper);
    }


}
