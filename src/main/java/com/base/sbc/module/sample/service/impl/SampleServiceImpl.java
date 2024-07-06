/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberExcelDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.dto.SampleSearchDTO;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.enums.SampleFromTypeEnum;
import com.base.sbc.module.sample.enums.SampleItemStatusEnum;
import com.base.sbc.module.sample.enums.SampleLogOperationType;
import com.base.sbc.module.sample.enums.SampleTypeEnum;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleItemVO;
import com.base.sbc.module.sample.vo.SamplePageByDesignNoVo;
import com.base.sbc.module.sample.vo.SampleVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.mapper.StyleMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类描述：样衣 service类
 *
 * @address com.base.sbc.module.sample.service.SampleService
 */
@Service
public class SampleServiceImpl extends BaseServiceImpl<SampleMapper, Sample> implements SampleService {
    @Autowired
    SampleMapper mapper;
    @Autowired
    PatternMakingMapper patternMakingMapper;
    @Autowired
    StyleMapper styleMapper;
    @Autowired
    SampleItemService sampleItemService;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    FlowableService flowableService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleVo save(SampleSaveDto dto) {
        String id = "";

        // 获取制版单
        PatternMaking pm = patternMakingMapper.selectById(dto.getPatternMakingId());
        // 获取款式设计
        Style sd = styleMapper.selectById(pm.getStyleId());
        if (pm != null && sd != null) {
            Sample sample = CopyUtil.copy(dto, Sample.class);

            if (StringUtil.isEmpty(sample.getId())) {
                sample.setId(idGen.nextIdStr());
                id = sample.getId();
            } else {
                id = dto.getId();
            }

            sample.setPatternMakingId(pm.getId());
            sample.setPatternMakingCode(pm.getCode());
            sample.setSampleType(pm.getSampleType());
            sample.setStyleName(sd.getStyleName());
            sample.setDesignNo(sd.getDesignNo());
            sample.setStyleId(pm.getStyleId());
            sample.setProdCategory(sd.getProdCategory());
            sample.setProdCategory1st(sd.getProdCategory1st());
            sample.setProdCategory2nd(sd.getProdCategory2nd());
            sample.setProdCategory3rd(sd.getProdCategory3rd());

            sample.setProdCategoryName(sd.getProdCategoryName());
            sample.setProdCategory1stName(sd.getProdCategory1stName());
            sample.setProdCategory2ndName(sd.getProdCategory2ndName());
            sample.setProdCategory3rdName(sd.getProdCategory3rdName());

            sample.setPatternDesignId(pm.getPatternDesignId());
            sample.setPatternDesignName(pm.getPatternDesignName());
            sample.setCompleteStatus(2); //库存状态：0-完全借出，1-部分借出，2-全部在库
            for (SampleItem item : dto.getSampleItemList()) {
                item.setCount(1);
                if (StringUtil.isEmpty(item.getId())) {
                    item.setId(idGen.nextIdStr());
                    item.setSampleId(sample.getId());
                    item.setBorrowCount(0);
                    item.setCode("YY" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
                    sampleItemService.save(item);

                    // 保存样衣操作日志
                    sampleItemLogService.save(item.getId(), SampleLogOperationType.INSERT.getK(), "新增样衣");
                } else {
                    SampleItem si = sampleItemService.getById(item.getId());
                    String beforRemark = "状态：" + si.getStatus() + "颜色：" + si.getColor() + "尺码：" + si.getSize()
                            + "价格：" + si.getPrice() + "位置：" + si.getPosition() + "备注：" + si.getRemarks();
                    sampleItemService.updateById(item);

                    String afterRemark = "状态：" + item.getStatus() + "，颜色：" + item.getColor() + "，尺码：" + item.getSize()
                            + "，价格：" + item.getPrice() + "，位置：" + item.getPosition() + "，备注：" + item.getRemarks();

                    String remarks = "更新样衣明细：变更前：【" + beforRemark + "】，变更后：【" + afterRemark + "】";
                    sampleItemLogService.save(item.getId(), SampleLogOperationType.UPDATE.getK(), remarks);
                }
            }
            sample.setCount(CollectionUtils.isEmpty(dto.getSampleItemList()) ? 0 : dto.getSampleItemList().size());
            sample.setBorrowCount(0);
            if (StringUtil.isEmpty(dto.getId())) {
                mapper.insert(sample);
            } else {
                mapper.updateById(sample);
            }
            this.sendApproval(sample.getId(), sample);
        }

        return mapper.getDetail(id);
    }

    @Override
    public SampleVo getDetail(String id) {
        SampleVo vo = mapper.getDetail(id);

        SamplePageDto dto = new SamplePageDto();
        dto.setSampleId(id);
        List<SampleItem> list = sampleItemService.getListBySampleId(dto);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SamplePageByDesignNoVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListByDesignNo(dto);

        return objects.toPageInfo();
    }

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        ImportParams params = new ImportParams();
        params.setNeedSave(false);

        List<ColorModelNumberExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ColorModelNumberExcelDto.class, params);

        List<ColorModelNumber> colorModelNumbers = BeanUtil.copyToList(list, ColorModelNumber.class);
        for (ColorModelNumber colorModelNumber : colorModelNumbers) {
            colorModelNumber.setFileName(split[0]);
            colorModelNumber.setStatus("1");
            QueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", colorModelNumber.getCode());
            // this.saveOrUpdate(colorModelNumber,queryWrapper);
        }
        return true;
    }

    @Override
    public SampleVo updateStatus(SampleSaveDto dto) {
        Sample s = mapper.selectById(dto.getId());
        s.setStatus(dto.getStatus());
        mapper.updateById(s);

        SampleVo vo = mapper.getDetail(dto.getId());
        SamplePageDto dto2 = new SamplePageDto();
        dto2.setSampleId(dto.getId());
        List<SampleItem> list = sampleItemService.getListBySampleId(dto2);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo<SampleItemVO> getSampleItemList(SampleSearchDTO dto) {
        if (StringUtils.isEmpty(dto.getSearch()) && (StringUtils.equals(dto.getSearchFlag(), "1") || StringUtils.isEmpty(dto.getSearchFlag()))) {
            return new PageInfo<>();
        }
        PageHelper.startPage(dto);
        List<SampleItemVO> sampleItemList = sampleItemService.getSampleItemList(dto);
        if (CollectionUtils.isEmpty(sampleItemList)) {
            return new PageInfo<>();
        }
        sampleItemList.forEach(sampleItemVO -> {
            sampleItemVO.setStatusName(SampleItemStatusEnum.getV(sampleItemVO.getStatus()));
            sampleItemVO.setTypeName(SampleTypeEnum.getV(sampleItemVO.getType()));
            sampleItemVO.setFromTypeName(SampleFromTypeEnum.getV(sampleItemVO.getFromType()));
        });
        return new PageInfo<>(sampleItemList);
    }

    @Override
    public void sampleBatchBorrow(List<SampleItem> sampleItems) {
        HashMap<String, Sample> sampleMap = this.updateSampleItem(sampleItems);
        List<Sample> samples = super.getBaseMapper().selectBatchIds(sampleMap.keySet());
        List<Sample> samplesUpdate = samples.stream()
                .map(e -> {
                    Sample sample = sampleMap.get(e.getId());
                    sample.setCompleteStatus(this.getCompleteStatus(e.getCount(), sample.getBorrowCount()));
                    sample.updateInit();
                    return sample;
                }).collect(Collectors.toList());
        super.updateBatchById(samplesUpdate);
    }


    @Override
    public void sampleReturn(List<String> sampleItemIds, Map<String, Integer> sampleIdMap) {
        List<Sample> samples = super.getBaseMapper().selectBatchIds(sampleIdMap.keySet());
        List<Sample> updateSamples = samples.stream()
                .map(e -> {
                    Integer returnCount = sampleIdMap.get(e.getId());
                    Sample sample = new Sample();
                    sample.setId(e.getId());
                    sample.updateInit();
                    sample.setBorrowCount(e.getBorrowCount() - returnCount);
                    sample.setCompleteStatus(this.getCompleteStatus(e.getCount(), sample.getBorrowCount()));
                    return sample;
                }).collect(Collectors.toList());
        super.updateBatchById(updateSamples);
        sampleItemService.sampleReturnUpdateByIds(sampleItemIds);
    }

    @Override
    public void sampleSale(String sampleItemId) {
        SampleItem sampleItem = sampleItemService.getById(sampleItemId);
        if (!SampleItemStatusEnum.IN_LIBRARY.getK().equals(sampleItem.getStatus())) {
            throw new OtherException("销售失败，存在未在库数据");
        }
        sampleItem.setStatus(SampleItemStatusEnum.SOLD.getK());
        sampleItem.setCount(0);
        sampleItem.updateInit();
        sampleItemService.updateById(sampleItem);
        Sample sample = super.getBaseMapper().selectById(sampleItem.getSampleId());
        sample.updateInit();
        sample.setCount(sampleItem.getCount() - sample.getCount());
        super.updateById(sample);
    }

    @Override
    public void sampleAllocate(List<String> sampleItemId, String position, String positionId) {
        List<SampleItem> sampleItems = this.getSampleItems(sampleItemId);
        sampleItems.forEach(sampleItem -> {
            sampleItem.setPosition(position);
            sampleItem.setPositionId(positionId);
            sampleItem.updateInit();
        });
        sampleItemService.updateBatchById(sampleItems);
    }

    private List<SampleItem> getSampleItems(List<String> sampleItemId) {
        List<SampleItem> sampleItems = sampleItemService.listByIds(sampleItemId);
        boolean flag = sampleItems.stream().anyMatch(e -> !SampleItemStatusEnum.IN_LIBRARY.getK().equals(e.getStatus()));
        if (flag) {
            throw new OtherException("调拨失败，存在未在库数据");
        }
        return sampleItems;
    }

    @Override
    public void sampleInventory(Map<String, Integer> sampleItemMap) {
        List<SampleItem> sampleItems = sampleItemService.listByIds(sampleItemMap.keySet());
        List<String> sampleIds = sampleItems.stream()
                .map(SampleItem::getSampleId)
                .distinct()
                .collect(Collectors.toList());
        List<Sample> samples = super.getBaseMapper().selectBatchIds(sampleIds);
        Map<String, Sample> sampleMap = samples.stream().collect(Collectors.toMap(Sample::getId, Function.identity()));
        sampleItems.forEach(sampleItem -> {
            Integer inventoryCount = sampleItemMap.get(sampleItem.getId());
            // 差额
            Integer differ = sampleItem.getCount() - inventoryCount;
            sampleItem.setCount(differ);
            sampleItem.setStatus(SampleItemStatusEnum.IN_LIBRARY.getK());

            Sample sample = sampleMap.get(sampleItem.getSampleId());
            if (Objects.nonNull(sample)) {
                sample.setCount(sample.getCount() - differ);
            }
        });
        sampleItemService.updateBatchById(sampleItems);
        super.updateBatchById(sampleMap.values());
    }

    @Override
    @Transactional
    public void submit(String id) {
        Sample sample = super.getById(id);
        if (Objects.isNull(sample)) {
            throw new OtherException("样衣档案数据不存在");
        }
        if (0 != sample.getExamineStatus() && 3 != sample.getExamineStatus()) {
            throw new OtherException("非草稿或驳回状态，不可提交");
        }

        Sample updateSample = new Sample();
        updateSample.setId(id);
        updateSample.updateInit();
        updateSample.setExamineStatus(1);
        mapper.updateById(updateSample);
        this.sendApproval(id, sample);
    }

    /**
     * 发起审批
     * @param id
     * @param sample
     */
    private void sendApproval(String id, Sample sample) {
        flowableService.start(FlowableService.SAMPLE_ARCHIVES,
                FlowableService.SAMPLE_ARCHIVES, id,
                "/pdm/api/saas/sampleManager/approval",
                "/pdm/api/saas/sampleManager/approval",
                "/pdm/api/saas/sampleManager/approval",
                "/pdm/api/saas/sampleManager/" + id, BeanUtil.beanToMap(sample));
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approval(AnswerDto dto) {
        Sample sample = super.getById(dto.getBusinessKey());
        if (Objects.isNull(sample)) {
            throw new OtherException("样衣数据不存在");
        }
        sample.setExamineStatus(StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS) ? 2 : 3);
        sample.setExamineId(super.getUserId());
        sample.setExamineName(super.getUserName());
        sample.setExamineDate(new Date());
        sample.updateInit();
        super.updateById(sample);
        return true;
    }

    private Integer getCompleteStatus(Integer count, Integer borrowCount) {
        count = Objects.isNull(count) ? 0 : count;
        borrowCount = Objects.isNull(borrowCount) ? 0 : borrowCount;
        return count.equals(borrowCount) ? 0 : borrowCount < count ? 1 : 2;
    }

    private HashMap<String, Sample> updateSampleItem(List<SampleItem> sampleItems) {
        HashMap<String, Sample> sampleMap = new HashMap<>();
        sampleItems.forEach(sampleItem -> {
            sampleItem.setStatus(SampleItemStatusEnum.LENDING.getK());
            sampleItem.setBorrowCount(sampleItem.getCount());
            sampleItem.updateInit();
            Sample sample = sampleMap.get(sampleItem.getSampleId());
            if (Objects.isNull(sample)) {
                Sample sampleNew = new Sample();
                sampleNew.setBorrowCount(sampleItem.getBorrowCount());
                sampleNew.setId(sampleItem.getSampleId());
                sampleMap.put(sampleItem.getSampleId(), sampleNew);
                return;
            }
            sample.setBorrowCount(sample.getBorrowCount() + sampleItem.getBorrowCount());
        });
        sampleItemService.updateBatchById(sampleItems);
        return sampleMap;
    }
}