/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.vo;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.base.sbc.config.enums.business.workload.WorkloadRatingItemType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：工作量评分选项配置QueryDto 实体类
 * @address com.base.sbc.module.workload.dto.WorkloadRatingConfigQueryDto
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Data
@ApiModel("工作量评分选项配置 WorkloadRatingConfigDTO")
public class WorkloadRatingDetailSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    /** 配置名 */
    @ApiModelProperty(value = "配置名")
    @NotBlank(message = "配置名不能为空")
    private String configName;

    /** 配置项id */
    @ApiModelProperty(value = "配置项id")
    private String itemId;

    /** 配置项名 */
    @ApiModelProperty(value = "配置项名")
    @NotBlank(message = "配置项名不能为空")
    private String itemValue;

    /** 是否启用 */
    @ApiModelProperty(value = "是否启用")
    @NotBlank(message = "启用状态不能为空")
    private YesOrNoEnum enableFlag = YesOrNoEnum.YES;

    /** 分数 */
    @ApiModelProperty(value = "分数")
    private BigDecimal score;

    /** 计算类型 base 基础分 rate 浮动比率分 append 附加分 */
    @ApiModelProperty(value = "计算类型 base 基础分 rate 浮动比率分 append 附加分")
    private WorkloadRatingCalculateType calculateType;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
