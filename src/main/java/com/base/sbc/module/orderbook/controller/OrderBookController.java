package com.base.sbc.module.orderbook.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.orderbook.dto.OrderBookQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/12/5 13:32:41
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/orderBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@Api(tags = "订货本")
public class OrderBookController extends BaseController {
    private final OrderBookService orderBookService;
    private final OrderBookDetailService orderBookDetailService;
    /**
     * 分页条件查询
     */
    @ApiOperation(value = "订货本-分页条件查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage(OrderBookQueryDto dto) {
        return selectSuccess(orderBookService.queryPage(dto));
    }

    /**
     * 新建订货本
     */
    @ApiOperation(value = "新建订货本")
    @PostMapping("/save")
    @DuplicationCheck
    public ApiResult save(@RequestBody OrderBookSaveDto dto) {
        return insertSuccess(orderBookService.saveOrderBook(dto));
    }

    /**
     * 订货本下单
     * @param dtos
     * @return
     */
    @ApiOperation(value = "订货本-下单")
    @PostMapping("/saveOrder")
    public ApiResult saveOrder(@RequestBody List<OrderBookSaveDto> dtos) {
        if (dtos==null || dtos.isEmpty()){
            throw new RuntimeException("数据不能为空");
        }
        List<String> ids = dtos.stream().map(OrderBookSaveDto::getId).collect(Collectors.toList());
        UpdateWrapper<OrderBook> updateWrapper =new UpdateWrapper<>();
        updateWrapper.in("id",ids);
        updateWrapper.set("status", "3");
        orderBookService.update(updateWrapper);
        return updateSuccess("下单成功");
    }

    @ApiOperation(value = "订货本-保存")
    @PostMapping("/saveList")
    @DuplicationCheck
    public ApiResult saveList(@RequestBody List<OrderBookSaveDto> dtos) {
        for (OrderBookSaveDto dto : dtos) {
            BaseQueryWrapper<OrderBook> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("season_id", dto.getSeasonId());
            if (StringUtils.isNotEmpty(dto.getId())){
                queryWrapper.ne("id", dto.getId());
                queryWrapper.ne("name", dto.getName());
            }
            if (orderBookService.count(queryWrapper) > 0) {
                throw new RuntimeException("该季节下已存在同名订货本");
            }
            orderBookService.save(dto);
        }

        return insertSuccess("新增成功");
    }
    /**
     * 查询订货本各状态数量
     */
    @ApiOperation(value = "订货本-查询各状态数量")
    @GetMapping("/countByStatus")
    public ApiResult countByStatus(OrderBookQueryDto dto) {
        return selectSuccess(orderBookService.countByStatus(dto));
    }

    /**
     * 导出
     */
    @ApiOperation(value = "订货本-导出")
    @GetMapping("/importExcel")
    public void importExcel(OrderBookQueryDto dto, HttpServletResponse response) throws IOException {
        orderBookService.importExcel(dto,response);
    }

    /**
     * 删除订货本
     */
    @ApiOperation(value = "订货本-根据ids删除")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto) {
        long l = orderBookDetailService.count(new QueryWrapper<OrderBookDetail>().in("order_book_id", Arrays.asList(removeDto.getIds().split(","))));
        if (l > 0) {
            throw new RuntimeException("该订货本下已存在大货款，不能删除");
        }
        boolean b = orderBookService.removeByIds(removeDto);
        return deleteSuccess(b);
    }
}
