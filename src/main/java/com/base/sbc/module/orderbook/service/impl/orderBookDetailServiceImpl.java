package com.base.sbc.module.orderbook.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.mapper.OrderBookDetailMapper;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailExportVo;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.service.impl.StylePricingServiceImpl;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class orderBookDetailServiceImpl extends BaseServiceImpl<OrderBookDetailMapper, OrderBookDetail> implements OrderBookDetailService {

    private final StylePicUtils stylePicUtils;
    private final StylePricingServiceImpl stylePricingServiceimpl;
    private final DataPermissionsService dataPermissionsService;
    private final PackInfoService packInfoService;
    private final PackBomService packBomService;
    private final PackBomVersionService packBomVersionService;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;

    private final OrderBookService orderBookService;

    private final FieldValService fieldValService;

    @Override
    public PageInfo<OrderBookDetailVo> queryPage(OrderBookDetailQueryDto dto) {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        PageHelper.startPage(dto);
        List<OrderBookDetailVo> querylist = this.querylist(queryWrapper);
        return new PageInfo<>(querylist);
    }


    @Override
    public void importExcel(OrderBookDetailQueryDto dto, HttpServletResponse response) throws IOException {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        List<OrderBookDetailVo> orderBookDetailVos = this.querylist(queryWrapper);
        if (orderBookDetailVos.isEmpty()) {
            throw new RuntimeException("没有数据");
        }
        List<OrderBookDetailExportVo> orderBookDetailExportVos = BeanUtil.copyToList(orderBookDetailVos, OrderBookDetailExportVo.class);
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(orderBookDetailExportVos.size()))
                .build();

        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(orderBookDetailExportVos) && orderBookDetailExportVos.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                // stylePicUtils.setStylePic(orderBookDetailExportVos, "stylePic",30);
                // stylePicUtils.setStylePic(orderBookDetailExportVos, "styleColorPic",30);
                CountDownLatch countDownLatch = new CountDownLatch(orderBookDetailExportVos.size());
                for (OrderBookDetailExportVo orderBookDetailExportVo : orderBookDetailExportVos) {
                    executor.submit(() -> {
                        try {
                            final String stylePic = orderBookDetailExportVo.getStylePic();
                            // final String styleColorPic = orderBookDetailExportVo.getStyleColorPic();
                            orderBookDetailExportVo.setStylePic1(HttpUtil.downloadBytes(stylePic));
                            // orderBookDetailExportVo.setStyleColorPic1(HttpUtil.downloadBytes(styleColorPic));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();

                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(orderBookDetailExportVos, OrderBookDetailExportVo.class, "订货本详情.xlsx", new ExportParams("款式配色", "款式配色", ExcelType.HSSF), response);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }

        // ExcelUtils.exportExcel(orderBookDetailExportVos, OrderBookDetailExportVo.class, "订货本详情.xls", new ExportParams(), response);
    }

    @Override
    public List<OrderBookDetailVo> querylist(QueryWrapper<OrderBookDetail> queryWrapper) {
        List<OrderBookDetailVo> orderBookDetailVos = this.getBaseMapper().queryPage(queryWrapper);
        stylePicUtils.setStylePic(orderBookDetailVos, "stylePic");
        stylePicUtils.setStylePic(orderBookDetailVos, "styleColorPic");
        this.queryStylePrice(orderBookDetailVos);
        /*按配色id获取到里面的围度数据*/
        Map<String, FieldVal> map = new HashMap<>();
        /*获取配色中的围度里面的动态数据*/
        List<String> stringList = orderBookDetailVos.stream().map(OrderBookDetailVo::getStyleColorId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(stringList)){
            QueryWrapper<FieldVal> fieldValQueryWrapper = new QueryWrapper<>();
            fieldValQueryWrapper.in("foreign_id",stringList);
            fieldValQueryWrapper.eq("data_group", FieldValDataGroupConstant.STYLE_COLOR);
            /*版型定位*/
            fieldValQueryWrapper.eq("field_name","positioningCode");
            List<FieldVal> list = fieldValService.list(fieldValQueryWrapper);

            if(CollUtil.isNotEmpty(list)){
                map =   Optional.ofNullable(list).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(FieldVal::getForeignId, v -> v, (a, b) -> b));
            }
        }

        for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {
            /*版型定位字段*/
            FieldVal fieldValList = map.get(orderBookDetailVo.getStyleColorId());
            if(!ObjectUtil.isEmpty(fieldValList)){
                orderBookDetailVo.setPatternPositioningCode(fieldValList.getVal());
                orderBookDetailVo.setPatternPositioningName(fieldValList.getValName());
            }

            if ("CMT".equals(orderBookDetailVo.getDevtTypeName())){
                orderBookDetailVo.setSupplierNo("");
                orderBookDetailVo.setSupplierColor("");
                orderBookDetailVo.setSupplierAbbreviation("");
                /*"FOB取配色 cmt设置为空*/
                orderBookDetailVo.setFobClothingFactoryName("");
                orderBookDetailVo.setFobClothingFactoryCode("");

//                orderBookDetailVo.setCost("")

                String styleColorId = orderBookDetailVo.getStyleColorId();

                PackInfoListVo packInfo = packInfoService.getByQw(new QueryWrapper<PackInfo>().eq("code", orderBookDetailVo.getBom()).eq("pack_type", "0".equals(orderBookDetailVo.getBomStatus()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS).eq("style_color_id", styleColorId));
                if (packInfo != null) {
                    String packType = "0".equals(orderBookDetailVo.getBomStatus()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS;
                    PackBomVersion enableVersion = packBomVersionService.getEnableVersion(packInfo.getId(), packType);
                    if (enableVersion != null) {
                        List<PackBom> packBoms = packBomService.list(new QueryWrapper<PackBom>().eq("bom_version_id", enableVersion.getId()));
                        for (PackBom packBom : packBoms) {
                            if ("1".equals(packBom.getMainFlag())) {
                                orderBookDetailVo.setFabricState(packBom.getStatus());
                                orderBookDetailVo.setFabricFactoryCode(packBom.getSupplierId());
                                orderBookDetailVo.setUnitFabricDosage(packBom.getUnitUse());
                                orderBookDetailVo.setFabricFactoryName(packBom.getSupplierName());
                                BasicsdatumColourLibrary colourCode = basicsdatumColourLibraryService.getByOne("colour_code", packBom.getColorCode());
                                orderBookDetailVo.setFabricFactoryColorNumber(colourCode.getColor16());
                                orderBookDetailVo.setFabricCode(packBom.getMaterialCode());
                                orderBookDetailVo.setFabricComposition(packBom.getIngredient());
                            }
                        }
                    }
                }
            }
        }

        return orderBookDetailVos;
    }

    @Override
    public OrderBookDetailVo getDetailById(String id) {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tobl.id", id);
        List<OrderBookDetailVo> orderBookDetailVos = this.querylist(queryWrapper);
        if (Objects.nonNull(orderBookDetailVos) && !orderBookDetailVos.isEmpty()) {
            return orderBookDetailVos.get(0);
        }
        return null;
    }

    /**
     * 构造查询条件
     */
    @Override
    public BaseQueryWrapper<OrderBookDetail> buildQueryWrapper(OrderBookDetailQueryDto dto) {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tobl.order_book_id", dto.getOrderBookId());
        if (StringUtil.isNotEmpty(dto.getCommissioningDate())){
            queryWrapper.between("tobl.commissioning_date", dto.getCommissioningDate().split(","));
        }
        queryWrapper.notEmptyIn("tobl.id",dto.getId());
        queryWrapper.notEmptyEq("tsc.devt_type_name",dto.getDevtTypeName());
        queryWrapper.likeList("ts.prod_category", dto.getCategoryCode());
        queryWrapper.likeList("tsc.band_name", dto.getBand());
        queryWrapper.likeList("tobl.designer_id", dto.getDesignerId());
        queryWrapper.likeList("tsc.style_no", dto.getBulkStyleNo());
        queryWrapper.eq("tobl.company_code", dto.getCompanyCode());

        queryWrapper.and(qw -> qw.eq("tobl.designer_id", dto.getUserId()).
                or().eq("tobl.business_id", dto.getUserId())
                .or().eq("tobl.create_id", dto.getUserId()));
        if(StrUtil.isNotBlank(dto.getPlanningSeasonId())){
            BaseQueryWrapper<OrderBook> baseQueryWrapper = new BaseQueryWrapper();
            baseQueryWrapper.eq("season_id",dto.getPlanningSeasonId());
            List<OrderBook> list = orderBookService.list(baseQueryWrapper);
            List<String> stringList = list.stream().map(OrderBook::getId).collect(Collectors.toList());
            queryWrapper.in("tobl.order_book_id",stringList);
        }


        return queryWrapper;
    }

    /**
     * 查询款式定价数据
     */

    private void queryStylePrice(List<OrderBookDetailVo> orderBookDetailVos) {
        if (orderBookDetailVos != null && !orderBookDetailVos.isEmpty()) {
            List<String> bulkStyleNos = orderBookDetailVos.stream().map(OrderBookDetailVo::getBulkStyleNo).collect(Collectors.toList());
            BaseQueryWrapper qw = new BaseQueryWrapper();
            qw.in("ssc.style_no", bulkStyleNos);
            dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_pricing.getK(), "sd.");
            /*获取款式定价的列表*/
            List<StylePricingVO> stylePricingList = stylePricingServiceimpl.getBaseMapper().getStylePricingList(new StylePricingSearchDTO(), qw);
            List<StylePricingVO> stylePricingVOS = BeanUtil.copyToList(stylePricingList, StylePricingVO.class);
            /*款式定价数据组装处理*/
            stylePricingServiceimpl.dataProcessing(stylePricingVOS, orderBookDetailVos.get(0).getCompanyCode());
            /*按大货款号分类*/
            Map<String, StylePricingVO> map = Optional.ofNullable(stylePricingVOS).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(StylePricingVO::getBulkStyleNo, v -> v, (a, b) -> b));

            for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {
                /*获取款式定价的数据*/
                StylePricingVO stylePricingVO = map.get(orderBookDetailVo.getBulkStyleNo());
                if (!ObjectUtil.isEmpty(stylePricingVO)) {
                    orderBookDetailVo.setCmtCost(stylePricingVO.getTotalCost());
                    orderBookDetailVo.setCmtCarpetCost(stylePricingVO.getSewingProcessingFee());
                    orderBookDetailVo.setCmtTotalCost(stylePricingVO.getTotalCost());
                    orderBookDetailVo.setFobCost(stylePricingVO.getTotalCost());
                    orderBookDetailVo.setRate(stylePricingVO.getPlanActualMagnification());
                    orderBookDetailVo.setHonest(stylePricingVO.getPackagingFee());
                }

            }
        }

    }
}
