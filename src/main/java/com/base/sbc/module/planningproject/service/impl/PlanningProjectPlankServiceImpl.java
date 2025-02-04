package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.vo.FieldDisplayVo;
import com.base.sbc.module.planningproject.constants.GeneralConstant;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.*;
import com.base.sbc.module.planningproject.mapper.PlanningProjectPlankMapper;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.SaleProductIntoDto;
import com.base.sbc.module.smp.entity.SalesData;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.module.tablecolumn.vo.TableColumnVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:19
 * @mail 247967116@qq.com
 */
@Service
public class PlanningProjectPlankServiceImpl extends BaseServiceImpl<PlanningProjectPlankMapper, PlanningProjectPlank> implements PlanningProjectPlankService {
    @Lazy
    @Autowired
    private StyleColorService styleColorService;
    @Autowired
    private StylePicUtils stylePicUtils;
    @Autowired
    private StylePricingMapper stylePricingMapper;
    @Autowired
    private BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    @Autowired
    private FieldValService fieldValService;
    @Autowired
    @Lazy
    private PlanningProjectDimensionService planningProjectDimensionService;
    @Autowired
    @Lazy
    private PlanningProjectPlankDimensionService planningProjectPlankDimensionService;
    @Autowired
    @Lazy
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private FieldManagementService fieldManagementService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    @Lazy
    private SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    @Autowired
    @Lazy
    private CategoryPlanningDetailsService categoryPlanningDetailsService;

    @Resource
    @Lazy
    private PlanningProjectService planningProjectService;
    @Resource
    @Lazy
    private MinioUtils minioUtils;
    @Resource
    private SmpService smpService;

    @Override
    public Map<String, Object> ListByDto(PlanningProjectPlankPageDto dto) {
        List<FieldDisplayVo> dimensionFieldCard = this.getDimensionFieldCard(dto);
        Map<String, Object> hashMap = new HashMap<>();

        String dimensionIds = dto.getDimensionIds();
        if (ObjectUtil.isEmpty(dimensionIds)) {
            throw new OtherException("请选择维度数据！");
        }

        PlanningProject planningProject = planningProjectService.getById(dto.getPlanningProjectId());

        BaseQueryWrapper<PlanningProjectPlank> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tppp.planning_project_id", dto.getPlanningProjectId());
        queryWrapper.notEmptyEq("tppd.prod_category_code", dto.getProdCategoryCode());
        queryWrapper.notEmptyEq("tppd.prod_category2nd_code", dto.getProdCategory2ndCode());
        queryWrapper.notEmptyEq("tppd.prod_category1st_code", dto.getProdCategory1stCode());
        queryWrapper.notEmptyIn("tppd.dimension_id", dimensionIds);
        if (StringUtils.isNotEmpty(dto.getPlanningBandCode())) {
            String[] split = dto.getPlanningBandCode().split(",");
            queryWrapper.in("tppp.band_code", Arrays.asList(split));
        }
        queryWrapper.notEmptyEq("tppp.bulk_style_no", dto.getPlanningBulkStyleNo());
        queryWrapper.orderBy(true, true, "tppp.band_code");

        List<PlanningProjectPlankVo> list = this.baseMapper.queryPage(queryWrapper);

        if (ObjectUtil.isEmpty(list)) {
            ArrayList<Object> resultEmptyList = new ArrayList<>();
            hashMap.put("list", resultEmptyList);
            hashMap.put("tableColumnVos", resultEmptyList);
            hashMap.put("map", resultEmptyList);
            hashMap.put("map2", resultEmptyList);
            return hashMap;
        }

        List<String> ids = list.stream().map(PlanningProjectPlankVo::getId).collect(Collectors.toList());


        // 维度
        QueryWrapper<PlanningProjectPlankDimension> dimensionQueryWrapper = new QueryWrapper<>();
        dimensionQueryWrapper.in("planning_project_plank_id", ids);
        List<PlanningProjectPlankDimension> planningProjectPlankDimensions = planningProjectPlankDimensionService.list(dimensionQueryWrapper);
        Map<String, List<PlanningProjectPlankDimension>> dimensionMap = planningProjectPlankDimensions.stream().collect(Collectors.groupingBy(PlanningProjectPlankDimension::getPlanningProjectPlankId));
        List<String> styleColorIds = list.stream().map(PlanningProjectPlankVo::getStyleColorId).collect(Collectors.toList());
        List<String> bulkStyleNos = list.stream().map(PlanningProjectPlankVo::getBulkStyleNo).collect(Collectors.toList());
        List<String> hisDesignNos = list.stream().map(PlanningProjectPlankVo::getHisDesignNo).collect(Collectors.toList());
        SaleProductIntoDto saleProductIntoDto = new SaleProductIntoDto();
        bulkStyleNos.addAll(hisDesignNos);
        saleProductIntoDto.setBulkStyleNoList(bulkStyleNos);
        PageInfo<OrderBookSimilarStyleVo> orderBookSimilarStyleVoPageInfo = smpService.querySaleIntoPageTotal(saleProductIntoDto);
        List<OrderBookSimilarStyleVo> list4 = orderBookSimilarStyleVoPageInfo.getList();
        Map<String, OrderBookSimilarStyleVo> map2 = list4.stream().collect(Collectors.toMap(OrderBookSimilarStyleVo::getBulkStyleNo, e -> e));

        if (!styleColorIds.isEmpty()) {
            List<StyleColor> list3 = styleColorService.list(new QueryWrapper<StyleColor>().in("style_no", hisDesignNos).select("id"));
            styleColorIds.addAll(list3.stream().map(StyleColor::getId).collect(Collectors.toList()));
        }
        // List<StyleColor> list4 = styleColorService.list(new QueryWrapper<StyleColor>().in("id", styleColorIds).select("id", "bom_status"));
        // Map<String, String> styleColorMap = list4.stream().collect(Collectors.toMap(StyleColor::getId, StyleColor::getBomStatus));
        List<FieldVal> fieldValList = styleColorService.ListDynamicDataByIds(styleColorIds);
        // 匹配
        // this.match(list);
        List<TableColumnVo> tableColumnVos = new ArrayList<>();
        Map<String, Integer> map = new TreeMap<>();
        Map<String, JSONObject> map1 = new TreeMap<>();


        BaseQueryWrapper<PlanningDimensionality> qw = new BaseQueryWrapper<>();
        qw.eq("planning_season_id", planningProject.getSeasonId());
        qw.eq("channel", planningProject.getPlanningChannelCode());
        qw.eq("coefficient_flag", "1");
        // qw.groupBy("dimensionality_name,prod_category,prod_category2nd");
        qw.orderByDesc("id");
        List<PlanningDimensionality> planningDimensionalityList = planningDimensionalityService.list(qw);

        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            // 获取所有波段,当作列  现在需要波段加上款式类别
            if (StringUtils.isNotEmpty(planningProjectPlankVo.getBandName())) {
                Integer i = map.get(planningProjectPlankVo.getBandName() + "," + planningProjectPlankVo.getBandCode() + "," + planningProjectPlankVo.getStyleCategory());
                if (i == null) {
                    if (StringUtils.isNotEmpty(planningProjectPlankVo.getId())) {
                        map.put(planningProjectPlankVo.getBandName() + "," + planningProjectPlankVo.getBandCode() + "," + planningProjectPlankVo.getStyleCategory(), 1);
                    } else {
                        map.put(planningProjectPlankVo.getBandName() + "," + planningProjectPlankVo.getBandCode() + "," + planningProjectPlankVo.getStyleCategory(), 0);
                    }
                } else {
                    if (StringUtils.isNotEmpty(planningProjectPlankVo.getId())) {
                        i++;
                    }
                    map.put(planningProjectPlankVo.getBandName() + "," + planningProjectPlankVo.getBandCode() + "," + planningProjectPlankVo.getStyleCategory(), i);
                }
            }
            // 获取所有的第一维度
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dimensionId", planningProjectPlankVo.getDimensionId());
            jsonObject.put("dimensionValue", planningProjectPlankVo.getDimensionValue());
            jsonObject.put("dimensionName", planningProjectPlankVo.getDimensionName());
            jsonObject.put("dimensionCode", planningProjectPlankVo.getDimensionCode());
            jsonObject.put("planningProjectDimensionId", planningProjectPlankVo.getPlanningProjectDimensionId());
            jsonObject.put("bandName", planningProjectPlankVo.getBandName());
            jsonObject.put("bandCode", planningProjectPlankVo.getBandCode());

            map1.put(planningProjectPlankVo.getDimensionValue() + planningProjectPlankVo.getDimensionCode() + planningProjectPlankVo.getDimensionName() + planningProjectPlankVo.getDimensionId(), jsonObject);
            // map2.put(planningProjectPlankVo.getDimensionValue()+planningProjectPlankVo.getDimensionCode()+planningProjectPlankVo.getDimensionName()+planningProjectPlankVo.getDimensionId()+planningProjectPlankVo.getBandName()+planningProjectPlankVo.getBandCode(), jsonObject);


            // if (StringUtils.isNotEmpty(planningProjectPlankVo.getStyleColorId())) {
            //     // List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(planningProjectPlankVo.getStyleColorId());
            //     // planningProjectPlankVo.setFieldManagementVos(fieldManagementVos);
            // }

            if (StringUtils.isNotEmpty(planningProjectPlankVo.getHisDesignNo())) {
                StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", planningProjectPlankVo.getHisDesignNo()).select("id", "style_color_pic"));
                StyleColorVo styleColorVo = new StyleColorVo();
                BeanUtil.copyProperties(styleColor, styleColorVo);
                String styleUrl = stylePicUtils.getStyleUrl(styleColorVo.getStyleColorPic());
                styleColorVo.setStyleColorPic(styleUrl);
                planningProjectPlankVo.setOldStyleColor(styleColorVo);
            }


            // 先去根据中类获取
            List<PlanningDimensionality> planningDimensionalities = new ArrayList<>();
            for (PlanningDimensionality planningDimensionality : planningDimensionalityList) {
                if (planningDimensionality.getProdCategory().equals(planningProjectPlankVo.getProdCategoryCode())) {
                    if (StringUtils.isNotEmpty(planningProjectPlankVo.getProdCategory2ndCode())) {
                        if (planningProjectPlankVo.getProdCategory2ndCode().equals(planningDimensionality.getProdCategory2nd())) {
                            planningDimensionalities.add(planningDimensionality);
                        }
                    } else {
                        planningDimensionalities.add(planningDimensionality);
                    }
                }
            }
            // 如果为空,说明中类未配置,取品类
            if (planningDimensionalities.isEmpty()) {
                for (PlanningDimensionality planningDimensionality : planningDimensionalityList) {
                    if (planningDimensionality.getProdCategory().equals(planningProjectPlankVo.getProdCategoryCode())) {
                        planningDimensionalities.add(planningDimensionality);

                    }
                }
            }


            // 查询字段管理配置
            List<String> fieldManagementIds = planningDimensionalities.stream().map(PlanningDimensionality::getFieldId).collect(Collectors.toList());
            List<FieldManagement> fieldManagements = new ArrayList<>();
            if (!fieldManagementIds.isEmpty()) {
                fieldManagements = fieldManagementService.listByIds(fieldManagementIds);
            }

            Map<String, FieldManagement> fieldManagementMap = fieldManagements.stream().filter(item -> !GeneralConstant.FIXED_ATTRIBUTES.equals(item.getGroupName())).collect(Collectors.toMap(FieldManagement::getId, f -> f));

            // 获取维度列表
            List<PlanningProjectPlankDimension> list1 = dimensionMap.get(planningProjectPlankVo.getId());

            List<PlanningProjectPlankDimension> list2 = new ArrayList<>();
            Map<String, PlanningProjectPlankDimension> plankDimensionMap = new HashMap<>();
            if (list1 != null) {
                plankDimensionMap = list1.stream().collect(Collectors.toMap(PlanningProjectPlankDimension::getDimensionName, planningProjectPlankDimension -> planningProjectPlankDimension));
            }
            for (PlanningDimensionality planningDimensionality : planningDimensionalities) {
                PlanningProjectPlankDimension planningProjectPlankDimension = plankDimensionMap.get(planningDimensionality.getDimensionalityName());
                if (planningProjectPlankDimension == null) {
                    planningProjectPlankDimension = new PlanningProjectPlankDimension();
                    planningProjectPlankDimension.setPlanningProjectPlankId(planningProjectPlankVo.getId());
                    planningProjectPlankDimension.setDimensionName(planningDimensionality.getDimensionalityName());
                    planningProjectPlankDimension.setDimensionalityGrade(planningDimensionality.getDimensionalityGrade());
                    planningProjectPlankDimension.setDimensionalityGradeName(planningDimensionality.getDimensionalityGradeName());
                    // 如果没有编辑就从配色取,
                    for (FieldVal fieldVal : fieldValList) {
                        String styleColorId;
                        if ("0".equals(planningProjectPlankVo.getMatchingStyleStatus())) {
                            break;
                        }
                        if ("3".equals(planningProjectPlankVo.getMatchingStyleStatus())) {
                            styleColorId = planningProjectPlankVo.getOldStyleColor().getId();
                        } else {
                            styleColorId = planningProjectPlankVo.getStyleColorId();
                        }

                        if (StringUtils.isNotBlank(fieldVal.getForeignId()) && fieldVal.getForeignId().equals(styleColorId) && planningDimensionality.getFieldId().equals(fieldVal.getFieldId())) {
                            // if ("5F2260721X".equals(planningProjectPlankVo.getHisDesignNo())) {
                            //     if ("色相".equals(planningDimensionality.getDimensionalityName())) {
                            //         System.out.println(planningDimensionality);
                            //     }
                            // }
                            planningProjectPlankDimension.setDimensionCode(fieldVal.getFieldName());
                            planningProjectPlankDimension.setDimensionValue(fieldVal.getVal());
                            planningProjectPlankDimension.setDimensionValueName(fieldVal.getValName());
                            break;
                        }
                    }
                }
                // if ("1".equals(planningProjectPlankDimension.getDimensionalityGrade())) {
                //     planningProjectPlankDimension.setDimensionValueName(planningProjectPlankVo.getDimensionCode());
                // }
                FieldManagement fieldManagement = fieldManagementMap.get(planningDimensionality.getFieldId());
                if (fieldManagement != null) {
                    planningProjectPlankDimension.setGroupName(fieldManagement.getGroupName());
                    planningProjectPlankDimension.setFieldManagement(fieldManagement);
                }

                list2.add(planningProjectPlankDimension);
            }
            String names = dto.getNames();
            if (StringUtils.isNotEmpty(names)) {
                for (String s : names.split(",")) {

                    PlanningProjectPlankDimension planningProjectPlankDimension = new PlanningProjectPlankDimension();
                    FieldManagement fieldManagement = new FieldManagement();
                    fieldManagement.setFieldExplain(s);
                    if ("波段".equals(s)) {
                        planningProjectPlankDimension.setDimensionValueName(planningProjectPlankVo.getBandName());
                    }

                    OrderBookSimilarStyleVo orderBookSimilarStyleVo = null;
                    if ("1".equals(planningProjectPlankVo.getMatchingStyleStatus())) {
                        orderBookSimilarStyleVo = map2.get(planningProjectPlankVo.getBulkStyleNo());
                    }
                    if ("3".equals(planningProjectPlankVo.getMatchingStyleStatus())) {
                        orderBookSimilarStyleVo = map2.get(planningProjectPlankVo.getHisDesignNo());
                    }
                    if (orderBookSimilarStyleVo != null) {
                        if ("产销".equals(s)) {
                            planningProjectPlankDimension.setDimensionValueName(orderBookSimilarStyleVo.getTotalSaleInto());
                        }
                        if ("销量".equals(s)) {
                            planningProjectPlankDimension.setDimensionValueName(orderBookSimilarStyleVo.getTotalSale().toString());
                        }
                    }

                    planningProjectPlankDimension.setFieldManagement(fieldManagement);
                    list2.add(planningProjectPlankDimension);
                }

            }


            for (PlanningProjectPlankDimension planningProjectPlankDimension : list2) {
                for (FieldDisplayVo fieldDisplayVo : dimensionFieldCard) {
                    if (planningProjectPlankDimension.getFieldManagement() != null && planningProjectPlankDimension.getFieldManagement().getFieldExplain().equals(fieldDisplayVo.getName())) {
                        planningProjectPlankDimension.setSort(fieldDisplayVo.getSort());
                        planningProjectPlankDimension.setDisplay(fieldDisplayVo.isDisplay());
                        break;
                    }
                }
            }
            // 按照分组名称进行分组,分组名称为空则过滤掉
            // Map<String, List<PlanningProjectPlankDimension>> groupedList = list2.stream()
            //         .filter(dimension -> dimension.getGroupName() != null).collect(Collectors.groupingBy(PlanningProjectPlankDimension::getGroupName));


            planningProjectPlankVo.setDimensionList(list2);
        }

        // 生成表格列
        for (Map.Entry<String, Integer> stringStringEntry : map.entrySet()) {
            TableColumnVo tableColumnVo = new TableColumnVo();
            String[] split = stringStringEntry.getKey().split(",");
            tableColumnVo.setTitle(split[0] + "/" + split[2]);
            tableColumnVo.setColKey(split[1] + "/" + split[2]);
            tableColumnVo.setNum(String.valueOf(stringStringEntry.getValue()));
            tableColumnVos.add(tableColumnVo);
        }

        List<JSONObject> jsonObjects = new ArrayList<>();
        // List<JSONObject> jsonObjects2 =new ArrayList<>();
        for (String s : map1.keySet()) {
            jsonObjects.add(map1.get(s));
        }
        // for (String s : map2.keySet()) {
        //     jsonObjects2.add(map2.get(s));
        // }

        stylePicUtils.setStyleColorPic2(list, "pic");
        minioUtils.setObjectUrlToList(list, "selfPic");

        // 实时查询大货款对应的合并款下所有大货的总销量和总投产
        if (ObjectUtil.isNotEmpty(list)) {
            // 拿到所有的大货款号
            List<String> styleNoList = list
                    .stream()
                    .map(PlanningProjectPlankVo::getHisDesignNo)
                    .filter(ObjectUtil::isNotEmpty)
                    .distinct()
                    .collect(Collectors.toList());
            // 根据大货款号查询到销量和投产数据
            List<SalesData> salesDataList = querySaleDataByStyleNos(styleNoList);
            if (ObjectUtil.isNotEmpty(salesDataList)) {
                Map<String, SalesData> salesDataMap = salesDataList
                        .stream().collect(Collectors.toMap(SalesData::getGoodsNo, item -> item));
                for (PlanningProjectPlankVo planningProjectPlankVo : list) {
                    List<PlanningProjectPlankDimension> dimensionList = planningProjectPlankVo.getDimensionList();
                    String hisDesignNo = planningProjectPlankVo.getHisDesignNo();
                    String bulkStyleNo = planningProjectPlankVo.getBulkStyleNo();
                    String no = StrUtil.isNotBlank(bulkStyleNo) ? bulkStyleNo : hisDesignNo;
                    if (StrUtil.isNotBlank(no) && ObjectUtil.isNotEmpty(dimensionList)) {
                        SalesData salesData = salesDataMap.get(no);
                        if (ObjectUtil.isNotEmpty(salesData)) {
                            BigDecimal salesNum = salesData.getSalesNum();
                            BigDecimal productionNum = salesData.getProductionNum();
                            if (salesNum.equals(BigDecimal.ZERO) || productionNum.equals(BigDecimal.ZERO)) {
                                planningProjectPlankVo.setSale("0");
                                planningProjectPlankVo.setSaleInto("0");
                            } else {
                                for (PlanningProjectPlankDimension planningProjectPlankDimension : dimensionList) {
                                    FieldManagement fieldManagement = planningProjectPlankDimension.getFieldManagement();
                                    if (ObjectUtil.isNotEmpty(fieldManagement)) {
                                        if ("销量".equals(fieldManagement.getFieldExplain())) {
                                            planningProjectPlankDimension.setDimensionValueName(String.valueOf(salesNum));
                                        }
                                        if ("产销".equals(fieldManagement.getFieldExplain())) {
                                            planningProjectPlankDimension.setDimensionValueName(salesNum.multiply(new BigDecimal("100")).divide(productionNum, 2, RoundingMode.HALF_UP) + "%");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        hashMap.put("list", list);
        hashMap.put("tableColumnVos", tableColumnVos);
        hashMap.put("map", jsonObjects);
        QueryWrapper<PlanningProjectDimension> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("planning_project_id", dto.getPlanningProjectId());
        List<PlanningProjectDimension> list1 = planningProjectDimensionService.list(queryWrapper1);
        hashMap.put("map2", list1);
        return hashMap;
    }

    /**
     * 根据大货款号集合查询销售信息（销售量和投产量），这个是计算这个大货对应的合并款号下的所有大货的销售信息相加
     *
     * @param styleNoList 大货款号集合
     * @return 销售信息（销售量和投产量）
     */
    public List<SalesData> querySaleDataByStyleNos(List<String> styleNoList) {
        if (ObjectUtil.isEmpty(styleNoList)) {
            return new ArrayList<>();
        }
        // 先根据大货款号集合查询出大货款号对应的合并款号 大货款号-合并款号
        List<SalesData> goodsNoInfoList = smpService.queryMergeGoodsNoByGoodsNo(styleNoList);
        if (ObjectUtil.isNotEmpty(goodsNoInfoList)) {
            // 如果不为空那么根据合并款号的集合查询所有对应的大货款号
            List<String> mergeGoodsNoList = goodsNoInfoList
                    .stream().map(SalesData::getMergeGoodsNo).distinct().collect(Collectors.toList());
            // 大货款号-合并款号
            List<SalesData> mergeGoodsNoInfoList = smpService.queryGoodsNoByMergeGoodsNo(mergeGoodsNoList);
            if (ObjectUtil.isNotEmpty(mergeGoodsNoInfoList)) {
                // 如果查询到了大货款号集合 那么查询大货款号对应的销售信息
                // 最终地需要查询销售信息的大货款号集合
                List<String> finalGoodsNoList = mergeGoodsNoInfoList
                        .stream().map(SalesData::getGoodsNo).distinct().collect(Collectors.toList());
                // 大货款号-销量（单个大货款的销量）-投产（单个大货款的投产）
                List<SalesData> salesDataList = smpService.querySalesNumAndProductionNumByGoodsNos(finalGoodsNoList);
                // 销售信息按照大货款号分组
                Map<String, SalesData> salesDataMap = salesDataList
                        .stream().collect(Collectors.toMap(SalesData::getGoodsNo, item -> item));

                // 合并款号信息按照合并款号分组
                Map<String, List<SalesData>> mergeGoodsNoMap = mergeGoodsNoInfoList
                        .stream().collect(Collectors.groupingBy(SalesData::getMergeGoodsNo));

                // 初始化合计统计的集合  大货款号-合并款号-销量（合并款号下的所有大货的销量）-投产（合并款号下的所有大货的投产）
                List<SalesData> totalList = new ArrayList<>(mergeGoodsNoMap.size());
                for (SalesData salesData : goodsNoInfoList) {
                    // 获取大货款对应的合并款下面的所有大货款
                    List<SalesData> mergeGoodsNoBelowGoodsNoList = mergeGoodsNoMap.get(salesData.getMergeGoodsNo());
                    BigDecimal salesNum = BigDecimal.ZERO;
                    BigDecimal productionNum = BigDecimal.ZERO;

                    for (SalesData item : mergeGoodsNoBelowGoodsNoList) {
                        // 每个大货款的销量和投产信息
                        SalesData data = salesDataMap.get(item.getGoodsNo());
                        // 计算每个合并款号的 大货款号的销售量合计 和 投产量合计
                        if (ObjectUtil.isNotEmpty(data)) {
                            salesNum = salesNum.add(data.getSalesNum());
                            productionNum = productionNum.add(data.getProductionNum());
                        }
                    }

                    SalesData totalSalesData = new SalesData();
                    totalSalesData.setGoodsNo(salesData.getGoodsNo());
                    totalSalesData.setSalesNum(salesNum);
                    totalSalesData.setProductionNum(productionNum);
                    totalList.add(totalSalesData);
                }

                // 得到最终的 大货款号-销量（合并款号下的所有大货的销量）-投产（合并款号下的所有大货的投产）
                return totalList;
            }
        }
        return CollUtil.newArrayList();
    }

    /**
     * 坑位匹配
     *
     * @param list 坑位列表
     */
    @Override
    public void match(List<PlanningProjectPlankVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        PlanningProject planningProject = planningProjectService.getById(list.get(0).getPlanningProjectId());
        // 查询已经匹配的大货款号
        List<PlanningProjectPlank> projectPlanks = this.list(new QueryWrapper<PlanningProjectPlank>().select("bulk_style_no", "his_design_no").isNotNull("bulk_style_no").ne("bulk_style_no", "").or().isNotNull("his_design_no").ne("his_design_no", ""));
        List<String> bulkNos = projectPlanks.stream().map(PlanningProjectPlank::getBulkStyleNo).distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<String> getHisDesignNos = projectPlanks.stream().map(PlanningProjectPlank::getHisDesignNo).distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        bulkNos.addAll(getHisDesignNos);
        // 匹配实体
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            // //如果已经匹配上了,就不再匹配
            if (!"0".equals(planningProjectPlankVo.getMatchingStyleStatus()) && !"3".equals(planningProjectPlankVo.getMatchingStyleStatus())) {
                continue;
            }
            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配
            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProject.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectPlankVo.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectPlankVo.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectPlankVo.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectPlankVo.getProdCategoryCode());
            styleColorQueryWrapper.eq("tsc.band_code", planningProjectPlankVo.getBandCode());
            styleColorQueryWrapper.eq("tsc.order_flag", "1");
            if (!bulkNos.isEmpty()) {
                styleColorQueryWrapper.notIn("tsc.style_no", bulkNos);
            }

            List<StyleColorVo> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper, null);
            if (styleColorList == null || styleColorList.isEmpty()) {
                continue;
            }
            List<String> styleColorIds = styleColorList.stream().map(StyleColorVo::getId).collect(Collectors.toList());
            //  初步匹配上,再去筛选维度
            List<FieldVal> fieldVals = fieldValService.list(new QueryWrapper<FieldVal>().eq("data_group", FieldValDataGroupConstant.STYLE_COLOR).in("foreign_id", styleColorIds));
            Map<String, List<FieldVal>> map = fieldVals.stream().collect(Collectors.groupingBy(FieldVal::getForeignId));

            //  初步匹配上,再去筛选维度
            out:
            for (StyleColorVo styleColorVo : styleColorList) {
                // List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(styleColorVo.getId());
                List<FieldVal> fieldVals1 = map.get(styleColorVo.getId());
                if (fieldVals1 == null || fieldVals1.isEmpty()) {
                    continue;
                }
                for (FieldVal fieldVal : fieldVals1) {
                    if (fieldVal.getFieldId().equals(planningProjectPlankVo.getDimensionId()) && fieldVal.getVal().equals(planningProjectPlankVo.getDimensionValue())) {
                        // 说明匹配上了
                        planningProjectPlankVo.setBulkStyleNo(styleColorVo.getStyleNo());
                        planningProjectPlankVo.setMatchingStyleStatus("2");
                        planningProjectPlankVo.setPic(styleColorVo.getStyleColorPic());
                        // planningProjectPlankVo.setBandCode(styleColorVo.getBandCode());
                        // planningProjectPlankVo.setBandName(styleColorVo.getBandName());
                        planningProjectPlankVo.setStyleColorId(styleColorVo.getId());
                        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColorVo.getColorCode()));
                        if (colourLibrary != null) {
                            planningProjectPlankVo.setColorSystem(colourLibrary.getColorType());
                        }

                        PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                        BeanUtil.copyProperties(planningProjectPlankVo, planningProjectPlank);
                        this.updateById(planningProjectPlank);
                        bulkNos.add(planningProjectPlankVo.getBulkStyleNo());
                        planningProject.setIsMatch("1");
                        break out;
                    }
                }
            }
        }

        List<PlanningProjectPlankVo> list1 = new ArrayList<>();
        // 匹配虚拟
        JSONObject jsonObject = new JSONObject();
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {

            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配
            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProject.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectPlankVo.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectPlankVo.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectPlankVo.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectPlankVo.getProdCategoryCode());
            // styleColorQueryWrapper.eq("tsc.band_code", planningProjectPlankVo.getBandCode());
            styleColorQueryWrapper.eq("tsc.order_flag", "1");
            if (!bulkNos.isEmpty()) {
                styleColorQueryWrapper.notIn("tsc.style_no", bulkNos);
            }

            List<StyleColorVo> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper, null);
            if (styleColorList == null || styleColorList.isEmpty()) {
                continue;
            }
            List<String> styleColorIds = styleColorList.stream().map(StyleColorVo::getId).collect(Collectors.toList());
            //  初步匹配上,再去筛选维度
            List<FieldVal> fieldVals = fieldValService.list(new QueryWrapper<FieldVal>().eq("data_group", FieldValDataGroupConstant.STYLE_COLOR).in("foreign_id", styleColorIds));
            Map<String, List<FieldVal>> map = fieldVals.stream().collect(Collectors.groupingBy(FieldVal::getForeignId));


            for (StyleColorVo styleColorVo : styleColorList) {
                List<FieldVal> fieldVals1 = map.get(styleColorVo.getId());
                if (fieldVals1 == null || fieldVals1.isEmpty()) {
                    continue;
                }
                for (FieldVal FieldVal : fieldVals1) {
                    if (FieldVal.getFieldId().equals(planningProjectPlankVo.getDimensionId())) {
                        if (FieldVal.getVal() == null) {
                            continue;
                        }
                        if (FieldVal.getVal().equals(planningProjectPlankVo.getDimensionValue())) {
                            // 说明匹配上了
                            PlanningProjectPlankVo planningProjectPlankVo1 = new PlanningProjectPlankVo();
                            BeanUtil.copyProperties(planningProjectPlankVo, planningProjectPlankVo1);
                            planningProjectPlankVo1.setBulkStyleNo(styleColorVo.getStyleNo());
                            planningProjectPlankVo1.setMatchingStyleStatus("2");
                            planningProjectPlankVo1.setPic(styleColorVo.getStyleColorPic());
                            planningProjectPlankVo1.setBandCode(styleColorVo.getBandCode());
                            planningProjectPlankVo1.setBandName(styleColorVo.getBandName());
                            planningProjectPlankVo1.setStyleColorId(styleColorVo.getId());
                            BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColorVo.getColorCode()));
                            if (colourLibrary != null) {
                                planningProjectPlankVo1.setColorSystem(colourLibrary.getColorType());
                            }
                            planningProjectPlankVo1.setId(null);
                            planningProjectPlankVo1.setIsVirtual("1");
                            bulkNos.add(planningProjectPlankVo1.getBulkStyleNo());
                            list1.add(planningProjectPlankVo1);
                            if (jsonObject.get(planningProjectPlankVo1.getBandName() + "," + planningProjectPlankVo1.getDimensionValue()) == null) {
                                jsonObject.put(planningProjectPlankVo1.getBandName() + "," + planningProjectPlankVo1.getDimensionValue(), 1);
                            } else {
                                jsonObject.put(planningProjectPlankVo1.getBandName() + "," + planningProjectPlankVo1.getDimensionValue(), jsonObject.getInteger(planningProjectPlankVo1.getBandName() + "," + planningProjectPlankVo1.getDimensionValue()) + 1);
                            }

                            planningProject.setIsMatch("1");

                        }


                    }
                }
            }
        }

        // hashMap转为字符串
        Set<String> ids = list1.stream().map(PlanningProjectPlankVo::getPlanningProjectDimensionId).collect(Collectors.toSet());
        if (!ids.isEmpty()) {
            UpdateWrapper<PlanningProjectDimension> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            updateWrapper.set("virtual_number", jsonObject.toJSONString());
            planningProjectDimensionService.update(updateWrapper);
        }

        list.addAll(list1);
        planningProjectService.updateById(planningProject);
    }

    @Override
    public void unMatchByBulkStyleNo(String bulkStyleNo) {
        QueryWrapper<PlanningProjectPlank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bulk_style_no", bulkStyleNo);
        PlanningProjectPlank planningProjectPlank = this.getOne(queryWrapper);
        if (planningProjectPlank != null) {
            PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectPlank.getPlanningProjectDimensionId());

            planningProjectPlank.setBulkStyleNo("");
            planningProjectPlank.setStyleColorId("");
            planningProjectPlank.setPic("");
            planningProjectPlank.setBandName(planningProjectDimension.getBandName());
            planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
            planningProjectPlank.setMatchingStyleStatus("0");
            planningProjectPlank.setColorSystem("");
            this.updateById(planningProjectPlank);
        }
    }

    @Override
    public List<FieldDisplayVo> getDimensionFieldCard(DimensionLabelsSearchDto dto) {
        List<PlanningDimensionality> planningDimensionalities = planningDimensionalityService.getDimensionalityList(dto).getPlanningDimensionalities();
        // 过滤固定属性的字段
        Map<String, FieldManagement> fieldManagementMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(planningDimensionalities)) {
            List<String> fieldIdList = planningDimensionalities
                    .stream().map(PlanningDimensionality::getFieldId).collect(Collectors.toList());
            List<FieldManagement> fieldManagements = fieldManagementService.listByIds(fieldIdList);
            if (ObjectUtil.isNotEmpty(fieldManagements)) {
                fieldManagementMap = fieldManagements
                        .stream().collect(Collectors.toMap(FieldManagement::getId, item -> item));
            }
        }
        Map<String, FieldManagement> finalFieldManagementMap = fieldManagementMap;
        List<FieldDisplayVo> fieldDisplayVoList = planningDimensionalities.stream().map(planningDimensionality -> {
            FieldManagement fieldManagement = finalFieldManagementMap.get(planningDimensionality.getFieldId());
            if (ObjectUtil.isNotEmpty(fieldManagement) && GeneralConstant.FIXED_ATTRIBUTES.equals(fieldManagement.getGroupName())) {
                return null;
            }
            FieldDisplayVo fieldDisplayVo = new FieldDisplayVo();
            fieldDisplayVo.setName(planningDimensionality.getDimensionalityName());
            fieldDisplayVo.setField(planningDimensionality.getId());
            return fieldDisplayVo;
        }).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        List<FieldDisplayVo> list = new ArrayList<>();
        // 额外的字段
        if (StringUtils.isNotBlank(dto.getNames())) {
            for (String s : dto.getNames().split(",")) {
                FieldDisplayVo fieldDisplayVo = new FieldDisplayVo();
                fieldDisplayVo.setField(s);
                fieldDisplayVo.setName(s);
                list.add(fieldDisplayVo);
            }
        }
        list.addAll(fieldDisplayVoList);
        for (int i = 0; i < list.size(); i++) {
            FieldDisplayVo displayVo = list.get(i);
            // 设置是否显示
            String key = "planningProjectPlank:dimensionFieldCard:" + this.getUserId() + ":" + displayVo.getField();
            if (redisUtils.hasKey(key)) {
                displayVo.setDisplay("1".equals(redisUtils.get(key)));
            } else {
                displayVo.setDisplay(true);
            }

            // 设置排序
            String sort = "planningProjectPlank:dimensionFieldCard:sort:" + this.getUserId() + ":" + displayVo.getField();
            if (redisUtils.hasKey(sort)) {
                displayVo.setSort(String.valueOf(redisUtils.get(sort)));
            }
            if (StringUtils.isBlank(displayVo.getSort())) {
                displayVo.setSort(i + "");
            }
        }
        return list;

    }

    /**
     * 根据 id 删除
     */
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    @Override
    public void delById(PlanningProjectPlank plank) {
        PlanningProjectPlank planningProjectPlank = getById(plank.getId());
        if (ObjectUtil.isEmpty(planningProjectPlank)) {
            throw new OtherException("数据不存在，请刷新后重试！");
        }
        planningProjectPlank.setStyleCategory(plank.getStyleCategory());
        planningProjectPlank.setIsRelevancyUpdateSeasonalPlanning(plank.getIsRelevancyUpdateSeasonalPlanning());
        if (!removeById(planningProjectPlank)) {
            throw new OtherException("删除失败，请刷新后重试！");
        }
        // 变更坑位数量
        PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectPlank.getPlanningProjectDimensionId());
        if (ObjectUtil.isNotEmpty(planningProjectDimension)) {
            long pitPositionCount = count(
                    new LambdaQueryWrapper<PlanningProjectPlank>()
                            .eq(PlanningProjectPlank::getPlanningProjectDimensionId, planningProjectDimension.getId())
            );
            planningProjectDimension.setNumber(String.valueOf(pitPositionCount));
            if (!planningProjectDimensionService.updateById(planningProjectDimension)) {
                throw new OtherException("删除失败，请刷新后重试！");
            }
            updateCategoryAndSeasonPlanning(planningProjectPlank, planningProjectDimension, pitPositionCount, 2);
        }
    }

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    @Override
    public void saveData(@RequestBody PlanningProjectPlank planningProjectPlank) {
        if (StringUtils.isEmpty(planningProjectPlank.getId())) {
            save(planningProjectPlank);
            // 重新增加坑位数量
            String planningProjectDimensionId = planningProjectPlank.getPlanningProjectDimensionId();
            PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectDimensionId);
            if (ObjectUtil.isNotEmpty(planningProjectDimension)) {
                long pitPositionCount = count(
                        new LambdaQueryWrapper<PlanningProjectPlank>()
                                .eq(PlanningProjectPlank::getPlanningProjectDimensionId, planningProjectDimensionId)
                );
                planningProjectDimension.setNumber(String.valueOf(pitPositionCount));
                planningProjectDimensionService.updateById(planningProjectDimension);

                updateCategoryAndSeasonPlanning(planningProjectPlank, planningProjectDimension, pitPositionCount, 1);
            }
        } else {

            updateById(planningProjectPlank);
        }
    }

    /**
     * 同步修改品类企划和季节企划信息
     *
     * @param planningProjectPlank     企划看板坑位信息
     * @param planningProjectDimension 企划看板维度信息
     * @param pitPositionCount         修改后的数量
     * @param type                     类型 1-添加 2-删除
     */
    @Override
    public void updateCategoryAndSeasonPlanning(PlanningProjectPlank planningProjectPlank,
                                                PlanningProjectDimension planningProjectDimension,
                                                long pitPositionCount,
                                                Integer type
    ) {
        // 同步修改品类企划的需求数量
        CategoryPlanningDetails categoryPlanningDetails = categoryPlanningDetailsService.getById(planningProjectDimension.getCategoryPlanningDetailsId());
        if (ObjectUtil.isNotEmpty(categoryPlanningDetails)) {
            categoryPlanningDetails.setNumber(String.valueOf(pitPositionCount));
            categoryPlanningDetailsService.updateById(categoryPlanningDetails);

            if (planningProjectPlank.getIsRelevancyUpdateSeasonalPlanning().equals(0)
                    && type.equals(1)) {
                List<CategoryPlanningDetails> categoryPlanningDetailsList = categoryPlanningDetailsService.list(
                        new LambdaQueryWrapper<CategoryPlanningDetails>()
                                .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanningDetails.getCategoryPlanningId())
                                .eq(CategoryPlanningDetails::getProdCategory1stCode, planningProjectDimension.getProdCategory1stCode())
                                .eq(CategoryPlanningDetails::getProdCategoryCode, planningProjectDimension.getProdCategoryCode())
                                .eq(ObjectUtil.isNotEmpty(planningProjectDimension.getProdCategory2ndCode()), CategoryPlanningDetails::getProdCategory2ndCode, planningProjectDimension.getProdCategory2ndCode())
                                .eq(CategoryPlanningDetails::getDimensionId, planningProjectDimension.getDimensionId())
                                .eq(CategoryPlanningDetails::getBandCode, planningProjectPlank.getBandCode())
                );
                if (ObjectUtil.isEmpty(categoryPlanningDetailsList)) {
                    throw new OtherException("数据不存在，请刷新后重试！");
                }
                int num = 0;
                for (CategoryPlanningDetails details : categoryPlanningDetailsList) {
                    num += Integer.parseInt(details.getNumber());
                }
                if (num > Integer.parseInt(categoryPlanningDetailsList.get(0).getSkcCount())) {
                    throw new OtherException("坑位数量超出需求数量，请联动修改季节企划数据！");
                }
            }

            if (planningProjectPlank.getIsRelevancyUpdateSeasonalPlanning().equals(1)) {
                String styleCategory = planningProjectPlank.getStyleCategory();
                if (ObjectUtil.isEmpty(styleCategory)) {
                    String typeName = type.equals(1) ? "新增" : "删除";
                    throw new OtherException("联动季节企划时" + typeName + "坑位的时候必须选择相应的款式类别！");
                }

                // 同步修改季节企划的数量按照「大类-品类-中类-波段-款式类别」粒度去修改
                List<SeasonalPlanningDetails> seasonalPlanningDetailsList = seasonalPlanningDetailsService.list(
                        new LambdaQueryWrapper<SeasonalPlanningDetails>()
                                .eq(SeasonalPlanningDetails::getSeasonalPlanningId, categoryPlanningDetails.getSeasonalPlanningId())
                                .eq(SeasonalPlanningDetails::getProdCategory1stCode, planningProjectDimension.getProdCategory1stCode())
                                .eq(SeasonalPlanningDetails::getProdCategoryCode, planningProjectDimension.getProdCategoryCode())
                                .eq(ObjectUtil.isNotEmpty(planningProjectDimension.getProdCategory2ndCode()), SeasonalPlanningDetails::getProdCategory2ndCode, planningProjectDimension.getProdCategory2ndCode())
                                .eq(SeasonalPlanningDetails::getBandCode, planningProjectDimension.getBandCode())
                                .eq(SeasonalPlanningDetails::getStyleCategory, styleCategory)
                );
                if (ObjectUtil.isEmpty(seasonalPlanningDetailsList)) {
                    throw new OtherException("季节企划不存在当前「" + styleCategory + "」的数据");
                }

                for (SeasonalPlanningDetails details : seasonalPlanningDetailsList) {
                    Integer skcCount = type.equals(1) ? Integer.parseInt(details.getSkcCount()) + 1 : Integer.parseInt(details.getSkcCount()) - 1;
                    details.setSkcCount(String.valueOf(skcCount));
                }
                seasonalPlanningDetailsService.updateBatchById(seasonalPlanningDetailsList);

                // 同步修改品类企划的合计数量按照「大类-品类-中类」粒度去修改
                List<CategoryPlanningDetails> categoryPlanningDetailsList = categoryPlanningDetailsService.list(
                        new LambdaQueryWrapper<CategoryPlanningDetails>()
                                .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanningDetails.getCategoryPlanningId())
                                .eq(CategoryPlanningDetails::getProdCategory1stCode, planningProjectDimension.getProdCategory1stCode())
                                .eq(CategoryPlanningDetails::getProdCategoryCode, planningProjectDimension.getProdCategoryCode())
                                .eq(ObjectUtil.isNotEmpty(planningProjectDimension.getProdCategory2ndCode()), CategoryPlanningDetails::getProdCategory2ndCode, planningProjectDimension.getProdCategory2ndCode())
                );
                if (ObjectUtil.isNotEmpty(categoryPlanningDetailsList)) {
                    for (CategoryPlanningDetails details : categoryPlanningDetailsList) {
                        Integer total = type.equals(1) ? Integer.parseInt(details.getTotal()) + 1 : Integer.parseInt(details.getTotal()) - 1;
                        Integer skcCount = type.equals(1) ? Integer.parseInt(details.getSkcCount()) + 1 : Integer.parseInt(details.getSkcCount()) - 1;
                        details.setTotal(String.valueOf(total));
                        details.setSkcCount(String.valueOf(skcCount));
                    }
                    categoryPlanningDetailsService.updateBatchById(categoryPlanningDetailsList);
                }
            }
        }
    }
}
