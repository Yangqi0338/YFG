package com.base.sbc.module.orderbook.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.orderbook.dto.OrderBookQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.mapper.OrderBookMapper;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookExportVo;
import com.base.sbc.module.orderbook.vo.OrderBookVo;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/12/5 13:48:09
 * @mail 247967116@qq.com
 */
@Service
public class OrderBookServiceImpl extends BaseServiceImpl<OrderBookMapper,OrderBook> implements OrderBookService {

    @Autowired
    private PlanningSeasonService planningSeasonService;

    /**
     * @param dto
     * @return
     */
    @Override
    public PageInfo<OrderBookVo> queryPage(OrderBookQueryDto dto) {
        BaseQueryWrapper<OrderBook> orderBookBaseQueryWrapper = this.buildQueryWrapper(dto);
        PageHelper.startPage(dto);
        List<OrderBookVo> list = this.queryList(orderBookBaseQueryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<OrderBookVo> queryList(QueryWrapper<OrderBook> queryWrapper) {
        return this.baseMapper.queryList(queryWrapper);
    }

    /**
     * @param dto
     * @param response
     */
    @Override
    public void importExcel(OrderBookQueryDto dto, HttpServletResponse response) throws IOException {
        BaseQueryWrapper<OrderBook> orderBookBaseQueryWrapper = this.buildQueryWrapper(dto);
        List<OrderBookVo> orderBookVos = this.queryList(orderBookBaseQueryWrapper);
        List<OrderBookExportVo> orderBookExportVos = BeanUtil.copyToList(orderBookVos, OrderBookExportVo.class);
        ExcelUtils.exportExcel(orderBookExportVos, OrderBookExportVo.class, "订货本.xls", new ExportParams(), response);
    }

    @Override
    public List<Map<String,String>>  countByStatus(OrderBookQueryDto dto) {
        BaseQueryWrapper<OrderBook> orderBookBaseQueryWrapper = this.buildQueryWrapper(dto);
        return this.baseMapper.countByStatus(orderBookBaseQueryWrapper);
    }

    /**
     * 新建订货本
     *
     * @param dto
     * @return
     */
    @Override
    public OrderBook saveOrderBook(OrderBookSaveDto dto) {
        /*校验订货本名称*/
        OrderBook byOne = getByOne("name", dto.getName());
        if (!ObjectUtil.isEmpty(byOne)) {
            throw new OtherException("订货本名称重复");
        }
        /*查询产品季*/
        QueryWrapper<PlanningSeason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand", dto.getBrand());
        queryWrapper.eq("year", dto.getYear());
        queryWrapper.eq("season", dto.getSeason());
        queryWrapper.eq("status", BaseGlobal.NO);
        /*查询产品季*/
        PlanningSeason planningSeason = planningSeasonService.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(planningSeason)) {
            throw new OtherException("该年份季节品牌下没有产品季");
        }
        OrderBook orderBook = new OrderBook();
        orderBook.setSeasonId(planningSeason.getId());
        orderBook.setSeasonName(planningSeason.getSeasonName());
        orderBook.setName(dto.getName());
        baseMapper.insert(orderBook);
        return orderBook;
    }

    /**
     * 构造查询条件
     */
    private BaseQueryWrapper<OrderBook> buildQueryWrapper(OrderBookQueryDto dto) {
        BaseQueryWrapper<OrderBook> queryWrapper=new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tob.season_id",dto.getSeasonId());
        queryWrapper.notEmptyEq("tob.status",dto.getStatus());
        return queryWrapper;
    }
}
