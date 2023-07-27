/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.CountVo;
import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.formType.service.FieldManagementService;
import com.base.sbc.module.formType.service.FieldValService;
import com.base.sbc.module.formType.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formType.utils.FormTypeCodes;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.ChartBarVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：企划-坑位信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 */
@Service
public class PlanningCategoryItemServiceImpl extends BaseServiceImpl<PlanningCategoryItemMapper, PlanningCategoryItem> implements PlanningCategoryItemService {

    @Autowired
    PlanningSeasonService planningSeasonService;

    @Autowired
    PlanningCategoryItemMaterialService planningCategoryItemMaterialService;
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    CcmService ccmService;
    @Autowired
    AmcFeignService amcFeignService;
    @Autowired
    CcmFeignService ccmFeignService;
    @Autowired
    SampleDesignService sampleDesignService;
    @Autowired
    FieldManagementService fieldManagementService;
    @Autowired
    FieldValService fieldValService;
    @Autowired
    AttachmentService attachmentService;



    @Autowired
    PlanningChannelService planningChannelService;


    private IdGen idGen = new IdGen();

    /**
     * 保存坑位信息
     *
     * @return
     */
    @Transactional(readOnly = false)
    public int saveCategoryItem(PlanningBand band, PlanningCategory category, List<PlanningCategoryItem> dbCategoryItemList) {
        //通过企划需求数生成
        BigDecimal planRequirementNum = category.getPlanRequirementNum();
        String companyCode = band.getCompanyCode();
        //坑位信息条件
        QueryCondition categoryItemQc = new QueryCondition(companyCode);
        categoryItemQc.andEqualTo("planning_category_id", category.getId());
        //删除之前数据
        delByPlanningCategory(companyCode, CollUtil.newArrayList(category.getId()));
        if (planRequirementNum == null || planRequirementNum.intValue() == 0) {
            return 0;
        }
        PlanningSeason planningSeason = planningSeasonService.getById(band.getPlanningSeasonId());
        IdGen idGen = new IdGen();
        int insertCount = 0;
        for (int i = 0; i < planRequirementNum.intValue(); i++) {
            PlanningCategoryItem item = new PlanningCategoryItem();
            item.setCompanyCode(companyCode);
            item.preInsert(idGen.nextIdStr());
            item.preUpdate();
            item.setPlanningSeasonId(category.getPlanningSeasonId());
            item.setPlanningBandId(category.getPlanningBandId());
            item.setPlanningCategoryId(category.getId());
            item.setCategoryIds(category.getCategoryIds());
            String designCode = Optional.ofNullable(CollUtil.get(dbCategoryItemList, i)).map(PlanningCategoryItem::getDesignNo).orElse(
                    getNextCode(
                            planningSeason.getBrand(),
                            planningSeason.getYear(),
                            planningSeason.getSeason(),
                            category.getCategoryName()));
            System.out.println("planningDesignNo:" + designCode);
            item.setDesignNo(designCode);
            PlanningUtils.setCategory(item);
            insertCount += save(item) ? 1 : 0;

        }
        return insertCount;
    }

    @Override
    public String getNextCode(String brand, String year, String season, String category) {
        Map<String, String> params = genNexcCodeParams(brand, year, season, category);
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        String planningDesignNo = getMaxCode.genCode("PLANNING_DESIGN_NO", params);
        return planningDesignNo;
    }

    @Override
    public List<String> getNextCode(String brand, String year, String season, String category, int count) {
        Map<String, String> params = genNexcCodeParams(brand, year, season, category);
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        List<String> planningDesignNo = getMaxCode.genCode("PLANNING_DESIGN_NO", count, params);
        return planningDesignNo;
    }

    private Map<String, String> genNexcCodeParams(String brand, String year, String season, String category) {
        if (StrUtil.contains(category, StrUtil.COMMA)) {
            category = getCategory(category);
        }
        Map<String, String> params = new HashMap<>(12);
        // ED 品牌取E品牌的编码 （暂时这么做，后续优化）
        if (StrUtil.equals(brand, "ED")) {
            params.put("brand", "1");
        } else {
            params.put("brand", brand);
        }
        params.put("year", year);
        params.put("season", season);
        params.put("category", category);
        return params;
    }


    @Transactional(readOnly = false)
    public boolean delByPlanningCategory(String companyCode, List<String> ids) {
        //删除坑位信息
        remove(new QueryWrapper<PlanningCategoryItem>().in("planning_category_id", ids));

        // 删除坑位信息关联的素材库
        planningCategoryItemMaterialService.remove(new QueryWrapper<PlanningCategoryItemMaterial>().in("planning_category_id", ids));
        return true;
    }

    private String getDesignCode(String manager) {
        if (StrUtil.isBlank(manager)) {
            throw new OtherException("获取设计师编码失败");
        }
        try {
            return manager.split(StrUtil.COMMA)[1];
        } catch (Exception e) {
            throw new OtherException("获取设计师编码失败");
        }
    }

    private String getCategory(String categoryName) {
        if (StrUtil.isBlank(categoryName)) {
            throw new OtherException("未选择品类,不能生成设计款号");
        }
        String categoryCode = null;
        try {
            categoryCode = categoryName.split(StrUtil.SLASH)[1].split(StrUtil.COMMA)[1];
        } catch (Exception e) {
            throw new OtherException("品类编码获取失败");
        }
        return categoryCode;

    }

    @Transactional(readOnly = false)
    public boolean delByPlanningBand(String userCompany, String id) {
        //删除坑位信息
        remove(new QueryWrapper<PlanningCategoryItem>().eq("planning_band_id", id));
        // 删除坑位信息关联的素材库
        planningCategoryItemMaterialService.remove(new QueryWrapper<PlanningCategoryItemMaterial>().eq("planning_band_id", id));
        return true;
    }

    @Override
    public String selectMaxDesignNo(QueryWrapper qc) {
        return getBaseMapper().selectMaxDesignNo(qc);
    }



    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateCategoryItem(List<PlanningCategoryItemSaveDto> item) {
        for (PlanningCategoryItemSaveDto dto : item) {
            PlanningCategoryItem categoryItem = BeanUtil.copyProperties(dto, PlanningCategoryItem.class);
            // 修改
            PlanningUtils.setCategory(categoryItem);
            updateById(categoryItem);
            fieldValService.save(categoryItem.getId(), FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION, dto.getFieldVals());
        }

    }

    @Override
    public String getMaxDesignNo(GetMaxCodeRedis data, String userCompany) {
        List<String> regexps = new ArrayList<>(12);
        List<String> textFormats = new ArrayList<>(12);
        data.getValueMap().forEach((key, val) -> {
            if (BaseConstant.FLOWING.equals(key)) {
                textFormats.add("{0}");
            } else {
                textFormats.add(String.valueOf(val));
            }
            regexps.add(String.valueOf(val));
        });
        String regexp = "^" + CollUtil.join(regexps, "") + "$";
        System.out.println("传过来的正则:" + regexp);
        QueryWrapper qc = new QueryWrapper();
        qc.eq(COMPANY_CODE, userCompany);
        qc.apply(" design_no REGEXP '" + regexp + "'");
        qc.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        String maxCode = selectMaxDesignNo(qc);
        if (StrUtil.isBlank(maxCode)) {
            return null;
        }
        // 替换,保留流水号
        String formatStr = CollUtil.join(textFormats, "");
        MessageFormat mf = new MessageFormat(formatStr);
        try {
            Object[] parse = mf.parse(maxCode);
            if (parse != null && parse.length > 0) {
                return String.valueOf(parse[0]);
            }
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean allocationDesign(List<AllocationDesignDto> dtoList) {
        // 查询数据是否存在
        Map<String, AllocationDesignDto> dtoMap = dtoList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
        List<PlanningCategoryItem> planningCategoryItems = listByIds(dtoMap.keySet());
        if (dtoList.size() != planningCategoryItems.size()) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {
            AllocationDesignDto allocationDesignDto = dtoMap.get(planningCategoryItem.getId());
            String newDesignNO = getNewDesignNo(planningCategoryItem.getDesignNo(), planningCategoryItem.getDesigner(), allocationDesignDto.getDesigner());
            planningCategoryItem.setDesignNo(newDesignNO);
            BeanUtil.copyProperties(allocationDesignDto, planningCategoryItem);
        }
        return updateBatchById(planningCategoryItems);
    }

    /**
     * 获取新的款号
     *
     * @param oldDesignNo 旧款号
     * @param oldDesigner 旧设计师
     * @param newDesigner 新设计师
     * @return
     */
    public String getNewDesignNo(String oldDesignNo, String oldDesigner, String newDesigner) {
        String newDesignNo = oldDesignNo;
        if (!newDesigner.contains(StrUtil.COMMA)) {
            throw new OtherException("设计师名称格式为:名称,代码");
        }

        if (StrUtil.equals(newDesigner, oldDesigner)) {
            return newDesignNo;
        }

        String newDesignerCode = newDesigner.split(",")[1];

        //如果还没设置设计师 款号= 款号+设计师代码
        if (StrUtil.isBlank(oldDesigner)) {
            newDesignNo = oldDesignNo + newDesignerCode;
        } else {
            //如果已经设置了设计师 款号=款号+新设计师代码
            String oldDesignCode = oldDesigner.split(",")[1];
            // 获取原始款号
            newDesignNo = StrUtil.sub(oldDesignNo, 0, oldDesignNo.length() - oldDesignCode.length()) + newDesignerCode;
        }
        return newDesignNo;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean setTaskLevel(List<SetTaskLevelDto> dtoList) {
        Map<String, SetTaskLevelDto> dtoMap = dtoList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
        List<PlanningCategoryItem> planningCategoryItems = listByIds(dtoMap.keySet());
        if (dtoList.size() != planningCategoryItems.size()) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {
            planningCategoryItem.setTaskLevel(dtoMap.get(planningCategoryItem.getId()).getTaskLevel());
        }
        updateBatchById(planningCategoryItems);
        return true;
    }

    private void setSeatQw(QueryWrapper<PlanningCategoryItem> qw, ProductCategoryItemSearchDto dto) {

    }

    @Override
    public PageInfo<PlanningSeasonOverviewVo> findProductCategoryItem(ProductCategoryItemSearchDto dto) {
        QueryWrapper<PlanningCategoryItem> qw = new QueryWrapper();
        // 设计款号
        qw.and(StrUtil.isNotBlank(dto.getSearch()), qwi -> qwi.like("c.design_no", dto.getSearch()).or().like("s.name", dto.getSearch()));
        //产品季
        qw.eq(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "c.planning_season_id", dto.getPlanningSeasonId());
        //月份
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "c.month", dto.getMonth());
        //波段
        qw.eq(StrUtil.isNotBlank(dto.getBandCode()), "c.band_code", dto.getBandCode());
        qw.eq(StrUtil.isNotBlank(dto.getPlanningChannelId()), "c.planning_channel_id", dto.getPlanningChannelId());
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory()), "c.prod_category", dto.getProdCategory());
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory1st()), "c.prod_category1st", dto.getProdCategory1st());
        // 品类
        qw.in(CollUtil.isNotEmpty(dto.getCategoryIds()), "c.prod_category", dto.getCategoryIds());
        // 设计师
        qw.in(CollUtil.isNotEmpty(dto.getDesignerIds()), "c.designer_id", dto.getDesignerIds());
        // 任务等级
        qw.in(CollUtil.isNotEmpty(dto.getTaskLevels()), "c.task_level", dto.getTaskLevels());
        // 状态
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "c.status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        Page<PlanningSeasonOverviewVo> objects = PageHelper.startPage(dto);
        getBaseMapper().listSeat(qw);
        PageInfo<PlanningSeasonOverviewVo> pageInfo = objects.toPageInfo();
        List<PlanningSeasonOverviewVo> list = pageInfo.getList();
        //获取设计师信息
        // 设置版师列表
        if (CollUtil.isNotEmpty(list)) {
            Map<String, List<SampleUserVo>> pdMap = new HashMap<>(16);
            for (PlanningSeasonOverviewVo tct : list) {
                String key = tct.getPlanningSeasonId();
                if (pdMap.containsKey(key)) {
                    tct.setDesigners(pdMap.get(key));
                } else {
                    List<UserCompany> designList = amcFeignService.getTeamUserListByPost(tct.getPlanningSeasonId(), "设计师");
                    List<SampleUserVo> sampleUserVos = Optional.of(designList).map(dl -> {
                        return dl.stream().map(item -> {
                            SampleUserVo su = new SampleUserVo();
                            su.setUserId(item.getUserId());
                            su.setAvatar(Optional.ofNullable(item.getAvatar()).orElse(item.getAliasUserAvatar()));
                            su.setName(item.getAliasUserName());
                            su.setUserCode(item.getUserCode());
                            return su;
                        }).collect(Collectors.toList());
                    }).orElse(null);
                    tct.setDesigners(sampleUserVos);
                    pdMap.put(key, sampleUserVos);
                }
            }
        }
        return pageInfo;
    }



    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean send(List<PlanningCategoryItem> categoryItemList) {
        // 1、保存
        List<AllocationDesignDto> allocationDesignDtoList = new ArrayList<>(16);
        List<SetTaskLevelDto> setTaskLevelDtoList = new ArrayList<>(16);
        // 坑位id
        List<String> itemIds = new ArrayList<>(16);
        // 产品季id
        List<String> seasonIds = new ArrayList<>(16);
        // 波段企划Id
        List<String> bandIds = new ArrayList<>(16);
        //图片信息
        List<String> fileUrls = new ArrayList<>(16);
        for (PlanningCategoryItem planningCategoryItem : categoryItemList) {
            allocationDesignDtoList.add(BeanUtil.copyProperties(planningCategoryItem, AllocationDesignDto.class));
            setTaskLevelDtoList.add(BeanUtil.copyProperties(planningCategoryItem, SetTaskLevelDto.class));
            itemIds.add(planningCategoryItem.getId());
            seasonIds.add(planningCategoryItem.getPlanningSeasonId());
            bandIds.add(planningCategoryItem.getPlanningBandId());
            if (StrUtil.isNotBlank(planningCategoryItem.getStylePic())) {
                fileUrls.add(planningCategoryItem.getStylePic());
            }
        }
        // 1.1 分配设计师
        this.allocationDesign(allocationDesignDtoList);
        // 1.2 设置任务等级
        this.setTaskLevel(setTaskLevelDtoList);

        // 2 状态修改为已下发 下发时间
        UpdateWrapper<PlanningCategoryItem> uw = new UpdateWrapper<>();
        uw.set("status", "2");
        uw.set("send_date", new Date());
        uw.in("id", itemIds);
        update(uw);
        // 3 将数据写入样衣设计
        // 查询已经下发的任务
        QueryWrapper<SampleDesign> existsQw = new QueryWrapper<>();
        existsQw.in("planning_category_item_id", itemIds);
        List<SampleDesign> dbItemList = sampleDesignService.list(existsQw);
        Map<String, SampleDesign> dbItemMap = Optional.ofNullable(dbItemList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getPlanningCategoryItemId(), v -> v, (a, b) -> b));
        // 查询产品季
        QueryWrapper<PlanningSeason> seasonQw = new QueryWrapper<>();
        seasonQw.in("id", seasonIds);
        List<PlanningSeason> seasonList = planningSeasonService.list(seasonQw);
        Map<String, PlanningSeason> seasonMap = Optional.ofNullable(seasonList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));

        // 图片文件id
        Map<String, String> fileUrlId = uploadFileService.findMapByUrls(fileUrls);

        List<SampleDesign> sampleDesignList = new ArrayList<>(16);

        for (PlanningCategoryItem item : categoryItemList) {
            if (dbItemMap.containsKey(item.getId())) {
                continue;
            }
            SampleDesign sampleDesign = PlanningUtils.toSampleDesign(seasonMap.get(item.getPlanningSeasonId()), item);
            sampleDesign.setSender(getUserId());
            sampleDesign.setStylePic(Optional.ofNullable(fileUrlId.get(sampleDesign.getStylePic())).orElse(""));
            sampleDesignList.add(sampleDesign);


        }
        // 保存样衣设计
        if (CollUtil.isNotEmpty(sampleDesignList)) {
            sampleDesignService.saveBatch(sampleDesignList);
            //保存图片附件
            List<Attachment> attachments = new ArrayList<>();
            // 保存维度信息
            List<FieldVal> fieldVals = new ArrayList<>(16);
            for (SampleDesign sampleDesign : sampleDesignList) {
                if (StrUtil.isNotEmpty(sampleDesign.getStylePic())) {
                    Attachment a = new Attachment();
                    a.setType(AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
                    a.setStatus(BaseGlobal.NO);
                    a.setForeignId(sampleDesign.getId());
                    a.setFileId(sampleDesign.getStylePic());
                    attachments.add(a);
                    List<FieldVal> fvList = fieldValService.list(sampleDesign.getPlanningCategoryItemId(), FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION);
                    if (CollUtil.isNotEmpty(fvList)) {
                        fvList.forEach(item -> {
                            item.setId(null);
                            item.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
                            item.setForeignId(sampleDesign.getId());
                        });
                        fieldVals.addAll(fvList);
                    }
                }
            }
            if (CollUtil.isNotEmpty(attachments)) {
                attachmentService.saveBatch(attachments);
            }
            if (CollUtil.isNotEmpty(fieldVals)) {
                fieldValService.saveBatch(fieldVals);
            }

        }
        return true;
    }

    @Override
    public List<FieldManagementVo> querySeatDimension(String id, String isSelected) {
        PlanningCategoryItem seat = getById(id);
        PlanningSeason season = planningSeasonService.getById(seat.getPlanningSeasonId());

        List<FieldManagementVo> fieldList = fieldManagementService.list(FormTypeCodes.DIMENSION_LABELS, seat.getProdCategory(), season.getSeason());
        List<FieldVal> valueList = fieldValService.list(id, FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION);
        fieldManagementService.conversion(fieldList, valueList);
        if (StrUtil.isNotBlank(isSelected) && CollUtil.isNotEmpty(fieldList)) {
            return fieldList.stream().filter(FieldManagementVo::isSelected).collect(Collectors.toList());
        }
        return fieldList;
    }

    @Override
    public boolean updateStylePic(String id, String stylePic) {
        UpdateWrapper<PlanningCategoryItem> uw = new UpdateWrapper<>();
        uw.eq("id", id);
        uw.set("style_pic", stylePic);
        return update(uw);
    }

    @Override
    public boolean updateItemBatch(PlanningCategoryItemBatchUpdateDto dto) {
        List<PlanningCategoryItem> planningCategoryItems = listByIds(dto.getIds());
        if (dto.getIds().size() != planningCategoryItems.size()) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {
            if (StrUtil.isNotBlank(dto.getDesigner())) {
                String newDesignNO = getNewDesignNo(planningCategoryItem.getDesignNo(), planningCategoryItem.getDesigner(), dto.getDesigner());
                planningCategoryItem.setDesignNo(newDesignNO);
            }
            BeanUtil.copyProperties(dto, planningCategoryItem);
            PlanningUtils.setCategory(planningCategoryItem);
        }
        return updateBatchById(planningCategoryItems);

    }

    @Override
    public List<SampleUserVo> getAllDesigner(String userCompany) {
        List<SampleUserVo> list = getBaseMapper().getAllDesigner(userCompany);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }

    @Override
    public List<DimensionTotalVo> dimensionTotal(QueryWrapper qw) {
        return getBaseMapper().dimensionTotal(qw);
    }

    @Override
    public List<PlanningSummaryDetailVo> planningSummaryDetail(QueryWrapper detailQw) {
        return getBaseMapper().planningSummaryDetail(detailQw);
    }

    @Override
    public List<ChartBarVo> categorySummary(QueryWrapper qw) {
        return getBaseMapper().categorySummary(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean seatSend(List<PlanningCategoryItemSaveDto> list) {
        List<String> bandIds = new ArrayList<>(2);
        List<String> seatIds = new ArrayList<>(2);
        for (PlanningCategoryItem planningCategoryItem : list) {
            seatIds.add(planningCategoryItem.getId());
            bandIds.add(planningCategoryItem.getPlanningBandId());
        }
        UpdateWrapper<PlanningCategoryItem> seatUw = new UpdateWrapper<>();
        seatUw.set("status", BasicNumber.ONE.getNumber());
        seatUw.in("status", BasicNumber.ZERO.getNumber(), "-1");
        seatUw.in("id", seatIds);
        update(seatUw);
        return true;
    }

    @Override
    public Map<String, Long> totalSkcByPlanningSeason(List<String> planningSeasonIds) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in(CollUtil.isNotEmpty(planningSeasonIds), "planning_season_id", planningSeasonIds);
        Map<String, Long> result = new HashMap<>(16);
        List<Map<String, Long>> list = getBaseMapper().totalSkcByPlanningSeason(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (Map<String, Long> stringLongMap : list) {
                result.putAll(stringLongMap);
            }
        }
        return result;
    }

    @Override
    public Map<String, Long> totalSkcByChannel(List<String> channelIds) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in("planning_channel_id", channelIds);
        Map<String, Long> result = new HashMap<>(16);
        List<Map<String, Long>> list = getBaseMapper().totalSkcByChannel(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (Map<String, Long> stringLongMap : list) {
                result.putAll(stringLongMap);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean addSeat(AddSeatDto dto) {
        //查询渠道信息
        PlanningChannel channel = planningChannelService.getById(dto.getPlanningChannelId());
        if (channel == null) {
            throw new OtherException("渠道信息为空");
        }
        PlanningCategoryItem base = BeanUtil.copyProperties(dto, PlanningCategoryItem.class);
        BeanUtil.copyProperties(channel, base);
        //获取设计编号
        List<String> nextCode = getNextCode(base.getBrand(), base.getYear(), base.getSeason(), base.getProdCategory(), dto.getCount());
        if (CollUtil.isEmpty(nextCode)) {
            throw new OtherException("设计编号生成失败");
        }
        List<PlanningCategoryItem> saveList = new ArrayList<>(16);
        for (String s : nextCode) {
            PlanningCategoryItem categoryItem = BeanUtil.copyProperties(base, PlanningCategoryItem.class);
            categoryItem.setId(null);
            CommonUtils.resetCreateUpdate(categoryItem);
            categoryItem.setDesignNo(s);
            saveList.add(categoryItem);
        }
        return saveBatch(saveList);
    }

    @Override
    public List<BasicStructureTreeVo> categoryTree(String planningChannelId) {

        List<BasicStructureTreeVo> tree = ccmFeignService.basicStructureTreeByCode("品类", null, "0,1");
        QueryWrapper qw = new QueryWrapper();
        qw.eq(StrUtil.isNotBlank(planningChannelId), "planning_channel_id", planningChannelId);
        List<CountVo> counts = getBaseMapper().countCategoryByChannelId(qw);
        if (CollUtil.isEmpty(counts)) {
            return tree;
        }
        Map<String, Long> collect = counts.stream().collect(Collectors.toMap(k -> k.getLabel(), v -> v.getCount()));
        setTreeCount(tree, collect);
        return tree;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean revoke(String ids) {
        // 查询样衣设计数据
        QueryWrapper<SampleDesign> sdQw = new QueryWrapper();
        List<String> seatIds = StrUtil.split(ids, CharUtil.COMMA);
        sdQw.in("planning_category_item_id", seatIds);
        List<SampleDesign> sdList = sampleDesignService.list(sdQw);
        //判断是否已开款
        if (CollUtil.isNotEmpty(sdList)) {
            String designNos = sdList.stream()
                    .filter(a -> StrUtil.equalsAny(a.getStatus(), BasicNumber.ONE.getNumber(), BasicNumber.TWO.getNumber()))
                    .map(a -> a.getDesignNo()).collect(Collectors.joining(StrUtil.COMMA));
            if (StrUtil.isNotBlank(designNos)) {
                throw new OtherException(designNos + "已开款无法撤回");
            }
        }
        UpdateWrapper<PlanningCategoryItem> uw = new UpdateWrapper<>();
        uw.in("id", seatIds);
        uw.set("status", "-1");
        update(uw);
        sampleDesignService.remove(sdQw);
        return true;
    }

    @Override
    public boolean del(String ids) {
        // 查询样衣设计数据
        QueryWrapper<SampleDesign> sdQw = new QueryWrapper();
        List<String> seatIds = StrUtil.split(ids, CharUtil.COMMA);
        sdQw.in("planning_category_item_id", seatIds);
        List<SampleDesign> sdList = sampleDesignService.list(sdQw);
        //判断是否已开款
        if (CollUtil.isNotEmpty(sdList)) {
            String designNos = sdList.stream()
                    .filter(a -> StrUtil.equalsAny(a.getStatus(), BasicNumber.ONE.getNumber(), BasicNumber.TWO.getNumber()))
                    .map(a -> a.getDesignNo()).collect(Collectors.joining(StrUtil.COMMA));
            if (StrUtil.isNotBlank(designNos)) {
                throw new OtherException(designNos + "已开款无法删除");
            }
        }
        removeBatchByIds(seatIds);
        sampleDesignService.remove(sdQw);
        return true;
    }

    private void setTreeCount(List<BasicStructureTreeVo> tree, Map<String, Long> counts) {
        if (CollUtil.isEmpty(tree) || CollUtil.isEmpty(counts)) {
            return;
        }
        for (BasicStructureTreeVo t : tree) {
            Long aLong = counts.get(t.getValue());
            if (aLong != null) {
                t.setName(StrUtil.format("{} ({})", t.getName(), aLong));
            }
            setTreeCount(t.getChildren(), counts);
        }

    }


}
