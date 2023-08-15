package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningSeasonMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.*;
import com.base.sbc.module.style.vo.ChartBarVo;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：企划-产品季 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningSeasonService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:08
 */
@Service
public class PlanningSeasonServiceImpl extends BaseServiceImpl<PlanningSeasonMapper, PlanningSeason> implements PlanningSeasonService {
    @Resource
    private AmcFeignService amcFeignService;
    @Resource
    private AmcService amcService;
    @Resource
    private CcmFeignService ccmFeignService;


    @Resource
    private PlanningChannelService planningChannelService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;
    @Resource
    private BandService bandService;

    @Override
    public boolean del(String companyCode, String id) {
        return removeById(id);
    }


    @Override
    public void checkNameRepeat(PlanningSeasonSaveDto dto, String userCompany) {
        QueryWrapper nameQc = new QueryWrapper();
        nameQc.eq(COMPANY_CODE, userCompany);
        nameQc.eq("name", dto.getName());
        nameQc.ne(StrUtil.isNotEmpty(dto.getId()), "id", dto.getId());
        long nameCount = count(nameQc);
        if (nameCount > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public PlanningSeason savePlanningSeason(PlanningSeasonSaveDto dto) {
        PlanningSeason bean = null;
        if (StrUtil.isEmpty(dto.getId())) {
            bean = BeanUtil.copyProperties(dto, PlanningSeason.class);
            bean.setStatus(BaseGlobal.STATUS_NORMAL);
            save(bean);
        } else {
            bean = getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            updateById(bean);
        }
        return bean;
    }

    @Override
    public PlanningSeason getByName(String name, String userCompany) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("name", name);
        qw.eq(COMPANY_CODE, userCompany);
        qw.last("limit 1");
        return getOne(qw);
    }

    @Override
    public PageInfo<PlanningSeasonVo> queryPlanningSeasonPageInfo(PlanningSeasonSearchDto dto, String userCompany) {
        QueryWrapper qc = new QueryWrapper();
        qc.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq(COMPANY_CODE, userCompany);
        qc.like(StrUtil.isNotBlank(dto.getSearch()), "name", dto.getSearch());
        qc.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        qc.in(CollUtil.isNotEmpty(dto.getYearList()), "year", dto.getYearList());
        qc.in(CollUtil.isNotEmpty(dto.getSeasonList()), "season", dto.getSeasonList());
        qc.in(CollUtil.isNotEmpty(dto.getBrandList()), "brand", dto.getBrandList());
        dto.setOrderBy("create_date desc ");
        Page<PlanningSeason> objects = PageHelper.startPage(dto);
        list(qc);
        PageInfo<PlanningSeason> planningSeasonPageInfo = objects.toPageInfo();
        List<PlanningSeason> list = planningSeasonPageInfo.getList();
        if (CollUtil.isNotEmpty(list)) {
            List<String> userIds = new ArrayList<>(16);
            List<String> columnIds = new ArrayList<>(16);
            for (PlanningSeason item : list) {
                userIds.add(item.getCreateId());
                columnIds.add(item.getId());
            }
            //查询用户信息
            Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(CollUtil.join(userIds, ","));
            List<PlanningSeasonVo> volist = BeanUtil.copyToList(list, PlanningSeasonVo.class);
            // 查询skc 数
            Map<String, Long> skcCount = planningCategoryItemService.totalSkcByPlanningSeason(columnIds);
            for (PlanningSeasonVo planningSeasonVo : volist) {
                planningSeasonVo.setAliasUserAvatar(userAvatarMap.get(planningSeasonVo.getCreateId()));
                planningSeasonVo.setSkcCount(skcCount.get(planningSeasonVo.getId()));
            }
            PageInfo<PlanningSeasonVo> pageInfoVO = new PageInfo<>();
            pageInfoVO.setList(volist);
            BeanUtil.copyProperties(planningSeasonPageInfo, pageInfoVO, "list");
            return pageInfoVO;
        }
        return null;
    }

    @Override
    public boolean checkPlanningSeasonHasSub(String id) {
        // 波段企划信息
        QueryWrapper<PlanningChannel> qw = new QueryWrapper<>();
        qw.eq("planning_season_id", id);
        qw.eq(DEL_FLAG, BaseEntity.DEL_FLAG_NORMAL);
        long i = planningChannelService.count(qw);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<AdTree> getTree() {
        List<AdTree> list = amcService.getAdList();
        for (AdTree adTree : list) {
            List<AdTree> adTrees = new ArrayList<>();
            List<PlanningSeason> seasonList = list();
            for (PlanningSeason planningSeason : seasonList) {
                if (planningSeason.getCompanyCode().equals(adTree.getId())) {
                    AdTree tree = new AdTree();
                    tree.setTitle(planningSeason.getName());
                    tree.setKey(planningSeason.getId());
                    tree.setId(planningSeason.getId());
                    adTrees.add(tree);
                }
            }
            adTree.setChildren(adTrees);
        }
        return list;
    }



    @Override
    public void checkBYSRepeat(PlanningSeasonSaveDto dto, String userCompany) {
        QueryWrapper nameQc = new QueryWrapper();
        nameQc.eq(COMPANY_CODE, userCompany);
        nameQc.eq("brand", dto.getBrand());
        nameQc.eq("year", dto.getYear());
        nameQc.eq("season", dto.getSeason());
        nameQc.ne(StrUtil.isNotEmpty(dto.getId()), "id", dto.getId());
        long nameCount = count(nameQc);
        if (nameCount > 0) {
            throw new OtherException("该品牌已经存在此年份、季节");
        }
    }

    @Override
    public boolean delPlanningSeason(String id) {
        removeById(id);
        return true;
    }

    @Override
    public PlanningSeasonVo getByName(String name) {
        PlanningSeason season = getByName(name, getCompanyCode());
        if (season != null) {
            PlanningSeasonVo planningSeasonVo = BeanUtil.copyProperties(season, PlanningSeasonVo.class);
            planningSeasonVo.setTeamList(amcFeignService.getTeamBySeasonId(planningSeasonVo.getId()));
            return planningSeasonVo;
        }
        return null;
    }

    @Override
    public List<SelectOptionsVo> getPlanningSeasonOptions(String userCompany) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(StrUtil.isNotBlank(userCompany), "COMPANY_CODE", userCompany);
        qw.eq("del_flag", BaseGlobal.NO);
        return getBaseMapper().getPlanningSeasonOptions(qw);
    }

    @Override
    public PlanningSummaryVo planningSummary(PlanningBoardSearchDto dto) {
        PlanningSummaryVo vo = new PlanningSummaryVo();
        //查询波段统计
        QueryWrapper brandTotalQw = new QueryWrapper();
        brandTotalQw.select("band_name as name,count(1) as total");
        brandTotalQw.groupBy("band_name");
        planningSummaryQw(brandTotalQw, dto);
        List<DimensionTotalVo> bandTotal = planningCategoryItemService.dimensionTotal(brandTotalQw);
        vo.setBandTotal(PlanningUtils.removeEmptyAndSort(bandTotal));


        //查询品类统计
        QueryWrapper categoryQw = new QueryWrapper();
        categoryQw.select("prod_category_name as name,count(1) as total");
        categoryQw.groupBy("prod_category_name");
        planningSummaryQw(categoryQw, dto);
        List<DimensionTotalVo> categoryTotal = planningCategoryItemService.dimensionTotal(categoryQw);
        vo.setCategoryTotal(PlanningUtils.removeEmptyAndSort(categoryTotal));
        //查询明细
        QueryWrapper detailQw = new QueryWrapper();
        planningSummaryQw(detailQw, dto);
        List<PlanningSummaryDetailVo> detailVoList = planningCategoryItemService.planningSummaryDetail(detailQw);
        if (CollUtil.isNotEmpty(detailVoList)) {
            amcFeignService.setUserAvatarToList(detailVoList);
            Map<String, List<PlanningSummaryDetailVo>> seatData = detailVoList.stream().collect(Collectors.groupingBy(k -> k.getProdCategoryName() + StrUtil.DASHED + k.getBandName()));
            vo.setSeatData(seatData);
        }
        return vo;
    }

    @Override
    public List bandSummary(PlanningBoardSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        planningSummaryQw(qw, dto);
        List result = new ArrayList();
        result.add(CollUtil.newArrayList("product", "总数"));
        List<ChartBarVo> data = planningCategoryItemService.bandSummary(qw);
        if (CollUtil.isNotEmpty(data)) {
            data.forEach(item -> {
                result.add(CollUtil.newArrayList(item.getDimension(), item.getTotal()));
            });
        }
        return result;
    }

    @Override
    public PlanningSummaryDetailVo hisDetail(String hisDesignNo) {
        QueryWrapper<PlanningSummaryDetailVo> detailQw = new QueryWrapper();
        detailQw.eq("ci.design_no", hisDesignNo).or().eq("style_no", hisDesignNo);
        detailQw.last("limit 1");
        List<PlanningSummaryDetailVo> detailVoList = planningCategoryItemService.planningSummaryDetail(detailQw);
        if (CollUtil.isNotEmpty(detailVoList)) {
            return detailVoList.get(0);
        }
        return null;
    }


    @Override
    public List<YearSeasonBandVo> queryYearBrandTree(YearSeasonBandVo vo) {
        // 查询波段
        if (vo.getLevel() == 1) {
            Map<String, Long> sckCountMap = planningCategoryItemService.totalBandSkcByPlanningSeason(vo.getPlanningSeasonId());
            return sckCountMap.entrySet().stream().map(b -> {
                YearSeasonBandVo bv = BeanUtil.copyProperties(vo, YearSeasonBandVo.class);
                bv.setLevel(2);
                bv.setBandName(b.getKey());
                bv.setTotal(b.getValue());
                return bv;
            }).collect(Collectors.toList());
        } else {
            QueryWrapper<PlanningSeason> qw = new QueryWrapper<>();
            qw.eq(COMPANY_CODE, getCompanyCode());
            qw.lambda().isNotNull(PlanningSeason::getYearName).isNotNull(PlanningSeason::getBrandName);
            qw.orderByDesc("year_name");
            //查询所有产品季
            List<PlanningSeason> seasonList = list(qw);
            if (CollUtil.isEmpty(seasonList)) {
                return new ArrayList<>();
            }
            //统计产品季skc数量
            // Map<String, Long> sckCountMap = planningCategoryItemService.totalSkcByPlanningSeason(null);
            Map<String, Long> sckCountMap = planningChannelService.countByPlanningSeason();
            List<YearSeasonBandVo> slist = seasonList.stream().map(s -> {
                YearSeasonBandVo v = new YearSeasonBandVo();
                v.setPlanningSeasonId(s.getId());
                v.setPlanningSeasonName(s.getName());
                v.setBrand(s.getBrand());
                v.setTotal(sckCountMap.getOrDefault(s.getId(), 0L));
                v.setChildren(StrUtil.isNotBlank(vo.getHasBand()));
                v.setYearName(s.getYearName());
                v.setLevel(1);
                return v;
            }).collect(Collectors.toList());
            List<YearSeasonBandVo> result = new ArrayList<>(16);
            Map<String, List<YearSeasonBandVo>> seasonMap = slist.stream().collect(Collectors.groupingBy(YearSeasonBandVo::getYearName, LinkedHashMap::new, Collectors.toList()));
            for (Map.Entry<String, List<YearSeasonBandVo>> season : seasonMap.entrySet()) {
                YearSeasonBandVo item = new YearSeasonBandVo();
                List<YearSeasonBandVo> value = season.getValue();
                YearSeasonBandVo planningSeason = value.get(0);
                item.setYearName(planningSeason.getYearName());
                item.setTotal(value.size());
                item.setChildren(value);
                item.setLevel(0);
                item.setBrand(planningSeason.getBrand());
                result.add(item);
            }
            return result;
        }

    }

    @Override
    public List<ProductSeasonSelectVO> getByYear(String year) {
        LambdaQueryWrapper<PlanningSeason> queryWrapper = new QueryWrapper<PlanningSeason>()
                .lambda()
                .eq(PlanningSeason::getCompanyCode, getCompanyCode())
                .eq(PlanningSeason::getYear, year)
                .select(PlanningSeason::getId, PlanningSeason::getName);
        List<PlanningSeason> planningSeasons = super.list(queryWrapper);
        return CollectionUtils.isEmpty(planningSeasons) ? Lists.newArrayList() : CopyUtil.copy(planningSeasons, ProductSeasonSelectVO.class);
    }


    private void planningSummaryQw(QueryWrapper<T> qw, PlanningBoardSearchDto dto) {
        qw.eq(StrUtil.isNotEmpty(dto.getPlanningSeasonId()), "ci.planning_season_id", dto.getPlanningSeasonId());
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("ci.design_no", dto.getSearch()).or().like("ci.his_design_no", dto.getSearch()));
        qw.in(StrUtil.isNotEmpty(dto.getBandCode()), "ci.band_code", StrUtil.split(dto.getBandCode(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getMonth()), "ci.month", StrUtil.split(dto.getMonth(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getProdCategory()), "ci.prod_category", StrUtil.split(dto.getProdCategory(), CharUtil.COMMA));

    }




}
