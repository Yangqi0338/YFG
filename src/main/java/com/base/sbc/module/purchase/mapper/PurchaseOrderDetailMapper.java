/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：采购-采购单-明细 dao类
 * @address com.base.sbc.module.purchase.dao.PurchaseOrderDetailDao
 * @author tzy
 * @email  974849633@qq.com
 * @date 创建时间：2023-8-4 9:43:21
 * @version 1.0
 */
@Mapper
public interface PurchaseOrderDetailMapper extends BaseMapper<PurchaseOrderDetail> {
// 自定义方法区 不替换的区域【other_start】

    List<PurchaseOrderDetail> selectPurchaseCode(@Param("ew") Wrapper<PurchaseOrderDetail> queryWrapper);

// 自定义方法区 不替换的区域【other_end】
}
