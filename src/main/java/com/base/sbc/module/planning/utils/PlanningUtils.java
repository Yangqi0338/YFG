package com.base.sbc.module.planning.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.style.entity.Style;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：企划帮助
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.utils.PlanningUtils
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-18 17:14
 */
public class PlanningUtils {

    public static Style toSampleDesign(PlanningSeason season, PlanningCategoryItem item) {
        Style style = new Style();
        toSampleDesign(style, season, item);
        return style;
    }

    public static void toSampleDesign(Style style, PlanningSeason season, PlanningCategoryItem item) {
        BeanUtil.copyProperties(season, style);
        BeanUtil.copyProperties(item, style);
        style.setPlanningCategoryItemId(item.getId());
        style.setId(null);
        style.setStatus(BaseGlobal.STATUS_NORMAL);
        style.setOldDesignNo(item.getDesignNo());
        style.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
    }


    /**
     * 删除空元素和排序
     *
     * @param list
     * @return
     */
    public static List<DimensionTotalVo> removeEmptyAndSort(List<DimensionTotalVo> list) {
        if (CollUtil.isEmpty(list)) {
            return list;
        }
        return list.stream().filter(item -> {
            return StrUtil.isNotBlank(item.getName());
        }).sorted((a, b) -> {
            return NumberUtil.compare(b.getTotal(), a.getTotal());
        }).collect(Collectors.toList());
    }


    /**
     * 获取新的款号
     *
     * @param oldDesignNo 旧款号
     * @param oldDesigner 旧设计师
     * @param newDesigner 新设计师
     * @return
     */
    public static String getNewDesignNo(String oldDesignNo, String oldDesigner, String newDesigner) {
        String newDesignNo = oldDesignNo;
        if (!newDesigner.contains(StrUtil.COMMA)) {
            throw new OtherException("设计师名称格式为:名称,代码");
        }

        if (StrUtil.equals(newDesigner, oldDesigner)) {
            return newDesignNo;
        }
        List<String> split = StrUtil.split(newDesigner, CharUtil.COMMA);

        String newDesignerCode = CollUtil.get(split,1);

        if (StrUtil.isBlank(newDesignerCode)) {
            throw new OtherException("设计师没有配置代码");
        }

        //如果还没设置设计师 款号= 款号+设计师代码
        if (StrUtil.isBlank(oldDesigner)) {
            newDesignNo = oldDesignNo + newDesignerCode;
        } else {
            //如果已经设置了设计师 款号=款号+新设计师代码
            String oldDesignCode = oldDesigner.split(",")[1];
            // 获取原始款号
            newDesignNo = StrUtil.sub(oldDesignNo, 0, oldDesignNo.length() - oldDesignCode.length()) + newDesignerCode;
        }
        return newDesignNo;
    }

    /**
     * 获取设计款号前缀
     *
     * @param designNo
     * @return
     */
    public static String getDesignNoPrefix(String designNo, String designer) {
        if (StrUtil.contains(designer, CharUtil.COMMA)) {
            String code = CollUtil.getLast(StrUtil.split(designer, CharUtil.COMMA));
            if (StrUtil.endWith(designNo, code)) {
                return StrUtil.sub(designNo, 0, designNo.length() - code.length());
            }
        }
        return designNo;
    }

    /**
     * 维度标签公共查询条件
     *
     * @param qw
     * @param bean
     * @param prefix 表别名
     */
    public static void dimensionCommonQw(BaseQueryWrapper qw, Object bean, String prefix) {
        PlanningCategoryItem dto = BeanUtil.copyProperties(bean, PlanningCategoryItem.class);
        String coefficientFlag = (String)BeanUtil.getProperty(bean, "coefficientFlag");
        qw.notEmptyEq("planning_season_id", dto.getPlanningSeasonId());
        qw.notEmptyEq("channel", dto.getChannel());
        Object dimensionalityName = BeanUtil.getProperty(bean, "dimensionalityName");
        if (ObjectUtil.isNotEmpty(dimensionalityName)) {
            qw.notEmptyIn("dimensionality_name", dimensionalityName.toString());
        }
   /*     if(StrUtil.equals(coefficientFlag,BaseGlobal.YES)){
            qw.eq("coefficient_flag",BaseGlobal.YES);
        }else {
            qw.eq("coefficient_flag",BaseGlobal.NO);
        }*/
        qw.eq("coefficient_flag",BaseGlobal.YES);
        if (StrUtil.isBlank(dto.getCategoryFlag())) {
            dto.setCategoryFlag(StrUtil.isBlank(dto.getProdCategory2nd()) ? BasicNumber.ZERO.getNumber() : BasicNumber.ONE.getNumber());
        } else if (StrUtil.equals(dto.getCategoryFlag(), BasicNumber.ZERO.getNumber())) {
            dto.setProdCategory2nd("");
        }
        if (prefix == null) {
            prefix = "";
        }
        if (StrUtil.equals(dto.getCategoryFlag(), BasicNumber.ONE.getNumber())) {
            qw.eq(StrUtil.isNotBlank(dto.getProdCategory1st()), prefix + "prod_category1st", dto.getProdCategory1st());
            qw.eq(prefix + "prod_category", dto.getProdCategory());
            qw.eq(prefix + "prod_category2nd", dto.getProdCategory2nd());
        } else {
            qw.eq(StrUtil.isNotBlank(dto.getProdCategory1st()), prefix + "prod_category1st", dto.getProdCategory1st());
            qw.eq(prefix + "prod_category", dto.getProdCategory());
//            qw.isNullStr(prefix + "prod_category2nd");
            qw.isNullStr(prefix + "prod_category2nd");

        }
    }

    //    字段配置
    public static void fieldConfigQw(BaseQueryWrapper qw, Object bean) {
        QueryDemandDto dto = BeanUtil.copyProperties(bean, QueryDemandDto.class);
        /*品类查询字段配置列表查询品类下的字段id*/
        qw.eq("status", BaseGlobal.STATUS_NORMAL);
        qw.eq("form_type_id", dto.getFieldId());
        if (StrUtil.isNotBlank(dto.getProdCategory2nd())) {
            qw.apply(StrUtil.isNotBlank(dto.getProdCategory2nd()), "FIND_IN_SET({0},prod_category2nd)", dto.getProdCategory2nd());
        } else {
            qw.apply(StrUtil.isNotBlank(dto.getProdCategory()), "FIND_IN_SET({0},category_code)", dto.getProdCategory());
        }
        qw.apply(StrUtil.isNotBlank(dto.getBrand()), "FIND_IN_SET({0},brand)", dto.getBrand());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
    }

    public static void dimensionCommonQw(BaseQueryWrapper qw, Object bean) {
        dimensionCommonQw(qw, bean, "");
    }

}
