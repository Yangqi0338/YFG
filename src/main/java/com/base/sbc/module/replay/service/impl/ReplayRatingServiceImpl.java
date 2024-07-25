/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.constant.BusinessProperties;
import com.base.sbc.config.constant.FieldValProperties;
import com.base.sbc.config.constant.ReplayRatingProperties;
import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProcessDatabaseType;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingLevelType;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.enums.business.replay.ReplayRatingWarnType;
import com.base.sbc.config.enums.business.smp.SaleFacResultType;
import com.base.sbc.config.enums.business.smp.SluggishSaleWeekendsType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.enums.SeasonEnum;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.UseStyleDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryBrand;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.service.PatternLibraryItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.replay.dto.ProductionSaleDTO;
import com.base.sbc.module.replay.dto.ReplayConfigDTO;
import com.base.sbc.module.replay.dto.ReplayConfigDetailDTO;
import com.base.sbc.module.replay.dto.ReplayConfigTimeDTO;
import com.base.sbc.module.replay.dto.ReplayRatingDetailList;
import com.base.sbc.module.replay.dto.ReplayRatingFabricDTO;
import com.base.sbc.module.replay.dto.ReplayRatingPatternDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO.ProductionInfoDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO.SaleLevelDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO.SupplierInfo;
import com.base.sbc.module.replay.dto.ReplayRatingTransferDTO;
import com.base.sbc.module.replay.dto.ReplayRatingYearProductionSaleDTO;
import com.base.sbc.module.replay.entity.ReplayConfig;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.entity.ReplayRatingDetail;
import com.base.sbc.module.replay.mapper.ReplayRatingMapper;
import com.base.sbc.module.replay.service.ReplayConfigService;
import com.base.sbc.module.replay.service.ReplayRatingDetailService;
import com.base.sbc.module.replay.service.ReplayRatingService;
import com.base.sbc.module.replay.vo.ReplayConfigQO;
import com.base.sbc.module.replay.vo.ReplayRatingBulkWarnVO;
import com.base.sbc.module.replay.vo.ReplayRatingFabricTotalVO;
import com.base.sbc.module.replay.vo.ReplayRatingFabricVO;
import com.base.sbc.module.replay.vo.ReplayRatingPageVO;
import com.base.sbc.module.replay.vo.ReplayRatingPatternTotalVO;
import com.base.sbc.module.replay.vo.ReplayRatingPatternVO;
import com.base.sbc.module.replay.vo.ReplayRatingQO;
import com.base.sbc.module.replay.vo.ReplayRatingStyleTotalVO;
import com.base.sbc.module.replay.vo.ReplayRatingStyleVO;
import com.base.sbc.module.replay.vo.ReplayRatingVO;
import com.base.sbc.module.replay.vo.ReplayRatingYearQO;
import com.base.sbc.module.replay.vo.ReplayRatingYearVO;
import com.base.sbc.module.smp.dto.FactoryMissionRateQO;
import com.base.sbc.module.smp.dto.GoodsSluggishSalesDTO;
import com.base.sbc.module.smp.dto.GoodsSluggishSalesQO;
import com.base.sbc.module.smp.dto.SaleFacQO;
import com.base.sbc.module.smp.entity.FactoryMissionRate;
import com.base.sbc.module.smp.entity.GoodsSluggishSales;
import com.base.sbc.module.smp.entity.SaleFac;
import com.base.sbc.module.smp.entity.StockSize;
import com.base.sbc.module.smp.mapper.FactoryMissionRateMapper;
import com.base.sbc.module.smp.mapper.GoodsSluggishSalesMapper;
import com.base.sbc.module.smp.mapper.SaleFacMapper;
import com.base.sbc.module.smp.mapper.StockSizeMapper;
import com.base.sbc.module.style.dto.StyleBomSearchDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.BI_CV;
import static com.base.sbc.module.common.convert.ConvertContext.REPLAY_CV;
import static com.base.sbc.module.replay.dto.ReplayRatingFabricDTO.FabricMonthDataDto;

/**
 * 类描述：基础资料-复盘评分 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.service.ReplayRatingService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Service
public class ReplayRatingServiceImpl extends BaseServiceImpl<ReplayRatingMapper, ReplayRating> implements ReplayRatingService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private ReplayRatingDetailService replayRatingDetailService;

    @Autowired
    private ReplayConfigService replayConfigService;

    @Autowired
    private PlanningSeasonService planningSeasonService;

    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private StyleColorService styleColorService;

    @Autowired
    private StyleService styleService;

    @Autowired
    private PatternLibraryService patternLibraryService;

    @Autowired
    private OrderBookDetailService orderBookDetailService;

    @Autowired
    private FieldValService fieldValService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private PatternLibraryTemplateService patternLibraryTemplateService;

    @Autowired
    private PatternLibraryItemService patternLibraryItemService;

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private PackInfoService packInfoService;

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;

    @Autowired
    private BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    @Autowired
    private BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    @Autowired
    private ProcessDatabaseService processDatabaseService;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private GoodsSluggishSalesMapper sluggishSalesMapper;

    @Autowired
    private SaleFacMapper saleFacMapper;

    @Autowired
    private FactoryMissionRateMapper factoryMissionRateMapper;

    @Autowired
    private StockSizeMapper stockSizeMapper;

    private static @NotNull BaseQueryWrapper<ReplayRating> buildQueryWrapper(ReplayRatingQO dto) {
        BaseQueryWrapper<ReplayRating> qw = new BaseQueryWrapper<>();
        String alias = "tsc.";
        if (dto.getType() == ReplayRatingType.FABRIC) {
            alias = "ts.";
        }
        qw.notEmptyEq(alias + "planning_season_id", dto.getPlanningSeasonId());
        qw.notEmptyEq(alias + "band_name", dto.getBandName());
        qw.notEmptyEq("ts.prod_category1st", dto.getProdCategory1st());
        qw.notEmptyEq("ts.prod_category", dto.getProdCategory());
        qw.notEmptyEq("ts.prod_category2nd", dto.getProdCategory2nd());
        qw.notEmptyEq("ts.prod_category3rd", dto.getProdCategory3rd());
//        qw.notEmptyEq("tsc.plan_season_id", dto.getSaleLevel());
        qw.notEmptyEq("ts.design_no", dto.getDesignNo());
        qw.ne("ts.dev_class", dto.getNoDevClass());
        qw.notEmptyEq("tsc.style_no", dto.getBulkStyleNo());
        qw.notEmptyEq("trr.rating_flag", dto.getRatingFlag());
        qw.notEmptyEq("ts.registering_id", dto.getRegisteringId());
        qw.notEmptyEq("ts.registering_no", dto.getRegisteringNo());
        qw.notEmptyEq("ifnull(tpl.place_order_style_nos like CONCAT('%', tsc.style_no, '%'), 0)", dto.getTransferPatternFlag());

        if (dto.getType() != ReplayRatingType.STYLE) {
            qw.notEmptyEq("tpb.material_code", dto.getMaterialCode());
        } else {
            qw.exists(StrUtil.isNotBlank(dto.getMaterialCode()), dto.getMaterialCodeSql(), dto.getMaterialCode());
        }
        qw.exists(StrUtil.isNotBlank(dto.getSilhouetteName()), dto.getSilhouetteSql(), dto.getGroupName(), dto.getFieldExplain(), dto.getSilhouetteName());
        qw.notEmptyEq("tpb.supplier_id", dto.getSupplierId());
        qw.notEmptyEq("tpl.color", dto.getColorCode());
//        qw.notEmptyEq("tpl.plan_season_id", dto.getMaterialOwnResearchFlag());
        return qw;
    }

    @Override
    public ReplayRatingPageVO<? extends ReplayRatingVO> queryPageInfo(ReplayRatingQO qo) {
        // 设置廓形请求参
        qo.setGroupName(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        qo.setFieldExplain(FieldValProperties.silhouette);
        // 构建基础ew
        BaseQueryWrapper<ReplayRating> queryWrapper = buildQueryWrapper(qo);
        // 加入动态列和数据权限
        QueryGenerator.initQueryWrapperByMap(queryWrapper, qo);
        // 开启分页
        Page<? extends ReplayRatingVO> page = qo.startPage();
        Map<String, Object> totalMap = new HashMap<>();
        switch (qo.getType()) {
            case STYLE:
                // 单款复盘以设计款排序
                queryWrapper.orderByAsc("tsc.design_no");
                // 装饰列表
                decorateStyleList(baseMapper.queryStyleList(queryWrapper, qo), qo);
                // 会从queryStyleList_COUNT获取分页的其他数据并转化实体类,详见SqlUtil 355
                ReplayRatingStyleTotalVO totalStyleVO = BeanUtil.toBean(page.getAttributes(), ReplayRatingStyleTotalVO.class);
                if (qo.needSpecialTotalSum()) {
                    // 根据大货款号获取生产销售数据
                    String bulkStyleNo = totalStyleVO.getBulkStyleNo();
                    if (StrUtil.isBlank(bulkStyleNo)) break;
                    SaleFacQO saleFacQO = new SaleFacQO();
                    saleFacQO.setBulkStyleNo(bulkStyleNo);
                    List<ProductionSaleDTO> productionSaleList = findProductionSaleList(saleFacQO);
                    // 直接通过多个计算合计
                    totalStyleVO.decorateTotal(productionSaleList);
                }
                // 最后将汇总实体类转成map
                totalMap.putAll(BeanUtil.beanToMap(totalStyleVO));
                break;
            case PATTERN:
                // 装饰列表
                decoratePatternList(baseMapper.queryPatternList(queryWrapper, qo), qo);
                // 会从queryPatternList_COUNT获取分页的其他数据并转化实体类,详见SqlUtil 355
                ReplayRatingPatternTotalVO totalPatternVO = BeanUtil.toBean(page.getAttributes(), ReplayRatingPatternTotalVO.class);
                // 若需要特殊查询某个汇总数据(多半是上面的count无法连表或性能差)
                if (qo.needSpecialTotalSum()) {
                    // 根据大货款号获取生产销售数据
                    String bulkStyleNo = totalPatternVO.getBulkStyleNo();
                    if (StrUtil.isBlank(bulkStyleNo)) break;
                    List<SaleFac> saleFacList = saleFacMapper.selectList(new BaseLambdaQueryWrapper<SaleFac>()
                            .notEmptyIn(SaleFac::getBulkStyleNo, bulkStyleNo)
                            .select(SaleFac::getProductionType, SaleFac::getNum)
                    );
                    // 判断其是否投产,设置到对应的合计数据
                    saleFacList.stream().collect(Collectors.groupingBy(SaleFac::isProduction)).forEach((isProduction, sameTypeList) -> {
                        BigDecimal sum = CommonUtils.sumBigDecimal(sameTypeList, SaleFac::getNum);
                        if (isProduction) totalPatternVO.setSeasonProductionCount(sum);
                        else totalPatternVO.setSeasonSaleCount(sum);
                    });
                }
                // 最后将汇总实体类转成map
                totalMap.putAll(BeanUtil.beanToMap(totalPatternVO));
                break;
            case FABRIC:
                // 若不是动态列,加入groupBy
                if (!qo.isColumnGroupSearch()) {
                    queryWrapper.groupBy("tpb.material_code", "tpb.foreign_id");
//                    queryWrapper.orderByAsc("tpb.material_code", "tpb.color");
                }
                // 装饰列表
                decorateFabricList(baseMapper.queryFabricList(queryWrapper, qo), qo);
                // 会从queryFabricList_COUNT获取分页的其他数据并转化实体类,详见SqlUtil 355
                ReplayRatingFabricTotalVO totalFabricVO = BeanUtil.toBean(page.getAttributes(), ReplayRatingFabricTotalVO.class);
                // 若需要特殊查询某个汇总数据(多半是上面的count无法连表或性能差)
                if (qo.needSpecialTotalSum()) {
                    // 根据大货款号获取生产数据
                    String bulkStyleNo = totalFabricVO.getBulkStyleNo();
                    if (StrUtil.isBlank(bulkStyleNo)) break;
                    List<SaleFac> saleFacList = saleFacMapper.selectList(new BaseLambdaQueryWrapper<SaleFac>()
                            .notEmptyIn(SaleFac::getBulkStyleNo, bulkStyleNo)
                            .in(SaleFac::getResultType, SaleFacResultType.productionList())
                            .select(SaleFac::getNum)
                    );
                    totalFabricVO.setProduction(CommonUtils.sumBigDecimal(saleFacList, SaleFac::getNum));
                    // TODO
                    totalFabricVO.setRemainingMaterial(new BigDecimal(Integer.MAX_VALUE));
                }
                totalMap.putAll(BeanUtil.beanToMap(totalFabricVO));
                break;
            default:
                throw new UnsupportedOperationException("不受支持的复盘类型");
        }
        PageInfo<? extends ReplayRatingVO> pageInfo = page.toPageInfo();

        // 设置汇总数据,通过JsonAnyGetter平铺数据
        ReplayRatingPageVO<? extends ReplayRatingVO> pageVo = BeanUtil.copyProperties(pageInfo, ReplayRatingPageVO.class);
        pageVo.setTotalMap(totalMap);
        return pageVo;
    }

    private <T extends ReplayRatingVO> List<T> decorateList(List<T> list, ReplayRatingQO qo) {
        // 若是动态列, 直接返回
        if (qo.isColumnGroupSearch()) return list;
        // 获取产品季名字和大货款图
        List<String> planningSeasonIdList = list.stream().map(ReplayRatingVO::getPlanningSeasonId).distinct().collect(Collectors.toList());
        Map<String, String> planningSeasonNameMap = planningSeasonService.mapOneField(new LambdaQueryWrapper<PlanningSeason>()
                        .in(PlanningSeason::getId, planningSeasonIdList)
                , PlanningSeason::getId, PlanningSeason::getName);
        stylePicUtils.setStyleColorPic2(list);
        list.forEach(it -> it.setPlanningSeasonName(planningSeasonNameMap.getOrDefault(it.getPlanningSeasonId(), "")));
        return list;
    }

    private void decorateStyleList(List<ReplayRatingStyleVO> styleVOList, ReplayRatingQO qo) {
        if (CollUtil.isEmpty(styleVOList)) return;
        // 先普通装饰
        decorateList(styleVOList, qo);

        // 获取版型id 用于版型复盘跳转
        List<String> styleIdList = styleVOList.stream().map(ReplayRatingStyleVO::getStyleId).collect(Collectors.toList());
        Map<String, String> patternIdMap = patternLibraryService.mapOneField(new LambdaQueryWrapper<PatternLibrary>()
                        .in(PatternLibrary::getStyleId, styleIdList)
                , PatternLibrary::getStyleId, PatternLibrary::getId);

        SaleFacQO saleFacQO = new SaleFacQO();
        saleFacQO.setBulkStyleNo(styleVOList.stream().map(ReplayRatingStyleVO::getBulkStyleNo).collect(Collectors.joining(COMMA)));
        List<ProductionSaleDTO> productionSaleList = findProductionSaleList(saleFacQO);
        styleVOList.forEach(styleDTO -> {
            // 版型id
            styleDTO.setGotoPatternId(patternIdMap.getOrDefault(styleDTO.getStyleId(), ""));
            // 获取所有销售记录
            styleDTO.setProductionSaleDTO(new ProductionSaleDTO().decorateTotal(
                    productionSaleList.stream().filter(it -> it.getBulkStyleNo().equals(styleDTO.getBulkStyleNo())).collect(Collectors.toList()))
            );
        });
    }

    /**
     * 根据某个key,获取维度数据,并将val和valName设置到对应的值上
     */
    private <T> void decorateFieldVal(List<T> sourceList, QueryFieldManagementDto qo, Function<T, String> keyFunc, BiConsumer<T, String> codeSetFunc, BiConsumer<T, String> nameSetFunc) {
        List<FieldVal> fieldValList = fieldValService.list(new BaseLambdaQueryWrapper<FieldVal>()
                .in(FieldVal::getForeignId, sourceList.stream().map(keyFunc).collect(Collectors.toList()))
                .eq(FieldVal::getDataGroup, qo.getGroupName())
                .eq(FieldVal::getFieldExplain, qo.getFieldExplain())
        );
        sourceList.forEach(source -> {
            // 获取时间最新的
            fieldValList.stream()
                    .filter(it -> it.getForeignId().equals(keyFunc.apply(source)))
                    .max(Comparator.comparing(FieldVal::getUpdateDate))
                    .ifPresent(fieldVal -> {
                        nameSetFunc.accept(source, fieldVal.getValName());
                        codeSetFunc.accept(source, fieldVal.getVal());
                    });
        });
    }

    private void decoratePatternList(List<ReplayRatingPatternVO> patternVOList, ReplayRatingQO qo) {
        if (CollUtil.isEmpty(patternVOList)) return;
        // 先普通装饰
        decorateList(patternVOList, qo);

        List<String> bulkStyleNoList = patternVOList.stream().map(ReplayRatingPatternVO::getBulkStyleNo).collect(Collectors.toList());

        /* ----------------------------获取廓形---------------------------- */

        QueryFieldManagementDto queryDto = new QueryFieldManagementDto();
        queryDto.setGroupName(qo.getGroupName());
        queryDto.setFieldExplain(qo.getFieldExplain());
        decorateFieldVal(patternVOList, queryDto, ReplayRatingPatternVO::getStyleId, ReplayRatingPatternVO::setSilhouetteCode, ReplayRatingPatternVO::setSilhouetteName);

        /* ----------------------------获取版型成功率---------------------------- */

        decoratePatternSuccessRate(patternVOList, ReplayRatingPatternVO::getPatternLibraryId, ReplayRatingPatternVO::setUseCount, ReplayRatingPatternVO::setProductionCount);

        /* ----------------------------当季投产销售---------------------------- */

        // 查询
        if (CollUtil.isEmpty(bulkStyleNoList)) return;
        List<SaleFac> saleFacList = saleFacMapper.selectList(new LambdaQueryWrapper<SaleFac>()
                .in(SaleFac::getBulkStyleNo, bulkStyleNoList)
        );
        patternVOList.forEach(pattern -> {
            saleFacList.stream()
                    .filter(it -> it.getBulkStyleNo().equals(pattern.getBulkStyleNo()))
                    .collect(Collectors.groupingBy(it -> StrUtil.isNotBlank(it.getProductionType())))
                    .forEach((isSale, sameTypeList) -> {
                        BigDecimal sum = CommonUtils.sumBigDecimal(sameTypeList, SaleFac::getNum);
                        if (isSale) {
                            pattern.setSeasonSaleCount(sum);
                        } else {
                            pattern.setSeasonProductionCount(sum);
                        }
                    });
        });
    }

    private <T> void decoratePatternSuccessRate(List<T> list, Function<T, String> registeringIdFunc, BiConsumer<T, BigDecimal> divisorFunc, BiConsumer<T, BigDecimal> dividendFunc) {
        String registeringId = list.stream().map(registeringIdFunc).distinct().collect(Collectors.joining(COMMA));
        /* ----------------------------获取使用款数量---------------------------- */
        Map<String, Integer> useCountMap = new HashMap<>();
        if (StrUtil.isNotBlank(registeringId)) {
            UseStyleDTO useStyleDTO = new UseStyleDTO();
            useStyleDTO.setPatternLibraryId(registeringId);
            useCountMap.putAll(patternLibraryService.patternUseCountMap(useStyleDTO));
        }

        /* ----------------------------获取版型成功率---------------------------- */
        Map<String, Integer> patternSuccessCountMap = new HashMap<>();
        if (MapUtil.isNotEmpty(useCountMap)) {
            OrderBookDetailQueryDto queryDto = new OrderBookDetailQueryDto();
            queryDto.setRegisteringId(String.join(COMMA, useCountMap.keySet()));
            queryDto.setGroupBy("ts.registering_id");
            patternSuccessCountMap.putAll(orderBookDetailService.patternSuccessCountMap(queryDto));
        }

        list.forEach(obj -> {
            divisorFunc.accept(obj, BigDecimal.valueOf(useCountMap.getOrDefault(registeringIdFunc.apply(obj), 0)));
            dividendFunc.accept(obj, BigDecimal.valueOf(patternSuccessCountMap.getOrDefault(registeringIdFunc.apply(obj), 0)));
        });
    }

    private void decorateFabricList(List<ReplayRatingFabricVO> fabricVOList, ReplayRatingQO qo) {
        if (qo.isColumnGroupSearch() || CollUtil.isEmpty(fabricVOList)) return;

        /* ----------------------------大货款维度数据---------------------------- */
        List<String> styleColorIdList = fabricVOList.stream().map(ReplayRatingFabricVO::getStyleColorId).collect(Collectors.toList());
        if (CollUtil.isEmpty(styleColorIdList)) return;
        Map<String, String> styleColorPicMap = styleColorService.mapOneField(new LambdaQueryWrapper<StyleColor>()
                        .in(StyleColor::getId, styleColorIdList)
                , StyleColor::getId, StyleColor::getStyleColorPic);
        fabricVOList.forEach(fabric -> {
            fabric.setStyleColorPic(styleColorPicMap.getOrDefault(fabric.getStyleColorId(), ""));
        });
        stylePicUtils.setStyleColorPic2(fabricVOList, "imageUrl");

        decorateList(fabricVOList, qo);

        /* ----------------------------面料自主研发 + 剩余备料---------------------------- */
        AtomicInteger remainingMaterial = new AtomicInteger(100);
        AtomicInteger fabricOwnDevelopFlagAtomic = new AtomicInteger();
        fabricVOList.forEach(fabric -> {
            int increment = fabricOwnDevelopFlagAtomic.getAndIncrement();
            // 是否交替 TODO
            fabric.setFabricOwnDevelopFlag(YesOrNoEnum.findByValue(increment < 2 ? increment : (increment / 2) % 2));
            // TODO
            fabric.setRemainingMaterial(BigDecimal.valueOf(remainingMaterial.getAndIncrement()));
        });

        /* ----------------------------大货款维度投产量 + 使用米数---------------------------- */
        List<String> bulkStyleNoList = fabricVOList.stream().map(ReplayRatingFabricVO::getBulkStyleNo).collect(Collectors.toList());
        if (CollUtil.isEmpty(bulkStyleNoList)) return;
        List<SaleFac> saleFacList = saleFacMapper.selectList(new LambdaQueryWrapper<SaleFac>().in(SaleFac::getBulkStyleNo, bulkStyleNoList)
                .in(SaleFac::getResultType, SaleFacResultType.productionList())
                .select(SaleFac::getBulkStyleNo, SaleFac::getNum)
        );
        fabricVOList.forEach(fabric -> {
            fabric.setProduction(CommonUtils.sumBigDecimal(saleFacList.stream()
                    .filter(it -> it.getBulkStyleNo().equals(fabric.getBulkStyleNo())).collect(Collectors.toList()), SaleFac::getNum));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doSave(ReplayRatingSaveDTO replayRatingSaveDTO) {
        /* ----------------------------保存主体---------------------------- */
        String id = replayRatingSaveDTO.getId();
        ReplayRating replayRating = new ReplayRating();
        if (StrUtil.isNotBlank(id)) {
            this.warnMsg("无效的主键id");
            replayRating = this.findOne(id);
        }
        REPLAY_CV.copy2Entity(replayRating, replayRatingSaveDTO);

        this.saveOrUpdate(replayRating);

        /* ----------------------------保存评分和后续改善---------------------------- */
        String replayRatingId = replayRating.getId();

        List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                .eq(ReplayRatingDetail::getReplayRatingId, replayRatingId));

        // 根据详情类型分割
        replayRatingSaveDTO.getDetailListMap().forEach((replayRatingDetailType, replayRatingDetailListDTO) -> {
            List<ReplayRatingDetail> newReplayRatingDetailList = replayRatingDetailListDTO.stream().map(replayRatingDetailDTO -> {
                // 数据库存在的 > 新创建
                ReplayRatingDetail newReplayRatingDetail = replayRatingDetailList.stream()
                        .filter(it -> it.getId().equals(replayRatingDetailDTO.getId())).findFirst().orElse(new ReplayRatingDetail());
                // 移除数据库数据
                replayRatingDetailList.remove(newReplayRatingDetail);

                // 修正详情数据
                replayRatingDetailDTO.setType(replayRatingDetailType);
                replayRatingDetailDTO.setReplayRatingId(replayRatingId);
                REPLAY_CV.copy(newReplayRatingDetail, replayRatingDetailDTO);
                newReplayRatingDetail.updateInit();

                // 发送钉钉消息
//                MessageSendClient.dingMsg(ReplayRatingProperties.dingMsgCode,ReplayRatingProperties.dingMsgContent,
//                        Arrays.asList(replayRatingDetailDTO.getUserId(), replayRatingDetailDTO.getFollowUserId()));
                return newReplayRatingDetail;
            }).collect(Collectors.toList());
            replayRatingDetailService.saveOrUpdateBatch(newReplayRatingDetailList);
        });
        // 移除修改后需要删除的数据
        if (CollUtil.isNotEmpty(replayRatingDetailList)) {
            replayRatingDetailService.removeByIds(replayRatingDetailList);
        }

        return replayRatingId;
    }

    @Override
    public ReplayRatingStyleDTO getStyleById(String styleColorId) {
        /* ----------------------------获取大货款/设计款/复盘评分/复盘管理---------------------------- */
        styleColorService.warnMsg("未找到对应大货款");
        StyleColor styleColor = styleColorService.findOne(styleColorId);
        styleColor.setStyleColorPic(stylePicUtils.getStyleUrl(styleColor.getStyleColorPic()));

        styleColorService.warnMsg("未找到对应款式");
        Style style = styleService.findOne(styleColor.getStyleId());

        ReplayRatingStyleDTO result = new ReplayRatingStyleDTO();
        ReplayRating replayRating = this.findOne(new LambdaQueryWrapper<ReplayRating>()
                .eq(ReplayRating::getType, result.getType())
                .eq(ReplayRating::getForeignId, styleColorId)
        );

        ReplayConfigQO configQO = new ReplayConfigQO();
        configQO.reset2QueryFirst();
        configQO.setBrand(style.getBrand());
        ReplayConfigDTO replayConfig = replayConfigService.queryPageInfo(configQO).getList().stream().findFirst().orElse(new ReplayConfigDTO());

        /* ----------------------------组装数据---------------------------- */
        REPLAY_CV.copy(result, style);
        REPLAY_CV.copy(result, styleColor);

        if (replayRating != null) {
            // 获取评分详情
            REPLAY_CV.copy(result, replayRating);
            decorateReplayRatingDetail(result);
        }
        // 销售等级周
        List<String> saleCycleList = StrUtil.split(replayConfig.getSaleCycle(), COMMA);
        result.setSaleLevelWeekends(CollUtil.removeEmpty(saleCycleList));

        // 设置款式基础数据
        QueryFieldManagementDto qo = new QueryFieldManagementDto();
        qo.setGroupName(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        if (StrUtil.isBlank(result.getSilhouetteCode())) {
            qo.setFieldExplain(FieldValProperties.silhouette);
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setSilhouetteCode, ReplayRatingStyleDTO::setSilhouetteName);
        }
        if (StrUtil.isBlank(result.getColorSystemCode())) {
            qo.setFieldExplain(FieldValProperties.colorSystem);
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setColorSystemCode, ReplayRatingStyleDTO::setColorSystem);
        }
        if (StrUtil.isBlank(result.getFabricComposition())) {
            qo.setFieldExplain(FieldValProperties.fabricComposition);
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setFabricComposition, ReplayRatingStyleDTO::setFabricComposition);
        }
        if (StrUtil.isBlank(result.getPositioning())) {
            qo.setFieldExplain(FieldValProperties.positioning);
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setPositioning, ReplayRatingStyleDTO::setPositioningName);
        }

        // 设置能否跳到版型复盘
        String registeringId = result.getRegisteringId();
        if (StrUtil.isNotBlank(registeringId)) {
            String registeringStyleId = patternLibraryService.findByIds2OneField(registeringId, PatternLibrary::getStyleId);
            if (StrUtil.isNotBlank(registeringStyleId)) {
                result.setGotoPatternId(styleColorService.findOneField(new LambdaQueryWrapper<StyleColor>().eq(StyleColor::getStyleId, registeringStyleId), StyleColor::getId));
            }
        }

        // 获取特定年份 + 复盘管理定义周 的款式等级数据
        GoodsSluggishSalesQO sluggishSalesQO = new GoodsSluggishSalesQO();
        sluggishSalesQO.setBulkStyleNo(result.getBulkStyleNo());
        sluggishSalesQO.setYear(Arrays.stream(ReplayRatingProperties.years).boxed().collect(Collectors.toList()));
        sluggishSalesQO.setWeekends(result.getSaleLevelWeekends());
        sluggishSalesQO.setBrand(result.getBrand());
        result.setSaleLevelList(findSaleLevelList(sluggishSalesQO));

        // 获取所有销售记录
        SaleFacQO saleFacQO = new SaleFacQO();
        saleFacQO.setBulkStyleNo(result.getBulkStyleNo());
        result.setProductionSaleList(findProductionSaleList(saleFacQO));

        // 获取所有生产记录
        FactoryMissionRateQO factoryMissionRateQO = new FactoryMissionRateQO();
        factoryMissionRateQO.setBulkStyleNo(result.getBulkStyleNo());
        result.setProductionInfoList(findProductionInfoList(factoryMissionRateQO));

        return result;
    }

    /**
     * 获取
     */
    private List<SaleLevelDTO> findSaleLevelList(GoodsSluggishSalesQO sluggishSalesQO) {
        List<String> saleCycleList = sluggishSalesQO.getWeekends();
        List<String> years = sluggishSalesQO.getYear().stream().map(it -> it + ReplayRatingProperties.yearCodeSuffix).collect(Collectors.toList());
        List<SaleLevelDTO> list = new ArrayList<>();

        String bulkStyleNo = sluggishSalesQO.getBulkStyleNo();
        if (StrUtil.isBlank(bulkStyleNo)) return list;

        // 转化一下Weekends
        List<SluggishSaleWeekendsType> weekendsTypeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(saleCycleList)) {
            weekendsTypeList.addAll(saleCycleList.stream().map(SluggishSaleWeekendsType::findByText).collect(Collectors.toList()));
        }

        // 查询销售数据
        List<GoodsSluggishSales> goodsSluggishSalesList = sluggishSalesMapper.selectList(new BaseLambdaQueryWrapper<GoodsSluggishSales>()
                .notEmptyIn(GoodsSluggishSales::getBulkStyleNo, bulkStyleNo)
                .notEmptyIn(GoodsSluggishSales::getYear, years)
                .notEmptyIn(GoodsSluggishSales::getWeekends, weekendsTypeList)
        );
        List<GoodsSluggishSalesDTO> salesList = BI_CV.copyList2DTO(goodsSluggishSalesList);

        // 遍历年份
        for (String year : years) {
            int yearInt = (int) Double.parseDouble(year);
            SaleLevelDTO levelDTO = new SaleLevelDTO();
            levelDTO.setType(ReplayRatingLevelType.LEVEL);
            levelDTO.setYear(yearInt + ReplayRatingProperties.yearNameSuffix);
            SaleLevelDTO avgDTO = new SaleLevelDTO();
            avgDTO.setType(ReplayRatingLevelType.AVG);
            avgDTO.setYear(yearInt + ReplayRatingProperties.yearNameSuffix);

            if (CollUtil.isNotEmpty(weekendsTypeList)) {
                weekendsTypeList.stream().collect(Collectors.toMap(Function.identity(), (weekends) ->
                        salesList.stream().filter(it -> it.getYear().equals(year) && it.getWeekends() == weekends).findFirst()
                )).forEach((weekends, yearSales) -> {
                    levelDTO.getWeekendDataMap().put(weekends, yearSales.map(GoodsSluggishSales::getLevel).orElse(null));
                    avgDTO.getWeekendDataMap().put(weekends, yearSales.map(GoodsSluggishSales::getAvg).orElse(BigDecimal.ZERO));
                });
                list.add(levelDTO);
                list.add(avgDTO);
            }
        }
        return list;
    }

    // 生产销售
    private List<ProductionSaleDTO> findProductionSaleList(SaleFacQO saleFacQO) {
        List<ProductionSaleDTO> list = new ArrayList<>();

        String bulkStyleNo = saleFacQO.getBulkStyleNo();
        if (StrUtil.isBlank(bulkStyleNo)) return list;

        LambdaQueryWrapper<SaleFac> queryWrapper = new BaseLambdaQueryWrapper<SaleFac>()
                .notEmptyIn(SaleFac::getBulkStyleNo, bulkStyleNo);
        List<SaleFac> sourceSaleFacList = saleFacMapper.selectList(queryWrapper);
        List<Map<String, Object>> sizeMapList = saleFacMapper.findSizeMap(queryWrapper);
        if (sizeMapList.isEmpty()) return list;

        // 获取库存款
        List<StockSize> stockSizeList = stockSizeMapper.selectList(new BaseLambdaQueryWrapper<StockSize>()
                .notEmptyIn(StockSize::getBulkStyleNo, bulkStyleNo)
        );

        sizeMapList.forEach(sizeMap -> {
            String id = sizeMap.remove("id").toString();
            sourceSaleFacList.stream().filter(it -> it.getId().equals(id)).findFirst().ifPresent(saleFac -> {
                sizeMap.forEach((sizeName, value) -> {
                    int stockQty = stockSizeList.stream().filter(it -> it.getSizeName().equals(sizeName)).mapToInt(StockSize::getQty).sum();
                    BigDecimal num = new BigDecimal(value.toString());
                    ProductionSaleDTO productionSaleDTO = list.stream()
                            .filter(it -> it.getBulkStyleNo().equals(saleFac.getBulkStyleNo()) && it.getSizeName().equals(sizeName))
                            .findFirst().orElseGet(() -> {
                                ProductionSaleDTO saleDTO = new ProductionSaleDTO();
                                list.add(saleDTO);
                                return saleDTO;
                            });
                    productionSaleDTO.setBulkStyleNo(saleFac.getBulkStyleNo());
                    productionSaleDTO.setSizeName(sizeName);
                    // 投产
                    if (StrUtil.isNotBlank(saleFac.getProductionType())) {
                        productionSaleDTO.setProduction(productionSaleDTO.getProduction().add(num));
                        if (BigDecimalUtil.biggerThenZero(num)) {
                            productionSaleDTO.setProductionCount(productionSaleDTO.getProductionCount() + 1);
                        }
                    } else {
                        productionSaleDTO.setSale(productionSaleDTO.getSale().add(num));
                    }
                    productionSaleDTO.setStorageCount(stockQty);
                });
            });
        });
        list.sort(CommonUtils.sizeNameSort(ProductionSaleDTO::getSizeName));
        return list;
    }

    // 投产信息
    private List<ProductionInfoDTO> findProductionInfoList(FactoryMissionRateQO factoryMissionRateQO) {
        List<ProductionInfoDTO> list = new ArrayList<>();

        String bulkStyleNo = factoryMissionRateQO.getBulkStyleNo();
        if (StrUtil.isBlank(bulkStyleNo)) return list;

        List<FactoryMissionRate> factoryMissionRateList = factoryMissionRateMapper.selectList(
                new BaseLambdaQueryWrapper<FactoryMissionRate>()
                        .notEmptyIn(FactoryMissionRate::getBulkStyleNo, bulkStyleNo)
                        .orderByAsc(FactoryMissionRate::getOrderDate)
        );
        factoryMissionRateList.stream().collect(CommonUtils.groupingBy(it -> Pair.of(it.getOrderNo(), it.getOrderDate())))
                .forEach((key, sameKeyList) -> {
                    ProductionInfoDTO productionSaleDTO = new ProductionInfoDTO();
                    productionSaleDTO.setDate(key.getValue());
                    productionSaleDTO.setOrderNo(key.getKey());
                    productionSaleDTO.setProduction(CommonUtils.sumBigDecimal(sameKeyList, FactoryMissionRate::getOrderNum));
                    productionSaleDTO.setSupplierInfoList(sameKeyList.stream().map(it -> new SupplierInfo(it.getSupplierId(), it.getSupplierName())).distinct().collect(Collectors.toList()));
                    productionSaleDTO.setStorageCount(CommonUtils.sumBigDecimal(sameKeyList, FactoryMissionRate::getDeliveryNum));
                    list.add(productionSaleDTO);
                });
        return list;
    }

    @Override
    public ReplayRatingPatternDTO getPatternById(String styleColorId) {
        /* ----------------------------获取设计款/版型库/复盘评分/复盘管理---------------------------- */
        // 复盘评分维度是大货款, 但其他数据维度是设计款, 所以这里只要styleId即可
        styleColorService.warnMsg("未找到对应大货款");
        String styleId = styleColorService.findByIds2OneField(styleColorId, StyleColor::getStyleId);

        patternLibraryService.warnMsg("未找到对应的版型数据");
        PatternLibrary patternLibrary = patternLibraryService.findOne(new LambdaQueryWrapper<PatternLibrary>().eq(PatternLibrary::getStyleId, styleId));
        patternLibrary.setPicUrl(uploadFileService.getReviewUrlById(patternLibrary.getPicId()));

        ReplayRatingPatternDTO result = new ReplayRatingPatternDTO();
        ReplayRating replayRating = this.findOne(new LambdaQueryWrapper<ReplayRating>()
                .eq(ReplayRating::getType, result.getType())
                .eq(ReplayRating::getForeignId, styleColorId)
        );

        ReplayConfigQO configQO = new ReplayConfigQO();
        configQO.reset2QueryFirst();
        configQO.setBrandName(patternLibrary.getBrandNames());
        ReplayConfigDTO replayConfig = replayConfigService.queryPageInfo(configQO).getList().stream().findFirst().orElse(new ReplayConfigDTO());

        /* ----------------------------组装数据---------------------------- */
        result.setStyleColorId(styleColorId);
        REPLAY_CV.copy(result, patternLibrary);

        result.setSaleSeason(replayConfig.getSaleSeason());
        if (replayRating != null) {
            // 获取评分详情
            REPLAY_CV.copy(result, replayRating);
            decorateReplayRatingDetail(result);
        }

        /* ----------------------------获取版型成功率---------------------------- */

        decoratePatternSuccessRate(Collections.singletonList(result), ReplayRatingPatternDTO::getStyleId, ReplayRatingPatternDTO::setUseCount, ReplayRatingPatternDTO::setProductionCount);

        /* ----------------------------设置版型库数据及子集合---------------------------- */

        String templateCode = result.getTemplateCode();
        if (StrUtil.isNotBlank(templateCode)) {
            result.setPatternLibraryTemplate(patternLibraryTemplateService.findOne(new LambdaQueryWrapper<PatternLibraryTemplate>()
                    .select(PatternLibraryTemplate::getPatternType)
                    .eq(PatternLibraryTemplate::getCode, templateCode)
            ));
        }

        List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, result.getPatternLibraryId())
        );
        result.setPatternLibraryItemList(patternLibraryItemList);

        return result;
    }

    @Override
    public ReplayRatingFabricDTO getFabricById(String materialId) {
        /* ----------------------------获取物料库/物料颜色/物料规格/复盘评分---------------------------- */
        basicsdatumMaterialService.warnMsg("未找到对应物料数据");
        BasicsdatumMaterial material = basicsdatumMaterialService.findOne(materialId);

        // 检查主数据是否是面料数据
        if (!material.getCategory1Code().equals(ReplayRatingProperties.category1Code)) {
            throw new OtherException("不支持非面料的复盘分析");
        }

        Map<String, String> materialWidthMap = basicsdatumMaterialWidthService.mapOneField(
                new LambdaQueryWrapper<BasicsdatumMaterialWidth>().eq(BasicsdatumMaterialWidth::getMaterialCode, material.getMaterialCode())
                , BasicsdatumMaterialWidth::getName, BasicsdatumMaterialWidth::getCode
        );

        material.setImageUrl(minioUtils.getObjectUrl(material.getImageUrl()));

        Map<String, String> colorMap = basicsdatumMaterialColorService.mapOneField(new LambdaQueryWrapper<BasicsdatumMaterialColor>()
                        .eq(BasicsdatumMaterialColor::getMaterialCode, material.getMaterialCode())
                , BasicsdatumMaterialColor::getColorCode, BasicsdatumMaterialColor::getColorName);

        ReplayRatingFabricDTO result = new ReplayRatingFabricDTO();
        ReplayRating replayRating = this.findOne(new LambdaQueryWrapper<ReplayRating>()
                .eq(ReplayRating::getType, result.getType())
                .eq(ReplayRating::getForeignId, materialId)
        );

        /* ----------------------------组装数据---------------------------- */
        REPLAY_CV.copy(result, material);
        result.setColor(String.join("/", colorMap.values()));
        result.setColorCode(String.join(COMMA, colorMap.keySet()));
        // 使用nacos配置的年份
        result.setYearList(Arrays.stream(ReplayRatingProperties.years).boxed().collect(Collectors.toList()));
        result.setTranslate(String.join("/", materialWidthMap.keySet()));
        result.setTranslateCode(String.join(COMMA, materialWidthMap.values()));

        if (replayRating != null) {
            // 获取评分详情
            REPLAY_CV.copy(result, replayRating);
            decorateReplayRatingDetail(result);
        }

        /* ----------------------------获取大货款号以及物料清单---------------------------- */

        // key: devtType value: bulkStyleNo, bulkUnitUse
        Map<ProductionType, Map<String, BigDecimal>> bulkUnitUseMap = findBulkUnitUseMap(materialId);
        if (MapUtil.isNotEmpty(bulkUnitUseMap)) {
            // 设置基础map
            Map<ProductionType, List<FabricMonthDataDto>> monthDataMap = Arrays.stream(ProductionType.values()).collect(Collectors.toMap(Function.identity(), (it) -> new ArrayList<>()));
            bulkUnitUseMap.forEach((productionType, bulkUseMap) -> {
                // 根据大货款号 获取生产数据
                List<SaleFac> saleFacList = saleFacMapper.selectList(new LambdaQueryWrapper<SaleFac>()
                        .in(SaleFac::getBulkStyleNo, bulkUseMap.keySet())
                        .in(SaleFac::getResultType, Arrays.asList(SaleFacResultType.FIRST_PRODUCTION, SaleFacResultType.APPEND_PRODUCTION))
                        .in(SaleFac::getYear, result.getYearList())
                        .select(SaleFac::getBulkStyleNo, SaleFac::getNum, SaleFac::getMonth)
                );
                // 获取单位转换枚举
                UnitConverterEnum converterEnum = productionType == ProductionType.CMT ? result.getCmtUnit() : result.getFobUnit();
                List<FabricMonthDataDto> dataList = monthDataMap.get(productionType);
                // 根据月份遍历
                for (Month month : Month.values()) {
                    int value = month.getValue();
                    // 根据年份遍历
                    for (int year : result.getYearList()) {
                        // 生成查询时间
                        YearMonth startDate = YearMonth.of(year, value);
                        // 结束时间大一个月
                        YearMonth endDate = startDate.plusMonths(ReplayRatingProperties.monthRange);
                        // 获取时间段内的数据
                        List<SaleFac> timeSaleFacList = saleFacList.stream().filter(it -> it.isBetween(startDate, endDate)).collect(Collectors.toList());
                        dataList.add(new FabricMonthDataDto(startDate, endDate, CommonUtils.sumBigDecimal(
                                timeSaleFacList, (saleFac) -> {
                                    // 获取每单位用量, 根据单位转换枚举 计算最终值
                                    BigDecimal bulkUnitUse = bulkUseMap.getOrDefault(saleFac.getBulkStyleNo(), BigDecimal.ZERO);
                                    return converterEnum.calculate(BigDecimalUtil.mul(saleFac.getNum(), bulkUnitUse));
                                })));
                    }
                }
            });
            // 设置月份数据
            result.setMonthData(monthDataMap);
        }
        return result;
    }

    private void decorateReplayRatingDetail(ReplayRatingSaveDTO replayRatingSaveDTO) {
        if (StrUtil.isNotBlank(replayRatingSaveDTO.getId())) {
            List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                    .eq(ReplayRatingDetail::getReplayRatingId, replayRatingSaveDTO.getId())
            );
            // copy2DTO 会自动调用 build 组装extend 并 获取图片/视频/附件的预览路径
            replayRatingDetailList.stream().map(REPLAY_CV::copy2DTO).peek(it -> {
                it.setFileUrl(minioUtils.getObjectUrl(it.getFileUrl()));
            }).collect(Collectors.groupingBy(ReplayRatingDetail::getType)).forEach((type, list) -> {
                // 放入数据并平铺
                replayRatingSaveDTO.getDetailListMap().put(type, new ReplayRatingDetailList(list));
            });
        }
    }

    // 获取款式配色-用量映射关系
    private Map<ProductionType, Map<String, BigDecimal>> findBulkUnitUseMap(String materialId) {
        // 如果PackBom 冗余了 styleColorId 就能省略这个步骤 TODO
        List<PackBom> packBomList = packBomService.list(new LambdaQueryWrapper<PackBom>()
                .select(PackBom::getBulkUnitUse, PackBom::getForeignId)
                .eq(PackBom::getMaterialId, materialId)
                .eq(PackBom::getStatus, YesOrNoEnum.YES)
                .eq(PackBom::getPackType, PackUtils.PACK_TYPE_BIG_GOODS)
        );
        if (CollUtil.isEmpty(packBomList)) return new HashMap<>();

        // key: packInfoId value: bulkUnitUse
        Map<String, BigDecimal> bulkUseMap = packBomList.stream().collect(CommonUtils.groupingSingleBy(PackBom::getForeignId,
                (list) -> CommonUtils.sumBigDecimal(list, PackBom::getBulkUnitUse)));

        // key: styleColorId value: packInfoId
        Map<String, String> packInfoStyleColorMap = MapUtil.reverse(packInfoService.mapOneField(new LambdaQueryWrapper<PackInfo>()
                .in(PackInfo::getId, bulkUseMap.keySet()), PackInfo::getId, PackInfo::getStyleColorId));

        // key: styleColorId value: bulkUnitUse
        Map<String, BigDecimal> bulkUnitUseMap = MapUtil.map(packInfoStyleColorMap, (styleColorId, packInfoId) -> bulkUseMap.getOrDefault(packInfoId, BigDecimal.ZERO));

        // key: cmt/fob value: styleColorIdList
        Map<ProductionType, List<String>> devtTypeStyleColorIdMap = CommonUtils.inverse(styleColorService.mapOneField(new LambdaQueryWrapper<StyleColor>()
                .in(StyleColor::getId, packInfoStyleColorMap.keySet()), StyleColor::getId, StyleColor::getDevtType));

        return MapUtil.map(devtTypeStyleColorIdMap, (productionType, styleColorIdList) ->
                MapUtil.filter(bulkUnitUseMap, styleColorIdList.toArray(new String[]{}))
        );
    }

    @Override
    public PageInfo<ReplayRatingYearVO> yearListByStyleNo(ReplayRatingYearQO replayRatingYearQO) {
        // 获取使用了该版型或面料的设计款
        ReplayRatingQO replayRatingQO = BeanUtil.copyProperties(replayRatingYearQO, ReplayRatingQO.class);
        replayRatingQO.setType(ReplayRatingType.STYLE);
        replayRatingQO.setRegisteringId(replayRatingYearQO.getPatternLibraryId());
        replayRatingQO.setMaterialCode(replayRatingYearQO.getMaterialCode());
        ReplayRatingPageVO<? extends ReplayRatingVO> pageInfo = queryPageInfo(replayRatingQO);

        List<? extends ReplayRatingVO> list = pageInfo.getList();
        if (CollUtil.isEmpty(list)) return CopyUtil.copy(pageInfo, new ArrayList<>());
        
        List<ReplayRatingYearVO> yearVOList = new ArrayList<>();

        List<ReplayConfig> configList = new ArrayList<>();
        // key: styleColorId value:packBomList
        Map<String, List<PackBom>> stylePackBomListMap = new HashMap<>(list.size());
        List<SaleFac> saleFacList = new ArrayList<>();
        // 构建支持数据
        findSupportData(replayRatingYearQO, list, configList, stylePackBomListMap, saleFacList);

        list.forEach(replayRatingVO -> {
            // 构建结果体
            ReplayRatingYearVO replayRatingYearVO = new ReplayRatingYearVO();
            replayRatingYearVO.setSeasonFlag(replayRatingYearQO.getSeasonFlag());
            REPLAY_CV.copy(replayRatingYearVO, replayRatingVO);

            // 通过复盘管理获取时间区间
            ReplayConfigDetailDTO saleSeason = configList.stream()
                    .filter(it -> it.getBrand().equals(replayRatingVO.getBrand()))
                    .findFirst().map(it -> {
                        it.build();
                        return it.getSaleSeason();
                    }).orElse(new ReplayConfigDetailDTO());

            List<SaleFac> bulkSaleFacList = saleFacList.stream().filter(it -> it.getBulkStyleNo().equals(replayRatingVO.getBulkStyleNo())).collect(Collectors.toList());

            ReplayRatingYearProductionSaleDTO yearProductionSaleDTO = new ReplayRatingYearProductionSaleDTO();
            // 设置合计字段的前缀
            yearProductionSaleDTO.setKey(ReplayRatingProperties.totalPrefix);
            replayRatingYearVO.setReplayRatingYearProductionSaleDTO(yearProductionSaleDTO);
            List<ReplayRatingYearProductionSaleDTO> childrenList = yearProductionSaleDTO.getChildrenList();

            // 解构复盘管理
            Map<String, Object> saleSeasonMap = BeanUtil.beanToMap(saleSeason);
            // !!注意!! 这个map顺序是ReplayConfigDetailDTO的字段顺序 不要改
            AtomicInteger yearIndex = new AtomicInteger();
            saleSeasonMap.forEach((key, value) -> {
                // 可选季节设置 没设置 就用默认季节
                List<ReplayConfigTimeDTO> timeDTOList = ObjectUtil.isEmpty(value)
                        ? Arrays.stream(SeasonEnum.values()).map(ReplayConfigTimeDTO::new).collect(Collectors.toList())
                        : (List<ReplayConfigTimeDTO>) value;
                // 可选年份设置
                replayRatingYearVO.getYearList().add(key);

                // 构建第一层子体
                ReplayRatingYearProductionSaleDTO children = new ReplayRatingYearProductionSaleDTO();
                children.setKey(key);
                List<ReplayRatingYearProductionSaleDTO> subChildrenList = children.getChildrenList();

                YearMonth date = YearMonth.now().minusYears(yearIndex.getAndIncrement()).withMonth(Month.JANUARY.getValue());
                timeDTOList.forEach(timeDTO -> {
                    // 构建第二层子体,季节
                    ReplayRatingYearProductionSaleDTO subChildren = new ReplayRatingYearProductionSaleDTO();

                    // 使用名称来作为前缀
                    SeasonEnum season = timeDTO.getSeason();
                    int index = season.ordinal();
                    String code = season.name().toLowerCase();
                    subChildren.setKey(code);

                    // 计算月份范围
                    YearMonth startMonth = Opt.ofNullable(timeDTO.getStartMonth()).orElse(date.plusMonths(index * 3L));
                    YearMonth endMonth = Opt.ofNullable(timeDTO.getEndMonth()).orElse(date.plusMonths((index + 1) * 3L));
                    // 获取投产销售数据
                    bulkSaleFacList.stream().filter(it -> it.isBetween(startMonth, endMonth))
                            .collect(Collectors.groupingBy(SaleFac::isProduction))
                            .forEach((isProduction, sameTypeList) -> {
                                if (isProduction) {
                                    subChildren.setProduction(CommonUtils.sumBigDecimal(sameTypeList, SaleFac::getNum));
                                } else {
                                    subChildren.setSale(CommonUtils.sumBigDecimal(sameTypeList, SaleFac::getNum));
                                }
                                bulkSaleFacList.removeAll(sameTypeList);
                            });
                    subChildrenList.add(subChildren);
                });
                childrenList.add(children);
            });
            yearVOList.add(replayRatingYearVO);
        });
        decorateYearList(yearVOList, saleFacList, stylePackBomListMap);
        return CopyUtil.copy(pageInfo, yearVOList);
    }

    private void decorateYearList(List<ReplayRatingYearVO> list, List<SaleFac> saleFacList, Map<String, List<PackBom>> stylePackBomListMap) {
        // 设置廓形
        QueryFieldManagementDto queryDto = new QueryFieldManagementDto();
        queryDto.setGroupName(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        queryDto.setFieldExplain(FieldValProperties.silhouette);
        decorateFieldVal(list, queryDto, ReplayRatingYearVO::getStyleId, ReplayRatingYearVO::setSilhouetteCode, ReplayRatingYearVO::setSilhouetteName);

        list.forEach(replayRatingYearVO -> {
            ReplayRatingYearProductionSaleDTO yearProductionSaleDTO = replayRatingYearVO.getReplayRatingYearProductionSaleDTO();
            // 设置除前年去年今年之外的时间的构建体
            ReplayRatingYearProductionSaleDTO longTimeAgoChildren = new ReplayRatingYearProductionSaleDTO();
            // 设置前缀 任意,前端不显示
            longTimeAgoChildren.setKey(ReplayRatingProperties.longTimeAgoPrefix);
            saleFacList.stream()
                    .filter(it -> it.getBulkStyleNo().equals(replayRatingYearVO.getBulkStyleNo()))
                    .collect(Collectors.groupingBy(SaleFac::isProduction)).forEach((isProduction, sameTypeList) -> {
                        if (isProduction) {
                            longTimeAgoChildren.setProduction(CommonUtils.sumBigDecimal(sameTypeList, SaleFac::getNum));
                        } else {
                            longTimeAgoChildren.setSale(CommonUtils.sumBigDecimal(sameTypeList, SaleFac::getNum));
                        }
                    });
            yearProductionSaleDTO.getChildrenList().add(longTimeAgoChildren);
            yearProductionSaleDTO.calculate();

            // 获取物料清单 设置搭配
            List<PackBom> stylePackBomList = stylePackBomListMap.getOrDefault(replayRatingYearVO.getStyleColorId(), new ArrayList<>());
            replayRatingYearVO.setCollocationCode(stylePackBomList.stream().map(PackBom::getCollocationCode).distinct().collect(Collectors.joining(COMMA)));
            replayRatingYearVO.setCollocationName(stylePackBomList.stream().map(PackBom::getCollocationName).distinct().collect(Collectors.joining("\n")));
        });
    }

    private void findSupportData(ReplayRatingYearQO replayRatingYearQO, List<? extends ReplayRatingVO> list, List<ReplayConfig> configList, Map<String, List<PackBom>> stylePackBomListMap, List<SaleFac> saleFacList) {
        List<String> bulkStyleNoList = list.stream().map(ReplayRatingVO::getBulkStyleNo).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<SaleFac> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SaleFac::getBulkStyleNo, bulkStyleNoList);
        switch (replayRatingYearQO.getType()) {
            case PATTERN:
                // 版型复盘需要复盘管理的配置
                List<String> brandList = list.stream().map(ReplayRatingVO::getBrand).distinct().collect(Collectors.toList());
                configList.addAll(replayConfigService.list(new LambdaQueryWrapper<ReplayConfig>().in(ReplayConfig::getBrand, brandList)));
                break;
            case FABRIC:
                queryWrapper.in(SaleFac::getResultType, SaleFacResultType.productionList());
                // 版型复盘需要 大货款的对应的特定物料清单列表
                List<String> styleColorIdList = list.stream().map(ReplayRatingVO::getStyleColorId).distinct().collect(Collectors.toList());
                Map<String, String> styleColorIdMap = packInfoService.mapOneField(
                        new LambdaQueryWrapper<PackInfo>().in(PackInfo::getStyleColorId, styleColorIdList)
                        , PackInfo::getStyleColorId, PackInfo::getId
                );
                // 存在大货款
                if (MapUtil.isNotEmpty(styleColorIdMap)) {
                    // 获取大货款的物料清单
                    List<PackBom> packBomList = packBomService.list(new LambdaQueryWrapper<PackBom>()
                            .select(PackBom::getForeignId, PackBom::getCollocationCode, PackBom::getCollocationName, PackBom::getBulkUnitUse)
                            .eq(PackBom::getPackType, PackUtils.PACK_TYPE_BIG_GOODS)
                            .in(PackBom::getForeignId, styleColorIdMap.values())
                            .eq(PackBom::getStatus, YesOrNoEnum.YES)
                    );
                    // key:styleColorId, value:PackBomList
                    stylePackBomListMap.putAll(
                            MapUtil.map(styleColorIdMap, (styleColorId, packInfoId) ->
                                    packBomList.stream().filter(it -> it.getForeignId().equals(packInfoId)).collect(Collectors.toList())
                            )
                    );
                }
                break;
            default:
                throw new UnsupportedOperationException("暂不支持" + replayRatingYearQO.getType().getText());
        }
        saleFacList.addAll(saleFacMapper.selectList(queryWrapper));
    }

    @Override
    public String transferPatternLibrary(ReplayRatingTransferDTO transferDTO) {
        // 获取大货款
        String styleColorId = transferDTO.getStyleColorId();
        String styleId = transferDTO.getStyleId();
        String bulkStyleNo = styleColorService.findOneField(new LambdaQueryWrapper<StyleColor>().eq(StyleColor::getStyleId, styleId).eq(StyleColor::getId, styleColorId), StyleColor::getStyleNo);
        if (StrUtil.isBlank(bulkStyleNo)) throw new OtherException("错误的款式配色");

        styleService.warnMsg("未找到对应设计款");
        Style style = styleService.findOne(styleId);

        // 获取版型库
        PatternLibrary patternLibrary = patternLibraryService.findOne(new LambdaQueryWrapper<PatternLibrary>().eq(PatternLibrary::getStyleId, styleId));

        if (patternLibrary == null) {
            // 版型库为空 则必须传入转入父编码
            if (transferDTO.getTransferParentFlag() == null) throw new OtherException("未找到对应的版型库");
            // 构建基础数据
            PatternLibraryDTO libraryDTO = new PatternLibraryDTO();
            libraryDTO.setPlanningSeasonId(style.getPlanningSeasonId());
            libraryDTO.setCode(style.getDesignNo());
            libraryDTO.setDesignNo(style.getDesignNo());
            libraryDTO.setStyleId(style.getId());
            libraryDTO.setStyleNos(bulkStyleNo);
            libraryDTO.setProdCategory1st(style.getProdCategory1st());
            libraryDTO.setProdCategory1stName(style.getProdCategory1stName());
            libraryDTO.setProdCategory(style.getProdCategory());
            libraryDTO.setProdCategoryName(style.getProdCategoryName());
            libraryDTO.setProdCategory2nd(style.getProdCategory2nd());
            libraryDTO.setProdCategory2ndName(style.getProdCategory2ndName());
            libraryDTO.setProdCategory3rd(style.getProdCategory3rd());
            libraryDTO.setProdCategory3rdName(style.getProdCategory3rdName());
            libraryDTO.setTemplateCode(transferDTO.getTemplateCode());
            libraryDTO.setTemplateName(transferDTO.getTemplateName());
            libraryDTO.setPicId(style.getStylePic());
            libraryDTO.setOldPicId(style.getStylePic());
            libraryDTO.setPicUrl(style.getStylePic());
            libraryDTO.setPicSource(2);
            libraryDTO.setStatus(2);
            libraryDTO.setEnableFlag(YesOrNoEnum.YES.getValue());
            PatternLibraryBrand patternLibraryBrand = new PatternLibraryBrand();
            patternLibraryBrand.setBrand(style.getBrand());
            patternLibraryBrand.setBrandName(style.getBrandName());
            libraryDTO.setPatternLibraryBrandList(Collections.singletonList(patternLibraryBrand));

            // 获取面料详情
            StyleBomSearchDto styleBomSearchDto = new StyleBomSearchDto();
            styleBomSearchDto.setIsStock(YesOrNoEnum.NO.getValueStr());
            styleBomSearchDto.setStyleId(styleId);
            styleBomSearchDto.reset2QueryList();
            List<PackBomVo> bomList = styleService.bomList(styleBomSearchDto).getList();
            if (CollUtil.isNotEmpty(bomList)) {
                libraryDTO.setMaterialCode(bomList.stream().map(PackBomVo::getMaterialCode).collect(Collectors.joining(COMMA)));
                libraryDTO.setMaterialName(bomList.stream().map(PackBomVo::getMaterialName).collect(Collectors.joining("；")));
            }

            //  通过名字获取对应的结构树 (改名就没办法)
            String prodCategory1st = style.getProdCategory1st();
            BasicCategoryDot basicCategoryDot = new BasicCategoryDot();
            basicCategoryDot.setValue(BusinessProperties.getStructureCode(prodCategory1st, "围度数据"));
            BasicCategoryDot patternTree = ccmFeignService.getTreeByName(basicCategoryDot, null);
            basicCategoryDot.setValue(BusinessProperties.getStructureCode(prodCategory1st, "长度数据"));
            BasicCategoryDot lengthTree = ccmFeignService.getTreeByName(basicCategoryDot, null);

            // 获取围度数据和长度数据
            List<PatternLibraryItem> patternLibraryItemList = new ArrayList<>();
            List<FieldVal> fieldValList = fieldValService.list(new LambdaQueryWrapper<FieldVal>().eq(FieldVal::getForeignId, styleId));
            fieldValList.forEach(fieldVal -> {
                PatternLibraryItem patternLibraryItem = new PatternLibraryItem();
                patternLibraryItem.setName(fieldVal.getFieldExplain());
                patternLibraryItem.setStructureKey(fieldVal.getVal());
                patternLibraryItem.setStructureValue(fieldVal.getValName());
                patternLibraryItem.setDescription(fieldVal.getFieldExplain());

                BasicCategoryDot patternCategoryDot = patternTree.findByValue(fieldVal.getVal());
                if (patternCategoryDot != null) {
                    patternLibraryItem.setCode(patternCategoryDot.getLevel().toString());
                    patternLibraryItem.setType(1);
                    patternLibraryItemList.add(patternLibraryItem);
                }
                BasicCategoryDot lengthCategoryDot = lengthTree.findByValue(fieldVal.getVal());
                if (lengthCategoryDot != null) {
                    patternLibraryItem.setCode(lengthCategoryDot.getLevel().toString());
                    patternLibraryItem.setType(2);
                    patternLibraryItemList.add(patternLibraryItem);
                }
                if (fieldVal.getFieldExplain().equals(FieldValProperties.silhouette)) {
                    libraryDTO.setSilhouetteCode(fieldVal.getVal());
                    libraryDTO.setSilhouetteName(fieldVal.getValName());
                }
            });

            // 获取模板部件
            if (StrUtil.isNotBlank(style.getPatternParts())) {
                ProcessDatabasePageDto processDatabasePageDto = new ProcessDatabasePageDto();
                processDatabasePageDto.setStatus("0");
                processDatabasePageDto.setType(ProcessDatabaseType.mbbj);
                processDatabasePageDto.setCategoryId(style.getProdCategory());
                processDatabasePageDto.setProcessNameList(style.getPatternParts());
                processDatabasePageDto.reset2QueryList();
                List<ProcessDatabase> databaseList = processDatabaseService.listPage(processDatabasePageDto).getList();
                databaseList.forEach(database -> {
                    PatternLibraryItem patternLibraryItem = new PatternLibraryItem();
                    patternLibraryItem.setName(database.getComponentName());
                    patternLibraryItem.setPicUrl(database.getPicture());
                    patternLibraryItem.setProcessName(database.getProcessName());
                    patternLibraryItem.setProdCategory(database.getCategoryId());
                    patternLibraryItem.setProdCategoryName(database.getCategoryName());
                    patternLibraryItem.setDescription(database.getDescription());
                    patternLibraryItem.setCode(database.getProcessName());
                    patternLibraryItem.setType(4);
                    patternLibraryItemList.add(patternLibraryItem);
                });
            }
            libraryDTO.setPatternLibraryItemList(patternLibraryItemList);

            // 设置父编码
            if (transferDTO.getTransferParentFlag() == YesOrNoEnum.YES) {
                libraryDTO.setParentId(style.getRegisteringId());
            }
            patternLibraryService.saveOrUpdateDetails(libraryDTO);
            return libraryDTO.getId();
        } else {
            // 设置热销大货款
            patternLibrary.setPlaceOrderStyleNos(StrJoiner.of(COMMA).setNullMode(StrJoiner.NullMode.IGNORE).append(patternLibrary.getPlaceOrderStyleNos()).append(bulkStyleNo).toString());
            // 设置父编码
            if (transferDTO.getTransferParentFlag() == YesOrNoEnum.YES) {
                patternLibrary.setParentId(style.getRegisteringId());
            }
            patternLibraryService.saveOrUpdate(patternLibrary);
            return patternLibrary.getId();
        }
    }

    @Override
    public List<ReplayRatingBulkWarnVO> bulkWarnMsg(String bulkStyleNo) {
        List<ReplayRatingBulkWarnVO> result = new ArrayList<>();
        for (ReplayRatingWarnType type : ReplayRatingWarnType.values()) {
            ReplayRatingBulkWarnVO warnVO = new ReplayRatingBulkWarnVO();
            warnVO.setWarnType(type);
            warnVO.setDate(LocalDate.now());
            warnVO.setOrderNo("AT53453");
        }
        return result;
    }

    @Override
    @SneakyThrows
    public void exportExcel(ReplayRatingQO qo) {
        /* 查询吊牌数据 */
        qo.setFindTotalFlag(YesOrNoEnum.NO);
        qo.notRequiredDownloadImage();
        List<? extends ReplayRatingVO> list = queryPageInfo(qo).getList();
        ExcelUtils.exportExcelByTableCode(list, qo.getType().getText(), qo);
    }

// 自定义方法区 不替换的区域【other_end】

}
