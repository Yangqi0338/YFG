/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.smp.dto;

import cn.hutool.core.lang.Opt;
import com.base.sbc.config.enums.business.smp.SluggishSaleLevelEnum;
import com.base.sbc.config.enums.business.smp.SluggishSaleWeekendsType;
import com.base.sbc.module.smp.entity.GoodsSluggishSales;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：基础资料-复盘评分Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@ApiModel("基础资料-复盘评分 ReplayRatingBulkWarnVO")
public class GoodsSluggishSalesDTO extends GoodsSluggishSales {

    public String getWeekendsText() {
        return Opt.ofNullable(getWeekends()).map(SluggishSaleWeekendsType::getText).orElse(null);
    }

    public SluggishSaleLevelEnum getRealLevel() {
        return Opt.ofNullable(getLevel()).map(SluggishSaleLevelEnum::startByCode).orElse(null);
    }

}
