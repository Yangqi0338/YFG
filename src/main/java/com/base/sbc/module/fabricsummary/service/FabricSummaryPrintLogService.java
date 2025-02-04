/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog;

/** 
 * 类描述：面料汇总-打印日志 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryPrintLogService
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-29 20:05:56
 * @version 1.0  
 */
public interface FabricSummaryPrintLogService extends BaseService<FabricSummaryPrintLog>{

// 自定义方法区 不替换的区域【other_start】
    Long getPrintLogCount(String fabricSummaryId);


// 自定义方法区 不替换的区域【other_end】

	
}
