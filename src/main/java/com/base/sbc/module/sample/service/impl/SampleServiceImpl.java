/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.entity.Technology;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.service.TechnologyService;
import com.base.sbc.module.sample.vo.SamplePageVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    @Override
    public Sample saveSample(SampleSaveDto dto) {
        Sample sample = getById(dto.getId());
        BeanUtil.copyProperties(dto, sample);
        save(sample);
        //保存工艺信息
        if(ObjectUtil.isNotEmpty(dto.getTechnology())){
            Technology technology=null;
            if(StrUtil.isNotBlank(dto.getId())){
                technology=technologyService.getById(dto.getId());
            }else{
                technology=BeanUtil.copyProperties(dto.getTechnology(),Technology.class);
            }
            technologyService.saveOrUpdate(technology);
        }
        return sample;
    }

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        String companyCode=getCompanyCode();
        String userId=getUserId();
        QueryWrapper<Sample> qw=new QueryWrapper<>();
        qw.like(StrUtil.isNotBlank(dto.getSearch()),"design_no",dto.getSearch()).or()
                .like(StrUtil.isNotBlank(dto.getSearch()),"his_design_no",dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()),"year",dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()),"month",dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()),"season",dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getStatus()),"status",StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getKitting()),"kitting",StrUtil.split(dto.getKitting(), CharUtil.COMMA));
        qw.eq(BaseConstant.COMPANY_CODE,companyCode);
        //1我下发的
        if(StrUtil.equals(dto.getUserType(),SamplePageDto.userType1)){
            qw.eq("sender",userId);
        }
        //2我创建的
        else if(StrUtil.equals(dto.getUserType(),SamplePageDto.userType2)){
            qw.isNull("sender");
            qw.eq("create_id",userId);
        }
        //3我负责的
        else if(StrUtil.equals(dto.getUserType(),SamplePageDto.userType3)){
            qw.eq("designer_id",userId);
        }
        Page<SamplePageVo> objects = PageHelper.startPage(dto);
        getBaseMapper().selectByQw(qw);
        return objects.toPageInfo();
    }


}

