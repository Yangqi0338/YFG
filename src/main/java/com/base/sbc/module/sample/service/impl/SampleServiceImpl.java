/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.Technology;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.service.TechnologyService;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.sample.vo.SamplePageVo;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：样衣 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Service
public class SampleServiceImpl extends ServicePlusImpl<SampleMapper, Sample> implements SampleService {


    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private FlowableService flowableService;
    @Autowired
    public AttachmentService attachmentService;
    @Autowired
    public PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    @Override
    public Sample saveSample(SampleSaveDto dto) {
        Sample sample = null;
        if (StrUtil.isNotBlank(dto.getId())) {
            sample = getById(dto.getId());
            BeanUtil.copyProperties(dto, sample);
            updateById(sample);
        } else {
            sample = new Sample();
            BeanUtil.copyProperties(dto, sample);
            save(sample);
        }
        //保存工艺信息
        if (ObjectUtil.isNotEmpty(dto.getTechnology())) {
            TechnologySaveDto technologyDto = dto.getTechnology();
            Technology technology = null;
            if (StrUtil.isNotBlank(technologyDto.getId())) {
                technology = technologyService.getById(technologyDto.getId());
            } else {
                technology = BeanUtil.copyProperties(technologyDto, Technology.class);
            }
            technologyService.saveOrUpdate(technology);
        }
        // 附件信息
        QueryWrapper<Attachment> aqw = new QueryWrapper<>();
        aqw.eq("f_id", dto.getId());
        List<Attachment> attachments = new ArrayList<>(12);
        if (CollUtil.isNotEmpty(dto.getAttachmentList())) {
            attachments = BeanUtil.copyToList(dto.getAttachmentList(), Attachment.class);
            for (Attachment attachment : attachments) {
                attachment.setFId(sample.getId());
                attachment.setType("sample");
                attachment.setStatus(BaseGlobal.STATUS_NORMAL);
            }
        }
        attachmentService.addAndUpdateAndDelList(attachments, aqw);
        return sample;
    }

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        String companyCode = getCompanyCode();
        String userId = getUserId();
        QueryWrapper<Sample> qw = new QueryWrapper<>();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "design_no", dto.getSearch()).or().like(StrUtil.isNotBlank(dto.getSearch()), "his_design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getKitting()), "kitting", StrUtil.split(dto.getKitting(), CharUtil.COMMA));
        qw.eq(BaseConstant.COMPANY_CODE, companyCode);
        //1我下发的
        if (StrUtil.equals(dto.getUserType(), SamplePageDto.userType1)) {
            qw.eq("sender", userId);
        }
        //2我创建的
        else if (StrUtil.equals(dto.getUserType(), SamplePageDto.userType2)) {
            qw.isNull("sender");
            qw.eq("create_id", userId);
        }
        //3我负责的
        else if (StrUtil.equals(dto.getUserType(), SamplePageDto.userType3)) {
            qw.eq("designer_id", userId);
        }
        Page<SamplePageVo> objects = PageHelper.startPage(dto);
        getBaseMapper().selectByQw(qw);
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean startApproval(String id) {
        Sample sample = getById(id);
        if (sample == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        boolean flg = flowableService.start(FlowableService.sample_pdn, id, "/pdm/api/saas/sample/approval", "/sampleClothesDesign/sampleDesign/" + id,"/sampleClothesDesign/sampleDesign/" + id, BeanUtil.beanToMap(sample));
        if (flg) {
            sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
            updateById(sample);
        }
        return flg;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approval(AnswerDto dto) {
        Sample sample = getById(dto.getBusinessKey());
        if (sample != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                //设置样衣状态为 已开款
                sample.setStatus("1");
                sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            }
            updateById(sample);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean sendSampleMaking(SendSampleMakingDto dto) {
        Sample sample = checkedSampleExists(dto.getId());
        sample.setStatus("2");
        sample.setKitting(dto.getKitting());
        updateById(sample);
        return true;
    }

    @Override
    public Sample checkedSampleExists(String id) {
        Sample sample = getById(id);
        if (sample == null) {
            throw new OtherException("样衣数据不存在");
        }
        return sample;
    }

    @Override
    public SampleVo getDetail(String id) {
        Sample sample = getById(id);
        if (sample == null) {
            return null;
        }
        SampleVo sampleVo = BeanUtil.copyProperties(sample, SampleVo.class);
        //查询工艺信息
        Technology technology = technologyService.getOne(new QueryWrapper<Technology>().eq("f_id", id));
        sampleVo.setTechnology(technology);

        //查询附件
        List<AttachmentVo> attachmentVoList = attachmentService.findByFId(id);
        sampleVo.setAttachmentList(attachmentVoList);

        // 关联的素材库
        QueryWrapper mqw=new QueryWrapper<PlanningCategoryItemMaterial>();
        mqw.eq("planning_category_item_id", sample.getPlanningCategoryItemId());
        List<PlanningCategoryItemMaterial> list = planningCategoryItemMaterialService.list(mqw);
        List<MaterialVo> materialList = BeanUtil.copyToList(list,MaterialVo.class);
        sampleVo.setMaterialList(materialList);


        return sampleVo;
    }


}

