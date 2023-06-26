/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMakingWorkLog;
import com.base.sbc.module.patternmaking.vo.PatternMakingWorkLogVo;

import java.util.List;

/**
 * 类描述：打版管理-工作记录 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.PatternMakingWorkLogService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 19:42:22
 */
public interface PatternMakingWorkLogService extends BaseService<PatternMakingWorkLog> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 查询
     *
     * @param dto
     * @return
     */
    List<PatternMakingWorkLogVo> findList(PatternMakingWorkLogSearchDto dto);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    boolean saveLog(PatternMakingWorkLogSaveDto dto);

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    boolean updateLog(PatternMakingWorkLogSaveDto dto);

// 自定义方法区 不替换的区域【other_end】


}
