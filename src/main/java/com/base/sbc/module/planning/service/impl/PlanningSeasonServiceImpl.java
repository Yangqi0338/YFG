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
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.enums.CcmBaseSettingEnum;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.SeatMatchFlagEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.mapper.FieldManagementMapper;
import com.base.sbc.module.formtype.mapper.FieldValMapper;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.entity.PlanningDemandProportionSeat;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMaterialMapper;
import com.base.sbc.module.planning.mapper.PlanningSeasonMapper;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.vo.*;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.ChartBarVo;
import com.base.sbc.module.style.vo.DemandOrderSkcVo;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.*;
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

    @Autowired
    private StylePicUtils stylePicUtils;
    @Resource
    private PlanningChannelService planningChannelService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;
    @Resource
    private PlanningCategoryItemMapper planningCategoryItemMapper;

    @Resource
    @Lazy
    private StyleService styleService;
    @Resource
    @Lazy
    private StyleColorMapper styleColorMapper;
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

    @Resource
    private PlanningDemandProportionSeatService seatService;

    @Override
    public boolean del(String companyCode, String id) {
        return removeById(id);
    }

    @Autowired
    private DataPermissionsService dataPermissionsService;
    @Autowired
    private UserUtils userUtils;

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
            bean.setCreateDept(getVirtualDeptIds());
            save(bean);
            if (ccmFeignService.getSwitchByCode(CcmBaseSettingEnum.ADD_PLANNING_SEASON_DEFAULT_INSERT_TEAM_SWITCH.getKeyCode())) {
                amcFeignService.seasonSaveDefaultTeam(bean.getId());
            }
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
    public List<SelectOptionsVo> getPlanningSeasonOptions(String userCompany, String businessType,ProductCategoryTreeVo vo) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(StrUtil.isNotBlank(userCompany), "COMPANY_CODE", userCompany);
        qw.eq(StrUtil.isNotBlank(vo.getBrand()), "brand", vo.getBrand());

        if (Constants.ONE_STR.equals(vo.getFutureStyleStatus())){
            qw.in(StrUtil.isNotBlank(vo.getYear()), "year", Lists.newArrayList(vo.getYear(),"2099"));
            qw.orderByAsc("name");
        }else {
            qw.eq(StrUtil.isNotBlank(vo.getYear()), "year", vo.getYear());
            qw.orderByDesc("name");
        }
        qw.eq("del_flag", BaseGlobal.NO);

        dataPermissionsService.getDataPermissionsForQw(qw, businessType, "", new String[]{"brand"}, true);
        return getBaseMapper().getPlanningSeasonOptions(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PlanningSummaryVo planningSummary(Principal user, PlanningBoardSearchDto dto, List<PlanningDemandVo> demandList) {
        //维度统计数据
        List<List<String>> demandSummary = CollUtil.newArrayList(
                //维度
                CollUtil.newArrayList(),
                CollUtil.newArrayList("企划skc数"),
                CollUtil.newArrayList("企划占比"),
                CollUtil.newArrayList("下单skc"),
                CollUtil.newArrayList("下单占比"),
                CollUtil.newArrayList("缺口")
        );

        //返回结果
        PlanningSummaryVo vo = new PlanningSummaryVo();
        Map<String, PlanningBandTotalVo> bMap = new LinkedHashMap<>(16);
        Map<String, PlanningDimensionTotalVo> dMap = new LinkedHashMap<>(16);
        Map<String, List<DemandOrderSkcVo>> xyData = new LinkedHashMap<>(16);
        vo.setXList(bMap.values());

        vo.setXyData(xyData);
        vo.setDemandSummary(demandSummary);

        if (demandList == null) {
            QueryDemandDto queryDemandDto = new QueryDemandDto();
            BeanUtil.copyProperties(dto, queryDemandDto);
            demandList = planningDemandService.getDemandListById(user, queryDemandDto);
        }
        PlanningDemandVo planningDemandVo = null;
        FieldManagement fieldManagement = null;
        List<PlanningDemandProportionDataVo> proportionData = null;
        Map<String, PlanningDemandProportionDataVo> proportionDataMap = new LinkedHashMap<>(16);
        //所有维度的值
        List<String> dValues = new ArrayList<>(6);
        //组装统计数据
        if (CollUtil.isNotEmpty(demandList) && CollUtil.isNotEmpty(demandList.get(0).getList())) {
            planningDemandVo = demandList.get(0);
            fieldManagement = fieldManagementMapper.selectById(planningDemandVo.getFieldId());
            demandSummary.get(0).add(planningDemandVo.getDemandName());
            proportionData = planningDemandVo.getList();
            Map<String, Integer> numTotal = new LinkedHashMap<>(16);
            Integer total = 0;
            for (PlanningDemandProportionDataVo e : proportionData) {
                dValues.add(e.getClassify());
                int i = Optional.ofNullable(e.getNum()).orElse(0);
                if (numTotal.containsKey(e.getClassifyName())) {
                    numTotal.put(e.getClassifyName(), numTotal.get(e.getClassifyName()) + i);
                } else {
                    numTotal.put(e.getClassifyName(), i);
                }
                total = total + i;
                proportionDataMap.put(e.getId(), e);
            }
            for (Map.Entry<String, Integer> numEntry : numTotal.entrySet()) {
                demandSummary.get(0).add(numEntry.getKey());
                demandSummary.get(1).add(String.valueOf(numEntry.getValue()));
                BigDecimal div = NumberUtil.div(numEntry.getValue(), total).multiply(new BigDecimal("100"));
                div = div.setScale(2, RoundingMode.HALF_UP);
                demandSummary.get(2).add(NumberUtil.toStr(div) + "%");
                demandSummary.get(3).add("0");
                demandSummary.get(4).add("0%");
                demandSummary.get(5).add(String.valueOf(numEntry.getValue()));
            }
        }
        if (CollUtil.isEmpty(proportionData)) {
            return vo;
        }
        // 组装坑位数据

        //查询配色匹配维度的数据
        BaseQueryWrapper scQw = new BaseQueryWrapper();
        scQw.eq("sc.planning_season_id", dto.getPlanningSeasonId());
        scQw.notEmptyEq("s.prod_category", dto.getProdCategory());
        scQw.notEmptyEq("s.prod_category2nd", dto.getProdCategory2nd());
        scQw.notEmptyEq("s.channel", dto.getChannel());
        scQw.eq("fv.field_id", planningDemandVo.getFieldId());
        scQw.in("fv.val", dValues);
        scQw.notNull("fv.val");
        scQw.notNull("fv.val_name");
        //数据权限
        dataPermissionsService.getDataPermissionsForQw(scQw, DataPermissionsBusinessTypeEnum.planningBoard.getK());
        List<DemandOrderSkcVo> demandOrderSkcVos = styleColorMapper.queryDemandOrderSkc(scQw);
        amcFeignService.setUserAvatarToList(demandOrderSkcVos);
        /*查询款式配色图*/
        stylePicUtils.setStylePic(demandOrderSkcVos, "stylePic");
        //key =id
        Map<String, DemandOrderSkcVo> idSkcMap = new LinkedHashMap<>(16);
        //key= 波段-维度
        Map<String, List<DemandOrderSkcVo>> dSkcMap = new LinkedHashMap<>(16);
        Map<String, Integer> orderTotal = new HashMap<>();

        if (CollUtil.isNotEmpty(demandOrderSkcVos)) {
            for (DemandOrderSkcVo demandOrderSkcVo : demandOrderSkcVos) {
                idSkcMap.put(demandOrderSkcVo.getStyleColorId(), demandOrderSkcVo);
                String key = demandOrderSkcVo.getBandCode() + StrUtil.DASHED + demandOrderSkcVo.getVal();
                if (dSkcMap.containsKey(key)) {
                    dSkcMap.get(key).add(demandOrderSkcVo);
                } else {
                    dSkcMap.put(key, CollUtil.newArrayList(demandOrderSkcVo));
                }
                if (orderTotal.containsKey(demandOrderSkcVo.getValName())) {
                    orderTotal.put(demandOrderSkcVo.getValName(), orderTotal.get(demandOrderSkcVo.getValName()) + 1);
                } else {
                    orderTotal.put(demandOrderSkcVo.getValName(), 1);
                }
            }


        }
        // 企划的坑位 (查询企划需求管理坑位信息)
        List<PlanningDemandProportionSeat> seatList = seatService.findByPid(proportionDataMap.keySet());
        vo.setSeatList(seatList);
        List<PlanningDemandProportionSeat> updateSeatList = new ArrayList<>(16);

        Map<String, Integer> matchTotal = new HashMap<>(16);
        for (PlanningDemandProportionSeat pseat : seatList) {
            PlanningDemandProportionSeat u = new PlanningDemandProportionSeat();
            u.setId(pseat.getId());
            // 需求数量占比
            PlanningDemandProportionData pdpd = proportionDataMap.get(pseat.getPlanningDemandProportionDataId());
            if (pdpd == null) {
                continue;
            }
            //波段的统计
            PlanningBandTotalVo planningBandTotalVo = bMap.get(pdpd.getBandCode());
            if (planningBandTotalVo == null) {
                planningBandTotalVo = BeanUtil.copyProperties(pdpd, PlanningBandTotalVo.class);
                planningBandTotalVo.planningTotalAdd();
                bMap.put(pdpd.getBandCode(), planningBandTotalVo);
            } else {
                planningBandTotalVo.planningTotalAdd();
            }
            //维度的统计
            PlanningDimensionTotalVo planningDimensionTotalVo = dMap.get(pdpd.getClassify());
            if (planningDimensionTotalVo == null) {
                planningDimensionTotalVo = BeanUtil.copyProperties(fieldManagement, PlanningDimensionTotalVo.class);
                planningDimensionTotalVo.setFieldId(planningDemandVo.getFieldId());
                planningDimensionTotalVo.setVal(pdpd.getClassify());
                planningDimensionTotalVo.setValName(pdpd.getClassifyName());
                planningDimensionTotalVo.planningTotalAdd();
                dMap.put(pdpd.getClassify(), planningDimensionTotalVo);
            } else {
                planningDimensionTotalVo.planningTotalAdd();
            }
            String bdkey = pdpd.getBandCode() + StrUtil.DASHED + pdpd.getClassify();
            //数据
            DemandOrderSkcVo demandOrderSkcVo = null;
            // 匹配过
            if (StrUtil.isNotBlank(pseat.getStyleColorId())) {
                demandOrderSkcVo = idSkcMap.get(pseat.getStyleColorId());
            }
            // 没匹配过 or 匹配过对象跑了
            if (demandOrderSkcVo == null) {
                //重新用波段维度匹配
                List<DemandOrderSkcVo> demandOrderSkcVos1 = dSkcMap.get(bdkey);
                if (CollUtil.isNotEmpty(demandOrderSkcVos1)) {
                    demandOrderSkcVo = demandOrderSkcVos1.remove(0);
                }
            }

            //最终未匹配 遗憾退场
            if (demandOrderSkcVo == null) {
                demandOrderSkcVo = new DemandOrderSkcVo();
                demandOrderSkcVo.setMatchFlag(SeatMatchFlagEnum.NO.getValue());
                demandOrderSkcVo.setBandCode(pdpd.getBandCode());
                demandOrderSkcVo.setBandName(pdpd.getBandName());
                demandOrderSkcVo.setVal(pdpd.getClassify());
                demandOrderSkcVo.setValName(pdpd.getClassifyName());
                u.setStyleColorId("");
            } else {
                boolean removeSuccess = false;
                planningBandTotalVo.orderTotalAdd();
                //发结婚证
                u.setStyleColorId(demandOrderSkcVo.getStyleColorId());
                String matchKey = demandOrderSkcVo.getBandCode() + StrUtil.DASHED + demandOrderSkcVo.getVal();
                // 门当户对 住一起
                if (StrUtil.equals(bdkey, matchKey)) {
                    demandOrderSkcVo.setMatchFlag(SeatMatchFlagEnum.YES.getValue());
                    //移除一个（搬一起住）
                    removeSuccess = dSkcMap.get(bdkey).remove(demandOrderSkcVo);
                }
                // 父母包办
                else {
//                    demandOrderSkcVo.setMatchBandCode(pdpd.getBandCode());
//                    demandOrderSkcVo.setMatchBandName(pdpd.getBandName());
//                    demandOrderSkcVo.setMatchFlag(SeatMatchFlagEnum.CUSTOM_COLOR.getValue());
//                    demandOrderSkcVo = BeanUtil.copyProperties(demandOrderSkcVo, DemandOrderSkcVo.class);
                    //移除一个（搬一起住）
                    removeSuccess = dSkcMap.get(matchKey).remove(demandOrderSkcVo);
                    demandOrderSkcVo.setMatchFlag(SeatMatchFlagEnum.CUSTOM_SEAT.getValue());
                    // 2023年10月14日 20:34:39 以前分开住 现在改为一起住

                }
                System.out.println("removeSuccess:" + removeSuccess);
                if (matchTotal.containsKey(demandOrderSkcVo.getValName())) {
                    matchTotal.put(demandOrderSkcVo.getValName(), matchTotal.get(demandOrderSkcVo.getValName()) + 1);
                } else {
                    matchTotal.put(demandOrderSkcVo.getValName(), 1);
                }

            }
            demandOrderSkcVo.setPlanningDemandProportionSeatId(pseat.getId());
            //放入数据集合
            if (xyData.containsKey(bdkey)) {
                xyData.get(bdkey).add(demandOrderSkcVo);
            } else {
                xyData.put(bdkey, CollUtil.newArrayList(demandOrderSkcVo));
            }
            u.setMatchFlag(demandOrderSkcVo.getMatchFlag());
            //放入修改集合
            updateSeatList.add(u);
        }
        vo.setSeatIds(updateSeatList.stream().map(item -> item.getId()).collect(Collectors.toList()));
        seatService.updateBatchById(updateSeatList);
        //处理未匹配的数据
        for (Map.Entry<String, List<DemandOrderSkcVo>> entrySet : dSkcMap.entrySet()) {
            List<DemandOrderSkcVo> item = entrySet.getValue();
            for (DemandOrderSkcVo demandOrderSkcVo : item) {
                //波段的统计
                PlanningBandTotalVo planningBandTotalVo = bMap.get(demandOrderSkcVo.getBandCode());
                if (planningBandTotalVo == null) {
                    planningBandTotalVo = BeanUtil.copyProperties(demandOrderSkcVo, PlanningBandTotalVo.class);
                    planningBandTotalVo.orderTotalAdd();
                    bMap.put(demandOrderSkcVo.getBandCode(), planningBandTotalVo);
                } else {
                    planningBandTotalVo.orderTotalAdd();
                }
                //维度的统计
                PlanningDimensionTotalVo planningDimensionTotalVo = dMap.get(demandOrderSkcVo.getVal());
                if (planningDimensionTotalVo == null) {
                    planningDimensionTotalVo = BeanUtil.copyProperties(demandOrderSkcVo, PlanningDimensionTotalVo.class);
                    planningDimensionTotalVo.setFieldId(planningDemandVo.getFieldId());
                    planningDimensionTotalVo.totalAdd();
                    dMap.put(demandOrderSkcVo.getVal(), planningDimensionTotalVo);
                } else {
                    planningDimensionTotalVo.totalAdd();
                }
                String bdkey = demandOrderSkcVo.getBandCode() + StrUtil.DASHED + demandOrderSkcVo.getVal();
                demandOrderSkcVo.setMatchFlag(Opt.ofBlankAble(demandOrderSkcVo.getMatchFlag()).orElse(SeatMatchFlagEnum.NO_MATCH_COLOR.getValue()));
                //放入数据集合
                if (xyData.containsKey(bdkey)) {
                    xyData.get(bdkey).add(demandOrderSkcVo);
                } else {
                    xyData.put(bdkey, CollUtil.newArrayList(demandOrderSkcVo));
                }


            }
        }
        if (CollUtil.isNotEmpty(demandOrderSkcVos)) {
            for (int i = 1; i < demandSummary.get(0).size(); i++) {
                String valName = demandSummary.get(0).get(i);
                int valNameCount = MapUtil.getInt(orderTotal, valName, 0);
                //企划skc数
                int dcount = NumberUtil.parseInt(demandSummary.get(1).get(i));
                double div = NumberUtil.div(valNameCount, demandOrderSkcVos.size()) * 100;
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(div));
                // 下单skc
                demandSummary.get(3).set(i, String.valueOf(valNameCount));
                //下单占比
                demandSummary.get(4).set(i, NumberUtil.toStr(bigDecimal.setScale(2, RoundingMode.HALF_UP)) + "%");
                //缺口
                demandSummary.get(5).set(i, String.valueOf(dcount - MapUtil.getInt(matchTotal, valName, 0)));
            }
        }


        vo.setYList(CollUtil.sort(dMap.values(), (a, b) -> {
            return StrUtil.compare(a.getValName(), b.getValName(), false);
        }));
        vo.setXList(CollUtil.sort(bMap.values(), (a, b) -> {
            return StrUtil.compare(a.getBandName(), b.getBandName(), false);
        }));

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
            dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PlanningSeason.getK());
            //查询所有产品季
            List<PlanningSeason> seasonList = getBaseMapper().list(qw);
            if (CollUtil.isEmpty(seasonList)) {
                return new ArrayList<>();
            }
            //统计产品季skc数量
            // Map<String, Long> sckCountMap = planningCategoryItemService.totalSkcByPlanningSeason(null);
            Map<String, Long> sckCountMap = getSckCountMap(vo.getBandflag());
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
     *
     * @param bandflag
     * @return
     */
    public Map<String, Long> getSckCountMap(String bandflag) {
        if (StringUtils.isNotBlank(bandflag)) {
            List<Map<String, Long>> list = planningCategoryItemMaterialMapper.getbandSum();
            Map<String, Long> map = new HashMap<>();
            for (Map<String, Long> stringLongMap : list) {
                map.put(String.valueOf(stringLongMap.get("label")), stringLongMap.get("count"));
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
