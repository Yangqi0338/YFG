package com.base.sbc.module.planning.utils;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.sample.entity.Sample;

/**
 * 类描述：企划帮助
 * @address com.base.sbc.module.planning.utils.PlanningUtils
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-18 17:14
 * @version 1.0
 */
public class PlanningUtils {

    public static Sample toSample(PlanningSeason season, PlanningBand band, PlanningCategoryItem item){
        Sample sample=new Sample();
        toSample(sample,season,band,item);
        return sample;
    }
    public static void toSample(Sample sample,PlanningSeason season, PlanningBand band, PlanningCategoryItem item){
        BeanUtil.copyProperties(season,sample);
        BeanUtil.copyProperties(band,sample);
        BeanUtil.copyProperties(item,sample);
        sample.setPlanningCategoryItemId(item.getId());
        sample.setId(null);
        sample.setStatus(BaseGlobal.STATUS_NORMAL);
        sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);

    }
}
