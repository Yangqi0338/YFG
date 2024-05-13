package com.base.sbc.module.orderbook.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.orderbook.dto.OrderBookQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.vo.OrderBookVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/12/5 13:47:46
 * @mail 247967116@qq.com
 */
public interface OrderBookService extends BaseService<OrderBook> {
    PageInfo<OrderBookVo> queryPage(OrderBookQueryDto dto);

    /**
     * 根据条件查询
     */
    List<OrderBookVo> queryList(QueryWrapper<OrderBook> queryWrapper, OrderBookQueryDto dto);

    void importExcel(OrderBookQueryDto dto, HttpServletResponse response) throws IOException;

    List<Map<String,String>> countByStatus(OrderBookQueryDto dto );


    /**
     * 新建订货本
     * @param dto
     * @return
     */
    OrderBook saveOrderBook(OrderBookSaveDto dto);
}
