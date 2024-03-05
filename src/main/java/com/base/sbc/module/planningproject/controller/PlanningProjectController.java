package com.base.sbc.module.planningproject.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumModelTypeExcelDto;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planningproject.dto.PlanningProjectImportDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@Api(tags = "企划看板规划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningProject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
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
        updateWrapper.in("id", Arrays.asList(ids.split(",")));
        updateWrapper.set("status", status);

        //如果是停用,清空关联的坑位数据
        if ("1".equals(status)) {
            updateWrapper.set("is_match", "0");

            UpdateWrapper<PlanningProjectPlank> wrapper =new UpdateWrapper<>();
            wrapper.in("planning_project_id",ids);
            wrapper.set("bulk_style_no","");
            wrapper.set("pic","");
            wrapper.set("color_system","");
            wrapper.set("style_color_id","0");
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
        Set<String> idSet = Collections.singleton(ids);
        boolean b = planningProjectService.removeByIds(idSet);
        if (b){
            QueryWrapper<PlanningProjectDimension> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.in("planning_project_id",idSet);
            planningProjectDimensionService.remove(queryWrapper);
            QueryWrapper<PlanningProjectMaxCategory> queryWrapper1 =new BaseQueryWrapper<>();
            queryWrapper1.in("planning_project_id",idSet);
            planningProjectMaxCategoryService.remove(queryWrapper1);
            QueryWrapper<PlanningProjectPlank> queryWrapper2 =new BaseQueryWrapper<>();
            queryWrapper2.in("planning_project_id",idSet);
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

}
