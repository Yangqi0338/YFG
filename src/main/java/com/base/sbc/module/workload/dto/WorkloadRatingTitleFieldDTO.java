/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.dto;

import cn.hutool.core.lang.Opt;
import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@ApiModel("工作量评分配置 WorkloadRatingTitleFieldDTO")
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadRatingTitleFieldDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    private String code;

    private String name;

    private String configName;

    private Integer index;

    private String configId;

    @JsonIgnore
    private WorkloadRatingCalculateType calculateType;

    public WorkloadRatingTitleFieldDTO(String code, String name, Integer index) {
        this.code = code;
        this.name = name;
        this.index = index;
    }

    public WorkloadRatingTitleFieldDTO(String code, String name, Integer index, String configId, WorkloadRatingCalculateType calculateType) {
        this.code = code;
        this.name = name;
        this.configName = name;
        this.index = index;
        this.configId = configId;
        this.calculateType = calculateType;
    }

    public WorkloadRatingTitleFieldDTO decorateMain(){
        this.name = Opt.ofNullable(calculateType).map(WorkloadRatingCalculateType::getText).orElse("");
        return this;
    }


    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
