/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.entity.SampleItemLog;
import com.base.sbc.module.sample.mapper.SampleDesignMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SamplePageByDesignNoVo;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：样衣 service类
 * @address com.base.sbc.module.sample.service.SampleService
 */
@Service
public class SampleServiceImpl extends BaseServiceImpl<SampleMapper, Sample> implements SampleService {
    @Autowired
    SampleMapper mapper;
    @Autowired
    PatternMakingMapper patternMakingMapper;
    @Autowired
    SampleDesignMapper sampleDesignMapper;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleVo saveSample(SampleSaveDto dto) {
        String id = "";

        // 获取制版单
        PatternMaking pm = patternMakingMapper.selectById(dto.getPatternMakingId());
        // 获取样衣设计
        SampleDesign sd = sampleDesignMapper.selectById(pm.getSampleDesignId());
        if (pm != null && sd != null){
            Sample sample = new Sample();

            sample.setImages(dto.getImages());
            sample.setPatternMakingId(pm.getId());
            sample.setPatternMakingCode(pm.getCode());
            sample.setSamplaType(pm.getSampleType());
            sample.setCustomerNo(dto.getCustomerNo());
            sample.setStyleName(sd.getStyleName());
            sample.setDesignNo(sd.getDesignNo());
            sample.setCategoryName(dto.getCategoryName());
            sample.setCategoryIds(dto.getCategoryIds());
            sample.setSeasonId(dto.getSeasonId());
            sample.setSeason(dto.getSeason());
            sample.setFromType(dto.getFromType());
            sample.setFromId(dto.getFromId());
            sample.setFromName(dto.getFromName());
            sample.setSampleDesignId(pm.getSampleDesignId());
            sample.setPatternDesignId(pm.getPatternDesignId());
            sample.setPatternDesignName(pm.getPatternDesignName());
            sample.setType(dto.getType());
            sample.setRemarks(dto.getRemarks());

            Integer count = 0, borrowCount = 0;
            for (SampleItem item : dto.getItemList()){
                if (StringUtil.isNotEmpty(item.getId())){
                    count += item.getCount();
                    borrowCount += item.getBorrowCount();

                    item.setId(idGen.nextIdStr());
                    item.setSampleId(sample.getId());
                    item.setBorrowCount(0);
                    sampleItemMapper.insert(item);

                    SampleItemLog log = new SampleItemLog();
                    log.setId(item.getId());
                    log.setSampleItemId(item.getId());
                    log.setType(1);
                    log.setRemarks("新增样衣明细：id-" + item.getId());

                    sampleItemLogService.save(log);
                } else {
                    SampleItem si = sampleItemMapper.selectById(item.getId());
                    String beforRemark = "状态：" + si.getStatus() + "颜色：" + si.getColor() + "尺码：" + si.getSize()
                            + "价格：" + si.getPrice() + "位置：" + si.getPosition() + "备注：" + si.getRemarks();

                    sampleItemMapper.updateById(item);
                    String afterRemark = "状态：" + item.getStatus() + "，颜色：" + item.getColor() + "，尺码：" + item.getSize()
                            + "，价格：" + item.getPrice() + "，位置：" + item.getPosition() + "，备注：" + item.getRemarks();

                    SampleItemLog log = new SampleItemLog();
                    log.setId(idGen.nextIdStr());
                    log.setSampleItemId(item.getId());
                    log.setType(2);
                    log.setRemarks("更新样衣明细：id-" + item.getId() + "，变更前：【" + beforRemark + "】，变更后：【" + afterRemark + "】");
                    sampleItemLogService.save(log);
                }
            }

            sample.setCount(count);
            sample.setBorrowCount(borrowCount);
            if (StringUtil.isNotEmpty(dto.getId())) {
                sample.setId(idGen.nextIdStr());
                mapper.insert(sample);
                id = sample.getId();
            } else {
                mapper.updateById(sample);
                id = dto.getId();
            }
        }

        return mapper.getDetail(id);
    }

    @Override
    public SampleVo getDetail(String id) {
        SampleVo vo = mapper.getDetail(id);

        SamplePageDto dto = new SamplePageDto();
        dto.setSampleId(id);
        List<SampleItem> list = sampleItemMapper.getListBySampleId(dto);
        vo.setItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SamplePageByDesignNoVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListByDesignNo(dto);

        return objects.toPageInfo();
    }
}