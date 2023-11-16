package com.base.sbc.module.planningproject.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.planning.dto.AddSeatDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.YearSeasonBandVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

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

    @ApiOperation(value = "企划看板计划查询")
    @GetMapping("/queryPage")
    public PageInfo<PlanningProjectVo> queryPage(@Valid PlanningProjectPageDTO dto) {
        return planningProjectService.queryPage(dto);
    }

    @ApiOperation(value = "新增、修改企划看板计划")
    @PostMapping("/save")
    public ApiResult save(@Valid @RequestBody PlanningProjectSaveDTO planningProjectSaveDTO) {
        return insertSuccess(planningProjectService.save(planningProjectSaveDTO));
    }

    @ApiOperation(value = "启用停用")
    @GetMapping("/startStop")
    public ApiResult startStop(@Valid @NotNull(message = "传入id不能为空") String ids, @Valid @NotNull(message = "传入状态不能为空") String status) {
        UpdateWrapper<PlanningProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", Arrays.asList(ids.split(",")));
        updateWrapper.set("status", status);
        return updateSuccess(planningProjectService.update(updateWrapper));
    }

    @ApiOperation(value = "删除企划看板计划")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(@Valid @NotNull(message = "传入id不能为空") String ids) {
        return deleteSuccess(planningProjectService.removeByIds(Arrays.asList(ids.split(","))));
    }

    @ApiOperation(value = "产品季-查询年份品牌树(新)")
    @GetMapping("/queryYearBrandTree")
    public List<YearSeasonBandVo> queryYearBrandTree(YearSeasonBandVo vo) {
        return planningSeasonService.queryYearBrandTree(vo);
    }

    @ApiOperation(value = "新建坑位(新)")
    @PostMapping("/addSeat")
    @DuplicationCheck
    public boolean addSeat(@Validated @RequestBody AddSeatDto dto) {
        return planningCategoryItemService.addSeat(dto);
    }

    @ApiOperation(value = "查询坑位列表")
    @PostMapping("/seatList")
    public PageInfo<PlanningSeasonOverviewVo> seatList(@Valid @RequestBody ProductCategoryItemSearchDto dto) {
        if (dto == null) {
            dto = new ProductCategoryItemSearchDto();
        }
        dto.setOrderBy("c.id desc ");
        PageInfo<PlanningSeasonOverviewVo> productCategoryItem = planningCategoryItemService.findProductCategoryItem(dto);
        minioUtils.setObjectUrlToList(productCategoryItem.getList(), "stylePic");
        return productCategoryItem;
    }

    @ApiOperation(value = "撤回(新)")
    @GetMapping("/seat/revoke")
    public boolean seatRevoke(@Validated IdsDto idsDto) {
        return planningCategoryItemService.revoke(idsDto.getId());
    }

    @ApiOperation(value = "删除坑位信息(新)")
    @GetMapping("/seat/del")
    public boolean seatDel(@Validated IdsDto idsDto) {
        return planningCategoryItemService.del(idsDto.getId());
    }
}
