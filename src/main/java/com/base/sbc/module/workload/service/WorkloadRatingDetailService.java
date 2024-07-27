/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailQO;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：工作量评分数据计算结果 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.service.WorkloadRatingDetailService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:45
 */
public interface WorkloadRatingDetailService extends BaseService<WorkloadRatingDetail> {

// 自定义方法区 不替换的区域【other_start】

    PageInfo<WorkloadRatingDetailVO> queryPageInfo(WorkloadRatingDetailQO qo);

    WorkloadRatingDetailDTO detail(String id);

    Boolean delByIds(List<String> ids);

    void save(WorkloadRatingDetailDTO workloadRatingDetail);

// 自定义方法区 不替换的区域【other_end】


}