/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.dto;

import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：工作量评分数据计算结果QueryDto 实体类
 * @address com.base.sbc.module.workload.dto.WorkloadRatingDetailQueryDto
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工作量评分数据计算结果 WorkloadRatingDetailDTO")
public class WorkloadRatingDetailDTO extends WorkloadRatingDetail {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    public BigDecimal getCalculateTypeResult(WorkloadRatingCalculateType calculateType) {
        return new BigDecimal(this.getExtend().getOrDefault(calculateType.getCode(), calculateType.getDefaultValue()).toString());
    }

    public BigDecimal getScoreByItemName(WorkloadRatingCalculateType calculateType) {
        return new BigDecimal(this.getExtend().getOrDefault(calculateType.getCode(), "0").toString());
    }

    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
