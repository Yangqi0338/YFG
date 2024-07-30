package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.Option;
import com.base.sbc.module.formtype.mapper.FieldManagementMapper;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.constants.GeneralConstant;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.*;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningMapper;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
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

    @Lazy
    @Autowired
    private PlanningProjectService planningProjectService;
    @Autowired
    private DataPermissionsService dataPermissionsService;


    /**
     * @param categoryPlanningQueryDto
     * @return
     */
    @Override
    public List<CategoryPlanningVo> queryList(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        BaseQueryWrapper<CategoryPlanning> queryWrapper = this.buildQueryWrapper(categoryPlanningQueryDto);
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.categoryPlanning.getK());
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
        categoryPlanning.setStatus(l > 0 || "1".equals(seasonalPlanning.getStatus()) ? "1" : "0");
        save(categoryPlanning);

        // 查询季节企划的详情数据
        List<SeasonalPlanningDetails> detailsList = seasonalPlanningDetailsService
                .listByField("seasonal_planning_id", seasonalPlanning.getId());
        List<CategoryPlanningDetails> categoryPlanningDetailsList
                = generationCategoryPlanningDetails(detailsList, seasonalPlanning, categoryPlanning, null);
        if (ObjectUtil.isNotEmpty(categoryPlanningDetailsList)) {
            boolean saveFlag = categoryPlanningDetailsService.saveBatch(categoryPlanningDetailsList);
            if (!saveFlag) {
                throw new OtherException("生成品类企划失败，请刷新后重试！");
            }
        } else {
            throw new OtherException("生成品类企划失败，请刷新后重试！");
        }
    }


    /**
     * 根据季节企划详情数据进行品类企划详情的组装生成
     *
     * @param detailsList      季节企划详情集合数据
     * @param seasonalPlanning 季节企划数据
     * @param categoryPlanning 品类企划数据
     * @return 品类企划详情集合数据
     */
    public List<CategoryPlanningDetails> generationCategoryPlanningDetails(
            List<SeasonalPlanningDetails> detailsList
            , SeasonalPlanning seasonalPlanning
            , CategoryPlanning categoryPlanning
            , List<Map<String, String>> queryList) {
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
            // 将季节企划详情数据转换成品类下面是中类集合的格式 用作查询维度数据信息
            Map<String, List<SeasonalPlanningDetails>> seasonalPlanningDetailMap
                    = detailsList.stream().collect(Collectors.groupingBy(SeasonalPlanningDetails::getProdCategoryName));
            // 初始化返回格式
            Map<String, List<String>> resultMap = new HashMap<>(16);
            for (Map.Entry<String, List<SeasonalPlanningDetails>> stringListEntry : seasonalPlanningDetailMap.entrySet()) {
                // 拿到品类分组下的季节企划数据
                List<SeasonalPlanningDetails> seasonalPlanningDetailsList = stringListEntry.getValue();
                // 初始化中类名称集合
                List<String> list = new ArrayList<>(seasonalPlanningDetailsList.size());
                for (SeasonalPlanningDetails details : seasonalPlanningDetailsList) {
                    if (ObjectUtil.isNotEmpty(details.getProdCategory2ndName())) {
                        // 如果品类下面的中类的名称不是空的 那么放到集合里面
                        list.add(details.getProdCategory2ndName());
                    }
                }
                if (ObjectUtil.isNotEmpty(list)) {
                    list = list.stream().distinct().collect(Collectors.toList());
                }
                resultMap.put(stringListEntry.getKey(), list);
            }
            List<PlanningDimensionality> planningDimensionalityList = getAllPlanningDimensionalitieList(resultMap, seasonalPlanning.getChannelCode(), seasonalPlanning.getSeasonId());
            if (ObjectUtil.isNotEmpty(queryList)) {
                planningDimensionalityList = planningDimensionalityList.stream()
                        .filter(item -> {
                            for (Map<String, String> stringStringMap : queryList) {
                                if (ObjectUtil.isNotEmpty(stringStringMap.get("prodCategory2nd"))) {
                                    if (stringStringMap.get("prodCategory2nd").equals(item.getProdCategory2nd()) && stringStringMap.get("dimensionId").equals(item.getFieldId())) {
                                        return true;
                                    }
                                } else {
                                    if (stringStringMap.get("dimensionId").equals(item.getFieldId())) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }).collect(Collectors.toList());
            }
            if (ObjectUtil.isEmpty(planningDimensionalityList)) {
                return new ArrayList<>();
            }
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
                    = fieldManagementService.getFieldManagementListMapper(queryFieldManagementDto);
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
            // 按照 品类-中类-波段分组 品类企划颗粒度到波段 不到款式类别 -> 2024/07/29 改成到款式类别
            Map<String, List<SeasonalPlanningDetails>> seasonalPlanningDetailsMap = detailsList.stream().collect(
                    Collectors.groupingBy(item -> item.getProdCategoryCode() + "-" + item.getProdCategory2ndCode() + "-" + item.getBandCode() + "-" + item.getStyleCategory(), LinkedHashMap::new, Collectors.toList())
            );

            // 将季节企划详情集合数据按照品类-中类分组 用作合并相同品类-中类下的skc数据

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
                        String prodCategory2nd = planningDimensionality.getProdCategory2nd();
                        // 判断这个维度数据是中类级别 还是品类级别
                        if (ObjectUtil.isNotEmpty(prodCategory2nd)) {
                            // 中类信息不为空 是中类级别 不属于当前中类直接 pass
                            if (!prodCategory2nd.equals(details.getProdCategory2ndCode())) {
                                continue;
                            }
                        }
                        // 拿到一个维度字段数据
                        FieldManagementVo fieldManagement = fieldManagementMap.get(planningDimensionality.getFieldId());
                        if (ObjectUtil.isNotEmpty(fieldManagement)) {
                            // 每个 品类-中类-波段 都要有自己的围度数据
                            if (GeneralConstant.FIXED_ATTRIBUTES.equals(fieldManagement.getGroupName())) {
                                // 固定属性赋值
                                if (GeneralConstant.PRODCATEGORY_2ND_CODE.equals(fieldManagement.getFieldExplain())) {
                                    // 固定属性-中类
                                    // 从数据结构获取
                                    List<BasicStructureTreeVo> basicStructureTree1VoList = structureMap.get(GeneralConstant.PRODCATEGORY_CODE + "-" + "1");
                                    List<BasicStructureTreeVo> basicStructureTree2VoList = structureMap.get(GeneralConstant.PRODCATEGORY_CODE + "-" + "2");
                                    boolean isMediumClass = false;
                                    if (!ObjectUtil.isEmpty(details.getProdCategory2ndCode())) {
                                        if (ObjectUtil.isNotEmpty(basicStructureTree2VoList)) {
                                            // 中类不为空的情况就是取当前中类
                                            basicStructureTree2VoList = basicStructureTree2VoList
                                                    .stream()
                                                    .filter(item -> item.getValue().equals(details.getProdCategory2ndCode()))
                                                    .collect(Collectors.toList());
                                            isMediumClass = true;
                                        }
                                    } else {
                                        if (ObjectUtil.isNotEmpty(basicStructureTree1VoList) || ObjectUtil.isNotEmpty(basicStructureTree2VoList)) {
                                            // 中类为空 取当前品类下的所有中类
                                            basicStructureTree1VoList = basicStructureTree1VoList
                                                    .stream()
                                                    .filter(item -> item.getValue().equals(details.getProdCategoryCode()))
                                                    .collect(Collectors.toList());
                                            if (ObjectUtil.isNotEmpty(basicStructureTree1VoList)) {
                                                List<BasicStructureTreeVo> finalBasicStructureTree1VoList = basicStructureTree1VoList;
                                                basicStructureTree2VoList = basicStructureTree2VoList
                                                        .stream()
                                                        .filter(item -> item.getParentId().equals(finalBasicStructureTree1VoList.get(0).getId()))
                                                        .collect(Collectors.toList());
                                            }
                                        }

                                    }
                                    if (ObjectUtil.isNotEmpty(basicStructureTree2VoList)) {
                                        for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTree2VoList) {
                                            initEmptyData(
                                                    planningDimensionality,
                                                    basicStructureTreeVo.getName(),
                                                    basicStructureTreeVo.getValue(),
                                                    details,
                                                    isMediumClass,
                                                    categoryPlanning,
                                                    fieldManagement, seasonalPlanningDetailsList,
                                                    categoryPlanningDetailsList,
                                                    totalSeasonalPlanningDetailsMap);
                                        }
                                    }
                                }
                            } else {
                                // 动态属性赋值
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
                                                    false,
                                                    categoryPlanning,
                                                    fieldManagement, seasonalPlanningDetailsList,
                                                    categoryPlanningDetailsList,
                                                    totalSeasonalPlanningDetailsMap);
                                        }
                                    }
                                } else if ("2".equals(isOption)) {
                                    // 从数据结构获取
                                    List<BasicStructureTreeVo> basicStructureTreeVoList = structureMap.get(fieldManagement.getOptionDictKey() + "-" + fieldManagement.getStructureTier());
                                    for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVoList) {
                                        initEmptyData(
                                                planningDimensionality,
                                                basicStructureTreeVo.getName(),
                                                basicStructureTreeVo.getValue(),
                                                details,
                                                false,
                                                categoryPlanning,
                                                fieldManagement, seasonalPlanningDetailsList,
                                                categoryPlanningDetailsList,
                                                totalSeasonalPlanningDetailsMap);
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
                                                    false,
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
        }
        return categoryPlanningDetailsList;
    }

    /**
     * 获取所有等级的维度数据
     *
     * @param prodCategoryNameList 品类名称集合
     * @param channelCode          渠道 code
     * @param seasonId             产品季 id
     * @return
     */
    @Override
    public List<PlanningDimensionality> getPlanningDimensionalitieList(List<String> prodCategoryNameList, String channelCode, String seasonId) {
        // 查询第一维度数据 如果任意一个品类下没有第一维度直接报错
        QueryWrapper<PlanningDimensionality> planningDimensionalityQueryWrapper = new QueryWrapper<>();
        planningDimensionalityQueryWrapper.eq("channel", channelCode);
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonId);
        planningDimensionalityQueryWrapper.in("prod_category_name", prodCategoryNameList);
        planningDimensionalityQueryWrapper.eq("dimensionality_grade", "1");
        planningDimensionalityQueryWrapper.and(item -> item.eq("prod_category2nd", "").or().isNull("prod_category2nd"));

        List<PlanningDimensionality> list = planningDimensionalityService.list(planningDimensionalityQueryWrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            if (prodCategoryNameList.size() != list.size()) {
                List<String> stringList = list.stream().map(PlanningDimensionality::getProdCategoryName).collect(Collectors.toList());
                List<String> newProdCategoryNameList = prodCategoryNameList.stream()
                        .filter(item -> !stringList.contains(item)).collect(Collectors.toList());
                throw new OtherException("品类「" + Arrays.toString(newProdCategoryNameList.toArray()) + "」第一维度数据不能为空！");
            }
        } else {
            throw new OtherException("品类「" + Arrays.toString(prodCategoryNameList.toArray()) + "」第一维度数据不能为空！");
        }

        // 当品类都有第一维度数据后 查询维度表维度数据维度等级不为空的数据
        planningDimensionalityQueryWrapper.clear();
        planningDimensionalityQueryWrapper.eq("channel", channelCode);
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonId);
        planningDimensionalityQueryWrapper.in("prod_category_name", prodCategoryNameList);
        planningDimensionalityQueryWrapper.isNotNull("dimensionality_grade");
        planningDimensionalityQueryWrapper.ne("dimensionality_grade", "");
        planningDimensionalityQueryWrapper.and(item -> item.eq("prod_category2nd", "").or().isNull("prod_category2nd"));

        // 拿到所有等级的维度数据
        List<PlanningDimensionality> planningDimensionalityList = planningDimensionalityService.list(planningDimensionalityQueryWrapper);
        return planningDimensionalityList;
    }

    /**
     * 获取所有等级的维度数据，如果品类没有就使用中类，如果中类也没有 那么抛出异常
     *
     * @param resultMap   品类和中类名称 Map 集合
     * @param channelCode 渠道 code
     * @param seasonId    产品季 id
     * @return
     */
    @Override
    public List<PlanningDimensionality> getAllPlanningDimensionalitieList(Map<String, List<String>> resultMap, String channelCode, String seasonId) {
        // 初始化中类或者品类对应的维度信息
        List<PlanningDimensionality> resultList = new ArrayList<>();
        // 品类名称集合
        List<String> initProdCategoryNameList = new ArrayList<>(resultMap.keySet());
        // 先查询品类的第一维度数据
        QueryWrapper<PlanningDimensionality> planningDimensionalityQueryWrapper = new QueryWrapper<>();
        planningDimensionalityQueryWrapper.eq("channel", channelCode);
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonId);
        planningDimensionalityQueryWrapper.in("prod_category_name", initProdCategoryNameList);
        planningDimensionalityQueryWrapper.eq("dimensionality_grade", "1");
        planningDimensionalityQueryWrapper.and(item -> item.eq("prod_category2nd", "").or().isNull("prod_category2nd"));
        List<PlanningDimensionality> prodCategorylist = planningDimensionalityService.list(planningDimensionalityQueryWrapper);

        // 找出品类没有第一维度的数据
        // 初始化需要查询维度信息的品类
        List<String> prodCategoryNameList = new ArrayList<>();
        // 初始化需要查询第一维度数据的中类
        List<String> prodCategory2ndNameList = new ArrayList<>();
        // 初始化没有查询到品类级别的第一维度数据的品类 且品类下面没有中类
        List<String> prodCategoryNo2ndNameList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(prodCategorylist)) {
            List<String> alreadyExistsList = prodCategorylist
                    .stream().map(PlanningDimensionality::getProdCategoryName).collect(Collectors.toList());
            for (Map.Entry<String, List<String>> item : resultMap.entrySet()) {
                if (!alreadyExistsList.contains(item.getKey())) {
                    if (ObjectUtil.isEmpty(item.getValue())) {
                        // 如果中类集合为空 代表导入的季节企划数据当前品类没有中类 那么这个品类必须有第一维度配置 而不是去查询下面的中类的第一维度配置
                        prodCategoryNo2ndNameList.add(item.getKey());
                    } else {
                        // 如果没有查询到品类级别的第一维度 那么就取出需要查询第一维度的中类级别的数据
                        prodCategory2ndNameList.addAll(item.getValue());
                    }
                } else {
                    // 需要查询其他维度的品类名称
                    prodCategoryNameList.add(item.getKey());
                }
            }
        } else {
            for (Map.Entry<String, List<String>> item : resultMap.entrySet()) {
                // 品类级别的第一维度都没查询到 全都要去查中类级别的第一维度
                prodCategory2ndNameList.addAll(item.getValue());
            }
        }

        if (ObjectUtil.isNotEmpty(prodCategoryNo2ndNameList)) {
            // 此时需要提示配置品类级别的第一维度
            throw new OtherException("由于季节企划没有导入以下品类「" + CollUtil.join(prodCategoryNo2ndNameList, ",") + "」下的中类数据，所以必须配置第一维度数据！");
        }

        if (ObjectUtil.isNotEmpty(prodCategory2ndNameList)) {
            // 说明品类级别的第一维度不存在 需要查询中类级别的第一维度
            planningDimensionalityQueryWrapper.clear();
            planningDimensionalityQueryWrapper.eq("channel", channelCode);
            planningDimensionalityQueryWrapper.eq("planning_season_id", seasonId);
            planningDimensionalityQueryWrapper.in("prod_category2nd_name", prodCategory2ndNameList);
            planningDimensionalityQueryWrapper.eq("dimensionality_grade", "1");
            List<PlanningDimensionality> prodCategory2ndlist = planningDimensionalityService.list(planningDimensionalityQueryWrapper);
            if (ObjectUtil.isNotEmpty(prodCategory2ndlist)) {
                if (prodCategory2ndNameList.size() != prodCategory2ndlist.size()) {
                    List<String> stringList = prodCategory2ndlist.stream().map(PlanningDimensionality::getProdCategory2ndName).collect(Collectors.toList());
                    List<String> newProdCategory2ndNameList = prodCategory2ndNameList.stream()
                            .filter(item -> !stringList.contains(item)).collect(Collectors.toList());
                    throw new OtherException("请配置中类为「" + CollUtil.join(newProdCategory2ndNameList, ",") + "」或者中类上级品类的第一维度数据！");
                }
            } else {
                throw new OtherException("请配置中类为「" + CollUtil.join(prodCategory2ndNameList, ",") + "」的第一维度数据！");
            }

            // 如果中类（刨除品类已配置第一维度）都有第一维度 查询中类的所有等级的维度数据
            planningDimensionalityQueryWrapper.clear();
            planningDimensionalityQueryWrapper.eq("channel", channelCode);
            planningDimensionalityQueryWrapper.eq("planning_season_id", seasonId);
            planningDimensionalityQueryWrapper.in("prod_category2nd_name", prodCategory2ndNameList);
            planningDimensionalityQueryWrapper.isNotNull("dimensionality_grade");
            planningDimensionalityQueryWrapper.ne("dimensionality_grade", "");
            // 拿到所有等级的维度数据
            resultList.addAll(planningDimensionalityService.list(planningDimensionalityQueryWrapper));
        }

        if (ObjectUtil.isNotEmpty(prodCategoryNameList)) {
            // 如果品类（刨除中类已配置第一维度）都有第一维度 查询品类的所有等级的维度数据
            planningDimensionalityQueryWrapper.clear();
            planningDimensionalityQueryWrapper.eq("channel", channelCode);
            planningDimensionalityQueryWrapper.eq("planning_season_id", seasonId);
            planningDimensionalityQueryWrapper.in("prod_category_name", prodCategoryNameList);
            planningDimensionalityQueryWrapper.isNotNull("dimensionality_grade");
            planningDimensionalityQueryWrapper.ne("dimensionality_grade", "");
            planningDimensionalityQueryWrapper.and(item -> item.eq("prod_category2nd", "").or().isNull("prod_category2nd"));
            // 拿到所有等级的维度数据
            resultList.addAll(planningDimensionalityService.list(planningDimensionalityQueryWrapper));
        }

        return resultList;
    }

    private Map<String, Map<String, String>> getFeildData(List<FieldManagementVo> fieldManagementList, Map<String, Map<String, String>> dictMap, HashMap<String, List<BasicStructureTreeVo>> structureMap) {
        // 所有的字典 key
        List<String> dictKeyList = fieldManagementList.stream()
                .filter(item -> "1".equals(item.getIsOption()) && !GeneralConstant.FIXED_ATTRIBUTES.equals(item.getGroupName()))
                .map(FieldManagementVo::getOptionDictKey)
                .distinct()
                .collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(dictKeyList)) {
            dictMap = ccmFeignService.getDictInfoToMap(CollUtil.join(dictKeyList, ","));
        }
        // 所有的结构管理的 key
        List<FieldManagementVo> structureKeyList = fieldManagementList.stream()
                .filter(item -> "2".equals(item.getIsOption()) && !GeneralConstant.FIXED_ATTRIBUTES.equals(item.getGroupName()))
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

        // 所有的固定属性的 key
        List<FieldManagementVo> fixedAttributesKeyList = fieldManagementList.stream()
                .filter(item -> GeneralConstant.FIXED_ATTRIBUTES.equals(item.getGroupName()))
                .distinct()
                .collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(fixedAttributesKeyList)) {
            {
                // 查询品类数据 用作没有中类时 查询品类下的数据
                List<BasicStructureTreeVo> structureTreeByCodes = ccmFeignService.basicStructureTreeByCode(GeneralConstant.PRODCATEGORY_CODE, null, "1");
                if (ObjectUtil.isNotEmpty(structureTreeByCodes)) {
                    structureMap.put(GeneralConstant.PRODCATEGORY_CODE + "-" + "1", structureTreeByCodes);
                }
            }
            for (FieldManagementVo fieldManagementVo : fixedAttributesKeyList) {
                if (GeneralConstant.PRODCATEGORY_2ND_CODE.equals(fieldManagementVo.getFieldExplain())) {
                    // 固定属性是中类
                    String key = GeneralConstant.PRODCATEGORY_CODE + "-" + "2";
                    if (ObjectUtil.isNotEmpty(structureMap.get(key))) {
                        continue;
                    }
                    // 将需要层级的所有数据查询出来
                    List<BasicStructureTreeVo> structureTreeByCodes = ccmFeignService.basicStructureTreeByCode(GeneralConstant.PRODCATEGORY_CODE, null, "2");

                    if (ObjectUtil.isNotEmpty(structureTreeByCodes)) {
                        structureMap.put(key, structureTreeByCodes);
                    }
                } else {
                    // 其他固定属性先不管
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
                               Boolean isMediumClass,
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
        String prodCategory2nd = planningDimensionality.getProdCategory2nd();
        // 判断这个维度数据是中类级别 还是品类级别
        if (ObjectUtil.isEmpty(prodCategory2nd)) {
            // 空的 就是品类级别
            categoryPlanningDetails.setDimensionalityType(1);
        } else {
            // 不是空的就是中类级别
            categoryPlanningDetails.setDimensionalityType(2);
        }
        categoryPlanningDetails.setBandCode(details.getBandCode());
        categoryPlanningDetails.setBandName(details.getBandName());
        categoryPlanningDetails.setStyleCategoryCode(details.getStyleCategoryCode());
        categoryPlanningDetails.setStyleCategory(details.getStyleCategory());
        // 计算不同款式类别相加起来的需求数
        // for (SeasonalPlanningDetails seasonalPlanningDetails : inSeasonalPlanningDetailsList) {
        //     skcCount += ObjectUtil.isEmpty(seasonalPlanningDetails.getSkcCount())
        //             ? 0 : Integer.parseInt(seasonalPlanningDetails.getSkcCount());
        // }
        skcCount += ObjectUtil.isEmpty(details.getSkcCount())
                ? 0 : Integer.parseInt(details.getSkcCount());
        categoryPlanningDetails.setSkcCount(String.valueOf(skcCount));
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
        if (isMediumClass) {
            categoryPlanningDetails.setNumber(String.valueOf(skcCount));
        }
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
        queryWrapper.notEmptyEq("tcp.channel_code", categoryPlanningQueryDto.getChannelCode());
        queryWrapper.notEmptyEq("tcp.season_id", categoryPlanningQueryDto.getSeasonId());
        queryWrapper.notEmptyLike("tcp.name", categoryPlanningQueryDto.getYearName());
        queryWrapper.orderByDesc("tcp.id");
        return queryWrapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void updateStatus(@RequestBody BaseDto baseDto) {
        String ids = baseDto.getIds();
        List<String> idList = Arrays.asList(ids.split(","));
        if ("0".equals(baseDto.getStatus())) {
            List<CategoryPlanning> categoryPlannings = listByIds(idList);
            // 判断季节企划是否启用 没启用不允许启用
            if (ObjectUtil.isEmpty(categoryPlannings)) {
                throw new OtherException("品类企划不存在，请刷新后重试！");
            }

            List<String> seasonIds = categoryPlannings.stream().map(CategoryPlanning::getSeasonId).collect(Collectors.toList());
            List<String> channelCodes = categoryPlannings.stream().map(CategoryPlanning::getChannelCode).collect(Collectors.toList());

            QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("season_id", seasonIds);
            queryWrapper.in("channel_code", channelCodes);
            queryWrapper.notIn("id", idList);
            queryWrapper.eq("status", "0");
            long l = count(queryWrapper);
            if (l > 0) {
                throw new RuntimeException("已存在启用的品类企划");
            }

            List<String> seasonalPlanningIdList = categoryPlannings
                    .stream().map(CategoryPlanning::getSeasonalPlanningId).distinct().collect(Collectors.toList());
            List<SeasonalPlanning> seasonalPlanningList = seasonalPlanningService.listByIds(seasonalPlanningIdList);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            seasonalPlanningList.forEach(item -> {
                if ("1".equals(item.getStatus())) {
                    throw new OtherException("请先启用季节企划「" + item.getSeasonName() + "●企划看板（" + item.getChannelName() + "）" + simpleDateFormat.format(item.getCreateDate()) + "」！");
                }
            });
        }
        UpdateWrapper<CategoryPlanning> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", baseDto.getStatus());
        updateWrapper.in("id", Arrays.asList(ids.split(",")));
        boolean updateFlag = update(updateWrapper);
        if (!updateFlag) {
            throw new OtherException("品类企划启用/停用失败，请刷新后重试！");
        } else {
            LambdaQueryWrapper<PlanningProject> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(PlanningProject::getCategoryPlanningId, idList);
            queryWrapper.orderByDesc(PlanningProject::getCreateDate);
            // 查询是否存在企划看板的数据 如果存在则 修改
            if ("0".equals(baseDto.getStatus())) {
                // 如果是启用 只启用最新创建时间的品类企划数据
                queryWrapper.last("limit 1");
            }
            List<PlanningProject> list = planningProjectService.list(queryWrapper);
            if (ObjectUtil.isNotEmpty(list)) {
                List<String> planningProjectList
                        = list.stream().map(PlanningProject::getId).collect(Collectors.toList());
                planningProjectService.startStop(CollUtil.join(planningProjectList, ","), baseDto.getStatus());
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void delByIds(RemoveDto removeDto) {
        String ids = removeDto.getIds();
        List<String> list = Arrays.asList(ids.split(","));
        QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", list);
        queryWrapper.eq("status", "0");
        long l = count(queryWrapper);
        if (l > 0) {
            throw new RuntimeException("存在启用的季节企划,不能删除");
        }
        // 查询是否存在企划看板的数据 如果存在则不允许删除
        List<PlanningProject> planningProjectList = planningProjectService.list(
                new LambdaQueryWrapper<PlanningProject>()
                        .in(PlanningProject::getCategoryPlanningId, list)
        );
        if (ObjectUtil.isNotEmpty(planningProjectList)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, List<PlanningProject>> map = planningProjectList.stream()
                    .collect(Collectors.groupingBy(item ->
                            item.getSeasonName() + "●企划看板（" + item.getPlanningChannelName() + "）" + simpleDateFormat.format(item.getCreateDate())));
            throw new OtherException("请先删除已生成的企划看板「" + CollUtil.join(map.keySet(), ",") + "」");
        }
        boolean removeFlag = removeByIds(list);
        if (!removeFlag) {
            throw new OtherException("品类企划删除失败，请刷新后重试！");
        }
    }
}
