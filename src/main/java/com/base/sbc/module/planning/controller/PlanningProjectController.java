package com.base.sbc.module.planning.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.planning.dto.AddSeatDto;
import com.base.sbc.module.planning.dto.PlanningProjectDTO;
import com.base.sbc.module.planning.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningProjectService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningProjectVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.YearSeasonBandVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.plugin.ClassLoaderInfo;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@Api(tags = "企划看板规划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningProject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validate
public class PlanningProjectController extends BaseController{
    private final ReentrantLock lock = new ReentrantLock();

    @Resource
    private PlanningSeasonService planningSeasonService;

    @Autowired
    private PlanningProjectService planningProjectService;

    @Autowired
    private MinioUtils minioUtils;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;
    @ApiOperation(value = "企划看板计划查询", notes = "")
    @GetMapping("/getPlanningProjectList")
    public PageInfo<PlanningProjectVo> planningProject(@Valid PlanningProjectPageDTO dto) {
        PageInfo<PlanningProjectVo> pageInfoVO = planningProjectService.planningProject(dto);
        return pageInfoVO;
    }

    @ApiOperation(value = "新增、修改企划看板计划", notes = "")
    @PostMapping("/getPlanningProjectAdd")
    public ApiResult planningProjectAdd(@Valid @RequestBody PlanningProjectDTO dto) {
        return insertSuccess(planningProjectService.planningProjectAdd(dto));
    }

    @ApiOperation(value = "新增、修改企划看板计划", notes = "")
    @PostMapping("/getPlanningProjectUpdate")
    public ApiResult planningProjectUpdate(@Valid @RequestBody PlanningProjectDTO dto) {
        return updateSuccess(planningProjectService.planningProjectUpdate(dto));
    }


    @ApiOperation(value = "删除企划看板计划", notes = "")
    @DeleteMapping("/getPlanningProjectDel")
    public boolean planningProjectDel(@Valid @NotNull(message = "编号不能为空") String id) {

        return planningProjectService.planningProjectDel(id);
    }

    @ApiOperation(value = "产品季-查询年份品牌树(新)")
    @GetMapping("/queryYearBrandTree")
    public List<YearSeasonBandVo> queryYearBrandTree(YearSeasonBandVo vo) {
        return planningSeasonService.queryYearBrandTree(vo);
    }

    @ApiOperation(value = "新建坑位(新)")
    @PostMapping("/addSeat")
    public boolean addSeat(@Validated @RequestBody AddSeatDto dto) {
        lock.lock();
        try {
            return planningCategoryItemService.addSeat(dto);
        } finally {
            lock.unlock();
        }
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
