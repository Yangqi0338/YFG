/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.entity.Dept;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.IFileLoaderImpl;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.TechnologyBoardConstant;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.*;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.nodestatus.dto.NodestatusPageSearchDto;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.entity.ScoreConfig;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.service.ScoreConfigService;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 类描述：打版管理 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.PatternMakingService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 */
@Service
@RequiredArgsConstructor

public class PatternMakingServiceImpl extends BaseServiceImpl<PatternMakingMapper, PatternMaking> implements PatternMakingService {
    Logger log = LoggerFactory.getLogger(getClass());
    // 自定义方法区 不替换的区域【other_start】
    private final StyleService styleService;
    private final NodeStatusService nodeStatusService;
    private final AttachmentService attachmentService;
    private final AmcFeignService amcFeignService;

    private final CcmFeignService ccmFeignService;

    private final MessageUtils messageUtils;

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private BaseController baseController;

    @Autowired
    private DataPermissionsService dataPermissionsService;
    @Autowired
    private StylePicUtils stylePicUtils;
    @Autowired
    private ScoreConfigService scoreConfigService;

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public List<PatternMakingListVo> findBySampleDesignId(String styleId) {
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("style_id", styleId);
        qw.eq("m.del_flag", BaseGlobal.NO);
        qw.orderBy(true, true, "create_date");
        List<PatternMakingListVo> patternMakingListVos = getBaseMapper().findBySampleDesignId(qw);
        return patternMakingListVos;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public PatternMaking savePatternMaking(PatternMakingDto dto) {
        Style style = styleService.getById(dto.getStyleId());
        if (style == null) {
            throw new OtherException("款式设计不存在");
        }
        QueryWrapper rQw = new QueryWrapper();
        rQw.eq("style_id", dto.getStyleId());
        rQw.eq("del_flag", BaseGlobal.NO);
        BigDecimal patternMakingScore = BigDecimal.ZERO;
        // 出版样只能有一个
        if (StrUtil.equals("初版样", dto.getSampleType())) {
            rQw.eq("sample_type", dto.getSampleType());
            long count = count(rQw);
            if (count != 0) {
                throw new OtherException(dto.getSampleType() + "只能有一个");
            }
            patternMakingScore = Opt.ofNullable(scoreConfigService.findOne(BeanUtil.copyProperties(style, ScoreConfigSearchDto.class))).map(ScoreConfig::getPatternDefaultScore).orElse(BigDecimal.ZERO);
        } else {
            rQw.ne("sample_type", "初版样");
            long count = count(rQw);
            if (count >= 5) {
                throw new OtherException("只能新建6个打版指令:1个初版样、5个其他");
            }
        }
        // 校验打样顺序重复
        checkPatSeqRepeat(dto.getStyleId(), null, dto.getPatSeq());
        // 查询样衣
        PatternMaking patternMaking = BeanUtil.copyProperties(dto, PatternMaking.class);
        patternMaking.setCode(getNextCode(style));
        patternMaking.setPlanningSeasonId(style.getPlanningSeasonId());
        if (StrUtil.equals(dto.getTechnicianKitting(), BaseGlobal.YES)) {
            patternMaking.setTechnicianKittingDate(new Date());
        }
        //设置版师工作量评分
        patternMaking.setPatternMakingScore(patternMakingScore);
        patternMaking.setSglKitting(BaseGlobal.NO);
        patternMaking.setBreakOffPattern(BaseGlobal.NO);
        patternMaking.setBreakOffSample(BaseGlobal.NO);
        patternMaking.setPrmSendStatus(BaseGlobal.NO);
        patternMaking.setDesignSendStatus(BaseGlobal.NO);
        // patternMaking.setSecondProcessing(BaseGlobal.NO);
        patternMaking.setSuspend(BaseGlobal.NO);
        patternMaking.setReceiveSample(BaseGlobal.NO);
        patternMaking.setExtAuxiliary(BaseGlobal.NO);
        patternMaking.setPatDiff(Opt.ofBlankAble(patternMaking.getPatDiff()).orElse(style.getPatDiff()));
        patternMaking.setPatternDesignName(style.getPatternDesignName());
        patternMaking.setPatternDesignId(style.getPatternDesignId());
        patternMaking.setSampleFinishNum(patternMaking.getRequirementNum());
        patternMaking.setCutterFinishNum(patternMaking.getRequirementNum());
        save(patternMaking);

        return patternMaking;
    }

    @Override
    public void checkPatSeqRepeat(String styleId, String patternMakingId, String patSeq) {
        //校验打样顺序重复  patSeq
        BaseQueryWrapper<PatternMaking> patSeqQw = new BaseQueryWrapper<>();
        patSeqQw.eq("style_id", styleId);
        patSeqQw.lambda().eq(PatternMaking::getPatSeq, patSeq);
        patSeqQw.ne(!CommonUtils.isInitId(patternMakingId), "id", patternMakingId);
        long patSeqCount = count(patSeqQw);
        if (patSeqCount > 0) {
            throw new OtherException("打样顺序重复");
        }
    }

    @Override
    public void checkPatternNoRepeat(String id, String patternNo) {
        QueryWrapper<PatternMaking> countQw = new QueryWrapper<>();
        countQw.eq("company_code", getCompanyCode());
        countQw.eq("pattern_no", patternNo);
        countQw.ne(StrUtil.isNotBlank(id), "id", id);
        long count = count(countQw);
        if (count > 0) {
            throw new OtherException("样板号【" + patternNo + "】重复");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean sampleDesignSend(StyleSendDto dto) {
        EnumNodeStatus enumNodeStatus = EnumNodeStatus.DESIGN_SEND;
        EnumNodeStatus enumNodeStatus2 = EnumNodeStatus.TECHNICAL_ROOM_RECEIVED;
        nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus.getNode(), enumNodeStatus.getStatus(), BaseGlobal.YES, BaseGlobal.YES);
        NodeStatus nodeStatus = nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus2.getNode(), enumNodeStatus2.getStatus(), BaseGlobal.YES, BaseGlobal.YES);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("node", enumNodeStatus2.getNode());
        uw.set("status", enumNodeStatus2.getStatus());
        uw.set("design_send_date", nodeStatus.getStartDate());
        uw.set("design_send_status", BaseGlobal.YES);
        uw.eq("id", dto.getId());
        setUpdateInfo(uw);
        PatternMaking patternMaking = getById(dto.getId());
        uw.lambda().set(PatternMaking::getSampleFinishNum, patternMaking.getRequirementNum())
                .set(PatternMaking::getCutterFinishNum, patternMaking.getRequirementNum());
        update(uw);
        /**
         * 1.当第一个下发版是初版样时先下发到技术中心，技术中心下发对版师时同时同步到款式信息 之后的版都自动下发给初版样技术中心指定的版师，
         * 2。当第一个下发版是改版样时 款式信息中存在版师时自动下发到指定版师 款式中无数据时到技术中心看板手动下发版师同时同步到款式 之后的款都自动下发到该版师
         *
         * 其他：档版师离职时到技术中心查询选新版师，同时带出离职版师，
         * 当初版在技术中心未下发又新建新版时不自动下发给到技术中心手动下发
         */
        /*判断是不是第一个版*/
        QueryWrapper qw = new QueryWrapper();
        qw.eq("style_id", patternMaking.getStyleId());
        qw.eq("design_send_status", "1");
        List<PatternMaking> list = baseMapper.selectList(qw);
        Style style = styleService.getById(patternMaking.getStyleId());
        /*判断是不是初版样*/
        if (!StrUtil.equals("初版样", patternMaking.getSampleTypeName())) {
            /*改版样判断没值时手动下发*/
            if (StrUtil.isNotBlank(style.getPatternDesignName())) {
                /*自动下发查看版师是否离职*/
                UserCompany userCompany = amcFeignService.getUserByUserId(style.getPatternDesignId());
                if (!StrUtil.equals(userCompany.getIsDimission(), BaseGlobal.YES)) {
                    // 自动下发到打板管理
                    SetPatternDesignDto setPatternDesignDto = new SetPatternDesignDto();
                    setPatternDesignDto.setId(patternMaking.getId());
                    setPatternDesignDto.setPatternDesignId(style.getPatternDesignId());
                    setPatternDesignDto.setPatternDesignName(style.getPatternDesignName());
                    // 自动下发打板
                    prmSend(setPatternDesignDto);
                } else {
                    UpdateWrapper<PatternMaking> uw2 = new UpdateWrapper<>();
                    /*初版版师离职后需要手动下发*/
                    uw2.set("first_pattern_design_id", patternMaking.getPatternDesignId());
                    uw2.set("first_pattern_design_name", patternMaking.getPatternDesignName());
                    uw2.eq("id", dto.getId());
                    update(uw2);
                }
            }
        }

        // 将款式设计状态改为已下发
        UpdateWrapper<Style> sdUw = new UpdateWrapper<>();
        sdUw.eq("id", patternMaking.getStyleId());
        sdUw.set("status", BasicNumber.TWO.getNumber());
        sdUw.set("send_pattern_making_date", new Date());
        styleService.update(sdUw);

        /*发送消息*/
        messageUtils.sampleDesignSendMessage(patternMaking.getPatternRoomId(), patternMaking.getPatternNo(), baseController.getUser());

        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setDocumentId(operaLogEntity.getId());
        operaLogEntity.setType("下发打板");
        operaLogEntity.setDocumentName(patternMaking.getPatternNo());
        operaLogEntity.setDocumentCode(patternMaking.getCode());
        operaLogEntity.setName("打板指令");
        operaLogEntity.setParentId(patternMaking.getStyleId());
        this.saveLog(operaLogEntity);
        // 修改单据
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(String userId, NodeStatusChangeDto dto, GroupUser groupUser) {
        nodeStatusService.nodeStatusChange(dto.getDataId(), dto.getNode(), dto.getStatus(), dto.getStartFlg(), dto.getEndFlg());
        // 修改单据
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.eq("id", dto.getDataId());
        if (CollUtil.isNotEmpty(dto.getUpdates())) {
            for (Map.Entry<String, Object> kv : dto.getUpdates().entrySet()) {
                uw.set(StrUtil.toUnderlineCase(kv.getKey()), kv.getValue());
            }
        }
        uw.set("node", dto.getNode());
        uw.set("status", dto.getStatus());
        setUpdateInfo(uw);
        setNodeStatusPmData(dto, groupUser, uw);
        // 修改单据
        return update(uw);
    }

    /**
     * 样衣状态改变时设置 相关的值
     *
     * @param dto
     * @param groupUser
     * @param uw
     */
    private void setNodeStatusPmData(NodeStatusChangeDto dto, GroupUser groupUser, UpdateWrapper<PatternMaking> uw) {
        EnumNodeStatus enumNodeStatus = EnumNodeStatus.byNodeStatus(dto.getNode(), dto.getStatus());
        if (enumNodeStatus == null) {
            return;
        }
        switch (enumNodeStatus) {
//            case GARMENT_CUTTING_RECEIVED:
//                uw.set("cutter_id", groupUser.getId());
//                uw.set("cutter_name", groupUser.getName());
//                uw.isNull("cutter_id");
//                break;
//            case GARMENT_SEWING_STARTED:
//                uw.set("stitcher_id", groupUser.getId());
//                uw.set("stitcher", groupUser.getName());
//                uw.isNull("stitcher_id");
//                break;
            case GARMENT_CUTTING_KITTING:
                uw.set("sgl_kitting", BaseGlobal.YES);
                uw.set("sgl_kitting_date", new Date());
                break;
            default:
                break;
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean prmSend(SetPatternDesignDto dto) {
        PatternMaking byId = getById(dto.getId());
        if (StrUtil.equals(BaseGlobal.YES, byId.getBreakOffPattern())) {
            throw new OtherException("已中断打版,不能下发");
        }
        EnumNodeStatus enumNodeStatus1 = EnumNodeStatus.TECHNICAL_ROOM_SEND;
        EnumNodeStatus enumNodeStatus2 = EnumNodeStatus.SAMPLE_TASK_WAITING_RECEIVE;
        nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus1.getNode(), enumNodeStatus1.getStatus(), BaseGlobal.NO, BaseGlobal.YES);
        NodeStatus nodeStatus2 = nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus2.getNode(), enumNodeStatus2.getStatus(), BaseGlobal.YES, BaseGlobal.NO);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("node", enumNodeStatus2.getNode());
        uw.set("status", enumNodeStatus2.getStatus());
        uw.set("prm_send_date", nodeStatus2.getStartDate());
        uw.set("prm_send_status", BaseGlobal.YES);
        uw.set("pattern_status", BasicNumber.ZERO.getNumber());
        uw.set(StrUtil.isNotBlank(dto.getPatternDesignId()), "pattern_design_id", dto.getPatternDesignId());
        uw.set(StrUtil.isNotBlank(dto.getPatternDesignName()), "pattern_design_name", dto.getPatternDesignName());
        uw.eq("id", dto.getId());
        // 修改单据
        update(uw);
        sort(byId, false);
        /*当为出版样时修改款的版师*/
        Style style = styleService.getById(byId.getStyleId());
        style.setStatus(BasicNumber.TWO.getNumber());
        style.setSendPatternMakingDate(new Date());
        if (StrUtil.equals("初版样", byId.getSampleTypeName()) || StrUtil.isBlank(style.getPatternDesignName())) {
            style.setPatternDesignId(dto.getPatternDesignId());
            style.setPatternDesignName(dto.getPatternDesignName());
        }
        styleService.updateById(style);
        /*消息通知*/
        messageUtils.prmSendMessage(dto.getPatternDesignId(), byId.getPatternNo(), baseController.getUser());
        return true;
    }

    @Override
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.like(StrUtil.isNotBlank(dto.getSampleType()), "p.sample_type_name", dto.getSampleType());
        qw.like(StrUtil.isNotBlank(dto.getUrgencyName()), "p.urgency_name", dto.getUrgencyName());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        qw.in(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "p.planning_season_id", StrUtil.split(dto.getPlanningSeasonId(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getDesignerIds()), "s.designer_id", StrUtil.split(dto.getDesignerIds(), CharUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory()), "s.prod_category", dto.getProdCategory());
        if (StrUtil.isNotBlank(dto.getDesignSendDate())) {
            String[] split = dto.getDesignSendDate().split(",");
            qw.ge("p.design_send_date", split[0]);
            qw.le("p.design_send_date", split[1]);
        }
        if (StrUtil.isNotBlank(dto.getPrmSendDate())) {
            String[] split = dto.getPrmSendDate().split(",");
            qw.ge("p.prm_send_date", split[0]);
            qw.le("p.prm_send_date", split[1]);
        }

        qw.eq("design_send_status", BaseGlobal.YES);
        qw.eq("s.del_flag", BaseGlobal.NO);
        qw.eq("p.del_flag", BaseGlobal.NO);
        amcFeignService.teamAuth(qw, "s.planning_season_id", getUserId());
        if (StrUtil.isBlank(dto.getOrderBy())) {
            dto.setOrderBy(" p.create_date desc ");
        } else {
            dto.setOrderBy(dto.getOrderBy());
        }
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.technologyCenter.getK());
        Page<TechnologyCenterTaskVo> page = PageHelper.startPage(dto);
        List<TechnologyCenterTaskVo> list = getBaseMapper().technologyCenterTaskList(qw);
        // 设置图片
        stylePicUtils.setStylePic(list, "stylePic");
        // 设置版师列表
        if (CollUtil.isNotEmpty(list)) {
            // 去掉 优化性能
//            Map<String, List<PatternDesignVo>> pdMap = new HashMap<>(16);
//            for (TechnologyCenterTaskVo tct : list) {
//                String key = tct.getPlanningSeasonId();
//                if (pdMap.containsKey(key)) {
//                    tct.setPdList(pdMap.get(key));
//                } else {
//                    List<PatternDesignVo> patternDesignList = getPatternDesignList(tct.getPlanningSeasonId());
//                    tct.setPdList(patternDesignList);
//                    pdMap.put(key, patternDesignList);
//                }
//            }
        }
        nodeStatusService.setNodeStatus(list);
        return page.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternDesign(SetPatternDesignDto dto) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("pattern_design_id", dto.getPatternDesignId());
        uw.set("pattern_design_name", dto.getPatternDesignName());
        uw.eq("id", dto.getId());
        setUpdateInfo(uw);
        boolean b = update(uw);
        PatternMaking patternMaking = baseMapper.selectById(dto.getId());
        if (StrUtil.equals(patternMaking.getSampleTypeName(), "初版样")) {
            //同步到款式信息
            UpdateWrapper<Style> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("pattern_design_id", dto.getPatternDesignId());
            updateWrapper.set("pattern_design_name", dto.getPatternDesignName());
            updateWrapper.eq("id", patternMaking.getStyleId());
            styleService.update(updateWrapper);
        }
        return b;
//        return true;
    }

    @Override
    public List<PatternDesignVo> getPatternDesignList(String planningSeasonId) {
        List<UserCompany> userList = amcFeignService.getTeamUserListByPost(planningSeasonId, "版师");
        if (CollUtil.isEmpty(userList)) {
            return null;
        }
        List<String> userIds = userList.stream().map(UserCompany::getUserId).collect(Collectors.toList());
        Map<String, String> sampleTypes = ccmFeignService.getDictInfoToMap("SampleType").get("SampleType");
        QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
        pmQw.in("pattern_design_id", userIds);
        // 查询已接受 已打版的数据
        pmQw.eq("node", EnumNodeStatus.SAMPLE_TASK_WAITING_RECEIVE.getNode());
        pmQw.in("status", CollUtil.newArrayList(
                EnumNodeStatus.SAMPLE_TASK_RECEIVED.getStatus(),
                EnumNodeStatus.SAMPLE_TASK_IN_VERSION.getStatus()
        ));
        List<PatternDesignSampleTypeQtyVo> qtyList = getBaseMapper().getPatternDesignSampleTypeCount(pmQw);
        List<PatternDesignVo> patternDesignVoList = new ArrayList<>();
        Map<String, List<PatternDesignSampleTypeQtyVo>> qtyMap = qtyList.stream().collect(Collectors.groupingBy(PatternDesignSampleTypeQtyVo::getPatternDesignId));
        Long total = null;
        for (UserCompany user : userList) {
            PatternDesignVo patternDesignVo = BeanUtil.copyProperties(user, PatternDesignVo.class);
            LinkedHashMap<String, Long> sampleTypeCount = new LinkedHashMap<>(16);
            total = 0L;
            for (Map.Entry<String, String> dict : sampleTypes.entrySet()) {
                Long qty = Optional.ofNullable(qtyMap).map(qtyMap1 -> qtyMap1.get(user.getUserId())).map(qtyList2 -> {
                    PatternDesignSampleTypeQtyVo one = CollUtil.findOne(qtyList2, a -> StrUtil.equals(a.getSampleType(), dict.getKey()));
                    return Optional.ofNullable(one).map(PatternDesignSampleTypeQtyVo::getQuantity).orElse(0L);
                }).orElse(0L);
                total = total + qty;
                sampleTypeCount.put(dict.getValue(), qty);
            }
            sampleTypeCount.put("总数", total);
            String deptName = Optional.ofNullable(user.getDeptList()).map(item -> {
                return item.stream().map(Dept::getName).collect(Collectors.joining(StrUtil.COMMA));
            }).orElse("");
            patternDesignVo.setDeptName(deptName);
            patternDesignVo.setSampleTypeCount(sampleTypeCount);
            patternDesignVoList.add(patternDesignVo);
        }
        return patternDesignVoList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean breakOffSample(String id, String flag) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("break_off_sample", flag);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean breakOffPattern(String id, String flag) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("break_off_pattern", flag);
        String[] split = id.split(",");
        List<PatternMaking> patternMakings = this.listByIds(Arrays.asList(split));

        for (PatternMaking patternMaking : patternMakings) {
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setDocumentId(operaLogEntity.getId());
            operaLogEntity.setType("中断打板");
            operaLogEntity.setDocumentName(patternMaking.getPatternNo());
            operaLogEntity.setDocumentCode(patternMaking.getCode());
            operaLogEntity.setName("打板指令");
            operaLogEntity.setParentId(patternMaking.getStyleId());
            this.saveLog(operaLogEntity);
        }

        uw.set("break_off_pattern", flag);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Integer prmSendBatch(List<SetPatternDesignDto> dtos) {
        int i = 0;
        for (SetPatternDesignDto dto : dtos) {
            if (prmSend(dto)) {
                i++;
            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternDesignBatch(List<SetPatternDesignDto> dtos) {
        for (SetPatternDesignDto dto : dtos) {
            setPatternDesign(dto);
        }
        return true;
    }

    @Override
    public PageInfo<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto) {
        BaseQueryWrapper qw = new BaseQueryWrapper();
//        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq("p.historical_data", "0");
        qw.andLike(dto.getSearch(), "s.design_no", "p.code");
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getNode()), "p.node", dto.getNode());
        qw.eq(StrUtil.isNotBlank(dto.getFinishFlag()), "p.finish_flag", dto.getFinishFlag());
        qw.eq(StrUtil.isNotBlank(dto.getSampleCompleteFlag()), "p.sample_complete_flag", dto.getSampleCompleteFlag());
        //手机端
        if (StrUtil.equals(dto.getIsApp(), BasicNumber.ONE.getNumber())) {
            //判断是否是样衣组长
            List<String> deptId = amcFeignService.getUserDepByUserIdUserType(getUserId(), BasicNumber.TWO.getNumber());
            if (CollUtil.isNotEmpty(deptId)) {
                qw.in("p.pattern_room_id", deptId);
            } else {
                userAuthQw(dto, qw);
            }
        } else {
            userAuthQw(dto, qw);
        }


        qw.in(StrUtil.isNotBlank(dto.getPatternStatus()), "p.pattern_status", StrUtil.split(dto.getPatternStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getCuttingStatus()), "p.cutting_status", StrUtil.split(dto.getCuttingStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getSewingStatus()), "p.sewing_status", StrUtil.split(dto.getSewingStatus(), CharUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getBreakOffPattern()), "p.break_off_pattern", dto.getBreakOffPattern());
        qw.eq(StrUtil.isNotBlank(dto.getSuspend()), "p.suspend", dto.getSuspend());
        qw.eq(StrUtil.isNotBlank(dto.getBreakOffSample()), "p.break_off_sample", dto.getBreakOffSample());
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "p.status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        if (StrUtil.isNotBlank(dto.getIsBlackList())) {
            if (StrUtil.equals(dto.getIsBlackList(), BasicNumber.ONE.getNumber())) {
                // 只查询黑单
                qw.eq("p.urgency", "0");
            } else {
                // 排除黑单
                qw.ne("p.urgency", "0");

            }
        }
        // 版房主管和设计师 看到全部，版师、裁剪工、车缝工、样衣组长看到自己,
//        amcFeignService.teamAuth(qw, "s.planning_season_id", getUserId());

        // 数据权限
        dataPermissionsService.getDataPermissionsForQw(qw, dto.getBusinessType(), "s.");
        if (StrUtil.isBlank(dto.getOrderBy())) {
            qw.orderByDesc("p.create_date");
            qw.orderByAsc("p.sort");
        }

        Page<PatternMakingTaskListVo> objects = PageHelper.startPage(dto);
        List<PatternMakingTaskListVo> list = getBaseMapper().patternMakingTaskList(qw);
        // 设置图片
        stylePicUtils.setStylePic(list, "stylePic");
        // 设置节点状态
        nodeStatusService.setNodeStatus(list);
        return objects.toPageInfo();
    }

    private static void userAuthQw(PatternMakingTaskSearchDto dto, BaseQueryWrapper qw) {
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        qw.eq(StrUtil.isNotBlank(dto.getCutterId()), "p.cutter_id", dto.getCutterId());
        qw.eq(StrUtil.isNotBlank(dto.getStitcherId()), "p.stitcher_id", dto.getStitcherId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Integer setSort(List<SetSortDto> dtoList) {
        int i = 0;
        boolean flg = false;
        for (SetSortDto setSortDto : dtoList) {
            UpdateWrapper<PatternMaking> puw = new UpdateWrapper<>();
            puw.set("sort", setSortDto.getSort());
            puw.eq("id", setSortDto.getId());
            setUpdateInfo(puw);
            flg = this.update(puw);
            if (flg) {
                i++;
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean suspend(SuspendDto dto) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("suspend", BaseGlobal.YES);
        uw.set("suspend_status", dto.getSuspendStatus());
        uw.set("suspend_remarks", dto.getSuspendRemarks());
        uw.eq("id", dto.getId());
        setUpdateInfo(uw);
        return update(uw);
    }

    @Override
    public boolean cancelSuspend(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("suspend", BaseGlobal.NO);
        uw.eq("id", id);
        setUpdateInfo(uw);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setKitting(String p, SetKittingDto dto) {
        if (!StrUtil.equalsAny(dto.getKitting(), BaseGlobal.NO, BaseGlobal.YES)) {
            throw new OtherException("是否齐套值不符合:0 or 1");
        }
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        if (StrUtil.equals(dto.getKitting(), BaseGlobal.YES)) {
            uw.set(p + "kitting_date", new Date());
        } else {
            uw.set(p + "kitting_date", null);
        }
        uw.set(p + "kitting", dto.getKitting());
        uw.eq("id", dto.getId());
        return update(uw);
    }

    @Override
    public StylePmDetailVo getDetailById(String id) {
        PatternMaking byId = getById(id);
        if (byId == null) {
            return null;
        }

        PatternMakingVo vo = BeanUtil.copyProperties(byId, PatternMakingVo.class);
        // 设置头像
        amcFeignService.setUserAvatarToObj(vo);
        // 查询款式设计信息
        StyleVo sampleDesignVo = styleService.getDetail(vo.getStyleId());
        // 设置头像
        amcFeignService.setUserAvatarToObj(sampleDesignVo);
        StylePmDetailVo result = BeanUtil.copyProperties(sampleDesignVo, StylePmDetailVo.class);
        result.setPatternMaking(vo);
        // 查询附件，纸样文件
        List<AttachmentVo> attachmentVoList = attachmentService.findByforeignId(vo.getId(), AttachmentTypeConstant.PATTERN_MAKING_PATTERN);
        vo.setAttachmentList(attachmentVoList);
        // 设置状态
        nodeStatusService.setNodeStatusToBean(vo, "nodeStatusList", "nodeStatus");
        return result;
    }

    @Override
    public boolean saveAttachment(SaveAttachmentDto dto) {
        attachmentService.saveAttachment(dto.getAttachmentList(), dto.getId(), AttachmentTypeConstant.PATTERN_MAKING_PATTERN);
        return true;
    }

    @Override
    public Map patternMakingSteps0(String userCompany) {
        Map<String, List<Map>> retMap = new HashMap<>();
        // 查询节点状态
        QueryWrapper<NodeStatus> nsQw = new QueryWrapper<>();
        nsQw.eq("company_code", userCompany);
        nsQw.eq("end_flg", BaseGlobal.NO);
        nsQw.eq("del_flag", BaseGlobal.NO);
        nsQw.orderByAsc("start_date");
        List<NodeStatus> nsList = nodeStatusService.nsWorkList(nsQw);
        if (CollUtil.isEmpty(nsList)) {
            return retMap;
        }
        if (CollUtil.isNotEmpty(nsList)) {
            List<String> pmIds = nsList.stream().map(NodeStatus::getDataId).collect(Collectors.toList());
            QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
            pmQw.in("id", pmIds);
            List<Map<String, Object>> pmList = getBaseMapper().workPatternMakingSteps(pmQw);
            if (CollUtil.isEmpty(pmList)) {
                return retMap;
            }
            Map<String, String> nsMap = nsList.stream().collect(Collectors.toMap(v -> v.getDataId(), k -> k.getNode() + StrUtil.DASHED + k.getStatus(), (a, b) -> b));
            for (Map patternMaking : pmList) {
                if (nsMap.containsKey(patternMaking.get("id"))) {
                    List<Map> patternMakings = null;
                    if (retMap.containsKey(nsMap.get(patternMaking.get("id")))) {
                        patternMakings = retMap.get(nsMap.get(patternMaking.get("id")));
                        patternMakings.add(patternMaking);
                    } else {
                        patternMakings = new ArrayList<>();
                        patternMakings.add(patternMaking);
                    }
                    retMap.put(nsMap.get(patternMaking.get("id")), patternMakings);
                }
            }
        }
        return retMap;
    }

    @Override
    public PageInfo patternMakingSteps(PatternMakingCommonPageSearchDto dto) {

        // 查询样衣信息
        QueryWrapper<Style> sdQw = new QueryWrapper<>();

        //临时逻辑,如果请求参数有打版指令的,就先查打版指令,再根据打版指令查的样衣id查询
        if (StrUtil.isNotBlank(dto.getPatternNo())) {
            QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
            pmQw.like("pattern_no", dto.getPatternNo());
            pmQw.select("style_id");
            List<PatternMaking> list = this.list(pmQw);
            if (CollUtil.isEmpty(list)) {
                return new PageInfo();
            }
            List<String> ids = list.stream().map(PatternMaking::getStyleId).collect(Collectors.toList());
            sdQw.in("id", ids);
        }


        sdQw.like(StrUtil.isNotBlank(dto.getSearch()), "design_no", dto.getSearch());
        sdQw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        sdQw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        sdQw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        sdQw.eq(StrUtil.isNotBlank(dto.getBandCode()), "band_code", dto.getBandCode());
        sdQw.eq(StrUtil.isNotBlank(dto.getDesignerId()), "designer_id", dto.getDesignerId());
        sdQw.in(StrUtil.isNotBlank(dto.getDesignerIds()), "designer_id", StrUtil.split(dto.getDesignerIds(), StrUtil.COMMA));
        sdQw.in(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "planning_season_id", StrUtil.split(dto.getPlanningSeasonId(), StrUtil.COMMA));
        dataPermissionsService.getDataPermissionsForQw(sdQw, DataPermissionsBusinessTypeEnum.patternMakingSteps.getK());
        sdQw.eq(COMPANY_CODE, getCompanyCode());
        sdQw.eq("del_flag", BaseGlobal.NO);
        sdQw.eq("status", BasicNumber.TWO.getNumber());
        sdQw.exists("select id from t_pattern_making where style_id=t_style.id and del_flag='0'");
        if (StrUtil.isNotBlank(dto.getOrderBy())) {
            dto.setOrderBy("create_date desc");
        }
        Page page = PageHelper.startPage(dto);
        List<Style> sdList = styleService.list(sdQw);
        PageInfo pageInfo = page.toPageInfo();
        if (CollUtil.isEmpty(sdList)) {
            return null;
        }
        List<StyleStepVo> styleStepVos = BeanUtil.copyToList(sdList, StyleStepVo.class);
//        PageInfo pageInfo = BeanUtil.copyProperties(sdPageInfo, PageInfo.class, "list");
//        pageInfo.setList(sampleDesignStepVos);
        pageInfo.setList(styleStepVos);
        stylePicUtils.setStylePic(styleStepVos, "stylePic");
        // 查询打版指令
        List<String> sdIds = styleStepVos.stream().map(StyleStepVo::getId).collect(Collectors.toList());
        QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
        pmQw.in("style_id", sdIds);
        List<PatternMaking> pmList = this.list(pmQw);
        if (CollUtil.isEmpty(pmList)) {
            return pageInfo;
        }
        List<String> pmIds = pmList.stream().map(PatternMaking::getId).collect(Collectors.toList());
        List<StyleStepVo.PatternMakingStepVo> patternMakingStepVos = BeanUtil.copyToList(pmList, StyleStepVo.PatternMakingStepVo.class);
        // 查询节点状态
        QueryWrapper<NodeStatus> nsQw = new QueryWrapper<>();
        nsQw.in("data_id", pmIds);
        List<NodeStatus> nsList = nodeStatusService.list(nsQw);
        if (CollUtil.isNotEmpty(nsList)) {
            Map<String, List<NodeStatus>> nsMap = nsList.stream().collect(Collectors.groupingBy(NodeStatus::getDataId));
            for (StyleStepVo.PatternMakingStepVo patternMakingStepVo : patternMakingStepVos) {
                Map<String, NodeStatus> stringNodeStatusMap = Optional.ofNullable(nsMap.get(patternMakingStepVo.getId())).map(item -> {
                    return item.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> {
                        if (DateUtil.compare(b.getStartDate(), a.getStartDate()) > 0) {
                            return b;
                        }
                        return a;
                    }));
                }).orElse(null);
                patternMakingStepVo.setNodeStatus(stringNodeStatusMap);
            }
        }
        LinkedHashMap<String, List<StyleStepVo.PatternMakingStepVo>> pmStepMap = patternMakingStepVos.stream().collect(Collectors.groupingBy(k -> k.getStyleId(), LinkedHashMap::new, Collectors.toList()));
        for (StyleStepVo styleStepVo : styleStepVos) {
            styleStepVo.setPatternMakingSteps(pmStepMap.get(styleStepVo.getId()));
        }

        return pageInfo;
    }

    @Override
    public PageInfo<NodeListVo> allProgressSteps(NodestatusPageSearchDto dto, String userCompany) {
        dto.setCompanyCode(userCompany);
        PageHelper.startPage(dto);
        List<NodeListVo> list = this.getBaseMapper().getProgressSteps(dto);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(String userId, List<NodeStatusChangeDto> list, GroupUser groupUser) {
        for (NodeStatusChangeDto dto : list) {
            nodeStatusChange(userId, dto, groupUser);
        }
        return true;
    }

    @Override
    public PageInfo sampleBoardList(PatternMakingCommonPageSearchDto dto) {
        BaseQueryWrapper<SampleBoardVo> qw = new BaseQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getBandCode()), "s.band_code", StrUtil.split(dto.getBandCode(), StrUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "s.planning_season_id", StrUtil.split(dto.getPlanningSeasonId(), StrUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        qw.eq(StrUtil.isNotBlank(dto.getSampleType()), "p.sample_type", dto.getSampleType());
        qw.like(StrUtil.isNotBlank(dto.getSampleBarCode()), "p.sample_bar_code", dto.getSampleBarCode());
        if(StrUtil.isNotBlank(dto.getPmCreateDate())){
            String[] s = dto.getPmCreateDate().split(",");
            s[0] = s[0] + " 00:00:00";
            s[1] = s[1] + " 23:59:59";
            qw.between("p.create_date",s);
        }
        qw.findInSet("s.pattern_parts", dto.getPatternParts());
        if (StrUtil.isNotBlank(dto.getDesignerIds())) {
            String[] split = dto.getDesignerIds().split(",");
            qw.in("s.designer_id", Arrays.asList(split));
        }

        if (StrUtil.isNotBlank(dto.getTechnicianKittingDate()) && dto.getTechnicianKittingDate().split(",").length > 1) {
            qw.gt(StrUtil.isNotBlank(dto.getTechnicianKittingDate()), "p.technician_kitting_date", dto.getTechnicianKittingDate().split(",")[0]);
            qw.lt(StrUtil.isNotBlank(dto.getTechnicianKittingDate()), "p.technician_kitting_date", dto.getTechnicianKittingDate().split(",")[1]);
        }
        if (StrUtil.isNotBlank(dto.getBfzgxfsj()) && dto.getBfzgxfsj().split(",").length > 1) {
            qw.exists(StrUtil.isNotBlank(dto.getBfzgxfsj()),
                    "select 1 from t_node_status where p.id=data_id and node ='技术中心' and status='版房主管下发' and start_date >={0} and {1} >= end_date"
                    , dto.getBfzgxfsj().split(",")[0], dto.getBfzgxfsj().split(",")[1]);
        }
        // bsjssj
        if (StrUtil.isNotBlank(dto.getBsjssj()) && dto.getBsjssj().split(",").length > 1) {
            qw.exists(StrUtil.isNotBlank(dto.getBsjssj()),
                    "select 1 from t_node_status where p.id=data_id and node ='打版任务' and status='已接收' and start_date >={0} and {1} >= end_date"
                    , dto.getBsjssj().split(",")[0], dto.getBsjssj().split(",")[1]);
        }
        //zysj
        if (StrUtil.isNotBlank(dto.getZysj()) && dto.getZysj().split(",").length > 1) {
            qw.exists(StrUtil.isNotBlank(dto.getZysj()),
                    "select 1 from t_node_status where p.id=data_id and node ='打版任务' and status='打版完成' and start_date >={0} and {1} >= end_date"
                    , dto.getZysj().split(",")[0], dto.getZysj().split(",")[1]);
        }
        //cjsj
        if (StrUtil.isNotBlank(dto.getCjsj()) && dto.getCjsj().split(",").length > 1) {
            qw.exists(StrUtil.isNotBlank(dto.getCjsj()),
                    "select 1 from t_node_status where p.id=data_id and node ='样衣任务' and status='裁剪开始' and start_date >={0}"
                    , dto.getCjsj().split(",")[0]);
            qw.exists(StrUtil.isNotBlank(dto.getCjsj()),
                    "select 1 from t_node_status where p.id=data_id and node ='样衣任务' and status='裁剪完成' and {0} >= start_date"
                    , dto.getCjsj().split(",")[1]);
        }
        //cfsj
        if (StrUtil.isNotBlank(dto.getCfsj()) && dto.getCfsj().split(",").length > 1) {
            qw.exists(StrUtil.isNotBlank(dto.getCfsj()),
                    "select 1 from t_node_status where p.id=data_id and node ='样衣任务' and status='车缝进行中' and start_date >={0}"
                    , dto.getCfsj().split(",")[0]);
            qw.exists(StrUtil.isNotBlank(dto.getCfsj()),
                    "select 1 from t_node_status where p.id=data_id and node ='样衣任务' and status='车缝完成' and {0} >= start_date"
                    , dto.getCfsj().split(",")[1]);
        }
        // yywcsj
        if (StrUtil.isNotBlank(dto.getYywcsj()) && dto.getYywcsj().split(",").length > 1) {
            qw.exists(StrUtil.isNotBlank(dto.getYywcsj()),
                    "select 1 from t_node_status where p.id=data_id and node ='样衣任务' and status='样衣完成' and start_date >={0} and {1} >= end_date order by start_date desc"
                    , dto.getYywcsj().split(",")[0], dto.getYywcsj().split(",")[1]);
        }

        Page<SampleBoardVo> objects = PageHelper.startPage(dto);
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.sampleBoard.getK(), "s.");
        if(!StringUtils.isBlank(dto.getDeriveflag())){
            qw.groupBy("p.id");
            baseMapper.deriveList(qw);
            if(StrUtil.equals(dto.getImgFlag(),BaseGlobal.YES)){
                /*带图片只能导出3000条*/
                if(objects.toPageInfo().getList().size() >3000){
                    throw new OtherException("带图片最多只能导出3000条");
                }
            }

            return objects.toPageInfo();
        }
        List<SampleBoardVo> list = getBaseMapper().sampleBoardList(qw);

        stylePicUtils.setStylePic(list, "stylePic");
        // 设置节点状态数据
        nodeStatusService.setNodeStatusToListBean(list, "patternMakingId", null, "nodeStatus");
        minioUtils.setObjectUrlToList(objects.toPageInfo().getList(), "samplePic");
        return objects.toPageInfo();
    }

    /**
     * 导出样衣看板
     *
     * @param response
     * @param dto
     */
    @Override
    public void deriveExcel(HttpServletResponse response, PatternMakingCommonPageSearchDto dto) throws IOException, InterruptedException {
        dto.setDeriveflag(BaseGlobal.YES);
        PageInfo<SampleBoardExcel> sampleBoardVoPageInfo = sampleBoardList(dto);
        List<SampleBoardExcel> excelList = sampleBoardVoPageInfo.getList();
        /*开启一个线程池*/
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(excelList.size()))
                .build();
        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*获取图片链接*/
                stylePicUtils.setStylePic(excelList, "stylePic");
                /*计时器*/
                CountDownLatch countDownLatch = new CountDownLatch(excelList.size());
                for (SampleBoardExcel sampleBoardExcel : excelList) {
                    executor.submit(() -> {
                        try {
                            final String stylePic = sampleBoardExcel.getStylePic();
                            sampleBoardExcel.setPic(HttpUtil.downloadBytes(stylePic));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(excelList, SampleBoardExcel.class, "样衣看板.xlsx", new ExportParams("样衣看板", "样衣看板", ExcelType.HSSF), response);
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
    @Override
    public boolean receiveSample(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, StrUtil.COMMA));
        uw.set("receive_sample", BaseGlobal.YES);
        uw.set("receive_sample_date", new Date());
        /*消息通知*/
        PatternMaking patternMaking = baseMapper.selectById(id);
        Style style = styleService.getById(patternMaking.getStyleId());
        messageUtils.receiveSampleSendMessage(patternMaking.getPatternRoomId(), style.getDesignNo(), baseController.getUser());
        return update(uw);
    }

    @Override
    public List<SampleUserVo> getAllPatternDesignList(PatternUserSearchVo vo) {

        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.select("DISTINCT pattern_design_id as user_id, pattern_design_name as name");
        qw.lambda().eq(PatternMaking::getCompanyCode, getCompanyCode())
                .isNotNull(PatternMaking::getPatternDesignId)
                .isNotNull(PatternMaking::getPatternDesignName)
                .ne(PatternMaking::getPatternDesignName, "")
                .ne(PatternMaking::getPatternDesignId, "")
                // .eq(StrUtil.isNotBlank(vo.getFinishFlag()), PatternMaking::getFinishFlag, vo.getFinishFlag())
        ;
        List<Map<String, Object>> maps = listMaps(qw);
        List<SampleUserVo> list = BeanUtil.copyToList(maps, SampleUserVo.class);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }

    @Override
    public List<SampleUserVo> getAllCutterList(PatternUserSearchVo vo) {
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.select("DISTINCT cutter_id as user_id, cutter_name as name");
        qw.lambda().eq(PatternMaking::getCompanyCode, getCompanyCode())
                .isNotNull(PatternMaking::getCutterName)
                .isNotNull(PatternMaking::getCutterId)
                .ne(PatternMaking::getCutterName, "")
                .ne(PatternMaking::getCutterId, "")
                // .eq(StrUtil.isNotBlank(vo.getFinishFlag()), PatternMaking::getFinishFlag, vo.getFinishFlag())
        ;
        List<Map<String, Object>> maps = listMaps(qw);
        List<SampleUserVo> list = BeanUtil.copyToList(maps, SampleUserVo.class);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }

    @Override
    public List<SampleUserVo> getAllStitcherList(PatternUserSearchVo vo) {
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.select("DISTINCT stitcher_id as user_id,  stitcher as name");
        qw.lambda().eq(PatternMaking::getCompanyCode, getCompanyCode())
                .isNotNull(PatternMaking::getStitcher)
                .isNotNull(PatternMaking::getStitcherId)
                .ne(PatternMaking::getStitcher, "")
                .ne(PatternMaking::getStitcherId, "")
                // .eq(StrUtil.isNotBlank(vo.getFinishFlag()), PatternMaking::getFinishFlag, vo.getFinishFlag())
        ;

        List<Map<String, Object>> maps = listMaps(qw);
        List<SampleUserVo> list = BeanUtil.copyToList(maps, SampleUserVo.class);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean finish(String id) {
        PatternMaking pm = getById(id);
        if (pm == null) {
            throw new OtherException("打版指令不存在");
        }
        PatternMaking upm = new PatternMaking();
        upm.setId(id);
        upm.setFinishFlag(BaseGlobal.YES);
        upm.setSampleCompleteFlag(BaseGlobal.YES);
        updateById(upm);
        return true;
    }

    /**
     * 上传样衣图
     *
     * @param dto
     * @return
     */
    @Override
    public boolean samplePicUpload(SamplePicUploadDto dto) {
        PatternMaking patternMaking = baseMapper.selectById(dto.getId());
        patternMaking.setSamplePic(CommonUtils.removeQuery(dto.getSamplePic()));
        baseMapper.updateById(patternMaking);
        return true;
    }


    @Override
    public List prmDataOverview(String time) {
        List result = new ArrayList(16);
        List<String> timeRange = StrUtil.split(time, CharUtil.COMMA);
        // 打版需求总数：版房内所有打版需求的总数，包括待打版、打版中、打版完成和暂停的任务。
        QueryWrapper allQw = new QueryWrapper<>();
        prmDataOverviewCommonQw(allQw, timeRange, BaseGlobal.NO);
        result.add(CollUtil.newArrayList("打版需求总数", count(allQw)));

        List<Map<String, Object>> nsCountList = getBaseMapper().nsCount(allQw);
        Map<String, Long> nsCountMap = new HashMap<>(16);
        for (Map<String, Object> nsCount : nsCountList) {
            if (!nsCount.containsKey("nodeStatus")) {
                continue;
            }
            nsCountMap.put(MapUtil.getStr(nsCount, "nodeStatus"), MapUtil.getLong(nsCount, "total", 0L));
        }
        result.add(CollUtil.newArrayList("待打版数量", MapUtil.getLong(nsCountMap, "打版任务-待接收", 0L)));
        result.add(CollUtil.newArrayList("打版中数量", MapUtil.getLong(nsCountMap, "打版任务-打版中", 0L)));
        result.add(CollUtil.newArrayList("打版完成", MapUtil.getLong(nsCountMap, "打版任务-打版完成", 0L)));
        QueryWrapper suspendQw = new QueryWrapper();
        prmDataOverviewCommonQw(suspendQw, timeRange, BaseGlobal.YES);
        result.add(CollUtil.newArrayList("打版暂停", count(suspendQw)));
        QueryWrapper sdQw = new QueryWrapper();
        prmDataOverviewCommonQw(sdQw, timeRange, BaseGlobal.NO);
        sdQw.eq("node", "样衣任务");
        result.add(CollUtil.newArrayList("样衣制作总数", count(sdQw)));
        result.add(CollUtil.newArrayList("裁剪中数量", MapUtil.getLong(nsCountMap, "样衣任务-裁剪开始", 0L)));
        result.add(CollUtil.newArrayList("车缝中", MapUtil.getLong(nsCountMap, "样衣任务-车缝开始", 0L)));
        return result;
    }

    @Override
    public JSONObject getNodeStatusConfig(GroupUser user, String node, String status, String dataId) {
        if (StrUtil.isNotBlank(dataId)) {
            PatternMaking bean = getById(dataId);
            JSONObject config = nodeStatusService.getNodeNextAndAuth(user, bean, NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, NodeStatusConfigService.NEXT);
            return config;
        }
        return nodeStatusService.getNodeStatusConfig(NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, node, status);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean assignmentUser(GroupUser groupUser, AssignmentUserDto dto) {
        PatternMaking byId = getById(dto.getId());
        if (byId == null) {
            throw new OtherException("任务不存在");
        }
        if (!StrUtil.equals(byId.getNode(), EnumNodeStatus.GARMENT_WAITING_ASSIGNMENT.getNode()) ||
                !StrUtil.equals(byId.getStatus(), EnumNodeStatus.GARMENT_WAITING_ASSIGNMENT.getStatus())
        ) {
            throw new OtherException("节点不匹配");
        }
        sort(byId, true);
        byId.setStitcher(dto.getStitcher());
        byId.setStitcherId(dto.getStitcherId());
        byId.setSglKitting(dto.getSglKitting());
        byId.setSampleBarCode(dto.getSampleBarCode());
        byId.setSglKittingDate(new Date());
        byId.setStitcherRemark(dto.getStitcherRemark());
        // 分配后进入下一节点
        nodeStatusService.nextOrPrev(groupUser, byId, NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, NodeStatusConfigService.NEXT);
        updateById(byId);
        sort(byId, false);
        return true;
    }

    @Override
    public List<PatternDesignVo> pdTaskDetail(String companyCode) {
        List<String> planningSeasonIdByUserId = amcFeignService.getPlanningSeasonIdByUserId(getUserId());
        if (CollUtil.isEmpty(planningSeasonIdByUserId)) {
            return null;
        }
        List<PatternDesignVo> patternDesignList = getPatternDesignList(CollUtil.join(planningSeasonIdByUserId, StrUtil.COMMA));
        return patternDesignList;
    }

    private void prmDataOverviewCommonQw(QueryWrapper qw, List timeRange, String suspend) {
        qw.ne("del_flag", BaseGlobal.YES);
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(StrUtil.isNotBlank(suspend), "suspend", suspend);
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        qw.between(CollUtil.isNotEmpty(timeRange), "create_date", CollUtil.getFirst(timeRange), CollUtil.getLast(timeRange));
    }


    public String getNextCode(Style style) {
        String designNo = style.getDesignNo();
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("style_id", style.getId());
        long count = count(qw);
        String code = StrUtil.padPre(String.valueOf(count + 1), 3, "0");
        return designNo + StrUtil.DASHED + code;

    }


    @Override
    public PageInfo queryPageInfo(PatternMakingCommonPageSearchDto dto) {
        String companyCode = getCompanyCode();
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(dto.getStatus()), "pk.status", dto.getStatus());
        qw.eq("pk.company_code", companyCode);

        Page<PatternMakingForSampleVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getAllList(qw);
        List<PatternMakingForSampleVo> result = objects.getResult();

        return objects.toPageInfo();
    }


    @Override
    public ArrayList<ArrayList> versionComparisonViewWeekMonth(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto, token, TechnologyBoardConstant.VERSION_COMPARISON);
        // 2、缓存为空则直接返回，
        if (null == dataMap) {
            return null;
        }
        return (ArrayList<ArrayList>) dataMap.get("dataLists");
    }

    /**
     * 品类汇总统计
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token                         token
     * @return 结果集
     */
    @Override
    public ArrayList<ArrayList> categorySummaryCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto, token, TechnologyBoardConstant.CATEGORY_SUMMARY_COUNT);
        // 2、缓存为空则直接返回，
        if (null == dataMap) {
            return null;
        }
        return (ArrayList<ArrayList>) dataMap.get("dataLists");
    }

    /**
     * 根据时间按周月 统计样衣产能总数
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token                         令牌
     * @return 返回集合数据
     */
    @Override
    public ArrayList<ArrayList> sampleCapacityTotalCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto, token, TechnologyBoardConstant.SAMPLE_CAPACITY_TOTAL);
        // 2、缓存为空则直接返回，
        if (null == dataMap) {
            return null;
        }
        return (ArrayList<ArrayList>) dataMap.get("dataLists");
    }

    /**
     * 产能对比统计
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token                         token
     * @return 返回集合数据
     */
    @Override
    public ArrayList<ArrayList> capacityContrastStatistics(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto, token, TechnologyBoardConstant.CAPACITY_CONTRAST);
        // 2、缓存为空则直接返回，
        if (null == dataMap) {
            return null;
        }
        return (ArrayList<ArrayList>) dataMap.get("dataLists");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nextOrPrev(Principal user, String id, String np) {
        PatternMaking pm = getById(id);
        checkBreak(pm);
        sort(pm, true);
        GroupUser groupUser = userUtils.getUserBy(user);
        if (pm == null) {
            throw new OtherException("任务不存在");
        }
        // 跳转
        boolean flg = nodeStatusService.nextOrPrev(groupUser, pm, NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, np);
        checkBreak(pm);
        updateById(pm);
        // 重置排序
        update(new UpdateWrapper<PatternMaking>().lambda().eq(PatternMaking::getId, id).set(PatternMaking::getSort, null));
        sort(pm, false);
        return flg;
    }

    @Override
    public void checkBreak(PatternMaking pm) {
        if (StrUtil.equals(pm.getNode(), "打版任务") && StrUtil.equals(pm.getBreakOffPattern(), BaseGlobal.YES)) {
            throw new OtherException("打版任务已中断");
        }
        if (StrUtil.equals(pm.getNode(), "样衣任务") && StrUtil.equals(pm.getBreakOffSample(), BaseGlobal.YES)) {
            throw new OtherException("样衣任务已中断");
        }
        if (StrUtil.equals(pm.getSuspend(), BaseGlobal.YES)) {
            throw new OtherException("任务已经挂起");
        }
    }


    /**
     * 排序
     *
     * @param pm
     * @param excludeSelf 排除自己
     */
    public void sort(PatternMaking pm, boolean excludeSelf) {
        String node = pm.getNode();
        String status = pm.getStatus();
        JSONObject nodeStatusConfig = nodeStatusService.getNodeStatusConfig(NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, node, status);
        if (nodeStatusConfig == null) {
            return;
        }
        String userField = nodeStatusConfig.getString("userName");
        // 样衣组长特殊处理
        if (StrUtil.equals(userField, "样衣组长")) {
            userField = "patternRoomId";
        }
        if (StrUtil.isBlank(userField)) {
            return;
        }
        boolean hasField = ReflectUtil.hasField(PatternMaking.class, userField);
        if (!hasField) {
            return;
        }
        // 重新排序
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.select("id");
        qw.lambda().eq(PatternMaking::getNode, node).eq(PatternMaking::getStatus, status);
        if (excludeSelf) {
            qw.ne("id", pm.getId());
        }
        if (StrUtil.equals(node, "打版任务")) {
            qw.eq("break_off_pattern", "0");
        }
        if (StrUtil.equals(node, "样衣任务")) {
            qw.eq("break_off_sample", "0");
        }
        qw.eq(StrUtil.toUnderlineCase(userField), BeanUtil.getProperty(pm, userField));
        // 黑单处理
//        if (StrUtil.equals(pm.getUrgency(), BasicNumber.ZERO.getNumber())) {
//            qw.lambda().eq(PatternMaking::getUrgency, BasicNumber.ZERO.getNumber());
//        } else {
//            qw.lambda().ne(PatternMaking::getUrgency, BasicNumber.ZERO.getNumber());
//        }
        qw.last("order by sort is null  , sort asc ");

        List<Map<String, Object>> ids = listMaps(qw);
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<PatternMaking> updataList = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            PatternMaking u = new PatternMaking();
            u.setId(MapUtil.getStr(ids.get(i), "id"));
            u.setSort(new BigDecimal(String.valueOf(i)).setScale(0));
            updataList.add(u);
        }
        updateBatchById(updataList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean patternMakingScore(Principal user, String id, BigDecimal score) {
        PatternMaking bean = getById(id);
        if (bean == null) {
            throw new OtherException("打版信息为空");
        }
        GroupUser groupUser = userUtils.getUserBy(user);
        //校验是否是样衣组长
//        boolean sampleTeamLeader = amcFeignService.isSampleTeamLeader(bean.getPatternRoomId(), groupUser.getId());
//        if (!sampleTeamLeader) {
//            throw new OtherException("您不是" + bean.getPatternRoom() + "的样衣组长");
//        }
        PatternMaking updateBean = new PatternMaking();
        updateBean.setPatternMakingScore(score);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.lambda().eq(PatternMaking::getId, id);
        return update(updateBean, uw);
    }

    @Override
    public boolean patternMakingQualityScore(Principal user, String id, BigDecimal score) {
        PatternMaking bean = getById(id);
        if (bean == null) {
            throw new OtherException("打版信息为空");
        }
        GroupUser groupUser = userUtils.getUserBy(user);
        // 校验是否是样衣组长
        boolean sampleTeamLeader = amcFeignService.isSampleTeamLeader(bean.getPatternRoomId(), groupUser.getId());
        if (!sampleTeamLeader) {
            throw new OtherException("您不是" + bean.getPatternRoom() + "的样衣组长");
        }
        PatternMaking updateBean = new PatternMaking();
        updateBean.setPatternMakingQualityScore(score);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.lambda().eq(PatternMaking::getId, id);
        return update(updateBean, uw);
    }

    @Override
    public boolean sampleMakingQualityScore(Principal user, String id, BigDecimal score) {
        PatternMaking bean = getById(id);
        if (bean == null) {
            throw new OtherException("打版信息为空");
        }
        GroupUser groupUser = userUtils.getUserBy(user);
        // 校验是否是样衣组长
        boolean sampleTeamLeader = amcFeignService.isSampleTeamLeader(bean.getPatternRoomId(), groupUser.getId());
        if (!sampleTeamLeader) {
            throw new OtherException("您不是" + bean.getPatternRoom() + "的样衣组长");
        }
        PatternMaking updateBean = new PatternMaking();
        updateBean.setSampleMakingQualityScore(score);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.lambda().eq(PatternMaking::getId, id);
        return update(updateBean, uw);
    }


    @Override
    public boolean sampleMakingScore(Principal user, String id, BigDecimal score) {
        PatternMaking bean = getById(id);
        if (bean == null) {
            throw new OtherException("打版信息为空");
        }
        GroupUser groupUser = userUtils.getUserBy(user);
        // 校验是否是样衣组长
        boolean sampleTeamLeader = amcFeignService.isSampleTeamLeader(bean.getPatternRoomId(), groupUser.getId());
        if (!sampleTeamLeader) {
            throw new OtherException("您不是" + bean.getPatternRoom() + "的样衣组长");
        }
        PatternMaking updateBean = new PatternMaking();
        updateBean.setSampleMakingScore(score);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.lambda().eq(PatternMaking::getId, id);
        return update(updateBean, uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setSampleBarCode(SetSampleBarCodeDto dto) {
        /*查询样衣码是否重复*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sample_bar_code", dto.getSampleBarCode());
        queryWrapper.ne("id", dto.getId());
        queryWrapper.eq("del_flag", BaseGlobal.NO);
        List<PatternMaking> patternMakingList = baseMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(patternMakingList)) {
            throw new OtherException("样衣条码重复");
        }
        PatternMaking update = new PatternMaking();
        update.setSampleBarCode(dto.getSampleBarCode());
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.lambda().eq(PatternMaking::getId, dto.getId());
        return update(update, uw);
    }

    @Override
    public List<PatternDesignVo> getStitcherList(String planningSeasonId) {
        List<UserCompany> userList = amcFeignService.getTeamUserListByPost(planningSeasonId, "车缝工");
        if (CollUtil.isEmpty(userList)) {
            return null;
        }
        List<String> userIds = userList.stream().map(UserCompany::getUserId).collect(Collectors.toList());
        List<PatternMaking> patternMakings = getBaseMapper().getPatternMakingSewingStatus(new QueryWrapper<>().in("p.stitcher_id", userIds)
                .isNotNull("p.stitcher_id").gt("p.sewing_status", 0));

        Map<String, List<PatternMaking>> qtyMap = patternMakings.stream().collect(Collectors.groupingBy(PatternMaking::getStitcherId));
        List<PatternDesignVo> result = new ArrayList<>();
        for (UserCompany user : userList) {
            PatternDesignVo patternDesignVo = BeanUtil.copyProperties(user, PatternDesignVo.class);
            LinkedHashMap<String, Long> sampleTypeCount = new LinkedHashMap<>(16);
            if (CollectionUtil.isNotEmpty(qtyMap.get(user.getUserId()))) {
                sampleTypeCount.put("未开始", qtyMap.get(user.getUserId()).stream().filter(f -> "1".equals(f.getSewingStatus())).count());
                sampleTypeCount.put("进行中", qtyMap.get(user.getUserId()).stream().filter(f -> "2".equals(f.getSewingStatus())).count());
                sampleTypeCount.put("已完成", qtyMap.get(user.getUserId()).stream().filter(f -> "3".equals(f.getSewingStatus())).count());
            } else {
                sampleTypeCount.put("未开始", 0L);
                sampleTypeCount.put("进行中", 0L);
                sampleTypeCount.put("已完成", 0L);
            }
            String deptName = Optional.ofNullable(user.getDeptList()).map(item -> item.stream().map(Dept::getName).collect(Collectors.joining(StrUtil.COMMA))).orElse("");
            patternDesignVo.setDeptName(deptName);
            patternDesignVo.setSampleTypeCount(sampleTypeCount);
            result.add(patternDesignVo);
        }
        return result;
    }


    /**
     * 产能对比统计 查数据库
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key                           token
     * @return 返回集合数据
     */
    private Map<String, Object> capacityContrastStatisticsView(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key) {
        // 1、拼接锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.CAPACITY_CONTRAST + patternMakingWeekMonthViewDto.getCompanyCode() + super.getUserId();
        try {
            // 1.1、缓存数据格式
            Map<String, Object> dataMap = Maps.newHashMap();
            // 2、返回数据集合
            ArrayList<ArrayList> dataLists = new ArrayList<>();
            // 2.1、添加表头
            dataLists.add(CollUtil.newArrayList("product", "打版需求数", "打版产能数"));
            // 3、参数校验
            this.paramCheck(patternMakingWeekMonthViewDto);
            QueryWrapper qw = new QueryWrapper();
            dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.capacity_contrast_statistics.getK(), "s.");
            // 4、查询数据库  打版需求
            List<PatternMakingWeekMonthViewVo> demandList = baseMapper.capacityContrastDemandStatistics(patternMakingWeekMonthViewDto, qw);
            // 4.1、查询数据库  打版产能
            List<PatternMakingWeekMonthViewVo> contrastList = baseMapper.capacityContrastCapacityStatistics(patternMakingWeekMonthViewDto, qw);
            // 4.2、都为空 都返回
            if (CollectionUtil.isEmpty(demandList) && CollectionUtil.isEmpty(contrastList)) {
                return null;
            }
            // 5、取出两个集合的有数据的年份，然后汇总，去重有数据的年份
            List<String> yearWeekDemandList = demandList.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
            List<String> yearWeekContrasList = contrastList.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
            yearWeekDemandList.addAll(yearWeekContrasList);
            List<String> yearWeekList = yearWeekDemandList.stream().distinct().collect(Collectors.toList());
            // 6、打版需求列表转为map,以年份为key
            Map<String, PatternMakingWeekMonthViewVo> demandMap = this.getDataByYearWeek(demandList);
            // 6.1、打版产能列表转为map,以年份为key
            Map<String, PatternMakingWeekMonthViewVo> contrastMap = this.getDataByYearWeek(contrastList);
            // 7、 循环有数据的年份，拼接数据
            for (String yearWeek : yearWeekList) {
                ArrayList<String> arrayList = new ArrayList<>();
                // 添加年份
                arrayList.add(yearWeek);
                // 7.1、初版数据
                if (demandMap != null && null != demandMap.get(yearWeek)) {
                    PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = demandMap.get(yearWeek);
                    arrayList.add(null != patternMakingWeekMonthViewVo.getRequirementNumSum() ? patternMakingWeekMonthViewVo.getRequirementNumSum() : String.valueOf(BaseGlobal.ZERO));
                }
                // 7.2、改版样数据
                if (contrastMap != null && null != contrastMap.get(yearWeek)) {
                    PatternMakingWeekMonthViewVo revisionTwo = contrastMap.get(yearWeek);
                    arrayList.add(null != revisionTwo.getRequirementNumSum() ? revisionTwo.getRequirementNumSum() : String.valueOf(BaseGlobal.ZERO));
                }
                dataLists.add(arrayList);
            }

            // 8. 缓存数据
            this.setRedisData(dataMap, dataLists, key, lockKey, DateUtils.HOUR_MINUTES, DateUtils.SECONDS);
            return dataMap;
        } catch (Exception e) {
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }
        return null;
    }

    /**
     * 根据时间按周月 统计样衣产能总数  查询数据库
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key                           缓存key
     * @return 返回集合数据
     */
    public Map<String, Object> querySampleCapacityTotalCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key) {
        // 1、缓存数据格式
        Map<String, Object> dataMap = Maps.newHashMap();
        // 2、返回数据集合
        ArrayList<ArrayList> dataLists = new ArrayList<>();
        // 2.1、添加表头
        dataLists.add(CollUtil.newArrayList("product", "裁剪数", "车缝数", "样衣完成总数"));
        // 3、参数校验
        this.paramCheck(patternMakingWeekMonthViewDto);
        // 5、参数样衣状态: 车缝完成、裁剪完成、样衣完成
        List<String> statusList = CollUtil.newArrayList(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus(),
                EnumNodeStatus.GARMENT_SEWING_COMPLETE.getStatus(), EnumNodeStatus.GARMENT_COMPLETE.getStatus());
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        QueryWrapper qw = new QueryWrapper();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.sample_capacity_total_count.getK(), "s.");
        List<PatternMakingWeekMonthViewVo> patternMakingWeekMonthViewVos = baseMapper.sampleCapacityTotalCount(patternMakingWeekMonthViewDto, qw);
        if (CollectionUtil.isEmpty(patternMakingWeekMonthViewVos)) {
            return null;
        }
        // 6、解析数据 取出有数据的年份
        List<String> yearWeekList = patternMakingWeekMonthViewVos.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
        // 6.1、 根据节点状态分组
        Map<String, List<PatternMakingWeekMonthViewVo>> patternMakingAllMap = patternMakingWeekMonthViewVos.stream().filter(item -> StrUtil.isNotBlank(item.getStatus()))
                .collect(Collectors.groupingBy(item -> item.getStatus()));
        // 6.2、获取车缝完成数据
        Map<String, PatternMakingWeekMonthViewVo> garmentCuttingCompleteMap = Maps.newHashMap();
        if (null != patternMakingAllMap.get(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus())) {
            List<PatternMakingWeekMonthViewVo> garmentCuttingCompleteList = patternMakingAllMap.get(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus());
            garmentCuttingCompleteMap = this.getDataByYearWeek(garmentCuttingCompleteList);
        }
        // 6.3、获取裁剪完成数据
        Map<String, PatternMakingWeekMonthViewVo> garmentSewingCompleteMap = Maps.newHashMap();
        if (null != patternMakingAllMap.get(EnumNodeStatus.GARMENT_SEWING_COMPLETE.getStatus())) {
            List<PatternMakingWeekMonthViewVo> garmentCuttingCompleteList = patternMakingAllMap.get(EnumNodeStatus.GARMENT_SEWING_COMPLETE.getStatus());
            garmentSewingCompleteMap = this.getDataByYearWeek(garmentCuttingCompleteList);
        }
        // 6.4、获取样衣完成数据
        Map<String, PatternMakingWeekMonthViewVo> garmentCompleteMap = Maps.newHashMap();
        if (null != patternMakingAllMap.get(EnumNodeStatus.GARMENT_COMPLETE.getStatus())) {
            List<PatternMakingWeekMonthViewVo> garmentCuttingCompleteList = patternMakingAllMap.get(EnumNodeStatus.GARMENT_COMPLETE.getStatus());
            garmentCompleteMap = this.getDataByYearWeek(garmentCuttingCompleteList);
        }
        // 7、拼接数据
        for (String yearWeek : yearWeekList) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(yearWeek);
            // 7.1 拼接车缝完成数据 无数据默认为 0
            if (null != garmentCuttingCompleteMap && null != garmentCuttingCompleteMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = garmentCuttingCompleteMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.2 拼接裁剪完成数据 无数据默认为 0
            if (null != garmentSewingCompleteMap && null != garmentSewingCompleteMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = garmentSewingCompleteMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.3 拼接样衣完成数据 无数据默认为 0
            if (null != garmentCompleteMap && null != garmentCompleteMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = garmentCompleteMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            dataLists.add(arrayList);
        }
        // 8、锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.SAMPLE_CAPACITY_TOTAL + patternMakingWeekMonthViewDto.getCompanyCode() + super.getUserId();
        // 8.1 缓存数据
        try {
            this.setRedisData(dataMap, dataLists, key, lockKey, DateUtils.HOUR_MINUTES, DateUtils.SECONDS);
        } catch (Exception e) {
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }
        return dataMap;
    }


    /**
     * 品类汇总统计
     *
     * @param patternMakingWeekMonthViewDto 参数
     * @param key                           缓存key
     * @return 数据
     */
    private Map<String, Object> queryCategorySummaryCountData(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key) {
        // 1、缓存数据格式
        Map<String, Object> dataMap = Maps.newHashMap();
        // 2、返回数据集合
        ArrayList<ArrayList> dataLists = new ArrayList<>();
        // 参数校验
        this.paramCheck(patternMakingWeekMonthViewDto);
        // 5、参数状态集合
        List<String> statusList = new ArrayList<>();
        // 5.1、查询未打版数据
        statusList.add(EnumNodeStatus.SAMPLE_TASK_WAITING_RECEIVE.getStatus());
        statusList.add(EnumNodeStatus.SAMPLE_TASK_RECEIVED.getStatus());
        patternMakingWeekMonthViewDto.setSampleType("未打版");
        patternMakingWeekMonthViewDto.setStatusList(statusList);

        QueryWrapper qw = new QueryWrapper();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.CategorySummaryCount.getK(), "sd.");
        List<PatternMakingWeekMonthViewVo> noPatternDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto, qw);
        // 5.1.1、未打版数据转为Map用于组装数据返回前端
        Map<String, PatternMakingWeekMonthViewVo> noPatternDataMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(noPatternDataList)) {
            noPatternDataMap = this.getDataByYearWeek(noPatternDataList);
        }
        // 5.1.2、清空查询数据
        statusList.clear();
        // 5.2、查询打版中数据
        statusList.add(EnumNodeStatus.SAMPLE_TASK_IN_VERSION.getStatus());
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        patternMakingWeekMonthViewDto.setSampleType(EnumNodeStatus.SAMPLE_TASK_IN_VERSION.getStatus());
        List<PatternMakingWeekMonthViewVo> patternCentreDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto, qw);
        // 5.2.1 打版中数据转为Map用于组装数据返回前端
        Map<String, PatternMakingWeekMonthViewVo> patternCentreDataMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(noPatternDataList)) {
            patternCentreDataMap = this.getDataByYearWeek(patternCentreDataList);
        }
        // 5.2.2 清空查询数据
        statusList.clear();
        // 5.3 查询已完成数据
        statusList.add(EnumNodeStatus.SAMPLE_TASK_VERSION_COMPLETE.getStatus());
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        patternMakingWeekMonthViewDto.setSampleType(EnumNodeStatus.SAMPLE_TASK_VERSION_COMPLETE.getStatus());
        List<PatternMakingWeekMonthViewVo> completePatternDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto, qw);
        Map<String, PatternMakingWeekMonthViewVo> completePatternDataMap = Maps.newHashMap();
        // 5.3.1 已完成数据转为Map用于组装数据返回前端
        if (CollectionUtil.isNotEmpty(noPatternDataList)) {
            completePatternDataMap = this.getDataByYearWeek(completePatternDataList);
        }
        // 5.3.2 清空查询数据
        statusList.clear();
        // 5.4 查询需求总数
        patternMakingWeekMonthViewDto.setStatusList(null);
        patternMakingWeekMonthViewDto.setSampleType("需求数总数");
        List<PatternMakingWeekMonthViewVo> requirementNumSumDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto, qw);
        Map<String, PatternMakingWeekMonthViewVo> requirementNumSumDataMap = Maps.newHashMap();
        // 5.4.1  已完成数据转为Map用于组装数据返回前端
        if (CollectionUtil.isNotEmpty(noPatternDataList)) {
            requirementNumSumDataMap = this.getDataByYearWeek(requirementNumSumDataList);
        }
        // 6、合并所有数据，取出有数据的年份
        List<PatternMakingWeekMonthViewVo> patternDataAllList = new ArrayList<>();
        // 6.1 未开版数据
        patternDataAllList.addAll(noPatternDataList);
        // 6.2 开版中数据
        patternDataAllList.addAll(patternCentreDataList);
        // 6.3 已开版数据
        patternDataAllList.addAll(completePatternDataList);
        // 6.4 需求总数数据
        patternDataAllList.addAll(requirementNumSumDataList);
        // 6.5 取出有数据的年份并去重
        List<String> yearWeekList = patternDataAllList.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
        // 7、拼接数据
        this.joinCategorySummaryData(yearWeekList, dataLists, noPatternDataMap, patternCentreDataMap, completePatternDataMap, requirementNumSumDataMap);
        // 8、锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.CATEGORY_SUMMARY_COUNT + patternMakingWeekMonthViewDto.getCompanyCode() + super.getUserId();
        // 8.1 缓存数据
        try {
            this.setRedisData(dataMap, dataLists, key, lockKey, DateUtils.HOUR_MINUTES, DateUtils.SECONDS);
        } catch (Exception e) {
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }
        return dataMap;
    }


    /**
     * 根据时间按周月统计版类对比
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key                           缓存KEY
     * @return 根据周返回集合
     */
    public Map<String, Object> queryVersionComparisonView(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key) {
        // 1、缓存数据格式
        Map<String, Object> dataMap = Maps.newHashMap();
        // 2、返回数据集合
        ArrayList<ArrayList> dataLists = new ArrayList<>();
        // 2.1、添加表头
        dataLists.add(CollUtil.newArrayList("product", "头版数", "改版数"));
        // 参数校验
        this.paramCheck(patternMakingWeekMonthViewDto);
        // 5、从数据库查询数据
        QueryWrapper qw = new QueryWrapper();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PatternMakingWeekMonthView.getK(), "sd.");
        List<PatternMakingWeekMonthViewVo> dataList = baseMapper.versionComparisonViewWeekMonth(patternMakingWeekMonthViewDto, qw);
        // 6、判断数据是为空
        if (CollectionUtil.isNotEmpty(dataList)) {
            // 6.1 取出所有的时间
            List<String> yearWeekList = dataList.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                    .map(m -> m.getYearWeek()).distinct().collect(Collectors.toList());

            // 6.2根据sampleType分类
            Map<Object, List<PatternMakingWeekMonthViewVo>> sampleTypeMap = dataList.stream().collect(Collectors.groupingBy(PatternMakingWeekMonthViewVo::getSampleType));

            // 6.3 取出初版数据
            List<PatternMakingWeekMonthViewVo> firstVersionList = null != sampleTypeMap.get("初版样") ? sampleTypeMap.get("初版样") : new ArrayList<>();
            //  6.3.1 根据时间转换为map
            Map<String, PatternMakingWeekMonthViewVo> firstVersionMap = Maps.newHashMap();
            if (CollectionUtil.isNotEmpty(firstVersionList)) {
                firstVersionMap = firstVersionList.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                        .collect(Collectors.toMap(k -> k.getYearWeek(), v -> v, (a, b) -> b));
            }

            // 6.4 取出改版样数据
            List<PatternMakingWeekMonthViewVo> revisionList = null != sampleTypeMap.get("改版样") ? sampleTypeMap.get("改版样") : new ArrayList<>();
            //  6.4.1 根据时间把改版样数据转换为map
            Map<String, PatternMakingWeekMonthViewVo> revisionMap = Maps.newHashMap();
            if (CollectionUtil.isNotEmpty(revisionList)) {
                revisionMap = revisionList.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                        .collect(Collectors.toMap(k -> k.getYearWeek(), v -> v, (a, b) -> b));
            }

            // 7、根据数据里的拼接返回的数据
            for (String yearWeek : yearWeekList) {
                ArrayList<String> arrayList = new ArrayList();
                arrayList.add(yearWeek);
                // 7.1、初版数据
                if (null != firstVersionMap.get(yearWeek)) {
                    PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = firstVersionMap.get(yearWeek);
                    arrayList.add(null != patternMakingWeekMonthViewVo.getNum() ? patternMakingWeekMonthViewVo.getNum() : String.valueOf(BaseGlobal.ZERO));
                } else {
                    arrayList.add(String.valueOf(BaseGlobal.ZERO));
                }
                // 7.2、改版样数据
                if (null != revisionMap.get(yearWeek)) {
                    PatternMakingWeekMonthViewVo revisionTwo = revisionMap.get(yearWeek);
                    arrayList.add(null != revisionTwo.getNum() ? revisionTwo.getNum() : String.valueOf(BaseGlobal.ZERO));
                } else {
                    arrayList.add(String.valueOf(BaseGlobal.ZERO));
                }
                dataLists.add(arrayList);
            }
        }
        // 8、锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.VERSION_COMPARISON + patternMakingWeekMonthViewDto.getCompanyCode() + super.getUserId();
        // 8.1 缓存数据
        try {
            this.setRedisData(dataMap, dataLists, key, lockKey, DateUtils.HOUR_MINUTES, DateUtils.SECONDS);
        } catch (Exception e) {
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }

        return dataMap;
    }

    /**
     * 获取缓存的数据
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token                         token
     * @param countType                     根据统计类型查询对应的方法
     * @return 数据
     */
    private Map<String, Object> redisGetData(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token, String countType) {
        // 1、组装缓存key
        StringBuilder key = new StringBuilder();
        key.append(countType);
        key.append(patternMakingWeekMonthViewDto.getCompanyCode());
        key.append(patternMakingWeekMonthViewDto.getWeeklyMonth());
        key.append(patternMakingWeekMonthViewDto.getStartTime());
        key.append(patternMakingWeekMonthViewDto.getEndTime());
        key.append(patternMakingWeekMonthViewDto.getNode());
        key.append(super.getUserId());
        if (CollectionUtil.isNotEmpty(patternMakingWeekMonthViewDto.getCategoryIds())) {
            key.append(StringUtils.convertListToString(patternMakingWeekMonthViewDto.getCategoryIds()));
        }
        Object redisData = redisUtils.get(key.toString());
        // 拼接锁KEY 区分企业缓存数据
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + countType + patternMakingWeekMonthViewDto.getCompanyCode() + super.getUserId();
        // 如果缓存没有直接返回null，开启线程查数据
        if (null == redisData) {
            // 获取锁，只能查询一次
            if (null == redisUtils.get(lockKey)) {
                try {
                    // 加锁
                    redisUtils.set(lockKey, token + patternMakingWeekMonthViewDto.getCompanyCode());
                    // 查询数据库数据再缓存
                    return this.getCountType(patternMakingWeekMonthViewDto, key.toString(), countType);
                } catch (Exception e) {
                    log.error("统计接口报错:{}", e);
                    redisUtils.del(lockKey);
                } finally {
                    // 无论成功报错 都会清除锁
                    redisUtils.del(lockKey);
                }


            }
            return null;
        }
        // 获取缓存数据
        Map<String, Object> data = (Map<String, Object>) redisData;
        // 获取设置的超时时间
        String timeOut = data.get(TechnologyBoardConstant.TIME_OUT).toString();
        // 获取缓存时间
        String dateString = data.get(TechnologyBoardConstant.TIME).toString();
        // 获取设置的超时时间类型
        String timeType = data.get(TechnologyBoardConstant.TIME_TYPE).toString();
        // 获取数据缓存过期时间
        Date dateOut = DateUtils.getDateByDateType(dateString, timeOut, timeType);
        Date currentDate = new Date();
        // 设置数据的缓存时间已过 开启新的线程去查数据
        if (dateOut.getTime() - currentDate.getTime() < 0) {
            // 设置锁，只能查询一次
            if (null == redisUtils.get(lockKey)) {
                // 开启线程查数据
                this.getData(patternMakingWeekMonthViewDto, key.toString(), token, lockKey, countType);
            }
        }
        return data;
    }

    /**
     * 开启线程查数据
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key                           缓存key
     * @param token                         token
     * @param lockKey                       拼接的锁key
     */
    private void getData(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key, String token, String lockKey, String countType) {
        new Thread(() -> {
            try {
                redisUtils.set(lockKey, token + patternMakingWeekMonthViewDto.getCompanyCode());
                // 查询数据库数据再缓存
                this.getCountType(patternMakingWeekMonthViewDto, key.toString(), countType);
            } catch (Exception e) {
                log.error("统计接口报错:{}", e);
                redisUtils.del(lockKey);
            } finally {
                // 无论成功报错 都会清除锁
                redisUtils.del(lockKey);
            }
        }).start();
    }

    /**
     * 根据类型调取那个方法
     *
     * @param patternMakingWeekMonthViewDto 参数
     * @param key                           缓存key
     * @param countType                     统计类型
     * @return 数据
     */
    private Map<String, Object> getCountType(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key, String countType) {
        if (countType.equals(TechnologyBoardConstant.VERSION_COMPARISON)) {
            // 版类对比统计
            return this.queryVersionComparisonView(patternMakingWeekMonthViewDto, key);
        } else if (countType.equals(TechnologyBoardConstant.CATEGORY_SUMMARY_COUNT)) {
            // 品类汇总统计
            return this.queryCategorySummaryCountData(patternMakingWeekMonthViewDto, key);
        } else if (countType.equals(TechnologyBoardConstant.SAMPLE_CAPACITY_TOTAL)) {
            // 样衣产能总数统计
            return this.querySampleCapacityTotalCount(patternMakingWeekMonthViewDto, key);
        } else if (countType.equals(TechnologyBoardConstant.CAPACITY_CONTRAST)) {
            // 产能对比统计
            return this.capacityContrastStatisticsView(patternMakingWeekMonthViewDto, key);
        }
        return null;
    }

    /**
     * 拼接数据 (方法建议不超过80行，超过则抽出来)
     *
     * @param yearWeekList             年份列表
     * @param dataLists                返回数据列表
     * @param noPatternDataMap         未开版数据
     * @param patternCentreDataMap     开版中数据
     * @param completePatternDataMap   已开版数据
     * @param requirementNumSumDataMap 需求总数数据
     */
    private void joinCategorySummaryData(List<String> yearWeekList, ArrayList<ArrayList> dataLists, Map<String, PatternMakingWeekMonthViewVo> noPatternDataMap, Map<String, PatternMakingWeekMonthViewVo> patternCentreDataMap
            , Map<String, PatternMakingWeekMonthViewVo> completePatternDataMap, Map<String, PatternMakingWeekMonthViewVo> requirementNumSumDataMap) {
        for (String yearWeek : yearWeekList) {
            ArrayList<String> arrayList = new ArrayList();
            arrayList.add(yearWeek);
            // 7.1 拼接未打版数据 无数据默认为 0
            if (null != noPatternDataMap && null != noPatternDataMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = noPatternDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.2 拼接打版中数据 无数据默认为 0
            if (null != patternCentreDataMap && null != patternCentreDataMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = patternCentreDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.3 拼接打版完成数据 无数据默认为 0
            if (null != completePatternDataMap && null != completePatternDataMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = completePatternDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.4 拼接需求总数数据 无数据默认为 0
            if (null != requirementNumSumDataMap && null != requirementNumSumDataMap.get(yearWeek)) {
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = requirementNumSumDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getRequirementNumSum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            dataLists.add(arrayList);
        }
    }

    /**
     * list转为map
     *
     * @param list 要转map的数据
     * @return map
     */
    private Map<String, PatternMakingWeekMonthViewVo> getDataByYearWeek(List<PatternMakingWeekMonthViewVo> list) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                .collect(Collectors.toMap(k -> k.getYearWeek(), v -> v, (a, b) -> b));
    }

    /**
     * 参数校验
     *
     * @param patternMakingWeekMonthViewDto 校验的参数
     */
    private void paramCheck(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        // 3.1、时间为空 默认查询当前时间的前一个月数据
        if (StringUtils.isBlank(patternMakingWeekMonthViewDto.getStartTime()) || StringUtils.isBlank(patternMakingWeekMonthViewDto.getEndTime())) {
            Date date = new Date();
            // 获取过去时间 默认一个月
            if (StringUtils.isNotBlank(patternMakingWeekMonthViewDto.getWeeklyMonth())) {
                if (BaseGlobal.WEEK.equals(patternMakingWeekMonthViewDto.getWeeklyMonth())) {
                    patternMakingWeekMonthViewDto.setStartTime(DateUtils.getMonthAgo(date));
                } else {
                    // 获取过去一年的时间
                    patternMakingWeekMonthViewDto.setStartTime(DateUtils.getYearAgo(date));
                }
            } else {
                // 获取过去一个月的时间
                patternMakingWeekMonthViewDto.setStartTime(DateUtils.getMonthAgo(date));
            }
            patternMakingWeekMonthViewDto.setEndTime(DateUtils.formatDateTime(date));
        }
        // 4、判断是否是根据周、月查询
        if (StringUtils.isBlank(patternMakingWeekMonthViewDto.getWeeklyMonth()) || (!patternMakingWeekMonthViewDto.getWeeklyMonth().equals(BaseGlobal.WEEK) && !patternMakingWeekMonthViewDto.getWeeklyMonth().equals(BaseGlobal.MONTH))) {
            patternMakingWeekMonthViewDto.setWeeklyMonth(BaseGlobal.WEEK);
        }
        // 默认为打版任务节点
        if (StringUtils.isBlank(patternMakingWeekMonthViewDto.getNode())) {
            patternMakingWeekMonthViewDto.setNode(EnumNodeStatus.SAMPLE_TASK_VERSION_COMPLETE.getNode());
        }

    }

    /**
     * 设置缓存数据
     *
     * @param dataMap   缓存拼接的数据
     * @param dataLists 缓存数据
     * @param key       缓存key
     * @param lockKey   锁key
     * @param timeOut   缓存时间
     * @param timeType  缓存类型 DateUtils 如 分、时、天
     */
    private void setRedisData(Map<String, Object> dataMap, ArrayList<ArrayList> dataLists, String key, String lockKey, int timeOut, String timeType) {
        // 8、放入Redis缓存在数据里面设置过期时间
        dataMap.put("dataLists", dataLists);
        // 8.1 数据里设置过期时间为一小时
        dataMap.put(TechnologyBoardConstant.TIME_OUT, timeOut);
        // 8.2 设置过期时间类型
        dataMap.put(TechnologyBoardConstant.TIME_TYPE, timeType);
        // 8.3 数据缓存时间
        dataMap.put(TechnologyBoardConstant.TIME, DateUtils.formatDateTime(new Date()));
        redisUtils.set(key, dataMap);
        // 9、解除缓存锁
        redisUtils.del(lockKey);
    }

    // 自定义方法区 不替换的区域【other_end】
}

