package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.TeamVo;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.dto.PlanningBandDto;
import com.base.sbc.module.planning.dto.PlanningBandSearchDto;
import com.base.sbc.module.planning.dto.ProductSeasonExpandByBandSearchDto;
import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.mapper.PlanningBandMapper;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningCategoryService;
import com.base.sbc.module.planning.vo.PlanningBandVo;
import com.base.sbc.module.planning.vo.PlanningCategoryItemVo;
import com.base.sbc.module.planning.vo.PlanningSeasonBandVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：波段企划 service
 * @address com.base.sbc.module.planning.service.impl.PlanningBandServiceImpl
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-14 15:00
 * @version 1.0
 */
@Service
public class PlanningBandServiceImpl extends ServicePlusImpl<PlanningBandMapper, PlanningBand> implements PlanningBandService {


    @Autowired
    private PlanningCategoryService planningCategoryService;
    @Autowired
    private PlanningCategoryItemService planningCategoryItemService;
    @Autowired
    private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    @Resource
    private AmcFeignService amcFeignService;
    @Override
    @Transactional
    public boolean del( String id) {
        this.removeById(id);
        return true;
    }

    @Override
    public List<PlanningSeasonBandVo> selectByQw(QueryWrapper<PlanningBand> qw) {
        return getBaseMapper().selectByQw(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PlanningBand savePlanningBand(PlanningBandDto dto) {
        PlanningBand bean = null;
        if (StrUtil.isBlank(dto.getId())) {
            bean = BeanUtil.copyProperties(dto, PlanningBand.class);
            save(bean);
            bean.setStatus(Optional.ofNullable(dto.getStatus()).orElse(BaseGlobal.STATUS_NORMAL));
        } else {
            bean = getById(dto.getId());
            if (bean == null) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, bean);
            updateById(bean);
            bean.setStatus(Optional.ofNullable(dto.getStatus()).orElse(BaseGlobal.STATUS_NORMAL));
        }
        List<PlanningCategory> categoryList = dto.getCategoryData();
        planningCategoryService.savePlanningCategory(bean, categoryList);
        return bean;
    }

    @Override
    public void checkRepeat(PlanningBandDto dto, String userCompany) {
        QueryWrapper nameQc = new QueryWrapper();
        nameQc.eq(COMPANY_CODE, userCompany);
        nameQc.eq("name", dto.getName());
        nameQc.eq("planning_season_id", dto.getPlanningSeasonId());
        if (StrUtil.isNotEmpty(dto.getId())) {
            nameQc.ne("id", dto.getId());
        }
        long nameCount = count(nameQc);
        if (nameCount > 0) {
            throw new OtherException("名称重复");
        }

        QueryWrapper bandQc = new QueryWrapper();
        bandQc.eq(COMPANY_CODE, userCompany);
        bandQc.eq("band_code", dto.getBandCode());
        bandQc.eq("planning_season_id", dto.getPlanningSeasonId());
        if (StrUtil.isNotEmpty(dto.getId())) {
            bandQc.ne("id", dto.getId());
        }
        long bandCount = count(bandQc);
        if (bandCount > 0) {
            throw new OtherException("该产品季已有此波段");
        }
    }

    @Override
    public PlanningSeasonBandVo getBandByName(String planningSeasonName, String planningBandName, String userCompany) {
        QueryWrapper qc = new QueryWrapper();
        qc.apply("b.planning_season_id=s.id");
        qc.eq("s.company_code", userCompany);
        qc.eq("s.name", planningSeasonName);
        qc.eq("b.name", planningBandName);
        qc.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        List<PlanningSeasonBandVo> list = selectByQw(qc);
        PlanningSeasonBandVo first = CollUtil.getFirst(list);
        //查询品类列表
        QueryWrapper categoryQc = new QueryWrapper();
        categoryQc.eq(COMPANY_CODE, userCompany);
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
        //获取团队信息
        List<TeamVo> teamList = amcFeignService.getTeamBySeasonId(first.getSeason().getId());
        first.getSeason().setTeamList(teamList);
        return first;
    }

    @Override
    public PageInfo<PlanningSeasonBandVo> queryPlanningSeasonBandPageInfo(PlanningBandSearchDto dto, String userCompany) {
        // 1 查询产品季
        QueryWrapper<PlanningBand> qc = new QueryWrapper<>();
        qc.apply("b.planning_season_id=s.id");
        qc.like(StrUtil.isNotBlank(dto.getSearch()), "b.name", dto.getSearch());
        qc.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qc.in(CollUtil.isNotEmpty(dto.getMonthList()),"b.month",dto.getMonthList());
        qc.eq("s.company_code", userCompany);
        qc.eq("s.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq("s.name", dto.getPlanningSeasonName());
        dto.setOrderBy("b.create_date desc ");
        PageHelper.startPage(dto);
        List<PlanningSeasonBandVo> list = selectByQw(qc);
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
        return null;
    }

    @Override
    public PageInfo expandByBand(ProductSeasonExpandByBandSearchDto dto,String companyCode) {

        QueryWrapper bqw = new QueryWrapper();
        bqw.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        bqw.eq(COMPANY_CODE, companyCode);
        bqw.eq("status", BaseGlobal.STOCK_STATUS_CHECKED);
        bqw.eq("planning_season_id", dto.getPlanningSeasonId());
        dto.setOrderBy("create_date desc ");
        Page<PlanningBand> objects = PageHelper.startPage(dto);
        list(bqw);
        PageInfo<PlanningBand> planningSeasonPageInfo = objects.toPageInfo();
        return planningSeasonPageInfo;
    }
}
