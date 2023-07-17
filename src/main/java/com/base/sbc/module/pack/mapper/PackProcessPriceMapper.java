/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.pack.entity.PackProcessPrice;
import com.base.sbc.module.pricing.vo.PricingProcessCostsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：资料包-工序工价 dao类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.dao.PackProcessPriceDao
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 14:12:07
 */
@Mapper
public interface PackProcessPriceMapper extends BaseMapper<PackProcessPrice> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取核价工序费用
     * @param foreignId
     * @return
     */
    List<PricingProcessCostsVO> getPricingProcessCostsByForeignId(@Param("foreignId") String foreignId);

// 自定义方法区 不替换的区域【other_end】
}