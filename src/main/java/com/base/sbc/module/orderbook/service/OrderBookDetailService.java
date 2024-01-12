package com.base.sbc.module.orderbook.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface OrderBookDetailService extends BaseService<OrderBookDetail> {
    /**
     *  * 订货本详情-分页条件查询
     * @param dto  查询条件
     * @return  分页结果
     */
    PageInfo<OrderBookDetailVo> queryPage(OrderBookDetailQueryDto dto);

    /**
     *  * 订货本详情-分页条件查询
     * @param queryWrapper  查询条件
     * @return  分页结果
     */
    List<OrderBookDetailVo> querylist(QueryWrapper<OrderBookDetail> queryWrapper);

    /**
     *  * 订货本详情-导出
     * @param dto  查询条件
     *
     */
    void importExcel(OrderBookDetailQueryDto dto, HttpServletResponse response) throws IOException;

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    OrderBookDetailVo getDetailById(String id);

    /**
     * 构造查询条件
     * @param dto
     * @return
     */
    BaseQueryWrapper<OrderBookDetail> buildQueryWrapper(OrderBookDetailQueryDto dto);


    /**
     * 设计师补充数据
     * @param dto
     * @return
     */
    boolean designConfirm( OrderBookDetailSaveDto dto);

}
