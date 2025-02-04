/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.workload.dto.WorkloadRatingConfigDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingConfig;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;

import java.util.List;

/**
 * 类描述：工作量评分选项配置 service类
 * @address com.base.sbc.module.workload.service.WorkloadRatingConfigService
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0  
 */
public interface WorkloadRatingConfigService extends BaseService<WorkloadRatingConfig> {

// 自定义方法区 不替换的区域【other_start】

    List<WorkloadRatingConfigVO> queryList(WorkloadRatingConfigQO qo);

    WorkloadRatingConfigDTO detail(String id);

    void save(WorkloadRatingConfigDTO workloadRatingConfig);

// 自定义方法区 不替换的区域【other_end】


}