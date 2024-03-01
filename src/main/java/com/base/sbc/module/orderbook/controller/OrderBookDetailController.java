package com.base.sbc.module.orderbook.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailSaveDto;
import com.base.sbc.module.orderbook.dto.OrderBookFollowUpSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.OrderBookFollowUp;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailPageVo;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.style.dto.PublicStyleColorDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "订货本")
@RequestMapping(value = BaseController.SAAS_URL + "/orderBookDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class OrderBookDetailController extends BaseController {

    private final OrderBookDetailService orderBookDetailService;
    private final StyleColorService styleColorService;
    private final StyleService styleService;
    private final MessageUtils messageUtils;
    private final StylePicUtils stylePicUtils;
    private final OrderBookService orderBookService;
    private final StylePricingService stylePricingService;
    private final AmcFeignService amcFeignService;

    @ApiOperation(value = "订货本详情-分页条件查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage(OrderBookDetailQueryDto dto) {
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        PageInfo<OrderBookDetailVo> pageInfo = orderBookDetailService.queryPage(dto);
        OrderBookDetailPageVo pageVo = BeanUtil.copyProperties(pageInfo,OrderBookDetailPageVo.class);
        pageVo.setTotalMap(orderBookDetailService.queryCount(dto));
        return selectSuccess(pageVo);
    }

    /**
     * 重新排序
     */
    @ApiOperation(value = "订货本详情-重新排序")
    @PostMapping("/reorder")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult reorder(@RequestBody List<OrderBookDetail> list) {
        boolean b = orderBookDetailService.updateBatchById(list);
        return selectSuccess(b);
    }

    /**
     * 查询统计数量
     */
    @ApiOperation(value = "订货本详情-统计数量")
    @GetMapping("/queryCount")
    public ApiResult queryCount(OrderBookDetailQueryDto dto) {
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        return selectSuccess(orderBookDetailService.queryCount(dto));
    }

    @ApiOperation(value = "订货本-提交审批")
    @PostMapping("/submitForApproval")
    public ApiResult submitForApproval(@RequestBody OrderBookDetailSaveDto dto) {
        orderBookDetailService.submitForApproval(dto);
        return updateSuccess("操作成功");
    }
    @ApiOperation(value = "订货本详情-修改")
    @PostMapping("/orderBookDetailUpdate")
        public ApiResult orderBookDetailUpdate(@RequestBody OrderBookDetailSaveDto dto) {
        return insertSuccess(orderBookDetailService.updateById(dto));
    }

    /**
     * 订货本下单
     */
    @ApiModelProperty(value = "订货本详情-下单")
    @PostMapping("/placeAnOrder")
    public ApiResult placeAnOrder(@RequestBody OrderBookDetailQueryDto dto) {
        String ids = dto.getIds();
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        return updateSuccess(orderBookDetailService.placeAnOrder(dto, ids));
    }

    /**
     * 首单下单历史数据
     */
    @ApiModelProperty(value = "首单下单历史数据")
    @GetMapping("/firstOrderHis")
    public ApiResult firstOrderHis(@RequestBody OrderBookDetailQueryDto dto) {
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        return updateSuccess(orderBookDetailService.queryPage(dto));
    }

    /**
     * 全部下单
     */
    @ApiModelProperty(value = "订货本详情-全部下单")
    @PostMapping("/placeAnOrderAll")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult placeAnOrderAll(@RequestBody OrderBookDetailQueryDto dto) {
        String orderBookId = dto.getOrderBookId();
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        return updateSuccess(orderBookDetailService.placeAnOrder(dto, orderBookId));
    }

    /**
     * 驳回
     */
    @ApiModelProperty(value = "订货本详情-驳回")
    @PostMapping("/placeAnOrderReject")
    public ApiResult placeAnOrderReject(@RequestBody OrderBookDetailQueryDto dto) {
        orderBookDetailService.placeAnOrderReject(dto);
        return updateSuccess("驳回成功");
    }

    /**
     * 取消锁定
     */
    @ApiOperation(value = "订货本详情-取消锁定")
    @PostMapping("/cancelLock")
    public ApiResult cancelLock(@RequestBody OrderBookDetailQueryDto dto) {
        if (StringUtils.isEmpty(dto.getIds())) {
            return updateSuccess("请选订货本");
        }
        BaseQueryWrapper<OrderBookDetail> queryWrapper = orderBookDetailService.buildQueryWrapper(dto);
        List<OrderBookDetailVo> orderBookDetails = orderBookDetailService.querylist(queryWrapper, null);
        for (OrderBookDetailVo orderBookDetail :orderBookDetails) {
            orderBookDetail.setIsLock(YesOrNoEnum.NO);
        }
        List<OrderBookDetail> orderBookDetails1 = BeanUtil.copyToList(orderBookDetails, OrderBookDetail.class);
        return updateSuccess(orderBookDetailService.updateBatchById(orderBookDetails1));
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
        stylePricingService.updateById(stylePricingSaveDTO);

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
        List<Style> styles = styleService.listByIds(styleColors.stream().map(StyleColor::getStyleId).collect(Collectors.toList()));

        for (StyleColor styleColor : styleColors) {
            OrderBookDetail orderBookDetail = new OrderBookDetail();
            orderBookDetail.setStyleColorId(styleColor.getId());
            orderBookDetail.setOrderBookId(orderBookDetailSaveDto.getOrderBookId());
            String brand = styles.stream().filter(it-> it.getId().equals(styleColor.getStyleId())).findFirst().map(Style::getBrand).orElse("");
            orderBookDetail.setBrand(brand);
            list.add(orderBookDetail);
        }
        boolean b = orderBookDetailService.saveBatch(list);
        return insertSuccess(b);
    }

    @ApiOperation(value = "订货本详情-分配商企")
    @PostMapping("/assignPersonnel")
    @DuplicationCheck
    public ApiResult assignPersonnel(@Validated({OrderBookDetailSaveDto.AssignPersonnel.class}) @RequestBody OrderBookDetailSaveDto dto) {
        return updateSuccess(orderBookDetailService.assignPersonnel(dto));
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
    public void importExcel(OrderBookDetailQueryDto dto, HttpServletResponse response,String tableCode) throws IOException {
        dto.setCompanyCode(super.getUserCompany());
        dto.setUserId(super.getUserId());
        orderBookDetailService.importExcel(dto,response,tableCode);
    }

    @ApiOperation(value = "订货本详情根据id查询")
    @GetMapping("/getDetailById")
    public ApiResult getDetailById(String id) {
        OrderBookDetailVo orderBookDetailVo = orderBookDetailService.getDetailById(id);
        return selectSuccess(orderBookDetailVo) ;
    }


    @ApiOperation(value = "订货本-删除")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto) {
        boolean b = orderBookDetailService.removeByIds(removeDto);
        return deleteSuccess(b);
    }

    /**
     * 修改套装款号
     */
    @ApiOperation(value = "修改套装款号")
    @PostMapping("/setStyleNumber")
    public ApiResult setStyleNumber(@RequestBody OrderBookDetailSaveDto dto) {
        OrderBookDetailVo orderBookDetailVo = orderBookDetailService.getDetailById(dto.getId());
        //先清掉之前关联的款号
        String bulkStyleNo = orderBookDetailVo.getBulkStyleNo();
        QueryWrapper<OrderBookDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("suit_no",bulkStyleNo);
        List<OrderBookDetail> list = orderBookDetailService.list(queryWrapper);
        for (OrderBookDetail orderBookDetail : list) {
            String suitNo = orderBookDetail.getSuitNo();
            String[] split = suitNo.split(",");
            List<String> list1 = StringUtils.convertStrsToList(split);
            // List<String> list1 = Arrays.asList(split);

            // list1.remove(bulkStyleNo);
            list1.removeIf(s -> s.equals(bulkStyleNo));

            if (!list1.isEmpty()){
                orderBookDetail.setSuitNo(StringUtils.join(list1, ","));
            }else {
                orderBookDetail.setSuitNo("");
            }
            orderBookDetailService.updateById(orderBookDetail);
        }

        //再做关联,把被关联的其他款，也关联上当前款号
        if (StringUtils.isNotBlank(dto.getSuitNo())){
            OrderBookDetailQueryDto orderBookDetailQueryDto = new OrderBookDetailQueryDto();
            orderBookDetailQueryDto.setBulkStyleNo(dto.getSuitNo());
            BaseQueryWrapper<OrderBookDetail> queryWrapper1 = orderBookDetailService.buildQueryWrapper(orderBookDetailQueryDto);
            List<OrderBookDetailVo> orderBookDetailVos = orderBookDetailService.querylist(queryWrapper1,null);
            for (OrderBookDetailVo bookDetailVo : orderBookDetailVos) {
                String suitNo = bookDetailVo.getSuitNo();
                if (StringUtils.isNotBlank(suitNo)){
                    String[] split1 = suitNo.split(",");
                    Set<String> list2 = new HashSet<>(Arrays.asList(split1));
                    list2.add(bulkStyleNo);
                    bookDetailVo.setSuitNo(StringUtils.join(list2, ","));
                }else {
                    bookDetailVo.setSuitNo(bulkStyleNo);
                }
                orderBookDetailService.updateById(bookDetailVo);
            }
        }

        boolean b = orderBookDetailService.updateById(dto);
        return updateSuccess(b);
    }
}
