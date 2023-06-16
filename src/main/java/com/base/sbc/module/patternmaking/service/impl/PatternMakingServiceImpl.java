/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.entity.Dept;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.SampleDesignVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
public class PatternMakingServiceImpl extends ServicePlusImpl<PatternMakingMapper, PatternMaking> implements PatternMakingService {
    // 自定义方法区 不替换的区域【other_start】
    private final SampleDesignService sampleDesignService;
    private final NodeStatusService nodeStatusService;
    private final AttachmentService attachmentService;
    private final AmcFeignService amcFeignService;

    private final CcmFeignService ccmFeignService;


    @Override
    public List<PatternMakingListVo> findBySampleDesignId(String sampleDesignId) {
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("sample_design_id", sampleDesignId);
        qw.orderBy(true, true, "create_date");
        List<PatternMaking> list = list(qw);
        List<PatternMakingListVo> patternMakingListVos = BeanUtil.copyToList(list, PatternMakingListVo.class);
        return patternMakingListVos;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public PatternMaking savePatternMaking(PatternMakingDto dto) {
        SampleDesign sampleDesign = sampleDesignService.getById(dto.getSampleDesignId());
        if (sampleDesign == null) {
            throw new OtherException("样衣设计不存在");
        }
        //查询样衣
        PatternMaking patternMaking = BeanUtil.copyProperties(dto, PatternMaking.class);
        patternMaking.setCode(getNextCode(sampleDesign));
        patternMaking.setPlanningSeasonId(sampleDesign.getPlanningSeasonId());
        if (StrUtil.equals(dto.getTechnicianKitting(), BaseGlobal.YES)) {
            patternMaking.setTechnicianKittingDate(new Date());
        }
        save(patternMaking);
        return patternMaking;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean sampleDesignSend(SampleDesignSendDto dto) {
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
        //将样衣设计状态改为已下发
        UpdateWrapper<SampleDesign> sdUw = new UpdateWrapper<>();
        sdUw.eq("id", patternMaking.getSampleDesignId());
        sdUw.set("status", BasicNumber.TWO.getNumber());
        sampleDesignService.update(sdUw);
        // 修改单据
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(NodeStatusChangeDto dto, GroupUser groupUser) {
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
            case GARMENT_CUTTING_RECEIVED:
                uw.set("cutter_id", groupUser.getId());
                uw.set("cutter_name", groupUser.getName());
                break;
            case GARMENT_SEWING_STARTED:
                uw.set("stitcher_id", groupUser.getId());
                uw.set("stitcher", groupUser.getName());
                break;
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
        uw.set(StrUtil.isNotBlank(dto.getPatternDesignId()), "pattern_design_id", dto.getPatternDesignId());
        uw.set(StrUtil.isNotBlank(dto.getPatternDesignName()), "pattern_design_name", dto.getPatternDesignName());
        uw.eq("id", dto.getId());
        // 修改单据
        update(uw);
        return true;
    }

    @Override
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        qw.eq("design_send_status", BaseGlobal.YES);
        qw.eq("s.del_flag", BaseGlobal.NO);
        qw.eq("p.del_flag", BaseGlobal.NO);
        amcFeignService.teamAuth(qw, "s.planning_season_id", getUserId());
        if (StrUtil.isBlank(dto.getOrderBy())) {
            dto.setOrderBy(" p.create_date asc ");
        } else {
            dto.setOrderBy(dto.getOrderBy());
        }

        Page<TechnologyCenterTaskVo> page = PageHelper.startPage(dto);
        List<TechnologyCenterTaskVo> list = getBaseMapper().technologyCenterTaskList(qw);
        //设置图片
        attachmentService.setListStylePic(list, "stylePic");
        // 设置版师列表
        if (CollUtil.isNotEmpty(list)) {
            Map<String, List<PatternDesignVo>> pdMap = new HashMap<>(16);
            for (TechnologyCenterTaskVo tct : list) {
                String key = tct.getPlanningSeasonId();
                if (pdMap.containsKey(key)) {
                    tct.setPdList(pdMap.get(key));
                } else {
                    List<PatternDesignVo> patternDesignList = getPatternDesignList(tct.getPlanningSeasonId());
                    tct.setPdList(patternDesignList);
                    pdMap.put(key, patternDesignList);
                }
            }
        }
        return page.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternDesign(SetPatternDesignDto dto) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("pattern_design_id", dto.getPatternDesignId());
        uw.set("pattern_design_name", dto.getPatternDesignName());
        uw.eq("id", dto.getId());
        return update(uw);
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
        // 重新已接受 已打版的数据
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
    public boolean breakOffSample(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("break_off_sample", BaseGlobal.YES);
        return update(uw);
    }

    @Override
    public boolean breakOffPattern(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("break_off_pattern", BaseGlobal.YES);
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
    public List<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getNode()), "p.node", dto.getNode());
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());

        if (StrUtil.isBlank(dto.getIsBlackList())) {
            //排除黑单
            qw.ne("p.urgency", "0");
        } else {
            //只查询黑单
            qw.eq("p.urgency", "0");
        }
        amcFeignService.teamAuth(qw, "s.planning_season_id", getUserId());
        // 版房主管和设计师 看到全部，版师看到自己
        qw.orderByAsc("p.sort");
        List<PatternMakingTaskListVo> list = getBaseMapper().patternMakingTaskList(qw);
        //设置图片
        attachmentService.setListStylePic(list, "stylePic");
        // 设置节点状态
        setNodeStatus(list);
        return list;
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
    public SampleDesignPmDetailVo getDetailById(String id) {
        PatternMaking byId = getById(id);
        if (byId == null) {
            return null;
        }

        PatternMakingVo vo = BeanUtil.copyProperties(byId, PatternMakingVo.class);
        //设置头像
        amcFeignService.setUserAvatarToObj(vo);
        //查询样衣设计信息
        SampleDesignVo sampleDesignVo = sampleDesignService.getDetail(vo.getSampleDesignId());
        //设置头像
        amcFeignService.setUserAvatarToObj(sampleDesignVo);
        SampleDesignPmDetailVo result = BeanUtil.copyProperties(sampleDesignVo, SampleDesignPmDetailVo.class);
        result.setPatternMaking(vo);
        // 查询附件，纸样文件
        List<AttachmentVo> attachmentVoList = attachmentService.findByFId(vo.getId(), AttachmentTypeConstant.PATTERN_MAKING_PATTERN);
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
    public PageInfo patternMakingSteps(PatternMakingCommonPageSearchDto dto) {
        // 查询样衣信息
        QueryWrapper<SampleDesign> sdQw = new QueryWrapper<>();
        sdQw.like(StrUtil.isNotBlank(dto.getSearch()), "design_no", dto.getSearch());
        sdQw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        sdQw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        sdQw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        sdQw.eq(StrUtil.isNotBlank(dto.getBandCode()), "band_code", dto.getBandCode());
        sdQw.eq(StrUtil.isNotBlank(dto.getDesignerId()), "designer_id", dto.getDesignerId());
        sdQw.eq(COMPANY_CODE, getCompanyCode());
        sdQw.eq("del_flag", BaseGlobal.NO);
        sdQw.eq("status", BasicNumber.TWO.getNumber());
        if (StrUtil.isNotBlank(dto.getOrderBy())) {
            dto.setOrderBy("create_date desc");
        }
        Page page = PageHelper.startPage(dto);
        List<SampleDesign> sdList = sampleDesignService.list(sdQw);
        PageInfo pageInfo = page.toPageInfo();
        if (CollUtil.isEmpty(sdList)) {
            return null;
        }
        List<SampleDesignStepVo> sampleDesignStepVos = BeanUtil.copyToList(sdList, SampleDesignStepVo.class);
//        PageInfo pageInfo = BeanUtil.copyProperties(sdPageInfo, PageInfo.class, "list");
//        pageInfo.setList(sampleDesignStepVos);
        pageInfo.setList(sampleDesignStepVos);
        attachmentService.setListStylePic(sampleDesignStepVos, "stylePic");
        // 查询打版指令
        List<String> sdIds = sampleDesignStepVos.stream().map(SampleDesignStepVo::getId).collect(Collectors.toList());
        QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
        pmQw.in("sample_design_id", sdIds);
        List<PatternMaking> pmList = this.list(pmQw);
        if (CollUtil.isEmpty(pmList)) {
            return pageInfo;
        }
        List<String> pmIds = pmList.stream().map(PatternMaking::getId).collect(Collectors.toList());
        List<SampleDesignStepVo.PatternMakingStepVo> patternMakingStepVos = BeanUtil.copyToList(pmList, SampleDesignStepVo.PatternMakingStepVo.class);
        //查询节点状态
        QueryWrapper<NodeStatus> nsQw = new QueryWrapper<>();
        nsQw.in("data_id", pmIds);
        List<NodeStatus> nsList = nodeStatusService.list(nsQw);
        if (CollUtil.isNotEmpty(nsList)) {
            Map<String, List<NodeStatus>> nsMap = nsList.stream().collect(Collectors.groupingBy(NodeStatus::getDataId));
            for (SampleDesignStepVo.PatternMakingStepVo patternMakingStepVo : patternMakingStepVos) {
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
        LinkedHashMap<String, List<SampleDesignStepVo.PatternMakingStepVo>> pmStepMap = patternMakingStepVos.stream().collect(Collectors.groupingBy(k -> k.getSampleDesignId(), LinkedHashMap::new, Collectors.toList()));
        for (SampleDesignStepVo sampleDesignStepVo : sampleDesignStepVos) {
            sampleDesignStepVo.setPatternMakingSteps(pmStepMap.get(sampleDesignStepVo.getId()));
        }

        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(List<NodeStatusChangeDto> list, GroupUser groupUser) {
        for (NodeStatusChangeDto dto : list) {
            nodeStatusChange(dto, groupUser);
        }
        return true;
    }

    @Override
    public PageInfo<SampleBoardVo> sampleBoardList(PatternMakingCommonPageSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getBandCode()), "s.band_code", StrUtil.split(dto.getBandCode(), StrUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        Page<SampleBoardVo> objects = PageHelper.startPage(dto);
        List<SampleBoardVo> list = getBaseMapper().sampleBoardList(qw);
        attachmentService.setListStylePic(list, "stylePic");
        // 设置节点状态数据
        nodeStatusService.setNodeStatusToListBean(list, "patternMakingId", null, "nodeStatus");
        return objects.toPageInfo();
    }

    @Override
    public boolean receiveSample(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, StrUtil.COMMA));
        uw.set("receive_sample", BaseGlobal.YES);
        uw.set("receive_sample_date", new Date());
        return update(uw);
    }

    @Override
    public List<SampleUserVo> getAllPatternDesignList(String companyCode) {
        List<SampleUserVo> list = getBaseMapper().getAllPatternDesignList(companyCode);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }


    public void setNodeStatus(List<PatternMakingTaskListVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<String> ids = new ArrayList<>(12);
        for (PatternMakingTaskListVo o : list) {
            ids.add(o.getId());
        }
        // 查询所有状态
        QueryWrapper<NodeStatus> qw = new QueryWrapper<>();
        qw.in("data_id", ids);
        qw.orderByAsc("start_date");
        List<NodeStatus> nodeStatusList = nodeStatusService.list(qw);
        if (CollUtil.isEmpty(nodeStatusList)) {
            return;
        }
        // 设置状态
        Map<String, List<NodeStatus>> nodeStatusMap = nodeStatusList.stream().collect(Collectors.groupingBy(NodeStatus::getDataId));
        for (PatternMakingTaskListVo o : list) {
            List<NodeStatus> nodeStatusList1 = nodeStatusMap.get(o.getId());
            if (CollUtil.isNotEmpty(nodeStatusList1)) {
                List<NodeStatusVo> nodeStatusVos = BeanUtil.copyToList(nodeStatusList1, NodeStatusVo.class);
                Map<String, NodeStatusVo> startDataMap = nodeStatusVos.stream().collect(Collectors.toMap(k -> k.getNode() + k.getStatus(), v -> v, (a, b) -> b, LinkedHashMap::new));
                o.setStartDate(Optional.ofNullable(startDataMap.get(o.getNode() + o.getStatus())).map(NodeStatusVo::getStartDate).orElse(null));
                nodeStatusVos.sort((a, b) -> {
                    return a.getStartDate().compareTo(b.getStartDate());
                });
                o.setNodeStatusList(nodeStatusVos);
            }
            if (StrUtil.equals(o.getSuspend(), BaseGlobal.YES)) {
                o.setStartDate(o.getUpdateDate());
            }
        }


    }

    public String getNextCode(SampleDesign sampleDesign) {
        String designNo = sampleDesign.getDesignNo();
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("sample_design_id", sampleDesign.getId());
        long count = count(qw);
        String code = StrUtil.padPre(String.valueOf(count + 1), 3, "0");
        return designNo + StrUtil.DASHED + code;

    }

    // 自定义方法区 不替换的区域【other_end】


}

