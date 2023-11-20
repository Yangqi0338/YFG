package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.mapper.PlanningProjectMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectMaxCategoryService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.converters.FileConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.module.formtype.utils.FormTypeCodes.DIMENSION_LABELS;

@Service
@RequiredArgsConstructor
public class planningProjectServiceImpl extends BaseServiceImpl<PlanningProjectMapper, PlanningProject> implements PlanningProjectService {
    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectMaxCategoryService planningProjectMaxCategoryService;
    private final PlanningProjectPlankService planningProjectPlankService;
    private final StyleService styleService;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    private final StyleColorService styleColorService;
    private final FieldValService fieldValService;
    private final StylePricingMapper stylePricingMapper;
    private final FieldManagementService fieldManagementService;

    /**
     * 分页查询企划看板规划信息
     * @param dto 查询条件
     * @return 返回的结果
     */
    @Override
    public PageInfo<PlanningProjectVo> queryPage(PlanningProjectPageDTO dto) {
        /*分页*/
        PageHelper.startPage(dto);
        // List<PlanningProjectVo> planningProjectVos = this.getBaseMapper().getplanningProjectList(dto);
        BaseQueryWrapper<PlanningProject> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("season_id",dto.getSeasonId());
        queryWrapper.notEmptyEq("planning_channel_code",dto.getPlanningChannelCode());



        List<PlanningProject> list = this.list(queryWrapper);
        List<PlanningProjectVo> planningProjectVos = BeanUtil.copyToList(list, PlanningProjectVo.class);
        for (PlanningProjectVo planningProjectVo : planningProjectVos) {
            List<PlanningProjectDimension> planningProjectDimensions = planningProjectDimensionService.list(new QueryWrapper<PlanningProjectDimension>().eq("planning_project_id", planningProjectVo.getId()));
            planningProjectVo.setPlanningProjectDimensionList(planningProjectDimensions);
            List<PlanningProjectMaxCategory> planningProjectMaxCategories = planningProjectMaxCategoryService.list(new QueryWrapper<PlanningProjectMaxCategory>().eq("planning_project_id", planningProjectVo.getId()));
            planningProjectVo.setPlanningProjectMaxCategoryList(planningProjectMaxCategories);
        }
        return new PageInfo<>(planningProjectVos);
    }

    /**
     * 保存企划看板规划信息
     *
     * @param planningProjectSaveDTO 实体对象
     * @return 返回的结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(PlanningProjectSaveDTO planningProjectSaveDTO) {
        //判断新增或者修改的时候是否存在相同的季度和渠道
        QueryWrapper<PlanningProject> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("season_id",planningProjectSaveDTO.getSeasonId());
        queryWrapper.eq("planning_channel_code",planningProjectSaveDTO.getPlanningChannelCode());
        queryWrapper.ne(StringUtils.isNotBlank(planningProjectSaveDTO.getId()),"id",planningProjectSaveDTO.getId());

        long count = this.count(queryWrapper);
        if (count>0){
            throw new RuntimeException("该季度已经存在该渠道的企划规划");
        }
        //已匹配不允许修改
        if ("1".equals(planningProjectSaveDTO.getIsMatch())){
            throw new RuntimeException("已匹配不允许修改");
        }


       this.saveOrUpdate(planningProjectSaveDTO);
        //更新对应关联的数据
        List<PlanningProjectDimension> planningProjectDimensionList = planningProjectSaveDTO.getPlanningProjectDimensionList();
        for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
            planningProjectDimension.setPlanningProjectId(planningProjectSaveDTO.getId());
        }
        planningProjectDimensionService.remove(new QueryWrapper<PlanningProjectDimension>().eq("planning_project_id",planningProjectSaveDTO.getId()));
        planningProjectDimensionService.saveBatch(planningProjectSaveDTO.getPlanningProjectDimensionList());

        List<PlanningProjectMaxCategory> planningProjectMaxCategoryList = planningProjectSaveDTO.getPlanningProjectMaxCategoryList();
        for (PlanningProjectMaxCategory planningProjectMaxCategory : planningProjectMaxCategoryList) {
            planningProjectMaxCategory.setPlanningProjectId(planningProjectSaveDTO.getId());
        }
        planningProjectMaxCategoryService.remove(new QueryWrapper<PlanningProjectMaxCategory>().eq("planning_project_id",planningProjectSaveDTO.getId()));
        planningProjectMaxCategoryService.saveBatch(planningProjectSaveDTO.getPlanningProjectMaxCategoryList());

        for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
            // 坑位数量
            String number = planningProjectDimension.getNumber();
            int i = Integer.parseInt(number);
            if (i == 0) {
                continue;
            }
            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配

            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProjectSaveDTO.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectDimension.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectDimension.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectDimension.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectDimension.getProdCategoryCode());
            styleColorQueryWrapper.eq("ts.band_code", planningProjectDimension.getBandCode());

            // 查询坑位所有已经匹配的大货款号
            QueryWrapper<PlanningProjectPlank> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("bulk_style_no");
            queryWrapper1.isNotNull("bulk_style_no");
            queryWrapper1.last("and bulk_style_no != ''");
            List<PlanningProjectPlank> list = planningProjectPlankService.list(queryWrapper1);
            if (!list.isEmpty()) {
                List<String> bulkStyleNoList = list.stream().map(PlanningProjectPlank::getBulkStyleNo).collect(Collectors.toList());
                styleColorQueryWrapper.notIn("style_no", bulkStyleNoList);
            }
            List<StyleColor> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper);
            // 查询款式设计所有的维度信息

            Iterator<StyleColor> iterator = styleColorList.iterator();

            // 匹配到的坑位信息
            List<PlanningProjectPlank> planningProjectPlanks = new ArrayList<>();

            // 匹配
            while (iterator.hasNext()) {
                StyleColor next = iterator.next();
                List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(next.getId());
                for (FieldManagementVo fieldManagementVo : fieldManagementVos) {
                    if (fieldManagementVo.getFieldName().equals(planningProjectDimension.getDimensionCode()) && fieldManagementVo.getVal().equals(planningProjectDimension.getDimensionValue())) {
                        // 说明匹配上了
                        PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                        planningProjectPlank.setBulkStyleNo(next.getStyleNo());
                        planningProjectPlank.setPlanningProjectId(planningProjectSaveDTO.getId());
                        planningProjectPlank.setMatchingStyleStatus("2");
                        planningProjectPlank.setPic(next.getStyleColorPic());
                        planningProjectPlank.setBandCode(next.getBandCode());
                        planningProjectPlank.setBandName(next.getBandName());
                        planningProjectPlank.setStyleColorId(next.getId());
                        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", next.getColorCode()));
                        if (colourLibrary != null) {
                            planningProjectPlank.setColorSystem(colourLibrary.getColorType());
                        }
                        planningProjectPlanks.add(planningProjectPlank);
                    }
                }
            }
            int i1 = i - planningProjectPlanks.size();
            if (i1 > 0) {
                // 说明匹配不够
                for (int j = 0; j < i1; j++) {
                    PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                    planningProjectPlank.setPlanningProjectId(planningProjectSaveDTO.getId());
                    planningProjectPlank.setMatchingStyleStatus("0");
                    planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
                    planningProjectPlank.setBandName(planningProjectDimension.getBandName());
                    planningProjectPlanks.add(planningProjectPlank);
                }
            }

            planningProjectPlankService.saveBatch(planningProjectPlanks);
        }
        return false;
    }
}
