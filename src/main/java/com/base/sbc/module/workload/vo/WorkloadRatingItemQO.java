/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.vo;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.text.StrJoiner;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.workload.WorkloadRatingType;
import com.base.sbc.module.workload.dto.WorkloadRatingItemDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：工作量评分配置QueryDto 实体类
 * @address com.base.sbc.module.workload.dto.WorkloadRatingItemQueryDto
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:16
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkloadRatingItemQO extends Page {
    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 类型: sample 样衣工 */
    @ApiModelProperty(value = "类型: sample 样衣工")
    private WorkloadRatingType type;
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /** 评分项id */
    @ApiModelProperty(value = "评分项id")
    private String configId;
    /** 评分项名称 */
    @ApiModelProperty(value = "评分项名称")
    private String configName;
    /** 评分项具体值 */
    @ApiModelProperty(value = "评分项具体值")
    @NotBlank(message = "具体评分项必须传入", groups = {WorkloadRatingItemDTO.class})
    private String itemValue;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

    /* 只接受两种请求 */
    @NotBlank(message = "配置id/类型-品牌-配置名 必须传入")
    public String checkList() {
        return Opt.ofNullable(configId).orElse(StrJoiner.of("").setNullMode(StrJoiner.NullMode.IGNORE).append(type).append(brand).append(configName).toString());
    }
}
