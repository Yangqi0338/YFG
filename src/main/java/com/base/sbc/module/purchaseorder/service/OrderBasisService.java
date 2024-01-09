package com.base.sbc.module.purchaseorder.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.purchaseorder.dto.OrderBasisDto;
import com.base.sbc.module.purchaseorder.entity.OrderBasis;
import com.base.sbc.module.purchaseorder.vo.OrderBasisVo;
import com.github.pagehelper.PageInfo;

public interface OrderBasisService extends BaseService<OrderBasis> {
    PageInfo<OrderBasisVo> queryPage(OrderBasisDto dto);

    Boolean orderBasisAdd(OrderBasisDto dto);

    String orderBasisSave(OrderBasisDto dto);

    String orderBasisUpdate(OrderBasisDto dto);

    Boolean getCompile(OrderBasisDto dto);

    void getStartApproval(String id,String userCompany);

}
