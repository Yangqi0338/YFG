/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.vo;

import cn.hutool.core.lang.Opt;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.workload.WorkloadRatingType;
import com.base.sbc.module.common.vo.SelectOptionsChildrenVo;
import com.base.sbc.module.workload.entity.WorkloadRatingConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：工作量评分选项配置Vo 实体类
 * @address com.base.sbc.module.workload.vo.WorkloadRatingConfigVo
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工作量评分选项配置 WorkloadRatingConfigVO")
public class WorkloadRatingConfigVO extends WorkloadRatingConfig {
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    private YesOrNoEnum enableFlag = YesOrNoEnum.YES;

    private List<SelectOptionsChildrenVo> optionsList;
    private String titleField;

    public String getTypeText() {
        return Opt.ofNullable(this.getType()).map(WorkloadRatingType::getText).orElse("");
    }

//    /**
//     * 拥有的itemValue
//     */
//    @ApiModelProperty(value = "拥有的itemValue")
//    private List<WorkloadRatingItemDTO> saveTemplateList;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
