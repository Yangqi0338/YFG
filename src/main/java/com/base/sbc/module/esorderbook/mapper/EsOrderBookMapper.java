/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.esorderbook.entity.EsOrderBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：ES订货本 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.dao.EsOrderBookDao
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@Mapper
public interface EsOrderBookMapper extends BaseMapper<EsOrderBook> {

    List<EsOrderBookItemVo> findPage(@Param(Constants.WRAPPER) BaseQueryWrapper<EsOrderBookItemVo> qw);

}