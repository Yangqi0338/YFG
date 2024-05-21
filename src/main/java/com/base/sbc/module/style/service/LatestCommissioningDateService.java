/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.entity.LatestCommissioningDate;
import com.base.sbc.module.style.dto.LatestCommissioningDateQueryDto;
import com.base.sbc.module.style.vo.LatestCommissioningDateExcel;
import com.base.sbc.module.style.vo.LatestCommissioningDateVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：下单最晚投产日期管理 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.LatestCommissioningDateService
 * @email your email
 * @date 创建时间：2024-3-11 19:05:30
 */
public interface LatestCommissioningDateService extends BaseService<LatestCommissioningDate> {

// 自定义方法区 不替换的区域【other_start】

    PageInfo<LatestCommissioningDateVo> findPage(LatestCommissioningDateQueryDto dto);

    ApiResult importExcel(List<LatestCommissioningDateExcel> list);

    void exportExcel(HttpServletResponse response, LatestCommissioningDateQueryDto dto) throws IOException;

    ApiResult updateMain(LatestCommissioningDate vo);

// 自定义方法区 不替换的区域【other_end】


}