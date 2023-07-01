/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.sample.entity.SampleSale;
import com.base.sbc.module.sample.vo.SampleSaleSampleItemVo;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：样衣销售 dao类
 * @address com.base.sbc.module.sample.dao.SampleSaleMapper
 */
@Mapper
public interface SampleSaleMapper extends BaseMapper<SampleSale> {
    List<SampleSaleVo> getList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SampleSaleSampleItemVo> getListBySampleItem(@Param(Constants.WRAPPER) QueryWrapper qw);

    SampleSaleVo getDetail(String id);
}

