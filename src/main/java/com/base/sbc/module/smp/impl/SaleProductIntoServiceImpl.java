package com.base.sbc.module.smp.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.smp.entity.SalesData;
import com.base.sbc.module.smp.mapper.SaleProductIntoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SaleProductIntoServiceImpl implements SaleProductIntoService{

    private final SaleProductIntoMapper saleProductIntoMapper;

    @Autowired
    public SaleProductIntoServiceImpl(SaleProductIntoMapper saleProductIntoMapper) {
        this.saleProductIntoMapper = saleProductIntoMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    @DS("starRocks")
    public List<Map<String, Object>> querySaleIntoPage(BaseQueryWrapper qw, Integer total) {
        return saleProductIntoMapper.querySaleIntoPage(qw, total);
    }

    @Override
    public List<SalesData> queryMergeGoodsNoByGoodsNo(List<String> goodsNoList) {
        return saleProductIntoMapper.queryMergeGoodsNoByGoodsNo(goodsNoList);
    }

    @Override
    public List<SalesData> queryGoodsNoByMergeGoodsNo(List<String> mergeGoodsNoList) {
        return saleProductIntoMapper.queryGoodsNoByMergeGoodsNo(mergeGoodsNoList);
    }

    @Override
    public List<SalesData> querySalesNumAndProductionNumByGoodsNos(List<String> goodsNoList) {
        return saleProductIntoMapper.querySalesNumAndProductionNumByGoodsNos(goodsNoList);
    }


}
