package com.base.sbc.module.planningproject.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.base.sbc.module.formtype.entity.FormType;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.formtype.service.FormTypeService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.FieldDisplayVo;
import com.base.sbc.module.planningproject.dto.MatchSaveDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.dto.UnMatchDto;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleColorVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:58
 * @mail 247967116@qq.com
 * 企划看板坑位
 */
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/planningProjectPlank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class PlanningProjectPlankController extends BaseController {
    private final PlanningProjectPlankService planningProjectPlankService;
    private final StyleColorService styleColorService;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final FormTypeService formTypeService;
    private final FieldOptionConfigService fieldOptionConfigService;
    private final FieldManagementService fieldManagementService;
    private final PlanningDimensionalityService planningDimensionalityService;
    private final StylePicUtils stylePicUtils;
    private final StyleService styleService;
    private final PlanningProjectService planningProjectService;
    private final RedisUtils redisUtils;

    /**
     * 查询列表
     */
    @ApiOperation(value = "查询列表")
    @GetMapping("/ListByDto")
    public ApiResult ListByDto(PlanningProjectPlankPageDto dto) {
        //将查询条件放入redis
        redisUtils.set("planningProjectPlank:ListByDto:"+dto.getPlanningProjectId() +":"+ this.getUserId(), dto);
        return selectSuccess(planningProjectPlankService.ListByDto(dto));
    }

    /**
     * 获取查询记录
     */
    @ApiOperation(value = "获取查询记录")
    @GetMapping("/getSearchRecord")
    public ApiResult getSearchRecord(String planningProjectId) {
        Object o = redisUtils.get("planningProjectPlank:ListByDto:" + planningProjectId + ":" + this.getUserId());
        return selectSuccess(o);
    }

    /**
     * 根据企划规划看板id和大类,品类,中类,查询
     */
    @ApiOperation(value = "根据企划规划看板id和大类,品类,中类,查询")
    @GetMapping("/ListByDtoAndCategory")
    public ApiResult ListByDtoAndCategory(PlanningProjectPlankPageDto dto) {
        QueryWrapper<PlanningProject> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("season_id",dto.getSeasonId());
        queryWrapper1.eq("planning_channel_code",dto.getPlanningChannelCode());
        PlanningProject planningProject = planningProjectService.getOne(queryWrapper1);
        if (planningProject==null) {
            throw new OtherException("当前产品季和渠道下无企划规划看板");
        }
        BaseQueryWrapper<PlanningProjectDimension> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("planning_project_id", planningProject.getId());
        queryWrapper.notEmptyEq("prod_category1st_code", dto.getProdCategory1stCode());
        queryWrapper.notEmptyEq("prod_category_code", dto.getProdCategoryCode());
        queryWrapper.notEmptyEq("prod_category2nd_code", dto.getProdCategory2ndCode());
        List<PlanningProjectDimension> list = planningProjectDimensionService.list(queryWrapper);
        for (PlanningProjectDimension planningProjectDimension : list) {
            //查询已匹配坑位的数量
            BaseQueryWrapper<PlanningProjectPlank> planningProjectPlankQueryWrapper = new BaseQueryWrapper<>();
            planningProjectPlankQueryWrapper.eq("planning_project_dimension_id", planningProjectDimension.getId());
            planningProjectPlankQueryWrapper.ne("matching_style_status", "0");
            long count = planningProjectPlankService.count(planningProjectPlankQueryWrapper);
            planningProjectDimension.setMatchedNumber(count);
        }
        return selectSuccess(list);
    }

    /**
     * 根据坑位Id查询坑位详情
     */
    @ApiOperation(value = "根据坑位Id查询坑位详情")
    @GetMapping("/getById")
    public ApiResult getById(String id) {
        PlanningProjectPlank planningProjectPlank = planningProjectPlankService.getById(id);
        PlanningProjectPlankVo planningProjectPlankVo =new PlanningProjectPlankVo();
        BeanUtil.copyProperties(planningProjectPlank,planningProjectPlankVo);
        if (StringUtils.isNotEmpty(planningProjectPlankVo.getStyleColorId())) {
            List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(planningProjectPlankVo.getStyleColorId());
            planningProjectPlankVo.setFieldManagementVos(fieldManagementVos);
        }

        if (StringUtils.isNotEmpty(planningProjectPlankVo.getHisDesignNo())) {
            StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", planningProjectPlankVo.getHisDesignNo()));
            Style style = styleService.getById(styleColor.getStyleId());
            StyleColorVo styleColorVo =new StyleColorVo();

            BeanUtil.copyProperties(style,styleColorVo);
            BeanUtil.copyProperties(styleColor,styleColorVo);
            String styleUrl = stylePicUtils.getStyleUrl(styleColorVo.getStyleColorPic());
            styleColorVo.setStyleColorPic(styleUrl);
            planningProjectPlankVo.setOldStyleColor(styleColorVo);

            List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(styleColor.getId());
            planningProjectPlankVo.setOldFieldManagementVos(fieldManagementVos);
        }
        if (StringUtils.isNotEmpty(planningProjectPlankVo.getStyleColorId())){
            StyleColor styleColor = styleColorService.getById(planningProjectPlankVo.getStyleColorId());
            if (styleColor!=null){
                String styleUrl = stylePicUtils.getStyleUrl(styleColor.getStyleColorPic());
                planningProjectPlankVo.setPic(styleUrl);
            }
        }

        return selectSuccess(planningProjectPlankVo);
    }

    /**
     * 查询维度标签列表
     */
    @ApiOperation(value = "查询维度标签列表")
    @GetMapping("/queryDimensionLabelList")
    public ApiResult queryDimensionLabelList(PlanningProjectPlankDto planningProjectPlankDto) {
        QueryWrapper<FormType> formTypeQueryWrapper = new QueryWrapper<>();
        formTypeQueryWrapper.eq("code", planningProjectPlankDto.getFormCode());
        FormType formType = formTypeService.getOne(formTypeQueryWrapper);
        if (formType==null) {
            throw new OtherException("获取表单失败");
        }
        /*品类查询字段配置列表查询品类下的字段id*/
        BaseQueryWrapper<FieldOptionConfig> qw = new BaseQueryWrapper<>();
        planningProjectPlankDto.setFieldId(formType.getId());
        PlanningUtils.fieldConfigQw(qw,planningProjectPlankDto);
        /*查询字段配置中的数据*/
        List<FieldOptionConfig> optionConfigList = fieldOptionConfigService.list(qw);

        /*获取到这个品类下存在的字段*/
        List<String> fieldManagementIdList = optionConfigList.stream().map(FieldOptionConfig::getFieldManagementId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fieldManagementIdList)) {
            throw new OtherException("字段未配置选项");
        }
        /*配置的字段*/
        /**
         * 查询需求占比中依赖于字段id
         */
        List<FieldManagement> fieldManagementList = fieldManagementService.listByIds(fieldManagementIdList);
        return selectSuccess(fieldManagementList);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@RequestBody PlanningProjectPlank planningProjectPlank) {
        if (StringUtils.isEmpty(planningProjectPlank.getId())){
            planningProjectPlankService.save(planningProjectPlank);
            //重新增加坑位数量
            String planningProjectDimensionId = planningProjectPlank.getPlanningProjectDimensionId();

            if (StringUtils.isNotEmpty(planningProjectDimensionId)){
                PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectDimensionId);
                QueryWrapper<PlanningProjectPlank> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("planning_project_dimension_id", planningProjectDimensionId);
                long l = planningProjectPlankService.count(queryWrapper);

                planningProjectDimension.setNumber(String.valueOf(l));
                planningProjectDimensionService.updateById(planningProjectDimension);
            }
            return insertSuccess("新增成功");
        }else {
            planningProjectPlankService.updateById(planningProjectPlank);
            return updateSuccess("修改成功");
        }

    }

    /**
     * 更换历史款
     */
    @ApiOperation(value = "更换历史款")
    @PostMapping("/changeHistoryStyle")
    public ApiResult changeHistoryStyle(@RequestBody PlanningProjectPlank planningProjectPlank) {
        PlanningProjectPlank planningProjectPlank1 = planningProjectPlankService.getById(planningProjectPlank.getId());

        if ("0".equals(planningProjectPlank1.getMatchingStyleStatus()) || "3".equals(planningProjectPlank1.getMatchingStyleStatus())) {
            // 是历史款或者未匹配就将历史款的数据放入当前坑位
            planningProjectPlank1.setHisDesignNo(planningProjectPlank.getHisDesignNo());
            planningProjectPlank1.setMatchingStyleStatus("3");
            planningProjectPlank1.setBulkStyleNo(planningProjectPlank.getBulkStyleNo());
            StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", planningProjectPlank.getHisDesignNo()));
            planningProjectPlank1.setStyleColorId(styleColor.getId());
            // planningProjectPlank1.setPic(styleColor.getStyleColorPic());
        } else {
            //不是是历史款就更换历史款号
            planningProjectPlank1.setHisDesignNo(planningProjectPlank.getHisDesignNo());

        }
        planningProjectPlankService.updateById(planningProjectPlank1);
        return updateSuccess("更换历史款成功");
    }

    /**
     * 匹配坑位
     */
    @ApiOperation(value = "匹配坑位")
    @PostMapping("/match")
    public ApiResult match(@RequestBody MatchSaveDto matchSaveDto) {
        StyleColor styleColor = styleColorService.getById(matchSaveDto.getStyleColorId());
        PlanningProjectPlank planningProjectPlank = planningProjectPlankService.getById(matchSaveDto.getPlankId());
        planningProjectPlank.setBulkStyleNo(styleColor.getStyleNo());
        planningProjectPlank.setStyleColorId(matchSaveDto.getStyleColorId());
        planningProjectPlank.setPic(styleColor.getStyleColorPic());
        // planningProjectPlank.setBandName(styleColor.getBandName());
        // planningProjectPlank.setBandCode(styleColor.getBandCode());
        planningProjectPlank.setMatchingStyleStatus("1");
        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColor.getColorCode()));
        if (colourLibrary != null) {
            planningProjectPlank.setColorSystem(colourLibrary.getColorType());
        }
        planningProjectPlankService.updateById(planningProjectPlank);
        return updateSuccess("匹配成功");
    }

    /**
     * 匹配坑位
     */
    @ApiOperation(value = "取消匹配")
    @PostMapping("/unMatch")
    public ApiResult unMatch(@RequestBody UnMatchDto unMatchDto) {
        PlanningProjectPlank planningProjectPlank = planningProjectPlankService.getById(unMatchDto.getPlankId());
        PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectPlank.getPlanningProjectDimensionId());

        planningProjectPlank.setBulkStyleNo("");
        planningProjectPlank.setStyleColorId("");
        planningProjectPlank.setPic("");
        planningProjectPlank.setBandName(planningProjectDimension.getBandName());
        planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
        planningProjectPlank.setMatchingStyleStatus("0");
        planningProjectPlank.setColorSystem("");
        planningProjectPlankService.updateById(planningProjectPlank);
        return updateSuccess("取消匹配成功");
    }

    /**
     * 根据ids删除
     */
    @ApiOperation(value = "根据ids删除")
    @DeleteMapping("/delByIds")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delByIds(String ids) {
        String[] split = ids.split(",");
        QueryWrapper<PlanningProjectPlank> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.in("id",Arrays.asList(split));
        queryWrapper.select("planning_project_dimension_id");
        List<PlanningProjectPlank> planningProjectPlanks = planningProjectPlankService.list(queryWrapper);
        boolean b = planningProjectPlankService.removeByIds(Arrays.asList(split));
        if (b){
            //变更坑位数量

            List<String> dimensionIds = planningProjectPlanks.stream().map(PlanningProjectPlank::getPlanningProjectDimensionId).collect(Collectors.toList());
            if (!dimensionIds.isEmpty()){
                List<PlanningProjectDimension> planningProjectDimensions = planningProjectDimensionService.listByIds(dimensionIds);
                for (PlanningProjectDimension planningProjectDimension : planningProjectDimensions) {
                    long l = planningProjectPlankService.count(new QueryWrapper<PlanningProjectPlank>().eq("planning_project_dimension_id", planningProjectDimension.getId()));
                    planningProjectDimension.setNumber(String.valueOf(l));
                }
                planningProjectDimensionService.saveOrUpdateBatch(planningProjectDimensions);
            }
        }

        return deleteSuccess(b);
    }

    /**
     * 获取维度字段卡片
     */
    @ApiOperation(value = "获取维度字段卡片")
    @GetMapping("/getDimensionFieldCard")
    public ApiResult getDimensionFieldCard(DimensionLabelsSearchDto dto){
        List<PlanningDimensionality> planningDimensionalities = planningDimensionalityService.getDimensionalityList(dto).getPlanningDimensionalities();
        List<FieldDisplayVo> fieldDisplayVoList = planningDimensionalities.stream().map(planningDimensionality -> {
            FieldDisplayVo fieldDisplayVo = new FieldDisplayVo();
            fieldDisplayVo.setField(planningDimensionality.getDimensionalityName());
            fieldDisplayVo.setName(planningDimensionality.getDimensionalityName());
            fieldDisplayVo.setField(planningDimensionality.getId());
            //设置是否显示
            String key = "planningProjectPlank:dimensionFieldCard:" +this.getUserId()+":"+ planningDimensionality.getDimensionalityName();
            if (redisUtils.hasKey(key)) {
                fieldDisplayVo.setDisplay("1".equals(redisUtils.get(key)));
            }else {
                fieldDisplayVo.setDisplay(true);
            }
            return fieldDisplayVo;
        }).collect(Collectors.toList());

        return selectSuccess(fieldDisplayVoList);
    }

    /**
     * 设置维度字段卡片
     */
    @ApiOperation(value = "设置维度字段卡片")
    @PostMapping("/setDimensionFieldCard")
    public ApiResult setDimensionFieldCard(@RequestBody List<FieldDisplayVo> fieldDisplayVoList){
        fieldDisplayVoList.forEach(fieldDisplayVo -> {
            String key = "planningProjectPlank:dimensionFieldCard:" +this.getUserId()+":"+ fieldDisplayVo.getField();
            redisUtils.set(key,fieldDisplayVo.isDisplay()?"1":"0");
        });
        return updateSuccess("设置成功");
    }


}
