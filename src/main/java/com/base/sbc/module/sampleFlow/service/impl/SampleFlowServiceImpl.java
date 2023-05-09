/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sampleFlow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.sampleFlow.mapper.SampleFlowMapper;
import com.base.sbc.module.sampleFlow.entity.SampleFlow;
import com.base.sbc.module.sampleFlow.service.SampleFlowService;
import com.base.sbc.module.sampleFlow.vo.SampleFlowVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：样衣流程 service类
 * @address com.base.sbc.module.sampleFlow.service.SampleFlowService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-9 10:26:38
 * @version 1.0  
 */
@Service
public class SampleFlowServiceImpl extends ServicePlusImpl<SampleFlowMapper, SampleFlow> implements SampleFlowService {

    @Autowired
    private BaseController baseController;
    @Resource
    private CcmFeignService ccmFeignService;

    @Override
    public List<SampleFlowVo> getFlowList(String sampleId) {
        QueryWrapper<SampleFlow> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("sample_id",sampleId);
        queryWrapper.eq("company_code",baseController.getUserCompany());
        /*查询样衣下的流程*/
        List<SampleFlow> sampleFlowList=  baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(sampleFlowList)){
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        /*转成vo返回*/
        List<SampleFlowVo> sampleFlowVoList = BeanUtil.copyToList(sampleFlowList, SampleFlowVo.class);
        return sampleFlowVoList;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean accomplishFlow(String flowId) {
        SampleFlow sampleFlow = baseMapper.selectById(flowId);
        if (ObjectUtils.isEmpty(sampleFlow)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        /*完成当前节点*/
        sampleFlow.setFlowStatus(BaseGlobal.STATUS_CLOSE);
        sampleFlow.setAuditUserId(baseController.getUserId());
        sampleFlow.setAuditUserName(baseController.getUser().getName());
        sampleFlow.setFlowEndDate(new Date());
        sampleFlow.updateInit();
        baseMapper.updateById(sampleFlow);
        /*修改下一个节点流程开始时间*/
        QueryWrapper<SampleFlow> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("sample_id",sampleFlow.getSampleId());
        queryWrapper.eq("flow_order",sampleFlow.getFlowOrder()+1);
        /*查询下个节点，设置开始时间*/
        SampleFlow nextSampleFlow =  baseMapper.selectOne(queryWrapper);
        if (!ObjectUtils.isEmpty(nextSampleFlow)) {
            nextSampleFlow.setFlowStartDate (new Date());
            nextSampleFlow.updateInit();
            baseMapper.updateById(nextSampleFlow);
        }
        return true;
    }

    @Override
    public boolean accomplishAdjFlow(String sampleId) {
        /*获取所有节点*/
        List<SampleFlowVo> list = getFlowList(sampleId);
        /*获取已完成的节点*/
        List<Integer> integerList=  list.stream().filter(s -> s.getFlowStatus().equals(BaseGlobal.STATUS_CLOSE)).map(SampleFlowVo::getFlowOrder).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(integerList)) {
            throw new OtherException("样衣流程已完成");
        }
        /*获取走到的节点*/
        SampleFlowVo sampleFlowVo= list.get(integerList.size());
        /*完成节点*/
        accomplishFlow(sampleFlowVo.getId());
        return true;
    }

    @Override
    public boolean rejectFlow(String flowId) {
        SampleFlow sampleFlow = baseMapper.selectById(flowId);
        if (ObjectUtils.isEmpty(sampleFlow) || sampleFlow.getFlowOrder()==1) {
            throw new OtherException("查无数据或是第一个节点无法驳回");
        }
        //   修改到上个节点
        QueryWrapper<SampleFlow> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("sample_id",sampleFlow.getSampleId());
        queryWrapper.eq("flow_order",sampleFlow.getFlowOrder()-1);
        SampleFlow nextSampleFlow =  baseMapper.selectOne(queryWrapper);
        if (!ObjectUtils.isEmpty(nextSampleFlow)) {
            sampleFlow.setFlowStatus(BaseGlobal.STATUS_NORMAL);
            nextSampleFlow.updateInit();
            baseMapper.updateById(nextSampleFlow);
        }
        return true;
    }

    @Override
    public boolean addFlow(String sampleId) {
        List<SampleFlow> list = new ArrayList<>();
        /*查询字典数据*/
        Map<String, Map<String, String>> sampleFlowMap = ccmFeignService.getDictInfoToMap("sampleFlow");
        Map<String, String> map = sampleFlowMap.get("sampleFlow");
        Integer index = 1;
        for (String key : map.keySet()) {
            SampleFlow sampleFlow = new SampleFlow();
            sampleFlow.setCompanyCode(baseController.getUserCompany());
            sampleFlow.setSampleId(sampleId);
            sampleFlow.setFlowName(map.get(key));
            sampleFlow.setFlowOrder(index);
            if (index == 1) {
                sampleFlow.setFlowStartDate(new Date());
            }
            sampleFlow.insertInit();
            index++;
            list.add(sampleFlow);
        }
        saveBatch(list);
        return true;
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
