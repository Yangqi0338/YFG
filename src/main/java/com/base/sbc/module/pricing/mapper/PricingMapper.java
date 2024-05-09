/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.pricing.dto.PricingSearchDTO;
import com.base.sbc.module.pricing.entity.Pricing;
import com.base.sbc.module.pricing.vo.PricingListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：核价表 dao类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.dao.PricingDao
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
@Mapper
public interface PricingMapper extends BaseMapper<Pricing> {
// 自定义方法区 不替换的区域【other_start】



    /**
     * 列表查询-明细维度
     *
     * @param dto
     * @return
     */
    List<PricingListVO> getItemDimension(@Param(Constants.WRAPPER) QueryWrapper qw, @Param("dto") PricingSearchDTO dto);

    /**
     * 列表查询-汇总维度
     *
     * @param dto
     * @return
     */
    List<PricingListVO> getSummaryDimension(@Param(Constants.WRAPPER) QueryWrapper qw, @Param("dto") PricingSearchDTO dto);


// 自定义方法区 不替换的区域【other_end】
}

