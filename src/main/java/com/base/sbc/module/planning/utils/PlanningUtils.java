package com.base.sbc.module.planning.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
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

    public static SampleDesign toSampleDesign(PlanningSeason season, PlanningCategoryItem item) {
        SampleDesign sampleDesign = new SampleDesign();
        toSampleDesign(sampleDesign, season, item);
        return sampleDesign;
    }

    public static void toSampleDesign(SampleDesign sampleDesign, PlanningSeason season, PlanningCategoryItem item) {
        BeanUtil.copyProperties(season, sampleDesign);
        BeanUtil.copyProperties(item, sampleDesign);
        sampleDesign.setPlanningCategoryItemId(item.getId());
        sampleDesign.setId(null);
        sampleDesign.setStatus(BaseGlobal.STATUS_NORMAL);
        sampleDesign.setOldDesignNo(item.getDesignNo());
        sampleDesign.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
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

        String newDesignerCode = newDesigner.split(",")[1];

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
}
