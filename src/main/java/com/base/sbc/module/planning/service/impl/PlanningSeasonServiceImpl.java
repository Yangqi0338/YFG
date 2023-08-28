package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
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
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.formType.mapper.FieldManagementMapper;
import com.base.sbc.module.formType.mapper.FieldValMapper;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMaterialMapper;
import com.base.sbc.module.planning.mapper.PlanningSeasonMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningDemandService;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private PlanningCategoryItemMapper planningCategoryItemMapper;
    @Resource
    private BandService bandService;
    @Resource
    private PlanningDemandService planningDemandService;
    @Resource
    private FieldManagementMapper fieldManagementMapper;

    @Resource
    private FieldValMapper fieldValMapper;
    @Resource
    private PlanningCategoryItemMaterialMapper planningCategoryItemMaterialMapper;
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
        qw.orderByDesc("name");
        return getBaseMapper().getPlanningSeasonOptions(qw);
    }

    @Override
    public PlanningSummaryVo planningSummary(PlanningBoardSearchDto dto) {
        PlanningSummaryVo vo = new PlanningSummaryVo();

        //查询明细
        QueryWrapper detailQw = new QueryWrapper();
        planningSummaryQw(detailQw, dto);
        List<PlanningSummaryDetailVo> detailVoList = planningCategoryItemService.planningSummaryDetail(detailQw);
        if (CollUtil.isEmpty(detailVoList)) {
            return null;
        }
        //  默认y 轴为品类,x为波段
        if (StrUtil.isBlank(dto.getFieldId()) || StrUtil.isBlank(dto.getProdCategory())) {

            Map<String, Long> xMap = new HashMap<>(16);
            Map<String, Long> yMap = new HashMap<>(16);
            for (PlanningSummaryDetailVo dv : detailVoList) {
                if (xMap.containsKey(dv.getBandName())) {
                    Long aLong = xMap.get(dv.getBandName());
                    xMap.put(dv.getBandName(), aLong + 1);
                } else {
                    xMap.put(dv.getBandName(), 1L);
                }
                if (yMap.containsKey(dv.getProdCategoryName())) {
                    Long aLong = yMap.get(dv.getProdCategoryName());
                    yMap.put(dv.getProdCategoryName(), aLong + 1);
                } else {
                    yMap.put(dv.getProdCategoryName(), 1L);
                }
            }
            vo.setXList(xMap.entrySet().stream().map(item -> {
                DimensionTotalVo dtv = new DimensionTotalVo();
                dtv.setName(item.getKey());
                dtv.setTotal(item.getValue());
                return dtv;
            }).collect(Collectors.toList()));
            vo.setYList(yMap.entrySet().stream().map(item -> {
                DimensionTotalVo dtv = new DimensionTotalVo();
                dtv.setName(item.getKey());
                dtv.setTotal(item.getValue());
                return dtv;
            }).collect(Collectors.toList()));
            amcFeignService.setUserAvatarToList(detailVoList);
            Map<String, List<PlanningSummaryDetailVo>> seatData = detailVoList.stream().collect(Collectors.groupingBy(k -> k.getBandName() + StrUtil.DASHED + k.getProdCategoryName()));
            vo.setXyData(seatData);
        } else {
            //维度统计数据
            List<List<String>> demandSummary=CollUtil.newArrayList(
                    //维度
                    CollUtil.newArrayList(),
                    CollUtil.newArrayList("企划skc数"),
                    CollUtil.newArrayList("企划占比"),
                    CollUtil.newArrayList("下单skc"),
                    CollUtil.newArrayList("下单占比"),
                    CollUtil.newArrayList("缺口"));
            //当选择维度时，y轴为维度 （企划需求管理的需求占比）
            QueryDemandDto queryDemandDto = new QueryDemandDto();
            queryDemandDto.setPlanningSeasonId(dto.getPlanningSeasonId());
            queryDemandDto.setCategoryId(dto.getProdCategory());
            queryDemandDto.setFieldId(dto.getFieldId());
            List<PlanningDemandVo> demandList = planningDemandService.getDemandListById(queryDemandDto);
            HashMap<String,List<String>> yValueS = new LinkedHashMap();
            AtomicInteger dTotal= new AtomicInteger(0);
            if (CollUtil.isNotEmpty(demandList) && CollUtil.isNotEmpty(demandList.get(0).getList())) {
                PlanningDemandVo planningDemandVo = demandList.get(0);
                demandSummary.get(0).add(planningDemandVo.getDemandName());
                List<PlanningDemandProportionData> list = planningDemandVo.getList();
                List<DimensionTotalVo> yList = list.stream().map(e -> {
                    DimensionTotalVo yvo = new DimensionTotalVo();
                    yvo.setTotal(Optional.ofNullable(e.getNum()).map(Integer::longValue).orElse(0L));
                    yvo.setName(e.getClassifyName());
                    dTotal.set(dTotal.get() + Optional.ofNullable(e.getNum()).orElse(0));
                    demandSummary.get(0).add(e.getClassifyName());
                    demandSummary.get(1).add(String.valueOf(yvo.getTotal()));
                    demandSummary.get(2).add(Optional.ofNullable(e.getProportion()).map(a->a.toString()).orElse(""));
                    yValueS.put(e.getClassifyName(),CollUtil.newArrayList());
                    return yvo;
                }).collect(Collectors.toList());
                vo.setYList(PlanningUtils.removeEmptyAndSort(yList));

                if (CollUtil.isNotEmpty(detailVoList)) {
                    List<String> seatIds = detailVoList.stream().map(PlanningSummaryDetailVo::getId).collect(Collectors.toList());
                    FieldManagement fieldManagement = fieldManagementMapper.selectById(planningDemandVo.getFieldId());
                    QueryWrapper<FieldVal> fvQw = new QueryWrapper<>();
                    fvQw.in("foreign_id", seatIds);
                    fvQw.eq("field_name", fieldManagement.getFieldName());
                    List<FieldVal> fieldVals = fieldValMapper.selectList(fvQw);
                    Map<String, FieldVal> seatFvMap = Opt.ofNullable(fieldVals).map(fvs -> {
                        return fvs.stream().collect(Collectors.toMap(k -> k.getForeignId(), v -> v, (a, b) -> a));
                    }).orElse(MapUtil.empty());
                    Map<String, List<PlanningSummaryDetailVo>> seatData = new HashMap<>(16);
                    Map<String, Long> xMap = new HashMap<>(16);
                    for (PlanningSummaryDetailVo psdv : detailVoList) {
                        String dv = Optional.ofNullable(seatFvMap.get(psdv.getId())).map(FieldVal::getValName).orElse("_null_");
                        //维度没匹配跳过
                        if (!yValueS.containsKey(dv)) {
                            continue;
                        }
                        yValueS.get(dv).add(psdv.getId());
                        String bandName = psdv.getBandName();
                        String key = bandName + StrUtil.DASHED + dv;
                        if (seatData.containsKey(key)) {
                            seatData.get(key).add(psdv);
                        } else {
                            seatData.put(key, CollUtil.newArrayList(psdv));
                        }
                        if (xMap.containsKey(bandName)) {
                            Long aLong = xMap.get(bandName);
                            xMap.put(bandName, aLong + 1);
                        } else {
                            xMap.put(bandName, 1L);
                        }
                    }
                    vo.setXList(xMap.entrySet().stream().map(item -> {
                        DimensionTotalVo dtv = new DimensionTotalVo();
                        dtv.setName(item.getKey());
                        dtv.setTotal(item.getValue());
                        return dtv;
                    }).collect(Collectors.toList()));
                    vo.setXyData(seatData);

                    // 查询下单skc,计算下单占比、计算缺口
                    QueryWrapper osQw=new QueryWrapper();
                    int index=1;
                    int dTotalInt = dTotal.get();
                    for (Map.Entry<String, List<String>> d : yValueS.entrySet()) {
                        List<String> dSeatIds = d.getValue();
                        Long count=0L;
                        if(CollUtil.isNotEmpty(dSeatIds)){
                            osQw.clear();
                            osQw.in("c.id",dSeatIds);
                            count=planningCategoryItemMapper.queryOrderSkc(osQw);
                        }
                        //下单skc
                        demandSummary.get(3).add(String.valueOf(count));
                        //下单占比
                        BigDecimal xdzb = new BigDecimal(String.valueOf(count)).divide(new BigDecimal(String.valueOf(dTotalInt)),8,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                        demandSummary.get(4).add(NumberUtil.toStr(NumberUtil.round(xdzb,2))+"%");
                        //缺口
                        String dCount = demandSummary.get(1).get(index);
                        demandSummary.get(5).add(String.valueOf(NumberUtil.parseInt(dCount)-count));
                        index++;
                    }
                    vo.setDemandSummary(demandSummary);
                }
            }

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
            Map<String, Long> sckCountMap =getSckCountMap(vo.getBandflag());
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

    /**
     * 查询产品季下的波段或渠道数量
     * @param bandflag
     * @return
     */
    public Map<String, Long> getSckCountMap(String bandflag) {
        if (StringUtils.isNotBlank(bandflag)) {
            List<Map<String, Long>> list = planningCategoryItemMaterialMapper.getbandSum();
            Map<String, Long>  map =new HashMap<>();
            for (Map<String, Long> stringLongMap : list) {
                map.put( String.valueOf(stringLongMap.get("label")) ,stringLongMap.get("count"));
            }
            return map;
        }
        return planningChannelService.countByPlanningSeason();
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
