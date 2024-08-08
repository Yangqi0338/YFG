/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service;

import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailQO;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/** 
 * 类描述：工作量评分数据计算结果 service类
 * @address com.base.sbc.module.workload.service.WorkloadRatingDetailService
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0  
 */
public interface WorkloadRatingDetailService extends BaseService<WorkloadRatingDetail> {

// 自定义方法区 不替换的区域【other_start】

    List<WorkloadRatingDetailDTO> queryList(WorkloadRatingDetailQO qo);

    void save(WorkloadRatingDetailDTO workloadRatingDetail);

    <T extends BaseEntity> void decorateWorkloadRating(List<T> list,
                                                       Function<T, Style> styleFunc,
                                                       Function<T, String> workloadRatingIdFunc,
                                                       BiConsumer<T, String> prodCategoryFunc,
                                                       BiConsumer<T, WorkloadRatingDetailDTO> resultKeyFunc,
                                                       BiConsumer<T, List<WorkloadRatingConfigVO>> resultValueFunc
    );

// 自定义方法区 不替换的区域【other_end】


}