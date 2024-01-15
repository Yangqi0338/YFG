package com.base.sbc.module.orderbook.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.orderbook.entity.OrderBookFollowUp;
import com.base.sbc.module.orderbook.vo.OrderBookFollowUpVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-15 15:00:43
 * @mail 247967116@qq.com
 */
@Mapper
public interface OrderBookFollowUpMapper extends BaseMapper<OrderBookFollowUp> {
    List<OrderBookFollowUpVo> queryPage(@Param("ew")QueryWrapper<OrderBookFollowUp> queryWrapper);
}
