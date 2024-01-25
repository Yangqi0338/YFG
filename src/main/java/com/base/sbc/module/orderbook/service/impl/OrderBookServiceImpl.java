package com.base.sbc.module.orderbook.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.mapper.OrderBookDetailMapper;
import com.base.sbc.module.orderbook.mapper.OrderBookMapper;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookExportVo;
import com.base.sbc.module.orderbook.vo.OrderBookVo;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/12/5 13:48:09
 * @mail 247967116@qq.com
 */
@Service
public class OrderBookServiceImpl extends BaseServiceImpl<OrderBookMapper,OrderBook> implements OrderBookService {

    @Autowired
    private PlanningSeasonService planningSeasonService;
//
//    @Autowired
//    private DataPermissionsService dataPermissionsService;

//    @Autowired
//    private OrderBookDetailMapper orderBookDetailMapper;

    /**
     * @param dto
     * @return
     */
    @Override
    public PageInfo<OrderBookVo> queryPage(OrderBookQueryDto dto) {
        BaseQueryWrapper<OrderBook> orderBookBaseQueryWrapper = this.buildQueryWrapper(dto);
        orderBookBaseQueryWrapper.orderByDesc("id");
        PageHelper.startPage(dto);
        List<OrderBookVo> list = this.queryList(orderBookBaseQueryWrapper, dto);
        return new PageInfo<>(list);
    }

    @Override
    public List<OrderBookVo> queryList(QueryWrapper<OrderBook> queryWrapper, OrderBookQueryDto dto) {
        List<OrderBookVo> voList = this.baseMapper.queryList(queryWrapper, dto);
//        if (CollectionUtil.isNotEmpty(voList)) {
//            BaseQueryWrapper<OrderBookDetail> baseCountQuery = new BaseQueryWrapper<>();
//            dataPermissionsService.getDataPermissionsForQw(baseCountQuery, "style_order_book", "tobl.");
//            QueryWrapper<OrderBookDetail> ew = baseCountQuery.clone().in("tobl.order_book_id", voList.stream().map(OrderBookVo::getId).collect(Collectors.toList()));
//            List<Map<String, Object>> countList = orderBookDetailMapper.queryCountByOrderBookId(ew);
//            voList.forEach(vo-> {
//                countList.stream().filter(it-> it.getOrDefault("orderBookId","").equals(vo.getId())).findFirst().ifPresent(map-> {
//                    vo.setCount(Integer.parseInt(map.getOrDefault("count","0").toString()));
//                });
//            });
//        }
        return voList;
    }

    /**
     * @param dto
     * @param response
     */
    @Override
    public void importExcel(OrderBookQueryDto dto, HttpServletResponse response) throws IOException {
        BaseQueryWrapper<OrderBook> orderBookBaseQueryWrapper = this.buildQueryWrapper(dto);
        orderBookBaseQueryWrapper.orderByDesc("id");
        List<OrderBookVo> orderBookVos = this.queryList(orderBookBaseQueryWrapper, dto);
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
    @Transactional(rollbackFor = Exception.class)
    public OrderBook saveOrderBook(OrderBookSaveDto dto) {
        if(StrUtil.isEmpty(dto.getSeasonId())){
            throw new OtherException("产品季id不能为空");
        }
        /*校验订货本名称*/
        QueryWrapper<OrderBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", dto.getName());
        queryWrapper.ne(!StrUtil.isEmpty(dto.getId()),"id",dto.getId());
        queryWrapper.eq("season_id",dto.getSeasonId());

        long l = this.count(queryWrapper);
        if (l > 0){
             throw new OtherException("订货本名称已存在");
        }
        // OrderBook orderBook = new OrderBook();
        /*修改订货本名称*/
        // if(StrUtil.isNotBlank(dto.getId())) {
        //     orderBook = baseMapper.selectById(dto.getId());
        //     /*只编辑名称*/
        //     orderBook.setName(dto.getName());
        //     baseMapper.updateById(orderBook);
        //     return orderBook;
        // }
        /*查询产品季*/
        PlanningSeason planningSeason = planningSeasonService.getById(dto.getSeasonId());
        if (ObjectUtil.isEmpty(planningSeason)) {
            throw new OtherException("产品季id错误，没有产品季");
        }

        if (StrUtil.isNotBlank(dto.getId())){
            this.updateById(dto);
        }else {
            this.save(dto);
        }
        return this.getById(dto.getId());
        /*做新增*/
        // orderBook.setSeasonId(planningSeason.getId());
        // orderBook.setSeasonName(planningSeason.getSeasonName());
        // orderBook.setName(dto.getName());
        // baseMapper.insert(orderBook);
        // return orderBook;
    }

    /**
     * 构造查询条件
     */
    private BaseQueryWrapper<OrderBook> buildQueryWrapper(OrderBookQueryDto dto) {
        BaseQueryWrapper<OrderBook> queryWrapper=new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tob.season_id",dto.getSeasonId());
        queryWrapper.notEmptyEq("tob.status",dto.getStatus());
        queryWrapper.likeList("tob.name",dto.getName());
        queryWrapper.notNullEq("tob.order_status", dto.getOrderStatus());
        return queryWrapper;
    }
}
