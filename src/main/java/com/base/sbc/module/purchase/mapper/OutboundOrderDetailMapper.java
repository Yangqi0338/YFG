/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.purchase.vo.OutBoundOrderDetailVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.purchase.entity.OutboundOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：出库单-明细 dao类
 * @address com.base.sbc.module.purchase.dao.OutboundOrderDetailDao
 * @author tzy  
 * @email  974849633@qq.com
 * @date 创建时间：2023-8-18 15:21:51 
 * @version 1.0  
 */
@Mapper
public interface OutboundOrderDetailMapper extends BaseMapper<OutboundOrderDetail> {
    List<OutBoundOrderDetailVo> relationMaterialStock(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
}