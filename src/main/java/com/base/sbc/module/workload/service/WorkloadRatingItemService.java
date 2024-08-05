/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.workload.dto.WorkloadRatingItemDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.vo.WorkloadRatingItemQO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：工作量评分配置 service类
 * @address com.base.sbc.module.workload.service.WorkloadRatingItemService
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:16
 * @version 1.0  
 */
public interface WorkloadRatingItemService extends BaseService<WorkloadRatingItem> {

// 自定义方法区 不替换的区域【other_start】

    PageInfo<WorkloadRatingItemVO> queryPageInfo(WorkloadRatingItemQO qo);

    WorkloadRatingItemDTO detail(WorkloadRatingItemQO qo);

    Boolean delByIds(List<String> ids);

    void save(List<WorkloadRatingItemDTO> workloadRatingItemList);

    void calculate(WorkloadRatingItemDTO workloadRatingItemDTO);

// 自定义方法区 不替换的区域【other_end】


}