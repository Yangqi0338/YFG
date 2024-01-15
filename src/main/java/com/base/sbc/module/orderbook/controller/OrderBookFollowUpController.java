package com.base.sbc.module.orderbook.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.orderbook.dto.OrderBookFollowUpQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookFollowUpSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBookFollowUp;
import com.base.sbc.module.orderbook.service.OrderBookFollowUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author 卞康
 * @date 2024-01-15 14:59:38
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "订货本跟进")
@RequestMapping(value = BaseController.SAAS_URL + "/orderBookFollowUp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class OrderBookFollowUpController extends BaseController {
    private final OrderBookFollowUpService orderBookFollowUpService;

    @ApiOperation(value = "订货本跟进-提交审批")
    @PostMapping("/submitForApproval")
    public ApiResult submitForApproval(@RequestBody OrderBookFollowUpSaveDto dto) {
        // OrderBookDetail orderBookDetail = orderBookDetailService.getById(dto.getId());
        // orderBookDetail.setStatus(dto.getStatus());
        // orderBookDetailService.updateById(orderBookDetail);

        // 提交审核生产订货本跟进
        dto.setStatus("1");
        orderBookFollowUpService.save(dto);
        return updateSuccess("操作成功");
    }

    /**
     * 分页条件查询
     */
    @ApiOperation(value = "分页条件查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage(OrderBookFollowUpQueryDto dto) {
        return selectSuccess(orderBookFollowUpService.queryPage(dto));
    }

}
