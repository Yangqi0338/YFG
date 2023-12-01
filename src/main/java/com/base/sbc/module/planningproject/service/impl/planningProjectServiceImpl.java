package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.BasePageInfo;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
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
import com.base.sbc.module.style.vo.StyleColorVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.converters.FileConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.module.formtype.utils.FormTypeCodes.DIMENSION_LABELS;

@Service
@RequiredArgsConstructor
public class planningProjectServiceImpl extends BaseServiceImpl<PlanningProjectMapper, PlanningProject> implements PlanningProjectService {
    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectMaxCategoryService planningProjectMaxCategoryService;
    private final PlanningProjectPlankService planningProjectPlankService;
    private final DataPermissionsService dataPermissionsService;
    private final MinioUtils minioUtils;
    private final PlanningChannelService planningChannelService;


    /**
     * 分页查询企划看板规划信息
     * @param dto 查询条件
     * @return 返回的结果
     */
    @Override
    public PageInfo<PlanningProjectVo> queryPage(PlanningProjectPageDTO dto) {
        /*分页*/
        PageHelper.startPage(dto);
        BaseQueryWrapper<PlanningProject> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("season_id",dto.getSeasonId());
        queryWrapper.notEmptyEq("planning_channel_code",dto.getPlanningChannelCode());
        queryWrapper.notEmptyLike("season_name",dto.getYear());
        queryWrapper.notEmptyLike("planning_project_name",dto.getPlanningProjectName());


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
        if (StringUtils.isNotBlank(planningProjectSaveDTO.getId())) {
            PlanningProject planningProject = this.getById(planningProjectSaveDTO.getId());
            if ("1".equals(planningProject.getIsMatch())){
                throw new RuntimeException("已匹配不允许修改");
            }
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
        //如果是修改,先删除之前的坑位
        if (StringUtils.isNotBlank(planningProjectSaveDTO.getId())) {
            QueryWrapper<PlanningProjectPlank> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("planning_project_id", planningProjectSaveDTO.getId());
            planningProjectPlankService.physicalDeleteQWrap(queryWrapper2);
        }
        //生成坑位
        for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
            List<PlanningProjectPlank> planningProjectPlanks = new ArrayList<>();
            for (int i = 0; i <  Integer.parseInt(planningProjectDimension.getNumber()); i++) {
                PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                planningProjectPlank.setPlanningProjectId(planningProjectSaveDTO.getId());
                planningProjectPlank.setMatchingStyleStatus("0");
                planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
                planningProjectPlank.setBandName(planningProjectDimension.getBandName());
                planningProjectPlank.setPlanningProjectDimensionId(planningProjectDimension.getId());
                planningProjectPlanks.add(planningProjectPlank);
            }
            planningProjectPlankService.saveBatch(planningProjectPlanks);
        }
        return true;
    }

    @Override
    public BasePageInfo<PlanningSeasonOverviewVo> historyList(PlanningProjectPageDTO dto) {
        Map<String,Object> map =new HashMap<>();
        //查询企划规划看板中每个品类或者或者开启中类的剩余未匹配的坑位数量
        QueryWrapper<PlanningProjectDimension> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("planning_project_id",dto.getPlanningProjectId());
        List<PlanningProjectDimension> list = planningProjectDimensionService.list(queryWrapper);

        Set<String> category1stCodes = new HashSet<>();
        Set<String> categoryCodes = new HashSet<>();
        //查询剩余还能匹配坑位的数量
        for (PlanningProjectDimension planningProjectDimension : list) {
            category1stCodes.add(planningProjectDimension.getProdCategory1stCode());
            categoryCodes.add(planningProjectDimension.getProdCategoryCode());

            QueryWrapper<PlanningProjectPlank> queryWrapper2 =new BaseQueryWrapper<>();
            queryWrapper2.eq("planning_project_dimension_id",planningProjectDimension.getId());
            queryWrapper2.eq("matching_style_status","0");
            long count = planningProjectPlankService.count(queryWrapper2);
            String key;
            if ("1".equals(planningProjectDimension.getIsProdCategory2nd())){
                key=planningProjectDimension.getProdCategory1stCode()+","+planningProjectDimension.getProdCategoryCode()+","+planningProjectDimension.getProdCategory2ndCode();
            }else {
                key=planningProjectDimension.getProdCategory1stCode()+","+planningProjectDimension.getProdCategoryCode();
            }

            if (!Objects.isNull(map.get(key))) {
                map.put(key, Long.parseLong(map.get(key).toString()) + count);
            }else {
                map.put(key,count);
            }
        }



        //查询已经匹配的大货款号
        List<PlanningProjectPlank> projectPlanks = planningProjectPlankService.list(
                new QueryWrapper<PlanningProjectPlank>().select("bulk_style_no","his_design_no").isNotNull("bulk_style_no").
                        ne("bulk_style_no","").or().isNotNull("his_design_no").ne("his_design_no",""));
        List<String>  bulkNos = projectPlanks.stream().map(PlanningProjectPlank::getBulkStyleNo).distinct().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> hisDesignNos = projectPlanks.stream().map(PlanningProjectPlank::getHisDesignNo).distinct().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        bulkNos.addAll(hisDesignNos);
        List<PlanningChannel> planningChannels = planningChannelService.list(new QueryWrapper<PlanningChannel>().eq("channel", dto.getPlanningChannelCode()).eq("planning_season_id", dto.getSeasonId()));
        if (planningChannels.isEmpty()){
            throw new RuntimeException("该渠道不存在");
        }
        PlanningChannel planningChannel = planningChannels.get(0);

        QueryWrapper queryWrapper2 = new BaseQueryWrapper<>();

        //中类筛选,有问题
        // List<String> category2ndCodes =new ArrayList<>();
        // for (PlanningProjectDimension planningProjectDimension : list) {
        //     if("1".equals(planningProjectDimension.getIsProdCategory2nd())){
        //         category2ndCodes.add(planningProjectDimension.getProdCategory2ndCode());
        //     }
        // }
        // if(!category2ndCodes.isEmpty()){
        //     queryWrapper2.or();
        //     queryWrapper2.in("c.prod_category2nd",category2ndCodes);
        // }




        queryWrapper2.eq("c.planning_channel_id",planningChannel.getId());
        if (!bulkNos.isEmpty()) {
            queryWrapper2.notIn("c.his_design_no", bulkNos);
        }
        // queryWrapper2.eq("c.planning_season_id",dto.getSeasonId());
        queryWrapper2.in("c.prod_category1st",category1stCodes);
        queryWrapper2.ne("c.his_design_no","");
        queryWrapper2.isNotNull("c.his_design_no");
        queryWrapper2.in("c.prod_category",categoryCodes);

        PageHelper.startPage(dto);
        dataPermissionsService.getDataPermissionsForQw(queryWrapper2, DataPermissionsBusinessTypeEnum.PlanningCategoryItem.getK(), "c.");
        List<PlanningSeasonOverviewVo> planningSeasonOverviewVos = this.baseMapper.historyList(queryWrapper2);
        minioUtils.setObjectUrlToList(planningSeasonOverviewVos, "planningPic");
        BasePageInfo<PlanningSeasonOverviewVo> pageInfo = new BasePageInfo<>(planningSeasonOverviewVos);



        pageInfo.setMap(map);
        return  pageInfo;
    }
}
