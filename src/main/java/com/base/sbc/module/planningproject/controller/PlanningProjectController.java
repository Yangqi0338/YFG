package com.base.sbc.module.planningproject.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@RestController
@Api(tags = "企划看板规划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningProject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validate
@RequiredArgsConstructor
public class PlanningProjectController extends BaseController {
    private final PlanningSeasonService planningSeasonService;
    private final PlanningProjectService planningProjectService;
    private final MinioUtils minioUtils;
    private final PlanningCategoryItemService planningCategoryItemService;
    private final PlanningProjectPlankService planningProjectPlankService;

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
    @GetMapping("/startStop")
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
            wrapper.set("matching_style_status","0");
            wrapper.set("dimension_label_ids","");
            planningProjectPlankService.update(wrapper);
        }

        return updateSuccess(planningProjectService.update(updateWrapper));
    }
}
