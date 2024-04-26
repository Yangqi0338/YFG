package com.base.sbc.module.orderbook.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.orderbook.dto.MaterialUpdateDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailSaveDto;
import com.base.sbc.module.orderbook.dto.QueryOrderDetailDTO;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.vo.*;
import com.base.sbc.module.smp.dto.HttpResp;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OrderBookDetailService extends BaseService<OrderBookDetail> {
    /**
     *  * 订货本详情-分页条件查询
     * @param dto  查询条件
     * @return  分页结果
     */
    OrderBookDetailPageVo queryPage(OrderBookDetailQueryDto dto);

    List<OrderBookDetailVo> querylist(QueryWrapper<OrderBookDetail> queryWrapper, Integer... judgeGroup);

    /**
     * * 订货本详情-导出
     *
     * @param dto       查询条件
     * @param tableCode
     */
    void importExcel(OrderBookDetailQueryDto dto, HttpServletResponse response, String tableCode) throws IOException;

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

    /**
     * 订货本详情-设计师分配
     * @param dto
     * @return
     */
    boolean assignmentDesigner(List<OrderBookDetailSaveDto> dto);

    Map<String, Double> queryCount(OrderBookDetailQueryDto dto);

    void submitForApproval(OrderBookDetailSaveDto dto);

    String placeAnOrderReject(OrderBookDetailQueryDto dto);

    boolean placeAnOrder(OrderBookDetailQueryDto dto);

    boolean assignPersonnel(OrderBookDetailSaveDto dto);

    Map<OrderBookChannelType, OrderBookDetailPageConfigVo> pageConfig(OrderBookDetailQueryDto dto);

    PageInfo<OrderBookSimilarStyleVo> similarStyleList(OrderBookDetailQueryDto dto);

    String placeAnProduction(OrderBookDetailQueryDto dto);

    String handlePlaceAnCancelProduction(List<OrderBookDetail> list, List<HttpResp> httpRespList);

    String handlePlaceAnProduction(List<OrderBookDetail> list, List<HttpResp> httpRespList);

    boolean updateMaterial(MaterialUpdateDto dto);

    UpdateResultVo businessConfirm(OrderBookDetailSaveDto dto);

    boolean removeByIds(RemoveDto removeDto);

    boolean similarStyleBinding(OrderBookDetailSaveDto dto);

    List<OrderBookDetailForSeasonPlanningVO> querySeasonalPlanningOrder(QueryOrderDetailDTO dto);
}
