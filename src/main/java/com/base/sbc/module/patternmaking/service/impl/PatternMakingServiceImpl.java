/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.patternmaking.dto.PatternMakingDto;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternMakingVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：打版管理 service类
 * @address com.base.sbc.module.patternmaking.service.PatternMakingService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 * @version 1.0  
 */
@Service
@RequiredArgsConstructor
public class PatternMakingServiceImpl extends ServicePlusImpl<PatternMakingMapper, PatternMaking> implements PatternMakingService {

    private final SampleDesignService sampleDesignService;
    private final NodeStatusService nodeStatusService;



// 自定义方法区 不替换的区域【other_start】

@Override
public List<PatternMakingVo> findBySampleDesignId(String sampleDesignId) {
    QueryWrapper<PatternMaking> qw=new QueryWrapper<>();
    qw.eq("sample_design_id",sampleDesignId);
    List<PatternMaking> list = list(qw);
    List<PatternMakingVo> patternMakingVos = BeanUtil.copyToList(list, PatternMakingVo.class);
    return patternMakingVos;
}

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public PatternMaking savePatternMaking(PatternMakingDto dto) {
        SampleDesign sampleDesign = sampleDesignService.getById(dto.getSampleDesignId());
        if(sampleDesign==null){
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
    public boolean sampleDesignSend(PatternMakingDto dto) {
        String node="技术中心看板";
        String status ="样衣设计下发";
        NodeStatus nodeStatus = nodeStatusService.nodeStatusChange(dto.getId(), node, status);
        dto.setNode(node);
        dto.setStatus(status);
        dto.setDesignSendDate(nodeStatus.getStartDate());
        dto.setDesignSendStatus(BaseGlobal.YES);
        // 修改单据
        PatternMaking patternMakings = BeanUtil.copyProperties(dto, PatternMaking.class);
        updateById(patternMakings);
        return true;
    }

    public String getNextCode(SampleDesign sampleDesign){
        String designNo = sampleDesign.getDesignNo();
        QueryWrapper<PatternMaking> qw=new QueryWrapper<>();
        qw.eq("sample_design_id",sampleDesign.getId());
        long count = count(qw);
        String code = StrUtil.padPre(String.valueOf(count+1), 3, "0");
        return designNo+StrUtil.DASHED+code;

    }// 自定义方法区 不替换的区域【other_end】
	
}

