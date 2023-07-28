package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.PlanningSeasonVo;
import com.base.sbc.module.planning.vo.YearSeasonBandVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：商品企划 相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.controller.PlanningController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 13:47
 */
@RestController
@Api(tags = "商品企划 相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningController extends BaseController {

    @Resource
    private PlanningSeasonService planningSeasonService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;

    @Resource
    private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;
    @Resource
    private PlanningChannelService planningChannelService;
    @Autowired
    private CcmService ccmService;
    @Autowired
    private AmcFeignService amcFeignService;
    private IdGen idGen = new IdGen();

    @ApiOperation(value = "保存产品季", notes = "")
    @PostMapping
    public PlanningSeasonVo save(@Valid @RequestBody PlanningSeasonSaveDto dto) {
        // 校验名称重复
        planningSeasonService.checkNameRepeat(dto, getUserCompany());
        // 校验品牌，年份，季节重复
        planningSeasonService.checkBYSRepeat(dto, getUserCompany());
        //保存产品季
        PlanningSeason bean = planningSeasonService.savePlanningSeason(dto);
        return BeanUtil.copyProperties(bean, PlanningSeasonVo.class);
    }

    @ApiOperation(value = "查询产品季-通过季名称查询")
    @GetMapping("/getByName")
    public PlanningSeasonVo getByName(@NotNull(message = "名称不能为空") String name) {
        return planningSeasonService.getByName(name);
    }

    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo query(PlanningSeasonSearchDto dto) {
        PageInfo<PlanningSeasonVo> pageInfoVO = planningSeasonService.queryPlanningSeasonPageInfo(dto, getUserCompany());
        if (pageInfoVO != null) {
            return pageInfoVO;
        }
        return new PageInfo<>();
    }

    @ApiOperation(value = "产品季-查询年份品牌树(新)")
    @GetMapping("/queryYearBrandTree")
    public List<YearSeasonBandVo> queryYearBrandTree(YearSeasonBandVo vo) {
        return planningSeasonService.queryYearBrandTree(vo);
    }

    @ApiOperation(value = "新增渠道")
    @PostMapping("/channel")
    public PlanningChannelVo save(@Valid @RequestBody PlanningChannelDto dto) {
        return planningChannelService.saveByDto(dto);
    }

    @ApiOperation(value = "渠道列表-分页查询")
    @GetMapping("/channel")
    public PageInfo<PlanningChannelVo> channelPageInfo(PlanningChannelSearchDto dto) {
        return planningChannelService.channelPageInfo(dto);
    }

    @ApiOperation(value = "坑位列表-左侧树")
    @GetMapping("/categoryTree")
    public List<BasicStructureTreeVo> categoryTree(String planningChannelId) {
        return planningCategoryItemService.categoryTree(planningChannelId);
    }





    @ApiOperation(value = "修改坑位信息")
    @PostMapping("/updateCategoryItem")
    public List<PlanningCategoryItemSaveDto> updateCategoryItem(@RequestBody List<PlanningCategoryItemSaveDto> item) {
        planningCategoryItemService.updateCategoryItem(item);
        return item;
    }

    @ApiOperation(value = "新建坑位(新)")
    @PostMapping("/addSeat")
    public boolean addSeat(@Validated @RequestBody AddSeatDto dto) {
        return planningCategoryItemService.addSeat(dto);
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


    @ApiOperation(value = "坑位信息下发(坑位信息下发到产品季总览)")
    @PostMapping("/seatSend")
    public boolean seatSend(@Valid @RequestBody List<PlanningCategoryItemSaveDto> item) {
        return planningCategoryItemService.seatSend(item);

    }

    @ApiOperation(value = "查询坑位列表")
    @PostMapping("/seatList")
    public PageInfo<PlanningSeasonOverviewVo> seatList(@Valid @RequestBody ProductCategoryItemSearchDto dto) {
        if (dto == null) {
            dto = new ProductCategoryItemSearchDto();
        }
        dto.setOrderBy("c.id desc ");
        return planningCategoryItemService.findProductCategoryItem(dto);
    }

    @ApiOperation(value = "修改图片")
    @GetMapping("/updateStylePic")
    public boolean updateStylePic(@Valid @NotBlank(message = "id不能为空") String id, String stylePic) {
        return planningCategoryItemService.updateStylePic(id, stylePic);
    }

    @ApiOperation(value = "获取下一个流水(测试)")
    @GetMapping("/nextDesignNo")
    public String nextDesignNo() {
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        Map<String, String> params = new HashMap<>(12);
        params.put("brand", "1");
        params.put("year", "2021");
        params.put("season", "A");
        params.put("category", "0");
        params.put("designCode", "LD");
        List<String> planningDesignNo1 = getMaxCode.genCode("PLANNING_DESIGN_NO", 10, params);
        return CollUtil.join(planningDesignNo1, ",");
    }

    @ApiOperation(value = "获取最大流水号")
    @PostMapping("/maxDesignNo")
    public String maxDesignNo(@RequestBody GetMaxCodeRedis data) {
        if (ObjectUtil.isEmpty(data.getValueMap())) {
            throw new OtherException("条件不能为空");
        }
        return planningCategoryItemService.getMaxDesignNo(data, getUserCompany());

    }


    @ApiOperation(value = "删除产品季")
    @DeleteMapping("/planningSeason")
    public boolean delPlanningSeason(@Valid @NotNull(message = "编号不能为空") String id) {
        if (planningSeasonService.checkPlanningSeasonHasSub(id)) {
            throw new OtherException("存在渠道企划无法删除");
        }
        return planningSeasonService.delPlanningSeason(id);
    }


    @ApiOperation(value = "保存关联的素材库")
    @PostMapping("/savePlanningCategoryItemMaterial")
    public boolean savePlanningCategoryItemMaterial(@RequestBody PlanningCategoryItemMaterialSaveDto dto) {
        return planningCategoryItemMaterialService.savePlanningCategoryItemMaterial(dto);
    }

    @ApiOperation(value = "查询产品季用户列表", notes = "可通过岗位查询")
    @GetMapping("/getTeamUserList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planningSeasonId", value = "产品季id", paramType = "query", required = true),
            @ApiImplicitParam(name = "post", value = "操作人", paramType = "query"),
    })
    public List<UserCompany> getTeamUserList(@Valid @NotBlank(message = "产品季id不能为空") String planningSeasonId, String post) {
        return amcFeignService.getTeamUserListByPost(planningSeasonId, post);
    }


    @ApiOperation(value = "查询坑位信息的维度数据", notes = "")
    @GetMapping("/querySeatDimension")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "坑位信息id", paramType = "query", required = true),
            @ApiImplicitParam(name = "isSelected", value = "选择标识(非空只返回选择了的维度)", paramType = "query", required = true),
    })
    public List<FieldManagementVo> querySeatDimension(String id, String isSelected) {
        return planningCategoryItemService.querySeatDimension(id, isSelected);
    }

}
