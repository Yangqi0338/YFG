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
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.nodestatus.service.impl.NodeStatusServiceImpl;
import com.base.sbc.module.patternmaking.dto.PatternMakingBarCodeQueryDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingBarCodeUpdateDto;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import com.base.sbc.module.patternmaking.mapper.PatternMakingBarCodeMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingBarCodeService;
import com.base.sbc.module.patternmaking.vo.PatternMakingBarCodeVo;
import com.base.sbc.module.sample.entity.PreProductionSampleTaskFob;
import com.base.sbc.module.sample.service.PreProductionSampleTaskFobService;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final PreProductionSampleTaskFobService preProductionSampleTaskFobService;
    @Lazy
    @Resource
    private SmpService smpService;
    @Autowired
    private UploadFileService uploadFileService;

    public PatternMakingBarCodeServiceImpl(MinioUtils minioUtils, NodeStatusServiceImpl nodeStatusServiceImpl, StylePicUtils stylePicUtils, PreProductionSampleTaskFobService preProductionSampleTaskFobService) {
        super();
        this.minioUtils = minioUtils;
        this.nodeStatusServiceImpl = nodeStatusServiceImpl;
        this.stylePicUtils = stylePicUtils;
        this.preProductionSampleTaskFobService = preProductionSampleTaskFobService;
    }

    @Override
    public PageInfo<PatternMakingBarCodeVo> findPage(PatternMakingBarCodeQueryDto dto) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.andLike(dto.getDesignNo(), "ts.design_no", "tpm.pattern_no");
        qw.notEmptyEq("ts.year_name", dto.getYearName());
        qw.notEmptyEq("ts.season_name", dto.getSeasonName());
        qw.notEmptyEq("ts.brand_name", dto.getBrandName());
        qw.notEmptyEq("tpm.sample_type_name", dto.getSampleTypeName());
        qw.notEmptyEq("tpmbc.status", dto.getStatus());
        qw.notEmptyIn("tpmbc.status", dto.getStatusList());
        qw.orderByDesc("tpmbc.create_date");

        QueryGenerator.initQueryWrapperByMapNoDataPermission(qw,dto);
        Page<Object> objects = PageHelper.startPage(dto);
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
    public List<PatternMakingBarCodeVo> getByBarCode(String barCode) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.eq("tpmbc.bar_code", barCode);
        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        if (CollUtil.isNotEmpty(list)) {
            stylePicUtils.setStylePic(list, "stylePic");
            stylePicUtils.setStyleColorPic2(list, "styleColorPic");
            minioUtils.setObjectUrlToList(list, "img", "suggestionImg", "suggestionVideo", "suggestionImg1", "suggestionImg2", "suggestionImg3", "suggestionImg4");
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean removeByBarCode(String barCode) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.eq("tpmbc.bar_code", barCode);
        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        if (CollUtil.isNotEmpty(list)) {
            List<String> collect = list.stream().map(PatternMakingBarCode::getHeadId).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<NodeStatus> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(NodeStatus::getDataId, collect);
            queryWrapper.eq(NodeStatus::getNode, "FOB");
            queryWrapper.eq(NodeStatus::getStatus, "绑样");
            nodeStatusServiceImpl.remove(queryWrapper);
        }

        LambdaQueryWrapper<PatternMakingBarCode> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(PatternMakingBarCode::getBarCode, barCode);
        remove(qw1);


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

            patternMakingBarCode.setId(null);
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
        nodeStatus.setStartDate(patternMakingBarCode.getUpdateDate());
        nodeStatus.setEndDate(patternMakingBarCode.getUpdateDate());
        nodeStatusServiceImpl.save(nodeStatus);


        //推送外部系统
        try {
            List<String> list = Arrays.asList("11", "12");
            if(list.contains(patternMakingBarCode.getStatus())){
                PreProductionSampleTaskFob taskFob = preProductionSampleTaskFobService.getById(patternMakingBarCode.getHeadId());
                smpService.pushPreProduction(patternMakingBarCode,taskFob);
            }
        }catch (Exception e){
            log.error("产前样确认推送外部系统失败："+e.getMessage(),e);
        }
    }

    @Override
    public List<PatternMakingBarCode> listbyHeadId(List<String> ids) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.in("head_id", ids);
        List<PatternMakingBarCode> list = list(qw);
        return list;
    }

    @Override
    public PageInfo<PatternMakingBarCodeVo> pageAudit(PatternMakingBarCodeQueryDto dto) {
        Page<Object> objects = PageHelper.startPage(dto);
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("ts.design_no",dto.getDesignNo());
        qw.notEmptyEq("tpm.pattern_no",dto.getPatternNo());
        qw.notEmptyEq("tpmbc.bar_code",dto.getBarCode());
        qw.notEmptyEq("tpm.sample_type_name", dto.getSampleTypeName());
        qw.notEmptyEq("tpmbc.status", dto.getStatus());
        qw.notEmptyIn("tpmbc.status", Arrays.asList("1","2"));
        qw.notEmptyLike("tbs.supplier", dto.getSupplierId());
        qw.notEmptyEq("tpm.pattern_room_id", dto.getPatternRoomId());
        qw.notEmptyEq("tpm.supplier_style_no", dto.getSupplierStyleNo());

        qw.orderByDesc("tpmbc.create_date");

        QueryGenerator.initQueryWrapperByMapNoDataPermission(qw,dto);

        List<PatternMakingBarCodeVo> list = baseMapper.findPage(qw);
        list.forEach(o->{
            o.setOriginalImg(o.getImg());
            o.setOriginalSuggestionVideo(o.getSuggestionVideo());
            o.setOriginalSuggestionImg(o.getSuggestionImg());
            o.setOriginalSuggestionImg1(o.getSuggestionImg1());
            o.setOriginalSuggestionImg2(o.getSuggestionImg2());
            o.setOriginalSuggestionImg3(o.getSuggestionImg3());
            o.setOriginalSuggestionImg4(o.getSuggestionImg4());
        });
        minioUtils.setObjectUrlToList(list, "img", "suggestionImg", "suggestionVideo", "suggestionImg1", "suggestionImg2", "suggestionImg3", "suggestionImg4");
        stylePicUtils.setStylePic(list, "stylePic");
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        uploadFileService.setObjectUrlToList(list,"sampleUrl");

        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<PatternMakingBarCodeVo> auditList(PatternMakingBarCodeQueryDto dto) {
        BaseQueryWrapper<PatternMakingBarCode> qw = new BaseQueryWrapper<>();
        qw.notEmptyIn("tpmbc.status", dto.getStatusList());
        qw.orderByDesc("tpmbc.create_date");

        QueryGenerator.initQueryWrapperByMapNoDataPermission(qw,dto);
        Page<Object> objects = PageHelper.startPage(dto);
        List<PatternMakingBarCodeVo> list;
        if("sampleAuditA".equals(dto.getTableCode())){
            qw.groupBy("ts.design_no");
            list = baseMapper.findSAuditPage(qw);
        }else{
            qw.groupBy("tsc.style_no");
            list = baseMapper.findPAuditPage(qw);
        }

        stylePicUtils.setStylePic(list, "stylePic");
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    public void statusByPc(PatternMakingBarCodeUpdateDto dto) {
        PatternMakingBarCode old = getById(dto.getBarCodeId());
        old.updateInit();
        old.setStatus(dto.getStatus());
        old.setSuggestion(dto.getSuggestion());
        old.setSuggestionImg(dto.getSuggestionImg());
        old.setSuggestionImg1(dto.getSuggestionImg1());
        old.setSuggestionImg2(dto.getSuggestionImg2());
        old.setSuggestionImg3(dto.getSuggestionImg3());
        old.setSuggestionImg4(dto.getSuggestionImg4());
        old.setSuggestionVideo(dto.getSuggestionVideo());
        updateById(old);

        //推送外部系统
        try {
            List<String> list = Arrays.asList("11", "12");
            if(list.contains(old.getStatus())){
                PreProductionSampleTaskFob taskFob = preProductionSampleTaskFobService.getById(old.getHeadId());
                smpService.pushPreProduction(old,taskFob);
            }
        }catch (Exception e){
            log.error("产前样确认推送外部系统失败："+e.getMessage(),e);
        }
    }


}
