/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.patternmaking.dto.WorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.WorkLogSearchDto;
import com.base.sbc.module.patternmaking.entity.WorkLog;
import com.base.sbc.module.patternmaking.vo.WorkLogVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：工作小账 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.WorkLogService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-10 19:29:31
 */
public interface WorkLogService extends BaseService<WorkLog> {

// 自定义方法区 不替换的区域【other_start】

    String redis_key = "workLog:";

    PageInfo<WorkLogVo> pageInfo(WorkLogSearchDto dto);

    WorkLogVo saveByDto(WorkLogSaveDto workLog);


    /**
     * 导出工作小帐
     * @param dto
     * @param response
     */
     void workLogDeriveExcel(WorkLogSearchDto dto, HttpServletResponse response) throws IOException;


// 自定义方法区 不替换的区域【other_end】


}
