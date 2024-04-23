package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.Option;
import com.base.sbc.module.formtype.mapper.FieldManagementMapper;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningMapper;
import com.base.sbc.module.planningproject.service.CategoryPlanningDetailsService;
import com.base.sbc.module.planningproject.service.CategoryPlanningService;
import com.base.sbc.module.planningproject.service.SeasonalPlanningDetailsService;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-19 17:19:27
 * @mail 247967116@qq.com
 */
@Service
public class CategoryPlanningServiceImpl extends BaseServiceImpl<CategoryPlanningMapper, CategoryPlanning> implements CategoryPlanningService {

    @Autowired
    @Lazy
    private SeasonalPlanningService seasonalPlanningService;
    @Autowired
    private SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    @Autowired
    @Lazy
    private CategoryPlanningDetailsService categoryPlanningDetailsService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private FieldOptionConfigService fieldOptionConfigService;
    @Autowired
    private FieldManagementService fieldManagementService;
    @Autowired
    private FieldManagementMapper fieldManagementMapper;
    @Autowired
    private CcmFeignService ccmFeignService;


    /**
     * @param categoryPlanningQueryDto
     * @return
     */
    @Override
    public List<CategoryPlanningVo> queryList(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        BaseQueryWrapper<CategoryPlanning> queryWrapper = this.buildQueryWrapper(categoryPlanningQueryDto);
        return baseMapper.listByQueryWrapper(queryWrapper);
    }

    @Override
    public List<CategoryPlanningVo> queryPage(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        PageHelper.startPage(categoryPlanningQueryDto);
        return this.queryList(categoryPlanningQueryDto);
    }

    /**
     * @param baseDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void generateCategoryPlanningNew(BaseDto baseDto) {
        // 生成品类企划
        SeasonalPlanning seasonalPlanning = seasonalPlanningService.getById(baseDto.getId());
        String status = seasonalPlanning.getStatus();
        if ("1".equals(status)) {
            throw new RuntimeException("已停用的季节企划不能生成品类企划");
        }
        // 设置为已生成过品类企划
        seasonalPlanning.setIsGenerate("1");
        seasonalPlanningService.updateById(seasonalPlanning);

        // 判断是否已经存在已启用品类企划数据
        QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        queryWrapper.eq("season_id", seasonalPlanning.getSeasonId());
        queryWrapper.eq("channel_code", seasonalPlanning.getChannelCode());
        queryWrapper.eq("company_code", userUtils.getCompanyCode());
        long l = count(queryWrapper);

        // 存在就生成不启用的数据 不存在就生成启用的数据
        CategoryPlanning categoryPlanning = new CategoryPlanning();
        categoryPlanning.setName(seasonalPlanning.getName());
        categoryPlanning.setChannelCode(seasonalPlanning.getChannelCode());
        categoryPlanning.setChannelName(seasonalPlanning.getChannelName());
        categoryPlanning.setSeasonId(seasonalPlanning.getSeasonId());
        categoryPlanning.setSeasonName(seasonalPlanning.getSeasonName());
        categoryPlanning.setSeasonalPlanningId(seasonalPlanning.getId());
        categoryPlanning.setStatus(l > 0 ? "1" : "0");
        save(categoryPlanning);

        // 查询季节企划的详情数据
        List<SeasonalPlanningDetails> detailsList = seasonalPlanningDetailsService
                .listByField("seasonal_planning_id", seasonalPlanning.getId());
        List<CategoryPlanningDetails> categoryPlanningDetailsList = generationCategoryPlanningDetails(detailsList, seasonalPlanning, categoryPlanning);
        boolean saveFlag = categoryPlanningDetailsService.saveBatch(categoryPlanningDetailsList);
        if (!saveFlag) {
            throw new OtherException("生成品类企划失败，请刷新后重试！");
        }
    }

    /**
     * 根据季节企划详情数据进行品类企划详情的组装生成
     * @param detailsList 季节企划详情集合数据
     * @param seasonalPlanning 季节企划数据
     * @param categoryPlanning 品类企划数据
     * @return 品类企划详情集合数据
     */
    public List<CategoryPlanningDetails> generationCategoryPlanningDetails(
            List<SeasonalPlanningDetails> detailsList
            , SeasonalPlanning seasonalPlanning
            , CategoryPlanning categoryPlanning) {
        // 初始化返回的数据
        List<CategoryPlanningDetails> categoryPlanningDetailsList = new ArrayList<>();
        // 初始化当前品类企划中所有品类的企划需求管理数据
        Map<String, List<PlanningDimensionality>> planningDimensionalityMap = new HashMap<>();
        // 初始化表单字段的数据 也就是企划需求管理的每个动态字段以及动态字段的内容的数据
        Map<String, FieldManagementVo> fieldManagementMap = new HashMap<>();
        // 初始化字典数据
        Map<String, Map<String, String>> dictMap = new HashMap<>();
        // 初始化结构管理数据
        HashMap<String, List<BasicStructureTreeVo>> structureMap = new HashMap<>();
        {
            // 获取所有维度等级的数据
            List<PlanningDimensionality> planningDimensionalityList = getPlanningDimensionalitieList(detailsList, seasonalPlanning);
            // 判断维度数据中是否有中类 有的话查询
            // 按照品类给维度等级分类
            planningDimensionalityMap = planningDimensionalityList.stream().collect(Collectors.groupingBy(PlanningDimensionality::getProdCategory));

            // 根据维度数据中的字段管理的 id 查询每个维度字段的信息
            List<String> fieldIdList = planningDimensionalityList
                    .stream().map(PlanningDimensionality::getFieldId).distinct().collect(Collectors.toList());
            QueryFieldManagementDto queryFieldManagementDto = new QueryFieldManagementDto();
            queryFieldManagementDto.setCompanyCode(userUtils.getCompanyCode());
            queryFieldManagementDto.setIds(fieldIdList);
            List<FieldManagementVo> fieldManagementList
                    = fieldManagementMapper.getFieldManagementList(queryFieldManagementDto);
            if (ObjectUtil.isNotEmpty(fieldManagementList)) {
                fieldManagementMap = fieldManagementList.stream().collect(Collectors.toMap(FieldManagementVo::getId, item -> item));
            }

            // 将字段管理中是字典和结构管理的数据全部查询出来 后面直接使用不需要循环查询数据库
            if (ObjectUtil.isNotEmpty(fieldManagementList)) {
                dictMap = getFeildData(fieldManagementList, dictMap, structureMap);
            }
        }

        if (ObjectUtil.isNotEmpty(detailsList)) {
            // 生成品类企划明细 空数据
            // 按照 品类-中类-波段分组 品类企划颗粒度到波段 不到款式类别
            Map<String, List<SeasonalPlanningDetails>> seasonalPlanningDetailsMap = detailsList.stream().collect(
                    Collectors.groupingBy(item -> item.getProdCategoryCode() + "-" + item.getProdCategory2ndCode() + "-" + item.getBandCode(), LinkedHashMap::new, Collectors.toList())
            );

            // 将季节企划详情集合数据按照品类-中类分组 用作合并相同品类-中类下的sck数据

            Map<String, List<SeasonalPlanningDetails>> totalSeasonalPlanningDetailsMap = detailsList.stream().collect(
                    Collectors.groupingBy(item -> item.getProdCategoryCode() + "-" + item.getProdCategory2ndCode(), LinkedHashMap::new, Collectors.toList())
            );

            for (Map.Entry<String, List<SeasonalPlanningDetails>> listEntry : seasonalPlanningDetailsMap.entrySet()) {
                // 获取该分组下的季节企划详情集合
                List<SeasonalPlanningDetails> seasonalPlanningDetailsList = listEntry.getValue();
                // 取出第一个
                SeasonalPlanningDetails details = seasonalPlanningDetailsList.get(0);
                // 开始循环维度数据
                List<PlanningDimensionality> planningDimensionalities = planningDimensionalityMap.get(seasonalPlanningDetailsList.get(0).getProdCategoryCode());
                if (ObjectUtil.isNotEmpty(planningDimensionalities)) {
                    for (PlanningDimensionality planningDimensionality : planningDimensionalities) {
                        // 拿到一个维度字段数据
                        FieldManagementVo fieldManagement = fieldManagementMap.get(planningDimensionality.getFieldId());
                        if (ObjectUtil.isNotEmpty(fieldManagement)) {

                            // 每个 品类-中类-波段 都要有自己的围度数据
                            // 获取选项类型 0-自定义选项 1-字典 2-结构管理)
                            String isOption = fieldManagement.getIsOption();
                            if ("1".equals(isOption)) {
                                // 从字典获取
                                Map<String, String> stringStringMap = dictMap.get(fieldManagement.getOptionDictKey());
                                if (ObjectUtil.isNotEmpty(stringStringMap)) {
                                    for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                                        initEmptyData(
                                                planningDimensionality,
                                                stringStringEntry.getValue(),
                                                stringStringEntry.getKey(),
                                                details,
                                                categoryPlanning,
                                                fieldManagement, seasonalPlanningDetailsList,
                                                categoryPlanningDetailsList,
                                                totalSeasonalPlanningDetailsMap);
                                    }
                                }
                            } else if ("2".equals(isOption)) {
                                // 从数据结构获取
                                List<BasicStructureTreeVo> basicStructureTreeVoList = structureMap.get(fieldManagement.getOptionDictKey() + "-" + fieldManagement.getStructureTier());
                                if (ObjectUtil.isNotEmpty(basicStructureTreeVoList)) {
                                    if ("固定属性".equals(fieldManagement.getGroupName()) && "品类".equals(fieldManagement.getOptionDictKey())) {
                                        // 找到对应层级的品类数据
                                        basicStructureTreeVoList = findProdCategoryList(basicStructureTreeVoList, details, fieldManagement);
                                        if (ObjectUtil.isNotEmpty(basicStructureTreeVoList)) {
                                            for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVoList) {
                                                initEmptyData(
                                                        planningDimensionality,
                                                        basicStructureTreeVo.getName(),
                                                        basicStructureTreeVo.getValue(),
                                                        details,
                                                        categoryPlanning,
                                                        fieldManagement, seasonalPlanningDetailsList,
                                                        categoryPlanningDetailsList,
                                                        totalSeasonalPlanningDetailsMap);
                                            }
                                        }
                                    } else {
                                        for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVoList) {
                                            initEmptyData(
                                                    planningDimensionality,
                                                    basicStructureTreeVo.getName(),
                                                    basicStructureTreeVo.getValue(),
                                                    details,
                                                    categoryPlanning,
                                                    fieldManagement, seasonalPlanningDetailsList,
                                                    categoryPlanningDetailsList,
                                                    totalSeasonalPlanningDetailsMap);
                                        }
                                    }
                                }
                            } else {
                                // 自定义选项
                                // 拿到所有自定义的值
                                List<Option> optionList = fieldManagement.getOptionList();
                                if (ObjectUtil.isNotEmpty(optionList)) {
                                    for (Option option : optionList) {
                                        initEmptyData(
                                                planningDimensionality,
                                                option.getOptionName(),
                                                option.getOptionCode(),
                                                details,
                                                categoryPlanning,
                                                fieldManagement, seasonalPlanningDetailsList,
                                                categoryPlanningDetailsList,
                                                totalSeasonalPlanningDetailsMap);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return categoryPlanningDetailsList;
    }

    /**
     * 获取所有等级的唯独数据
     *
     * @param detailsList      季节企划数据详情
     * @param seasonalPlanning 季节企划数据
     * @return 维度数据
     */
    private List<PlanningDimensionality> getPlanningDimensionalitieList(List<SeasonalPlanningDetails> detailsList, SeasonalPlanning seasonalPlanning) {
        // 查询第一维度数据 如果任意一个品类下没有第一维度直接报错
        List<String> prodCategoryCodeList = detailsList
                .stream().map(SeasonalPlanningDetails::getProdCategoryCode).distinct().collect(Collectors.toList());

        QueryWrapper<PlanningDimensionality> planningDimensionalityQueryWrapper = new QueryWrapper<>();
        planningDimensionalityQueryWrapper.eq("channel", seasonalPlanning.getChannelCode());
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonalPlanning.getSeasonId());
        planningDimensionalityQueryWrapper.in("prod_category", prodCategoryCodeList);
        planningDimensionalityQueryWrapper.eq("dimensionality_grade", "1");

        long count = planningDimensionalityService.count(planningDimensionalityQueryWrapper);
        if (count != prodCategoryCodeList.size()) {
            // 如果查询出来第一维度数据为空 或者 有品类存在没有第一维度的数据的情况就报错
            throw new OtherException("品类级别的第一维度数据不能为空！");
        }

        // 当品类都有第一维度数据后 查询维度表维度数据维度等级不为空的数据
        planningDimensionalityQueryWrapper.clear();
        planningDimensionalityQueryWrapper.eq("channel", seasonalPlanning.getChannelCode());
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonalPlanning.getSeasonId());
        planningDimensionalityQueryWrapper.in("prod_category", prodCategoryCodeList);
        planningDimensionalityQueryWrapper.isNotNull("dimensionality_grade");
        planningDimensionalityQueryWrapper.ne("dimensionality_grade", "");
        // 拿到所有等级的维度数据
        List<PlanningDimensionality> planningDimensionalityList = planningDimensionalityService.list(planningDimensionalityQueryWrapper);
        return planningDimensionalityList;
    }

    private Map<String, Map<String, String>> getFeildData(List<FieldManagementVo> fieldManagementList, Map<String, Map<String, String>> dictMap, HashMap<String, List<BasicStructureTreeVo>> structureMap) {
        // 所有的字典 key
        List<String> dictKeyList = fieldManagementList.stream()
                .filter(item -> "1".equals(item.getIsOption()))
                .map(FieldManagementVo::getOptionDictKey)
                .distinct()
                .collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(dictKeyList)) {
            dictMap = ccmFeignService.getDictInfoToMap(CollUtil.join(dictKeyList, ","));
        }
        // 所有的结构管理的 key
        List<FieldManagementVo> structureKeyList = fieldManagementList.stream()
                .filter(item -> "2".equals(item.getIsOption()))
                .distinct()
                .collect(Collectors.toList());

        if (ObjectUtil.isNotEmpty(structureKeyList)) {
            for (FieldManagementVo fieldManagementVo : structureKeyList) {
                String key = fieldManagementVo.getOptionDictKey() + "-" + fieldManagementVo.getStructureTier();
                if (ObjectUtil.isNotEmpty(structureMap.get(key))) {
                    continue;
                }
                // 将需要层级的所有数据查询出来
                List<BasicStructureTreeVo> structureTreeByCodes = ccmFeignService.basicStructureTreeByCode(fieldManagementVo.getOptionDictKey(), null, fieldManagementVo.getStructureTier());

                if (ObjectUtil.isNotEmpty(structureTreeByCodes)) {
                    structureMap.put(key, structureTreeByCodes);
                }
            }
        }
        return dictMap;
    }

    /**
     * 在品类层级中找到对应层级的 大||品||中
     *
     * @param basicStructureTreeVoList 品类层级数据
     * @param details                  季节企划的一条数据
     * @param fieldManagement          维度数据的一条数据
     */
    private List<BasicStructureTreeVo> findProdCategoryList(
            List<BasicStructureTreeVo> basicStructureTreeVoList,
            SeasonalPlanningDetails details,
            FieldManagementVo fieldManagement) {
        if ("0".equals(fieldManagement.getStructureTier())) {
            // 大类
            basicStructureTreeVoList = basicStructureTreeVoList
                    .stream()
                    .filter(item -> item.getValue().equals(details.getProdCategory1stCode()))
                    .collect(Collectors.toList());
        } else if ("1".equals(fieldManagement.getStructureTier())) {
            // 品类
            basicStructureTreeVoList = basicStructureTreeVoList
                    .stream()
                    .filter(item -> item.getValue().equals(details.getProdCategoryCode()))
                    .collect(Collectors.toList());
        } else if ("2".equals(fieldManagement.getStructureTier())) {
            // 中类
            basicStructureTreeVoList = basicStructureTreeVoList
                    .stream()
                    .filter(item -> item.getValue().equals(details.getProdCategory2ndCode()))
                    .collect(Collectors.toList());
        }
        return basicStructureTreeVoList;
    }

    /**
     * 初始化空的品类企划数据
     */
    private void initEmptyData(PlanningDimensionality planningDimensionality,
                               String dimensionCode,
                               String dimensionValue,
                               SeasonalPlanningDetails details,
                               CategoryPlanning categoryPlanning,
                               FieldManagementVo fieldManagement,
                               List<SeasonalPlanningDetails> inSeasonalPlanningDetailsList,
                               List<CategoryPlanningDetails> categoryPlanningDetailList,
                               Map<String, List<SeasonalPlanningDetails>> totalSeasonalPlanningDetailsMap) {
        // 根据品类维度的维度信息来
        // 初始化空的品类企划数据
        Integer skcCount = 0;
        CategoryPlanningDetails categoryPlanningDetails = new CategoryPlanningDetails();
        categoryPlanningDetails.setSeasonalPlanningName(details.getSeasonalPlanningName());
        categoryPlanningDetails.setSeasonalPlanningId(details.getSeasonalPlanningId());
        categoryPlanningDetails.setCategoryPlanningName(categoryPlanning.getName());
        categoryPlanningDetails.setCategoryPlanningId(categoryPlanning.getId());
        categoryPlanningDetails.setProdCategory1stCode(details.getProdCategory1stCode());
        categoryPlanningDetails.setProdCategory1stName(details.getProdCategory1stName());
        categoryPlanningDetails.setProdCategoryCode(details.getProdCategoryCode());
        categoryPlanningDetails.setProdCategoryName(details.getProdCategoryName());
        categoryPlanningDetails.setProdCategory2ndCode(details.getProdCategory2ndCode());
        categoryPlanningDetails.setProdCategory2ndName(details.getProdCategory2ndName());
        categoryPlanningDetails.setDimensionTypeCode(fieldManagement.getFormTypeId());
        categoryPlanningDetails.setDimensionId(planningDimensionality.getFieldId());
        categoryPlanningDetails.setDimensionName(planningDimensionality.getDimensionalityName());
        categoryPlanningDetails.setDimensionCode(dimensionCode);
        categoryPlanningDetails.setDimensionValue(dimensionValue);
        categoryPlanningDetails.setDimensionalityGrade(planningDimensionality.getDimensionalityGrade());
        categoryPlanningDetails.setDimensionalityGradeName(planningDimensionality.getDimensionalityGradeName());
        categoryPlanningDetails.setBandCode(details.getBandCode());
        categoryPlanningDetails.setBandName(details.getBandName());
        // 计算不同款式类别相加起来的需求数
        for (SeasonalPlanningDetails seasonalPlanningDetails : inSeasonalPlanningDetailsList) {
            skcCount += ObjectUtil.isEmpty(seasonalPlanningDetails.getSkcCount())
                    ? 0 : Integer.parseInt(seasonalPlanningDetails.getSkcCount());
        }
        categoryPlanningDetails.setSkcCount(String.valueOf(skcCount));
        categoryPlanningDetails.setStyleCategory(
                inSeasonalPlanningDetailsList
                        .stream()
                        .map(SeasonalPlanningDetails::getStyleCategory)
                        .collect(Collectors.joining(",")))
        ;
        categoryPlanningDetails.setOrderTime(details.getOrderTime());
        categoryPlanningDetails.setLaunchTime(details.getLaunchTime());

        // 合并品类-中类下的 skc 数量
        Integer total = 0;
        List<SeasonalPlanningDetails> planningDetailsList
                = totalSeasonalPlanningDetailsMap.get(details.getProdCategoryCode() + "-" + details.getProdCategory2ndCode());
        for (SeasonalPlanningDetails seasonalPlanningDetails : planningDetailsList) {
            String skc = seasonalPlanningDetails.getSkcCount();
            total += (ObjectUtil.isEmpty(skc) ? 0 : Integer.parseInt(skc));
        }
        categoryPlanningDetails.setTotal(String.valueOf(total));
        categoryPlanningDetailList.add(categoryPlanningDetails);
    }

    private void getFieldData(FieldManagementVo fieldManagementVo) {
        // 初始化选项值
        List<Option> optionList = new ArrayList<>();
        // 获取选项类型 0-自定义选项 1-字典 2-结构管理)
        String isOption = fieldManagementVo.getIsOption();
        if ("1".equals(isOption)) {
            // 从字典获取
            String dimensionTypeCode = fieldManagementVo.getOptionDictKey();
            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap(dimensionTypeCode);

            for (String s : dictInfoToMap.keySet()) {
                Map<String, String> map = dictInfoToMap.get(s);
                for (String string : map.keySet()) {
                    Option option = new Option();
                    option.setFieldId(fieldManagementVo.getId());
                    option.setOptionName(string);
                    option.setOptionCode(map.get(string));
                    optionList.add(option);
                }
            }

        }
        if ("2".equals(isOption)) {
            String optionDictKey = fieldManagementVo.getOptionDictKey();
            List<BasicStructureTreeVo> basicStructureTreeVoList = ccmFeignService.basicStructureTreeByCode(optionDictKey, "1", fieldManagementVo.getStructureTier());
            for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVoList) {
                Option option = new Option();
                option.setOptionName(basicStructureTreeVo.getName());
                option.setOptionCode(basicStructureTreeVo.getValue());
                option.setFieldId(fieldManagementVo.getId());
                optionList.add(option);
            }
        }
        fieldManagementVo.setOptionList(optionList);
    }

    /**
     * 构造查询条件
     */
    private BaseQueryWrapper<CategoryPlanning> buildQueryWrapper(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        BaseQueryWrapper<CategoryPlanning> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("channel_code", categoryPlanningQueryDto.getChannelCode());
        queryWrapper.notEmptyEq("season_id", categoryPlanningQueryDto.getSeasonId());
        queryWrapper.notEmptyLike("name", categoryPlanningQueryDto.getYearName());
        queryWrapper.orderByDesc("id");
        return queryWrapper;
    }
}
