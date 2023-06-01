/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.entity.Dept;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.patternmaking.dto.PatternMakingDto;
import com.base.sbc.module.patternmaking.dto.SampleDesignSendDto;
import com.base.sbc.module.patternmaking.dto.SetPatternDesignDto;
import com.base.sbc.module.patternmaking.dto.TechnologyCenterTaskSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternDesignSampleTypeQtyVo;
import com.base.sbc.module.patternmaking.vo.PatternDesignVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingVo;
import com.base.sbc.module.patternmaking.vo.TechnologyCenterTaskVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
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

    private final SampleDesignService sampleDesignService;
    private final NodeStatusService nodeStatusService;
    private final AttachmentService attachmentService;
    private final AmcFeignService amcFeignService;

    private final CcmFeignService ccmFeignService;


// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<PatternMakingVo> findBySampleDesignId(String sampleDesignId) {
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("sample_design_id", sampleDesignId);
        List<PatternMaking> list = list(qw);
        List<PatternMakingVo> patternMakingVos = BeanUtil.copyToList(list, PatternMakingVo.class);
        return patternMakingVos;
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
        save(patternMaking);
        return patternMaking;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean sampleDesignSend(SampleDesignSendDto dto) {
        EnumNodeStatus enumNodeStatus = EnumNodeStatus.DESIGN_SEND;
        NodeStatus nodeStatus = nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus.getNode(), enumNodeStatus.getStatus());
        UpdateWrapper<PatternMaking> uw=new UpdateWrapper<>();
        uw.set("node",enumNodeStatus.getNode());
        uw.set("status",enumNodeStatus.getStatus());
        uw.set("design_send_date",nodeStatus.getStartDate());
        uw.set("design_send_status",BaseGlobal.YES);
        uw.eq("id",dto.getId());
        // 修改单据
        return update(uw);
    }

    @Override
    public boolean nodeStatusChange(PatternMakingDto dto, String node, String status) {
        return false;
    }


    @Override
    public boolean prmSend(SetPatternDesignDto dto) {
        EnumNodeStatus enumNodeStatus = EnumNodeStatus.TECHNICAL_ROOM_SEND;
        NodeStatus nodeStatus = nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus.getNode(), enumNodeStatus.getStatus());
        UpdateWrapper<PatternMaking> uw=new UpdateWrapper<>();
        uw.set("node",enumNodeStatus.getNode());
        uw.set("status",enumNodeStatus.getStatus());
        uw.set("prm_send_date",nodeStatus.getStartDate());
        uw.set("prm_send_status",BaseGlobal.YES);
        uw.eq("id",dto.getId());
        // 修改单据
        return update(uw);
    }

    @Override
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        Page<TechnologyCenterTaskVo> page = PageHelper.startPage(dto);
        List<TechnologyCenterTaskVo> list = getBaseMapper().technologyCenterTaskList(qw);
        //设置图片
        attachmentService.setListStylePic(list,"stylePic");
        return page.toPageInfo();
    }

    @Override
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
        List<PatternDesignSampleTypeQtyVo> qtyList = getBaseMapper().getPatternDesignSampleTypeCount(pmQw);
        List<PatternDesignVo> patternDesignVoList = new ArrayList<>();
        Map<String, List<PatternDesignSampleTypeQtyVo>> qtyMap = qtyList.stream().collect(Collectors.groupingBy(PatternDesignSampleTypeQtyVo::getPatternDesignId));
        for (UserCompany user : userList) {
            PatternDesignVo patternDesignVo = BeanUtil.copyProperties(user, PatternDesignVo.class);
            LinkedHashMap<String, Long> sampleTypeCount = new LinkedHashMap<>(16);
            for (Map.Entry<String, String> dict : sampleTypes.entrySet()) {
                Long qty = Optional.ofNullable(qtyMap).map(qtyMap1 -> qtyMap1.get(user.getUserId())).map(qtyList2 -> {
                    PatternDesignSampleTypeQtyVo one = CollUtil.findOne(qtyList2, a -> StrUtil.equals(a.getSampleType(), dict.getKey()));
                    return Optional.ofNullable(one).map(PatternDesignSampleTypeQtyVo::getQuantity).orElse(0L);
                }).orElse(0L);
                sampleTypeCount.put(dict.getValue(), qty);
            }
            String deptName = Optional.ofNullable(user.getDeptList()).map(item -> {
                return item.stream().map(Dept::getName).collect(Collectors.joining(StrUtil.COMMA));
            }).orElse("");
            patternDesignVo.setDeptName(deptName);
            patternDesignVo.setSampleTypeCount(sampleTypeCount);
            patternDesignVoList.add(patternDesignVo);
        }
        return patternDesignVoList;
    }

    public String getNextCode(SampleDesign sampleDesign) {
        String designNo = sampleDesign.getDesignNo();
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("sample_design_id", sampleDesign.getId());
        long count = count(qw);
        String code = StrUtil.padPre(String.valueOf(count + 1), 3, "0");
        return designNo + StrUtil.DASHED + code;

    }// 自定义方法区 不替换的区域【other_end】

}

