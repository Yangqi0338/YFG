/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingDto;
import com.base.sbc.module.pack.dto.SyncPricingBomDto;
import com.base.sbc.module.pack.entity.PackPricing;
import com.base.sbc.module.pack.vo.PackPricingVo;
import com.base.sbc.module.pricing.dto.QueryContractPriceDTO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 类描述：资料包-核价信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:16
 */
public interface PackPricingService extends PackBaseService<PackPricing> {

// 自定义方法区 不替换的区域【other_start】
    /**
     * 获取合同价
     * @param queryContractPriceDTO
     * @return
     */
    ApiResult getContractPrice(QueryContractPriceDTO queryContractPriceDTO);

    PackPricingVo getDetail(PackCommonSearchDto dto);

    Map<String, BigDecimal> calculateCosts(PackCommonSearchDto dto);

    /**
     * 计算价格
     * @param formula
     * @param itemVal
     * @param decimal 保留小数点
     * @return
     */
    BigDecimal formula(String formula, Map<String, Object> itemVal,Integer decimal);

    PackPricingVo saveByDto(PackPricingDto dto);

    /**
     * 计算总价格
     * 默认查大货 flag=1 查询设计  flag=2 当前阶段
     * @param packInfoId
     * @param flag
     * @return
     */
    BigDecimal countTotalPrice(String packInfoId,String flag,Integer decimal);

    void asyncCost(String foreignId);

    /**
     * 获取核价信息中的路由信息
     * @param styleNo
     * @return
     */
    Map getPricingRoute(String styleNo);

    /**
     * 生成核价信息
     * @param styleId
     * @param foreignId
     * @return
     */
    PackPricing createPackPricing( String styleId,String foreignId);


    PackPricing getByForeignIdOne(String foreignId, String packType);


    /**
     * 从物料清单同步数据到核价
     * @param dto
     * @return
     */
    boolean syncPricingBom(SyncPricingBomDto dto);

    /**
     * 重新计算核价中的JSON
     * @param foreignId
     * @param packType
     * @param fields 可修改字段
     * @return
     */
    boolean calculatePricingJson(String foreignId, String packType);

    /**
     * 重新计算核价中的JSON
     * @param foreignId
     * @param packType
     * @param fields 可修改字段
     * @return
     */
    boolean calculatePricingJson(String foreignId, String packType,Map<String,Object> map);


    void updatePricingJson(Integer pageNum, Integer pageSize);

// 自定义方法区 不替换的区域【other_end】


}
