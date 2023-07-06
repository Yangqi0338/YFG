package com.base.sbc.module.planning.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.sample.entity.SampleDesign;

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

    public static SampleDesign toSampleDesign(PlanningSeason season, PlanningBand band, PlanningCategoryItem item) {
        SampleDesign sampleDesign = new SampleDesign();
        toSampleDesign(sampleDesign, season, band, item);
        return sampleDesign;
    }

    public static void toSampleDesign(SampleDesign sampleDesign, PlanningSeason season, PlanningBand band, PlanningCategoryItem item) {
        BeanUtil.copyProperties(season, sampleDesign);
        BeanUtil.copyProperties(band, sampleDesign);
        BeanUtil.copyProperties(item, sampleDesign);
        sampleDesign.setPlanningCategoryItemId(item.getId());
        sampleDesign.setId(null);
        sampleDesign.setStatus(BaseGlobal.STATUS_NORMAL);
        sampleDesign.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
    }

    /**
     * 设置品类信息 大类，品类，中类，小类
     *
     * @param bean
     */
    public static void setCategory(PlanningCategoryItem bean) {
        String categoryIds = bean.getCategoryIds();
        List<String> categoryIdList = StrUtil.split(categoryIds, StrUtil.COMMA);
        bean.setProdCategory1st(Opt.ofBlankAble(CollUtil.get(categoryIdList, 0)).orElse(""));
        bean.setProdCategory(Opt.ofBlankAble(CollUtil.get(categoryIdList, 1)).orElse(""));
        bean.setProdCategory2nd(Opt.ofBlankAble(CollUtil.get(categoryIdList, 2)).orElse(""));
        bean.setProdCategory3rd(Opt.ofBlankAble(CollUtil.get(categoryIdList, 3)).orElse(""));
    }

    public static void setCategory(PlanningCategory bean) {
        String categoryIds = bean.getCategoryIds();
        List<String> categoryIdList = StrUtil.split(categoryIds, StrUtil.COMMA);
        bean.setProdCategory1st(Opt.ofBlankAble(CollUtil.get(categoryIdList, 0)).orElse(""));
        bean.setProdCategory(Opt.ofBlankAble(CollUtil.get(categoryIdList, 1)).orElse(""));
    }

    public static void setCategory(SampleDesign bean) {
        String categoryIds = bean.getCategoryIds();
        List<String> categoryIdList = StrUtil.split(categoryIds, StrUtil.COMMA);
        bean.setProdCategory1st(Opt.ofBlankAble(CollUtil.get(categoryIdList, 0)).orElse(""));
        bean.setProdCategory(Opt.ofBlankAble(CollUtil.get(categoryIdList, 1)).orElse(""));
        bean.setProdCategory2nd(Opt.ofBlankAble(CollUtil.get(categoryIdList, 2)).orElse(""));
        bean.setProdCategory3rd(Opt.ofBlankAble(CollUtil.get(categoryIdList, 3)).orElse(""));
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
     * 获取品类名称(第二级别)
     *
     * @param categoryName eg:外套,A01/风衣,6/风衣,61/短风衣,6101
     * @return
     */
    public static String getProdCategoryName(String categoryName) {
        return getCategoryName(categoryName, 1);
    }

    /**
     * 获取品类名称
     *
     * @param categoryName eg:外套,A01/风衣,6/风衣,61/短风衣,6101
     * @param idx          0 大类,1 品类,2 中类,3 小类
     * @return
     */
    public static String getCategoryName(String categoryName, int idx) {
        return CollUtil.get(StrUtil.split(CollUtil.get(StrUtil.split(categoryName, CharUtil.SLASH), idx), CharUtil.COMMA), 0);
    }
}
