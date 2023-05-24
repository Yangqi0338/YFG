package com.base.sbc.module.planning.utils;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.sample.entity.SampleDesign;

/**
 * 类描述：企划帮助
 * @address com.base.sbc.module.planning.utils.PlanningUtils
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-18 17:14
 * @version 1.0
 */
public class PlanningUtils {

    public static SampleDesign toSampleDesign(PlanningSeason season, PlanningBand band, PlanningCategoryItem item){
        SampleDesign sampleDesign =new SampleDesign();
        toSampleDesign(sampleDesign,season,band,item);
        return sampleDesign;
    }
    public static void toSampleDesign(SampleDesign sampleDesign, PlanningSeason season, PlanningBand band, PlanningCategoryItem item){
        BeanUtil.copyProperties(season, sampleDesign);
        BeanUtil.copyProperties(band, sampleDesign);
        BeanUtil.copyProperties(item, sampleDesign);
        sampleDesign.setPlanningCategoryItemId(item.getId());
        sampleDesign.setId(null);
        sampleDesign.setStatus(BaseGlobal.STATUS_NORMAL);
        sampleDesign.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);

    }
}
