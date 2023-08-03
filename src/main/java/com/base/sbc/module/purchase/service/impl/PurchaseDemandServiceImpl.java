/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.purchase.mapper.PurchaseDemandMapper;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：采购-采购需求表 service类
 * @address com.base.sbc.module.purchase.service.PurchaseDemandService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-2 14:29:52
 * @version 1.0  
 */
@Service
public class PurchaseDemandServiceImpl extends BaseServiceImpl<PurchaseDemandMapper, PurchaseDemand> implements PurchaseDemandService {

    @Autowired
    private PackBomService packBomService;

    @Override
    public ApiResult cancel(String companyCode, String ids) {
        QueryWrapper<PurchaseDemand> qw = new QueryWrapper();
        qw.eq("company_code", companyCode);
        qw.in("id", StringUtils.convertList(ids));
        List<PurchaseDemand> purchaseDemandList = this.list(qw);

        for(PurchaseDemand item : purchaseDemandList){
            if(StringUtils.equals(item.getOrderStatus(), "1") || StringUtils.equals(item.getStatus(), "1")){
                return ApiResult.success("请选择单据状态为正常或者审核状态为待审核");
            }
            item.setOrderStatus("1");
        }

        boolean result = this.updateBatchById(purchaseDemandList);
        if(result) {
            return ApiResult.success("操作成功！", result);
        }
        return ApiResult.error("操作失败！", 500);
    }

	
}

