/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTree;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.*;
import com.base.sbc.config.utils.StringUtils.MatchStrType;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.service.ColumnUserDefineService;
import com.base.sbc.module.common.dto.DelStylePicDto;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.pack.dto.PackBomPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningDimensionalityMapper;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningDimensionalityVo;
import com.base.sbc.module.planningproject.constants.GeneralConstant;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.report.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.smp.DataUpdateScmService;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.PdmStyleCheckParam;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.entity.StyleColorAgent;
import com.base.sbc.module.style.entity.StyleMainAccessories;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.*;
import com.base.sbc.module.style.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 类描述：样衣-款式配色 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleStyleColorService
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Service
@RequiredArgsConstructor
@EnableAsync
public class StyleColorServiceImpl<pricingTemplateService> extends BaseServiceImpl<StyleColorMapper, StyleColor> implements StyleColorService {

    Logger log = LoggerFactory.getLogger(getClass());

    private final BaseController baseController;

    private final BasicsdatumColourLibraryService basicsdatumColourLibraryServicel;

    private final UserUtils userUtils;

    private final DataUpdateScmService dataUpdateScmService;

    private final StylePricingMapper stylePricingMapper;

    private final PackBomVersionService packBomVersionService;

    @Resource
    @Lazy
    private PlanningProjectPlankService planningProjectPlankService;


    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private StylePicService stylePicService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private StyleColorService styleColorService;
    @Autowired
    private CustomStylePicUpload customStylePicUpload;

    private final FieldManagementService fieldManagementService;

    @Lazy
    @Resource
    private SmpService smpService;

    private final PackInfoService packInfoService;

    private final PackInfoMapper packInfoMapper;
    @Lazy
    @Resource
    private final StyleService styleService;
    private final PackInfoStatusService packInfoStatusService;
    private final CcmFeignService ccmFeignService;

    private final FieldValService fieldValService;

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private PackPricingService packPricingService;

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private StyleMainAccessoriesService styleMainAccessoriesService;

    @Lazy
    @Autowired
    private HangTagService hangTagService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private  PlanningSeasonService planningSeasonService;
       @Resource
    @Lazy
    private OrderBookDetailService orderBookDetailService;
    @Autowired
    private BasicsdatumModelTypeService basicsdatumModelTypeService;

    @Autowired
    private StyleColorAgentService styleColorAgentService;

    @Autowired
    private StylePricingService stylePricingService;

    @Autowired
    private BasicsdatumSizeService basicsdatumSizeService;

    @Autowired
    private BasicsdatumColourLibraryAgentService colourLibraryAgentService;

    @Autowired
    private BasicsdatumWashIconService basicsdatumWashIconService;

    @Autowired
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private PlanningDimensionalityMapper planningDimensionalityMapper;


    @Autowired
    private PackPricingCraftCostsService packPricingCraftCostsService;

    @Autowired
    private PackPricingProcessCostsService packPricingProcessCostsService;
    Pattern pattern = Pattern.compile("[a-z||A-Z]");


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 样衣-款式配色分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    //@EditPermission(type = DataPermissionsBusinessTypeEnum.styleColor)
    public PageInfo<StyleColorVo> getSampleStyleColorList(Principal user, QueryStyleColorDto queryDto) {

        /*分页*/
        BaseQueryWrapper queryWrapper = getBaseQueryWrapper(queryDto);
        if((MapUtils.isNotEmpty(queryDto.getFieldQueryMap()) && queryDto.getFieldQueryMap().containsKey("styleNo") && ("styleNo".equals(queryDto.getFieldQueryMap().get("styleNo")) || "styleNo".equals(queryDto.getColumnHeard())))
         || (MapUtils.isNotEmpty(queryDto.getFieldQueryMap()) && queryDto.getFieldQueryMap().containsKey("designNo") && ("designNo".equals(queryDto.getFieldQueryMap().get("designNo")) || "designNo".equals(queryDto.getColumnHeard())))
        ){
            queryWrapper.orderByDesc("CAST(ts.year AS SIGNED)");
            queryWrapper.orderByDesc("ts.season");
            queryWrapper.orderByDesc("ts.brand");
        }
        QueryGenerator.initQueryWrapperByMapNoDataPermission(queryWrapper,queryDto);

        //添加数据权限，根据前端传值
        //打版进度	patternMakingSteps
        //款式配色	styleColor
        //款式列表	stylePage
        //款式库	    styleLibrary
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, queryDto.getBusinessType(), "ts.");
        Page<Object> objects = PageHelper.startPage(queryDto);

        if(StrUtil.equals("colorBatch",queryDto.getBusinessType())){
            //只查询已发送的
            queryWrapper.ne("tsc.scm_send_flag","0");
            //只查询已经有关联Bom的
            queryWrapper.isNotNullStr("tsc.bom");
            queryWrapper.notEmptyIn("tsc.style_no",queryDto.getStyleNo());
        }


        /*获取配色数据*/
        List<StyleColorVo> sampleStyleColorList = new ArrayList<>();
        if (StringUtils.isNotBlank(queryDto.getColorListFlag())) {
            queryWrapper.eq("tsc.del_flag", "0");
            // bug 3325
            objects.setOrderBy("CASE tsc.scm_send_flag " +
                    " WHEN 0 THEN 0 " +
                    " WHEN 3 THEN 1 " +
                    " WHEN 2 THEN 2 " +
                    " WHEN 1 THEN 3 " +
                    " ELSE 99 " +
                    "END " +
                    ",tsc.create_date asc");
            //queryWrapper.orderByAsc("tsc.scm_send_flag asc ");
//            查询配色列表
            sampleStyleColorList = baseMapper.colorList(queryWrapper);
            if( StrUtil.equals(queryDto.getExcelFlag(),BaseGlobal.YES) ){
                return new PageInfo<>(sampleStyleColorList);
            }
        } else {
            queryWrapper.eq("ts.del_flag", "0");
            // 2474
            queryWrapper.orderByDesc("CAST(ts.year AS SIGNED)");
            queryWrapper.orderByDesc("ts.create_date");
//            queryWrapper.orderByDesc("ts.id");
//            查询款式配色
            sampleStyleColorList = baseMapper.styleColorList(queryWrapper);
            if( StrUtil.equals(queryDto.getExcelFlag(),BaseGlobal.YES) ){
                return new PageInfo<>(sampleStyleColorList);
            }
            List<String> stringList = IdGen.getIds(sampleStyleColorList.size());
            int index = 0;
            for (StyleColorVo styleColorVo : sampleStyleColorList) {
                if (stringList != null) {
                    styleColorVo.setIssuerId(stringList.get(index));
                }
                index++;
            }
        }
        /*查询款式图*/
        stylePicUtils.setStylePic(sampleStyleColorList, "stylePic");

        if (user != null) {
            /*查询款式配色图*/
            GroupUser userBy = userUtils.getUserBy(user);
            stylePicUtils.setStyleColorPic2(sampleStyleColorList, "styleColorPic");
        }

        return new PageInfo<>(sampleStyleColorList);
    }

    @Override
    public PageInfo<CompleteStyleVo> getCompleteStyleVoList(QueryBulkCargoDto queryDto) {
        /*分页*/
        BaseQueryWrapper<StyleAnalyseQueryDto> queryWrapper = new BaseQueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.bulkCargoStyle.getK());

        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(queryWrapper, queryDto);
        Page<Object> objects = PageHelper.startPage(queryDto);
        List<String> styleNos = new ArrayList<>();
        if (StrUtil.isNotBlank(queryDto.getStyleNo())) {
            styleNos = Arrays.asList(queryDto.getStyleNo().split(","));
        }
        String yearParam = queryDto.getYear();
        String seasonParam = queryDto.getSeason();
        queryWrapper.notEmptyIn("tsc.style_no", styleNos);
        queryWrapper.notEmptyEq("ts.year", yearParam);
        queryWrapper.notEmptyEq("ts.season", seasonParam);
        QueryGenerator.reportParamBulkStyleNosCheck(styleNos, yearParam, seasonParam);
        queryWrapper.eq("tsc.del_flag", "0");
        //查询配色列表
        List<CompleteStyleVo> completeStyleVoList = baseMapper.pageCompleteStyle(queryWrapper);
        if( StrUtil.equals(queryDto.getExcelFlag(),BaseGlobal.YES) ){
            return new PageInfo<>(completeStyleVoList);
        }

        if (CollUtil.isNotEmpty(completeStyleVoList)) {
            //根据查询出维度系数数据
            LambdaQueryWrapper<FieldVal> fieldValQueryWrapper = new LambdaQueryWrapper<>();
            List<String> styleColorIdList = completeStyleVoList.stream().map(CompleteStyleVo::getId).distinct().collect(Collectors.toList());
            fieldValQueryWrapper.in(FieldVal::getForeignId, styleColorIdList);
            fieldValQueryWrapper.eq(FieldVal::getDataGroup, FieldValDataGroupConstant.STYLE_MARKING_ORDER);
            List<FieldVal> fieldValList = fieldValService.list(fieldValQueryWrapper);
            Map<String, Map<String, String>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId, Collectors.toMap(FieldVal::getFieldName, o -> StrUtil.isEmpty(o.getValName()) ? StrUtil.isEmpty(o.getVal()) ? "" : o.getVal() : o.getValName(), (v1, v2) -> v1)));
            for (CompleteStyleVo styleVo : completeStyleVoList) {
                if (fieldValMap.containsKey(styleVo.getId())) {
                    Map<String, String> map = fieldValMap.get(styleVo.getId());
                    styleVo.setFieldValMap(map);
                }
            }
        }

        /*查询款式图*/
        stylePicUtils.setStylePic(completeStyleVoList, "stylePic");
        stylePicUtils.setStyleColorPic2(completeStyleVoList, "styleColorPic");

        dataProcessing(completeStyleVoList, super.getCompanyCode());
        return new PageInfo<>(completeStyleVoList);
    }

    @Override
    public void getStyleColorListExport(HttpServletResponse response, QueryBulkCargoDto queryDto) throws IOException {
        queryDto.setPageNum(0);
        queryDto.setPageSize(0);
        List<CompleteStyleVo> list = getCompleteStyleVoList(queryDto).getList();
        ExcelUtils.exportExcelByTableCode(list, "大货款列表", response, queryDto);
    }

    /**
     * 数据组装处理
     *
     * @param styleList
     * @param companyCode
     */
    public void dataProcessing(List<CompleteStyleVo> styleList, String companyCode) {
        List<String> packId = styleList.stream()
                .map(CompleteStyleVo::getPackInfoId)
                .collect(Collectors.toList());
        String packType = PackUtils.PACK_TYPE_BIG_GOODS;
        Map<String, BigDecimal> otherCostsMap = this.getOtherCosts(packId, companyCode,packType);
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .build();

        try {
            CountDownLatch countDownLatch = new CountDownLatch(styleList.size());
            for (CompleteStyleVo styleVO : styleList) {
                executor.submit(() -> {
                    PackCommonSearchDto packCommonSearchDto = new PackCommonSearchDto();
                    packCommonSearchDto.setPackType(packType);

                    packCommonSearchDto.setForeignId(styleVO.getPackInfoId());
                    //材料成本,如果fob,则不计算
                    if ("CMT".equals(styleVO.getDevtTypeName())) {
                        styleVO.setMaterialCost(packBomService.calculateCosts(packCommonSearchDto));
                    } else {
                        styleVO.setMaterialCost(BigDecimal.ZERO);
                    }
                    styleVO.setPackagingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "包装费")));
                    styleVO.setTestingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "检测费")));
                    styleVO.setSewingProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "车缝加工费")));
                    styleVO.setWoolenYarnProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "毛纱加工费")));
                    BigDecimal coordinationProcessingFee = new BigDecimal(0);
                    coordinationProcessingFee = coordinationProcessingFee.add(
                                    BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "外协其他"))).
                            add(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "外协印花"))).
                            add(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "外协绣花"))).
                            add(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(styleVO.getPackInfoId() + "外协压皱")));

                    styleVO.setCoordinationProcessingFee(coordinationProcessingFee);

                    //加工费
                    List<PackPricingProcessCosts> processCostsList = packPricingProcessCostsService.list(new QueryWrapper<PackPricingProcessCosts>().eq("foreign_id", styleVO.getPackInfoId()).eq("pack_type", "packBigGoods"));
                    if (!processCostsList.isEmpty()) {
                        try {
                            processCostsList.stream()
                                    .map(costs -> costs.getProcessPrice().multiply(costs.getMultiple()))
                                    .reduce(BigDecimal::add)
                                    .ifPresent(styleVO::setProcessingFee);
                        } catch (Exception e) {
                            log.error("StylePricingServiceImpl#dataProcessing 加工费计算异常", e);
                        }

                    }
                    //二次加工费用
                    List<PackPricingCraftCosts> pricingCraftCostsList = packPricingCraftCostsService.list(new QueryWrapper<PackPricingCraftCosts>().eq("foreign_id", styleVO.getPackInfoId()).eq("pack_type", "packBigGoods"));
                    if (!pricingCraftCostsList.isEmpty()) {
                        try {
                            pricingCraftCostsList.stream()
                                    .map(costs -> costs.getPrice().multiply(costs.getNum()))
                                    .reduce(BigDecimal::add)
                                    .ifPresent(styleVO::setSecondaryProcessingFee);
                        } catch (Exception e) {
                            log.error("StylePricingServiceImpl#dataProcessing 二次加工费用计算异常", e);
                        }
                    }


                    styleVO.setTotalCost(BigDecimalUtil.add(styleVO.getMaterialCost(), styleVO.getPackagingFee(),
                            styleVO.getTestingFee(), styleVO.getSewingProcessingFee(), styleVO.getWoolenYarnProcessingFee(),
                            styleVO.getCoordinationProcessingFee(), styleVO.getSecondaryProcessingFee(), styleVO.getProcessingFee()));
                    styleVO.setTotalCost(styleVO.getTotalCost().setScale(3, RoundingMode.HALF_UP));
                    BigDecimal taxRate = BigDecimal.ONE;

                    if ("CMT".equals(styleVO.getDevtTypeName())) {
                        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(styleVO.getCalcItemVal());
                        if (jsonObject != null) {
                            taxRate = jsonObject.getBigDecimal("税率");

                        }
                        if (styleVO.getTotalCost() != null && taxRate != null) {
                            styleVO.setTotalCost(styleVO.getTotalCost().multiply(taxRate).setScale(3, RoundingMode.HALF_UP));
                        }
                    }

                    /*优先展示手数的数据*/
                    if(styleVO.getControlPlanCost() != null){
                        styleVO.setPlanCost((styleVO.getControlPlanCost()));
                    }else {
                        //目前逻辑修改为取计控实际成本取总成本
                        styleVO.setPlanCost(styleVO.getTotalCost());
                    }
                    //计控实际倍率 = 吊牌价/计控实际成本
                    //stylePricingVO.setPlanActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getPlanCost(), 2));
                    //实际倍率 = 吊牌价/总成本
                    styleVO.setActualMagnification(BigDecimalUtil.div(styleVO.getTagPrice(), styleVO.getTotalCost(), 2));

                    //每次减一
                    countDownLatch.countDown();
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
        stylePicUtils.setStylePic(styleList, "sampleDesignPic");
    }

    /**
     * 获取其他费用
     *
     * @param packId
     * @param companyCode
     * @return
     */
    private Map<String, BigDecimal> getOtherCosts(List<String> packId, String companyCode, String packType) {
        List<PackPricingOtherCosts> packPricingOtherCosts = packPricingOtherCostsService.getPriceSumByForeignIds(packId, companyCode,packType);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(packPricingOtherCosts)) {
            return new HashMap<>();
        }
        return packPricingOtherCosts.stream()
                .filter(x -> Objects.nonNull(x.getPrice()))
                .collect(Collectors.toMap(e -> e.getForeignId() + e.getCostsType(), PackPricingOtherCosts::getPrice,(k1, k2) -> k1));

    }

    @Override
    public ApiResult getStyleColorBystyleNo(String styleNo) {
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        queryWrapper.eq("tsc.style_no", styleNo);
        queryWrapper.eq("tsc.del_flag", "0");
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.bulkCargoStyle.getK());
        List<CompleteStyleVo> completeStyleVos = baseMapper.pageCompleteStyle(queryWrapper);

        /*查询款式图*/
        stylePicUtils.setStyleColorPic2(completeStyleVos, "styleColorPic");
        if (CollUtil.isNotEmpty(completeStyleVos)) {
            dataProcessing(completeStyleVos, super.getCompanyCode());
            CompleteStyleVo detail = completeStyleVos.get(0);
            List<BasicsdatumModelType> basicsdatumModelTypeList = basicsdatumModelTypeService.queryByCode(detail.getStyleCompanyCode(), detail.getSizeRange());
            if (CollUtil.isNotEmpty(basicsdatumModelTypeList)) {
                BasicsdatumModelType modelType = basicsdatumModelTypeList.get(0);
                detail.setSizeRangeSizes(modelType.getSize());
                detail.setSizeRangeSizeIds(modelType.getSizeIds());
                detail.setSizeRangeSizeCodes(modelType.getSizeCode());
                detail.setSizeRangeSizeRealCodes(modelType.getSizeRealCode());
                // 款式图片
                if (customStylePicUpload.isOpen()) {
                    List<StylePicVo> stylePicVos = stylePicService.listByStyleId(detail.getStyleId());
                    detail.setStylePicList(stylePicVos);
                } else {
                    List<AttachmentVo> stylePicList = attachmentService.findByforeignId(detail.getStyleId(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
                    detail.setStylePicList(BeanUtil.copyToList(stylePicList, StylePicVo.class));
                }
            }
            return ApiResult.success("查询成功", detail);
        }
        return ApiResult.success("查询成功", null);
    }

    @NotNull
    private BaseQueryWrapper getBaseQueryWrapper(QueryStyleColorDto queryDto) {
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleId()), "tsc.style_id", queryDto.getStyleId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getIsTrim()), "tsc.is_trim", queryDto.getIsTrim());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getColorSpecification()), "tsc.color_specification", queryDto.getColorSpecification());
        if(StringUtils.isNotBlank(queryDto.getStyleNo())){
            queryWrapper.likeList("tsc.style_no",StringUtils.convertList(queryDto.getStyleNo()));
        }
        if(StringUtils.isNotBlank(queryDto.getDesignNo())){
            queryWrapper.likeList("ts.design_no",StringUtils.convertList(queryDto.getDesignNo()));
        }
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getColorName()), "tsc.color_name", queryDto.getColorName());
        queryWrapper.notEmptyLike("tsc.scm_send_flag", queryDto.getScmSendFlag());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getColorCode()), "tsc.color_code", queryDto.getColorCode());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getMeetFlag()), "tsc.meet_flag", queryDto.getMeetFlag());
        queryWrapper.notEmptyEqOrIsNull("ts.prod_category_name", queryDto.getProdCategoryName());
        queryWrapper.notEmptyEqOrIsNull("ts.prod_category", queryDto.getProdCategory());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSubdivide()), "tsc.subdivide", queryDto.getSubdivide());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getCategoryName()), "ts.prod_category_name", queryDto.getCategoryName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getTaskLevelName()), "ts.task_level_name", queryDto.getTaskLevelName());
        queryWrapper.notEmptyEqOrIsNull("ts.devt_type_name", queryDto.getDevtTypeName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDesignerId()), "ts.designer_id", queryDto.getDesignerId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getTechnicianId()), "ts.technician_id", queryDto.getTechnicianId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getPlanningSeasonId()), "ts.planning_season_id", queryDto.getPlanningSeasonId());
        queryWrapper.notEmptyEqOrIsNull("ts.prod_category1st", queryDto.getProdCategory1st());
        queryWrapper.in(StringUtils.isNotBlank(queryDto.getStyleStatus()), "ts.status", StringUtils.convertList(queryDto.getStyleStatus()));
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleTypeName()), "ts.style_type_name", queryDto.getStyleTypeName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getHisDesignNo()), "ts.his_design_no", queryDto.getHisDesignNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSizeRangeName()), "ts.size_range_name", queryDto.getSizeRangeName());
        queryWrapper.notEmptyEqOrIsNull("ts.band_code", queryDto.getBandCode());
        queryWrapper.notEmptyEqOrIsNull("ts.brand_name", queryDto.getBrandName());
        queryWrapper.notEmptyEqOrIsNull("ts.brand", queryDto.getBrand());
        queryWrapper.notEmptyLikeOrIsNull("tsc.band_name", queryDto.getBandName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getDesigner()), "ts.designer", queryDto.getDesigner());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getTechnicianName()), "ts.technician_name", queryDto.getTechnicianName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStatus()), "tsc.status", queryDto.getStatus());
        queryWrapper.notEmptyLike("ts.prod_category3rd_name", queryDto.getProdCategory3ndName());
        queryWrapper.notEmptyLike("ts.prod_category2nd_name", queryDto.getProdCategory2ndName());
        queryWrapper.notEmptyLikeOrIsNull("ts.prod_category1st_name", queryDto.getProdCategory1stName());
        queryWrapper.notEmptyLikeOrIsNull("ts.season_name", queryDto.getSeasonName());
        queryWrapper.notEmptyLikeOrIsNull("ts.year_name", queryDto.getYearName());
        queryWrapper.notEmptyLike("tppst.technologist_name", queryDto.getTechnologistName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProdCategory3nd()), "ts.prod_category3rd", queryDto.getProdCategory3nd());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProdCategory2nd()), "ts.prod_category2nd", queryDto.getProdCategory2nd());

        queryWrapper.between("tsc.create_date", queryDto.getCreateDate());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCreateName()), "tsc.create_name", queryDto.getCreateName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getNewDate()), "tsc.new_date", queryDto.getNewDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getIsLuxury()), "tsc.is_luxury", queryDto.getIsLuxury());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSubdivideName()), "tsc.subdivide_name", queryDto.getSubdivideName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendMainFabricDate()), "tsc.send_main_fabric_date", queryDto.getSendMainFabricDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate1()), "tsc.send_batching_date1", queryDto.getSendBatchingDate1());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate2()), "tsc.send_batching_date2", queryDto.getSendBatchingDate2());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate3()), "tsc.send_batching_date3", queryDto.getSendBatchingDate3());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendSingleDate()), "tsc.send_single_date", queryDto.getSendSingleDate());
        if (StringUtils.isNotBlank(queryDto.getDesignDetailDate())) {
            queryWrapper.between("tsc.design_detail_date", queryDto.getDesignDetailDate().split(","));
        }
        if (StringUtils.isNotBlank(queryDto.getDesignCorrectDate())) {
            queryWrapper.between("tsc.design_correct_date", queryDto.getDesignCorrectDate().split(","));
        }
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDesignCorrectDate()), "tsc.design_correct_date", queryDto.getDesignCorrectDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProductSubdivideName()), "tsc.product_subdivide_name", queryDto.getProductSubdivideName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getPrincipalStyle()), "tsc.principal_style", queryDto.getPrincipalStyle());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getPrincipalStyleNo()), "tsc.principal_style_no", queryDto.getPrincipalStyleNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getAccessory()), "tsc.accessory", queryDto.getAccessory());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getAccessoryNo()), "tsc.accessory_no", queryDto.getAccessoryNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSenderDesignerName()), "tsc.sender_designer_name", queryDto.getSenderDesignerName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getMerchDesignName()), "ts.merch_design_name", queryDto.getMerchDesignName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getBom()), "tsc.bom", queryDto.getBom());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getBomStatus()), "tsc.bom_status", queryDto.getBomStatus());
        queryWrapper.notEmptyEqOrIsNull("ts.sales_type_name", queryDto.getSalesTypeName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getTagPrice()), "tsc.tag_price", queryDto.getTagPrice());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleFlavourName()), "ts.style_flavour_name", queryDto.getStyleFlavourName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplier()), "tsc.supplier", queryDto.getSupplier());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplier()), "tsc.supplier", queryDto.getSupplier());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplier()), "tsc.supplier", queryDto.getSupplier());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplierColor()), "tsc.supplier_color", queryDto.getSupplierColor());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplierAbbreviation()), "tsc.supplier_abbreviation", queryDto.getSupplierAbbreviation());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplierNo()), "tsc.supplier_no", queryDto.getSupplierNo());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProductName()), "tsc.product_name", queryDto.getProductName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDefectiveName()), "tsc.defective_name", queryDto.getDefectiveName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProductHangtagConfirm()), "tsp.control_confirm", queryDto.getProductHangtagConfirm());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getControlConfirm()), "tsp.product_hangtag_confirm", queryDto.getControlConfirm());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getControlHangtagConfirm()), "tsp.control_hangtag_confirm", queryDto.getControlHangtagConfirm());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getPatternDesignName()), "ts.pattern_design_name", queryDto.getPatternDesignName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getOrderFlag()), "tsc.order_flag", queryDto.getOrderFlag());
        if (StringUtils.isNotBlank(queryDto.getMarkingOrderFlag())) {
            /*暂时注释 改为查询大货款号不为空的数据
            queryWrapper.inSql("tsc.id","select style_color_id from t_order_book_detail where status = '4'");
            */
            queryWrapper.isNotNullStr("tsc.style_no");
            queryWrapper.notIn("tsc.defective_no",Arrays.asList("-9","-10","-11","-ZC"));
        }
        if(StrUtil.isNotBlank(queryDto.getDesignMarkingStatus())){
            if (BaseGlobal.STATUS_NORMAL.equals(queryDto.getDesignMarkingStatus())) {
                queryWrapper.isNullStrEq("ts.design_marking_status", queryDto.getDesignMarkingStatus());
            } else {
                queryWrapper.eq("ts.design_marking_status", queryDto.getDesignMarkingStatus());
            }
        }
        if(StrUtil.isNotBlank(queryDto.getOrderMarkingStatus())){
            if (BaseGlobal.STATUS_NORMAL.equals(queryDto.getOrderMarkingStatus())) {
                queryWrapper.isNullStrEq("tsc.order_marking_status", queryDto.getOrderMarkingStatus());
            } else {
                queryWrapper.eq("tsc.order_marking_status", queryDto.getOrderMarkingStatus());
            }
        }
        return queryWrapper;
    }

    /**
     * 方法描述: 获取款式或配饰
     *
     * @return
     */
    @Override
    public List<StyleColorVo> getStyleAccessoryBystyleNo(String designNo) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("design_no", designNo);
        Style style = styleService.getOne(qw);
        if (ObjectUtils.isEmpty(style)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        QueryWrapper<StyleColor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("style_id", style.getId());
        List<StyleColor> styleColorList = baseMapper.selectList(queryWrapper);
        /*转换vo*/
        List<StyleColorVo> list = BeanUtil.copyToList(styleColorList, StyleColorVo.class);
        return list;
    }

    /**
     * 修改吊牌价-款式配色
     *
     * @param updateTagPriceDto
     * @return
     */
    @Override
    public Boolean updateTagPrice(UpdateTagPriceDto updateTagPriceDto) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("tag_price", updateTagPriceDto.getTagPrice());
        updateWrapper.eq("id", updateTagPriceDto.getId());
        baseMapper.update(null, updateWrapper);
        return true;
    }

    /**
     * 大货款号查询
     *
     * @param querySampleStyleColorDto
     * @return
     */
    @Override
    public List<StyleColorVo> getByStyleNo(QueryStyleColorDto querySampleStyleColorDto) {
        if (StringUtils.isBlank(querySampleStyleColorDto.getStyleNo())) {
            throw new OtherException(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("style_no", StringUtils.convertList(querySampleStyleColorDto.getStyleNo()));
        List<StyleColor> list = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        list = list.stream()
                .filter(it-> StrUtil.isNotBlank(it.getStyleId()) && styleService.exists(it.getStyleId()))
                .collect(Collectors.toList());
        List<StyleColorVo> styleColorVoList = BeanUtil.copyToList(list, StyleColorVo.class);
        return styleColorVoList;
    }

    /**
     * 方法描述: 批量新增款式配色-款式配色
     *
     * @param list@return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean batchAddSampleStyleColor(Principal user,List<AddRevampStyleColorDto> list) throws Exception {
        int index = 0;
        list = CollUtil.distinct(list, AddRevampStyleColorDto::getColorCode, true);
        /*查询颜色*/
        if(CollUtil.isEmpty(list)){
            throw new OtherException("未选中数据");
        }
        List<String> colorCodeList = list.stream().map(AddRevampStyleColorDto::getColorCode).distinct().collect(Collectors.toList());
        List<BasicsdatumColourLibrary> libraryList = basicsdatumColourLibraryServicel.listByField("colour_code",colorCodeList);
        /*颜色数据*/
        Map<String, BasicsdatumColourLibrary> map = libraryList.stream().collect(Collectors.toMap(BasicsdatumColourLibrary::getColourCode, c -> c));
        /*查询款式主数据*/
        Style style = styleService.getById(list.get(0).getStyleId());
        /*款式主数据数据*/
        for (AddRevampStyleColorDto addRevampStyleColorDto : list) {
            BasicsdatumColourLibrary basicsdatumColourLibrary = map.get(addRevampStyleColorDto.getColorCode());
            addRevampStyleColorDto.setStyleId(style.getId());
            addRevampStyleColorDto.setAccessory("");
            addRevampStyleColorDto.setAccessoryNo("");
            addRevampStyleColorDto.setPrincipalStyle("");
            addRevampStyleColorDto.setPrincipalStyleNo("");
            addRevampStyleColorDto.setColourLibraryId(basicsdatumColourLibrary.getId());
            addRevampStyleColorDto.setColorName(basicsdatumColourLibrary.getColourName());
            addRevampStyleColorDto.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            addRevampStyleColorDto.setColorCode(basicsdatumColourLibrary.getColourCode());
            addRevampStyleColorDto.setStyleNo(getNextCode( style ,StringUtils.isNotEmpty(addRevampStyleColorDto.getBandName()) ? addRevampStyleColorDto.getBandName() : style.getBandName(), StringUtils.isNotBlank(style.getOldDesignNo())?style.getOldDesignNo():style.getDesignNo(),addRevampStyleColorDto.getIsLuxury(),  ++index));
            addRevampStyleColorDto.setSendDeptId(style.getSendDeptId());
            addRevampStyleColorDto.setReceiveDeptId(style.getReceiveDeptId());
        }
        List<StyleColor> styleColorList = BeanUtil.copyToList(list, StyleColor.class);
        saveBatch(styleColorList);
        addMainAccessories(style, list, styleColorList);
        for (StyleColor styleColor : styleColorList) {
            /*获取到款式里面款式图给到配色中*/
            if(StringUtils.isNotBlank(style.getStylePic())){
                UploadStylePicDto uploadStylePicDto = new UploadStylePicDto();
                uploadStylePicDto.setStyleColorId(styleColor.getId());
                MultipartFile multipartFile = uploadFileService.downloadImage(style.getStylePic(), styleColor.getStyleNo() + ".jpg");
                uploadStylePicDto.setFile(multipartFile);
                uploadFileService.uploadStyleImage(uploadStylePicDto,user);
            }
        }

        return true;
    }

    /**
     * 新增配色异步操作维度信息和主款配色数据
     *
     * @param style
     * @param list
     * @param styleColorList
     */
//    @Async
    public void addMainAccessories(Style style, List<AddRevampStyleColorDto> list, List<StyleColor> styleColorList) {
        /*保存维度信息*/
        /*查询款式中的维度信息*/
        DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(style, dto);
        dto.setForeignId(style.getId());
        dto.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        List<FieldManagementVo> fieldManagementVoList = styleService.queryDimensionLabels(dto);
        List<FieldVal> fieldValList = BeanUtil.copyToList(fieldManagementVoList, FieldVal.class);
        /*主款配饰数据*/
        List<StyleMainAccessoriesSaveDto> saveDtoList = list.get(0).getSaveDtoList();
        List<StyleMainAccessoriesSaveDto> styleMainAccessoriesSaveDtoList = new ArrayList<>();
        /*获取新增的维度信息*/
        for (StyleColor styleColor : styleColorList) {
            // 保存工艺信息
            fieldValService.save(styleColor.getId(), FieldValDataGroupConstant.STYLE_COLOR, fieldValList);
            fieldValService.save(styleColor.getId(), FieldValDataGroupConstant.STYLE_MARKING_ORDER, fieldValList);
            /*获取全部的主款或配饰*/
            if (CollUtil.isNotEmpty(saveDtoList)) {
                saveDtoList.forEach(s -> s.setStyleColorId(styleColor.getId()));
                styleMainAccessoriesSaveDtoList.addAll(saveDtoList);
            }
        }
        /*保存主款配饰信息*/
        if (CollUtil.isNotEmpty(styleMainAccessoriesSaveDtoList)) {
            List<StyleMainAccessories> mainAccessoriesList = BeanUtil.copyToList(styleMainAccessoriesSaveDtoList, StyleMainAccessories.class);
            /*主款配饰相互绑定*/
            //获取配色信息
            List<String> styleNolist = mainAccessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("style_no", styleNolist);
            /*需要反向绑定的配色*/
            List<StyleColor> colorList = baseMapper.selectList(queryWrapper);

            for (StyleColor styleColor : colorList) {
                for (StyleColor color : styleColorList) {
                    StyleMainAccessories styleMainAccessories = new StyleMainAccessories();
                    styleMainAccessories.setColorCode(color.getColorCode());
                    styleMainAccessories.setColorName(color.getColorName());
                    styleMainAccessories.setStyleColorId(styleColor.getId());
                    styleMainAccessories.setIsTrim(styleColor.getIsTrim());
                    styleMainAccessories.setStyleNo(color.getStyleNo());
                    mainAccessoriesList.add(styleMainAccessories);
                }
            }
            /*新增*/
            styleMainAccessoriesService.saveBatch(mainAccessoriesList);
        }
    }

    /**
     * 大货款号生成规则 品牌 + 年份  + 波段 +品类 +设计款号流水号+颜色流水号
     * @param style
     * @param bandName
     * @param designNo
     * @param isLuxury
     * @param index
     * @return
     */
    public String getNextCode(Style style,String bandName,  String designNo,String isLuxury, int index) {
        if (ccmFeignService.getSwitchByCode("STYLE_EQUAL_DESIGN_NO")) {
            return designNo;
        }
        String category = "";
        /*查询产品季*/
        PlanningSeason planningSeason = planningSeasonService.getById(StrUtil.isNotBlank(style.getOldPlanningSeasonId()) ? style.getOldPlanningSeasonId() : style.getPlanningSeasonId());
        String brand = planningSeason.getBrand();
        String year = planningSeason.getYearName();

        PlanningSeason newPlanningSeason = planningSeasonService.getById(style.getPlanningSeasonId());
        // 240429 存在修改季节但不修改产品季的操作, 这里用产品季季节会导致匹配不上
        String season = style.getSeason();
        if (StringUtils.isNotBlank(style.getProdCategory())) {
            category = style.getProdCategory();
        }
        if (StringUtils.isBlank(bandName)) {
            throw new OtherException("款式波段为空");
        }
        if (StringUtils.isBlank(brand) || StringUtils.isBlank(newPlanningSeason.getBrand())) {
            throw new OtherException("款式品牌为空");
        }
        if (StringUtils.isBlank(year) || StringUtils.isBlank(newPlanningSeason.getYear())) {
            throw new OtherException("款式年份为空");
        }

        String yearOn = "";
        try {
//        获取年份
//            yearOn = getYearOn(year);
//            波段
//            bandName = getBandName(bandName);
            /*获取款号流水号*/
            /**
             * 款号流水号：拼接品牌 年份 季节 品类 成为设计款号前几位 去掉设计编码
             */
            String years = year.substring(year.length() - 2);
            /*拼接的设计款号（用于获取流水号）*/
            String joint = brand + years + planningSeason.getSeason() + category;
            designNo = designNo.replaceAll(joint, "");
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(designNo);
            designNo = m.replaceAll("").trim();
        } catch (Exception e) {
            throw new OtherException(e.getMessage() + "大货编码生成失败");
        }
//        获取款式下的配色
        String styleNoFront = newPlanningSeason.getBrand() + getYearOn(newPlanningSeason.getYear()) + getBandName(bandName) + category + designNo;
        String styleNo = "";
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_id", style.getId());
        Long aLong = baseMapper.selectCount(queryWrapper);

        /*轻奢款选是，大货款号最后一位自带Q*/
        isLuxury = StrUtil.equals(isLuxury, BaseGlobal.YES) ? "Q" : "";
        /*先使用款式下的流水号 如果重复
         * 不加流水号查询大货款号的最大流水号
         * */
        styleNo = styleNoFront + (aLong + index) + isLuxury;


        //region ED品牌大货款号特殊处理
        if ("6".equals(brand)) {
            styleNo = createEDStyleNo(style.getOldDesignNo(),year,season,brand, isLuxury, category, yearOn, styleNo,style.getMonth(),aLong + index);
            return styleNo;
        }
        //endregion

        int i = baseMapper.isStyleNoExist(styleNo);
        if (i != 0) {
            String maxMark = "0";
            String number = baseMapper.getStyleColorNumber(styleNoFront, styleNoFront.length() + 1);
            if (StringUtils.isNotBlank(number)) {
                /*获取最大流水号*/
                maxMark = number;
            }
            /*拼接流水号*/
            styleNo = styleNoFront + (Long.parseLong(maxMark) + index) + isLuxury;
            /*查询编码是否重复*/
            i = baseMapper.isStyleNoExist(styleNo);
            if (i != 0) {
                throw new OtherException("编码重复");
            }
        }
        return styleNo;
    }
    /**
     *
     * @param designNo 设计款号
     * @param year 年份
     * @param season 季节
     * @param brand 品牌
     * @param isLuxury 是否高奢标识
     * @param category 品类
     * @param yearOn 年份转义后的字母
     * @param styleNo 大货款号空字符串（进来设置为空）
     * @param month 月份
     * @param aLong 该大货款的设计款下有几个大货款
     * @return
     */
    @NotNull
    private static String createEDStyleNo(String designNo, String year, String season, String brand, String isLuxury, String category, String yearOn, String styleNo, String month, Long aLong) {
            yearOn = getYearOn(year);
            String years = year.substring(year.length() - 2);
            /*拼接的设计款号（用于获取流水号）*/
            String joint = brand + years + season + category;
            designNo = designNo.replaceAll(joint, "");
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(designNo);
            String designNoSeq = m.replaceAll("").trim();
            designNoSeq = designNoSeq.substring(designNoSeq.length() - 3, designNoSeq.length());
            styleNo = "1";
            styleNo += yearOn;
            String monthStr = "";
            monthStr = String.valueOf(month);
            if ("10".equals(month)) {
                monthStr = "A";
            } else if ("11".equals(month)) {
                monthStr = "B";
            } else if ("12".equals(month)) {
                monthStr = "C";
            }else{
                monthStr = monthStr.replace("0","");
            }
            styleNo+=monthStr;
            styleNo +="9";
            styleNo += category;
            //设计款的流水位
            styleNo += designNoSeq;
            //统计该大货款设计款下的大货款个数
            styleNo += aLong;
            styleNo += isLuxury;
            return styleNo;
    }




    /**
     * 波段：使用波段中的字母生产 1A：1 2B：2
     *
     * @param bandName
     * @return
     */
    public String getBandName(String bandName) {
        // 使用正则表达式匹配字母
        Matcher matcher = pattern.matcher(bandName);
        StringBuilder Letter = new StringBuilder();
        String month = "";
        // 打印匹配到的字母
        while (matcher.find()) {
            Letter.append(matcher.group());
        }
        if (!StringUtils.isEmpty(Letter.toString())) {
            month = getMonth(bandName, Letter.toString());
            Letter = new StringBuilder(Letter.toString().toUpperCase());
            char[] charArray = Letter.toString().toCharArray();
            int char1 = charArray[0];
            bandName = String.valueOf(char1 - 64);
        } else {
            bandName = "";
        }

        return month + bandName;
    }

    /**
     * 查询波段中的月份
     *
     * @param bandName
     * @param s
     * @return
     */
    public String getMonth(String bandName, String s) {
        String month = bandName.replace(s, "");
        if (!month.matches("[1-9]")) {
            month = "10".equals(month) ? "A" : "11".equals(month) ? "B" : "12".equals(month) ? "C" : "";
        }
        return month;
    }

    /**
     * 年份 初始值从2019开始为A依次往后推 超过26年份为A1
     *
     * @param year
     * @return
     */
    public static String getYearOn(String year) {
        if (StrUtil.equals(year, "2099")) {
            return "99";
        }
        String yearOn = "";
        int initial = Integer.parseInt("2019");
        int year1 = Integer.parseInt(year);
        int ascii = 'A';
        /*超过2044年重新开始*/
        if (year1 <= 2044) {
            char c = (char) (ascii + Math.abs(year1 - initial));
            yearOn = String.valueOf(c);
        } else {
            char c1 = (char) (ascii + (year1 - initial) - ((year1 - initial) / 26) * 26);
            yearOn = String.valueOf(c1) + (year1 - initial) / 26;
        }
        return yearOn;
    }

    /**
     * 获取品类生产的字符
     *
     * @param categoryName
     * @return
     */
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

    /**
     * 方法描述：新增修改样衣-款式配色
     *
     * @param addRevampStyleColorDto 样衣-款式配色Dto类
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addRevampSampleStyleColor(AddRevampStyleColorDto addRevampStyleColorDto) {
        StyleColor styleColor = new StyleColor();
        if (StringUtils.isEmpty(addRevampStyleColorDto.getId())) {
            /*新增*/
            BeanUtils.copyProperties(addRevampStyleColorDto, styleColor);
            styleColor.setCompanyCode(baseController.getUserCompany());
            styleColor.insertInit();
            this.save(styleColor, "款式配色");
        } else {
            /*修改*/
            if (StringUtils.isBlank(addRevampStyleColorDto.getColorCode())) {
                throw new OtherException("颜色不能为空");
            }
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getByOne("colour_code",addRevampStyleColorDto.getColorCode());
            if (ObjectUtils.isEmpty(basicsdatumColourLibrary)) {
                throw new OtherException("无颜色");
            }
            styleColor = baseMapper.selectById(addRevampStyleColorDto.getId());
            StyleColor old = new StyleColor();
            BeanUtil.copyProperties(styleColor, old);
            /*颜色修改*/
            if(!StrUtil.equals(styleColor.getColorCode(),addRevampStyleColorDto.getColorCode())){
                PackInfo packInfo = packInfoService.getByOne("style_no",styleColor.getStyleNo());
                if(!ObjectUtils.isEmpty(packInfo)){
                    packInfo.setColorCode(basicsdatumColourLibrary.getColourCode());
                    packInfo.setColor(basicsdatumColourLibrary.getColourName());
                    packInfoService.updateById(packInfo);
                }
            }
            /*修改*/
           boolean b = !StringUtils.equals(styleColor.getScmSendFlag(), BaseGlobal.IN_READY)&&StrUtil.isBlank(styleColor.getHisStyleNo());

            /*判断轻奢款是否修改*/
            if( !StrUtil.equals( styleColor.getIsLuxury(),addRevampStyleColorDto.getIsLuxury())&& b) {
                /*修改了*/
                if (StrUtil.equals(addRevampStyleColorDto.getIsLuxury(), BaseGlobal.NO)) {
                    /*判断最后以为是不是Q*/
                    String s = addRevampStyleColorDto.getStyleNo().substring(addRevampStyleColorDto.getStyleNo().length() - 1);
                    if (StrUtil.equals(s, "Q")) {
                        /*去掉Q*/
                        addRevampStyleColorDto.setStyleNo(addRevampStyleColorDto.getStyleNo().substring(0,addRevampStyleColorDto.getStyleNo().length()-1));
                    }
                } else {
                    addRevampStyleColorDto.setStyleNo(addRevampStyleColorDto.getStyleNo() + "Q");
                }
                /**
                 * 修改所有引用的大货款号
                 */
                updateStyleColor(styleColor.getBom(),styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            /*判断是否修改波段
             * 当配色未下发时可以修改会影响大货款号
             * 当配色下发后可以修改波段不会影响大货款号
             * 如果手动修改过大货款号 再去修改波段则不会变化
             */
            if (!StringUtils.equals(addRevampStyleColorDto.getBandCode(), styleColor.getBandCode()) && b ) {
                /*新大货款号 ：换标波段生成的字符*/
                /**
                 * 先生成波段之前的字符串替换为空，在拼接
                 */
                Style style = styleService.getById(styleColor.getStyleId());
                //获取年份
                String year = getYearOn(style.getYearName());
                /*品牌*/
                String brand = style.getBrand();
                /*大货款的前及位 品牌加年份*/
                String newStyle = brand + year;
                /*后几位大货款号*/
                String s = styleColor.getStyleNo().replaceAll(newStyle + getBandName(styleColor.getBandName()), "");
                /*拼接新大货款号*/
                String styleNo = newStyle + getBandName(addRevampStyleColorDto.getBandName()) + s;
                int i = baseMapper.isStyleNoExist(styleNo);
                if (i > 0) {
                    throw new OtherException("波段修改失败，大货款重复");
                }
                addRevampStyleColorDto.setStyleNo(styleNo);
                /**
                 * 修改所有引用的大货款号
                 */
                updateStyleColor(styleColor.getBom(),styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            /*判断波段及细分是否改动 改动则需要同步大货款号*/
            if (StringUtils.isNotBlank(addRevampStyleColorDto.getSubdivide())&& b) {
                /**
                 * 修改所有引用的大货款号
                 * 查看之前有没有细分
                 */
                String styleNo = "";
                if (StrUtil.isNotBlank(styleColor.getSubdivide())) {
                    styleNo = addRevampStyleColorDto.getStyleNo().replace(styleColor.getSubdivide(), "");
                } else {
                    styleNo = addRevampStyleColorDto.getStyleNo();
                }
                /*新大货款号=大货款号+细分*/
                addRevampStyleColorDto.setStyleNo(styleNo + addRevampStyleColorDto.getSubdivide());
                updateStyleColor(styleColor.getBom(),styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            if (ObjectUtils.isEmpty(styleColor)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            /*主款配饰*/
            List<StyleMainAccessoriesSaveDto> saveDtoList = addRevampStyleColorDto.getSaveDtoList();
            if (StrUtil.equals(addRevampStyleColorDto.getSaveDtoFlag(), BaseGlobal.STATUS_CLOSE)) {
                //删除
                QueryWrapper<StyleMainAccessories> styleColorIdQw = new QueryWrapper<StyleMainAccessories>();
                styleColorIdQw.eq("style_color_id", styleColor.getId()).or().eq("style_no", styleColor.getStyleNo());
                styleMainAccessoriesService.remove(styleColorIdQw);
                if (CollUtil.isNotEmpty(saveDtoList)) {
                    /*查询需要保存的数据*/
                    List<StyleMainAccessories> accessoriesList = BeanUtil.copyToList(saveDtoList, StyleMainAccessories.class);
                    for (StyleMainAccessories styleMainAccessories : accessoriesList) {
                        styleMainAccessories.setStyleColorId(styleColor.getId());
                        styleMainAccessories.setIsTrim(styleMainAccessories.getIsTrim());
                    }
                    /*修改主款配饰*/
                    List<String> styleNoList = accessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
                    List<StyleColor> colorList = listByField("style_no", styleNoList);
                    /*需要反向绑定的配色*/

                    /*新增主款配饰*/
                    if (CollUtil.isNotEmpty(colorList)) {
                        for (StyleColor color : colorList) {
                            StyleMainAccessories styleMainAccessories = new StyleMainAccessories();
                            styleMainAccessories.setIsTrim(color.getIsTrim());
                            styleMainAccessories.setStyleColorId(color.getId());
                            styleMainAccessories.setColorCode(styleColor.getColorCode());
                            styleMainAccessories.setColorName(styleColor.getColorName());
                            styleMainAccessories.setStyleNo(styleColor.getStyleNo());
                            accessoriesList.add(styleMainAccessories);
                        }
                    }
                    styleMainAccessoriesService.saveBatch(accessoriesList);
                }

            }
            addRevampStyleColorDto.setStyleColorPic(styleColor.getStyleColorPic());
            BeanUtils.copyProperties(addRevampStyleColorDto, styleColor);
            /*赋值颜色*/
            styleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
            styleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
            styleColor.setColorName(basicsdatumColourLibrary.getColourName());
            styleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            styleColor.setAccessory("");
            styleColor.setAccessoryNo("");
            styleColor.setPrincipalStyle("");
            styleColor.setPrincipalStyleNo("");
            this.updateById(styleColor);

            UpdateWrapper<StyleColor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(StyleColor::getSendBatchingDate1, styleColor.getSendBatchingDate1())
                    .set(StyleColor::getSendBatchingDate2, styleColor.getSendBatchingDate2())
                    .set(StyleColor::getSendBatchingDate3, styleColor.getSendBatchingDate3())
                    .set(StyleColor::getSendSingleDate, styleColor.getSendSingleDate())
                    .set(StyleColor::getDesignDetailDate, styleColor.getDesignDetailDate())
                    .set(StyleColor::getDesignCorrectDate, styleColor.getDesignCorrectDate())
                    .eq(StyleColor::getId, styleColor.getId());
            this.update(null, updateWrapper);

            if ("0".equals(addRevampStyleColorDto.getOrderFlag())){
                planningProjectPlankService.unMatchByBulkStyleNo(styleColor.getStyleNo());
            }
            StyleColor styleColor1 = this.getById(styleColor.getId());

            this.saveOperaLog("修改", "款式配色", styleColor.getColorName(), styleColor.getStyleNo(), styleColor1, old);

            if (StrUtil.isNotBlank(styleColor1.getBom())) {
                /*重新下发配色*/
                dataUpdateScmService.updateStyleColorSendById(styleColor.getId());
            }
        }
        return true;
    }

    /**
     * 同步大货
     * @param styleNo
     * @param newStyleNo
     * @return
     */
    public void updateStyleColor(String bom,String styleNo, String newStyleNo) {
        baseMapper.reviseAllStyleNo(styleNo, newStyleNo);
        if (StringUtils.isNotBlank(bom)) {
            packInfoService.updateBomName(bom,newStyleNo);
        }
    }

    /**
     * 方法描述：删除样衣-款式配色
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delSampleStyleColor(String id, String styleId) {
        /*      List<String> ids = StringUtils.convertList(id);
         *//*批量删除*//*
        baseMapper.deleteBatchIds(ids);*/
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("del_flag", "1");
        updateWrapper.eq("style_id", styleId);
        updateWrapper.in("colour_library_id", StringUtils.convertList(id));
        baseMapper.update(null, updateWrapper);
        return true;
    }

    /**
     * 方法描述：删除样衣-款式配色
     *
     * @return boolean
     */
    @Override
    public Boolean delStyleColor(RemoveDto removeDto) {
        /*配色数据和BOM关联的不能删除*/
        List<StyleColor> styleColors = baseMapper.selectBatchIds(StringUtils.convertList(removeDto.getIds()));

    /*    styleColors = styleColors.stream().filter(s -> StringUtils.isBlank(s.getAccessoryNo()) || StringUtils.isBlank(s.getPrincipalStyleNo())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(styleColors)){
            throw new OtherException("该配色存在主款或配饰");
        }*/
        styleColors = styleColors.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(styleColors)) {
            throw new OtherException("存在BOM关联数据无法删除");
        }
        /*批量删除*/
        this.removeByIds(removeDto);
        return true;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopSampleStyleColor(StartStopDto startStopDto) {
        UpdateWrapper<StyleColor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        this.startStopLog(startStopDto);
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 方法描述: 修改颜色
     *
     * @param updateColorDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean updateColor(UpdateColorDto updateColorDto) {

        StyleColor styleColor = baseMapper.selectById(updateColorDto.getId());
        BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getByOne("colour_code",updateColorDto.getColorCode());
        if (ObjectUtils.isEmpty(basicsdatumColourLibrary)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        /*赋值颜色*/
        styleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
        styleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
        styleColor.setColorName(basicsdatumColourLibrary.getColourName());
        styleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
        baseMapper.updateById(styleColor);
        PackInfo packInfo = packInfoService.getByOne("style_no",styleColor.getStyleNo());
        if(!ObjectUtils.isEmpty(packInfo)){
            packInfo.setColorCode(styleColor.getColorCode());
            packInfo.setColor(styleColor.getColorName());
            packInfoService.updateById(packInfo);
        }
        /*重新下发配色*/
        dataUpdateScmService.updateStyleColorSendById(styleColor.getId());
        return true;
    }

    /**
     * 方法描述 下发scm
     * 已下发数据不能再次下发
     * 没关联bom的配色不能下发
     * 物料成本为0的不能下发
     * 核价里面总成本为0的不能下发
     * 吊牌价为0不能下发
     *
     * @param queryStyleColorDto
     * @return
     */
    @Override
    public ApiResult issueScm(QueryStyleColorDto queryStyleColorDto) {
        String ids = queryStyleColorDto.getIds();
        if (StringUtils.isBlank(ids)) {
            throw new OtherException("ids为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
/*        queryWrapper.in("id", StringUtils.convertList(ids));
        queryWrapper.ne("scm_send_flag", BaseGlobal.YES);*/
        List<StyleColor> styleColorList = baseMapper.getStyleMainAccessories(StringUtils.convertList(ids));
        /*查询配色是否下发*/
        if(StrUtil.isEmpty(queryStyleColorDto.getTargetBusinessSystem())){
            //不是从下发界面推送时判断，是否解锁
            if (CollectionUtils.isEmpty(styleColorList)) {
                throw new OtherException("存在已下发数据");
            }
        }else{
            styleColorList = baseMapper.getStyleMainAccessoriesNoSendFlag(StringUtils.convertList(ids));
        }

        List<String> stringList = styleColorList.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).map(StyleColor::getId).collect(Collectors.toList());

        /*禁止下发未关联bom数据*/
        if (CollectionUtils.isEmpty(stringList) || styleColorList.size() != stringList.size()) {
            String styleNo =  styleColorList.stream().filter(s -> StringUtils.isBlank(s.getBom())).map(StyleColor::getStyleNo).collect(Collectors.joining(","));
            throw new OtherException(styleNo+"无关联BOM，主款或配饰未关联BOM");
        }
        /*查询BOM*/
        queryWrapper.clear();
        queryWrapper.in("code", styleColorList.stream().map(StyleColor::getBom).collect(Collectors.toList()));
        List<PackInfo> packInfoList = packInfoService.list(queryWrapper);
        Map<String, List<PackInfo>> listMap = packInfoList.stream().collect(Collectors.groupingBy(PackInfo::getCode));
        /*查询配色关联的bom总成本价不等为0
         * 先查询物料的总成本 没有则去查询核价里面的总成本*/
        for (StyleColor styleColor : styleColorList) {
            PackInfo packInfo = listMap.get(styleColor.getBom()).get(0);
            PackCommonSearchDto packCommonSearchDto = new PackCommonSearchDto();
            packCommonSearchDto.setForeignId(packInfo.getId());
            packCommonSearchDto.setPackType(PackUtils.PACK_TYPE_DESIGN);
//            核价成本
            BigDecimal packBomCost = packBomService.calculateCosts(packCommonSearchDto);
//            物料成本为0时查询核价信息的总成本
            if (packBomCost.compareTo(BigDecimal.ZERO) == 0) {
//                核价信息总成本
                BigDecimal bigDecimal = packPricingService.countTotalPrice(packInfo.getId(), "1",2);
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                    throw new OtherException(styleColor.getStyleNo() + "的开发成本为0,请联系设计工艺员添加材料到BOM");
                }
            }
        }
        int i = smpService.goods(StringUtils.convertListToString(stringList).split(","),queryStyleColorDto.getTargetBusinessSystem(),queryStyleColorDto.getYshBusinessSystem());
        if (stringList.size() == i) {
            return ApiResult.success("下发：" + stringList.size() + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + stringList.size() + "条，成功：" + i + "条,失败：" + (stringList.size() - i) + "条", 200);
        }
    }

    @Override
    public ApiResult checkAccessoryRelatedMainStyle(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new OtherException("ids为空");
        }
        List<StyleColor> styleColorList = baseMapper.getStyleMainAccessories(StringUtils.convertList(ids));
        String result = "";
        for (StyleColor styleColor : styleColorList) {
            //验证配饰款是否绑定主款号
            result = result + checkAccessoriesMainStyleNo(styleColor);
        }

        if (StrUtil.isNotBlank(result)) {
            return ApiResult.error(result,BaseErrorEnum.ERR_ACCESSORIES_NOT_RELATE_MAIN_STYLE.getErrorCode());
        }
        return ApiResult.success();
    }


    /**
     * 判断配饰款是否绑定主款号
     * @param styleColor
     */
    private static String checkAccessoriesMainStyleNo(StyleColor styleColor) {
        //region 1.检查款号最后一位是不是P结尾的，2。 判断是否有主款号，没有就报错
        String styleColorNo = styleColor.getStyleNo();
        if (StrUtil.isNotBlank(styleColorNo) && styleColorNo.contains("P")) {
            String lastStr = styleColorNo.substring(styleColorNo.length() - 1);
            if ("P".equals(lastStr) && StrUtil.isEmpty(styleColor.getPrincipalStyleNo())) {
                return "该配饰款：【" + styleColorNo + "】没有绑定主款号！";
            }
        }
        return null;
        //endregion
    }
    /**
     * 方法描述 获取款式下的颜色
     *
     * @param styleId
     */
    @Override
    public List<String> getStyleColorId(String styleId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_id", styleId);
        queryWrapper.eq("is_defective", BaseGlobal.NO);
        List<StyleColor> styleColorList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(styleColorList)) {
            return new ArrayList<>();
        }
        return styleColorList.stream().map(StyleColor::getColorCode).collect(Collectors.toList());
    }

    /**
     * 方法描述 关联bom
     *
     * @param relevanceBomDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean relevanceBom(RelevanceBomDto relevanceBomDto) {
        /*查询配色信息*/
        StyleColor styleColor = baseMapper.selectById(relevanceBomDto.getStyleColorId());
        if (ObjectUtils.isEmpty(styleColor)) {
            throw new OtherException("无配色信息");
        }
        if (StringUtils.isNotBlank(styleColor.getBom())) {
            throw new OtherException("该配色已关联bom");
        }
        /*查询bom信息*/
        PackInfo packInfo = packInfoService.getById(relevanceBomDto.getPackInfoId());
        if (ObjectUtils.isEmpty(packInfo)) {
            throw new OtherException("无bom信息");
        }
        if (StringUtils.isNotBlank(packInfo.getStyleNo())) {
            throw new OtherException("该bom以关联bom");
        }
        /*关联bom*/
        styleColor.setBom(packInfo.getCode());
        /*bom关联配色*/
        packInfo.setStyleNo(styleColor.getStyleNo());
        packInfo.setColorCode(styleColor.getColorCode());
        packInfo.setColor(styleColor.getColorName());
        packInfo.setStyleColorId(styleColor.getId());
        packInfo.setName(styleColor.getStyleNo());
        baseMapper.updateById(styleColor);
        packInfoService.updateById(packInfo);
        return true;
    }

    /**
     * 方法描述 修改大货款号,波段
     *
     * @param updateStyleNoBandDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean updateStyleNoBand(Principal user,UpdateStyleNoBandDto updateStyleNoBandDto) {
        StyleColor sampleStyleColor = baseMapper.selectById(updateStyleNoBandDto.getId());
        String styleNo = sampleStyleColor.getStyleNo();
        String updateStyleNo = StringUtils.keepStrByType(updateStyleNoBandDto.getStyleNo(), "检查大货款号,仅允许字母数字",MatchStrType.LETTER, MatchStrType.NUMBER, MatchStrType.BARRE);
        Assert.isFalse(updateStyleNo.length() > 18,"大货款号不能超过18位");
        StyleColor styleColor1 = new StyleColor();
        styleColor1.setStyleNo(styleNo);
        if (ObjectUtils.isEmpty(sampleStyleColor)) {
            throw new OtherException("id错误");
        }
        /*修改大货款号*/
        if (StringUtils.isNotBlank(sampleStyleColor.getStyleNo()) && StringUtils.isNotBlank(updateStyleNo) && !sampleStyleColor.getStyleNo().equals(updateStyleNo)) {

          /*  if (!updateStyleNoBandDto.getStyleNo().substring(0, 1).equals(sampleStyleColor.getStyleNo().substring(0, 1))) {
                throw new OtherException("无法修改大货款号前1位");
            }*/
            if ("1".equals(sampleStyleColor.getScmSendFlag())){
                throw new OtherException("大货款号已下发,无法修改");
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("style_no", updateStyleNo);
            StyleColor styleColor = baseMapper.selectOne(queryWrapper);
            if (!ObjectUtils.isEmpty(styleColor)) {
                throw new OtherException("大货款号已存在");
            }
            /**
             * 修改下放大货款号
             */
            baseMapper.reviseAllStyleNo(sampleStyleColor.getStyleNo(), updateStyleNo);

            /*修改关联的BOM名称*/
            if (StringUtils.isNotBlank(sampleStyleColor.getBom())) {
                packInfoService.updateBomName(sampleStyleColor.getBom(), updateStyleNo);
            }
            /*只会记录最开始的大货款号*/
            if (StringUtils.isBlank(sampleStyleColor.getHisStyleNo())) {
                sampleStyleColor.setHisStyleNo(sampleStyleColor.getStyleNo());
            }
            sampleStyleColor.setStyleNo(updateStyleNo.replaceAll(" ", ""));
        }
        /*修改波段*/
    /*    if(StringUtils.isNotBlank(sampleStyleColor.getBandCode()) && StringUtils.isNotBlank(updateStyleNoBandDto.getBandCode())){
            if(!sampleStyleColor.getBandCode().equals(updateStyleNoBandDto.getBandCode())){
                sampleStyleColor.setBandCode(updateStyleNoBandDto.getBandCode());
                sampleStyleColor.setBandName(updateStyleNoBandDto.getBandName());
            }
        }*/
        baseMapper.updateById(sampleStyleColor);
        StyleColor styleColor = new StyleColor();
        styleColor.setStyleNo(updateStyleNo);
        this.saveOperaLog("修改大货款号", "款式配色", sampleStyleColor.getColorName(), sampleStyleColor.getStyleNo(), styleColor, styleColor1);
        /*重新下发配色*/
        dataUpdateScmService.updateStyleColorSendById(sampleStyleColor.getId());

        //region 20231219 huangqiang 修改大货款号将老款图片下载重新上传，上传成功后删除
        if(StrUtil.isNotBlank(sampleStyleColor.getStyleColorPic())){
            Boolean result = uploadImgAndDeleteOldImg(user, updateStyleNoBandDto, sampleStyleColor, styleColor);
            if (!result) {
                throw new OtherException("图片上传失败！");
            }
        }
        //endregion
        return true;
    }


    /**
     * 方法描述 验证配色是否可修改
     *
     * @param id
     * @return
     */
    @Override
    public Boolean verification(String id) {
        StyleColor styleColor = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(styleColor)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        // 款式配色不能已下发
        if (StrUtil.equals("1", styleColor.getScmSendFlag())) {
            throw new OtherException("请解锁配色");
        }
        PageInfo<PackInfoListVo> packInfoListVoPageInfo = packInfoService.getInfoListByDesignNo(styleColor.getStyleId());
        if (CollUtil.isEmpty(packInfoListVoPageInfo.getList())) {
            throw new OtherException("未找到关联Bom");
        }

        // BOM阶段不为样品不允许修改
        if (StrUtil.isNotBlank(styleColor.getBomStatus()) && StrUtil.equals("1", styleColor.getBomStatus())) {
            throw new OtherException("BOM阶段为大货阶段，请工艺部和外发部退回样品阶段");
        }
        // 物料清单不能有已下发的
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        queryWrapper.eq("ts.id", styleColor.getStyleId());
        queryWrapper.eq("tsc.id", id);
        List<StyleColorVo> sampleStyleColorList = baseMapper.colorList(queryWrapper);
        if (CollUtil.isEmpty(sampleStyleColorList)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        StyleColorVo styleColorVo = sampleStyleColorList.get(0);
        PackCommonPageSearchDto dto = new PackCommonPageSearchDto();
        dto.setForeignId(styleColorVo.getPackInfoId());
        dto.setPackType("packDesign");
        PageInfo<PackBomVersionVo> packBomVersionVoPageInfo = packBomVersionService.pageInfo(dto);
        if (CollUtil.isNotEmpty(packBomVersionVoPageInfo.getList())) {
            PackBomVersionVo packBomVersionVo = packBomVersionVoPageInfo.getList().get(0);
            PackBomPageSearchDto pageSearchDto = new PackBomPageSearchDto();
            pageSearchDto.setBomVersionId(packBomVersionVo.getId());
            pageSearchDto.setForeignId(styleColorVo.getPackInfoId());
            pageSearchDto.setPackType("packDesign");
            PageInfo<PackBomVo> pageInfo = packBomService.pageInfo(pageSearchDto);
            if (CollUtil.isNotEmpty(pageInfo.getList())) {
                List<PackBomVo> packBomVos = pageInfo.getList();
                PackBomVo packBomVo = packBomVos.stream().filter(p -> StrUtil.equals(p.getScmSendFlag(), "1")).findFirst().orElse(null);
                if (null != packBomVo) {
                    throw new OtherException("请解锁BOM");
                }
            }
        }

        PdmStyleCheckParam pdmStyleCheckParam = new PdmStyleCheckParam();
        pdmStyleCheckParam.setStyleNo(styleColor.getStyleNo());
        pdmStyleCheckParam.setCode(styleColor.getColorCode());
        try {
            Boolean b = smpService.checkColorSize(pdmStyleCheckParam);
            if (!b) {
                throw new OtherException("SCM系统存在投产单或备料单,不允许修改颜色!");
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
        return true;
    }

    /**
     * 方法描述 配色解锁
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    public Boolean unlockStyleColor(PublicStyleColorDto publicStyleColorDto) {
        if (StringUtils.isBlank(publicStyleColorDto.getScmSendFlag())) {
            throw new OtherException("SCM下发状态为空");
        }
        StyleColor styleColor = baseMapper.selectById(publicStyleColorDto.getId());
        if (ObjectUtils.isEmpty(styleColor)) {
            throw new OtherException("id错误无数据");
        }
        styleColor.setScmSendFlag(publicStyleColorDto.getScmSendFlag());
        baseMapper.updateById(styleColor);
        return true;
    }

    /**
     * 方法描述 新增次品款
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addDefective(PublicStyleColorDto publicStyleColorDto,Principal user) {
        /**
         *复制一个配色次品款
         * 校验 计控吊牌确定已确定
         * 大货款号加上次品编号，改为次品 ，同时复制一个bom名称为大货款号加次品编号
         * 复制一个未审核的吊牌
         * 复制出的BOM是样品阶段，里面的物料也修改未样品阶段未下发
         * 资料包如果已经转大货需要查看设计用量是否为空 空取大货用量
         */
        if (StringUtils.isBlank(publicStyleColorDto.getDefectiveName())) {
            throw new OtherException("次品必填");
        }
        if (StringUtils.isBlank(publicStyleColorDto.getDefectiveNo())) {
            throw new OtherException("次品必填");
        }
        if (StringUtils.isBlank(publicStyleColorDto.getColorCode())) {
            throw new OtherException("颜色必填");
        }
        /*配色信息*/
        StyleColor styleColor = baseMapper.selectById(publicStyleColorDto.getId());
        if (ObjectUtils.isEmpty(styleColor) || StringUtils.isBlank(styleColor.getBom())) {
            throw new OtherException("id错误无数据或该配色无bom");
        }
        if (StringUtils.isNotEmpty(styleColor.getDefectiveName())) {
            throw new OtherException("报次款不能生成报次款");
        }
        /*查询次品的大货款号是否重复*/
        int i = baseMapper.isStyleNoExist(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        if (i != 0) {
            throw new OtherException("该次品已生成款");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("tsc.style_no", styleColor.getStyleNo());
        queryWrapper.eq("tsc.del_flag", 0);
        List<StyleColorVo> styleColorVoList = baseMapper.colorList(queryWrapper);
        if (CollectionUtils.isEmpty(styleColorVoList)) {
            throw new OtherException("大货款号查无数据");
        }
        StyleColorVo styleColorVo = styleColorVoList.get(0);
        /*判断吊牌价是否确定*/
        if (StringUtils.isBlank(styleColorVo.getProductHangtagConfirm()) || styleColorVo.getProductHangtagConfirm().equals(BaseGlobal.NO)) {
            throw new OtherException("计控吊牌确定未确定");
        }
        /*查询配色关联的资料包*/
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("style_color_id", styleColor.getId());
        queryWrapper1.eq("style_no", styleColor.getStyleNo());
        PackInfo packInfo = packInfoService.getOne(queryWrapper1);
        PackInfoStatus packInfoStatus = packInfoStatusService.get(packInfo.getId(), PackUtils.PACK_TYPE_DESIGN);

        /*复制配色数据*/
        StyleColor copyStyleColor = new StyleColor();
        BeanUtils.copyProperties(styleColor, copyStyleColor, "id");

        //设置bom编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("foreign_id", styleColor.getStyleId());
        long count = packInfoMapper.countByQw(codeQw);
        /*复制配色*/
        String packType = "";
        if(StrUtil.equals(packInfoStatus.getBomStatus(),BaseGlobal.NO)){
            packType = PackUtils.PACK_TYPE_DESIGN;
        }else {
            packType =  PackUtils.PACK_TYPE_BIG_GOODS;
        }

        IdGen idGen = new IdGen();
        BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getByOne("colour_code",publicStyleColorDto.getColorCode());
        copyStyleColor.setColorName(basicsdatumColourLibrary.getColourName());
        copyStyleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
        copyStyleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
        copyStyleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
        copyStyleColor.setStyleNo(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        copyStyleColor.setStyleColorPic(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        copyStyleColor.setDefectiveName(publicStyleColorDto.getDefectiveName());
        copyStyleColor.setDefectiveNo(publicStyleColorDto.getDefectiveNo());
        copyStyleColor.setIsDefective(BaseGlobal.YES);
        copyStyleColor.setBom(styleColor.getDesignNo() + StrUtil.DASHED + (count + 1));
        copyStyleColor.setWareCode(null);
        copyStyleColor.setScmSendFlag(BaseGlobal.NO);
        copyStyleColor.setBomStatus(BaseGlobal.STATUS_NORMAL);
        copyStyleColor.setPrincipalStyleNo("");
        copyStyleColor.setPrincipalStyle("");
        copyStyleColor.setAccessoryNo("");
        copyStyleColor.setAccessory("");
        copyStyleColor.setId(String.valueOf(idGen.nextId()));
        baseMapper.insert(copyStyleColor);

        /*吊牌复制*/
        hangTagService.copyPack(styleColor.getStyleNo(), copyStyleColor.getStyleNo());
        /*新建一个资料包*/
        PackInfo copyPackInfo = new PackInfo();
        BeanUtils.copyProperties(packInfo, copyPackInfo, "id");
        copyPackInfo.setStyleNo(copyStyleColor.getStyleNo());
        copyPackInfo.setStyleColorId(copyStyleColor.getId());
        copyPackInfo.setCode(styleColor.getDesignNo() + StrUtil.DASHED + (count + 1));
        copyPackInfo.setName(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        copyPackInfo.setColor(basicsdatumColourLibrary.getColourName());
        copyPackInfo.setColorCode(basicsdatumColourLibrary.getColourCode());
        packInfoService.save(copyPackInfo);

        /*复制资料包里面的数据*/
        packInfoService.copyPack(packInfo.getId(),packType , copyPackInfo.getId(), packInfoStatus.getPackType(), BaseGlobal.YES, BasicNumber.ZERO.getNumber(),BaseGlobal.YES);
        /*复制状态*/
        packInfoStatusService.copy(packInfo.getId(), packType, copyPackInfo.getId(), packInfoStatus.getPackType(), BaseGlobal.YES);
        /*查询BOM状态，BOM阶段修改未为样品 BOM里面物料也修改为样品*/
        QueryWrapper<PackBom> packBomQueryWrapper = new QueryWrapper<>();
        packBomQueryWrapper.eq("foreign_id",copyPackInfo.getId());
        packBomQueryWrapper.eq("del_flag",0);
        List<PackBom> packBomList = packBomService.list(packBomQueryWrapper);
        if (CollUtil.isNotEmpty(packBomList)) {
            packBomList.forEach(item->{
                    item.setStageFlag(PackUtils.PACK_TYPE_DESIGN);
                    item.setScmSendFlag("0");
                    packBomService.updateById(item);
            });
        }

        /*复制出来的BOM*/
        PackInfoStatus copyPackInfoStatus = packInfoStatusService.get(copyPackInfo.getId(), packInfoStatus.getPackType());
        copyPackInfoStatus.setBomStatus(BaseGlobal.STATUS_NORMAL);
        packInfoStatusService.updateById(copyPackInfoStatus);

        /*        *//*查询物料清单*//*
        List<PackBomVo> packBomVoList = packBomVersionService.getEnableVersionBomList(copyPackInfo.getId(), packInfoStatus.getPackType());
        packBomVoList.forEach(p -> p.setStageFlag(PackUtils.PACK_TYPE_DESIGN));
        *//*修改阶段*//*
        List<PackBom> packBomList = BeanUtil.copyToList(packBomVoList, PackBom.class);
        packBomService.saveOrUpdateBatch(packBomList);*/
        /*复制出款式定价确定数据*/
        StylePricing stylePricing = new StylePricing();
        stylePricing.setControlConfirm(styleColorVo.getControlConfirm());
        stylePricing.setProductHangtagConfirm(styleColorVo.getProductHangtagConfirm());
        stylePricing.setControlHangtagConfirm(styleColorVo.getControlHangtagConfirm());
        stylePricing.setWagesConfirm(styleColorVo.getWagesConfirm());
        stylePricing.setControlPlanCost(styleColorVo.getControlPlanCost());
        stylePricing.setPackId(copyPackInfo.getId());
        stylePricing.setCompanyCode(baseController.getUserCompany());
        stylePricingMapper.insert(stylePricing);



        //region 20240527 huangqiang 生成报此款老款图片下载重新上传
        uploadImg(user, styleColor, copyStyleColor);
        //endregion
        return true;
    }

    private void uploadImg(Principal user, StyleColor styleColor, StyleColor copyStyleColor) {
        if(StrUtil.isNotBlank(styleColor.getStyleColorPic())){
            Boolean result = true;
            String styleColorPic = styleColor.getStyleColorPic();
            if(StringUtils.isNotBlank(copyStyleColor.getStyleNo())){
                UploadStylePicDto uploadStylePicDto = new UploadStylePicDto();
                uploadStylePicDto.setStyleColorId(copyStyleColor.getId());
                MultipartFile multipartFile = null;
                Boolean uploadStatus = false;
                try {
                    multipartFile = uploadFileService.downloadImage(styleColorPic, copyStyleColor.getStyleNo() + ".jpg");
                    uploadStylePicDto.setFile(multipartFile);
                    uploadStatus = uploadFileService.uploadStyleImage(uploadStylePicDto, user);
                } catch (Exception e) {
                    result = false;
                    log.info(e.getMessage());
//                throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 方法描述 更新下单标记
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    public Boolean updateOrderFlag(PublicStyleColorDto publicStyleColorDto) {
        if (StringUtils.isBlank(publicStyleColorDto.getOrderFlag())) {
            throw new OtherException("下单标记为空");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        List<String> ids = StringUtils.convertList(publicStyleColorDto.getId());
        /*下单时需要校验维度信息必填*/
        if (StringUtils.equals(publicStyleColorDto.getOrderFlag(), BaseGlobal.STATUS_CLOSE)) {
            updateWrapper.set("order_date", new Date());
            /*获取款式数据*/
            List<StyleColor> styleColorList = baseMapper.selectBatchIds(ids);
            if (publicStyleColorDto.isCheckScmSendFlag()) {
                styleColorList.stream().filter(it-> !YesOrNoEnum.YES.getValueStr().equals(it.getScmSendFlag())).forEach(styleColor-> {
                    throw new OtherException(String.format("款式配色%s未下发", styleColor.getStyleNo()));
                });
            }
            Map<String, StyleColor> styleColorMap = styleColorList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
            /*获取款式id*/
            List<String> styleIdList = styleColorList.stream().map(StyleColor::getStyleId).collect(Collectors.toList());
            /*款式列表可能下单不同款下面的配色*/
            List<Style> styleList = styleService.listByIds(styleIdList);
            Map<String, Style> styleMap = styleList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
            /*获取维度必填的维度信息*/
            for (String id : ids) {
                StyleColor styleColor = styleColorMap.get(id);
                DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
                BeanUtil.copyProperties(styleMap.get(styleColor.getStyleId()), dto);
                dto.setId(styleColor.getStyleId());
                dto.setForeignId(id);
                dto.setDataGroup(FieldValDataGroupConstant.STYLE_COLOR);
                List<FieldManagementVo> managementVoList = styleService.queryDimensionLabels(dto);
                /*过滤必填字段*/
                managementVoList = managementVoList.stream().filter(f -> StringUtils.equals(f.getIsMustFill(), BaseGlobal.STATUS_CLOSE) && StringUtils.isBlank(f.getVal())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(managementVoList)) {
                    String fieldName = managementVoList.stream().map(FieldManagementVo::getFieldName).collect(Collectors.joining(","));
                    throw new OtherException("大货款号：" + styleColor.getStyleNo() + "中的" + fieldName + "未填写");
                }
            }
        } else {
            updateWrapper.set("order_date", null);
        }

        updateWrapper.set("order_flag", publicStyleColorDto.getOrderFlag());
        updateWrapper.in("id", ids);
        baseMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public void updateTagPrice(String id, BigDecimal tagPrice) {
        if(StringUtils.isNotBlank(id)){
//            StyleColor styleColor = new StyleColor();
//            styleColor.setId(id);
//            styleColor.setTagPrice(tagPrice);
//            styleColor.updateInit();
//            super.updateById(styleColor);
            UpdateWrapper<StyleColor> qw = new UpdateWrapper<>();
            qw.lambda().eq(StyleColor::getId,id);
            qw.lambda().set(StyleColor::getTagPrice,tagPrice);
            qw.lambda().set(StyleColor::getUpdateId,companyUserInfo.get().getUserId());
            qw.lambda().set(StyleColor::getUpdateName,companyUserInfo.get().getAliasUserName());
            qw.lambda().set(StyleColor::getUpdateDate,new Date());
            update(qw);

            /*重新下发配色*/
            dataUpdateScmService.updateStyleColorSendById(id);
        }
    }

    @Override
    public void updateProductStyle(String id, String productStyle, String productStyleName) {
        StyleColor styleColor = new StyleColor();
        styleColor.setId(id);
        styleColor.setProductStyle(productStyle);
        styleColor.setProductStyleName(productStyleName);
        super.updateById(styleColor);
    }

    /**
     * 取消关联Bom
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean disassociateBom(PublicStyleColorDto publicStyleColorDto) {
        /*查询配色数据*/
        List<StyleColor> styleColorList = baseMapper.selectBatchIds(StringUtils.convertList(publicStyleColorDto.getId()));
        /*校验是否关联配色*/
        List<String> stringList = styleColorList.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).map(StyleColor::getBom).collect(Collectors.toList());
        if (styleColorList.size() != stringList.size()) {
            throw new OtherException("未关联款号");
        }
        /*查询BOM*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("code", stringList);
        List<PackInfo> packInfoList = packInfoService.list(queryWrapper);
        /*取消关联*/
        styleColorList.forEach(s -> {
            /*验证是否下发*/
            if (!s.getScmSendFlag().equals(BaseGlobal.NO)) {
                throw new OtherException("数据存在已下发");
            }
            s.setBom("");
        });
        /*取消关联*/
        packInfoList.forEach(p -> {
            p.setStyleNo("");
            p.setColorCode("");
            p.setColor("");
            p.setStyleColorId("");
            p.setName(p.getDesignNo() + p.getStyleName() + " BOM");
        });
        updateBatchById(styleColorList);
        packInfoService.updateBatchById(packInfoList);
        return true;
    }

    /**
     * 查询款式配色设计维度数据
     *
     * @param id 配色id
     * @return
     */
    @Override
    public List<FieldManagementVo> getStyleColorDynamicDataById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new OtherException("配色id不能为空");
        }
        StyleColor styleColor = baseMapper.selectById(id);
        Style style = styleService.getById(styleColor.getStyleId());
        DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(style, dto);
        dto.setId(styleColor.getStyleId());
        dto.setForeignId(id);
        dto.setDataGroup(FieldValDataGroupConstant.STYLE_COLOR);
        return styleService.queryDimensionLabels(dto);
    }

    @Override
    public List<FieldVal> ListDynamicDataByIds(List<String> ids) {
        if (ids==null || ids.isEmpty()){
            return new ArrayList<>();
        }
        QueryWrapper<FieldVal> fieldValQueryWrapper =new QueryWrapper<>();

        fieldValQueryWrapper.in("foreign_id", ids);
        fieldValQueryWrapper.eq("data_group",FieldValDataGroupConstant.STYLE_MARKING_ORDER);
        fieldValQueryWrapper.orderByDesc("id");
        return fieldValService.list(fieldValQueryWrapper);
    }

    /**
     * 保存配色维度数据
     *
     * @param technologyInfo
     * @return
     */
    @Override
    public Boolean saveStyleColorDynamicData(List<FieldVal> technologyInfo) {
        // 保存工艺信息
        fieldValService.save(technologyInfo.get(0).getForeignId(), FieldValDataGroupConstant.STYLE_COLOR, technologyInfo);
        /*修改后下发已下发的数据*/
        dataUpdateScmService.updateStyleColorSendById(technologyInfo.get(0).getForeignId());
        return true;
    }

    /**
     * 查询款式配色主款配饰数据
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo<StyleColorVo> getStyleMainAccessoriesList(Principal user, QueryStyleColorDto dto) {
        if (StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getIsTrim())) {
            throw new OtherException("配色id不能为空");
        }
        List<StyleMainAccessories> mainAccessoriesList = styleMainAccessoriesService.styleMainAccessoriesList(dto.getId(), null);
        List<StyleColorVo> styleColorVoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(mainAccessoriesList)) {
            List<String> styleNoList = mainAccessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("style_no", styleNoList);
            queryWrapper.eq(StringUtils.isNotBlank(dto.getStyleNo()), "style_no", dto.getStyleNo());
            queryWrapper.eq(StringUtils.isNotBlank(dto.getColorName()), "color_name", dto.getColorName());
            PageHelper.startPage(dto);
            styleColorVoList = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), StyleColorVo.class);
            /*查询款式配色图*/
            stylePicUtils.setStyleColorPic2(styleColorVoList, "styleColorPic");
        }
        PageInfo<StyleColorVo> pageInfo = new PageInfo<>(styleColorVoList);
        return pageInfo;
    }



    /**
     * 复制配色
     *
     * @param idDto
     * @return
     */
    @Override
    public Boolean copyStyleColor(IdDto idDto, Principal user) {
        StyleColor styleColor = baseMapper.selectById(idDto.getId());
        String styleNo = styleColor.getStyleNo();
        styleColor.insertInit();
        styleColor.setId(null);
        styleColor.setScmSendFlag(null);
        styleColor.setBom(null);
        styleColor.setBomStatus(BaseGlobal.NO);
        // 查询款式
        Style style = styleService.getById(styleColor.getStyleId());
        styleColor.setStyleNo(getNextCode(style, StringUtils.isNotEmpty(styleColor.getBandName()) ? styleColor.getBandName() : style.getBandName(),  StringUtils.isNotBlank(style.getOldDesignNo())?style.getOldDesignNo():style.getDesignNo() ,styleColor.getIsLuxury(),  1));
        styleColor.setHisStyleNo(null);
        styleColor.setWareCode(null);
        styleColor.setHistoricalData(BaseGlobal.NO);
        baseMapper.insert(styleColor);

        String styleColorPic = styleColor.getStyleColorPic();
        UploadStylePicDto uploadStylePicDto = new UploadStylePicDto();
        uploadStylePicDto.setStyleColorId(styleColor.getId());
        try {
            MultipartFile multipartFile = uploadFileService.downloadImage(styleColorPic, styleNo + ".jpg");
            uploadStylePicDto.setFile(multipartFile);
            Boolean uploadStatus = uploadFileService.uploadStyleImage(uploadStylePicDto, user);
            if (!uploadStatus) {
                return false;
            }
        } catch (Exception e) {
            log.error("上传图片出错，出错原因：「{}」", e.getMessage(), e);
            return false;
        }
        return true;
    }
    /**
     * 款式列表导出
     *
     * @param response
     * @param dto
     */
    @Override
    public void styleListDeriveExcel(Principal user,HttpServletResponse response, QueryStyleColorDto dto) throws IOException {
        dto.setExcelFlag(BaseGlobal.YES);
        PageInfo<StyleColorVo> pageInfo = getSampleStyleColorList(user, dto);
        List<StyleColorVo> styleColorVoList = pageInfo.getList();
        List<StyleColorListExcel> list = BeanUtil.copyToList(styleColorVoList, StyleColorListExcel.class);

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                .build();

        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(styleColorVoList) && styleColorVoList.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                stylePicUtils.setStylePic(list, "stylePic",30);
                stylePicUtils.setStylePic(list, "styleColorPic",30);
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (StyleColorListExcel styleColorListExcel : list) {
                    executor.submit(() -> {
                        try {
                            final String stylePic = styleColorListExcel.getStylePic();
                            final String styleColorPic = styleColorListExcel.getStyleColorPic();
                            styleColorListExcel.setStylePic1(HttpUtil.downloadBytes(stylePic));
                            styleColorListExcel.setStyleColorPic1(HttpUtil.downloadBytes(styleColorPic));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(list, StyleColorListExcel.class, "款式列表.xlsx", new ExportParams("款式列表", "款式列表", ExcelType.HSSF), response);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 款式配色列表导出
     *
     * @param user
     * @param response
     * @param dto
     * @throws IOException
     */
    @Override
    public void styleColorListDeriveExcel(Principal user, HttpServletResponse response, QueryStyleColorDto dto) throws IOException {
        dto.setExcelFlag(BaseGlobal.YES);
        PageInfo<StyleColorVo> pageInfo = getSampleStyleColorList(user, dto);
        List<StyleColorVo> styleColorVoList = pageInfo.getList();
        List<StyleColorExcel> list = BeanUtil.copyToList(styleColorVoList, StyleColorExcel.class);

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                .build();

        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(styleColorVoList) && styleColorVoList.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                stylePicUtils.setStylePic(list, "stylePic",30);
                stylePicUtils.setStylePic(list, "styleColorPic",30);
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (StyleColorExcel styleColorExcel : list) {
                    executor.submit(() -> {
                        try {
                            final String stylePic = styleColorExcel.getStylePic();
                            final String styleColorPic = styleColorExcel.getStyleColorPic();
                            styleColorExcel.setStylePic1(HttpUtil.downloadBytes(stylePic));
                            styleColorExcel.setStyleColorPic1(HttpUtil.downloadBytes(styleColorPic));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(list, StyleColorExcel.class, "款式配色.xlsx", new ExportParams("款式配色", "款式配色", ExcelType.HSSF), response);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public void saveCorrectBarCode(StyleColor styleColor) {
        LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColor::getCorrectBarCode, styleColor.getCorrectBarCode());
        updateWrapper.set(StyleColor::getUpdateId, getUserId());
        updateWrapper.set(StyleColor::getUpdateName, getUserName());
        updateWrapper.set(StyleColor::getUpdateDate, new Date());
        updateWrapper.eq(StyleColor::getId, styleColor.getId());
        update(updateWrapper);
    }

    @Override
    public void saveDesignDate(AddRevampStyleColorDto styleColor) {
        LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColor::getDesignDetailDate, styleColor.getDesignDetailDate());
        updateWrapper.set(StyleColor::getDesignCorrectDate, styleColor.getDesignCorrectDate());
        updateWrapper.set(StyleColor::getUpdateId, getUserId());
        updateWrapper.set(StyleColor::getUpdateName, getUserName());
        updateWrapper.set(StyleColor::getUpdateDate, new Date());
        updateWrapper.eq(StyleColor::getId, styleColor.getId());
        update(updateWrapper);
    }

    @Override
    public List<StyleNoUserInfoVo> getDesignerInfo(String styleNo) {
        return baseMapper.getStyleDesignerInfo(Arrays.asList(styleNo.split(",")));
    }

    @Override
    public void updateStyleColorOverdueReason(StyleColorOverdueReasonDto styleColorOverdueReasonDto) {
        LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
        if ("0".equals(styleColorOverdueReasonDto.getType())) {
            updateWrapper.set(StyleColor::getSendMainFabricOverdueReason, styleColorOverdueReasonDto.getRemark());
        } else if ("1".equals(styleColorOverdueReasonDto.getType())) {
            updateWrapper.set(StyleColor::getDesignDetailOverdueReason, styleColorOverdueReasonDto.getRemark());
        } else if ("2".equals(styleColorOverdueReasonDto.getType())) {
            updateWrapper.set(StyleColor::getDesignCorrectOverdueReason, styleColorOverdueReasonDto.getRemark());
        }else{
            throw  new OtherException("类型不正确");
        }
        updateWrapper.eq(StyleColor::getId,styleColorOverdueReasonDto.getId());
        this.update(updateWrapper);
    }

    /**
     * 查询已下单的配色
     *
     * @param dto
     * @return
     */
    @Override
    public  PageInfo<StyleColorVo> getByStyleList(StyleColorsDto dto) {
        FieldManagement fieldManagement = fieldManagementService.getById(dto.getDimensionLabelId());
        if (fieldManagement == null) {
            throw new OtherException("维度信息为空");
        }
        // 查询坑位所有已经匹配的大货款号
        QueryWrapper<PlanningProjectPlank> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("bulk_style_no");
        queryWrapper1.isNotNull("bulk_style_no");
        queryWrapper1.last("and bulk_style_no != ''");
        List<PlanningProjectPlank> list = planningProjectPlankService.list(queryWrapper1);

        if (GeneralConstant.FIXED_ATTRIBUTES.equals(fieldManagement.getGroupName()) && GeneralConstant.PRODCATEGORY_2ND_CODE.equals(fieldManagement.getFieldExplain())) {
            // 如果是 固定属性-中类 那么不使用动态字段进行匹配
            BaseQueryWrapper<StyleColor> styleQueryWrapper = new BaseQueryWrapper<>();
            styleQueryWrapper.eq("ts.planning_season_id", dto.getSeasonId());
            styleQueryWrapper.eq("ts.prod_category1st", dto.getProdCategory1st());
            styleQueryWrapper.notEmptyEq("ts.prod_category2nd", dto.getProdCategory2nd());
            styleQueryWrapper.eq("ts.prod_category", dto.getProdCategory());
            if (StringUtils.isNotBlank(dto.getSearch())) {
                styleQueryWrapper.and(wrapper -> {
                    wrapper.like("tsc.style_no", dto.getSearch());
                    wrapper.or().like("ts.design_no", dto.getSearch());
                });
            }
            if (!list.isEmpty()) {
                List<String> bulkStyleNoList = list.stream().map(PlanningProjectPlank::getBulkStyleNo).collect(Collectors.toList());
                styleQueryWrapper.notIn("tsc.style_no", bulkStyleNoList);
            }
            PageHelper.startPage(dto);
            List<StyleColorVo> styleList = stylePricingMapper.getByStyleList(styleQueryWrapper, dto);
            return new PageInfo<>(styleList);
        } else {
            QueryWrapper<FieldVal> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("field_name", fieldManagement.getFieldName());
            queryWrapper.eq("data_group", FieldValDataGroupConstant.STYLE_MARKING_ORDER);
            queryWrapper.select("foreign_id");

            List<FieldVal> fieldValList = fieldValService.list(queryWrapper);
            List<String> styleColorIds = fieldValList.stream().map(FieldVal::getForeignId).filter(StrUtil::isNotBlank).collect(Collectors.toList());

            // 查询已下单的配色id
            QueryWrapper<OrderBookDetail> queryWrapper2 = new QueryWrapper<>();
            if (styleColorIds.isEmpty()) {
                return new PageInfo<>(new ArrayList<>());
            }
            queryWrapper2.in("style_color_id", styleColorIds);
            queryWrapper2.select("style_color_id");
            List<OrderBookDetail> list1 = orderBookDetailService.list(queryWrapper2);
            List<String> list2 = list1.stream().map(OrderBookDetail::getStyleColorId).collect(Collectors.toList());
            if (CollUtil.isEmpty(list2)) return new PageInfo<>();

            BaseQueryWrapper<StyleColor> styleQueryWrapper = new BaseQueryWrapper<>();
            styleQueryWrapper.eq("ts.planning_season_id", dto.getSeasonId());
            styleQueryWrapper.eq("ts.prod_category1st", dto.getProdCategory1st());
            styleQueryWrapper.notEmptyEq("ts.prod_category2nd", dto.getProdCategory2nd());
            styleQueryWrapper.eq("ts.prod_category", dto.getProdCategory());
            styleQueryWrapper.in("tsc.id", list2);
            if (StringUtils.isNotBlank(dto.getSearch())) {
                styleQueryWrapper.and(wrapper -> {
                    wrapper.like("tsc.style_no", dto.getSearch());
                    wrapper.or().like("ts.design_no", dto.getSearch());
                });
            }
            if (!list.isEmpty()) {
                List<String> bulkStyleNoList = list.stream().map(PlanningProjectPlank::getBulkStyleNo).collect(Collectors.toList());
                styleQueryWrapper.notIn("tsc.style_no", bulkStyleNoList);
            }
            PageHelper.startPage(dto);
            List<StyleColorVo> styleList = stylePricingMapper.getByStyleList(styleQueryWrapper, null);


            return new PageInfo<>(styleList);
        }
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

    /**
     * 大货款号改名先下载图片上传，然后删除原图片
     * @param user
     * @param updateStyleNoBandDto
     * @param sampleStyleColor
     * @param styleColor
     */
    private Boolean uploadImgAndDeleteOldImg(Principal user, UpdateStyleNoBandDto updateStyleNoBandDto, StyleColor sampleStyleColor, StyleColor styleColor) {
        Boolean result = true;
        String styleColorPic = sampleStyleColor.getStyleColorPic();
        if(StringUtils.isNotBlank(updateStyleNoBandDto.getStyleNo())){
            UploadStylePicDto uploadStylePicDto = new UploadStylePicDto();
            uploadStylePicDto.setStyleColorId(updateStyleNoBandDto.getId());
            MultipartFile multipartFile = null;
            Boolean uploadStatus = false;
            try {
                multipartFile = uploadFileService.downloadImage(styleColorPic, updateStyleNoBandDto.getStyleNo() + ".jpg");
                uploadStylePicDto.setFile(multipartFile);
                uploadStatus = uploadFileService.uploadStyleImage(uploadStylePicDto, user);
            } catch (Exception e) {
                result = false;
                log.info(e.getMessage());
//                throw new RuntimeException(e);
            }finally {
                //上传成功后删除
                if (uploadStatus) {
                    DelStylePicDto delStylePicDto = new DelStylePicDto();
                    delStylePicDto.setStyleColorId(styleColor.getId());
                    delStylePicDto.setStyleId(sampleStyleColor.getStyleId());
                    uploadFileService.delStyleColorImage(delStylePicDto, user,styleColorPic,"0");
                    uploadFileService.delStyleColorImage(delStylePicDto, user,styleColorPic,"1");
                }
            }
        }
        return result;
    }

    @Override
    public void markingDeriveExcel(Principal user, HttpServletResponse response, QueryStyleColorDto dto) {
        dto.setExcelFlag(BaseGlobal.YES);
        PageInfo<StyleColorVo> pageInfo = getSampleStyleColorList(user, dto);
        List<StyleColorVo> styleColorVoList = pageInfo.getList();

        //根据查询出维度系数数据
        LambdaQueryWrapper<FieldVal> fieldValQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(dto.getMarkingOrderFlag())){
            List<String> styleColorIdList = styleColorVoList.stream().map(StyleColorVo::getId).distinct().collect(Collectors.toList());
            fieldValQueryWrapper.in(FieldVal::getForeignId,styleColorIdList);
            fieldValQueryWrapper.eq(FieldVal::getDataGroup,FieldValDataGroupConstant.STYLE_MARKING_ORDER);
        }else{
            List<String> styleIdList = styleColorVoList.stream().map(StyleColorVo::getStyleId).distinct().collect(Collectors.toList());
            fieldValQueryWrapper.in(FieldVal::getForeignId,styleIdList);
            fieldValQueryWrapper.eq(FieldVal::getDataGroup,FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        }
        List<FieldVal> fieldValList = fieldValService.list(fieldValQueryWrapper);
        Map<String, List<FieldVal>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId));

        List<Map<String,Object>> dataList = new ArrayList<>();
        for (StyleColorVo styleColorVo : styleColorVoList) {
            Map<String, Object> dataMap = JSONObject.parseObject(JSONObject.toJSONString(styleColorVo), Map.class);

            List<FieldVal> fieldValList1 = new ArrayList<>();
            if (StringUtils.isNotBlank(dto.getMarkingOrderFlag())) {
                if (fieldValMap.containsKey(styleColorVo.getId())) {
                    fieldValList1 = fieldValMap.get(styleColorVo.getId());
                }
            } else {
                if (fieldValMap.containsKey(styleColorVo.getStyleId())) {
                    fieldValList1 = fieldValMap.get(styleColorVo.getStyleId());
                }
            }

            for (FieldVal fieldVal : fieldValList1) {
                String fieldName = fieldVal.getFieldName();
                String val = StrUtil.isNotBlank(fieldVal.getValName()) ? fieldVal.getValName() : fieldVal.getVal();
                dataMap.put(fieldName,val);
            }

            dataList.add(dataMap);
        }

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(styleColorVoList.size()))
                .build();

        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                CountDownLatch countDownLatch = new CountDownLatch(styleColorVoList.size());
                for (Map<String, Object> styleColorVo : dataList) {
                    executor.submit(() -> {
                        try {
                            if(styleColorVo.get("stylePic") != null){
                                final String stylePic = styleColorVo.get("stylePic").toString();
                                styleColorVo.put("stylePic1",HttpUtil.downloadBytes(stylePic));
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }

            String type = "款式打标设计阶段";
            if(StringUtils.isNotBlank(dto.getMarkingOrderFlag())){
                type = "款式打标下单阶段";
            }

            //组装excel头部
            List<ExcelExportEntity> entityList = new ArrayList<>();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String tableCode = requestAttributes.getRequest().getHeader("tableCode");
            List<ColumnDefine> defaultDetail = SpringUtil.getBean(ColumnUserDefineService.class).findDefaultDetail(tableCode);
            for (ColumnDefine columnDefine : defaultDetail) {
                if(BaseGlobal.NO.equals(columnDefine.getHidden())){
                    continue;
                }
                ExcelExportEntity entity = new ExcelExportEntity();
                entity.setKey(columnDefine.getColumnCode());
                entity.setName(columnDefine.getColumnName());
                if(StrUtil.equals("img",columnDefine.getColumnType())){
                    entity.setExportImageType(2);
                    entity.setType(2);
                }
                entity.setOrderNum(columnDefine.getSortOrder());
                entityList.add(entity);
            }

            ExcelUtils.defaultExport(dataList, type+".xlsx", response,new ExportParams(type, type, ExcelType.HSSF),entityList);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public PageInfo<StyleMarkingCheckVo> markingCheckPage(QueryStyleColorDto queryDto) {
        /*分页*/
        Page<Object> objects = PageHelper.startPage(queryDto);
        BaseQueryWrapper queryWrapper = getBaseQueryWrapper(queryDto);

        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.markingCheckPage.getK(), "ts.");

        List<StyleMarkingCheckVo> styleMarkingCheckVos = baseMapper.markingCheckPage(queryWrapper);
        return new PageInfo<>(styleMarkingCheckVos);
    }

    @Override
    public PageInfo<StyleColorAgentVo> agentPageList(QueryStyleColorAgentDto queryDto) {
        Page<Object> objects = PageHelper.startPage(queryDto);
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        queryWrapper.notEmptyLikeOrIsNull("ts.year_name", queryDto.getYearName());
        queryWrapper.notEmptyEqOrIsNull("ts.prod_category_name", queryDto.getProdCategoryName());
        queryWrapper.notEmptyEqOrIsNull("ts.prod_category", queryDto.getProdCategory());
        if(StringUtils.isNotBlank(queryDto.getStyleNo())){
            queryWrapper.likeList("tsc.style_no", StringUtils.convertList(queryDto.getStyleNo()));
        }
        if(StringUtils.isNotBlank(queryDto.getDesignNo())){
            queryWrapper.likeList("ts.design_no",StringUtils.convertList(queryDto.getDesignNo()));
        }
        queryWrapper.notEmptyIn("tsca.status",queryDto.getSendStatus());

        queryWrapper.eq("ts.brand_name","MANGO");
        queryWrapper.eq("tsca.del_flag","0");

        if ("1".equals(queryDto.getUploadImageFlag())) {
            queryWrapper.isNotNullStr("tsc.style_color_pic");
        } else if ("0".equals(queryDto.getUploadImageFlag())) {
            queryWrapper.isNullStr("tsc.style_color_pic");
        }

        objects.setOrderBy("tsc.create_date desc,tsc.style_no,tsca.size_id");

        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.styleAgent.getK(), "ts.");

        List<StyleColorAgentVo> list = baseMapper.agentList(queryWrapper);
        if(StrUtil.isBlank(queryDto.getExcelFlag()) || !"1".equals(queryDto.getExcelFlag())){
            stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        }
        return new PageInfo<>(list);
    }

    @Override
    public List<String> agentStyleNoList() {
        QueryWrapper<StyleColorAgent> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag","0");
        queryWrapper.ne("status","1");
        queryWrapper.select(" DISTINCT style_color_no ");
        List<String> objects =(List<String>)(List) styleColorAgentService.listObjs(queryWrapper);
        return objects;
    }

    @Override
    public List<TagPrinting> agentListByStyleNo(String styleNo, boolean likeQueryFlag) {
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        if (likeQueryFlag) {
            queryWrapper.eq("tsc.style_no", styleNo);
        } else {
            queryWrapper.like("tsc.style_no", styleNo);
        }

        queryWrapper.eq("ts.brand_name", "MANGO");
        queryWrapper.eq("tsca.del_flag","0");
        queryWrapper.last("order by tsc.create_date desc,tsc.style_no,tsca.size_id");

        List<StyleColorAgentVo> list = baseMapper.agentList(queryWrapper);

        Map<String, List<StyleColorAgentVo>> collect = list.stream().collect(Collectors.groupingBy(o -> o.getStyleNo() + o.getColorCode()));

        List<TagPrinting> tagPrintings = new ArrayList<>();
        for (Map.Entry<String, List<StyleColorAgentVo>> entry : collect.entrySet()) {
            List<StyleColorAgentVo> value = entry.getValue();
            StyleColorAgentVo styleColorAgentVo1 = value.get(0);

            TagPrinting tagPrinting = new TagPrinting();
            tagPrinting.setStyleCode(styleColorAgentVo1.getStyleNo());
            tagPrinting.setColorCode(styleColorAgentVo1.getOutsideColorCode());
            tagPrinting.setColorDescription(styleColorAgentVo1.getOutsideColorName());

            List<TagPrinting.Size> sizes = new ArrayList<>();
            for (StyleColorAgentVo styleColorAgentVo : value) {
                TagPrinting.Size size = new TagPrinting.Size();
                size.setSORTCODE(styleColorAgentVo.getOutsideSizeCode());
                size.setOutsideBarcode(styleColorAgentVo.getOutsideBarcode());
                size.setEXTSIZECODE(styleColorAgentVo.getHangtags());
                size.setSIZECODE(styleColorAgentVo.getInternalSize());
                sizes.add(size);
            }
            tagPrinting.setSize(sizes);
            //品牌
            tagPrinting.setC8_Season_Brand(styleColorAgentVo1.getBrand());
            //品名
            tagPrinting.setProductName(styleColorAgentVo1.getProductName());
            //质量等级
            tagPrinting.setQualityClass(styleColorAgentVo1.getQualityGrade());
            //执行标准
            tagPrinting.setOPStandard(styleColorAgentVo1.getExecuteStandard());
            //安全技术类别
            tagPrinting.setSaftyType(styleColorAgentVo1.getSaftyType());
            //成分
            tagPrinting.setComposition(styleColorAgentVo1.getIngredient());
            //产地
            tagPrinting.setC8_APPBOM_MadeIn(styleColorAgentVo1.getProducer());
            //水洗标路径
            tagPrinting.setCareSymbols(styleColorAgentVo1.getWashingCode());
            //吊牌价
            tagPrinting.setC8_Colorway_SalesPrice(StrUtil.isNotEmpty(styleColorAgentVo1.getTagPrice()) ? new BigDecimal(styleColorAgentVo1.getTagPrice()) : null);
            //品名翻译
            tagPrinting.setProductNameTranslate(styleColorAgentVo1.getProductNameTranslate());
            //颜色code翻译
            tagPrinting.setColorCodeTranslate(styleColorAgentVo1.getColorCodeTranslate());

            tagPrintings.add(tagPrinting);
        }

        //大货款号+供应商颜色编码+外部尺码code+供应商条码
        return tagPrintings;
    }

    @Transactional
    @Override
    public void agentDelete(String id) {
        //状态校验
        StyleColorAgent styleColorAgent = styleColorAgentService.getById(id);
        if (!"0".equals(styleColorAgent.getStatus())) {
            throw new OtherException("只有未下发时才能删除");
        }
        QueryWrapper<StyleColorAgent> styleColorAgentQueryWrapper = new QueryWrapper<>();
        styleColorAgentQueryWrapper.eq("style_color_id",styleColorAgent.getStyleColorId());
        styleColorAgentQueryWrapper.eq("del_flag",0);
        List<StyleColorAgent> styleColorAgentList = styleColorAgentService.list(styleColorAgentQueryWrapper);
        if (CollUtil.isNotEmpty(styleColorAgentList)) {
            for (StyleColorAgent colorAgent : styleColorAgentList) {
                String status = colorAgent.getStatus();
                if (!"0".equals(status)) {
                    throw new OtherException("存在已下发到下游系统数据，不允许删除，只允许导入修改！");
                }
            }
        }


        String styleColorId = styleColorAgent.getStyleColorId();
        StyleColor styleColor = this.getById(styleColorId);
        if (styleColor != null) {
            //删除配色
            this.removeById(styleColorId);
            String styleId = styleColor.getStyleId();
            //删除设计款
            styleService.removeById(styleId);
            QueryWrapper<PackInfo> packInfoQueryWrapper = new QueryWrapper<>();
            packInfoQueryWrapper.eq("foreign_id", styleId);
            packInfoQueryWrapper.eq("del_flag", 0);
            packInfoQueryWrapper.last(" limit 1 ");
            PackInfo packInfoUpdate = packInfoService.getOne(packInfoQueryWrapper);
            if (packInfoUpdate != null) {
                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfoUpdate.getId(), baseController.getUserCompany());
                if (stylePricingVO != null) {
                    //删除款式定价信息
                    stylePricingService.removeById(stylePricingVO.getStylePricingId());
                }
                //删除大货资料包信息
                packInfoService.removeById(packInfoUpdate.getId());
            }

            QueryWrapper<HangTag> hangTagQueryWrapper = new QueryWrapper<>();
            hangTagQueryWrapper.eq("bulk_style_no", styleColor.getStyleNo());
            //删除款式定价
            hangTagService.remove(hangTagQueryWrapper);
        }

        styleColorAgentService.remove(new QueryWrapper<StyleColorAgent>().eq("style_color_id", styleColorId).eq("del_flag", 0));
    }

    @Override
    public void agentStop(String id) {
        //状态校验
        StyleColorAgent byId = styleColorAgentService.getById(id);
        if(!"2".equals(byId.getStatus()) && !"3".equals(byId.getStatus())) {
            throw new OtherException("只有重新打开或可编辑时才能停用");
        }
        StyleColor styleColor = styleColorService.getById(byId.getStyleColorId());
        String styleColorId = styleColor.getStyleId();
        if("1".equals(styleColor.getStatus())){
            throw new OtherException("该款已经停用");
        }

        LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColor::getStatus,"1");
        updateWrapper.eq(StyleColor::getId,styleColorId);
        styleColorService.update(updateWrapper);

        //同步下游系统
        smpService.goodsAgent(new String[]{styleColor.getId()},true);
    }

    @Override
    public void agentUnlock(String[] ids) {
        //状态校验
        List<StyleColorAgent> list = styleColorAgentService.listByField("style_color_id",Arrays.asList(ids));
        for (StyleColorAgent styleColorAgent : list) {
            if(!"1".equals(styleColorAgent.getStatus())){
                throw new OtherException("只有已下发时才能解锁");
            }
        }

        LambdaUpdateWrapper<StyleColorAgent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColorAgent::getStatus,"2");
        updateWrapper.in(StyleColorAgent::getStyleColorId, Arrays.asList(ids));
        styleColorAgentService.update(updateWrapper);
    }

    @Override
    public void agentEnable(String id) {
        //状态校验
        StyleColorAgent byId = styleColorAgentService.getById(id);
        if(!"2".equals(byId.getStatus()) && !"3".equals(byId.getStatus())) {
            throw new OtherException("只有重新打开或可编辑时才能启用");
        }
        StyleColor styleColor = styleColorService.getById(byId.getStyleColorId());
        String styleColorId = styleColor.getId();
        if("0".equals(styleColor.getStatus())){
            throw new OtherException("该款已经启用");
        }

        LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColor::getStatus,"0");
        updateWrapper.eq(StyleColor::getId,styleColorId);
        styleColorService.update(updateWrapper);

        //同步下游系统
        smpService.goodsAgent(new String[]{styleColor.getId()},true);
    }

    @Override
    public void exportAgentExcel(HttpServletResponse response, QueryStyleColorAgentDto dto) {


        dto.setPageSize(Integer.MAX_VALUE);
        dto.setExcelFlag("1");
        PageInfo<StyleColorAgentVo> styleColorAgentVoPageInfo = this.agentPageList(dto);
        List<StyleColorAgentVo> styleColorAgentVoList = styleColorAgentVoPageInfo.getList();

        List<MangoStyleColorExeclExportDto> list = BeanUtil.copyToList(styleColorAgentVoList, MangoStyleColorExeclExportDto.class);

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                .build();

        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(styleColorAgentVoList) && styleColorAgentVoList.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                stylePicUtils.setStylePic(list, "styleColorPic",30);
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (MangoStyleColorExeclExportDto mangoStyleColorExeclDto : list) {
                    executor.submit(() -> {
                        try {
                            final String styleColorPic = mangoStyleColorExeclDto.getStyleColorPic();
                            mangoStyleColorExeclDto.setStyleColorPic1(HttpUtil.downloadBytes(styleColorPic));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(list, MangoStyleColorExeclExportDto.class, "代理货品资料导出.xlsx", new ExportParams("代理货品资料导出", "代理货品资料导出", ExcelType.HSSF), response);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public ApiResult agentSync(String[] ids) {
        int i = smpService.goodsAgent(ids,true);

        if (ids.length== i) {
            return ApiResult.success("下发：" + ids.length + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + ids.length + "条，成功：" + i + "条,失败：" + (ids.length - i) + "条", 200);
        }
    }

    @Transactional
    @Override
    public ApiResult mangoExeclImport(List<MangoStyleColorExeclDto> list,Boolean isUpdate) {

        String errorInfo = "";
        //验证数据是否为空或者重复数据
        errorInfo = checkData1(list, errorInfo);

        if (StrUtil.isNotEmpty(errorInfo)) {
            return ApiResult.error(errorInfo,500);
        }

        //
        List<BasicBaseDict> styleTypeList = ccmFeignService.getDictInfoToList("StyleType");
        List<BasicBaseDict> c8GenderList = ccmFeignService.getDictInfoToList("C8_Gender");
        List<BasicBaseDict> packageTypeList = ccmFeignService.getDictInfoToList("C8_PackageType");



        //验证导入数据和数据库数据做校验
        errorInfo = checkData2(list, errorInfo);


        Map<String,String> checkMap = new HashMap<>();

        List<BasicStructureTreeVo> tree = ccmFeignService.basicStructureTreeByCode("品类", null, "0,1,2");
        if (CollUtil.isNotEmpty(tree)) {
            for (BasicStructureTreeVo basicStructureTreeVo : tree) {
                String bigName = basicStructureTreeVo.getName();
                String bigValue = basicStructureTreeVo.getValue();
                List<BasicStructureTreeVo> children = basicStructureTreeVo.getChildren();
                if (CollUtil.isNotEmpty(children)) {
                    for (BasicStructureTreeVo category : children) {
                        String catagory = category.getName();
                        String catagoryValue = category.getValue();

                        List<BasicStructureTreeVo> categoryChildren = category.getChildren();
                        for (BasicStructureTreeVo middleCatagory : categoryChildren) {
                            String  middleCatagoryName = middleCatagory.getName();
                            String  middleCatagoryValue = middleCatagory.getValue();
                            checkMap.put(bigName + "-" + catagory + "-" + middleCatagoryName,bigValue + "-" + catagoryValue + "-" + middleCatagoryValue);
                        }
                    }
                }
            }
        }

        if (StrUtil.isNotEmpty(errorInfo)) {
            return ApiResult.error(errorInfo,500);
        }

        Style style;
        StyleColor styleColor;
        StyleColorAgent styleColorAgent;
        StylePricing stylePricing;
        PackInfo packInfo;
        HangTag hangTag;


        List<Style> insertStyleList = new ArrayList<>();
        List<Style> updateStyleList = new ArrayList<>();
        List<StyleColor> insertStyleColorList = new ArrayList<>();
        List<StyleColor> updateStyleColorList = new ArrayList<>();
        List<StyleColorAgent> insertStyleColorAgentList = new ArrayList<>();
        List<StyleColorAgent> updateStyleColorAgentList = new ArrayList<>();

        List<PackInfo> insertPackInfoList = new ArrayList<>();
        List<StylePricing> insertStylePricingList = new ArrayList<>();
        List<StylePricing> updateStylePricingList = new ArrayList<>();

        List<HangTag> insertHangTagList = new ArrayList<>();
        List<HangTag> updateHangTagList = new ArrayList<>();

        Map<String,String> styleColorMap = new HashMap<>();
        Map<String,String> updateStyleColorMap = new HashMap<>();

        //导入 新增 和修改
        for (int i = 0; i < list.size(); i++) {
            MangoStyleColorExeclDto dto = list.get(i);
            style = new Style();
            styleColor = new StyleColor();
            styleColorAgent = new StyleColorAgent();
            stylePricing = new StylePricing();
            packInfo = new PackInfo();
            hangTag = new HangTag();

            String styleColorNo = dto.getStyleColorNo();
            String outsideSizeCode = dto.getOutsideSizeCode();

            QueryWrapper styleColorAgentQueryWrapper = new QueryWrapper<StyleColorAgent>();
            styleColorAgentQueryWrapper.eq("style_color_no",styleColorNo);
            styleColorAgentQueryWrapper.eq("outside_size_code",outsideSizeCode);
            styleColorAgentQueryWrapper.eq("del_flag",0);
            styleColorAgentQueryWrapper.last("limit 1");
            StyleColorAgent validStyleColorAgent = styleColorAgentService.getOne(styleColorAgentQueryWrapper);

            //如果存在做更新，不存在则新增
            if (validStyleColorAgent == null) {
                String year = dto.getYear();
                String season = dto.getSeason();
                if (season.equals("春")) {
                    season= "S";
                }else if(season.equals("夏")){
                    season= "X";
                }else if(season.equals("秋")){
                    season= "A";
                }else if(season.equals("冬")){
                    season= "W";
                }
                String brandName = dto.getBrandName();
                String productSeason =year+" "+season+" "+brandName;
                //根据产品季名称获取产品季id
                PlanningSeason planningSeason = planningSeasonService.getByName(productSeason, baseController.getUserCompany());
                if (planningSeason == null) {
                    throw new OtherException("第"+(i+1)+"行,找不到产品季信息");
                }
                style.setPlanningSeasonId(planningSeason.getId());
                style.setYear(planningSeason.getYear());
                style.setYearName(planningSeason.getYearName());
                style.setBrand(planningSeason.getBrand());
                style.setBrandName(planningSeason.getBrandName());
                style.setSeason(planningSeason.getSeason());
                style.setSeasonName(planningSeason.getSeasonName());




                //坑位信息id 缺失
                style.setCompanyCode(baseController.getUserCompany());
                //坑位信息暂时默认一个
                style.setPlanningCategoryItemId("11111111");

                //款式类型
                List<BasicBaseDict> styleTypeListDictList = getBasicBaseDicts(styleTypeList,dto.getStyleTypeName());

                if (CollUtil.isNotEmpty(styleTypeListDictList)) {
                    style.setStyleType(styleTypeListDictList.get(0).getValue());
                    style.setStyleTypeName(styleTypeListDictList.get(0).getName());
                } else {
                    throw new OtherException("第"+(i+1)+"行,找不到对应的款式类型");
                }

                //款式性别
                if (StrUtil.isEmpty(dto.getGender()) || StrUtil.isBlank(dto.getGender())) {
                    style.setGenderType("");
                    style.setGenderName("");
                }else{


                    List<BasicBaseDict> c8GenderListDictList = getBasicBaseDicts(c8GenderList,dto.getGender());
                    if (CollUtil.isNotEmpty(c8GenderListDictList)) {
                        style.setGenderType(c8GenderListDictList.get(0).getValue());
                        style.setGenderName(c8GenderListDictList.get(0).getName());
                    } else {
                        throw new OtherException("第"+(i+1)+"行,找不到对应的产品季式性别");
                    }
                }


                //品类
                String prodCategory1stName = dto.getProdCategory1stName();
                String prodCategoryName = dto.getProdCategoryName();
                String prodCategory2ndName = dto.getProdCategory2ndName();

                String key = prodCategory1stName + "-" + prodCategoryName + "-" + prodCategory2ndName;
                if (checkMap.containsKey(key)) {
                    String mapValue = checkMap.get(key);
                    if (StrUtil.isNotEmpty(mapValue)) {
                        style.setProdCategory1stName(prodCategory1stName);
                        style.setProdCategory1st(mapValue.split("-")[0]);
                        style.setProdCategoryName(prodCategoryName);
                        style.setProdCategory(mapValue.split("-")[1]);
                        style.setProdCategory2ndName(prodCategory2ndName);
                        style.setProdCategory2nd(mapValue.split("-")[2]);
                    }else{
                        throw new OtherException("第" + (i + 1) + "行,系统找不到对应的大类-品类-中类" + key);
                    }
                }else{
                    throw new OtherException("第" + (i + 1) + "行,Execl中的大类-品类-中类在系统中找不到" + key);
                }

                if ("裙子".equals(prodCategoryName) || "裤子".equals(prodCategoryName) || "裤子".equals(prodCategoryName)) {
                    style.setStyleUnitCode("T");
                    style.setStyleUnit("条");
                }else{
                    style.setStyleUnitCode("J");
                    style.setStyleUnit("件");
                }

                //配色

                String outsideColorCode = dto.getOutsideColorCode();

                styleColor.setStyleNo(styleColorNo);
                QueryWrapper<BasicsdatumColourLibraryAgent>  agentQueryWrapper = new QueryWrapper<>();
                agentQueryWrapper.eq("colour_code",outsideColorCode);
                agentQueryWrapper.eq("del_flag","0");
                agentQueryWrapper.eq("status","0");
                agentQueryWrapper.eq("brand",planningSeason.getBrand());

                BasicsdatumColourLibraryAgent colourLibraryAgent = colourLibraryAgentService.getOne(agentQueryWrapper);

                if (colourLibraryAgent != null) {
                    String colorId = colourLibraryAgent.getColorId();
                    styleColorAgent.setOutsideColorName(colourLibraryAgent.getColourName());
                    BasicsdatumColourLibrary basicsdatumColour = basicsdatumColourLibraryServicel.getById(colorId);
                    if (basicsdatumColour != null) {
                        styleColor.setColourLibraryId(basicsdatumColour.getId());
                        styleColor.setColorCode(basicsdatumColour.getColourCode());
                        styleColor.setColorName(basicsdatumColour.getColourName());
                    }else{
                        throw new OtherException("第"+(i+1)+"行,找不到对应的集团颜色名称或编码，确认外部颜色是否关联集团颜色");
                    }
                }else{
                    throw new OtherException("第"+(i+1)+"行,找不到外部颜色码映射内部颜色码数据");
                }

                //设计款号
                //styleColor.setDesignNo();
                styleColor.setBandCode("11");
                styleColor.setBandName("1A");
                style.setBandCode("11");
                style.setBandName("1A");
                styleColor.setTagPrice(new BigDecimal(dto.getTagPrice()));


                styleColor.setDevtType(ProductionType.SALE);
                styleColor.setDevtTypeName(ProductionType.SALE.getText());
                style.setDevtType("999");
                style.setDevtTypeName("代销");

                styleColor.setProductName(style.getProdCategoryName());
                styleColor.setProductCode(style.getProdCategory());
                styleColor.setBomStatus("1");

                QueryWrapper basicsdatumModelTypeQueryWrapper = new QueryWrapper<BasicsdatumModelType>();
                basicsdatumModelTypeQueryWrapper.eq("model_type", dto.getSizeRangeName());
                basicsdatumModelTypeQueryWrapper.eq("status", "0");
                basicsdatumModelTypeQueryWrapper.eq("del_flag", "0");
                basicsdatumModelTypeQueryWrapper.like("brand", planningSeason.getBrand());
                basicsdatumModelTypeQueryWrapper.last(" limit 1");

                BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeService.getOne(basicsdatumModelTypeQueryWrapper);



                if (basicsdatumModelType != null) {
                    String outsideBarcode = dto.getOutsideBarcode();
                    styleColorAgent.setStyleColorId(styleColor.getId());
                    styleColorAgent.setStyleColorNo(styleColor.getStyleNo());
                    String code = basicsdatumModelType.getCode();
                    String sizes = basicsdatumModelType.getSize();
                    String sizeCodes = basicsdatumModelType.getSizeCode();

                    QueryWrapper<BasicsdatumSize> basicsdatumSizeQueryWrapper = new QueryWrapper();
                    basicsdatumSizeQueryWrapper.like("model_type_code",code);
                    basicsdatumSizeQueryWrapper.like("model_type",dto.getSizeRangeName());
                    basicsdatumSizeQueryWrapper.eq("european_size",outsideSizeCode);

                    List<BasicsdatumSize> basicsdatumSizeList = basicsdatumSizeService.list(basicsdatumSizeQueryWrapper);
                    if (CollUtil.isNotEmpty(basicsdatumSizeList)) {
                        for (BasicsdatumSize basicsdatumSize : basicsdatumSizeList) {
                            if (outsideSizeCode.equals(basicsdatumSize.getEuropeanSize())) {
                                styleColorAgent.setSizeId(basicsdatumSize.getCode());
                                styleColorAgent.setSizeCode(basicsdatumSize.getModel());
                                style.setSizeRange(basicsdatumModelType.getCode());
                                style.setSizeRangeName(dto.getSizeRangeName());
                                style.setDefaultSize(basicsdatumModelType.getBasicsSize());
                                style.setSizeIds(basicsdatumModelType.getSizeIds());
                                style.setSizeCodes(sizeCodes);
                                style.setProductSizes(sizes);
                                if (!sizes.contains(basicsdatumSize.getModel())) {
                                    throw new OtherException("第" + (i + 1) + "行,【" + dto.getSizeRangeName() + "】找不到对应的【" + basicsdatumSize.getModel() + "】信息");
                                }
                            }
                        }
                    }

                    styleColorAgent.setOutsideBarcode(outsideBarcode);
                    styleColorAgent.setOutsideColorCode(dto.getOutsideColorCode());

                    styleColorAgent.setOutsideSizeCode(dto.getOutsideSizeCode());
                } else {
                    throw new OtherException("第"+(i+1)+"行,找不到对应的号型类型");
                }


                String packagingForm = dto.getPackagingForm();
                if (StrUtil.isNotEmpty(packagingForm)) {
                    List<BasicBaseDict> packageTypeListDict = getBasicBaseDicts(packageTypeList,packagingForm);
                    if (CollUtil.isNotEmpty(packageTypeListDict)) {
                        hangTag.setPackagingForm(packageTypeListDict.get(0).getName());
                        hangTag.setPackagingFormCode(packageTypeListDict.get(0).getValue());
                    }else{
                        throw new OtherException("第" + (i + 1) + "行,【" + packagingForm + "】找不到对应的包装类型");
                    }
                }


                if (!styleColorMap.containsKey(styleColorNo)) {
                    QueryWrapper styleColorAgentExitQueryWrapper = new QueryWrapper<StyleColorAgent>();
                    styleColorAgentExitQueryWrapper.eq("style_color_no",styleColorNo);
                    styleColorAgentExitQueryWrapper.eq("del_flag",0);
                    styleColorAgentExitQueryWrapper.last("limit 1");
                    StyleColorAgent validUpdateStyleColorAgent = styleColorAgentService.getOne(styleColorAgentExitQueryWrapper);
                    if (validUpdateStyleColorAgent != null) {
                        styleColorAgent.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        styleColorAgent.setStyleColorId(validUpdateStyleColorAgent.getStyleColorId());
                        styleColorAgent.setStyleColorNo(validUpdateStyleColorAgent.getStyleColorNo());
                    }else{
                        style.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        insertStyleList.add(style);
                        styleColor.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        styleColor.setStyleId(style.getId());
                        styleColor.setPlanningSeasonId(style.getPlanningSeasonId());
                        insertStyleColorList.add(styleColor);
                        styleColorMap.put(styleColorNo,styleColor.getId());
                        styleColorAgent.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        styleColorAgent.setStyleColorId(styleColor.getId());
                        styleColorAgent.setStyleColorNo(styleColor.getStyleNo());
                        //资料包
                        packInfo.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        packInfo.setForeignId(style.getId());
                        packInfo.setPlanningSeasonId(style.getPlanningSeasonId());
                        packInfo.setPlanningCategoryItemId(style.getPlanningCategoryItemId());
                        packInfo.setStyleColorId(styleColor.getId());
                        packInfo.setStyleNo(styleColor.getStyleNo());
                        packInfo.setStyleId(style.getId());
                        packInfo.setCode(styleColor.getStyleNo()+"-1");
                        //款式定价
                        stylePricing.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        stylePricing.setPackId(packInfo.getId());
                        stylePricing.setControlPlanCost(new BigDecimal(dto.getPlanCostPrice()));

                        //吊牌
                        hangTag.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                        hangTag.setStyleNo(style.getDesignNo());
                        hangTag.setBulkStyleNo(styleColorNo);

                        insertPackInfoList.add(packInfo);
                        insertStylePricingList.add(stylePricing);
                        insertHangTagList.add(hangTag);
                    }
                }else{
                    styleColorAgent.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                    styleColorAgent.setStyleColorId(styleColorMap.get(styleColor.getStyleNo()));
                    styleColorAgent.setStyleColorNo(styleColor.getStyleNo());
                }


                insertStyleColorAgentList.add(styleColorAgent);
            }else{
                StyleColor styleColorUpdate = this.getById(validStyleColorAgent.getStyleColorId());
                if (styleColorUpdate != null) {
                    styleColorUpdate.setTagPrice(new BigDecimal(dto.getTagPrice()));

                    String styleId = styleColorUpdate.getStyleId();

                    Style styleUpdate = styleService.getById(styleId);
                    if (styleUpdate != null) {
                        String prodCategoryName = dto.getProdCategoryName();
                        String prodCategory1stName = dto.getProdCategory1stName();
                        String prodCategory2ndName = dto.getProdCategory2ndName();

                        String prodCategoryName1 = styleUpdate.getProdCategoryName();
                        String prodCategory1stName1 = styleUpdate.getProdCategory1stName();
                        String prodCategory2ndName1 = styleUpdate.getProdCategory2ndName();

                        if (!prodCategoryName.equals(prodCategoryName1) || !prodCategory1stName.equals(prodCategory1stName1) || !prodCategory2ndName.equals(prodCategory2ndName1)) {
                            /*List<BasicCategoryDot> basicCategoryDotList = ccmFeignService.getTreeByNamelList("品类", "1");
                            //获取品类的code
                            List<BasicCategoryDot> prodCategoryNameList = basicCategoryDotList.stream().filter(o -> o.getName().equals(prodCategoryName)).collect(Collectors.toList());
                            if (CollUtil.isNotEmpty(prodCategoryNameList)) {
                                styleUpdate.setProdCategoryName(prodCategoryNameList.get(0).getName());
                                styleUpdate.setProdCategory(prodCategoryNameList.get(0).getValue());
                                styleColorUpdate.setProductName(prodCategoryNameList.get(0).getName());
                                styleColorUpdate.setProductCode(prodCategoryNameList.get(0).getValue());
                            } else {
                                //报错
                                throw new OtherException("第"+(i+1)+"行,找不到对应的品类");
                            }

                            List<BasicCategoryDot> basicCategoryDotList1 = ccmFeignService.getTreeByNamelList("品类", "0");

                            //获取大类的code
                            List<BasicCategoryDot> prodCategory1stNameList = basicCategoryDotList1.stream().filter(o -> o.getName().equals(prodCategory1stName)).collect(Collectors.toList());
                            if (CollUtil.isNotEmpty(prodCategory1stNameList)) {
                                styleUpdate.setProdCategory1stName(prodCategory1stNameList.get(0).getName());
                                styleUpdate.setProdCategory1st(prodCategory1stNameList.get(0).getValue());
                            } else {
                                //报错
                                throw new OtherException("第"+(i+1)+"行,找不到对应的大类");
                            }

                            List<BasicCategoryDot> basicCategoryDotList2 = ccmFeignService.getTreeByNamelList("品类", "2");
                            //获取中类的code
                            List<BasicCategoryDot> prodCategory2ndNameList = basicCategoryDotList2.stream().filter(o -> o.getName().equals(prodCategory2ndName)).collect(Collectors.toList());
                            if (CollUtil.isNotEmpty(prodCategory2ndNameList)) {
                                styleUpdate.setProdCategory2ndName(prodCategory2ndNameList.get(0).getName());
                                styleUpdate.setProdCategory2nd(prodCategory2ndNameList.get(0).getValue());
                            } else {
                                //报错
                                throw new OtherException("第"+(i+1)+"行,找不到对应的中类");
                            }*/


                            String updatekey = prodCategory1stName1 + "-" + prodCategoryName1 + "-" + prodCategory2ndName1;
                            if (checkMap.containsKey(updatekey)) {
                                String updateMapValue = checkMap.get(updatekey);
                                if (StrUtil.isNotEmpty(updateMapValue)) {
                                    styleUpdate.setProdCategory1stName(prodCategory1stName1);
                                    styleUpdate.setProdCategory1st(updateMapValue.split("-")[0]);
                                    styleUpdate.setProdCategoryName(prodCategoryName1);
                                    styleUpdate.setProdCategory(updateMapValue.split("-")[1]);
                                    styleUpdate.setProdCategory2ndName(prodCategory2ndName1);
                                    styleUpdate.setProdCategory2nd(updateMapValue.split("-")[2]);
                                    styleColorUpdate.setProductName(prodCategoryName1);
                                    styleColorUpdate.setProductCode(updateMapValue.split("-")[1]);
                                }else{
                                    throw new OtherException("第" + (i + 1) + "行,系统找不到对应的大类-品类-中类" + updatekey);
                                }
                            }else{
                                throw new OtherException("第" + (i + 1) + "行,Execl中的大类-品类-中类在系统中找不到" + updatekey);
                            }

                        }
                        String year = dto.getYear();
                        String season = dto.getSeason();
                        if (season.equals("春")) {
                            season= "s";
                        }else if(season.equals("夏")){
                            season= "X";
                        }else if(season.equals("秋")){
                            season= "A";
                        }else if(season.equals("冬")){
                            season= "W";
                        }
                        String brandName = dto.getBrandName();
                        String productSeason =year+" "+season+" "+brandName;

                        //根据产品季名称获取产品季id
                        PlanningSeason planningSeason = planningSeasonService.getByName(productSeason, baseController.getUserCompany());
                        if (planningSeason == null) {
                            throw new OtherException("第"+(i+1)+"行,找不到产品季信息");
                        }
                        if (!planningSeason.getId().equals(styleUpdate.getPlanningSeasonId())) {
                            styleUpdate.setPlanningSeasonId(planningSeason.getId());
                            styleUpdate.setYear(planningSeason.getYear());
                            styleUpdate.setYearName(planningSeason.getYearName());
                            styleUpdate.setBrand(planningSeason.getBrand());
                            styleUpdate.setBrandName(planningSeason.getBrandName());
                            styleUpdate.setSeason(planningSeason.getSeason());
                            styleUpdate.setSeasonName(planningSeason.getSeasonName());
                        }


                        String outsideColorCode = dto.getOutsideColorCode();

                        QueryWrapper<BasicsdatumColourLibraryAgent>  agentQueryWrapper = new QueryWrapper<>();
                        agentQueryWrapper.eq("colour_code",outsideColorCode);
                        agentQueryWrapper.eq("del_flag","0");
                        agentQueryWrapper.eq("status","0");
                        agentQueryWrapper.eq("brand",planningSeason.getBrand());

                        BasicsdatumColourLibraryAgent colourLibraryAgent = colourLibraryAgentService.getOne(agentQueryWrapper);

                        if (colourLibraryAgent != null) {
                            validStyleColorAgent.setOutsideColorName(colourLibraryAgent.getColourName());
                            String colorId = colourLibraryAgent.getColorId();
                            BasicsdatumColourLibrary basicsdatumColour = basicsdatumColourLibraryServicel.getById(colorId);
                            if (basicsdatumColour != null) {
                                styleColorUpdate.setColourLibraryId(basicsdatumColour.getId());
                                styleColorUpdate.setColorCode(basicsdatumColour.getColourCode());
                                styleColorUpdate.setColorName(basicsdatumColour.getColourName());
                            }else{
                                throw new OtherException("第"+(i+1)+"行,找不到对应的集团颜色名称或编码，确认外部颜色是否关联集团颜色");
                            }
                        }else{
                            throw new OtherException("第"+(i+1)+"行,找不到外部颜色码映射内部颜色码数据");
                        }

                        if (!styleUpdate.getStyleTypeName().equals(dto.getStyleTypeName())) {
                            String styleTypeName = dto.getStyleTypeName();
                            List<BasicBaseDict> styleTypeListDictList = getBasicBaseDicts(styleTypeList,styleTypeName);

                            if (CollUtil.isNotEmpty(styleTypeListDictList)) {
                                styleUpdate.setStyleType(styleTypeListDictList.get(0).getValue());
                                styleUpdate.setStyleTypeName(styleTypeListDictList.get(0).getName());
                            } else {
                                throw new OtherException("第"+(i+1)+"行,找不到对应的款式类型");
                            }
                        }

                        if (StrUtil.isEmpty(dto.getGender()) || StrUtil.isBlank(dto.getGender())) {
                            styleUpdate.setGenderType("");
                            styleUpdate.setGenderName("");
                        }else{
                            List<BasicBaseDict> c8GenderListDictList = getBasicBaseDicts(c8GenderList,dto.getGender());
                            if (CollUtil.isNotEmpty(c8GenderListDictList)) {
                                styleUpdate.setGenderType(c8GenderListDictList.get(0).getValue());
                                styleUpdate.setGenderName(c8GenderListDictList.get(0).getName());
                            } else {
                                throw new OtherException("第"+(i+1)+"行,找不到对应的产品季式性别");
                            }
                        }
                    }else {
                        throw new OtherException("第"+(i+1)+"行,找不到对应的设计款信息");
                    }


                    if (!styleUpdate.getSizeRangeName().equals(dto.getSizeRangeName())) {

                        QueryWrapper styleAgentQueryWrapper = new QueryWrapper<StyleColorAgent>();
                        styleAgentQueryWrapper.eq("style_color_no",styleColorNo);
                        styleAgentQueryWrapper.eq("del_flag",0);
                        styleAgentQueryWrapper.last("limit 1");
                        StyleColorAgent styleColorAgent1 = styleColorAgentService.getOne(styleAgentQueryWrapper);
                        if (styleColorAgent1 != null) {
                            throw new OtherException("该款【" + styleColorNo + "】，已存在号型类型【" + styleUpdate.getSizeRangeName() + "】数据，请确认新导入号型类型【" + dto.getSizeRangeName() + "】是否正确？如继续导入，请删除该款已存在号型类型数据！");
                        }

                        QueryWrapper basicsdatumModelTypeQueryWrapper = new QueryWrapper<BasicsdatumModelType>();
                        basicsdatumModelTypeQueryWrapper.eq("model_type", dto.getSizeRangeName());
                        basicsdatumModelTypeQueryWrapper.eq("status", "0");
                        basicsdatumModelTypeQueryWrapper.eq("del_flag", "0");
                        basicsdatumModelTypeQueryWrapper.last(" limit 1");

                        BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeService.getOne(basicsdatumModelTypeQueryWrapper);

                        if (basicsdatumModelType != null) {
                            // /** 翻译名称 */  对应欧码
                            //    private String translateName;
                            //    /** 欧码 */  对应外部尺码
                            //    private String europeanSize;
                            //号型类型尺码
                            //获取所有尺码插入所有尺码的条数
                            String code = basicsdatumModelType.getCode();
                            String size = basicsdatumModelType.getSize();
                            if (StrUtil.isEmpty(code)) {
                                throw new OtherException("第" + (i + 1) + "行,【" + dto.getSizeRangeName() + "】配置问题请联系管理员！");

                            }
                            if (StrUtil.isEmpty(size)) {
                                throw new OtherException("第" + (i + 1) + "行,【" + dto.getSizeRangeName() + "】配置问题请联系管理员！");
                            }
                            QueryWrapper<BasicsdatumSize> basicsdatumSizeQueryWrapper = new QueryWrapper();
                            basicsdatumSizeQueryWrapper.like("model_type_code",code);
                            basicsdatumSizeQueryWrapper.like("model_type",dto.getSizeRangeName());
                            basicsdatumSizeQueryWrapper.eq("translate_name",dto.getOutsideSizeCode());

                            List<BasicsdatumSize> basicsdatumSizeList = basicsdatumSizeService.list(basicsdatumSizeQueryWrapper);
                            if (CollUtil.isNotEmpty(basicsdatumSizeList)) {
                                for (BasicsdatumSize basicsdatumSize : basicsdatumSizeList) {
                                    if (outsideSizeCode.equals(basicsdatumSize.getEuropeanSize())) {
                                        validStyleColorAgent.setSizeId(basicsdatumSize.getCode());
                                        validStyleColorAgent.setSizeCode(basicsdatumSize.getModel());
                                        style.setSizeRange(basicsdatumModelType.getCode());
                                        style.setSizeRangeName(dto.getSizeRangeName());
                                        style.setDefaultSize(basicsdatumModelType.getBasicsSize());
                                        style.setSizeIds(basicsdatumModelType.getSizeIds());
                                        style.setSizeCodes(basicsdatumModelType.getSizeCode());
                                        style.setProductSizes(basicsdatumModelType.getSize());
                                        if (!size.contains(basicsdatumSize.getModel())) {
                                            throw new OtherException("第" + (i + 1) + "行,【" + dto.getSizeRangeName() + "】找不到对应的【" + basicsdatumSize.getModel() + "】信息");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    validStyleColorAgent.setOutsideBarcode(dto.getOutsideBarcode());
                    validStyleColorAgent.setOutsideColorCode(dto.getOutsideColorCode());

                    validStyleColorAgent.setOutsideSizeCode(dto.getOutsideSizeCode());

                    StylePricing stylePricingUpdate = null;
                    QueryWrapper<PackInfo> packInfoQueryWrapper = new QueryWrapper<>();
                    packInfoQueryWrapper.eq("foreign_id",styleUpdate.getId());
                    packInfoQueryWrapper.last(" limit 1 ");
                    PackInfo packInfoUpdate = packInfoService.getOne(packInfoQueryWrapper);
                    if (packInfoUpdate != null) {
                        StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfoUpdate.getId(), baseController.getUserCompany());
                        if (stylePricingVO != null) {
                            BigDecimal controlPlanCostDecimal = stylePricingVO.getControlPlanCost() != null ? stylePricingVO.getControlPlanCost() : BigDecimal.ZERO;
                            BigDecimal planCostPriceDecimal = StrUtil.isNotEmpty(dto.getPlanCostPrice()) ? new BigDecimal(dto.getPlanCostPrice()) : BigDecimal.ZERO;

                            if (controlPlanCostDecimal.compareTo(planCostPriceDecimal) != 0) {
                                stylePricingUpdate = new StylePricing();
                                stylePricingUpdate.setId(stylePricingVO.getStylePricingId());
                                stylePricingUpdate.setControlPlanCost(planCostPriceDecimal);
                            }
                        }else{
                            throw new OtherException("第"+(i+1)+"行,找不到对应的款式定价包信息");
                        }
                    }else{
                        throw new OtherException("第"+(i+1)+"行,找不到对应的资料包信息");
                    }

                    if (!updateStyleColorMap.containsKey(styleColorUpdate)) {
                        updateStyleColorMap.put(styleColor.getStyleNo(),styleColor.getId());
                        updateStyleList.add(styleUpdate);
                        updateStyleColorList.add(styleColorUpdate);
                        //款式定价
                        if (stylePricingUpdate != null) {
                            updateStylePricingList.add(stylePricingUpdate);
                        }

                    }
                    updateStyleColorAgentList.add(validStyleColorAgent);



                    String packagingForm = dto.getPackagingForm();

                    LambdaQueryWrapper<HangTag> hangTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    hangTagLambdaQueryWrapper.eq(HangTag::getBulkStyleNo, styleColorNo)
                            .eq(HangTag::getDelFlag, "0")
                            .last("limit 1");
                    HangTag hangTagUpdate = hangTagService.getOne(hangTagLambdaQueryWrapper);
                    if (hangTagUpdate != null) {
                        if (StrUtil.isNotEmpty(packagingForm)) {
                            List<BasicBaseDict> packageTypeListDict = getBasicBaseDicts(packageTypeList,packagingForm);
                            if (CollUtil.isNotEmpty(packageTypeListDict)) {
                                hangTagUpdate.setPackagingForm(packageTypeListDict.get(0).getName());
                                hangTagUpdate.setPackagingFormCode(packageTypeListDict.get(0).getValue());
                            } else {
                                throw new OtherException("第" + (i + 1) + "行,【" + packagingForm + "】找不到对应的包装类型");
                            }
                        }else{
                            hangTagUpdate.setPackagingForm("");
                            hangTagUpdate.setPackagingFormCode("");
                        }
                        updateHangTagList.add(hangTagUpdate);
                    }else{
                        List<HangTag> hangTagList = insertHangTagList.stream().filter(o -> o.getBulkStyleNo().equals(styleColorNo)).collect(Collectors.toList());
                        if (CollUtil.isEmpty(hangTagList)) {
                            //吊牌
                            hangTag.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
                            hangTag.setStyleNo(style.getDesignNo());
                            hangTag.setBulkStyleNo(styleColorNo);
                            if (StrUtil.isNotEmpty(packagingForm)) {
                                List<BasicBaseDict> packageTypeListDict = getBasicBaseDicts(packageTypeList,packagingForm);
                                if (CollUtil.isNotEmpty(packageTypeListDict)) {
                                    hangTag.setPackagingForm(packageTypeListDict.get(0).getName());
                                    hangTag.setPackagingFormCode(packageTypeListDict.get(0).getValue());
                                } else {
                                    throw new OtherException("第" + (i + 1) + "行,【" + packagingForm + "】找不到对应的包装类型");
                                }
                            }
                        }
                        insertHangTagList.add(hangTag);
                    }
                }
            }
        }

        if (CollUtil.isNotEmpty(updateStyleColorAgentList) && !isUpdate) {
            return ApiResult.success("总共检查条数:" + list.size() + "条，其中新增" + insertStyleColorAgentList.size() + "条，修改" + updateStyleColorAgentList.size() + "条。确定是否继续导入？",true);
        }

        batchSaveOrUpdate(insertStyleList, updateStyleList, insertStyleColorList, updateStyleColorList, insertStyleColorAgentList, updateStyleColorAgentList, insertPackInfoList, insertStylePricingList, updateStylePricingList,insertHangTagList,updateHangTagList);

        String msg = "";
            msg+="成功导入条数"+list.size()+"条！";
            msg+="其中新增"+insertStyleColorAgentList.size()+"条，";
            msg+="修改"+updateStyleColorAgentList.size()+"条。";
        return ApiResult.success(msg);
    }

    @Transactional
    @Override
    public ApiResult mangoHangTagExeclImport(List<MangoHangTagExeclDto> list) {

        String errorInfo = "";
        for (int i = 0; i < list.size(); i++) {
            MangoHangTagExeclDto mangoHangTagExeclDto = list.get(i);
            //大货款号
            String styleNo = mangoHangTagExeclDto.getStyleNo();
            //品名翻译
            String productNameTranslate = mangoHangTagExeclDto.getProductNameTranslate();
            //质量等级
            String qualityGrade = mangoHangTagExeclDto.getQualityGrade();
            //品名
            String productName = mangoHangTagExeclDto.getProductName();
            //执行标准
            String executeStandard = mangoHangTagExeclDto.getExecuteStandard();
            //安全技术类别
            String saftyType = mangoHangTagExeclDto.getSaftyType();
            //材料成分
            String ingredient = mangoHangTagExeclDto.getIngredient();
            //颜色代码翻译
            String colorCodeTranslate = mangoHangTagExeclDto.getColorCodeTranslate();
            //产地
            String producer = mangoHangTagExeclDto.getProducer();
            //水洗唛编码
            String washingCode = mangoHangTagExeclDto.getWashingCode();

            if (StrUtil.isEmpty(styleNo)) {
                errorInfo+="第" + (i + 1) + "行" + "大货款号不能为空！\n";
            }
            if (StrUtil.isEmpty(qualityGrade)) {
                errorInfo+="第" + (i + 1) + "行" + "质量等级不能为空！\n";
            }
            if (StrUtil.isEmpty(productName)) {
                errorInfo+="第" + (i + 1) + "行" + "品名不能为空！\n";
            }
            if (StrUtil.isEmpty(productNameTranslate)) {
                errorInfo+="第" + (i + 1) + "行" + "品名翻译不能为空！\n";
            }
            if (StrUtil.isEmpty(colorCodeTranslate)) {
                errorInfo+="第" + (i + 1) + "行" + "颜色代码翻译不能为空！\n";
            }
            if (StrUtil.isEmpty(executeStandard)) {
                errorInfo+="第" + (i + 1) + "行" + "执行标准不能为空！\n";
            }
            if (StrUtil.isEmpty(saftyType)) {
                errorInfo+="第" + (i + 1) + "行" + "安全技术类别不能为空！\n";
            }
            if (StrUtil.isEmpty(ingredient)) {
                errorInfo+="第" + (i + 1) + "行" + "材料成分不能为空！\n";
            }
            if (StrUtil.isEmpty(producer)) {
                errorInfo+="第" + (i + 1) + "行" + "原产地不能为空！\n";
            }
            if (StrUtil.isEmpty(washingCode)) {
                errorInfo+="第" + (i + 1) + "行" + "洗标编码不能为空！\n";
            }
        }

        if (StrUtil.isNotEmpty(errorInfo)) {
            return ApiResult.error(errorInfo,500);
        }

        //获取对应的枚举信息
        //品名
        List<BasicBaseDict> productList = ccmFeignService.getDictInfoToList("ProductName");
        //质量等级
        List<BasicBaseDict> qualityClassList = ccmFeignService.getDictInfoToList("C8_QualityClass");
        //执行标准
        List<BasicBaseDict> standardList = ccmFeignService.getDictInfoToList("C8_OPStandard");
        //安全技术类别
        List<BasicBaseDict> saftyTypeList = ccmFeignService.getAllDictInfoToList("C8_SaftyType");
        //水洗唛
        List<BasicsdatumWashIcon> basicsdatumWashIconList = basicsdatumWashIconService.list(new QueryWrapper<BasicsdatumWashIcon>().eq("del_flag", "0").eq("status","0"));
        //创建吊牌信息数据
        List<HangTag> updateHangTagList = new ArrayList<>();

        Map<String, StyleColorAgentVo> styleHangTagMap = null;
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        queryWrapper.eq("ts.brand_name","MANGO");
        queryWrapper.eq("tsca.del_flag","0");
        List<StyleColorAgentVo> agentList = baseMapper.agentList(queryWrapper);
        if (CollUtil.isNotEmpty(agentList)) {
            styleHangTagMap = agentList.stream().collect(Collectors.toMap(StyleColorAgentVo::getStyleNo, m -> m, (k1, k2) -> k1));
        }


        HangTag updateHangTag = null;

        for (int i = 0; i < list.size(); i++) {

            updateHangTag = new HangTag();

            MangoHangTagExeclDto mangoHangTagExeclDto = list.get(i);
            //品名
            String productName = mangoHangTagExeclDto.getProductName();
            //大货款号
            String styleNo = mangoHangTagExeclDto.getStyleNo();
            //质量等级
            String qualityGrade = mangoHangTagExeclDto.getQualityGrade();
            //执行标准
            String executeStandard = mangoHangTagExeclDto.getExecuteStandard();
            //安全技术类别
            String saftyType = mangoHangTagExeclDto.getSaftyType();
            //水洗唛编码
            String washingCode = mangoHangTagExeclDto.getWashingCode();
            //产地
            String producer = mangoHangTagExeclDto.getProducer();
            //品名翻译
            String productNameTranslate = mangoHangTagExeclDto.getProductNameTranslate();
            //材料成分
            String ingredient = mangoHangTagExeclDto.getIngredient();
            //颜色代码翻译
            String colorCodeTranslate = mangoHangTagExeclDto.getColorCodeTranslate();

            //得到MANGO品牌大货款信息，并判断是否导入的大货款是否存在数据库
            if (!styleHangTagMap.containsKey(styleNo)) {
                errorInfo+="第" + (i + 1) + "行,【" + styleNo + "】找不到对应的大货款信息！\n";
            }

            //region 质量等级验证
            List<BasicBaseDict> productNameIsExistList = getBasicBaseDicts(productList, productName);

            if (CollUtil.isNotEmpty(productNameIsExistList)) {
                updateHangTag.setProductCode(productNameIsExistList.get(0).getValue());
                updateHangTag.setProductName(productNameIsExistList.get(0).getName());
            } else {
                errorInfo+="第" + (i + 1) + "行,【" + qualityGrade + "】找不到对应的品名！\n";
            }
            //endregion
            //region 质量等级验证
            List<BasicBaseDict> qualityClassIsExistList = getBasicBaseDicts(qualityClassList, qualityGrade);

            if (CollUtil.isNotEmpty(qualityClassIsExistList)) {
                updateHangTag.setQualityGradeCode(qualityClassIsExistList.get(0).getValue());
                updateHangTag.setQualityGrade(qualityClassIsExistList.get(0).getName());
            } else {
                errorInfo+="第" + (i + 1) + "行,【" + qualityGrade + "】找不到对应的质量等级！\n";
            }
            //endregion

            //region 执行标准验证
            List<BasicBaseDict> standardIsExistList = getBasicBaseDicts(standardList, executeStandard);

            if (CollUtil.isNotEmpty(standardIsExistList)) {
                updateHangTag.setExecuteStandardCode(standardIsExistList.get(0).getValue());
                updateHangTag.setExecuteStandard(standardIsExistList.get(0).getName());
            } else {
                errorInfo+="第" + (i + 1) + "行,【" + executeStandard + "】找不到对应的执行标准！\n";
            }
            //endregion

            //region 安全技术类别验证
            List<BasicBaseDict> saftyTypeIsExistList = getBasicBaseDicts(saftyTypeList, saftyType);

            if (CollUtil.isNotEmpty(saftyTypeIsExistList)) {
                updateHangTag.setSaftyTitleCode(saftyTypeIsExistList.get(0).getValue());
                updateHangTag.setSaftyType(saftyTypeIsExistList.get(0).getName());
            } else {
                errorInfo+="第" + (i + 1) + "行,【" + saftyType + "】找不到对应的安全技术类别！\n";
            }
            //endregion

            //region 洗标编码是否存在验证
            List<BasicsdatumWashIcon> washIconsisExistList = basicsdatumWashIconList.stream().filter(o -> o.getCode().equals(washingCode)).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(washIconsisExistList)) {
                updateHangTag.setWashingCode(washIconsisExistList.get(0).getCode());
                updateHangTag.setWashingLabel(washIconsisExistList.get(0).getUrl());
            }else{
                errorInfo+="第" + (i + 1) + "行,【" + washingCode + "】找不到对应的洗标信息！\n";
            }
            //endregion

            StyleColorAgentVo styleColorAgentVo = styleHangTagMap.get(styleNo);
            if (styleColorAgentVo == null) {
                errorInfo+="第" + (i + 1) + "行,【" + washingCode + "】找不到对应的吊牌信息！\n";
            }else{
                //如果是已下发不允许导入吊牌信息，必须先解锁再导入
                String enableStatus = styleColorAgentVo.getSendStatus();
                if ("1".equals(enableStatus)) {
                    errorInfo+="第" + (i + 1) + "行,【" + styleNo + "】请先解锁，然后再导入吊牌信息！\n";
                }
            }



            updateHangTag.setId(styleColorAgentVo.getHangTagId());
            updateHangTag.setIngredient(ingredient);
            updateHangTag.setProductNameTranslate(productNameTranslate);
            updateHangTag.setColorCodeTranslate(colorCodeTranslate);
            updateHangTag.setProducer(producer);
            //添加吊牌需要修改的信息
            updateHangTagList.add(updateHangTag);

        }

        if (StrUtil.isNotEmpty(errorInfo)) {
            return ApiResult.error(errorInfo,500);
        }

        if (CollUtil.isNotEmpty(updateHangTagList)) {
            hangTagService.updateBatchById(updateHangTagList);
        }

        String msg = "";
        msg+="成功导入条数:"+updateHangTagList.size()+"！";
        return ApiResult.success(msg);
    }

    private String checkData2(List<MangoStyleColorExeclDto> list, String errorInfo) {
        for (int i = 0; i < list.size(); i++) {
            MangoStyleColorExeclDto entity = list.get(i);
            String styleColorNo = entity.getStyleColorNo();
            String prodCategoryName = entity.getProdCategoryName();
            String prodCategory1stName = entity.getProdCategory1stName();
            String prodCategory2ndName = entity.getProdCategory2ndName();
            String outsideColorCode = entity.getOutsideColorCode();
            String sizeRangeName = entity.getSizeRangeName();
            String rowText = "";

            //存在已发送状态
            QueryWrapper styleColorAgentQueryWrapper = new QueryWrapper<StyleColorAgent>();
            styleColorAgentQueryWrapper.eq("style_color_no",styleColorNo);
            //styleColorAgentQueryWrapper.eq("status",1);
            styleColorAgentQueryWrapper.eq("del_flag",0);
            styleColorAgentQueryWrapper.last("limit 1");
            StyleColorAgent styleColorAgent = styleColorAgentService.getOne(styleColorAgentQueryWrapper);

            if (styleColorAgent != null) {
                String status = styleColorAgent.getStatus();

                StyleColor styleColor = this.getById(styleColorAgent.getStyleColorId());

                if (styleColor == null) {
                    rowText = commonPromptInfo(true, rowText, "第" + (i + 1) + "行" + "【" + styleColorNo + "】" + "找不到对应的配色信息！\n");
                }
                boolean styleStatus = "0".equals(styleColor.getStatus());
                if ("1".equals(status) && styleStatus) {
                    rowText = commonPromptInfo(styleColorAgent != null, rowText, "第" + (i + 1) + "行" + "【" + styleColorNo + "】" + "已下发到下游业务系统，需先解锁再导入数据！\n");
                }else if ("2".equals(status) && styleStatus) {
                    if (styleColor != null) {
                        String styleId = styleColor.getStyleId();
                        Style style = styleService.getById(styleId);
                        if (style != null) {
                            //品牌
                            rowText = commonPromptInfo( ! style.getBrandName().equals(entity.getBrandName()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getBrandName() + "】 数据已下发过，品牌不允许修改！\n");
                            //品类
                            rowText = commonPromptInfo( !style.getProdCategoryName().equals(entity.getProdCategoryName()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getProdCategoryName() + "】 数据已下发过，品类不允许修改！\n");
                            //大类
                            rowText = commonPromptInfo( !style.getProdCategory1stName().equals(entity.getProdCategory1stName()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getProdCategory1stName() + "】 数据已下发过，大类不允许修改！\n");
                            //中类
                            rowText = commonPromptInfo( !style.getProdCategory2ndName().equals(entity.getProdCategory2ndName()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getProdCategory2ndName() + "】 数据已下发过，中类不允许修改！\n");
                            //号型类型
                            rowText = commonPromptInfo( !style.getSizeRangeName().equals(entity.getSizeRangeName()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getSizeRangeName() + "】 数据已下发过，号型类型不允许修改！\n");
                            //合作方颜色
                            rowText = commonPromptInfo( !styleColorAgent.getOutsideColorCode().equals(entity.getOutsideColorCode()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getOutsideColorCode() + "】 数据已下发过，合作方颜色编码不允许修改！\n");



                            QueryWrapper styleColorAgentQueryWrapperExit = new QueryWrapper<StyleColorAgent>();
                            styleColorAgentQueryWrapperExit.eq("style_color_no",styleColorNo);
                            styleColorAgentQueryWrapperExit.eq("outside_size_code",entity.getOutsideSizeCode());
                            styleColorAgentQueryWrapperExit.last("limit 1");

                            StyleColorAgent styleColorAgentServiceOne = styleColorAgentService.getOne(styleColorAgentQueryWrapperExit);
                            if (styleColorAgentServiceOne != null) {
                                //合作方尺码
                                rowText = commonPromptInfo( styleColorAgentServiceOne.getOutsideSizeCode().equals(entity.getOutsideSizeCode()), rowText, "第" + (i + 1) + "行" +"【"+entity.getStyleColorNo()+"】,"+ "【" + entity.getOutsideSizeCode() + "】 数据已下发过，合作方尺码不允许修改！\n");
                            }
                        }
                    }
                }else if ("3".equals(status) && !styleStatus){
                        rowText = commonPromptInfo(true, rowText, "第" + (i + 1) + "行" + "【" + styleColorNo + "】" + "解除卡控后，请先停用后再导入数据！\n");
                }
            }


            QueryWrapper styleColorAgentQueryBarcodeWrapper = new QueryWrapper<StyleColorAgent>();
            styleColorAgentQueryBarcodeWrapper.eq("style_color_no",styleColorNo);
            styleColorAgentQueryBarcodeWrapper.eq("outside_barcode",entity.getOutsideBarcode());
            styleColorAgentQueryBarcodeWrapper.ne("outside_size_code",entity.getOutsideSizeCode());
            //styleColorAgentQueryBarcodeWrapper.ne("size_id",entity.getSizeCode());
            styleColorAgentQueryBarcodeWrapper.eq("del_flag",0);
            styleColorAgentQueryBarcodeWrapper.last("limit 1");
            StyleColorAgent styleColorAgentExit = styleColorAgentService.getOne(styleColorAgentQueryBarcodeWrapper);
            rowText = commonPromptInfo(styleColorAgentExit != null, rowText, "第" + (i + 1) + "行" + "【" + styleColorNo + "】【"+entity.getOutsideSizeCode()+"】【" + entity.getOutsideBarcode() + "】" + "数据库存在重复数据请确认后再导入数据！\n");

            QueryWrapper styleColorAgentBracodeQueryBarcodeWrapper = new QueryWrapper<StyleColorAgent>();
            styleColorAgentBracodeQueryBarcodeWrapper.ne("style_color_no",styleColorNo);
            styleColorAgentBracodeQueryBarcodeWrapper.eq("outside_barcode",entity.getOutsideBarcode());
            styleColorAgentBracodeQueryBarcodeWrapper.last("limit 1");
            StyleColorAgent styleColorAgentBracodeExit = styleColorAgentService.getOne(styleColorAgentBracodeQueryBarcodeWrapper);
            rowText = commonPromptInfo(styleColorAgentBracodeExit != null, rowText, "第" + (i + 1) + "行,合作方条码【" + entity.getOutsideBarcode() + "】" + "数据库已存在请确认后再导入数据！\n");


            //根据产品季名称获取产品季id
            String year = entity.getYear();
            String season = entity.getSeason();
            if (season.equals("春")) {
                season= "S";
            }else if(season.equals("夏")){
                season= "X";
            }else if(season.equals("秋")){
                season= "A";
            }else if(season.equals("冬")){
                season= "W";
            }
            String brandName = entity.getBrandName();
            String productSeason =year+" "+season+" "+brandName;
            PlanningSeason planningSeason = planningSeasonService.getByName(productSeason, baseController.getUserCompany());
            rowText = commonPromptInfo(planningSeason == null, rowText, "第" + (i + 1) + "行" + "【" + productSeason + "】" + "数据库找不到对应产品季信息！\n");


            //获取大类的code
            List<BasicCategoryDot> prodCategory1stList = ccmFeignService.getTreeByNamelList("品类", "0");
            List<BasicCategoryDot> prodCategory1stNameList = prodCategory1stList.stream().filter(o -> o.getName().equals(prodCategory1stName)).collect(Collectors.toList());
            rowText = commonPromptInfo(CollUtil.isEmpty(prodCategory1stNameList), rowText, "第" + (i + 1) + "行" + "【" + prodCategory1stName + "】" + "数据库找不到对应大类信息！\n");

            //根据大类找品类
            if (CollUtil.isNotEmpty(prodCategory1stNameList)) {
                //根据大类找品类
                List<BasicStructureTreeVo> basicStructureTreeVos = ccmFeignService.basicStructureTreeByCode("品类", prodCategory1stNameList.get(0).getValue(), "1");
                if (CollUtil.isNotEmpty(basicStructureTreeVos)) {
                    //找中类
                    List<BasicStructureTreeVo> children = basicStructureTreeVos.get(0).getChildren();
                    if (CollUtil.isEmpty(children)) {
                        rowText = commonPromptInfo(CollUtil.isEmpty(children), rowText, "第" + (i + 1) + "行" + "【" + prodCategoryName + "】" + "数据库找不到对应品类信息！\n");
                    }
                    //获取品类的code
                    List<BasicStructureTreeVo> children2 = children.stream().filter(o -> o.getName().equals(prodCategoryName)).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(children2)) {
                        List<BasicStructureTreeVo> basicStructureTreeVoList2 = ccmFeignService.basicStructureTreeByCode("品类", children2.get(0).getValue(), "2");
                        if (CollUtil.isNotEmpty(basicStructureTreeVoList2)) {
                            List<BasicStructureTreeVo> children3 = basicStructureTreeVoList2.get(0).getChildren();
                            if (CollUtil.isNotEmpty(children3)) {
                                List<BasicStructureTreeVo> collect = children3.stream().filter(o -> o.getName().equals(prodCategory2ndName)).collect(Collectors.toList());
                                if (CollUtil.isEmpty(collect)) {
                                    rowText = commonPromptInfo(CollUtil.isEmpty(collect), rowText, "第" + (i + 1) + "行" + "【" + prodCategory2ndName + "】" + "数据库找不到对应中类信息！\n");
                                }
                            }else{
                                rowText = commonPromptInfo(CollUtil.isEmpty(children3), rowText, "第" + (i + 1) + "行" + "【" + prodCategory2ndName + "】" + "数据库找不到对应中类信息！\n");
                            }

                        }else{
                            rowText = commonPromptInfo(CollUtil.isEmpty(children2), rowText, "第" + (i + 1) + "行" +"【" + prodCategoryName + "】>【" + prodCategory2ndName + "】" + "数据库找不到对应中类信息！\n");
                        }
                    }else{
                        rowText = commonPromptInfo(CollUtil.isEmpty(children2), rowText, "第" + (i + 1) + "行" + "【" + prodCategoryName + "】" + "数据库找不到对应品类信息！\n");
                    }
                }else{
                    rowText = commonPromptInfo(CollUtil.isEmpty(basicStructureTreeVos), rowText, "第" + (i + 1) + "行" + "【" + prodCategoryName + "】" + "数据库找不到对应品类信息！\n");
                }
            }

            BasicsdatumColourLibraryAgent colourLibraryAgent = null;
            if (planningSeason != null) {
                QueryWrapper<BasicsdatumColourLibraryAgent>  agentQueryWrapper = new QueryWrapper<>();
                agentQueryWrapper.eq("colour_code",outsideColorCode);
                agentQueryWrapper.eq("del_flag","0");
                agentQueryWrapper.eq("status","0");
                agentQueryWrapper.eq("brand",planningSeason.getBrand());
                colourLibraryAgent = colourLibraryAgentService.getOne(agentQueryWrapper);
            }




            if (colourLibraryAgent != null) {
                String colorId = colourLibraryAgent.getColorId();
                rowText = commonPromptInfo(StrUtil.isEmpty(colorId), rowText, "第" + (i + 1) + "行" + "【" + outsideColorCode + "】" + "数据库找不到外部颜色映射集团内部颜色信息！\n");


                QueryWrapper<BasicsdatumColourLibrary>  basicsdatumColourLibraryQueryWrapper = new QueryWrapper<>();
                basicsdatumColourLibraryQueryWrapper.eq("id",colourLibraryAgent.getColorId());
                basicsdatumColourLibraryQueryWrapper.eq("del_flag","0");
                basicsdatumColourLibraryQueryWrapper.eq("status","0");

                BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getOne(basicsdatumColourLibraryQueryWrapper);

                rowText = commonPromptInfo(basicsdatumColourLibrary == null, rowText, "第" + (i + 1) + "行" + "【" + "-" + outsideColorCode + "】" + "数据库找不到外部颜色映射集团内部颜色信息！\n");
            }else{
                rowText = commonPromptInfo(colourLibraryAgent == null, rowText, "第" + (i + 1) + "行" + "【" + outsideColorCode + "】" + "数据库找不到外部颜色映射集团内部颜色信息！\n");
            }

            BasicsdatumModelType basicsdatumModelType = null;
            if (planningSeason != null) {
                QueryWrapper basicsdatumModelTypeQueryWrapper = new QueryWrapper<BasicsdatumModelType>();
                basicsdatumModelTypeQueryWrapper.eq("model_type", sizeRangeName);
                basicsdatumModelTypeQueryWrapper.eq("status", "0");
                basicsdatumModelTypeQueryWrapper.eq("del_flag", "0");
                basicsdatumModelTypeQueryWrapper.like("brand", planningSeason.getBrand());
                basicsdatumModelTypeQueryWrapper.last(" limit 1");

                basicsdatumModelType = basicsdatumModelTypeService.getOne(basicsdatumModelTypeQueryWrapper);
            }


            String outsideSizeCode = entity.getOutsideSizeCode();

            if (basicsdatumModelType == null) {
                rowText += "第"+(i+1)+"行"+"【"+entity.getBrandName()+"】【" + sizeRangeName + "】" + "数据库找不到对应号型类型信息！\n";
            }else{
                //号型类型尺码
                String code = basicsdatumModelType.getCode();
                QueryWrapper<BasicsdatumSize> basicsdatumSizeQueryWrapper = new QueryWrapper();
                basicsdatumSizeQueryWrapper.like("model_type_code",code);
                basicsdatumSizeQueryWrapper.like("model_type",sizeRangeName);
                basicsdatumSizeQueryWrapper.eq("european_size",outsideSizeCode);

                List<BasicsdatumSize> basicsdatumSizeList = basicsdatumSizeService.list(basicsdatumSizeQueryWrapper);
                if (CollUtil.isEmpty(basicsdatumSizeList)) {
                    errorInfo += "第" + (i + 1) + "行" + "外部尺码【" + outsideSizeCode + "】" + "【" + basicsdatumModelType.getModelType() + "】号型类型数据库找不到对应外部尺码【" + outsideSizeCode + "】信息！\n";
                }
            }
            errorInfo +=rowText;
        }
        return errorInfo;
    }


    @NotNull
    private List<BasicBaseDict> getBasicBaseDicts(List<BasicBaseDict> list, String name) {
        return list.stream().filter(o -> o.getName().equals(name)).collect(Collectors.toList());
    }

    /**
     * 批量新增和修改数据
     * @param insertStyleList
     * @param updateStyleList
     * @param insertStyleColorList
     * @param updateStyleColorList
     * @param insertStyleColorAgentList
     * @param updateStyleColorAgentList
     * @param insertPackInfoList
     * @param insertStylePricingList
     * @param updateStylePricingList
     */
    private void batchSaveOrUpdate(List<Style> insertStyleList, List<Style> updateStyleList, List<StyleColor> insertStyleColorList, List<StyleColor> updateStyleColorList, List<StyleColorAgent> insertStyleColorAgentList, List<StyleColorAgent> updateStyleColorAgentList, List<PackInfo> insertPackInfoList, List<StylePricing> insertStylePricingList, List<StylePricing> updateStylePricingList,List<HangTag> insertHangTagList,List<HangTag> updateHangTagList) {
        if (CollUtil.isNotEmpty(insertStyleList)) {
            styleService.saveBatch(insertStyleList);
        }
        if (CollUtil.isNotEmpty(insertStyleColorList)) {
            this.saveBatch(insertStyleColorList);
        }
        if (CollUtil.isNotEmpty(insertStyleColorAgentList)) {
            styleColorAgentService.saveBatch(insertStyleColorAgentList);
        }
        if (CollUtil.isNotEmpty(insertPackInfoList)) {
            packInfoService.saveBatch(insertPackInfoList);
        }
        if (CollUtil.isNotEmpty(insertStylePricingList)) {
            stylePricingService.saveBatch(insertStylePricingList);
        }
        if (CollUtil.isNotEmpty(updateStyleList)) {
            styleService.updateBatchById(updateStyleList);
        }
        if (CollUtil.isNotEmpty(updateStyleColorList)) {
            this.updateBatchById(updateStyleColorList);
        }
        if (CollUtil.isNotEmpty(updateStyleColorAgentList)) {
            styleColorAgentService.updateBatchById(updateStyleColorAgentList);
        }
        if (CollUtil.isNotEmpty(updateStylePricingList)) {
            stylePricingService.updateBatchById(updateStylePricingList);
        }
        if (CollUtil.isNotEmpty(insertHangTagList)) {
            hangTagService.saveBatch(insertHangTagList);
        }
        if (CollUtil.isNotEmpty(updateHangTagList)) {
            hangTagService.updateBatchById(updateHangTagList);
        }
    }

    private static String checkData1(List<MangoStyleColorExeclDto> list, String errorInfo) {
        Map<String,String> map = new HashMap<>();
        Map<String,String> map1 = new HashMap<>();
        Map<String,String> map3 = new HashMap<>();
        Map<String,String> map4 = new HashMap<>();
        Map<String,String> map5 = new HashMap<>();
        Map<String,String> map6 = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            MangoStyleColorExeclDto entity = list.get(i);
            Field[] declaredFields = entity.getClass().getDeclaredFields();
            String rowText = "";

            rowText = checkExeclIsNullData(entity, declaredFields, rowText);

            if (StrUtil.isNotEmpty(rowText)) {
                errorInfo += "第" + (i + 1) + "行:【" + rowText.substring(0, rowText.length() - 1) + "】内容不能为空！ \n ";
            }

            String styleColorNo = entity.getStyleColorNo();
            String outsideBarcode = entity.getOutsideBarcode();
            String outsideSizeCode = entity.getOutsideSizeCode();
            String tagPrice = entity.getTagPrice();
            String planCostPrice = entity.getPlanCostPrice();
            String sizeRangeName = entity.getSizeRangeName();

            checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo)  && StrUtil.isNotBlank(outsideBarcode) && StrUtil.isNotBlank(outsideSizeCode), styleColorNo  + outsideSizeCode + outsideBarcode, map, i);

            checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(outsideBarcode), styleColorNo + outsideBarcode, map1, i);

            //checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(sizeCode), styleColorNo + sizeCode, map2, i);

            checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(outsideSizeCode), styleColorNo + outsideSizeCode, map3, i);

            checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(tagPrice), styleColorNo + tagPrice, map4, i);
            checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(planCostPrice), styleColorNo + planCostPrice, map5, i);
            checkBulkStyleNoRelateData(StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(sizeRangeName), styleColorNo + sizeRangeName, map6, i);


            if (StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(tagPrice)) {

                if (map4.containsKey(styleColorNo)) {
                    String tagPriceValue = map4.get(styleColorNo);
                    if (!tagPriceValue.equals(tagPrice)) {
                        errorInfo += styleColorNo+ ":【大货款号】+【吊牌价】 出现不同的价格";
                    }

                }else{
                    map4.put(styleColorNo,tagPrice);
                }

            }
            if (StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(planCostPrice)) {

                if (map5.containsKey(styleColorNo)) {
                    String planCostPriceValue = map5.get(styleColorNo);
                    if (!planCostPriceValue.equals(planCostPrice)) {
                        errorInfo += styleColorNo+ ":【大货款号】+【成本价】 出现不同的价格";
                    }

                }else{
                    map5.put(styleColorNo,planCostPrice);
                }
            }
            if (StrUtil.isNotBlank(styleColorNo) && StrUtil.isNotBlank(sizeRangeName)) {

                if (map6.containsKey(styleColorNo)) {
                    String planCostPriceValue = map6.get(styleColorNo);
                    if (!planCostPriceValue.equals(sizeRangeName)) {
                        errorInfo += styleColorNo+ ":【大货款号】+【号型类型】 出现不同的号型类型";
                    }

                }else{
                    map6.put(styleColorNo,sizeRangeName);
                }
            }
        }


        errorInfo = checkMapInfo(map, errorInfo, "行:【大货款号】+【合作方尺码】+【合作方条形码】 出现重复！ \n ");
        errorInfo = checkMapInfo(map1, errorInfo, "行:【大货款号】+【合作方条形码】出现重复！ \n ");
        errorInfo = checkMapInfo(map3, errorInfo, "行:【大货款号】+【合作方尺码】出现重复！ \n ");
        return errorInfo;
    }

    /**
     * 验证execl数据为空的数据
     * @param entity
     * @param declaredFields
     * @param rowText
     * @return
     */
    private static String checkExeclIsNullData(MangoStyleColorExeclDto entity, Field[] declaredFields, String rowText) {
        for (Field field : declaredFields) {
            //开启权限
            try {
                field.setAccessible(true);
                Object o = field.get(entity);
                if (o == null) {
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    if (annotation != null) {
                        String text = annotation.name();
                        if (StrUtil.isNotEmpty(text)) {
                            rowText = rowText + text + "/";
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                field.setAccessible(false);
            }
        }
        return rowText;
    }

    /**
     * 通用报错信息
     * @param rowText
     * @param errorInfo
     * @param i
     * @return
     */
    private static String commonPromptInfo(boolean rowText, String errorInfo, String i) {
        if (rowText) {
            errorInfo += i;
        }
        return errorInfo;
    }

    private static String checkMapInfo(Map<String, String> map, String errorInfo, String x) {
        for (String value : map.values()) {
            errorInfo = commonPromptInfo(value.contains(","), errorInfo, "第" + value + x);
        }
        return errorInfo;
    }

    /**
     * 验证大货款关联得尺码重复问题
     * @param styleColorNo
     * @param styleColorNo1
     * @param map
     * @param i
     */
    private static void checkBulkStyleNoRelateData(boolean styleColorNo, String styleColorNo1, Map<String, String> map, int i) {
        if (styleColorNo) {
            String content = styleColorNo1;
            if (map.containsKey(content)) {
                String s = map.get(content);
                map.put(content, s + "," + (i + 1));
            } else {
                map.put(content, (i + 1) + "");
            }
        }
    }

    @Override
    @Transactional
    public void agentUpdate(StyleColorAgentVo styleColorAgentVo) {
        String id = styleColorAgentVo.getId();
        if(StrUtil.isNotBlank(id)){
            LambdaUpdateWrapper<StyleColorAgent> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(StyleColorAgent::getOutsideBarcode,styleColorAgentVo.getOutsideBarcode());
            updateWrapper.set(StyleColorAgent::getUpdateId, getUserId());
            updateWrapper.set(StyleColorAgent::getUpdateName, getUserName());
            updateWrapper.set(StyleColorAgent::getUpdateDate, new Date());
            updateWrapper.eq(StyleColorAgent::getId,id);
            styleColorAgentService.update(updateWrapper);
        }

        String styleColorId = styleColorAgentVo.getStyleColorId();
        if(StrUtil.isNotBlank(styleColorId)){
            LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(StyleColor::getTagPrice,styleColorAgentVo.getTagPrice());
            updateWrapper.set(StyleColor::getUpdateId, getUserId());
            updateWrapper.set(StyleColor::getUpdateName, getUserName());
            updateWrapper.set(StyleColor::getUpdateDate, new Date());
            updateWrapper.eq(StyleColor::getId,styleColorId);
            update(updateWrapper);
        }

        String stylePricingId = styleColorAgentVo.getStylePricingId();
        if(StrUtil.isNotBlank(stylePricingId)){
            LambdaUpdateWrapper<StylePricing> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(StylePricing::getControlPlanCost,styleColorAgentVo.getControlPlanCost());
            updateWrapper.set(StylePricing::getUpdateId, getUserId());
            updateWrapper.set(StylePricing::getUpdateName, getUserName());
            updateWrapper.set(StylePricing::getUpdateDate, new Date());
            updateWrapper.eq(StylePricing::getId,stylePricingId);
            stylePricingService.update(updateWrapper);
        }

        String hangTagId = styleColorAgentVo.getHangTagId();
        if(StrUtil.isNotBlank(hangTagId)){
            LambdaUpdateWrapper<HangTag> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(HangTag::getPackagingForm,styleColorAgentVo.getPackagingForm());
            updateWrapper.set(HangTag::getPackagingFormCode,styleColorAgentVo.getPackagingFormCode());
            updateWrapper.set(HangTag::getUpdateId, getUserId());
            updateWrapper.set(HangTag::getUpdateName, getUserName());
            updateWrapper.set(HangTag::getUpdateDate, new Date());
            updateWrapper.eq(HangTag::getId,hangTagId);
            hangTagService.update(updateWrapper);
        }
    }

    @Override
    public ApiResult uploadStyleColorPics(Principal user, MultipartFile[] files) {

        List<String> styleNos = new ArrayList<>();
        Map<String,MultipartFile> map = new HashMap<>();

        for (MultipartFile file : files) {
            //根据文件名  大货款号匹配数据   67065748_43 替换为-
            //判断是否是图片
            String originalFilename = file.getOriginalFilename();
            CommonUtils.isImage(originalFilename, true);
            String styleNo = FileUtil.mainName(originalFilename);

            if(styleNos.contains(styleNo)){
                throw new OtherException("导入文件名不能重复");
            }

            styleNos.add(styleNo);
            map.put(styleNo,file);
        }

        List<StyleColor> styleColorList = listByField("style_no", styleNos);
        Map<String, List<StyleColor>> styleColorMap = styleColorList.stream().collect(Collectors.groupingBy(StyleColor::getStyleNo));
        List<StyleColorAgent> styleColorAgents = styleColorAgentService.listByField("style_color_no", styleNos);
        Map<String, List<StyleColorAgent>> styleColorAgentMap = styleColorAgents.stream().collect(Collectors.groupingBy(StyleColorAgent::getStyleColorNo));

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();

        for (Map.Entry<String, MultipartFile> stringMultipartFileEntry : map.entrySet()) {
            String styleNo = stringMultipartFileEntry.getKey();
            if(styleColorMap.containsKey(styleNo) && styleColorAgentMap.containsKey(styleNo)){
                String status = styleColorAgentMap.get(styleNo).get(0).getStatus();
                if(!StrUtil.equals(status,"0") && !StrUtil.equals(status,"2")){
                    list4.add(styleNo);
                    continue;
                }
                StyleColor styleColor = styleColorMap.get(styleNo).get(0);
                String id = styleColor.getId();
                String styleId = styleColor.getStyleId();
                try {
                    DelStylePicDto delStylePicDto = new DelStylePicDto();
                    delStylePicDto.setStyleColorId(id);
                    delStylePicDto.setStyleId(styleId);
                    uploadFileService.delStyleImage(delStylePicDto, user);
                    UploadStylePicDto picDto = new UploadStylePicDto();
                    picDto.setFile(stringMultipartFileEntry.getValue());
                    picDto.setStyleColorId(id);
                    picDto.setStyleId(styleId);
                    uploadFileService.uploadStyleImage(picDto, user);
                } catch (Exception e) {
                    //修改失败
                    list1.add(styleNo);
                    continue;
                }
                //修改成功
                list2.add(styleNo);
            }else{
                //大货款号不存在
                list3.add(styleNo);
            }
        }
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("修改失败", String.join(",", list1));
        resultMap.put("修改成功",String.join(",", list2));
        resultMap.put("大货款号不存在",String.join(",", list3));
        resultMap.put("大货款号状态不允许修改",String.join(",", list4));
        return ApiResult.success("成功",resultMap);
    }

    @Override
    public void agentControl(String id) {
        //状态校验
        List<StyleColorAgent> list = styleColorAgentService.listByField("style_color_id", Collections.singletonList(id));
        for (StyleColorAgent styleColorAgent : list) {
            if(!"3".equals(styleColorAgent.getStatus())){
                throw new OtherException("只有可编辑时才能卡控");
            }
        }

        LambdaUpdateWrapper<StyleColorAgent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColorAgent::getStatus,"2");
        updateWrapper.in(StyleColorAgent::getStyleColorId, Collections.singletonList(id));
        styleColorAgentService.update(updateWrapper);
    }

    @Override
    public void agentUnControl(String id) {
        //状态校验
        List<StyleColorAgent> list = styleColorAgentService.listByField("style_color_id",Collections.singletonList(id));
        for (StyleColorAgent styleColorAgent : list) {
            if(!"2".equals(styleColorAgent.getStatus())){
                throw new OtherException("只有重新打开时才能解控");
            }
        }

        LambdaUpdateWrapper<StyleColorAgent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColorAgent::getStatus,"3");
        updateWrapper.in(StyleColorAgent::getStyleColorId, Collections.singletonList(id));
        styleColorAgentService.update(updateWrapper);
    }

    @Override
    @Transactional
    public ApiResult importMarkingOrder(List<Map<String, Object>> readAll) {
        /*if(readAll.size() > 40){
            throw new OtherException("导入数据不能超过40条");
        }*/
        //大货款号清单
        List<String> styleNos = new ArrayList<>();
        //大货款号对应的动态字段，用于后续检查字段是否符合 大货款号对应的企划需求管理
        Map<String, Map<String, Object>> styleColorFields = new HashMap<>();
        //遍历导入数据
        for (Map<String, Object> map : readAll) {
            if (map.containsKey("大货款号")) {
                styleNos.add(map.get("大货款号").toString());
                styleColorFields.put(map.get("大货款号").toString(), map);
            }else{
                throw new OtherException("大货款号不能为空");
            }
        }
        //根据大货款号查询所有数据
        List<StyleColorVo> styleColors = baseMapper.colorList(new QueryWrapper<>().in("tsc.style_no", styleNos));
        Map<String, StyleColorVo> styleColorMap = new HashMap<>();
        List<String> ids = new ArrayList<>();
        List<String> planningSeasonIds = new ArrayList<>();
        List<String> channels = new ArrayList<>();
        List<String> prodCategory1sts = new ArrayList<>();
        List<String> prodCategorys = new ArrayList<>();
        List<String> prodCategory2nds = new ArrayList<>();
        for (StyleColorVo styleColor : styleColors) {
            styleColorMap.put(styleColor.getStyleNo(),styleColor);
            ids.add(styleColor.getId());
            planningSeasonIds.add(styleColor.getPlanningSeasonId());
            channels.add(styleColor.getChannel());
            prodCategory1sts.add(styleColor.getProdCategory1st());
            prodCategorys.add(styleColor.getProdCategory());
            prodCategory2nds.add(styleColor.getProdCategory2nd());
        }

        //查询维度数据，判断修改的字段是否超出配置范围
        //先按照中类查询一遍，如果中类取不到，则按照品类取
        BaseQueryWrapper<PlanningDimensionality> qw = new BaseQueryWrapper<>();
        qw.eq("coefficient_flag",BaseGlobal.YES);
        qw.in("planning_season_id", planningSeasonIds);
        qw.in("channel", channels);
        qw.in("prod_category1st", prodCategory1sts);
        qw.in("prod_category", prodCategorys);
        qw.in("prod_category2nd", prodCategory2nds);
        qw.eq("tpd.del_flag", BaseGlobal.NO);
        List<PlanningDimensionalityVo> planningDimensionalities = planningDimensionalityMapper.getMaterialCoefficient(qw);
        //按照产品季+渠道+大类+品类+中类 分组
        Map<String, List<PlanningDimensionalityVo>> dimensionalityMap = planningDimensionalities.stream().collect(Collectors.groupingBy(
                o -> o.getPlanningSeasonId() + "_" + o.getChannel() + "_" + o.getProdCategory1st()+"_"+o.getProdCategory()+"_"+o.getProdCategory2nd()));
        //按照品类查询一遍
        qw = new BaseQueryWrapper<>();
        qw.eq("coefficient_flag",BaseGlobal.YES);
        qw.in("planning_season_id", planningSeasonIds);
        qw.in("channel", channels);
        qw.in("prod_category1st", prodCategory1sts);
        qw.in("prod_category", prodCategorys);
        qw.isNullStr("prod_category2nd");
        qw.eq("tpd.del_flag", BaseGlobal.NO);
        List<PlanningDimensionalityVo> planningDimensionalities1 = planningDimensionalityMapper.getMaterialCoefficient(qw);
        //按照产品季+渠道+大类+品类 分组
        Map<String, List<PlanningDimensionalityVo>> dimensionalityMap1 = planningDimensionalities1.stream().collect(Collectors.groupingBy(
                o -> o.getPlanningSeasonId() + "_" + o.getChannel() + "_" + o.getProdCategory1st()+"_"+o.getProdCategory()));

        List<String> dictList = new ArrayList<>();
        List<String> structList = new ArrayList<>();
        for (PlanningDimensionalityVo planningDimensionality : planningDimensionalities) {
            if("1".equals(planningDimensionality.getIsOption())){
                dictList.add(planningDimensionality.getOptionDictKey());
            }else if("2".equals(planningDimensionality.getIsOption())){
                structList.add(planningDimensionality.getOptionDictKey());
            }
        }
        for (PlanningDimensionalityVo planningDimensionality : planningDimensionalities1) {
            if("1".equals(planningDimensionality.getIsOption())){
                dictList.add(planningDimensionality.getOptionDictKey());
            }else if("2".equals(planningDimensionality.getIsOption())){
                structList.add(planningDimensionality.getOptionDictKey());
            }
        }

        //将两个里面需要用到的数据字典拉出来一起查询一遍
        Map<String, Map<String, String>> dictInfoToMap = new HashMap<>();
        if(CollUtil.isNotEmpty(dictList)){
            dictList = dictList.stream().distinct().collect(Collectors.toList());
            dictInfoToMap = ccmFeignService.getDictInfoToMapTurnOver(String.join(",", dictList));
        }
        //这里可能要加入字典依赖的查询，后续判断使用TODO

        //结构字典也同理
        Map<String, List<BasicStructureTree>> allByStructureCodes = new HashMap<>();
        if(CollUtil.isNotEmpty(structList)){
            structList = structList.stream().distinct().collect(Collectors.toList());
            allByStructureCodes = ccmFeignService.getAllByStructureCodes(String.join(",", structList));
        }
        Map<String, Map<String, Map<String, String>>> structureTreesMap = allByStructureCodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                o -> o.getValue().stream().collect(Collectors.groupingBy(s->String.valueOf(s.getLevel()), Collectors.toMap(BasicStructureTree::getName, BasicStructureTree::getValue,(v1,v2)->v1)))));

        //查询已经保存的维度数据集合，方便后续修改使用
        List<FieldVal> list = fieldValService.list(ids, FieldValDataGroupConstant.STYLE_MARKING_ORDER);
        Map<String, Map<String,FieldVal>> styleColorFieldMap = list.stream().collect(Collectors.groupingBy(FieldVal::getForeignId,Collectors.toMap(FieldVal::getFieldId, o->o,(v1,v2)->v1)));

        List<FieldVal> updateFieldValList = new ArrayList<>();

        StringBuffer sbMsg = new StringBuffer();

        List<OperaLogEntity> updateLogs = new ArrayList<>();

        int successCount = 0;
        for (Map.Entry<String, Map<String, Object>> entry : styleColorFields.entrySet()) {
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            List<Map<String,String>> updateLogMaps = new ArrayList<>();
            String styleNo = entry.getKey();
            if(!styleColorMap.containsKey(styleNo)){
                //该款不存在
                sbMsg.append("大货款号：").append(styleNo).append(",不存在;");
                continue;
            }
            StyleColorVo styleColor = styleColorMap.get(styleNo);

            String key = styleColor.getPlanningSeasonId() + "_" + styleColor.getChannel() + "_" + styleColor.getProdCategory1st() + "_" + styleColor.getProdCategory();
            //获取该款的动态字段
            List<PlanningDimensionalityVo> dimensionalities = null;
            if(dimensionalityMap.containsKey(key + "_" + styleColor.getProdCategory2nd())){
                dimensionalities = dimensionalityMap.get(key + "_" + styleColor.getProdCategory2nd());
            }else if(dimensionalityMap1.containsKey(key)){
                dimensionalities = dimensionalityMap1.get(key);
            }else{
                //该款没有获取到动态字段
                sbMsg.append("大货款号：").append(styleNo).append(",没有查询到动态字段,如需要,请联系管理员维护;");
                continue;
            }
            Map<String, PlanningDimensionalityVo> collect = dimensionalities.stream().collect(Collectors.toMap(PlanningDimensionalityVo::getFieldExplain, o -> o,(v1,v2)->v1));

            //获取该款已经保存的下单阶段动态字段
            Map<String,FieldVal> fieldValMap = styleColorFieldMap.getOrDefault(styleColor.getId(),new HashMap<>());

            boolean fieldValFlag = true;
            List<FieldVal> fieldValList = new ArrayList<>();
            for (Map.Entry<String, Object> entry1 : entry.getValue().entrySet()) {
                if(!"大货款号".equals(entry1.getKey()) && entry1.getValue() != null && StrUtil.isNotEmpty(String.valueOf(entry1.getValue()))){
                    if(collect.containsKey(entry1.getKey())){
                        String value = String.valueOf(entry1.getValue());
                        String valName = "";
                        //校验是否字典项
                        PlanningDimensionalityVo planningDimensionality = collect.get(entry1.getKey());
                        if("1".equals(planningDimensionality.getIsOption())){
                            //判断使用的是字典项
                            //判断是否多选
                            Map<String, String> orDefault = dictInfoToMap.getOrDefault(planningDimensionality.getOptionDictKey(), new HashMap<>());
                            if("multiple".equals(planningDimensionality.getFieldTypeCoding())){
                                List<String> values = StrUtil.split(value, ",");
                                List<String> vals = new ArrayList<>();
                                for (String s : values) {
                                    if(orDefault.containsKey(s)){
                                        vals.add(orDefault.get(s));
                                    }else{
                                        //字典项不存在
                                        sbMsg.append("大货款号：").append(styleNo).append(",").append(entry1.getKey()).append("中不存在字典值:").append(s).append(";");
                                        fieldValFlag = false;
                                    }
                                }
                                if(CollUtil.isNotEmpty(vals)){
                                    valName = value;
                                    value = String.join(",", vals);
                                }else {
                                    continue;
                                }
                            }else{
                                if(orDefault.containsKey(value)){
                                    valName = value;
                                    value = orDefault.get(value);
                                }else{
                                    //字典项不存在
                                    sbMsg.append("大货款号：").append(styleNo).append(",").append(entry1.getKey()).append("中不存在字典值:").append(value).append(";");
                                    fieldValFlag = false;
                                    continue;
                                }
                            }
                        }else if("2".equals(planningDimensionality.getIsOption())){
                            //还是使用的结构字段项
                            //判断是否多选
                            Map<String, String> structureTrees = structureTreesMap.getOrDefault(planningDimensionality.getOptionDictKey(), new HashMap<>()).getOrDefault(planningDimensionality.getStructureTier(), new HashMap<>());
                            if("multiple".equals(planningDimensionality.getFieldTypeCoding())){
                                List<String> values = StrUtil.split(value, ",");
                                List<String> vals = new ArrayList<>();
                                for (String s : values) {
                                    if(structureTrees.containsKey(s)){
                                        vals.add(structureTrees.get(s));
                                    }else{
                                        //字典项不存在
                                        sbMsg.append("大货款号：").append(styleNo).append(",").append(entry1.getKey()).append("中不存在字典值:").append(s).append(";");
                                        fieldValFlag = false;
                                    }
                                }
                                if(CollUtil.isNotEmpty(vals)){
                                    valName = value;
                                    value = String.join(",", vals);
                                }else {
                                    continue;
                                }
                            }else {
                                if(structureTrees.containsKey(value)){
                                    valName = value;
                                    value = structureTrees.get(value);
                                }else{
                                    //结构字典项不存在
                                    sbMsg.append("大货款号：").append(styleNo).append(",").append(entry1.getKey()).append("中不存在字典值:").append(value).append(";");
                                    fieldValFlag = false;
                                    continue;
                                }
                            }
                        }
                        Map<String,String> updateLogMap = new HashMap<>();
                        updateLogMap.put("name",planningDimensionality.getFieldExplain());
                        updateLogMap.put("newStr",StrUtil.isEmpty(valName)?value:valName);
                        if(fieldValMap.containsKey(planningDimensionality.getFieldId())){
                            FieldVal fieldVal = fieldValMap.get(planningDimensionality.getFieldId());
                            if(StrUtil.equals(StrUtil.isEmpty(valName)?value:valName,StrUtil.isEmpty(fieldVal.getValName())?fieldVal.getVal():fieldVal.getValName())){
                                //没有修改时跳过
                                continue;
                            }
                            updateLogMap.put("oldStr",StrUtil.isEmpty(fieldVal.getValName())?fieldVal.getVal():fieldVal.getValName());
                            fieldVal.setVal(value);
                            fieldVal.setValName(valName);
                            fieldValList.add(fieldVal);
                        }else{
                            updateLogMap.put("oldStr","");
                            FieldVal fieldVal = new FieldVal();
                            fieldVal.setForeignId(styleColor.getId());
                            fieldVal.setDataGroup(FieldValDataGroupConstant.STYLE_MARKING_ORDER);
                            fieldVal.setFieldId(planningDimensionality.getFieldId());
                            fieldVal.setFieldName(planningDimensionality.getFieldName());
                            fieldVal.setFieldExplain(planningDimensionality.getFieldExplain());
                            fieldVal.setVal(value);
                            fieldVal.setValName(valName);
                            fieldVal.insertInit();
                            fieldValList.add(fieldVal);
                        }
                        updateLogMaps.add(updateLogMap);
                    }else{
                        //该款没有这个动态字段
                        sbMsg.append("大货款号：").append(styleNo).append(",没有查询到动态字段").append(entry1.getKey()).append(",如需要,请联系管理员维护;");
                        fieldValFlag = false;
                    }
                }
            }
            if(fieldValFlag){
                updateFieldValList.addAll(fieldValList);
                operaLogEntity.setJsonContent(JSONObject.toJSONString(updateLogMaps));
                operaLogEntity.setType("修改");
                operaLogEntity.setDocumentId(styleColor.getId());
                operaLogEntity.setName("款式打标-批量导入修改");
                operaLogEntity.setDocumentName(styleNo);
                updateLogs.add(operaLogEntity);
                successCount++;
            }
        }
        if(CollUtil.isNotEmpty(updateFieldValList)){
            List<String> collect = updateFieldValList.stream().map(BaseEntity::getId).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
            List<List<String>> partition = Lists.partition(collect, 1000);
            for (List<String> strings : partition) {
                fieldValService.removeByIds(strings);
            }

            fieldValService.saveBatch(updateFieldValList);
        }

        StringBuffer sbMsg1 = new StringBuffer();

        if(readAll.size() != successCount){
            sbMsg1.append("导入：").append(readAll.size()).append("条,成功：").append(successCount).append("条,失败原因如下;").append(sbMsg);
        }else{
            sbMsg1.append("导入：").append(readAll.size()).append("条,成功：").append(successCount).append("条;");
        }

        String[] idsUpdate = updateFieldValList.stream().map(FieldVal::getForeignId).distinct().toArray(String[]::new);

        //保存修改记录
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setType("导入");
        operaLogEntity.setDocumentId("导入结果");
        operaLogEntity.setName("款式打标-批量导入修改");
        operaLogEntity.setDocumentName("导入结果");
        operaLogEntity.setContent(sbMsg1.toString());
        updateLogs.add(operaLogEntity);
        operaLogService.saveBatch(updateLogs);


        if(idsUpdate.length > 0){
            //推送下游系统
            smpService.goodsAsync(idsUpdate,"BCS",null);
        }

        return ApiResult.success(sbMsg1.toString());
    }


}
