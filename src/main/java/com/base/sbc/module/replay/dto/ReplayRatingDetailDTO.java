/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.module.replay.entity.ReplayRatingDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-复盘评分-详情Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingDetailVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("基础资料-复盘评分-详情 ReplayRatingDetailDTO")
public class ReplayRatingDetailDTO extends ReplayRatingDetail {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
