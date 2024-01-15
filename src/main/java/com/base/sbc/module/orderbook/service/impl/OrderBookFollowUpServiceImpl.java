package com.base.sbc.module.orderbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookFollowUpQueryDto;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.OrderBookFollowUp;
import com.base.sbc.module.orderbook.mapper.OrderBookFollowUpMapper;
import com.base.sbc.module.orderbook.service.OrderBookFollowUpService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.orderbook.vo.OrderBookFollowUpVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 卞康
 * @date 2024-01-15 15:01:21
 * @mail 247967116@qq.com
 */
@Service
public class OrderBookFollowUpServiceImpl extends BaseServiceImpl<OrderBookFollowUpMapper, OrderBookFollowUp> implements OrderBookFollowUpService {
    /**
     * @param dto
     * @return
     */
    @Override
    public PageInfo<OrderBookFollowUpVo> queryPage(OrderBookFollowUpQueryDto dto) {
        BaseQueryWrapper<OrderBookFollowUp> queryWrapper = this.buildQueryWrapper(dto);
        PageHelper.startPage(dto);
        List<OrderBookFollowUpVo> querylist = this.querylist(queryWrapper);
        return new PageInfo<>(querylist);
    }

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderBookFollowUp getDetailById(String id) {
        BaseQueryWrapper<OrderBookFollowUp> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tobl.id", id);
        List<OrderBookFollowUpVo> orderBookFollowUps = this.querylist(queryWrapper);
        if (Objects.nonNull(orderBookFollowUps) && !orderBookFollowUps.isEmpty()) {
            return orderBookFollowUps.get(0);
        }
        return null;
    }

    /**
     * 构造查询条件
     *
     * @param dto
     * @return
     */
    @Override
    public BaseQueryWrapper<OrderBookFollowUp> buildQueryWrapper(OrderBookFollowUpQueryDto dto) {
        BaseQueryWrapper<OrderBookFollowUp> baseQueryWrapper =new BaseQueryWrapper<>();
        baseQueryWrapper.between("tobl.productionDate",dto.getProductionDate());
        baseQueryWrapper.notEmptyEq("tobl.id", dto.getId());
        baseQueryWrapper.notEmptyEq("tobl.seasonId", dto.getSeasonId());
        return baseQueryWrapper;
    }

    @Override
    public List<OrderBookFollowUpVo> querylist(QueryWrapper<OrderBookFollowUp> queryWrapper) {
        List<OrderBookFollowUpVo> orderBookFollowUps = this.getBaseMapper().queryPage(queryWrapper);
        return orderBookFollowUps;
    }
}
