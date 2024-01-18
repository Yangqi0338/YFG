package com.base.sbc.module.planningproject.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumModelTypeExcelDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planningproject.dto.HistoryMatchDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectImportDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "企划看板规划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningProject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validate
@RequiredArgsConstructor
public class PlanningProjectController extends BaseController {
    private final PlanningProjectService planningProjectService;
    private final PlanningProjectPlankService planningProjectPlankService;
    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectImportService planningProjectImportService;
    private final PlanningProjectMaxCategoryService  planningProjectMaxCategoryService;
    private final PlanningCategoryItemService planningCategoryItemService;
    private final DataPermissionsService dataPermissionsService;
    private final PlanningChannelService planningChannelService;
    private final StyleColorService styleColorService;
    private final StyleService styleService;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    @ApiOperation(value = "企划看板计划查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage(@Valid PlanningProjectPageDTO dto) {
        return selectSuccess(planningProjectService.queryPage(dto));
    }

    @ApiOperation(value = "新增、修改企划看板计划")
    @PostMapping("/save")
    @DuplicationCheck
    public ApiResult save(@Valid @RequestBody PlanningProjectSaveDTO planningProjectSaveDTO) {
        return insertSuccess(planningProjectService.save(planningProjectSaveDTO));
    }

    @ApiOperation(value = "启用停用")
    @PostMapping("/startStop")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult startStop(@Valid @NotNull(message = "传入id不能为空") String ids, @Valid @NotNull(message = "传入状态不能为空") String status) {
        UpdateWrapper<PlanningProject> updateWrapper = new UpdateWrapper<>();
        List<String> idList = Arrays.asList(ids.split(","));
        updateWrapper.in("id", idList);
        updateWrapper.set("status", status);

        //如果是停用,清空关联的坑位数据,不清除关联历史款的坑位数据
        if ("1".equals(status)) {
            updateWrapper.set("is_match", "0");

            UpdateWrapper<PlanningProjectPlank> wrapper =new UpdateWrapper<>();
            wrapper.in("planning_project_id",idList);
            // wrapper.ne("matching_style_status","3");
            wrapper.set("bulk_style_no","");
            wrapper.set("pic","");
            wrapper.set("color_system","");
            wrapper.set("style_color_id","");
            wrapper.set("his_design_no","");
            wrapper.set("matching_style_status","0");
            planningProjectPlankService.update(wrapper);
        }

        return updateSuccess(planningProjectService.update(updateWrapper));
    }

    /**
     * 根据ids删除
     */
    @ApiOperation(value = "根据ids删除")
    @DeleteMapping("/delByIds")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return deleteSuccess(false);
        }
        List<String> idList = Arrays.asList(ids.split(","));
        boolean b = planningProjectService.removeByIds(idList);
        if (b){
            QueryWrapper<PlanningProjectDimension> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.in("planning_project_id",idList);
            planningProjectDimensionService.remove(queryWrapper);
            QueryWrapper<PlanningProjectMaxCategory> queryWrapper1 =new BaseQueryWrapper<>();
            queryWrapper1.in("planning_project_id",idList);
            planningProjectMaxCategoryService.remove(queryWrapper1);
            QueryWrapper<PlanningProjectPlank> queryWrapper2 =new BaseQueryWrapper<>();
            queryWrapper2.in("planning_project_id",idList);
            planningProjectPlankService.remove(queryWrapper2);
        }
        return deleteSuccess(b);

    }

    /**
     * 导入数据看板
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file,String seasonId,String planningChannelCode) throws Exception {
        List<PlanningProjectImportDto> list = ExcelImportUtil.importExcel(file.getInputStream(), PlanningProjectImportDto.class, new ImportParams());
        QueryWrapper<PlanningProjectImportDto> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("season_id",seasonId);
        queryWrapper.eq("planning_channel_code",planningChannelCode);
        planningProjectImportService.remove(queryWrapper);
        for (PlanningProjectImportDto planningProjectImportDto : list) {
            planningProjectImportDto.setPlanningChannelCode(planningChannelCode);
            planningProjectImportDto.setSeasonId(seasonId);
        }
        planningProjectImportService.saveBatch(list);
        return insertSuccess(true);
    }

    /**
     * 导入季节企划
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel2")
    public ApiResult importExcel2(@RequestParam("file") MultipartFile file) throws Exception {
        List<HashMap<Integer,String>> hashMaps = EasyExcel.read(file.getInputStream()).headRowNumber(0).doReadAllSync();
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < hashMaps.size(); i++) {
            for (Integer s : hashMaps.get(i).keySet()) {
                System.out.println(s);
                jsonObject.put(String.valueOf(s),hashMaps.get(i).get(s));
            }



            switch (i){
                case 0:
                    //标题
                    Object o = jsonObject.get(0);
                    break;
                    case 1:

                        for (String s : jsonObject.keySet()) {
                            System.out.println(s);
                            int i1 = Integer.parseInt(s);
                            if (i1>3){
                                System.out.println(jsonObject.get(i1));
                            }
                        }

                        //上市波段
                    break;
                    case 2:
                        //下单时间
                    break;
                    case 3:
                        //上市时间
                    break;
                    case 4:
                        //款式类别
                    break;
                    case 5:
                        //品类
                    break;
                    case 6:
                        //品类
                    break;
                    case 7:
                        //品类
                    break;
            }

        }

        return null;
    }

    /**
     * 根据导入数据的ids删除
     */
    @ApiOperation(value = "根据导入数据的ids删除")
    @DeleteMapping("/delByImportIds")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delByImportIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return deleteSuccess(false);
        }
        boolean b = planningProjectImportService.removeByIds(Arrays.asList(ids.split(",")));
        return deleteSuccess(b);

    }

    /**
     * 查询看板数据
     */
    @ApiOperation(value = "查询看板数据")
    @GetMapping("/queryPlank")
    public ApiResult queryPlank(String seasonId,String planningChannelCode) {
        QueryWrapper<PlanningProjectImportDto> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("season_id",seasonId);
        queryWrapper.eq("planning_channel_code",planningChannelCode);
        return selectSuccess(planningProjectImportService.list(queryWrapper));
    }


    /**
     * 根据大类或者中类查询最大坑位数量
     */
    @ApiOperation(value = "根据大类或者中类查询最大坑位数量")
    @GetMapping("/seatCount")
    public ApiResult seatCount(ProductCategoryItemSearchDto dto) {
        List<PlanningChannel> planningChannels = planningChannelService.list(new QueryWrapper<PlanningChannel>().eq("planning_season_id", dto.getPlanningSeasonId()));
        if (planningChannels.isEmpty()){
           throw new OtherException("该季节下没有企划渠道");
        }
        boolean flag =false;
        BaseQueryWrapper<PlanningCategoryItem> qw = new BaseQueryWrapper<>();
        for (PlanningChannel planningChannel : planningChannels) {
            if (planningChannel.getChannel().equals(dto.getChannelCode())){
                qw.eq("planning_channel_id",planningChannel.getId());
                flag=true;
                break;
            }
        }
        if (!flag){
            throw new OtherException("该季节下没有该企划渠道");
        }
        qw.notEmptyEq("prod_category",dto.getProdCategory());
        qw.notEmptyEq("prod_category1st",dto.getProdCategory1st());
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PlanningCategoryItem.getK(), "t_planning_category_item");
        long count = planningCategoryItemService.count(qw);
        return selectSuccess(count);

    }

    /**
     * 可引用历史款列表
     */
    @ApiOperation(value = "可引用历史款列表")
    @GetMapping("/historyList")
    public ApiResult historyList(PlanningProjectPageDTO dto) {
        PlanningProject planningProject = planningProjectService.getById(dto.getPlanningProjectId());

        List<PlanningChannel> planningChannels = planningChannelService.list(new QueryWrapper<PlanningChannel>().eq("planning_season_id", planningProject.getSeasonId()));
        if (planningChannels.isEmpty()){
            return selectSuccess(Collections.emptyList());
        }
        boolean flag =false;
        for (PlanningChannel planningChannel : planningChannels) {
            if (planningChannel.getChannel().equals(planningProject.getPlanningChannelCode())){
                dto.setPlanningChannelId(planningChannel.getId());
                flag=true;
                break;
            }
        }
        if (!flag){
            return selectSuccess(Collections.emptyList());
        }
        return selectSuccess( planningProjectService.historyList(dto));
    }

    /**
     * 引用历史款匹配
     */
    @ApiOperation(value = "引用历史款匹配")
    @PostMapping("/historyMatch")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult historyMatch(@Valid @RequestBody HistoryMatchDto historyMatchDto) {
        PlanningProject planningProject = planningProjectService.getById(historyMatchDto.getPlanningProjectId());
        QueryWrapper<PlanningProjectPlank> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("planning_project_id",historyMatchDto.getPlanningProjectId());
        queryWrapper.eq("matching_style_status","0");
        List<PlanningProjectPlank> list = planningProjectPlankService.list(queryWrapper);
        if (list.isEmpty()){
            throw new OtherException("没有可匹配的坑位");
        }
        String hisDesignNos = historyMatchDto.getHisDesignNos();
        String[] split = hisDesignNos.split(",");
        Set<String> oldDesignNoList =new HashSet<>( Arrays.asList(split));
        for (PlanningProjectPlank planningProjectPlank : list) {
            String planningProjectDimensionId = planningProjectPlank.getPlanningProjectDimensionId();
            PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectDimensionId);
            if (oldDesignNoList.isEmpty()){
                break;
            }
            List<PlanningCategoryItem> categoryItemList = planningCategoryItemService.listByField("his_design_no", oldDesignNoList);

            for (PlanningCategoryItem planningCategoryItem : categoryItemList) {
                if (planningCategoryItem.getProdCategory().equals(planningProjectDimension.getProdCategoryCode()) && planningCategoryItem.getProdCategory1st().equals(planningProjectDimension.getProdCategory1stCode())){
                    QueryWrapper<StyleColor> queryWrapper1 =new BaseQueryWrapper<>();
                    queryWrapper1.eq("style_no",planningCategoryItem.getHisDesignNo());
                    StyleColor styleColor = styleColorService.getOne(queryWrapper1);
                    if (styleColor==null){
                        throw new OtherException("不存在对应的大货:"+planningCategoryItem.getHisDesignNo());
                    }
                    planningProjectPlank.setBulkStyleNo(styleColor.getStyleNo());
                    planningProjectPlank.setStyleColorId(styleColor.getId());
                    planningProjectPlank.setPic(styleColor.getStyleColorPic());
                    planningProjectPlank.setMatchingStyleStatus("3");
                    planningProjectPlank.setHisDesignNo(styleColor.getStyleNo());
                    BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColor.getColorCode()));
                    if (colourLibrary != null) {
                        planningProjectPlank.setColorSystem(colourLibrary.getColorType());
                    }
                    planningProjectPlankService.updateById(planningProjectPlank);

                    oldDesignNoList.remove(styleColor.getStyleNo());
                    planningProject.setIsMatch("1");
                    break;
                }
            }
        }
        planningProjectService.updateById(planningProject);
        return updateSuccess("匹配成功");
    }


}
