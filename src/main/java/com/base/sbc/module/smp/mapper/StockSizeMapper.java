package com.base.sbc.module.smp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.smp.entity.StockSize;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("starRocks")
public interface StockSizeMapper extends BaseMapper<StockSize> {
}
