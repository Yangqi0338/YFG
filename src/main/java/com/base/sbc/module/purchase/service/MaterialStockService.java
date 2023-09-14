/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.entity.WarehousingOrderDetail;

import java.util.List;

/** 
 * 类描述：物料库存 service类
 * @address com.base.sbc.module.purchase.service.MaterialStockService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-13 15:44:13
 * @version 1.0  
 */
public interface MaterialStockService extends BaseService<MaterialStock>{

    void warehousingMaterialStock(WarehousingOrder order, List<WarehousingOrderDetail> orderDetailList, String operation);
	
}
