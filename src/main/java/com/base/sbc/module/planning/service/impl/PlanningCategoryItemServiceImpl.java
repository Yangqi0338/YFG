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
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.formType.service.FieldManagementService;
import com.base.sbc.module.formType.service.FieldValService;
import com.base.sbc.module.formType.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formType.utils.FormTypeCodes;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
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
public class PlanningCategoryItemServiceImpl extends ServicePlusImpl<PlanningCategoryItemMapper, PlanningCategoryItem> implements PlanningCategoryItemService {

    @Autowired
    PlanningSeasonService planningSeasonService;
    @Autowired
    PlanningBandService planningBandService;
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
    PlanningCategoryService planningCategoryService;
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
        //编码规则条件
        Map<String, String> params = new HashMap<>(12);
        params.put("brand", planningSeason.getBrand());
        params.put("year", planningSeason.getYear());
        params.put("season", planningSeason.getSeason());
        params.put("category", getCategory(category.getCategoryName()));
        for (int i = 0; i < planRequirementNum.intValue(); i++) {
            PlanningCategoryItem item = new PlanningCategoryItem();
            item.setCompanyCode(companyCode);
            item.preInsert(idGen.nextIdStr());
            item.preUpdate();
            item.setPlanningSeasonId(category.getPlanningSeasonId());
            item.setPlanningBandId(category.getPlanningBandId());
            item.setPlanningCategoryId(category.getId());
            GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
            String designCode = Optional.ofNullable(CollUtil.get(dbCategoryItemList, i)).map(PlanningCategoryItem::getDesignNo).orElse(getMaxCode.genCode("PLANNING_DESIGN_NO", params));
            System.out.println("planningDesignNo:" + designCode);
            item.setDesignNo(designCode);
            insertCount += save(item) ? 1 : 0;

        }
        return insertCount;
    }

    public String getNextCode(String brand,String year,String season,String category){
        if(StrUtil.contains(category,StrUtil.COMMA)){
            category=getCategory(category);
        }
        Map<String, String> params = new HashMap<>(12);
        params.put("brand", brand);
        params.put("year", year);
        params.put("season", season);
        params.put("category", category);
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        String planningDesignNo = getMaxCode.genCode("PLANNING_DESIGN_NO", params);
        return planningDesignNo;
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
    public List<String> selectCategoryIdsByBand(QueryWrapper qw) {
        return getBaseMapper().selectCategoryIdsByBand(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public void updateAndCommit(String planningBandId, List<PlanningCategoryItemSaveDto> item) {


        //提交
        if (StrUtil.isNotBlank(planningBandId)) {
            PlanningBand byId = planningBandService.getById(planningBandId);
            if (byId == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            byId.setStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            planningBandService.updateById(byId);
        } else {
            for (PlanningCategoryItemSaveDto dto : item) {
                PlanningCategoryItem categoryItem = BeanUtil.copyProperties(dto, PlanningCategoryItem.class);
                // 修改
                updateById(categoryItem);
                fieldValService.save(categoryItem.getId(), FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION, dto.getFieldVals());
            }
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
        String formatStr=CollUtil.join(textFormats, "");
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
            String newDesigner = allocationDesignDto.getDesigner();
            if (!newDesigner.contains(",")) {
                throw new OtherException("设计师名称格式为:名称,代码");
            }

            String oldDesigner = planningCategoryItem.getDesigner();
            if(StrUtil.equals(newDesigner,oldDesigner)){
                continue;
            }

            String newDesignerCode = newDesigner.split(",")[1];
            //重新设置款号
            String designNo = planningCategoryItem.getDesignNo();
            //如果还没设置设计师 款号= 款号+设计师代码
            if (StrUtil.isBlank(oldDesigner)) {
                planningCategoryItem.setDesignNo(designNo + newDesignerCode);
            } else {
                //如果已经设置了设计师 款号=款号+新设计师代码
                String oldDesignCode = oldDesigner.split(",")[1];
                // 获取原始款号
                designNo = StrUtil.sub(designNo, 0, designNo.length() - oldDesignCode.length());
                planningCategoryItem.setDesignNo(designNo + newDesignerCode);
            }
            BeanUtil.copyProperties(allocationDesignDto, planningCategoryItem);
        }
        return updateBatchById(planningCategoryItems);
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

    @Override
    public ApiResult findProductCategoryItem(ProductCategoryItemSearchDto dto) {

        QueryWrapper qw = new QueryWrapper();
        // 设计款号
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "c.design_no", dto.getSearch());
        //产品季
        qw.eq(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "c.planning_season_id", dto.getPlanningSeasonId());
        // 品类
        qw.in(CollUtil.isNotEmpty(dto.getCategoryIds()), "c.category_id", dto.getCategoryIds());
        // 波段企划
        qw.eq(StrUtil.isNotBlank(dto.getPlanningBandId()), "c.planning_band_id", dto.getPlanningBandId());
        // 设计师
        qw.in(CollUtil.isNotEmpty(dto.getDesignerIds()), "c.designer_id", dto.getDesignerIds());
        // 任务等级
        qw.in(CollUtil.isNotEmpty(dto.getTaskLevels()), "c.task_level", dto.getTaskLevels());
        // 状态 多选
        qw.in(CollUtil.isNotEmpty(dto.getStatusList()), "c.status", dto.getStatusList());
        // 状态 单个
        qw.eq(StrUtil.isNotBlank(dto.getStatus()), "c.status", dto.getStatus());

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
                            return su;
                        }).collect(Collectors.toList());
                    }).orElse(null);
                    tct.setDesigners(sampleUserVos);
                    pdMap.put(key, sampleUserVos);
                }
            }
        }


        return ApiResult.success("SUCCESS_OK", pageInfo, null);
    }

    @Override
    public List<BasicStructureTreeVo> expandByCategory(ProductSeasonExpandByCategorySearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qw.eq("c.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qw.eq("b.status", BaseGlobal.STOCK_STATUS_CHECKED);
        qw.eq("b.planning_season_id", dto.getPlanningSeasonId());
        qw.eq("c.planning_season_id", dto.getPlanningSeasonId());

        List<String> categoryIds=selectCategoryIdsByBand(qw);
        if(CollUtil.isEmpty(categoryIds)){
            return null;
        }
        return ccmFeignService.findStructureTreeByCategoryIds(CollUtil.join(categoryIds, ","));
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean send(List<PlanningCategoryItem> categoryItemList) {
        // 1、保存
        List<AllocationDesignDto> allocationDesignDtoList =new ArrayList<>(16);
        List<SetTaskLevelDto> setTaskLevelDtoList= new ArrayList<>(16);
        // 坑位id
        List<String> itemIds=new ArrayList<>(16);
        // 产品季id
        List<String> seasonIds=new ArrayList<>(16);
        // 波段企划Id
        List<String> bandIds=new ArrayList<>(16);
        //图片信息
        List<String>fileUrls=new ArrayList<>(16);
        for (PlanningCategoryItem planningCategoryItem : categoryItemList) {
            allocationDesignDtoList.add(BeanUtil.copyProperties(planningCategoryItem, AllocationDesignDto.class));
            setTaskLevelDtoList.add(BeanUtil.copyProperties(planningCategoryItem, SetTaskLevelDto.class));
            itemIds.add(planningCategoryItem.getId());
            seasonIds.add(planningCategoryItem.getPlanningSeasonId());
            bandIds.add(planningCategoryItem.getPlanningBandId());
            if(StrUtil.isNotBlank(planningCategoryItem.getStylePic())){
                fileUrls.add(planningCategoryItem.getStylePic());
            }
        }
        // 1.1 分配设计师
        this.allocationDesign(allocationDesignDtoList);
        // 1.2 设置任务等级
        this.setTaskLevel(setTaskLevelDtoList);

        // 2 状态修改为已下发
        UpdateWrapper<PlanningCategoryItem> uw=new UpdateWrapper<>();
        uw.set("status","1");
        uw.in("id",itemIds);
        update(uw);
        // 3 将数据写入样衣设计
        // 查询已经下发的任务
        QueryWrapper<SampleDesign> existsQw=new QueryWrapper<>();
        existsQw.in("planning_category_item_id",itemIds);
        List<SampleDesign> dbItemList = sampleDesignService.list(existsQw);
        Map<String, SampleDesign> dbItemMap = Optional.ofNullable(dbItemList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getPlanningCategoryItemId(), v -> v, (a, b) -> b));
        // 查询产品季
        QueryWrapper<PlanningSeason> seasonQw=new QueryWrapper<>();
        seasonQw.in("id",seasonIds);
        List<PlanningSeason> seasonList = planningSeasonService.list(seasonQw);
        Map<String, PlanningSeason> seasonMap = Optional.ofNullable(seasonList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
        // 查询波段企划
        QueryWrapper<PlanningBand> bandQw=new QueryWrapper<>();
        bandQw.in("id",bandIds);
        List<PlanningBand> bandList = planningBandService.list(bandQw);
        // 图片文件id
        Map<String,String> fileUrlId=uploadFileService.findMapByUrls(fileUrls);
        Map<String, PlanningBand> bandMap = Optional.ofNullable(bandList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));

        List<SampleDesign> sampleDesignList =new ArrayList<>(16);

        for (PlanningCategoryItem item : categoryItemList) {
            if(dbItemMap.containsKey(item.getId())){
                continue;
            }
            SampleDesign sampleDesign = PlanningUtils.toSampleDesign(seasonMap.get(item.getPlanningSeasonId()), bandMap.get(item.getPlanningBandId()), item);
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
                    a.setFId(sampleDesign.getId());
                    a.setFileId(sampleDesign.getStylePic());
                    attachments.add(a);
                    List<FieldVal> fvList = fieldValService.list(sampleDesign.getPlanningCategoryItemId(), FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION);
                    if (CollUtil.isNotEmpty(fvList)) {
                        fvList.forEach(item -> {
                            item.setId(null);
                            item.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
                            item.setFId(sampleDesign.getId());
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
        PlanningCategory category = planningCategoryService.getById(seat.getPlanningCategoryId());
        List<String> categoryIds = StrUtil.split(category.getCategoryIds(), CharUtil.COMMA);
        List<FieldManagementVo> fieldList = fieldManagementService.list(FormTypeCodes.DIMENSION_LABELS, CollUtil.get(categoryIds, 1), season.getSeason());
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


}
