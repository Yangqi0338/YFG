/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.dto;

import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：工作量评分配置QueryDto 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.dto.WorkloadRatingItemQueryDto
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工作量评分配置 WorkloadRatingItemDTO")
public class WorkloadRatingItemDTO extends WorkloadRatingItem {

    private static final long serialVersionUID = 1L;

    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    private Boolean applyAll;

    @JsonAnySetter
    public void decorateWebDTO(String key, Object obj) {
        getExtend().put(key, obj);
    }

    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
