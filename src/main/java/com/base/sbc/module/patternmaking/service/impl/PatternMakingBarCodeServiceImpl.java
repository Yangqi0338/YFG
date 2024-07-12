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
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.nodestatus.service.impl.NodeStatusServiceImpl;
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
import java.util.stream.Collectors;

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
    private final NodeStatusService nodeStatusServiceImpl;
    private final StylePicUtils stylePicUtils;

    public PatternMakingBarCodeServiceImpl(MinioUtils minioUtils, NodeStatusServiceImpl nodeStatusServiceImpl, StylePicUtils stylePicUtils) {
        super();
        this.minioUtils = minioUtils;
        this.nodeStatusServiceImpl = nodeStatusServiceImpl;
        this.stylePicUtils = stylePicUtils;
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
        qw.notEmptyEq("tpmbc.status", dto.getStatus());
        qw.notEmptyIn("tpmbc.status", dto.getStatusList());
        qw.orderByDesc("tpmbc.create_date");
        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        minioUtils.setObjectUrlToList(list, "img", "suggestionImg", "suggestionVideo", "suggestionImg1", "suggestionImg2", "suggestionImg3", "suggestionImg4");
        stylePicUtils.setStylePic(list, "stylePic");
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<PatternMakingBarCodeVo> findPageLog(PatternMakingBarCodeQueryDto dto) {
        Page<Object> objects = PageHelper.startPage(dto);
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.andLike(dto.getDesignNo(), "ts.design_no", "tpm.pattern_no");
        qw.notEmptyEq("ts.year_name", dto.getYearName());
        qw.notEmptyEq("ts.season_name", dto.getSeasonName());
        qw.notEmptyEq("ts.brand_name", dto.getBrandName());
        qw.notEmptyEq("tpm.sample_type_name", dto.getSampleTypeName());
        qw.notEmptyEq("tpmbc.status", dto.getStatus());
        qw.eq("tpmbc.bar_code", dto.getBarCode());
        qw.orderByDesc("tpmbc.create_date");
        List<PatternMakingBarCodeVo> list = baseMapper.findPageLog(qw);
        minioUtils.setObjectUrlToList(list, "img");
        stylePicUtils.setStylePic(list, "stylePic");
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    public PatternMakingBarCodeVo getByBarCode(String barCode) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.eq("tpmbc.bar_code", barCode);
        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        if (CollUtil.isNotEmpty(list)) {
            stylePicUtils.setStylePic(list, "stylePic");
            stylePicUtils.setStyleColorPic2(list, "styleColorPic");
            PatternMakingBarCodeVo patternMakingBarCodeVo = list.get(0);
            minioUtils.setObjectUrlToList(list, "img", "suggestionImg", "suggestionVideo", "suggestionImg1", "suggestionImg2", "suggestionImg3", "suggestionImg4");
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
        //删除绑样时间
        List<PatternMakingBarCode> patternMakingBarCodes = listByCode(patternMakingBarCode.getHeadId(), patternMakingBarCode.getPitSite());
        if (CollUtil.isNotEmpty(patternMakingBarCodes)) {
            List<String> collect = patternMakingBarCodes.stream().map(PatternMakingBarCode::getHeadId).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<NodeStatus> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(NodeStatus::getDataId, collect);
            queryWrapper.eq(NodeStatus::getNode, "FOB");
            queryWrapper.eq(NodeStatus::getStatus, "绑样");
            nodeStatusServiceImpl.remove(queryWrapper);
            LambdaQueryWrapper<PatternMakingBarCode> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(PatternMakingBarCode::getHeadId, patternMakingBarCode.getHeadId());
            queryWrapper1.eq(PatternMakingBarCode::getPitSite, patternMakingBarCode.getPitSite());
            remove(queryWrapper1);
        }

        saveOrUpdate(patternMakingBarCode);
        //添加新的绑样时间
        NodeStatus nodeStatus = new NodeStatus();
        nodeStatus.setDataId(patternMakingBarCode.getHeadId());
        nodeStatus.setNode("FOB");
        nodeStatus.setStatus("绑样");
        nodeStatus.setStartDate(patternMakingBarCode.getCreateDate());
        nodeStatus.setEndDate(patternMakingBarCode.getCreateDate());
        nodeStatusServiceImpl.saveOrUpdate(nodeStatus);

        patternMakingBarCode.setId(null);
        baseMapper.saveBarCodeLog(patternMakingBarCode);
    }

    @Override
    public List<PatternMakingBarCode> listByCode(String headId, Integer pitSite) {
        LambdaQueryWrapper<PatternMakingBarCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatternMakingBarCode::getHeadId, headId);
        queryWrapper.eq(PatternMakingBarCode::getPitSite, pitSite);
        return list(queryWrapper);
    }

    @Override
    public void status(PatternMakingBarCode patternMakingBarCode) {
        updateById(patternMakingBarCode);
        //审样时间
        NodeStatus nodeStatus = new NodeStatus();
        nodeStatus.setDataId(patternMakingBarCode.getHeadId());
        nodeStatus.setNode("FOB");
        nodeStatus.setStatus("审样");
        nodeStatus.setStartDate(patternMakingBarCode.getCreateDate());
        nodeStatus.setEndDate(patternMakingBarCode.getCreateDate());
        nodeStatusServiceImpl.save(nodeStatus);
    }

    @Override
    public List<PatternMakingBarCode> listbyHeadId(List<String> ids) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.eq("head_id", ids);
        List<PatternMakingBarCode> list = list(qw);
        return list;
    }


}
