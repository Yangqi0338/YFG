package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.amc.utils.AmcUtils;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.dto.PlanningBandDto;
import com.base.sbc.module.planning.dto.PlanningBandSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningCategoryService;
import com.base.sbc.module.planning.vo.PlanningBandVo;
import com.base.sbc.module.planning.vo.PlanningSeasonBandVo;
import com.base.sbc.module.planning.vo.PlanningSeasonVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import feign.Body;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    @Resource
    private PlanningBandService planningBandService;
    @Resource
    private PlanningCategoryService planningCategoryService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;
    @Resource
    private AmcService amcService;
    private IdGen idGen = new IdGen();

    @ApiOperation(value = "保存产品季", notes = "")
    @PostMapping
    public PlanningSeasonVo save(@Valid @RequestBody PlanningSeasonSaveDto dto) {
        // 校验名称重复
        QueryCondition nameQc = new QueryCondition(getUserCompany())
                .andEqualTo("name", dto.getName());
        if (StrUtil.isNotEmpty(dto.getId())) {
            nameQc.andNotEqualTo("id", dto);
        }
        int nameCount = planningSeasonService.
                selectOne2("countByQc", nameQc);
        if (nameCount > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }


        PlanningSeason bean = null;
        int insert = 0;
        if (StrUtil.isEmpty(dto.getId())) {
            bean = BeanUtil.copyProperties(dto, PlanningSeason.class);
            //保存
            bean.preInsert();
            bean.setCompanyCode(getUserCompany());
            bean.setStatus(BaseGlobal.STATUS_NORMAL);
            insert = planningSeasonService.insert(bean);
        } else {
            bean = planningSeasonService.getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            insert = planningSeasonService.updateAll(bean);
        }
        if (insert > 0) {
            return BeanUtil.copyProperties(bean, PlanningSeasonVo.class);
        }
        throw new OtherException(BaseErrorEnum.ERR_INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "查询产品季-通过季名称查询")
    @GetMapping("/getByName")
    public PlanningSeasonVo getByName(@NotNull(message = "名称不能为空") String name) {
        QueryCondition qc = new QueryCondition(getUserCompany());
        qc.andEqualTo("name", name);
        qc.andEqualTo("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        List<PlanningSeason> seasons = planningSeasonService.findByCondition(qc);
        if (CollUtil.isEmpty(seasons)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        return BeanUtil.copyProperties(seasons.get(0), PlanningSeasonVo.class);

    }

    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo<PlanningSeasonVo> query(PlanningSeasonSearchDto dto) {

        QueryCondition qc = new QueryCondition(getUserCompany());
        qc.andEqualTo("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        if (StrUtil.isNotBlank(dto.getSearch())) {
            qc.andLikeOr(dto.getSearch(), "name");
        }
        if (StrUtil.isNotBlank(dto.getYear())) {
            qc.andEqualTo("year", dto.getYear());
        }
        if (StrUtil.isNotBlank(dto.getOrder())) {
            qc.setOrderByClause(dto.getOrder());
        } else {
            qc.setOrderByClause("create_date desc");
        }
        PageHelper.startPage(dto);
        List<PlanningSeason> list = planningSeasonService.selectList2("select", qc);
        if (CollUtil.isNotEmpty(list)) {
            //查询用户信息
            String companyUserInfoByUserIds = amcService.getCompanyUserInfoByUserIds(list.stream().map(PlanningSeason::getCreateId).collect(Collectors.joining(",")));
            Map<String, UserCompany> userMap = AmcUtils.parseStrToMap(companyUserInfoByUserIds);
            List<PlanningSeasonVo> result = list.stream().map(item -> {
                        PlanningSeasonVo vo = BeanUtil.copyProperties(item, PlanningSeasonVo.class);
                        vo.setAliasUserAvatar(Optional.ofNullable(userMap.get(item.getCreateId()))
                                .map(u -> u.getAliasUserAvatar()).orElse(null));
                        return vo;
                    })
                    .collect(Collectors.toList());
            PageInfo<PlanningSeasonVo> pageInfo = new PageInfo<>(result);
            return pageInfo;
        }

        return new PageInfo<>();
    }
    @ApiOperation(value = "查询波段企划-分页查询")
    @GetMapping("/planBand")
    public PageInfo<PlanningSeasonBandVo> bandList(@Valid PlanningBandSearchDto dto) {
        // 1 查询产品季
        QueryCondition qc = new QueryCondition();
        qc.andConditionSql("b.planning_season_id=s.id");
        qc.andEqualTo("s.name", dto.getSeasonName());
        qc.andEqualTo("s.company_code", getUserCompany());
        qc.andEqualTo("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        PageHelper.startPage(dto);
        List<PlanningSeasonBandVo> list = planningBandService.selectList2("selectByQc", qc);
        if (CollUtil.isNotEmpty(list)) {
            //查询用户信息
            String companyUserInfoByUserIds = amcService.getCompanyUserInfoByUserIds(
                    list.stream().map(PlanningSeasonBandVo::getBand).map(PlanningBandVo::getCreateId).collect(Collectors.joining(",")));
            Map<String, UserCompany> userMap = AmcUtils.parseStrToMap(companyUserInfoByUserIds);
            list.forEach(item -> {
                PlanningBandVo band = item.getBand();
                band.setAliasUserAvatar(Optional.ofNullable(userMap.get(band.getCreateId()))
                        .map(u -> u.getAliasUserAvatar()).orElse(null));

            });
            PageInfo<PlanningSeasonBandVo> pageInfo = new PageInfo<>(list);
            return pageInfo;
        }

        return new PageInfo<>();
    }

    @ApiOperation(value = "查询波段企划-通过产品季和波段企划名称")
    @GetMapping("/planBand/getByName")
    public PlanningSeasonBandVo getBandByName(@NotNull(message = "产品季名称不能为空") String planningSeasonName,
                                                   @NotNull(message = "波段企划名称不能为空") String planningBandName) {
        QueryCondition qc = new QueryCondition();
        qc.andConditionSql("b.planning_season_id=s.id");
        qc.andEqualTo("s.company_code", getUserCompany());
        qc.andEqualTo("s.name", planningSeasonName);
        qc.andEqualTo("b.name", planningBandName);
        qc.andEqualTo("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.andEqualTo("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        List<PlanningSeasonBandVo> list = planningBandService.selectList2("selectByQc", qc);
        PlanningSeasonBandVo first = CollUtil.getFirst(list);
        //查询品类列表
        QueryCondition categoryQc=new QueryCondition(getUserCompany());
        categoryQc.andEqualTo("planning_season_id",first.getSeason().getId());
        categoryQc.andEqualTo("planning_band_id",first.getBand().getId());
        categoryQc.andEqualTo("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        List<PlanningCategory> categoryData = planningCategoryService.findByCondition(categoryQc);
        first.getBand().setCategoryData(categoryData);
        //查询坑位信息
        List<PlanningCategoryItem> categoryItemData = planningCategoryItemService.findByCondition(categoryQc);
        first.getBand().setCategoryItemData(categoryItemData);
        return first;
    }

    @ApiOperation(value = "保存波段企划")
    @PostMapping("/planBand")
    public PlanningBandVo savePlanBand(@Valid @RequestBody PlanningBandDto dto) {
        // 校验名称重复
        QueryCondition nameQc = new QueryCondition(getUserCompany())
                .andEqualTo("name", dto.getName())
                .andEqualTo("planning_season_id",dto.getPlanningSeasonId())
                ;
        if (StrUtil.isNotEmpty(dto.getId())) {
            nameQc.andNotEqualTo("id", dto.getId());
        }
        int nameCount = planningBandService.
                selectOne2("countByQc", nameQc);
        if (nameCount > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }


        PlanningBand bean = null;
        if (StrUtil.isBlank(dto.getId())) {
            bean = BeanUtil.copyProperties(dto, PlanningBand.class);
            bean.preInsert(idGen.nextIdStr());
            bean.setCompanyCode(getUserCompany());
            planningBandService.insert(bean);

        } else {
            bean = planningBandService.getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            bean.preUpdate();
            planningBandService.updateAll(bean);
        }
        List<PlanningCategory> categoryList = dto.getCategoryData();
        if (CollUtil.isNotEmpty(categoryList)) {
            planningCategoryService.savePlanningCategory(bean,categoryList);

        }
        return BeanUtil.copyProperties(bean, PlanningBandVo.class);
    }

    @ApiOperation(value = "修改坑位信息")
    @PostMapping("/updateCategoryItem")
    public PlanningCategoryItem updateCategoryItem(@RequestBody PlanningCategoryItem item){
         item.preUpdate();
         planningCategoryItemService.updateAll(item);
        return item;
    }
}
