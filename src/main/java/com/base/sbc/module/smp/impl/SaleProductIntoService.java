package com.base.sbc.module.smp.impl;


import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.smp.entity.SalesData;

import java.util.List;
import java.util.Map;

public interface SaleProductIntoService {

    public List<Map<String, Object>> querySaleIntoPage(BaseQueryWrapper qw, Integer total);

    /**
     * 根据大货款查询合并款
     * @param goodsNoList 大货款号集合
     * @return 大货款号-合并款号
     */
    List<SalesData> queryMergeGoodsNoByGoodsNo(List<String> goodsNoList);

    /**
     * 根据合并款查询大货款
     * @param mergeGoodsNoList 合并款号集合
     * @return 大货款号-合并款号
     */
    List<SalesData> queryGoodsNoByMergeGoodsNo(List<String> mergeGoodsNoList);

    /**
     * 根据大货款查询销售量和投产量
     * @param goodsNoList 大货款号集合
     * @return 大货款号（prodCode） 销售量（salesNum） 投产量（productionNum）
     */
    List<SalesData> querySalesNumAndProductionNumByGoodsNos(List<String> goodsNoList);
}
