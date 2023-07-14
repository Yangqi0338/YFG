package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.dto.ProductSeasonExpandByCategorySearchDto;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningSeasonMapper;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningCategoryService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.*;
import com.base.sbc.module.sample.vo.ChartBarVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
    private PlanningCategoryService planningCategoryService;
    @Resource
    private PlanningBandService planningBandService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;
    @Resource
    private BandService bandService;

    @Override
    public boolean del(String companyCode, String id) {
        return removeById(id);
    }

    @Override
    public List<PlanningSeason> selectProductSeason(QueryWrapper qw) {
        return getBaseMapper().selectProductSeason(qw);
    }

    @Override
    public List<PlanningSeason> queryYs(String companyCode) {
        return getBaseMapper().queryYs(companyCode);
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
    public List<PlanningSeason> getByName(String name, String userCompany) {
        QueryWrapper qc = new QueryWrapper();
        qc.eq("name", name);
        qc.eq(COMPANY_CODE, userCompany);
        List<PlanningSeason> seasons = list(qc);
        if (CollUtil.isEmpty(seasons)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        return seasons;
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
            Map<String, Long> skcCount = planningCategoryService.countSkc("planning_season_id", columnIds);
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
    public boolean checkPlanningSeasonHasBand(String id) {
        // 波段企划信息
        QueryWrapper<PlanningBand> qw = new QueryWrapper<>();
        qw.eq("planning_season_id", id);
        qw.eq(DEL_FLAG, BaseEntity.DEL_FLAG_NORMAL);
        long i = planningBandService.count(qw);
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
    public PageInfo queryByPage(PlanningSeasonSearchDto dto, String userCompany) {
        QueryWrapper qc = new QueryWrapper();
        qc.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq(COMPANY_CODE, userCompany);
        qc.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        qc.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        qc.in(CollUtil.isNotEmpty(dto.getYearList()), "year", dto.getYearList());
        qc.in(CollUtil.isNotEmpty(dto.getSeasonList()), "season", dto.getSeasonList());
        qc.like(StrUtil.isNotBlank(dto.getSearch()), "name", dto.getSearch());
        qc.in(CollUtil.isNotEmpty(dto.getBrandList()), "brand", dto.getBrandList());
        dto.setOrderBy("create_date desc ");
        qc.select("*");
        Page<PlanningSeason> objects = PageHelper.startPage(dto);
        selectProductSeason(qc);
        PageInfo<PlanningSeason> planningSeasonPageInfo = objects.toPageInfo();
        return planningSeasonPageInfo;
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
        List<PlanningSeason> seasons = getByName(name, getCompanyCode());
        if (CollUtil.isNotEmpty(seasons)) {
            PlanningSeasonVo planningSeasonVo = BeanUtil.copyProperties(seasons.get(0), PlanningSeasonVo.class);
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
        brandTotalQw.select("band_code as name,count(1) as total");
        brandTotalQw.groupBy("name");
        planningSummaryQw(brandTotalQw, dto);
        List<DimensionTotalVo> bandTotal = planningCategoryItemService.dimensionTotal(brandTotalQw);
        vo.setBandTotal(PlanningUtils.removeEmptyAndSort(bandTotal));
        Map<String, String> bandNames = new HashMap<>(4);
        if (CollUtil.isNotEmpty(bandTotal)) {
            bandNames = bandService.getNamesByCodes(bandTotal.stream().map(DimensionTotalVo::getName).collect(Collectors.joining(StrUtil.COMMA)));
            for (DimensionTotalVo dimensionTotalVo : bandTotal) {
                dimensionTotalVo.setName(bandNames.getOrDefault(dimensionTotalVo.getName(), dimensionTotalVo.getName()));
            }
        }

        //查询品类统计
        QueryWrapper categoryQw = new QueryWrapper();
        categoryQw.select("prod_category as name,count(1) as total");
        categoryQw.groupBy("name");
        planningSummaryQw(categoryQw, dto);
        List<DimensionTotalVo> categoryTotal = planningCategoryItemService.dimensionTotal(categoryQw);
        ccmFeignService.setCategoryName(categoryTotal, "name", "name");
        vo.setCategoryTotal(PlanningUtils.removeEmptyAndSort(categoryTotal));
        //查询明细
        QueryWrapper detailQw = new QueryWrapper();
        planningSummaryQw(detailQw, dto);
        List<PlanningSummaryDetailVo> detailVoList = planningCategoryItemService.planningSummaryDetail(detailQw);
        if (CollUtil.isNotEmpty(detailVoList)) {
            for (PlanningSummaryDetailVo planningSummaryDetailVo : detailVoList) {
                planningSummaryDetailVo.setBandCode(bandNames.getOrDefault(planningSummaryDetailVo.getBandCode(), planningSummaryDetailVo.getBandCode()));
            }
            amcFeignService.setUserAvatarToList(detailVoList);
            ccmFeignService.setCategoryName(detailVoList, "prodCategory", "prodCategory");
            Map<String, List<PlanningSummaryDetailVo>> seatData = detailVoList.stream().collect(Collectors.groupingBy(k -> k.getProdCategory() + StrUtil.DASHED + k.getBandCode()));
            vo.setSeatData(seatData);
        }
        return vo;
    }

    @Override
    public List categorySummary(PlanningBoardSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        planningSummaryQw(qw, dto);
        List result = new ArrayList();
        result.add(CollUtil.newArrayList("product", "总数"));
        List<ChartBarVo> data = planningCategoryItemService.categorySummary(qw);
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
    /**
     * 获取产品季品类树
     *
     * @param userCompany
     */
    @Override
    public List<ProductCategoryTreeVo> getProductCategoryTree(String userCompany) {

        QueryWrapper qc = new QueryWrapper();
        qc.eq("company_code",userCompany);
        /*查询到的产品季*/
        List<PlanningSeason> planningSeasonList = baseMapper.selectList(qc);
        /*返回的数据*/
        List<ProductCategoryTreeVo> list = null;
        if(!CollectionUtils.isEmpty(planningSeasonList)){
            list= BeanUtil.copyToList(planningSeasonList, ProductCategoryTreeVo.class);
            list.forEach(productCategoryTreeVo -> {
                ProductSeasonExpandByCategorySearchDto productSeasonExpandByCategorySearchDto=new ProductSeasonExpandByCategorySearchDto();
                /*查询产品下的品类*/
                productSeasonExpandByCategorySearchDto.setPlanningSeasonId(productCategoryTreeVo.getId());
                List<BasicStructureTreeVo> basicStructureTreeVoList =  planningCategoryItemService.expandByCategory(productSeasonExpandByCategorySearchDto);
                if(!CollectionUtils.isEmpty(basicStructureTreeVoList)){
                    productCategoryTreeVo.setChildren(basicStructureTreeVoList);
                }
            });
        }
        return list;
    }


    private void planningSummaryQw(QueryWrapper<T> qw, PlanningBoardSearchDto dto) {
        qw.eq(StrUtil.isNotEmpty(dto.getPlanningSeasonId()), "ci.planning_season_id", dto.getPlanningSeasonId());
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("ci.design_no", dto.getSearch()).or().like("ci.his_design_no", dto.getSearch()));
        qw.in(StrUtil.isNotEmpty(dto.getBandCode()), "b.band_code", StrUtil.split(dto.getBandCode(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getMonth()), "b.month", StrUtil.split(dto.getMonth(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getProdCategoryId()), "ci.prod_category", StrUtil.split(dto.getProdCategoryId(), CharUtil.COMMA));

    }
}
