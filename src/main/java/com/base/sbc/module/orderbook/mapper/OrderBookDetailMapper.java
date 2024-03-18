package com.base.sbc.module.orderbook.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.mapper.BaseEnhanceMapper;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
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

     @DS("starRocks")
     List<Map<String, Object>> queryStarRocks(@Param("ew") QueryWrapper<OrderBookDetail> queryWrapper, @Param("channel") String channel, @Param("total") Integer total);
     @DS("starRocks")
     List<Map<String, Object>> queryStarRocks1(@Param("ew") QueryWrapper<OrderBookDetail> queryWrapper, @Param("total") Integer total);
     @DS("starRocks")
     default List<Map<String, Object>> queryStarRocksTotal(@Param("ew") QueryWrapper<OrderBookDetail> queryWrapper) {
          return queryStarRocks1(queryWrapper,1);
     };
     @DS("starRocks")
     default List<Map<String, Object>> queryStarRocksDetail(@Param("ew") QueryWrapper<OrderBookDetail> queryWrapper) {
          return queryStarRocks1(queryWrapper,0);
     };
}
