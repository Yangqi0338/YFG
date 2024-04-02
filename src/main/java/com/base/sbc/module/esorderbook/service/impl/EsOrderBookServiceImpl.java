/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.esorderbook.dto.EsOrderBookQueryDto;
import com.base.sbc.module.esorderbook.dto.EsOrderBookSaveDto;
import com.base.sbc.module.esorderbook.entity.EsOrderBook;
import com.base.sbc.module.esorderbook.entity.EsOrderBookItem;
import com.base.sbc.module.esorderbook.mapper.EsOrderBookMapper;
import com.base.sbc.module.esorderbook.service.EsOrderBookItemService;
import com.base.sbc.module.esorderbook.service.EsOrderBookService;
import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import com.base.sbc.module.esorderbook.vo.EsOrderBookVo;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.pricing.service.impl.StylePricingServiceImpl;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：ES订货本 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.service.EsOrderBookService
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@Service
public class EsOrderBookServiceImpl extends BaseServiceImpl<EsOrderBookMapper, EsOrderBook> implements EsOrderBookService {

    @Autowired
    private StylePricingServiceImpl stylePricingService;
    @Autowired
    private StylePicUtils stylePicUtils;
    @Autowired
    private EsOrderBookItemService esOrderBookItemService;
    @Autowired
    private PlanningSeasonService planningSeasonService;

    @Override
    public PageInfo<EsOrderBookItemVo> findPage(EsOrderBookQueryDto dto) {
        BaseQueryWrapper<EsOrderBookItemVo> qw = new BaseQueryWrapper<>();
        QueryGenerator.initQueryWrapperByMap(qw, dto);
        Page<Object> objects = PageHelper.startPage(dto);
        List<EsOrderBookItemVo> list = baseMapper.findPage(qw);

        //组装费用信息
        List<StylePricingVO> stylePricingList = list.stream().map(o -> {
            StylePricingVO vo = new StylePricingVO();
            //vo.setStylePricingId();
            return vo;
        }).collect(Collectors.toList());
        stylePricingService.dataProcessing(stylePricingList, getCompanyCode(), true);
        Map<String, StylePricingVO> collect = stylePricingList.stream().collect(Collectors.toMap(StylePricingVO::getId, o -> o));
        for (EsOrderBookItemVo esOrderBookItemVo : list) {
            if (collect.containsKey(esOrderBookItemVo.getStylePricingId())) {
                StylePricingVO stylePricingVO = collect.get(esOrderBookItemVo.getStylePricingId());
                esOrderBookItemVo.setWoolenYarnProcessingFee(stylePricingVO.getWoolenYarnProcessingFee());
                esOrderBookItemVo.setSewingProcessingFee(stylePricingVO.getSewingProcessingFee());
                esOrderBookItemVo.setCoordinationProcessingFee(stylePricingVO.getCoordinationProcessingFee());
                esOrderBookItemVo.setActualMagnification(stylePricingVO.getActualMagnification());
                esOrderBookItemVo.setMultiplePrice(esOrderBookItemVo.getTotalCost().multiply(esOrderBookItemVo.getActualMagnification()));
            }
        }
        stylePicUtils.setStyleColorPic2(list, "groupImg");
        return new PageInfo<>(list);
    }

    @Override
    public void lock(List<String> ids) {
        LambdaUpdateWrapper<EsOrderBookItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(EsOrderBookItem::getIsLock, BaseGlobal.YES);
        updateWrapper.set(EsOrderBookItem::getUpdateId, getUserId());
        updateWrapper.set(EsOrderBookItem::getUpdateName, getUserName());
        updateWrapper.set(EsOrderBookItem::getUpdateDate, new Date());
        updateWrapper.in(EsOrderBookItem::getId, ids);
        esOrderBookItemService.update(updateWrapper);
    }

    @Override
    public void unLock(List<String> ids) {
        LambdaUpdateWrapper<EsOrderBookItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(EsOrderBookItem::getIsLock, BaseGlobal.NO);
        updateWrapper.set(EsOrderBookItem::getUpdateId, getUserId());
        updateWrapper.set(EsOrderBookItem::getUpdateName, getUserName());
        updateWrapper.set(EsOrderBookItem::getUpdateDate, new Date());
        updateWrapper.in(EsOrderBookItem::getId, ids);
        esOrderBookItemService.update(updateWrapper);
    }

    @Override
    public void exportExcel(HttpServletResponse response, EsOrderBookQueryDto dto) throws IOException {
        List<EsOrderBookItemVo> list = findPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "es订货本", response, dto);
    }

    @Override
    @Transactional
    public void del(List<EsOrderBookItemVo> list) {
        List<String> ids = list.stream().map(EsOrderBookItemVo::getId).collect(Collectors.toList());
        removeByIds(ids);
        //查询一下是否全部删除了，给头数据也删除一下
        List<String> headIds = list.stream().map(EsOrderBookItemVo::getHeadId).distinct().collect(Collectors.toList());
        List<EsOrderBookItem> headId = esOrderBookItemService.listByField("head_id", headIds);
        List<String> dbHeadIds = headId.stream().map(EsOrderBookItem::getHeadId).distinct().collect(Collectors.toList());
        headIds.removeAll(dbHeadIds);
        if (CollUtil.isNotEmpty(headIds)) {
            removeByIds(headIds);
        }
    }

    @Override
    public EsOrderBook saveMain(EsOrderBookVo dto) {
        if (StrUtil.isEmpty(dto.getSeasonId())) {
            throw new OtherException("产品季id不能为空");
        }
        /*校验订货本名称*/
        LambdaQueryWrapper<EsOrderBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EsOrderBook::getName, dto.getName());
        queryWrapper.ne(!StrUtil.isEmpty(dto.getId()), EsOrderBook::getId, dto.getId());
        queryWrapper.eq(EsOrderBook::getSeasonId, dto.getSeasonId());

        long l = this.count(queryWrapper);
        if (l > 0) {
            throw new OtherException("es订货本名称已存在");
        }
        /*查询产品季*/
        PlanningSeason planningSeason = planningSeasonService.getById(dto.getSeasonId());
        if (ObjectUtil.isEmpty(planningSeason)) {
            throw new OtherException("产品季id错误，没有产品季");
        }

        EsOrderBook esOrderBook = BeanUtil.copyProperties(dto, EsOrderBook.class);
        if (StrUtil.isNotBlank(esOrderBook.getId())) {
            esOrderBook.updateInit();
            this.updateById(esOrderBook);
        } else {
            esOrderBook.insertInit();
            this.save(esOrderBook);
        }
        return getById(esOrderBook.getId());
    }

    @Override
    public void updateHeadName(EsOrderBookItemVo vo) {
        LambdaUpdateWrapper<EsOrderBookItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(EsOrderBookItem::getGroupName, vo.getNewGroupName());
        updateWrapper.set(EsOrderBookItem::getUpdateId, getUserId());
        updateWrapper.set(EsOrderBookItem::getUpdateName, getUserName());
        updateWrapper.set(EsOrderBookItem::getUpdateDate, new Date());
        updateWrapper.eq(EsOrderBookItem::getHeadId, vo.getHeadId());
        updateWrapper.eq(EsOrderBookItem::getGroupName, vo.getGroupName());
        esOrderBookItemService.update(updateWrapper);
    }

    @Override
    public ApiResult uploadStyleColorPics(Principal user, MultipartFile file, EsOrderBookItemVo vo) {
        return null;
    }

    @Override
    public void saveItemList(EsOrderBookSaveDto dto) {
        String id = dto.getHead().getId();
        List<EsOrderBookItem> itemList = BeanUtil.copyToList(dto.getItemList(),EsOrderBookItem.class);
        for (EsOrderBookItem esOrderBookItem : itemList) {
            esOrderBookItem.setHeadId(id);
            esOrderBookItem.insertInit();
        }
        esOrderBookItemService.saveBatch(itemList);
    }


}
