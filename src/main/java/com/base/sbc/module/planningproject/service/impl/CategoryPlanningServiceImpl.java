package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-19 17:19:27
 * @mail 247967116@qq.com
 */
@Service
public class CategoryPlanningServiceImpl extends BaseServiceImpl<CategoryPlanningMapper, CategoryPlanning> implements CategoryPlanningService {

    @Autowired
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
        // 初始化当前品类企划中所有品类的企划需求管理数据
        Map<String, List<PlanningDimensionality>> planningDimensionalityMap = new HashMap<>();
        // 初始化表单字段的数据 也就是企划需求管理的每个动态字段以及动态字段的内容的数据
        Map<String, FieldManagementVo> fieldManagementMap = new HashMap<>();
        {
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
        }

        if (ObjectUtil.isNotEmpty(detailsList)) {
            // 生成品类企划明细 空数据
            // 按照 品类-中类-波段分组 品类企划颗粒度到波段 不到款式类别

            // 先分组到品类 这一步是为了根据品类去查询维度数据信息
            Map<String, List<SeasonalPlanningDetails>> seasonalPlanningDetailsMap = detailsList.stream().collect(
                    Collectors.groupingBy(SeasonalPlanningDetails::getProdCategoryName)
            );
            // 初始化新增的品类企划
            List<CategoryPlanningDetails> categoryPlanningDetailList = new ArrayList<>();
            for (Map.Entry<String, List<SeasonalPlanningDetails>> stringListEntry : seasonalPlanningDetailsMap.entrySet()) {
                List<SeasonalPlanningDetails> seasonalPlanningDetailsList = stringListEntry.getValue();

                // 开始循环维度数据
                List<PlanningDimensionality> planningDimensionalities = planningDimensionalityMap.get(seasonalPlanningDetailsList.get(0).getProdCategoryCode());
                if (ObjectUtil.isNotEmpty(planningDimensionalities)) {
                    for (PlanningDimensionality planningDimensionality : planningDimensionalities) {
                        // 拿到一个维度字段数据
                        FieldManagementVo fieldManagement = fieldManagementMap.get(planningDimensionality.getFieldId());
                        if (ObjectUtil.isNotEmpty(fieldManagement)) {
                            // 选项（0:自定义选项，1字典,2结构管理)
                            if (!"0".equals(fieldManagement.getIsOption())) {
                                // 如果不是自定义配置 那么就从字典 或者 结构中获取数据 （optionList 存的就是自定义配置，所以自定义配置不用查询）
                                // TODO：此处现在是循环查询数据库 后期可优化 ——XHTE
                                getFieldData(fieldManagement);
                            }
                            // 拿到所有的值
                            List<Option> optionList = fieldManagement.getOptionList();

                            // 再分组到波段
                            Map<String, List<SeasonalPlanningDetails>> inMap = seasonalPlanningDetailsList.stream().collect(
                                    Collectors.groupingBy(item -> item.getProdCategoryName() + item.getProdCategory2ndName() + item.getBandName())
                            );
                            for (Map.Entry<String, List<SeasonalPlanningDetails>> listEntry : inMap.entrySet()) {
                                // 获取该分组下的季节企划详情集合
                                List<SeasonalPlanningDetails> inSeasonalPlanningDetailsList = listEntry.getValue();
                                // 取出第一个
                                SeasonalPlanningDetails details = inSeasonalPlanningDetailsList.get(0);
                                // 每个 品类-中类-波段 都要有自己的围度数据
                                for (Option option : optionList) {
                                    initEmptyData(
                                            planningDimensionality,
                                            option,
                                            details,
                                            categoryPlanning,
                                            fieldManagement, inSeasonalPlanningDetailsList,
                                            categoryPlanningDetailList);
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(JSONUtil.toJsonStr(categoryPlanningDetailList.stream().filter(item -> item.getProdCategory2ndName().equals("小外套") && item.getDimensionName().equals("围度大类")).collect(Collectors.toList())));
            categoryPlanningDetailsService.saveBatch(categoryPlanningDetailList);
        }

    }

    /**
     * 初始化空的品类企划数据
     */
    private void initEmptyData(PlanningDimensionality planningDimensionality,
                               Option option,
                               SeasonalPlanningDetails details,
                               CategoryPlanning categoryPlanning,
                               FieldManagementVo fieldManagement,
                               List<SeasonalPlanningDetails> inSeasonalPlanningDetailsList,
                               List<CategoryPlanningDetails> categoryPlanningDetailList) {
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
        categoryPlanningDetails.setDimensionCode(option.getOptionName());
        categoryPlanningDetails.setDimensionValue(option.getOptionCode());
        categoryPlanningDetails.setBandCode(details.getBandCode());
        categoryPlanningDetails.setBandName(details.getBandName());
        // 计算不同款式类别相加起来的需求数
        for (SeasonalPlanningDetails seasonalPlanningDetails : inSeasonalPlanningDetailsList) {
            skcCount += Integer.parseInt(seasonalPlanningDetails.getSkcCount());
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
            List<BasicStructureTreeVo> basicStructureTreeVoList = ccmFeignService.basicStructureTreeByCode(optionDictKey, null, fieldManagementVo.getStructureTier());
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
