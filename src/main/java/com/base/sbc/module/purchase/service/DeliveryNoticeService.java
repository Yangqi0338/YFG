/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.purchase.entity.DeliveryNotice;
import com.base.sbc.open.dto.OpenMaterialNoticeDto;

import java.util.List;

/**
 * 类描述：送货通知单 service类
 * @address com.base.sbc.module.purchase.service.DeliveryNoticeService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-8 16:38:37
 * @version 1.0
 */
public interface DeliveryNoticeService extends BaseService<DeliveryNotice>{
    ApiResult generateNotice(UserCompany userCompany, String companyCode, String ids);

    /**
     * 保存通知单（领猫）
     * @param noticeDtoList
     * @return
     */
    ApiResult saveNoticeList(List<OpenMaterialNoticeDto> noticeDtoList);

}
