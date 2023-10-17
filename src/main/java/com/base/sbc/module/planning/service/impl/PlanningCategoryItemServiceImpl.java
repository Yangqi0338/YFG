/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.entity.BasicStructureTree;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.CountVo;
import com.base.sbc.module.formType.dto.QueryFieldOptionConfigDto;
import com.base.sbc.module.formType.entity.FieldOptionConfig;
import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.formType.service.FieldManagementService;
import com.base.sbc.module.formType.service.FieldOptionConfigService;
import com.base.sbc.module.formType.service.FieldValService;
import com.base.sbc.module.formType.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.formType.vo.FieldOptionConfigVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.planning.mapper.PlanningDimensionalityMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.ChartBarVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    StyleService styleService;
    @Autowired
    FieldManagementService fieldManagementService;
    @Autowired
    FieldValService fieldValService;
    @Autowired
    AttachmentService attachmentService;

    @Autowired
    MessageUtils messageUtils;
    @Autowired
    BaseController baseController;

    @Autowired
    PlanningChannelService planningChannelService;

    @Autowired
    PlanningDimensionalityMapper planningDimensionalityMapper;

    @Autowired
    FieldOptionConfigService fieldOptionConfigService;

    private IdGen idGen = new IdGen();

    @Autowired
    private DataPermissionsService dataPermissionsService;


    @Override
    public String getNextCode(Object obj) {
        Map<String, String> params = genNexcCodeParams(obj);
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        String planningDesignNo = getMaxCode.genCode("PLANNING_DESIGN_NO", params);
        return planningDesignNo;
    }

    @Override
    public List<String> getNextCode(Object obj, int count) {
        Map<String, String> params = genNexcCodeParams(obj);
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        List<String> planningDesignNo = getMaxCode.genCode("PLANNING_DESIGN_NO", count, params);
        return planningDesignNo;
    }

    private Map<String, String> genNexcCodeParams(Object obj) {

        String brand = BeanUtil.getProperty(obj, "brand");
        String year = BeanUtil.getProperty(obj, "year");
        String season = BeanUtil.getProperty(obj, "season");
        String category = BeanUtil.getProperty(obj, "prodCategory");
        String prodCategory3rd = BeanUtil.getProperty(obj, "prodCategory3rd");
        String channel = BeanUtil.getProperty(obj, "channel");

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
        params.put("channel", channel);
        params.put("prodCategory3rd", prodCategory3rd);
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
        boolean flg;
        for (PlanningCategoryItemSaveDto dto : item) {
            PlanningCategoryItem categoryItem = BeanUtil.copyProperties(dto, PlanningCategoryItem.class);
            // 修改
            flg = updateById(categoryItem);
            if (!flg) {
                throw new OtherException("修改失败,请检查是否有权限");
            }
            //处理维度标签
            if (StrUtil.equals(dto.getDimensionFlag(), BaseGlobal.YES)) {
                fieldValService.save(categoryItem.getId(), FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION, dto.getFieldVals());
            }
            // 修改款式设计数据
            styleService.updateBySeatChange(categoryItem);
        }

    }

    @Override
    public String getMaxDesignNo(GetMaxCodeRedis data, String userCompany) {
        List<String> regexps = new ArrayList<>(12);
        List<String> textFormats = new ArrayList<>(12);
        data.getValueMap().forEach((key, val) -> {
            if (BaseConstant.FLOWING.equals(key)) {
                textFormats.add("(" + val + ")");
            } else {
                textFormats.add(String.valueOf(val));
            }
            regexps.add(String.valueOf(val));
        });
        String regexp = "^" + CollUtil.join(regexps, "");
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
        try {
            String flowing = ReUtil.get(formatStr, maxCode, 1);
            if (StrUtil.isNotBlank(flowing)) {
                return flowing;
            }
            return null;
        } catch (Exception e) {
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
        Map<String, PlanningCategoryItem> seatMap = new HashMap<>(16);
        //key = old designNo val= new  designNo
        Map<String, String> designNoUpdate = new HashMap<>(16);
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {

            AllocationDesignDto allocationDesignDto = dtoMap.get(planningCategoryItem.getId());
            String newDesignNO = PlanningUtils.getNewDesignNo(planningCategoryItem.getDesignNo(), planningCategoryItem.getDesigner(), allocationDesignDto.getDesigner());
            designNoUpdate.put(planningCategoryItem.getDesignNo(), newDesignNO);
            planningCategoryItem.setDesignNo(newDesignNO);
            BeanUtil.copyProperties(allocationDesignDto, planningCategoryItem);
            if (!StrUtil.equals(planningCategoryItem.getStatus(), BasicNumber.TWO.getNumber())) {
                planningCategoryItem.setOldDesignNo(newDesignNO);
            }
            seatMap.put(planningCategoryItem.getId(), planningCategoryItem);
        }

        QueryWrapper<Style> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(Style::getPlanningCategoryItemId, seatMap.keySet());
        List<Style> styleList = styleService.list(queryWrapper);
        if (CollUtil.isNotEmpty(styleList)) {
            styleList.forEach(s -> {
                s.setDesigner(dtoList.get(0).getDesigner());
                s.setDesignerId(dtoList.get(0).getDesignerId());
            });
            styleService.updateBatchById(styleList);
        }
        styleService.reviseAllDesignNo(designNoUpdate);
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
            SetTaskLevelDto setTaskLevelDto = dtoMap.get(planningCategoryItem.getId());
            planningCategoryItem.setTaskLevel(setTaskLevelDto.getTaskLevel());
            planningCategoryItem.setTaskLevelName(setTaskLevelDto.getTaskLevelName());
        }
        updateBatchById(planningCategoryItems);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean setSeries(List<SetSeriesDto> dtoList) {
        Map<String, SetSeriesDto> dtoMap = dtoList.stream().collect(Collectors.toMap(SetSeriesDto::getId, v -> v, (a, b) -> b));
        List<PlanningCategoryItem> planningCategoryItems = listByIds(dtoMap.keySet());
        if (dtoList.size() != planningCategoryItems.size()) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems){
            SetSeriesDto setSeriesDto = dtoMap.get(planningCategoryItem.getId());
            planningCategoryItem.setSeriesId(setSeriesDto.getSeriesId());
            planningCategoryItem.setSeries(setSeriesDto.getSeries());
        }
        updateBatchById(planningCategoryItems);
        return true;
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
        qw.eq(StrUtil.isNotBlank(dto.getBandName()), "c.band_name", dto.getBandName());
        qw.eq(StrUtil.isNotBlank(dto.getPlanningChannelId()), "c.planning_channel_id", dto.getPlanningChannelId());
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory1st()), "c.prod_category1st", dto.getProdCategory1st());
        // 品类
        qw.in(StrUtil.isNotEmpty(dto.getProdCategory()), "c.prod_category", StrUtil.split(dto.getProdCategory(), CharUtil.COMMA));
        // 设计师
        qw.in(StrUtil.isNotEmpty(dto.getDesignerIds()), "c.designer_id", StringUtils.convertList(dto.getDesignerIds()));
        // 任务等级
        qw.in(StrUtil.isNotEmpty(dto.getTaskLevels()), "c.task_level", StringUtils.convertList(dto.getTaskLevels()));
        // 状态
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "c.status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        //款式状态
        if (StrUtil.isNotBlank(dto.getSdStatus())) {
            // 未开款
            if (StrUtil.equals(BasicNumber.ZERO.getNumber(), dto.getSdStatus())) {
                qw.in("c.status", BasicNumber.ZERO.getNumber(), BasicNumber.ONE.getNumber());
            } else {
                // 已开款、已下发打板
                qw.eq("sd.status", dto.getSdStatus());
            }
        }
        // 特殊需求
        qw.eq(StrUtil.isNotBlank(dto.getSpecialNeedsFlag()), "c.special_needs_flag", dto.getSpecialNeedsFlag());
        Page<PlanningSeasonOverviewVo> objects = PageHelper.startPage(dto);
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PlanningCategoryItem.getK(), "c.");
        getBaseMapper().listSeat(qw);
        PageInfo<PlanningSeasonOverviewVo> pageInfo = objects.toPageInfo();
        List<PlanningSeasonOverviewVo> list = pageInfo.getList();
        //获取设计师信息
        // 设置版师列表
        if (CollUtil.isNotEmpty(list)) {
//            Map<String, List<SampleUserVo>> pdMap = new HashMap<>(16);
//            for (PlanningSeasonOverviewVo tct : list) {
//                String key = tct.getPlanningSeasonId();
//                if (pdMap.containsKey(key)) {
//                    tct.setDesigners(pdMap.get(key));
//                } else {
//                    List<UserCompany> designList = amcFeignService.getTeamUserListByPost(tct.getPlanningSeasonId(), "设计师");
//                    List<SampleUserVo> sampleUserVos = Optional.of(designList).map(dl -> {
//                        return dl.stream().map(item -> {
//                            SampleUserVo su = new SampleUserVo();
//                            su.setUserId(item.getUserId());
//                            su.setAvatar(Optional.ofNullable(item.getAvatar()).orElse(item.getAliasUserAvatar()));
//                            su.setName(item.getAliasUserName());
//                            su.setUserCode(item.getUserCode());
//                            return su;
//                        }).collect(Collectors.toList());
//                    }).orElse(null);
//                    tct.setDesigners(sampleUserVos);
//                    pdMap.put(key, sampleUserVos);
//                }
//            }
            // 素材信息
            List<PlanningCategoryItemMaterial> materialVoList = planningCategoryItemMaterialService.findBySeatIds(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
            if (CollUtil.isNotEmpty(materialVoList)) {
                Map<String, List<PlanningCategoryItemMaterial>> collect = materialVoList.stream().collect(Collectors.groupingBy(PlanningCategoryItemMaterial::getPlanningCategoryItemId));
                for (PlanningSeasonOverviewVo v : list) {
                    v.setMaterialVoList(collect.get(v.getId()));
                }
            }
        }
        return pageInfo;
    }

    public static void main(String[] args) {
      String s =  "http://47.97.254.249:9000/deesha/pdm/2023-09-28/1695884813354.png";


     System.out.println( getImageNameWithoutExtension(s));
    }
    public static String getImageNameWithoutExtension(String imageUrl) {
        int lastIndexOfSlash = imageUrl.lastIndexOf('/');
        int lastIndexOfDot = imageUrl.lastIndexOf('.');
        if (lastIndexOfSlash == -1 || lastIndexOfDot == -1 || lastIndexOfDot < lastIndexOfSlash) {
            return "";
        }
        return imageUrl.substring(lastIndexOfSlash + 1, lastIndexOfDot);
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean send(List<SeatSendDto> categoryItemList) {
        // 1、保存
        List<AllocationDesignDto> allocationDesignDtoList = new ArrayList<>(16);
        List<SetTaskLevelDto> setTaskLevelDtoList = new ArrayList<>(16);
        // 坑位id
        List<String> itemIds = new ArrayList<>(16);
        // 产品季id
        List<String> seasonIds = new ArrayList<>(16);
        //图片信息
        List<String> fileUrls = new ArrayList<>(16);
        for (PlanningCategoryItem planningCategoryItem : categoryItemList) {
            allocationDesignDtoList.add(BeanUtil.copyProperties(planningCategoryItem, AllocationDesignDto.class));
            setTaskLevelDtoList.add(BeanUtil.copyProperties(planningCategoryItem, SetTaskLevelDto.class));
            itemIds.add(planningCategoryItem.getId());
            seasonIds.add(planningCategoryItem.getPlanningSeasonId());
            /*后续再优化*/
            if (StrUtil.isNotBlank(planningCategoryItem.getStylePic())) {
                /*新地址*/
                String newUrl = planningCategoryItem.getStylePic().replaceAll(getImageNameWithoutExtension(planningCategoryItem.getStylePic()), planningCategoryItem.getDesignNo());
                /*改图片名称*/
              boolean b=  uploadFileService.updatePicName(planningCategoryItem.getStylePic(), newUrl);
              if(!b){
                  throw new OtherException("修改图片名称错误");
              }
                /*修改文件名称加上设计师代码*/
                fileUrls.add(newUrl);
//                修改坑位图片 后续优化
                UpdateWrapper updateWrapper =new UpdateWrapper();
                updateWrapper.set("style_pic",newUrl);
                updateWrapper.eq("id",planningCategoryItem.getId());
                baseMapper.update(null,updateWrapper);
            }
        }
        //查询款式信息是已经存在
        QueryWrapper<Style> sqw = new QueryWrapper<>();
        sqw.in("planning_category_item_id", itemIds);
        sqw.eq("del_flag", BaseGlobal.NO);
        long count = styleService.count(sqw);
        if (count > 0) {
            throw new OtherException("存在下发的数据");
        }
        Boolean genDesignNoAction = ccmFeignService.getSwitchByCode("GEN_DESIGN_NO_ACTION");
        if (!genDesignNoAction) {
            // 1.1 分配设计师
            this.allocationDesign(allocationDesignDtoList);
        }
        // 1.2 设置任务等级
        this.setTaskLevel(setTaskLevelDtoList);

        // 2 状态修改为已下发 下发时间
        UpdateWrapper<PlanningCategoryItem> uw = new UpdateWrapper<>();
        Date sendDate = new Date();
        uw.set("status", "2");
        uw.set("send_date", sendDate);
        uw.in("id", itemIds);
        update(uw);
        // 3 将数据写入款式设计
        // 查询已经下发的任务
        QueryWrapper<Style> existsQw = new QueryWrapper<>();
        existsQw.in("planning_category_item_id", itemIds);
        List<Style> dbItemList = styleService.list(existsQw);
        Map<String, Style> dbItemMap = Optional.ofNullable(dbItemList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getPlanningCategoryItemId(), v -> v, (a, b) -> b));
        // 查询产品季
        QueryWrapper<PlanningSeason> seasonQw = new QueryWrapper<>();
        seasonQw.in("id", seasonIds);
        List<PlanningSeason> seasonList = planningSeasonService.list(seasonQw);
        Map<String, PlanningSeason> seasonMap = Optional.ofNullable(seasonList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
        // 图片文件id
        Map<String, String> fileUrlId = uploadFileService.findMapByUrls(fileUrls);

        List<Style> styleList = new ArrayList<>(16);

        for (PlanningCategoryItem item : categoryItemList) {
            if (dbItemMap.containsKey(item.getId())) {
                continue;
            }
            Style style = PlanningUtils.toSampleDesign(seasonMap.get(item.getPlanningSeasonId()), item);
            style.setSender(getUserId());
            style.setStartTime(sendDate);
            style.setEndTime(item.getPlanningFinishDate());
            style.setStylePic(Optional.ofNullable(fileUrlId.get(style.getStylePic())).orElse(""));

            if (genDesignNoAction) {
                style.setDesignNo(null);
            }
            styleList.add(style);


        }
        // 保存款式设计
        if (CollUtil.isNotEmpty(styleList)) {
            styleService.saveBatch(styleList);
            //保存图片附件
            List<Attachment> attachments = new ArrayList<>();
            // 保存维度信息
//            List<FieldVal> fieldVals = new ArrayList<>(16);
//            for (SampleDesign sampleDesign : sampleDesignList) {
//                if (StrUtil.isNotEmpty(sampleDesign.getStylePic())) {
//                    Attachment a = new Attachment();
//                    a.setType(AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
//                    a.setStatus(BaseGlobal.NO);
//                    a.setForeignId(sampleDesign.getId());
//                    a.setFileId(sampleDesign.getStylePic());
//                    attachments.add(a);
//                    List<FieldVal> fvList = fieldValService.list(sampleDesign.getPlanningCategoryItemId(), FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION);
//                    if (CollUtil.isNotEmpty(fvList)) {
//                        fvList.forEach(item -> {
//                            item.setId(null);
//                            item.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
//                            item.setForeignId(sampleDesign.getId());
//                        });
//                        fieldVals.addAll(fvList);
//                    }
//                }
//            }
//            if (CollUtil.isNotEmpty(fieldVals)) {
//                fieldValService.saveBatch(fieldVals);
//            }
            if (CollUtil.isNotEmpty(attachments)) {
                attachmentService.saveBatch(attachments);
            }


        }
        /*产品季下发提醒*/
        messageUtils.seasonSendMessage(BeanUtil.copyToList(categoryItemList,PlanningCategoryItem.class),baseController.getUser());
        return true;
    }

    /**
     * 查询坑位信息的维度数据
     * @param id
     * @param isSelected
     * @param categoryFlag 用于查询品类或中类 0品类1中类
     * @return
     */
    @Override
    public List<FieldManagementVo> querySeatDimension(String id, String isSelected,String categoryFlag) {
        /*查询坑位信息*/
        PlanningCategoryItem seat = getById(id);
        /*产品季信息*/
        PlanningSeason season = planningSeasonService.getById(seat.getPlanningSeasonId());
        /*渠道信息*/
        PlanningChannel planningChannel = planningChannelService.getById(seat.getPlanningChannelId());
        /**
         * 查询标签维度中选中的字段
         */
        List<FieldManagementVo> fieldList = new ArrayList<>();
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        queryWrapper.eq("planning_season_id", seat.getPlanningSeasonId());
        queryWrapper.eq("channel", planningChannel.getChannel());
        seat.setCategoryFlag(categoryFlag);
        /*拼接处理查询条件*/
        PlanningUtils.dimensionCommonQw(queryWrapper, seat);
        List<PlanningDimensionality> dimensionalityList = planningDimensionalityMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(dimensionalityList)) {
            List<String> stringList = dimensionalityList.stream().map(PlanningDimensionality::getFieldId).distinct().collect(Collectors.toList());
            fieldList = BeanUtil.copyToList(fieldManagementService.listByIds(stringList), FieldManagementVo.class);
            if (CollUtil.isNotEmpty(fieldList)) {
                List<String> stringList2 = fieldList.stream().map(FieldManagementVo::getId).collect(Collectors.toList());
                QueryFieldOptionConfigDto queryFieldOptionConfigDto = new QueryFieldOptionConfigDto();
                categoryFlag = Opt.ofBlankAble(seat.getCategoryFlag()).orElse(BaseGlobal.NO);
                if (categoryFlag.equals(BaseGlobal.YES)) {
                    queryFieldOptionConfigDto.setProdCategory2nd(seat.getProdCategory2nd());
                } else {
                    queryFieldOptionConfigDto.setCategoryCode(seat.getProdCategory());
                }
                /*查询每个字段下的配置选项*/
                queryFieldOptionConfigDto.setBrand(season.getBrand());
                queryFieldOptionConfigDto.setSeason(season.getSeason());
                queryFieldOptionConfigDto.setFieldManagementIdList(stringList2);
                Map<String, List<FieldOptionConfig>> listMap = fieldOptionConfigService.getFieldConfig(queryFieldOptionConfigDto);

                fieldList.forEach(f -> {
                    f.setFieldId(f.getId());
                    List<FieldOptionConfig> list = listMap.get(f.getId());
                    if (CollUtil.isNotEmpty(list)) {
                        f.setConfigVoList(BeanUtil.copyToList(list, FieldOptionConfigVo.class));
                    }
                });
            }
        }
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
        update(uw);
        return true;
    }

    @Override
    public boolean updateItemBatch(PlanningCategoryItemBatchUpdateDto dto) {
        List<PlanningCategoryItem> planningCategoryItems = listByIds(dto.getIds());
        if (dto.getIds().size() != planningCategoryItems.size()) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {
            if (StrUtil.isNotBlank(dto.getDesigner())) {
                String newDesignNO = PlanningUtils.getNewDesignNo(planningCategoryItem.getDesignNo(), planningCategoryItem.getDesigner(), dto.getDesigner());
                planningCategoryItem.setDesignNo(newDesignNO);
            }
            BeanUtil.copyProperties(dto, planningCategoryItem);
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
    public List<ChartBarVo> bandSummary(QueryWrapper qw) {
        return getBaseMapper().bandSummary(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean seatSend(List<PlanningCategoryItemSaveDto> list) {

        List<String> seatIds = new ArrayList<>(2);
        for (PlanningCategoryItem planningCategoryItem : list) {
            seatIds.add(planningCategoryItem.getId());

        }
        Date sendDate = new Date();
        UpdateWrapper<PlanningCategoryItem> seatUw = new UpdateWrapper<>();
        seatUw.set("status", BasicNumber.ONE.getNumber());
        seatUw.set("send_date", sendDate);
        seatUw.in("status", BasicNumber.ZERO.getNumber(), "-1");
        seatUw.in("id", seatIds);
        update(seatUw);
        /*发送消息*/
        messageUtils.seatSendMessage(seatIds,"",baseController.getUser());
        return true;
    }

    @Override
    public Map<String, Long> totalSkcByPlanningSeason(List<String> planningSeasonIds) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in(CollUtil.isNotEmpty(planningSeasonIds), "planning_season_id", planningSeasonIds);
        Map<String, Long> result = new HashMap<>(16);
        List<CountVo> list = getBaseMapper().totalSkcByPlanningSeason(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (CountVo countVo : list) {
                result.put(countVo.getLabel(), countVo.getCount());
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
        List<CountVo> list = getBaseMapper().totalSkcByChannel(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (CountVo countVo : list) {
                result.put(countVo.getLabel(), countVo.getCount());
            }
        }
        return result;
    }

    @Override
    public Map<String, Long> totalBandSkcByPlanningSeason(String planningSeasonId) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq("planning_season_id", planningSeasonId);
        qw.isNotNull("band_name");
        qw.ne("band_name", "");
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PlanningSeason.getK());
        Map<String, Long> result = new HashMap<>(16);
        List<CountVo> list = getBaseMapper().totalBandSkcByPlanningSeason(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (CountVo countVo : list) {
                result.put(countVo.getLabel(), countVo.getCount());
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateByChannelChange(PlanningChannel channel) {
        PlanningCategoryItem item = new PlanningCategoryItem();
        item.setChannel(channel.getChannel());
        item.setChannelName(channel.getChannelName());
        item.setSex(channel.getSex());
        item.setSexName(channel.getSexName());
        UpdateWrapper<PlanningCategoryItem> uw = new UpdateWrapper<>();
        uw.eq("planning_channel_id", channel.getId());
        update(item, uw);
    }

    @Override
    public String getStylePicUrlById(String id) {
        return getBaseMapper().getStylePicUrlById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult importPlanningExcel(MultipartFile file, String planningChannelId) throws Exception {
        //查询渠道信息
        PlanningChannel channel = planningChannelService.getById(planningChannelId);
        if (channel == null) {
            throw new OtherException("渠道信息为空");
        }

        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<PlanningCategoryItemImportDto> list = ExcelImportUtil.importExcel(file.getInputStream(), PlanningCategoryItemImportDto.class, params);

        //大类
        List<BasicStructureTree> bigList = ccmFeignService.appointNextLevelList("品类", "0");
        Map<String, BasicStructureTree> bigMap = bigList.stream().collect(Collectors.toMap(BasicStructureTree::getId, item -> item));
        //品类
        List<BasicStructureTree> categoryList = ccmFeignService.appointNextLevelList("品类", "1");
        Map<String, BasicStructureTree> categoryMap = new HashMap<>();
        Map<String, BasicStructureTree> categoryNameMap = new HashMap<>();
        for(BasicStructureTree item : categoryList){
            categoryMap.put(item.getId(), item);

            BasicStructureTree basicStructureTree = categoryNameMap.get(item.getName());
            if(basicStructureTree == null){
                categoryNameMap.put(item.getName(), item);
            }
        }
        //中类
        List<BasicStructureTree> centerList = ccmFeignService.appointNextLevelList("品类", "2");
        Map<String, List<BasicStructureTree>> centreMap = new HashMap<>();
        Map<String, BasicStructureTree> centreNameMap = new HashMap<>();
        for(BasicStructureTree item : centerList){
            List<BasicStructureTree> currentList = centreMap.get(item.getParentId());
            if(currentList == null){
                currentList = new ArrayList<>();
            }
            currentList.add(item);
            centreMap.put(item.getParentId(), currentList);

            BasicStructureTree basicStructureTree = centreNameMap.get(item.getName());
            if(basicStructureTree == null){
                centreNameMap.put(item.getName(), item);
            }
        }
        //小类
        List<BasicStructureTree> smallList = ccmFeignService.appointNextLevelList("品类", "3");
        Map<String, List<BasicStructureTree>> smallMap = smallList.stream().collect(Collectors.groupingBy(BasicStructureTree::getParentId));

        Map<String, Map<String, Map<String, BasicStructureTree>>> treeMap = new HashMap<>();
        for(Map.Entry<String, BasicStructureTree> category : categoryMap.entrySet()){
            List<BasicStructureTree> currentCenterList = centreMap.get(category.getKey());
            if(CollectionUtil.isNotEmpty(currentCenterList)){
                Map<String, Map<String, BasicStructureTree>> centerTemporaryMap = new HashMap<>();
                for(BasicStructureTree center : currentCenterList){
                    List<BasicStructureTree> currentSmallList = smallMap.get(center.getId());
                    if(CollectionUtil.isNotEmpty(currentSmallList)){
                        Map<String, BasicStructureTree> currentSmallMap = currentSmallList.stream().collect(Collectors.toMap(BasicStructureTree::getName, item -> item, (item1, item2) -> item1));
                        centerTemporaryMap.put(center.getName(), currentSmallMap);
                    }
                }
                treeMap.put(category.getValue().getName(), centerTemporaryMap);
            }
        }

        List<PlanningCategoryItem> addList = new ArrayList<>();
        List<Integer> dataCompleteErrorList = new ArrayList<>();
        List<Integer> dataCorrectErrorList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            PlanningCategoryItemImportDto itemImportDto = list.get(i);
            if(StringUtils.isBlank(itemImportDto.getProdCategory()) || StringUtils.isBlank(itemImportDto.getProdCategory2nd()) || StringUtils.isBlank(itemImportDto.getProdCategory3rd())){
                dataCompleteErrorList.add(i+1);
            }

            Map<String, Map<String, BasicStructureTree>> currentCenterMap = treeMap.get(itemImportDto.getProdCategory());
            if(CollectionUtil.isNotEmpty(currentCenterMap)){
                BasicStructureTree prodCategory = categoryNameMap.get(itemImportDto.getProdCategory());
                BasicStructureTree prodCategory1st = bigMap.get(prodCategory.getParentId());

                Map<String, BasicStructureTree> currentSmallMap = currentCenterMap.get(itemImportDto.getProdCategory2nd());
                if(CollectionUtil.isNotEmpty(currentSmallMap)){
                    BasicStructureTree prodCategory2st = centreNameMap.get(itemImportDto.getProdCategory2nd());
                    BasicStructureTree prodCategory3st = currentSmallMap.get(itemImportDto.getProdCategory3rd());
                    if(prodCategory3st != null){
                        PlanningCategoryItem categoryItem = new PlanningCategoryItem();
                        BeanUtil.copyProperties(channel, categoryItem);

                        categoryItem.setPlanningChannelId(planningChannelId);
                        categoryItem.setProdCategory1st(prodCategory1st.getValue());
                        categoryItem.setProdCategory1stName(prodCategory1st.getName());
                        categoryItem.setProdCategory(prodCategory.getValue());
                        categoryItem.setProdCategoryName(prodCategory.getName());
                        categoryItem.setProdCategory2nd(prodCategory2st.getValue());
                        categoryItem.setProdCategory2ndName(prodCategory2st.getName());
                        categoryItem.setProdCategory3rd(prodCategory3st.getValue());
                        categoryItem.setProdCategory3rdName(prodCategory3st.getName());

                        categoryItem.setImportantStyleFlag(StringUtils.equals(itemImportDto.getImportantStyleFlag(), "是") ? "1" : "0");
                        categoryItem.setSpecialNeedsFlag(StringUtils.equals(itemImportDto.getSpecialNeedsFlag(), "是") ? "1" : "0");

                        categoryItem.setMonth(itemImportDto.getMonth());
                        categoryItem.setMonthName(itemImportDto.getMonth());
                        categoryItem.setBandCode(itemImportDto.getBand());
                        categoryItem.setBandName(itemImportDto.getBand());

                        categoryItem.setRemarks(itemImportDto.getRemarks());
                        categoryItem.setStyleType(itemImportDto.getStyleType());
                        categoryItem.setLevelFourType(itemImportDto.getLevelFourType());
                        categoryItem.setHisDesignNo(itemImportDto.getOldDesignNo());
                        categoryItem.setTargetSalePrice(itemImportDto.getTargetSalePrice());
                        categoryItem.setTargetSalePriceInterval(itemImportDto.getTargetSalePriceInterval());
                        categoryItem.setPlanningTargetRate(itemImportDto.getPlanningTargetRate());
                        categoryItem.setProductCost(itemImportDto.getProductCost());
                        addList.add(categoryItem);
                    }else{
                        dataCorrectErrorList.add(i+1);
                    }
                }else{
                    dataCorrectErrorList.add(i+1);
                }
            }else{
                dataCorrectErrorList.add(i+1);
            }
        }

        if(CollectionUtil.isNotEmpty(dataCompleteErrorList)){
            String rowInfo = dataCompleteErrorList.stream().map(String::valueOf).collect(Collectors.joining(","));
            return ApiResult.error("第" + rowInfo + "行数据的品类，请填写完整！", 500);
        }

        if(CollectionUtil.isNotEmpty(dataCorrectErrorList)){
            String rowInfo = dataCorrectErrorList.stream().map(String::valueOf).collect(Collectors.joining(","));
            return ApiResult.error("第" + rowInfo + "行数据的品类与系统数据不匹配！", 500);
        }

        //获取设计编号
        List<String> designCodeList = getNextCode(addList.get(0), addList.size());
        for(int i = 0; i < addList.size(); i++){
            PlanningCategoryItem item = addList.get(i);
            item.setId(null);
            item.setDesignNo(designCodeList.get(i));
            CommonUtils.resetCreateUpdate(item);
        }

        Boolean result = saveBatch(addList);
        if(result){
            return ApiResult.success("导入成功！", result);
        }
        return ApiResult.error("导入成功！", 500);
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
        List<String> nextCode = getNextCode(base, dto.getCount());
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
        // 查询款式设计数据
        QueryWrapper<Style> sdQw = new QueryWrapper();
        List<String> seatIds = StrUtil.split(ids, CharUtil.COMMA);
        sdQw.in("planning_category_item_id", seatIds);
        List<Style> sdList = styleService.list(sdQw);
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
        styleService.remove(sdQw);
        return true;
    }

    @Override
    public boolean del(String ids) {
        // 查询款式设计数据
        QueryWrapper<Style> sdQw = new QueryWrapper();
        List<String> seatIds = StrUtil.split(ids, CharUtil.COMMA);
        sdQw.in("planning_category_item_id", seatIds);
        List<Style> sdList = styleService.list(sdQw);
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
        styleService.remove(sdQw);
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

    @Override
    public List<String> selectCategoryIdsByBand(QueryWrapper qw) {
        return getBaseMapper().selectCategoryIdsByBand(qw);
    }

    @Override
    public List<BasicStructureTreeVo> expandByCategory(ProductSeasonExpandByCategorySearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qw.eq("planning_season_id", dto.getPlanningSeasonId());
        List<String> categoryIds = selectCategoryIdsByBand(qw);
        if (CollUtil.isEmpty(categoryIds)) {
            return null;
        }
        return ccmFeignService.findStructureTreeByCodes(CollUtil.join(categoryIds, ","));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateBySampleDesignChange(Style style) {
        PlanningCategoryItem byId = getById(style.getPlanningCategoryItemId());
        BeanUtil.copyProperties(style, byId, "createId", "createName", "id", "status", "stylePic");
        updateById(byId);
    }

}
