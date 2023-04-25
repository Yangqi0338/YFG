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
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.*;
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
 * 类描述：商品企划 相关接口
 * @address com.base.sbc.module.planning.controller.PlanningController
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 13:47
 * @version 1.0
 */
@RestController
@Api(tags = "商品企划 相关接口")
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
        qc.like(StrUtil.isNotBlank(dto.getSearch()), "name",dto.getSearch());
        qc.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        dto.setOrderBy("create_date desc ");
        Page<PlanningSeason> objects = PageHelper.startPage(dto);
        planningSeasonService.list(qc);
        PageInfo<PlanningSeason> planningSeasonPageInfo = objects.toPageInfo();
        List<PlanningSeason> list = planningSeasonPageInfo.getList();
        if (CollUtil.isNotEmpty(list)) {
            List<String> userIds=new ArrayList<>(16);
            List<String> columnIds=new ArrayList<>(16);
            for (PlanningSeason item : list) {
                userIds.add(item.getCreateId());
                columnIds.add(item.getId());
            }
            //查询用户信息
            Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(CollUtil.join(userIds,","));
            List<PlanningSeasonVo> volist = BeanUtil.copyToList(list, PlanningSeasonVo.class);
            // 查询skc 数
            Map<String, Long> skcCount=planningCategoryService.countSkc("planning_season_id",columnIds);
            for (PlanningSeasonVo planningSeasonVo : volist) {
                planningSeasonVo.setAliasUserAvatar(userAvatarMap.get(planningSeasonVo.getCreateId()));
                planningSeasonVo.setSkcCount(skcCount.get(planningSeasonVo.getId()));
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
        qc.like(StrUtil.isNotBlank(dto.getSearch()), "b.name", dto.getSearch());
        qc.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qc.eq("s.company_code", getUserCompany());
        qc.eq("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("s.name", dto.getPlanningSeasonName());
        dto.setOrderBy("b.create_date desc ");
        PageHelper.startPage(dto);
        List<PlanningSeasonBandVo> list = planningBandService.selectByQw(qc);
        if (CollUtil.isNotEmpty(list)) {
            List<String> userIds=new ArrayList<>(16);
            List<String> columnIds=new ArrayList<>(16);
            for (PlanningSeasonBandVo item : list) {
                PlanningBandVo band = item.getBand();
                userIds.add(band.getCreateId());
                columnIds.add(band.getId());
            }
            //查询用户信息
            Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(CollUtil.join(userIds,","));
            // skc 数
            // 查询skc 数
            Map<String, Long> skcCount=planningCategoryService.countSkc("planning_band_id",columnIds);
            list.forEach(item -> {
                PlanningBandVo band = item.getBand();
                band.setAliasUserAvatar(userAvatarMap.get(band.getCreateId()));
                band.setSkcCount(skcCount.get(band.getId()));
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
        List<PlanningCategoryItemMaterial> itemMaterials = planningCategoryItemMaterialService.list(categoryQc);

        if (CollUtil.isNotEmpty(categoryItemData) && CollUtil.isNotEmpty(categoryData)) {
            List<PlanningCategoryItemVo> categoryItemVoData = new ArrayList<>(categoryData.size());
            Map<String, PlanningCategory> planningCategoryMap = categoryData.stream().collect(Collectors.toMap(PlanningCategory::getId, v -> v, (a, b) -> b));
            Map<String, List<PlanningCategoryItemMaterial>> itemMaterialMap = Optional.ofNullable(itemMaterials).orElse(CollUtil.newArrayList()).stream().collect(Collectors.groupingBy(PlanningCategoryItemMaterial::getPlanningCategoryItemId));
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
            bean.setStatus(Optional.ofNullable(dto.getStatus()).orElse(BaseGlobal.STATUS_NORMAL));
        } else {
            bean = planningBandService.getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            planningBandService.updateById(bean);
            bean.setStatus(Optional.ofNullable(dto.getStatus()).orElse(BaseGlobal.STATUS_NORMAL));
        }
        List<PlanningCategory> categoryList = dto.getCategoryData();
        planningCategoryService.savePlanningCategory(bean, categoryList);

        return BeanUtil.copyProperties(bean, PlanningBandVo.class);
    }

    @ApiOperation(value = "修改坑位信息/提交")
    @PostMapping("/updateCategoryItem")
    public List<PlanningCategoryItem> updateCategoryItem(@RequestParam(value = "planningBandId",required = false) String planningBandId,  @RequestBody List<PlanningCategoryItem> item) {
        planningCategoryItemService.updateBatchById(item);
        if(StrUtil.isNotBlank(planningBandId)){
            PlanningBand byId = planningBandService.getById(planningBandId);
            if(byId==null){
                throw  new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            byId.setStatus(BaseGlobal.STOCK_STATUS_CHECKED);
           planningBandService.updateById(byId);
        }
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

    @ApiOperation(value = "保存关联的素材库")
    @PostMapping("/savePlanningCategoryItemMaterial")
    public boolean savePlanningCategoryItemMaterial(@RequestBody PlanningCategoryItemMaterialSaveDto dto){
        // 删除之前的
        QueryWrapper<PlanningCategoryItemMaterial> qw=new QueryWrapper<>();
        qw.eq("planning_category_item_id",dto.getId());
        planningCategoryItemMaterialService.remove(qw);
        //保存
        if(CollUtil.isNotEmpty(dto.getItem())){
            planningCategoryItemMaterialService.saveBatch(dto.getItem());
        }
        //修改关联数量
        UpdateWrapper<PlanningCategoryItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",dto.getId());
        updateWrapper.set("material_count",Optional.ofNullable(dto.getItem()).map(List::size).orElse(0));
        planningCategoryItemService.update(updateWrapper);

        return true;
    }




}
