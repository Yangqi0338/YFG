package com.base.sbc.module.orderbook.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookQueryDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.vo.OrderBookVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/12/5 13:49:56
 * @mail 247967116@qq.com
 */
@Mapper
public interface OrderBookMapper extends BaseMapper<OrderBook> {
    List<OrderBookVo> queryList(@Param("ew") QueryWrapper<OrderBook> queryWrapper, @Param("dto") OrderBookQueryDto dto);

    List<Map<String,String>>  countByStatus(@Param("ew")QueryWrapper<OrderBook> queryWrapper);
}
