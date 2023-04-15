package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.planning.dto.PlanningBandDto;
import com.base.sbc.module.planning.dto.PlanningBandSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningCategoryService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.service.impl.PlanningBandServiceImpl;
import com.base.sbc.module.planning.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/3/17 14:17:04
 */
@RestController
@Api(tags = "1.2 SAAS接口[企划接口]")
@RequestMapping(value = BaseController.SAAS_URL + "/planning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningController extends BaseController {

    @Resource
    private PlanningSeasonService planningSeasonService;
    @Autowired
    private PlanningBandServiceImpl planningBandService;
    @Resource
    private PlanningCategoryService planningCategoryService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;

    @Resource
    private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    @Autowired
    private CcmService ccmService;

    @Resource
    private AmcService amcService;
    @Resource
    private AmcFeignService amcFeignService;
    private IdGen idGen = new IdGen();

    @ApiOperation(value = "保存产品季", notes = "")
    @PostMapping
    public PlanningSeasonVo save(@Valid @RequestBody PlanningSeasonSaveDto dto) {
        // 校验名称重复
        QueryWrapper nameQc = new QueryWrapper();
        nameQc.eq(COMPANY_CODE, getUserCompany());
        nameQc.eq("name", dto.getName());
        nameQc.eq(StrUtil.isNotEmpty(dto.getId()), "id", dto.getId());
        long nameCount = planningSeasonService.count(nameQc);
        if (nameCount > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }


        PlanningSeason bean = null;
        if (StrUtil.isEmpty(dto.getId())) {
            bean = BeanUtil.copyProperties(dto, PlanningSeason.class);
            bean.setStatus(BaseGlobal.STATUS_NORMAL);
            planningSeasonService.save(bean);
        } else {
            bean = planningSeasonService.getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            planningSeasonService.updateById(bean);
        }
        return BeanUtil.copyProperties(bean, PlanningSeasonVo.class);
    }

    @ApiOperation(value = "查询产品季-通过季名称查询")
    @GetMapping("/getByName")
    public PlanningSeasonVo getByName(@NotNull(message = "名称不能为空") String name) {
        QueryWrapper qc = new QueryWrapper();
        qc.eq("name", name);
        qc.eq(COMPANY_CODE, getUserCompany());
        List<PlanningSeason> seasons = planningSeasonService.list(qc);
        if (CollUtil.isEmpty(seasons)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        return BeanUtil.copyProperties(seasons.get(0), PlanningSeasonVo.class);

    }

    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo query(PlanningSeasonSearchDto dto) {
        QueryWrapper qc = new QueryWrapper();
        qc.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq(COMPANY_CODE, getUserCompany());
        qc.like(StrUtil.isNotBlank(dto.getSearch()), dto.getSearch(), "name");
        qc.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
//        dto.setOrderBy("create_date desc ");
        Page<PlanningSeason> objects = PageHelper.startPage(dto);
        planningSeasonService.list(qc);
        PageInfo<PlanningSeason> planningSeasonPageInfo = objects.toPageInfo();
        List<PlanningSeason> list = planningSeasonPageInfo.getList();
        if (CollUtil.isNotEmpty(list)) {
            //查询用户信息
            Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(list.stream().map(PlanningSeason::getCreateId).collect(Collectors.joining(",")));
            List<PlanningSeasonVo> volist = BeanUtil.copyToList(list, PlanningSeasonVo.class);
            for (PlanningSeasonVo planningSeasonVo : volist) {
                planningSeasonVo.setAliasUserAvatar(userAvatarMap.get(planningSeasonVo.getCreateId()));
            }
            PageInfo<PlanningSeasonVo> pageInfoVO=new PageInfo<>();
            pageInfoVO.setList(volist);
            BeanUtil.copyProperties(planningSeasonPageInfo,pageInfoVO,"list");
            return pageInfoVO;
        }
        return new PageInfo<>();
    }

    @ApiOperation(value = "查询波段企划-分页查询")
    @GetMapping("/planBand")
    public PageInfo<PlanningSeasonBandVo> bandList(@Valid PlanningBandSearchDto dto) {
        // 1 查询产品季
        QueryWrapper<PlanningBand> qc = new QueryWrapper<>();
        qc.apply("b.planning_season_id=s.id");
        qc.eq(StrUtil.isNotBlank(dto.getSeasonName()), "s.name", dto.getSeasonName());
        qc.eq("s.company_code", getUserCompany());
        qc.eq("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        dto.setOrderBy("b.create_date desc ");
        PageHelper.startPage(dto);
        List<PlanningSeasonBandVo> list = planningBandService.selectByQw(qc);
        if (CollUtil.isNotEmpty(list)) {
            //查询用户信息
            Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(list.stream().map(item -> item.getBand().getCreateId()).collect(Collectors.joining(",")));
            list.forEach(item -> {
                PlanningBandVo band = item.getBand();
                band.setAliasUserAvatar(userAvatarMap.get(band.getCreateId()));
            });
            PageInfo<PlanningSeasonBandVo> pageInfo = new PageInfo<>(list);
            return pageInfo;
        }
        return new PageInfo<>();
    }

    @ApiOperation(value = "查询波段企划-通过产品季和波段企划名称")
    @GetMapping("/planBand/getByName")
    public PlanningSeasonBandVo getBandByName(@NotNull(message = "产品季名称不能为空") String planningSeasonName, @NotNull(message = "波段企划名称不能为空") String planningBandName) {
        QueryWrapper qc = new QueryWrapper();
        qc.apply("b.planning_season_id=s.id");
        qc.eq("s.company_code", getUserCompany());
        qc.eq("s.name", planningSeasonName);
        qc.eq("b.name", planningBandName);
        qc.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        List<PlanningSeasonBandVo> list = planningBandService.selectByQw(qc);
        PlanningSeasonBandVo first = CollUtil.getFirst(list);
        //查询品类列表
        QueryWrapper categoryQc = new QueryWrapper();
        categoryQc.eq(COMPANY_CODE, getUserCompany());
        categoryQc.eq("planning_season_id", first.getSeason().getId());
        categoryQc.eq("planning_band_id", first.getBand().getId());
        categoryQc.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        List<PlanningCategory> categoryData = planningCategoryService.list(categoryQc);
        first.getBand().setCategoryData(categoryData);
        //查询坑位信息
        List<PlanningCategoryItem> categoryItemData = planningCategoryItemService.list(categoryQc);
        // 关联素材库
        List<PlanningCategoryItemMaterialVo> itemMaterials = planningCategoryItemMaterialService.selectByQw(categoryQc);

        if (CollUtil.isNotEmpty(categoryItemData) && CollUtil.isNotEmpty(categoryData)) {
            List<PlanningCategoryItemVo> categoryItemVoData = new ArrayList<>(categoryData.size());
            Map<String, PlanningCategory> planningCategoryMap = categoryData.stream().collect(Collectors.toMap(PlanningCategory::getId, v -> v, (a, b) -> b));
            Map<String, List<PlanningCategoryItemMaterialVo>> itemMaterialMap = Optional.ofNullable(itemMaterials).orElse(CollUtil.newArrayList()).stream().collect(Collectors.groupingBy(PlanningCategoryItemMaterialVo::getPlanningCategoryItemId));
            for (PlanningCategoryItem categoryItem : categoryItemData) {
                PlanningCategoryItemVo planningCategoryItemVo = BeanUtil.copyProperties(categoryItem, PlanningCategoryItemVo.class);
                PlanningCategory p = planningCategoryMap.get(categoryItem.getPlanningCategoryId());
                if (p != null) {
                    planningCategoryItemVo.setParentCategoryIds(p.getCategoryIds());
                    planningCategoryItemVo.setParentCategoryName(p.getCategoryName());
                }
                planningCategoryItemVo.setMaterialVoList(itemMaterialMap.get(categoryItem.getId()));
                categoryItemVoData.add(planningCategoryItemVo);
            }
            first.getBand().setCategoryItemData(categoryItemVoData);
        } else {
            first.getBand().setCategoryItemData(new ArrayList<>());
        }

        return first;
    }

    @ApiOperation(value = "保存波段企划")
    @PostMapping("/planBand")
    public PlanningBandVo savePlanBand(@Valid @RequestBody PlanningBandDto dto) {
        // 校验名称重复
        QueryWrapper nameQc = new QueryWrapper();
        nameQc.eq(COMPANY_CODE, getUserCompany());
        nameQc.eq("name", dto.getName());
        nameQc.eq("planning_season_id", dto.getPlanningSeasonId());
        if (StrUtil.isNotEmpty(dto.getId())) {
            nameQc.ne("id", dto.getId());
        }
        long nameCount = planningBandService.count(nameQc);
        if (nameCount > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }


        PlanningBand bean = null;
        if (StrUtil.isBlank(dto.getId())) {
            bean = BeanUtil.copyProperties(dto, PlanningBand.class);
            planningBandService.save(bean);

        } else {
            bean = planningBandService.getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            planningBandService.updateById(bean);
        }
        List<PlanningCategory> categoryList = dto.getCategoryData();
        planningCategoryService.savePlanningCategory(bean, categoryList);

        return BeanUtil.copyProperties(bean, PlanningBandVo.class);
    }

    @ApiOperation(value = "修改坑位信息")
    @PostMapping("/updateCategoryItem")
    public PlanningCategoryItem updateCategoryItem(@RequestBody PlanningCategoryItem item) {
        planningCategoryItemService.updateById(item);
        return item;
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
//        String planningDesignNo = getMaxCode.genCode("PLANNING_DESIGN_NO", params);
        List<String> planningDesignNo1 = getMaxCode.genCode("PLANNING_DESIGN_NO", 10, params);
        return CollUtil.join(planningDesignNo1, ",");

    }

    @ApiOperation(value = "获取最大流水号")
    @PostMapping("/maxDesignNo")
    public String maxDesignNo(@RequestBody GetMaxCodeRedis data) {
        if (ObjectUtil.isEmpty(data.getValueMap())) {
            throw new OtherException("空");
        }
        List<String> regexps = new ArrayList<>(12);
        List<String> textFormats = new ArrayList<>(12);
        data.getValueMap().forEach((key, val) -> {
            if (BaseConstant.FLOWING.equals(key)) {
                textFormats.add("{0}");
            } else {
                textFormats.add(String.valueOf(val));
            }
            regexps.add(String.valueOf(val));
        });
        String regexp = "^" + CollUtil.join(regexps, "") + "$";
        System.out.println("传过来的正则:" + regexp);
        QueryWrapper qc = new QueryWrapper();
        qc.eq(COMPANY_CODE, getUserCompany());
        qc.apply(" design_no REGEXP '" + regexp + "'");
        qc.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        String maxCode = planningCategoryItemService.selectMaxDesignNo(qc);
        if (StrUtil.isBlank(maxCode)) {
            return null;
        }
        // 替换,保留流水号
        MessageFormat mf = new MessageFormat(CollUtil.join(textFormats, ""));
        try {
            Object[] parse = mf.parse(maxCode);
            if (parse != null && parse.length > 0) {
                return String.valueOf(parse[0]);
            }
            return null;
        } catch (ParseException e) {
            return null;
        }

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
        // 波段企划信息
        QueryWrapper<PlanningBand> qw = new QueryWrapper<>();
        UpdateWrapper<PlanningBand> up = new UpdateWrapper<>();

        qw.eq("planning_season_id", id);
        qw.eq(DEL_FLAG, BaseEntity.DEL_FLAG_NORMAL);
        long i = planningBandService.count(qw);
        if (i > 0) {
            throw new OtherException("存在波段企划无法删除");
        }
        return planningSeasonService.del(getUserCompany(), id);
    }

    @ApiOperation(value = "删除波段企划")
    @DeleteMapping("/planningBand")
    public boolean delPlanningBand(@Valid @NotNull(message = "编号不能为空") String id) {
        //TODO 其他校验
        return planningBandService.del(id);
    }

}
