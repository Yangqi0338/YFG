/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.common.vo.TotalVo;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.entity.PackPricingOtherCostsGst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：资料包-核价信息-其他费用 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.dao.PackPricingOtherCostsDao
 * @email your email
 * @date 创建时间：2023-7-10 13:35:18
 */
@Mapper
public interface PackPricingOtherCostsGstMapper extends BaseMapper<PackPricingOtherCostsGst> {

}