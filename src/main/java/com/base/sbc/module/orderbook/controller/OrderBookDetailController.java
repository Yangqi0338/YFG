package com.base.sbc.module.orderbook.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "订货本")
@RequestMapping(value = BaseController.SAAS_URL + "/orderBookDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class OrderBookDetailController extends BaseController {

    private final OrderBookDetailService orderBookDetailService;
    private final StyleColorService styleColorService;
    private final MessageUtils messageUtils;
    private final StylePicUtils stylePicUtils;
    private final OrderBookService orderBookService;
    private final StylePricingService stylePricingService;

    @ApiOperation(value = "订货本详情-分页条件查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage(OrderBookDetailQueryDto dto) {
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        return selectSuccess(orderBookDetailService.queryPage(dto));
    }

    @ApiOperation(value = "订货本详情-修改")
    @PostMapping("/orderBookDetailUpdate")
        public ApiResult orderBookDetailUpdate(@RequestBody OrderBookDetailSaveDto dto) {
        return insertSuccess(orderBookDetailService.updateById(dto));
    }

    /**
     * 设计师填写资料
     */
    @ApiOperation(value = "订货本详情-设计师填写资料")
    @PostMapping("/designConfirm")
    @DuplicationCheck(time = 10)
    @Transactional(rollbackFor = Exception.class)
    public boolean designConfirm(@RequestBody OrderBookDetailSaveDto dto) {
        return orderBookDetailService.designConfirm(dto);
    }



    /**
     * 商企填写资料
     */
    @ApiOperation(value = "订货本详情-商企填写资料")
    @PostMapping("/businessConfirm")
    @DuplicationCheck(time = 10)
    @Transactional(rollbackFor = Exception.class)
    public ApiResult businessConfirm(@RequestBody OrderBookDetailSaveDto dto) {
        dto.setBusinessConfirm("1");
        //修改吊牌价
        styleColorService.updateTagPrice(dto.getStyleColorId(),dto.getTagPrice());

        //修改倍率和系数
        StylePricingSaveDTO stylePricingSaveDTO =new StylePricingSaveDTO();
        stylePricingSaveDTO.setId(dto.getStylePricingId());
        stylePricingSaveDTO.setPackId(dto.getPackInfoId());
        stylePricingSaveDTO.setPlanningRate(dto.getRate());
        stylePricingSaveDTO.setProductStyle(dto.getProductStyleName());
        stylePricingService.insertOrUpdate(stylePricingSaveDTO,null);

        return insertSuccess(orderBookDetailService.updateById(dto));
    }

    /**
     * 添加大货款
     *
     * @param orderBookDetailSaveDto
     * @return 是否成功
     */
    @ApiOperation(value = "订货本详情-添加大货款")
    @PostMapping("/addByStyleColorIds")
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addByStyleColorIds(@RequestBody OrderBookDetailSaveDto orderBookDetailSaveDto) {
        if (orderBookDetailSaveDto == null) {
            throw new OtherException("参数不可为空");
        }
        if (StringUtils.isEmpty(orderBookDetailSaveDto.getOrderBookId()) || orderBookDetailSaveDto.getStyleColorIds()==null) {
            throw new OtherException("订货本id或者款式配色id不可为空");
        }


        OrderBook orderBook = orderBookService.getById(orderBookDetailSaveDto.getOrderBookId());
        if (orderBook==null){
            throw new OtherException("订货本不存在");
        }

        //相同产品季下不能有同样的大货款
        QueryWrapper<OrderBookDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_book_id",orderBookDetailSaveDto.getOrderBookId());
        queryWrapper.in("style_color_id", orderBookDetailSaveDto.getStyleColorIds());

        long l = orderBookDetailService.count(queryWrapper);
        if (l>0){
            throw new OtherException("大货款号已经添加过");
        }

        List<OrderBookDetail> list =new ArrayList<>();
        List<StyleColor> styleColors = styleColorService.listByIds(orderBookDetailSaveDto.getStyleColorIds());

        for (StyleColor styleColor : styleColors) {
            OrderBookDetail orderBookDetail = new OrderBookDetail();
            orderBookDetail.setStyleColorId(styleColor.getId());
            orderBookDetail.setOrderBookId(orderBookDetailSaveDto.getOrderBookId());
            list.add(orderBookDetail);
        }
        boolean b = orderBookDetailService.saveBatch(list);
        return insertSuccess(b);
    }

    @ApiOperation(value = "订货本详情-分配人员")
    @PostMapping("/assignPersonnel")
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public ApiResult assignPersonnel(@Valid @RequestBody OrderBookDetailSaveDto dto) {
        OrderBookDetail orderBookDetail = orderBookDetailService.getById(dto.getId());
        if (orderBookDetail==null){
            throw new OtherException("订货本详情不存在");
        }
        String userId;
        if ("1".equals(dto.getType())) {
            userId = dto.getDesignerId();
            if (StringUtils.isEmpty(userId)) {
                throw new OtherException("设计师id不可为空");
            }
            orderBookDetail.setDesignerId(userId);
            orderBookDetail.setDesignerName(dto.getDesignerName());
            orderBookDetail.setStatus("1");
        } else {
            userId = dto.getBusinessId();
            if (StringUtils.isEmpty(userId)) {
                throw new OtherException("商企id不可为空");
            }
            orderBookDetail.setBusinessId(userId);
            orderBookDetail.setBusinessName(dto.getBusinessName());
            orderBookDetail.setStatus("2");
        }
        boolean b = orderBookDetailService.updateById(orderBookDetail);
        if (b) {
            // 发送通知消息给对应的人员
            messageUtils.sendCommonMessage(userId, "您有新的订货本消息待处理", "/styleManagement/orderBook", stylePicUtils.getGroupUser());
        }
        return updateSuccess(b);
    }

    @ApiOperation(value = "订货本详情-设计师分配")
    @PostMapping("/assignmentDesigner")
    @DuplicationCheck
    public boolean assignmentDesigner(@Valid @RequestBody List<OrderBookDetailSaveDto> dto) {
        return  orderBookDetailService.assignmentDesigner(dto);
    }


    @ApiOperation(value = "订货本详情-导出")
    @GetMapping("/importExcel")
    @DuplicationCheck(type = 1,time = 100)
    public void importExcel(OrderBookDetailQueryDto dto, HttpServletResponse response) throws IOException {
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        orderBookDetailService.importExcel(dto,response);
    }

    @ApiOperation(value = "订货本详情根据id查询")
    @GetMapping("/getDetailById")
    public ApiResult getDetailById(String id) {
        OrderBookDetailVo orderBookDetailVo = orderBookDetailService.getDetailById(id);
        return selectSuccess(orderBookDetailVo) ;
    }

    @ApiOperation(value = "订货本-提交审批")
    @PostMapping("/submitForApproval")
    public ApiResult submitForApproval(@RequestBody OrderBookDetailSaveDto dto) {
        OrderBookDetail orderBookDetail = orderBookDetailService.getById(dto.getId());
        orderBookDetail.setStatus(dto.getStatus());
        orderBookDetailService.updateById(orderBookDetail);
        // TODO 2023/12/8 18:56:03 待确认是否需要发送消息
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "订货本-删除")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto) {
        boolean b = orderBookDetailService.removeByIds(removeDto);
        return deleteSuccess(b);
    }
}
