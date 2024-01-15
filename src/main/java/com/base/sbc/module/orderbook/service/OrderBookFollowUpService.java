package com.base.sbc.module.orderbook.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookFollowUpQueryDto;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.OrderBookFollowUp;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.orderbook.vo.OrderBookFollowUpVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-15 15:01:00
 * @mail 247967116@qq.com
 */
public interface OrderBookFollowUpService extends BaseService<OrderBookFollowUp> {
    PageInfo<OrderBookFollowUpVo> queryPage(OrderBookFollowUpQueryDto dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    OrderBookFollowUp getDetailById(String id);

    /**
     * 构造查询条件
     * @param dto
     * @return
     */
    BaseQueryWrapper<OrderBookFollowUp> buildQueryWrapper(OrderBookFollowUpQueryDto dto);

    /**
     *  * 订货本跟进-分页条件查询
     * @param queryWrapper  查询条件
     * @return  分页结果
     */
    List<OrderBookFollowUpVo> querylist(QueryWrapper<OrderBookFollowUp> queryWrapper);
}
