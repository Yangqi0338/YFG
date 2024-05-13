/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.hangtag.entity.HangTagLog;
import com.base.sbc.module.hangtag.vo.HangTagLogVO;

import java.util.List;

/**
 * 类描述：吊牌日志 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagLogService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:57
 */
public interface HangTagLogService extends BaseService<HangTagLog> {

    // 自定义方法区 不替换的区域【other_start】

    /**
     * 通过吊牌id获取
     *
     * @param HangTagId
     * @param userCompany
     * @return
     */
    List<HangTagLogVO> getByHangTagId(String HangTagId, String userCompany);


// 自定义方法区 不替换的区域【other_end】


}

