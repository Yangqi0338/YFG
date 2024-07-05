/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.constant.BusinessProperties;
import com.base.sbc.config.constant.ReplayRatingProperties;
import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingLevelEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.enums.business.replay.ReplayRatingWarnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
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
import com.base.sbc.module.replay.dto.ReplayConfigDTO;
import com.base.sbc.module.replay.dto.ReplayConfigDetailDTO;
import com.base.sbc.module.replay.dto.ReplayConfigTimeDTO;
import com.base.sbc.module.replay.dto.ReplayRatingDetailList;
import com.base.sbc.module.replay.dto.ReplayRatingFabricDTO;
import com.base.sbc.module.replay.dto.ReplayRatingPatternDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO.ProductionInfoDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO.ProductionSaleDTO;
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
import com.base.sbc.module.replay.vo.ReplayRatingFabricVO;
import com.base.sbc.module.replay.vo.ReplayRatingPageVO;
import com.base.sbc.module.replay.vo.ReplayRatingPatternVO;
import com.base.sbc.module.replay.vo.ReplayRatingQO;
import com.base.sbc.module.replay.vo.ReplayRatingStyleVO;
import com.base.sbc.module.replay.vo.ReplayRatingVO;
import com.base.sbc.module.replay.vo.ReplayRatingYearQO;
import com.base.sbc.module.replay.vo.ReplayRatingYearVO;
import com.base.sbc.module.style.dto.StyleBomSearchDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.Constants.COMMA;
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

    private static @NotNull BaseQueryWrapper<ReplayRating> buildQueryWrapper(ReplayRatingQO dto) {
        BaseQueryWrapper<ReplayRating> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("tsc.planning_season_id", dto.getPlanningSeasonId());
        qw.notEmptyEq("tsc.band_name", dto.getBandName());
        qw.notEmptyEq("ts.prod_category1st", dto.getProdCategory1st());
        qw.notEmptyEq("ts.prod_category", dto.getProdCategory());
        qw.notEmptyEq("ts.prod_category2nd", dto.getProdCategory2nd());
        qw.notEmptyEq("ts.prod_category3rd", dto.getProdCategory3rd());
//        qw.notEmptyEq("tsc.plan_season_id", dto.getSaleLevel());
        qw.notEmptyEq("ts.design_no", dto.getDesignNo());
        qw.notEmptyEq("tsc.style_no", dto.getBulkStyleNo());
        qw.notEmptyEq("trr.rating_flag", dto.getRatingFlag());
        qw.notEmptyEq("ts.registering_id", dto.getRegisteringId());
        qw.notEmptyEq("ts.registering_no", dto.getRegisteringNo());
        qw.notEmptyEq("ifnull(tpl.style_nos like CONCAT('%', tsc.style_no, '%'), 0)", dto.getTransferPatternFlag());

        if (dto.getType() != ReplayRatingType.STYLE) {
            qw.notEmptyEq("tpb.material_code", dto.getMaterialCode());
        }
        qw.notEmptyEq("tpb.supplier_id", dto.getSupplierId());
        qw.notEmptyEq("tpl.color", dto.getColorCode());
        qw.notEmptyEq("tpl.plan_season_id", dto.getMaterialOwnResearchFlag());
        return qw;
    }

    @Override
    public ReplayRatingPageVO<? extends ReplayRatingVO> queryPageInfo(ReplayRatingQO qo) {
        BaseQueryWrapper<ReplayRating> queryWrapper = buildQueryWrapper(qo);
        QueryGenerator.initQueryWrapperByMap(queryWrapper, qo);
        Page<? extends ReplayRatingVO> page = PageHelper.startPage(qo);
        Map<String, Object> totalMap;
        switch (qo.getType()) {
            case STYLE:
                queryWrapper.orderByAsc("tsc.design_no");
                decorateStyleList(baseMapper.queryStyleList(queryWrapper, qo), qo);
                totalMap = new HashMap<>();
                break;
            case PATTERN:
                qo.setGroupName(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
                qo.setFieldExplain("廓形及代码");
                decoratePatternList(baseMapper.queryPatternList(queryWrapper, qo), qo);
                ReplayRatingPatternVO totalPatternVO = new ReplayRatingPatternVO();
                totalPatternVO.setSeasonSaleCount(BigDecimal.ONE);
                totalPatternVO.setSeasonProductionCount(BigDecimal.TEN);
                totalPatternVO.setUseCount(BigDecimal.valueOf(100));
                totalPatternVO.setProductionCount(BigDecimal.valueOf(200));
                totalMap = BeanUtil.beanToMap(totalPatternVO, false, true);
                totalMap.putAll(MapUtil.ofEntries(
                        MapUtil.entry("seasonProductionSaleRate", totalPatternVO.getSeasonProductionSaleRate()),
                        MapUtil.entry("patternSuccessRate", totalPatternVO.getPatternSuccessRate())
                ));
                break;
            case FABRIC:
                if (!qo.isColumnGroupSearch()) {
                    queryWrapper.groupBy("tpb.material_code", "tpb.foreign_id");
                    queryWrapper.orderByAsc("tpb.material_code");
                }
                ;
                decorateFabricList(baseMapper.queryFabricList(queryWrapper, qo), qo);
                ReplayRatingFabricVO totalFabricVO = new ReplayRatingFabricVO();
                totalFabricVO.setRemainingMaterial(BigDecimal.TEN);
                totalFabricVO.setProduction(BigDecimal.valueOf(100));
                totalFabricVO.setBulkUnitUse(BigDecimal.TEN);

                totalMap = BeanUtil.beanToMap(totalFabricVO, false, true);
                totalMap.putAll(MapUtil.ofEntries(
                        MapUtil.entry("realProduction", totalFabricVO.getRealProduction())
                ));
                break;
            default:
                throw new UnsupportedOperationException("不受支持的复盘类型");
        }

        PageInfo<? extends ReplayRatingVO> pageInfo = page.toPageInfo();

        ReplayRatingPageVO<? extends ReplayRatingVO> pageVo = BeanUtil.copyProperties(pageInfo, ReplayRatingPageVO.class);
        pageVo.setTotalMap(totalMap);
        return pageVo;
    }

    private <T extends ReplayRatingVO> List<T> decorateList(List<T> list, ReplayRatingQO qo) {
        if (qo.isColumnGroupSearch() || CollUtil.isEmpty(list)) return new ArrayList<>();
        Map<String, String> planningSeasonNameMap = planningSeasonService.mapOneField(
                new LambdaQueryWrapper<PlanningSeason>().in(PlanningSeason::getId, list.stream().map(ReplayRatingVO::getPlanningSeasonId).distinct().collect(Collectors.toList())), PlanningSeason::getId, PlanningSeason::getName);
        stylePicUtils.setStyleColorPic2(list);
        list.forEach(it -> it.setPlanningSeasonName(planningSeasonNameMap.getOrDefault(it.getPlanningSeasonId(), "")));
        return list;
    }

    private void decorateStyleList(List<ReplayRatingStyleVO> styleVOList, ReplayRatingQO qo) {
        styleVOList = decorateList(styleVOList, qo);
        if (CollUtil.isEmpty(styleVOList)) return;

        Map<String, String> patternIdMap = patternLibraryService.mapOneField(new LambdaQueryWrapper<PatternLibrary>()
                        .eq(PatternLibrary::getStyleId, styleVOList.stream().map(ReplayRatingStyleVO::getStyleId).collect(Collectors.toList()))
                , PatternLibrary::getStyleId, PatternLibrary::getId);
        styleVOList.forEach(styleDTO -> {
            // 为空获取 想要实时就去掉该空判断
            if (styleDTO.getPlanningLevel() == null) {
//                        styleDTO.setPlanningLevel();
            }
            if (styleDTO.getSeasonLevel() == null) {
//                        styleDTO.setSaleLevel();
            }
            // 版型id
            styleDTO.setGotoPatternId(patternIdMap.getOrDefault(styleDTO.getStyleId(), ""));
        });
    }

    private <T> void decorateFieldVal(List<T> sourceList, QueryFieldManagementDto qo, Function<T, String> keyFunc, BiConsumer<T, String> codeSetFunc, BiConsumer<T, String> nameSetFunc) {
        List<FieldVal> fieldValList = fieldValService.list(new BaseLambdaQueryWrapper<FieldVal>()
                .in(FieldVal::getForeignId, sourceList.stream().map(keyFunc).collect(Collectors.toList()))
                .eq(FieldVal::getDataGroup, qo.getGroupName())
                .eq(FieldVal::getFieldExplain, qo.getFieldExplain())
        );
        sourceList.forEach(pattern -> {
            fieldValList.stream()
                    .filter(it -> it.getForeignId().equals(keyFunc.apply(pattern)))
                    .max(Comparator.comparing(FieldVal::getUpdateDate))
                    .ifPresent(fieldVal -> {
                        nameSetFunc.accept(pattern, fieldVal.getValName());
                        codeSetFunc.accept(pattern, fieldVal.getVal());
                    });
        });
    }

    private void decoratePatternList(List<ReplayRatingPatternVO> patternVOList, ReplayRatingQO qo) {
        patternVOList = decorateList(patternVOList, qo);
        if (CollUtil.isEmpty(patternVOList)) return;

        List<String> styleIdList = patternVOList.stream().map(ReplayRatingPatternVO::getStyleId).distinct().collect(Collectors.toList());
        List<String> bulkStyleNoList = patternVOList.stream().map(ReplayRatingPatternVO::getBulkStyleNo).collect(Collectors.toList());

        /* ----------------------------获取廓形---------------------------- */

        QueryFieldManagementDto queryDto = new QueryFieldManagementDto();
        queryDto.setGroupName(qo.getGroupName());
        queryDto.setFieldExplain(qo.getFieldExplain());
        decorateFieldVal(patternVOList, queryDto, ReplayRatingPatternVO::getStyleId, ReplayRatingPatternVO::setSilhouetteCode, ReplayRatingPatternVO::setSilhouetteName);

        /* ----------------------------是否转入版型库---------------------------- */

        // 最多不会超过list条数, 所以加入分页且不计算count
        qo.startPage(false);
        List<String> patternLibraryStyleNoList = patternLibraryService.listOneField(new BaseLambdaQueryWrapper<PatternLibrary>()
                        .notNull(PatternLibrary::getStyleNos)
                        .likeList(PatternLibrary::getStyleNos, StrUtil.join(COMMA, bulkStyleNoList))
                        .in(PatternLibrary::getStyleId, styleIdList)
                , PatternLibrary::getStyleNos);
        String patternStyleNoList = StrUtil.join(COMMA, patternLibraryStyleNoList);
        patternVOList.forEach(pattern -> {
            pattern.setTransferPatternFlag(YesOrNoEnum.findByValue(patternStyleNoList.contains(pattern.getBulkStyleNo())));
        });

        /* ----------------------------获取版型成功率---------------------------- */

        decoratePatternSuccessRate(patternVOList, ReplayRatingPatternVO::getPatternLibraryId, ReplayRatingPatternVO::setUseCount, ReplayRatingPatternVO::setProductionCount);

        /* ----------------------------当季投产销售---------------------------- */

        patternVOList.forEach(pattern -> {
            pattern.setSeasonProductionCount(RandomUtil.randomBigDecimal(BigDecimal.ONE, BigDecimal.TEN.add(BigDecimal.TEN)).setScale(0, RoundingMode.HALF_UP));
            pattern.setSeasonSaleCount(RandomUtil.randomBigDecimal(BigDecimal.ONE, BigDecimal.TEN).setScale(0, RoundingMode.HALF_UP));
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
        /* ----------------------------大货款维度数据---------------------------- */
        List<String> styleColorIdList = fabricVOList.stream().map(ReplayRatingFabricVO::getStyleColorId).collect(Collectors.toList());
        Map<String, String> styleColorPicMap = styleColorService.mapOneField(new LambdaQueryWrapper<StyleColor>()
                .in(StyleColor::getId, styleColorIdList), StyleColor::getId, StyleColor::getStyleColorPic);
        fabricVOList.forEach(fabric -> {
            fabric.setStyleColorPic(styleColorPicMap.getOrDefault(fabric.getStyleColorId(), ""));
        });

        fabricVOList = decorateList(fabricVOList, qo);
        if (CollUtil.isEmpty(fabricVOList)) return;

        /* ----------------------------面料自主研发 + 剩余备料---------------------------- */
        AtomicInteger remainingMaterial = new AtomicInteger(100);
        AtomicInteger fabricOwnDevelopFlagAtomic = new AtomicInteger();
        fabricVOList.forEach(fabric -> {
            int increment = fabricOwnDevelopFlagAtomic.getAndIncrement();
            fabric.setFabricOwnDevelopFlag(YesOrNoEnum.findByValue(increment < 2 ? increment : (increment / 2) % 2));
            fabric.setRemainingMaterial(BigDecimal.valueOf(remainingMaterial.getAndIncrement()));
        });

        /* ----------------------------大货款维度投产量 + 使用米数---------------------------- */
        AtomicInteger production = new AtomicInteger(1000);
        AtomicInteger unitUseCount = new AtomicInteger(0);
        fabricVOList.forEach(fabric -> {
            fabric.setProduction(BigDecimal.valueOf(production.getAndIncrement()));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doSave(ReplayRatingSaveDTO replayRatingSaveDTO) {
        String id = replayRatingSaveDTO.getId();
        ReplayRating replayRating = new ReplayRating();
        if (StrUtil.isNotBlank(id)) {
            this.warnMsg("无效的主键id");
            replayRating = this.findOne(id);
        }
        REPLAY_CV.copy2Entity(replayRating, replayRatingSaveDTO);

        this.saveOrUpdate(replayRating);

        String replayRatingId = replayRating.getId();

        List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                .eq(ReplayRatingDetail::getReplayRatingId, replayRatingId));

        replayRatingSaveDTO.getDetailListMap().forEach((replayRatingDetailType, replayRatingDetailListDTO) -> {
            // 获取跟进人
            List<ReplayRatingDetail> newReplayRatingDetailList = replayRatingDetailListDTO.stream().map(replayRatingDetailDTO -> {
                ReplayRatingDetail newReplayRatingDetail = replayRatingDetailList.stream()
                        .filter(it -> it.getId().equals(replayRatingDetailDTO.getId())).findFirst().orElse(new ReplayRatingDetail());
                replayRatingDetailList.remove(newReplayRatingDetail);

                replayRatingDetailDTO.setType(replayRatingDetailType);
                replayRatingDetailDTO.setReplayRatingId(replayRatingId);
                REPLAY_CV.copy(newReplayRatingDetail, replayRatingDetailDTO);
                newReplayRatingDetail.updateInit();
                return newReplayRatingDetail;
            }).collect(Collectors.toList());
            replayRatingDetailService.saveOrUpdateBatch(newReplayRatingDetailList);
        });

        if (CollUtil.isNotEmpty(replayRatingDetailList)) {
            replayRatingDetailService.removeByIds(replayRatingDetailList);
        }

        return replayRatingId;
    }

    @Override
    public ReplayRatingStyleDTO getStyleById(String styleColorId) {
        // 获取主表数据
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

        REPLAY_CV.copy(result, style);
        REPLAY_CV.copy(result, styleColor);

        if (replayRating != null) {
            REPLAY_CV.copy(result, replayRating);
            List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                    .eq(ReplayRatingDetail::getReplayRatingId, replayRating.getId())
            );
            replayRatingDetailList.stream().map(REPLAY_CV::copy2DTO).peek(it -> {
                it.setFileUrl(minioUtils.getObjectUrl(it.getFileUrl()));
            }).collect(Collectors.groupingBy(ReplayRatingDetail::getType)).forEach((type, list) -> {
                result.getDetailListMap().put(type, new ReplayRatingDetailList(list));
            });
        }

        // 销售等级周
        List<String> saleCycleList = StrUtil.split(replayConfig.getSaleCycle(), COMMA);
        result.setSaleLevelWeekends(CollUtil.removeEmpty(saleCycleList));

        // 设置款式基础数据
        QueryFieldManagementDto qo = new QueryFieldManagementDto();
        qo.setGroupName(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        if (StrUtil.isBlank(result.getSilhouetteCode())) {
            qo.setFieldExplain("廓形及代码");
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setSilhouetteCode, ReplayRatingStyleDTO::setSilhouetteName);
        }
        if (StrUtil.isBlank(result.getColorSystemCode())) {
            qo.setFieldExplain("色系");
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setColorSystemCode, ReplayRatingStyleDTO::setColorSystem);
        }
        if (StrUtil.isBlank(result.getFabricComposition())) {
            qo.setFieldExplain("面料成分");
            decorateFieldVal(Collections.singletonList(result), qo, ReplayRatingStyleDTO::getStyleId, ReplayRatingStyleDTO::setFabricComposition, ReplayRatingStyleDTO::setFabricComposition);
        }

        // 设置能否跳到版型复盘
        String registeringId = result.getRegisteringId();
        if (StrUtil.isNotBlank(registeringId)) {
            String registeringStyleId = patternLibraryService.findByIds2OneField(registeringId, PatternLibrary::getStyleId);
            if (StrUtil.isNotBlank(registeringStyleId)) {
                result.setGotoPatternId(styleColorService.findOneField(new LambdaQueryWrapper<StyleColor>().eq(StyleColor::getStyleId, registeringStyleId), StyleColor::getId));
            }
        }

        result.setSaleLevelList(findSaleLevelList(saleCycleList));
        result.setProductionSaleList(findProductionSaleList());
        result.setProductionInfoList(findProductionInfoList());

        return result;
    }

    // 销售等级
    private List<SaleLevelDTO> findSaleLevelList(List<String> saleCycleList) {
        return Stream.of("等级", "店均/件").map(saleLevelType -> {
            SaleLevelDTO saleLevelDTO = new SaleLevelDTO();
            saleLevelDTO.setType(saleLevelType);
            saleLevelDTO.setSeasonLevel("等级".equals(saleLevelType) ? ReplayRatingLevelEnum.A.getCode() : BigDecimal.ZERO.toString());
            saleLevelDTO.setWeekendDataMap(saleCycleList.stream().collect(Collectors.toMap(Function.identity(), (it) -> {
                if ("等级".equals(saleLevelType)) {
                    ReplayRatingLevelEnum levelEnum = ReplayRatingLevelEnum.S;
                    if ("4周".equals(it)) {
                        levelEnum = ReplayRatingLevelEnum.S;
                    }
                    if ("8周".equals(it)) {
                        levelEnum = ReplayRatingLevelEnum.S;
                    }
                    if ("12周".equals(it)) {
                        levelEnum = ReplayRatingLevelEnum.B;
                    }
                    return levelEnum;
                }
                if ("店均/件".equals(saleLevelType)) {
                    BigDecimal num = BigDecimal.ZERO;
                    if ("4周".equals(it)) {
                        num = num.add(new BigDecimal("2.1"));
                    }
                    if ("8周".equals(it)) {
                        num = num.add(new BigDecimal("3.2"));
                    }
                    if ("12周".equals(it)) {
                        num = num.add(new BigDecimal("4.5"));
                    }
                    saleLevelDTO.setSeasonLevel(new BigDecimal(saleLevelDTO.getSeasonLevel()).add(num).toString());
                    return num;
                }
                return "";
            })));
            saleLevelDTO.setPlanningLevel(ReplayRatingLevelEnum.S);
            return saleLevelDTO;
        }).collect(Collectors.toList());
    }

    // 生产销售
    private List<ProductionSaleDTO> findProductionSaleList() {
        return Stream.of("X", "XL", "XXL").map(size -> {
            ProductionSaleDTO productionSaleDTO = new ProductionSaleDTO();
            productionSaleDTO.setSizeCode(size);
            productionSaleDTO.setSizeName(size);
            productionSaleDTO.setProduction(new BigDecimal("1200"));
            productionSaleDTO.setProductionCount(5);
            productionSaleDTO.setSale(new BigDecimal(1200));
            productionSaleDTO.setStorageCount(new BigDecimal("0"));
            return productionSaleDTO;
        }).collect(Collectors.toList());
    }

    // 投产信息
    private List<ProductionInfoDTO> findProductionInfoList() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(now, now.minusDays(1)).map(dateTime -> {
            ProductionInfoDTO productionSaleDTO = new ProductionInfoDTO();
            productionSaleDTO.setDate(dateTime);
            if (dateTime == now) {
                productionSaleDTO.setOrderNo("54353453");
                productionSaleDTO.setProduction(new BigDecimal("1000"));
                productionSaleDTO.setSupplierInfoList(Arrays.asList(new SupplierInfo("1", "A供应商"), new SupplierInfo("2", "B供应商")));
                productionSaleDTO.setStorageCount(new BigDecimal("500"));
            } else {
                productionSaleDTO.setOrderNo("45654645");
                productionSaleDTO.setProduction(new BigDecimal("2000"));
                productionSaleDTO.setSupplierInfoList(Arrays.asList(new SupplierInfo("2", "B供应商")));
                productionSaleDTO.setStorageCount(new BigDecimal("1000"));
            }
            return productionSaleDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public ReplayRatingPatternDTO getPatternById(String styleColorId) {
        // 复盘评分维度是大货款, 但其他数据维度是设计款, 所以这里只要styleId即可
        styleColorService.warnMsg("未找到对应大货款");
        String styleId = styleColorService.findByIds2OneField(styleColorId, StyleColor::getStyleId);

        // 获取主表数据
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

        result.setStyleColorId(styleColorId);
        REPLAY_CV.copy(result, patternLibrary);

        result.setSaleSeason(replayConfig.getSaleSeason());
        if (replayRating != null) {
            REPLAY_CV.copy(result, replayRating);
            List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                    .eq(ReplayRatingDetail::getReplayRatingId, replayRating.getId())
            );
            replayRatingDetailList.stream().map(REPLAY_CV::copy2DTO).peek(it -> {
                it.setFileUrl(minioUtils.getObjectUrl(it.getFileUrl()));
            }).collect(Collectors.groupingBy(ReplayRatingDetail::getType)).forEach((type, list) -> {
                result.getDetailListMap().put(type, new ReplayRatingDetailList(list));
            });
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
        // 获取主表数据
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

        REPLAY_CV.copy(result, material);
        result.setColor(String.join("/", colorMap.values()));
        result.setColorCode(String.join(COMMA, colorMap.keySet()));
        result.setYearArray(ReplayRatingProperties.years);
        result.setCmtUnit(UnitConverterEnum.KILOMETER);
        result.setFobUnit(UnitConverterEnum.PIECE);
        result.setTranslate(String.join("/", materialWidthMap.keySet()));
        result.setTranslateCode(String.join(COMMA, materialWidthMap.values()));

        if (replayRating != null) {
            REPLAY_CV.copy(result, replayRating);
            List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                    .eq(ReplayRatingDetail::getReplayRatingId, replayRating.getId())
            );
            replayRatingDetailList.stream().map(REPLAY_CV::copy2DTO).peek(it -> {
                it.setFileUrl(minioUtils.getObjectUrl(it.getFileUrl()));
            }).collect(Collectors.groupingBy(ReplayRatingDetail::getType)).forEach((type, list) -> {
                result.getDetailListMap().put(type, new ReplayRatingDetailList(list));
            });
        }

        /* ----------------------------获取大货款号以及物料清单---------------------------- */
        List<PackBom> packBomList = packBomService.list(new LambdaQueryWrapper<PackBom>()
                .select(PackBom::getBulkUnitUse, PackBom::getForeignId)
                .eq(PackBom::getMaterialId, materialId)
                .eq(PackBom::getStatus, YesOrNoEnum.YES)
                .eq(PackBom::getPackType, PackUtils.PACK_TYPE_BIG_GOODS)
        );
        if (CollUtil.isEmpty(packBomList)) return result;

        Map<String, BigDecimal> bulkUseMap = packBomList.stream().collect(CommonUtils.groupingSingleBy(PackBom::getForeignId,
                (list) -> BigDecimal.valueOf(list.stream()
                        .map(PackBom::getBulkUnitUse)
                        .filter(Objects::nonNull)
                        .mapToDouble(BigDecimal::doubleValue)
                        .sum()
                ).setScale(2, RoundingMode.HALF_UP)
        ));

        Map<String, String> packInfoStyleColorMap = MapUtil.reverse(packInfoService.mapOneField(new LambdaQueryWrapper<PackInfo>()
                .in(PackInfo::getId, bulkUseMap.keySet()), PackInfo::getId, PackInfo::getStyleColorId));

        Map<ProductionType, List<String>> devtTypeStyleColorIdMap = CommonUtils.inverse(styleColorService.mapOneField(new LambdaQueryWrapper<StyleColor>()
                .in(StyleColor::getId, packInfoStyleColorMap.keySet()), StyleColor::getId, StyleColor::getDevtType));

        Map<ProductionType, List<FabricMonthDataDto>> monthDataMap = Arrays.stream(ProductionType.values()).collect(Collectors.toMap(Function.identity(), (it) -> new ArrayList<>()));
        devtTypeStyleColorIdMap.forEach((devtType, styleColorIdList) -> {
            List<FabricMonthDataDto> dataList = monthDataMap.get(devtType);
            for (int year : result.getYearArray()) {
                // 生成查询时间
                YearMonth startDate = YearMonth.of(year, Month.JANUARY);
                YearMonth endDate;
                for (Month month : Month.values()) {
                    int value = month.getValue();
                    startDate = startDate.withMonth(value);
                    endDate = startDate.plusMonths(1);
                    // 查投产信息
                    int baseVal = devtType == ProductionType.CMT ? year : value + 10;
                    dataList.add(new FabricMonthDataDto(startDate, endDate, RandomUtil.randomBigDecimal(BigDecimal.valueOf(baseVal / 2), BigDecimal.valueOf(baseVal))));
                }
            }
        });
        result.setMonthData(monthDataMap);

        return result;
    }

    @Override
    public PageInfo<ReplayRatingYearVO> yearListByStyleNo(ReplayRatingYearQO replayRatingYearQO) {
        // 获取使用了该版型的设计款
        ReplayRatingQO replayRatingQO = BeanUtil.copyProperties(replayRatingYearQO, ReplayRatingQO.class);
        replayRatingQO.setType(ReplayRatingType.STYLE);
        replayRatingQO.setRegisteringId(replayRatingYearQO.getPatternLibraryId());
        replayRatingQO.setMaterialCode(replayRatingYearQO.getMaterialCode());
        ReplayRatingPageVO<? extends ReplayRatingVO> pageInfo = queryPageInfo(replayRatingQO);

        List<? extends ReplayRatingVO> list = pageInfo.getList();

        List<ReplayRatingYearVO> yearVOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            List<ReplayConfig> configList = new ArrayList<>();

            Map<String, List<PackBom>> stylePackBomListMap = new HashMap<>();
            switch (replayRatingYearQO.getType()) {
                case PATTERN:
                    List<String> brandList = list.stream().map(ReplayRatingVO::getBrand).distinct().collect(Collectors.toList());
                    configList.addAll(replayConfigService.list(new LambdaQueryWrapper<ReplayConfig>().in(ReplayConfig::getBrand, brandList)));
                    break;
                case FABRIC:
                    List<String> styleColorIdList = list.stream().map(ReplayRatingVO::getStyleColorId).distinct().collect(Collectors.toList());
                    Map<String, String> styleColorIdMap = packInfoService.mapOneField(
                            new LambdaQueryWrapper<PackInfo>().in(PackInfo::getStyleColorId, styleColorIdList)
                            , PackInfo::getStyleColorId, PackInfo::getId
                    );
                    if (MapUtil.isNotEmpty(styleColorIdMap)) {
                        List<PackBom> packBomList = packBomService.list(new LambdaQueryWrapper<PackBom>()
                                .select(PackBom::getForeignId, PackBom::getCollocationCode, PackBom::getCollocationName, PackBom::getBulkUnitUse)
                                .eq(PackBom::getPackType, PackUtils.PACK_TYPE_BIG_GOODS)
                                .in(PackBom::getForeignId, styleColorIdMap.values())
                                .eq(PackBom::getStatus, YesOrNoEnum.YES)
                        );
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

            list.forEach(replayRatingVO -> {
                ReplayRatingYearVO replayRatingYearVO = new ReplayRatingYearVO();
                REPLAY_CV.copy(replayRatingYearVO, replayRatingVO);

                List<PackBom> stylePackBomList = stylePackBomListMap.getOrDefault(replayRatingYearVO.getStyleColorId(), new ArrayList<>());
                replayRatingYearVO.setCollocationCode(stylePackBomList.stream().map(PackBom::getCollocationCode).distinct().collect(Collectors.joining(COMMA)));
                replayRatingYearVO.setCollocationName(stylePackBomList.stream().map(PackBom::getCollocationName).distinct().collect(Collectors.joining("\n")));

                replayRatingYearVO.setSeasonFlag(replayRatingYearQO.getSeasonFlag());
                // 设置年份数据
                ReplayRatingYearProductionSaleDTO yearProductionSaleDTO = new ReplayRatingYearProductionSaleDTO();
                yearProductionSaleDTO.setKey("total");
                replayRatingYearVO.setReplayRatingYearProductionSaleDTO(yearProductionSaleDTO);

                ReplayConfigDetailDTO saleSeason = configList.stream()
                        .filter(it -> it.getBrand().equals(replayRatingVO.getBrand()))
                        .findFirst().map(it -> {
                            it.build();
                            return it.getSaleSeason();
                        }).orElse(new ReplayConfigDetailDTO());

                BigDecimal production = BigDecimal.valueOf(1000);
                BigDecimal sale = BigDecimal.valueOf(500);

                List<ReplayRatingYearProductionSaleDTO> childrenList = new ArrayList<>();
                yearProductionSaleDTO.setChildrenList(childrenList);

                ReplayRatingYearProductionSaleDTO longTimeAgoChildren = new ReplayRatingYearProductionSaleDTO();
                longTimeAgoChildren.setKey("longTimeAgo");
                longTimeAgoChildren.setChildrenList(new ArrayList<>());
                childrenList.add(longTimeAgoChildren);

                YearMonth date = YearMonth.now().withMonth(Month.JANUARY.getValue());
                Map<String, Object> saleSeasonMap = BeanUtil.beanToMap(saleSeason);
                for (Map.Entry<String, Object> entry : saleSeasonMap.entrySet()) {
                    List<ReplayConfigTimeDTO> timeDTOList = Arrays.stream(SeasonEnum.values()).map(ReplayConfigTimeDTO::new).collect(Collectors.toList());
                    if (ObjectUtil.isNotEmpty(entry.getValue())) {
                        timeDTOList = (List<ReplayConfigTimeDTO>) entry.getValue();
                    }
                    replayRatingYearVO.getYearList().add(entry.getKey());
                    ReplayRatingYearProductionSaleDTO children = new ReplayRatingYearProductionSaleDTO();
                    children.setKey(entry.getKey());
                    List<ReplayRatingYearProductionSaleDTO> subChildrenList = new ArrayList<>();
                    children.setChildrenList(subChildrenList);
                    for (int i = 0; i < timeDTOList.size(); i++) {
                        ReplayConfigTimeDTO timeDTO = timeDTOList.get(i);
                        ReplayRatingYearProductionSaleDTO subChildren = new ReplayRatingYearProductionSaleDTO();
                        String code = timeDTO.getSeason().name().toLowerCase();

                        YearMonth startMonth = replayRatingYearVO.getSeasonFlag() == YesOrNoEnum.NO ? date.plusMonths(i * 3L) : timeDTO.getStartMonth();
                        YearMonth endMonth = replayRatingYearVO.getSeasonFlag() == YesOrNoEnum.NO ? date.plusMonths((i + 1) * 3L) : timeDTO.getEndMonth();
                        subChildren.setKey(code);
                        production = production.subtract(BigDecimal.TEN);
                        sale = sale.add(BigDecimal.TEN);
                        subChildren.setProduction(production);
                        subChildren.setSale(sale);
                        subChildrenList.add(subChildren);

                        ReplayRatingYearProductionSaleDTO subLongTimeAgoChildren = longTimeAgoChildren.getChildrenList().stream().filter(it -> it.getKey().equals(code))
                                .findFirst().orElseGet(() -> {
                                    ReplayRatingYearProductionSaleDTO dto = new ReplayRatingYearProductionSaleDTO();
                                    dto.setProduction(BigDecimal.TEN);
                                    dto.setSale(BigDecimal.ONE);
                                    longTimeAgoChildren.getChildrenList().add(dto);
                                    return dto;
                                });
                        subLongTimeAgoChildren.setKey(code);
                        subLongTimeAgoChildren.setProduction(subLongTimeAgoChildren.getProduction().subtract(BigDecimal.ONE));
                        subLongTimeAgoChildren.setSale(subLongTimeAgoChildren.getSale().add(BigDecimal.ONE));
                    }
                    childrenList.add(children);
                    date = date.minusYears(1);
                }

                yearProductionSaleDTO.calculate();

                yearVOList.add(replayRatingYearVO);
            });

            QueryFieldManagementDto queryDto = new QueryFieldManagementDto();
            queryDto.setGroupName(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
            queryDto.setFieldExplain("廓形及代码");
            decorateFieldVal(yearVOList, queryDto, ReplayRatingYearVO::getStyleId, ReplayRatingYearVO::setSilhouetteCode, ReplayRatingYearVO::setSilhouetteName);
        }
        return CopyUtil.copy(pageInfo, yearVOList);
    }

    @Override
    public String transferPatternLibrary(ReplayRatingTransferDTO transferDTO) {
        String styleColorId = transferDTO.getStyleColorId();
        String styleId = transferDTO.getStyleId();
        String bulkStyleNo = styleColorService.findOneField(new LambdaQueryWrapper<StyleColor>().eq(StyleColor::getStyleId, styleId).eq(StyleColor::getId, styleColorId), StyleColor::getStyleNo);
        if (StrUtil.isBlank(bulkStyleNo)) throw new OtherException("错误的款式配色");

        styleService.warnMsg("未找到对应设计款");
        Style style = styleService.findOne(styleId);

        PatternLibrary patternLibrary = patternLibraryService.findOne(new LambdaQueryWrapper<PatternLibrary>().eq(PatternLibrary::getStyleId, styleId));

        if (patternLibrary == null) {
            if (transferDTO.getTransferParentFlag() == null) throw new OtherException("未找到对应的版型库");
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
            libraryDTO.setSilhouetteCode(style.getSilhouette());
            libraryDTO.setSilhouetteName(style.getSilhouetteName());
            libraryDTO.setTemplateCode(transferDTO.getTemplateCode());
            libraryDTO.setTemplateName(transferDTO.getTemplateName());
            libraryDTO.setPicId(style.getStylePic());
            libraryDTO.setPicUrl(style.getStylePic());
            libraryDTO.setPicSource(2);
            libraryDTO.setStatus(2);
            libraryDTO.setEnableFlag(YesOrNoEnum.YES.getValue());
            libraryDTO.setPatternLibraryBrandList(Collections.singletonList(new PatternLibraryBrand()));

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
            });

            if (StrUtil.isNotBlank(style.getPatternParts())) {
                ProcessDatabasePageDto processDatabasePageDto = new ProcessDatabasePageDto();
                processDatabasePageDto.setStatus("0");
                processDatabasePageDto.setType("7");
                processDatabasePageDto.setCategoryId(style.getProdCategory());
                processDatabasePageDto.setBrandCode(style.getBrand());
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
            patternLibraryService.saveOrUpdateDetails(libraryDTO);
            return libraryDTO.getId();
        } else {
            patternLibrary.setStyleNos(StrJoiner.of(COMMA).setNullMode(StrJoiner.NullMode.IGNORE).append(patternLibrary.getStyleNos()).append(bulkStyleNo).toString());
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
        qo.reset2QueryList();
        List<? extends ReplayRatingVO> list = queryPageInfo(qo).getList();
        ExcelUtils.exportExcelByTableCode(list, qo.getType().getText(), qo);
    }

// 自定义方法区 不替换的区域【other_end】

}
