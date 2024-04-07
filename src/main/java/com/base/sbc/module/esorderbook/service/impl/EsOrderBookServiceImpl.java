/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
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
import java.util.*;
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
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public PageInfo<EsOrderBookItemVo> findPage(EsOrderBookQueryDto dto) {
        BaseQueryWrapper<EsOrderBookItemVo> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("head.season_id", dto.getSeasonId());
        qw.notEmptyEq("ts.year_name", dto.getYearName());
        qw.notEmptyEq("head.id", dto.getHeadId());
        qw.notEmptyIn("item.id", dto.getIdList());

        QueryGenerator.initQueryWrapperByMap(qw, dto);
        Page<Object> objects = PageHelper.startPage(dto);
        qw.eq("item.del_flag", BaseGlobal.DEL_FLAG_NORMAL);

        StringBuilder sbStr = new StringBuilder("order by CASE ");
        for (int i = 1; i <= 12; i++) {
            sbStr.append(" WHEN SUBSTRING_INDEX(item.group_name,'-',1) = '").append(i).append("A' THEN ").append(i).append("0")
                    .append(" WHEN SUBSTRING_INDEX(item.group_name,'-',1) = '").append(i).append("B' THEN ").append(i).append("1")
                    .append(" WHEN SUBSTRING_INDEX(item.group_name,'-',1) = '").append(i).append("C' THEN ").append(i).append("2")
                    .append(" WHEN SUBSTRING_INDEX(item.group_name,'-',1) = '").append(i).append("D' THEN ").append(i).append("3")
                    .append(" WHEN SUBSTRING_INDEX(item.group_name,'-',1) = '").append(i).append("E' THEN ").append(i).append("4");
        }
        sbStr.append(" ELSE 999 END,SUBSTRING_INDEX(item.group_name,'-',-1),item.sort_index");
        qw.last(sbStr.toString());

        List<EsOrderBookItemVo> list = baseMapper.findPage(qw);

        //组装费用信息
        List<StylePricingVO> stylePricingList = list.stream().map(o -> {
            StylePricingVO vo = new StylePricingVO();
            vo.setStylePricingId(o.getStylePricingId());
            vo.setPackType(o.getPackType());
            vo.setProductionType(o.getDevtTypeName());
            return vo;
        }).distinct().collect(Collectors.toList());
        stylePricingService.dataProcessing(stylePricingList, getCompanyCode(), true);
        Map<String, StylePricingVO> collect = stylePricingList.stream().collect(Collectors.toMap(StylePricingVO::getId, o -> o, (v1, v2) -> v1));
        for (EsOrderBookItemVo esOrderBookItemVo : list) {
            if (collect.containsKey(esOrderBookItemVo.getStylePricingId())) {
                StylePricingVO stylePricingVO = collect.get(esOrderBookItemVo.getStylePricingId());
                esOrderBookItemVo.setWoolenYarnProcessingFee(stylePricingVO.getWoolenYarnProcessingFee());
                esOrderBookItemVo.setSewingProcessingFee(stylePricingVO.getSewingProcessingFee());
                esOrderBookItemVo.setCoordinationProcessingFee(stylePricingVO.getCoordinationProcessingFee());
                esOrderBookItemVo.setActualMagnification(stylePricingVO.getActualMagnification());
                esOrderBookItemVo.setTotalCost(stylePricingVO.getTotalCost());
                esOrderBookItemVo.setMultiplePrice(esOrderBookItemVo.getTotalCost().multiply(esOrderBookItemVo.getActualMagnification()));
            }
        }
        minioUtils.setObjectUrlToList(list, "groupImg");
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public void lock(List<EsOrderBookItemVo> list) {
        Map<String, List<String>> headIdGroupNames =
                list.stream().collect(Collectors.groupingBy(EsOrderBookItemVo::getHeadId,
                        Collectors.mapping(EsOrderBookItemVo::getGroupName, Collectors.toList())));
        LambdaUpdateWrapper<EsOrderBookItem> updateWrapper;
        for (Map.Entry<String, List<String>> entry : headIdGroupNames.entrySet()) {
            String headId = entry.getKey();
            List<String> groupNames = entry.getValue();
            updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(EsOrderBookItem::getIsLock, BaseGlobal.YES);
            updateWrapper.set(EsOrderBookItem::getUpdateId, getUserId());
            updateWrapper.set(EsOrderBookItem::getUpdateName, getUserName());
            updateWrapper.set(EsOrderBookItem::getUpdateDate, new Date());
            updateWrapper.eq(EsOrderBookItem::getHeadId, headId);
            updateWrapper.in(EsOrderBookItem::getGroupName, groupNames);
            esOrderBookItemService.update(updateWrapper);
        }
    }

    @Override
    @Transactional
    public void unLock(List<EsOrderBookItemVo> list) {
        Map<String, List<String>> headIdGroupNames =
                list.stream().collect(Collectors.groupingBy(EsOrderBookItemVo::getHeadId,
                        Collectors.mapping(EsOrderBookItemVo::getGroupName, Collectors.toList())));
        LambdaUpdateWrapper<EsOrderBookItem> updateWrapper;
        for (Map.Entry<String, List<String>> entry : headIdGroupNames.entrySet()) {
            String headId = entry.getKey();
            List<String> groupNames = entry.getValue();
            updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(EsOrderBookItem::getIsLock, BaseGlobal.NO);
            updateWrapper.set(EsOrderBookItem::getUpdateId, getUserId());
            updateWrapper.set(EsOrderBookItem::getUpdateName, getUserName());
            updateWrapper.set(EsOrderBookItem::getUpdateDate, new Date());
            updateWrapper.eq(EsOrderBookItem::getHeadId, headId);
            updateWrapper.in(EsOrderBookItem::getGroupName, groupNames);
            esOrderBookItemService.update(updateWrapper);
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, EsOrderBookQueryDto dto) throws IOException {
        List<EsOrderBookItemVo> list = findPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "es订货本", response, dto);
    }

    @Override
    @Transactional
    public void del(List<EsOrderBookItemVo> list) {
        Map<String, List<String>> headIdGroupNames =
                list.stream().collect(Collectors.groupingBy(EsOrderBookItemVo::getHeadId,
                        Collectors.mapping(EsOrderBookItemVo::getGroupName, Collectors.toList())));
        LambdaQueryWrapper<EsOrderBookItem> queryWrapper;
        for (Map.Entry<String, List<String>> entry : headIdGroupNames.entrySet()) {
            String headId = entry.getKey();
            List<String> groupNames = entry.getValue();
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EsOrderBookItem::getHeadId, headId);
            queryWrapper.in(EsOrderBookItem::getGroupName, groupNames);
            esOrderBookItemService.remove(queryWrapper);
        }
        for (EsOrderBookItemVo esOrderBookItemVo : list) {
            this.saveOperaLog("删除", "es订货本", esOrderBookItemVo.getHeadName() + "_" + esOrderBookItemVo.getGroupName(), esOrderBookItemVo.getStyleNo(), new EsOrderBook(), null);
        }
    }

    @Override
    @Transactional
    public void delItem(List<EsOrderBookItemVo> list) {
        List<String> ids = list.stream().map(EsOrderBookItemVo::getId).collect(Collectors.toList());
        esOrderBookItemService.removeByIds(ids);
        //单行删除时，重新排序当前组所有数据
        String headId = list.get(0).getHeadId();
        String groupName = list.get(0).getGroupName();
        List<EsOrderBookItem> itemListBy = getItemListBy(headId, groupName);
        if (itemListBy.size() == 1) {
            itemListBy.get(0).setSortIndex(-1);
            esOrderBookItemService.updateBatchById(itemListBy);
        } else if (itemListBy.size() > 1) {
            for (int i = 0; i < itemListBy.size(); i++) {
                itemListBy.get(i).setSortIndex(i + 1);
            }
            itemListBy.get(itemListBy.size() - 1).setSortIndex(999);
            esOrderBookItemService.updateBatchById(itemListBy);
        }
        //this.saveOperaLog("删除", "es订货本", sampleStyleColor.getColorName(), sampleStyleColor.getStyleNo(), styleColor, styleColor1);
    }

    private List<EsOrderBookItem> getItemListBy(String headId, String groupName) {
        LambdaQueryWrapper<EsOrderBookItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EsOrderBookItem::getHeadId, headId);
        queryWrapper.eq(EsOrderBookItem::getGroupName, groupName);
        queryWrapper.orderByAsc(EsOrderBookItem::getSortIndex);
        return esOrderBookItemService.list(queryWrapper);
    }

    @Override
    @Transactional
    public void delHead(String id) {
        //查询一下是否全部删除了，给头数据也删除一下
        List<EsOrderBookItem> headId = esOrderBookItemService.listByField("head_id", Collections.singleton(id));
        List<String> dbHeadIds = headId.stream().map(EsOrderBookItem::getHeadId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(dbHeadIds)) {
            removeById(id);
        } else {
            throw new OtherException("该订货本中还有内容，无法删除");
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
        String extName = FileUtil.extName(file.getOriginalFilename());
        Assert.notEmpty(vo.getPlanningSeason(), "请先选中产品季");
        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(file, "PDM/esOrderBook/" + vo.getPlanningSeason() + "/" + System.currentTimeMillis() + "." + extName);
        LambdaUpdateWrapper<EsOrderBookItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(EsOrderBookItem::getGroupImg, CommonUtils.removeQuery(attachmentVo.getUrl()));
        updateWrapper.set(EsOrderBookItem::getUpdateId, getUserId());
        updateWrapper.set(EsOrderBookItem::getUpdateName, getUserName());
        updateWrapper.set(EsOrderBookItem::getUpdateDate, new Date());
        updateWrapper.eq(EsOrderBookItem::getHeadId, vo.getHeadId());
        updateWrapper.eq(EsOrderBookItem::getGroupName, vo.getGroupName());
        esOrderBookItemService.update(updateWrapper);
        return ApiResult.success("操作成功");
    }

    @Override
    public void saveItemList(EsOrderBookSaveDto dto) {
        String id = dto.getHead().getId();
        List<EsOrderBookItem> itemList = BeanUtil.copyToList(dto.getItemList(), EsOrderBookItem.class);
        String groupName = itemList.get(0).getGroupName();
        //根据headId + groupName 查询是否存在重复数据，重复数据跳过添加
        List<EsOrderBookItem> list = getItemListBy(id, groupName);
        List<String> styleColorIds = list.stream().map(EsOrderBookItem::getStyleColorId).distinct().collect(Collectors.toList());

        itemList = itemList.stream().filter(o -> !styleColorIds.contains(o.getStyleColorId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(itemList)) {
            Integer sortIndex = 1;
            if (list.size() == 1) {
                EsOrderBookItem esOrderBookItem = list.get(0);
                esOrderBookItem.setSortIndex(sortIndex);
                esOrderBookItemService.updateById(esOrderBookItem);
                sortIndex++;
            } else if (list.size() > 1) {
                sortIndex = list.get(list.size() - 2).getSortIndex();
                EsOrderBookItem esOrderBookItem = list.get(list.size() - 1);
                esOrderBookItem.setSortIndex(sortIndex + 1);
                esOrderBookItemService.updateById(esOrderBookItem);
                sortIndex++;
            }
            for (EsOrderBookItem esOrderBookItem : itemList) {
                esOrderBookItem.setHeadId(id);
                esOrderBookItem.setSortIndex(sortIndex);
                esOrderBookItem.insertInit();
                sortIndex++;
            }
            if (list.size() == 0 && itemList.size() == 1) {
                //当前组只有一条，设置为-1，标识不能进行移动
                itemList.get(0).setSortIndex(-1);
            } else {
                //最后一条设置999，标识不能向下移动
                itemList.get(itemList.size() - 1).setSortIndex(999);
            }
            esOrderBookItemService.saveBatch(itemList);
        }
    }

    @Override
    @Transactional
    public void updateItemSort(EsOrderBookItemVo vo) {
        //type 0表示上移， 1表示下移
        List<EsOrderBookItem> itemListBy = getItemListBy(vo.getHeadId(), vo.getGroupName());
        Integer sortIndex = vo.getSortIndex();
        String type = vo.getType();
        if("0".equals(type)){
            EsOrderBookItem esOrderBookItem = itemListBy.get(sortIndex);
            esOrderBookItem.setSortIndex(sortIndex-1);
            EsOrderBookItem esOrderBookItem1 = itemListBy.get(sortIndex - 1);
            esOrderBookItem1.setSortIndex(sortIndex+1);
            esOrderBookItemService.updateBatchById(Arrays.asList(esOrderBookItem,esOrderBookItem1));
        }else{
            EsOrderBookItem esOrderBookItem = itemListBy.get(sortIndex);
            esOrderBookItem.setSortIndex(sortIndex+1);
            EsOrderBookItem esOrderBookItem1 = itemListBy.get(sortIndex + 1);
            esOrderBookItem1.setSortIndex(sortIndex-1);
            esOrderBookItemService.updateBatchById(Arrays.asList(esOrderBookItem,esOrderBookItem1));
        }
    }


}
