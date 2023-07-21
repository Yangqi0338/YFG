package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.vo.*;
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
    @Autowired
    private PlanningBandService planningBandService;
    @Resource
    private PlanningCategoryService planningCategoryService;
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

    @ApiOperation(value = "产品季-查询年份品牌树")
    @GetMapping("/queryYearBrandTree")
    public List<YearBrandVo> queryYearBrandTree(String search) {
        return planningSeasonService.queryYearBrandTree(search);
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


    @ApiOperation(value = "查询波段企划-分页查询")
    @GetMapping("/planBand")
    public PageInfo<PlanningSeasonBandVo> bandList(@Valid PlanningBandSearchDto dto) {
        PageInfo<PlanningSeasonBandVo> pageInfo = planningBandService.queryPlanningSeasonBandPageInfo(dto, getUserCompany());
        if (pageInfo != null) {
            return pageInfo;
        }
        return new PageInfo<>();
    }

    @ApiOperation(value = "查询波段企划-通过产品季和波段企划名称")
    @GetMapping("/planBand/getByName")
    public PlanningSeasonBandVo getBandByName(@NotNull(message = "产品季名称不能为空") String planningSeasonName, @NotNull(message = "波段企划名称不能为空") String planningBandName) {
        PlanningSeasonBandVo result = planningBandService.getBandByName(planningSeasonName, planningBandName, getUserCompany());
        return result;
    }

    @ApiOperation(value = "保存波段企划")
    @PostMapping("/planBand")
    public PlanningBandVo savePlanBand(@Valid @RequestBody PlanningBandDto dto) {
        // 校验重复
        planningBandService.checkRepeat(dto, getUserCompany());
        PlanningBand bean = planningBandService.savePlanningBand(dto);
        return BeanUtil.copyProperties(bean, PlanningBandVo.class);
    }

    @ApiOperation(value = "修改坑位信息")
    @PostMapping("/updateCategoryItem")
    public List<PlanningCategoryItemSaveDto> updateCategoryItem(@RequestParam(value = "planningBandId", required = false) String planningBandId, @RequestBody List<PlanningCategoryItemSaveDto> item) {
        planningCategoryItemService.updateCategoryItem(planningBandId, item);
        return item;
    }

    @ApiOperation(value = "坑位信息下发(坑位信息下发到产品季总览)")
    @PostMapping("/seatSend")
    public boolean seatSend(@Valid @RequestBody List<PlanningCategoryItemSaveDto> item) {
        return planningCategoryItemService.seatSend(item);

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


    @ApiOperation(value = "删除波段企划下的品类信息")
    @DeleteMapping("/planBand/delCategory")
    public boolean delPlanBandCategory(String ids) {
        if (StrUtil.isBlank(ids)) {
            return false;
        }
        List<String> idList = StrUtil.split(ids, CharUtil.COMMA);
        return planningCategoryService.delPlanningCategory(getUserCompany(), idList);
    }


    @ApiOperation(value = "删除产品季")
    @DeleteMapping("/planningSeason")
    public boolean delPlanningSeason(@Valid @NotNull(message = "编号不能为空") String id) {
        if (planningSeasonService.checkPlanningSeasonHasBand(id)) {
            throw new OtherException("存在波段企划无法删除");
        }
        return planningSeasonService.delPlanningSeason(id);
    }


    @ApiOperation(value = "删除波段企划")
    @DeleteMapping("/planningBand")
    public boolean delPlanningBand(@Valid @NotNull(message = "编号不能为空") String id) {
        return planningBandService.del(id);
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
            @ApiImplicitParam(name = "post", value = "岗位,名称，编码", paramType = "query"),
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
