package com.base.sbc.module.orderbook.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.mapper.BaseEnhanceMapper;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderBookDetailMapper extends BaseEnhanceMapper<OrderBookDetail> {
     List<OrderBookDetailVo> queryPage(@Param("ew") QueryWrapper<OrderBookDetail> queryWrapper);
     List<Map<String, Object>> queryCountByOrderBookId(@Param("ew") QueryWrapper<OrderBookDetail> queryWrapper);

    String getByStyleNoTotalProductionList(@Param("ew")BaseQueryWrapper queryWrapper);
}
