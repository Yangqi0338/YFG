package com.base.sbc.module.orderbook.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.func.Consumer3;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StylePutIntoType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookOrderStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.common.dto.BasePageInfo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailSaveDto;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.base.sbc.module.orderbook.mapper.OrderBookDetailMapper;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailExportVo;
import com.base.sbc.module.orderbook.vo.OrderBookDetailPageConfigVo;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleChannelVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleSizeMapVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleVo;
import com.base.sbc.module.orderbook.vo.StyleSaleIntoDto;
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
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.dto.PublicStyleColorDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.ORDER_BOOK_CV;

@Service
@RequiredArgsConstructor
public class orderBookDetailServiceImpl extends BaseServiceImpl<OrderBookDetailMapper, OrderBookDetail> implements OrderBookDetailService {

    private final StylePicUtils stylePicUtils;
    private final StylePricingServiceImpl stylePricingServiceimpl;
    private final DataPermissionsService dataPermissionsService;
    private final PackInfoService packInfoService;
    private final PackBomService packBomService;
    private final PackBomVersionService packBomVersionService;
    private final AmcFeignService amcFeignService;

    private final OrderBookService orderBookService;

    private final FieldValService fieldValService;

    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    private final StyleColorService styleColorService;
    private final StyleService styleService;

    private final MessageUtils messageUtils;
    private final BasicsdatumSizeService sizeService;
    @Resource
    @Lazy
    private SmpService smpService;

    @Override
    public BasePageInfo<OrderBookDetailVo> queryPage(OrderBookDetailQueryDto dto) {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        PageHelper.startPage(dto);
        List<OrderBookDetailVo> querylist = this.querylist(queryWrapper,null);
        return new BasePageInfo<>(querylist);
    }


    @Override
    public void importExcel(OrderBookDetailQueryDto dto, HttpServletResponse response, String tableCode) throws IOException {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        List<OrderBookDetailVo> orderBookDetailVos = this.querylist(queryWrapper,null);
        if (orderBookDetailVos.isEmpty()) {
            throw new RuntimeException("没有数据");
        }
        List<OrderBookDetailExportVo> orderBookDetailExportVos = BeanUtil.copyToList(orderBookDetailVos, OrderBookDetailExportVo.class);
        //导出
        // ExcelUtils.executorExportExcel();
        ExportParams exportParams = new ExportParams("订货本详情", "订货本详情", ExcelType.HSSF);
        ExcelUtils.exportExcelByTableCode(orderBookDetailExportVos, OrderBookDetailExportVo.class,"订货本详情",exportParams,response,tableCode,dto.getImgFlag(),3000,"stylePic","styleColorPic");
    }

    @Override
    public List<OrderBookDetailVo> querylist(QueryWrapper<OrderBookDetail> queryWrapper,Integer openDataAuth) {
        if (null == openDataAuth) {
            dataPermissionsService.getDataPermissionsForQw(queryWrapper, "style_order_book", "tobl.");
        }
        List<OrderBookDetailVo> orderBookDetailVos = this.getBaseMapper().queryPage(queryWrapper);
        if (CollUtil.isEmpty(orderBookDetailVos)) return orderBookDetailVos;

        List<OrderBook> orderBookList = orderBookService.list(new LambdaQueryWrapper<OrderBook>()
                .in(OrderBook::getId, orderBookDetailVos.stream().map(OrderBookDetailVo::getOrderBookId).collect(Collectors.toList())));
        /*设置图片分辨路*/
        stylePicUtils.setStylePic(orderBookDetailVos, "stylePic",30);
        stylePicUtils.setStylePic(orderBookDetailVos, "styleColorPic",30);
        OrderBookDetailQueryDto pageConfigQueryDto = new OrderBookDetailQueryDto();
        pageConfigQueryDto.setCompanyCode(orderBookDetailVos.get(0).getCompanyCode());
        Map<OrderBookChannelType, OrderBookDetailPageConfigVo> channelPageConfig = pageConfig(pageConfigQueryDto);

        //查询BOM版本
        for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {
            QueryWrapper<PackBomVersion> queryWrapper1 =new BaseQueryWrapper<>();
            queryWrapper1.eq("foreign_id",orderBookDetailVo.getPackInfoId());
            orderBookDetailVo.setPackType("0".equals(orderBookDetailVo.getBomStatus())?"packDesign":"packBigGoods");
            queryWrapper1.eq("pack_type",orderBookDetailVo.getPackType());
            queryWrapper1.eq("status","1");
            queryWrapper1.orderByDesc("version");
            queryWrapper1.last("limit 1");
            PackBomVersion packBomVersion = packBomVersionService.getOne(queryWrapper1);
            if (packBomVersion!=null){
                orderBookDetailVo.setBomVersionId(packBomVersion.getId());
            }

        }

        /*款式定价相关参数*/
        this.queryStylePrice(orderBookDetailVos,openDataAuth);
        /*按配色id获取到里面的围度数据*/
        // Map<String, FieldVal> map = new HashMap<>();
        /*获取配色中的围度里面的动态数据*/
        // // List<String> stringList = orderBookDetailVos.stream().map(OrderBookDetailVo::getStyleColorId).collect(Collectors.toList());
        // if(CollUtil.isNotEmpty(stringList)){
            // QueryWrapper<FieldVal> fieldValQueryWrapper = new QueryWrapper<>();
            // fieldValQueryWrapper.in("foreign_id",stringList);
            // fieldValQueryWrapper.eq("data_group", FieldValDataGroupConstant.STYLE_COLOR);
            // /*版型定位*/
            // fieldValQueryWrapper.eq("field_name","positioningCode");
            // List<FieldVal> list = fieldValService.list(fieldValQueryWrapper);

            // if(CollUtil.isNotEmpty(list)){
            //     // map =   Optional.ofNullable(list).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(FieldVal::getForeignId, v -> v, (a, b) -> b));
            // }
        // }

        String sizeCodes = orderBookDetailVos.stream().map(OrderBookDetailVo::getSizeCodes).filter(StrUtil::isNotBlank)
                .flatMap(it-> Stream.of(it.split(COMMA))).distinct().collect(Collectors.joining(COMMA));
        List<BasicsdatumSize> sizeList = sizeService.list(new BaseLambdaQueryWrapper<BasicsdatumSize>()
                .notEmptyIn(BasicsdatumSize::getCode, sizeCodes)
                .select(BasicsdatumSize::getModel, BasicsdatumSize::getInternalSize, BasicsdatumSize::getCode));

        List<String> packBomIds = orderBookDetailVos.stream().map(it ->
                Opt.ofNullable(it.getUnitFabricDosageIds()).orElse("")
                        + COMMA +
                Opt.ofNullable(it.getUnitDosageIds()).orElse("")
        ).flatMap(it -> Arrays.stream(it.split(COMMA))).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());

        List<PackBom> packBoms = new ArrayList<>();
        if (CollUtil.isNotEmpty(packBomIds)) {
            packBoms.addAll(packBomService.list(new LambdaQueryWrapper<PackBom>().in(PackBom::getId, packBomIds)));
        }

        for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {

            // /*版型定位字段*/
            // FieldVal fieldValList = map.get(orderBookDetailVo.getStyleColorId());
            // if(!ObjectUtil.isEmpty(fieldValList)){
            //     orderBookDetailVo.setPatternPositioningCode(fieldValList.getVal());
            //     orderBookDetailVo.setPatternPositioningName(fieldValList.getValName());
            // }
            Function<String, String> getDosageName = (String dosageId) -> {
                if (StrUtil.isBlank(dosageId)) return null;
                return packBoms.stream().filter(it -> dosageId.contains(it.getId())).map(it ->
                        new StringJoiner(":").add(it.getCollocationName())
                                .add(it.findUnitUse().setScale(2, RoundingMode.HALF_UP).toString())
                         + it.getStockUnitName()
                ).collect(Collectors.joining("\n"));
            };

            orderBookDetailVo.setUnitFabricDosage(getDosageName.apply(orderBookDetailVo.getUnitFabricDosageIds()));
            orderBookDetailVo.setUnitDosage(getDosageName.apply(orderBookDetailVo.getUnitDosageIds()));


            if ("CMT".equals(orderBookDetailVo.getDevtTypeName())){
                // orderBookDetailVo.setSupplierNo("");
                // orderBookDetailVo.setSupplierColor("");
                // orderBookDetailVo.setSupplierAbbreviation("");
                // /*"FOB取配色 cmt设置为空*/
                // orderBookDetailVo.setFobClothingFactoryName("");
                // orderBookDetailVo.setFobClothingFactoryCode("");

//                orderBookDetailVo.setCost("")

                String styleColorId = orderBookDetailVo.getStyleColorId();

                PackInfoListVo packInfo = packInfoService.getByQw(new QueryWrapper<PackInfo>().eq("code", orderBookDetailVo.getBom()).eq("pack_type",
                        "0".equals(orderBookDetailVo.getBomStatus()) ?
                                PackUtils.PACK_TYPE_DESIGN :
                                PackUtils.PACK_TYPE_BIG_GOODS).eq("style_color_id", styleColorId));
                if (packInfo != null) {
                    String packType = "0".equals(orderBookDetailVo.getBomStatus()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS;
                    PackBomVersion enableVersion = packBomVersionService.getEnableVersion(packInfo.getId(), packType);
                    if (enableVersion != null) {
                        List<PackBom> packBoms1 = packBomService.list(new QueryWrapper<PackBom>().eq("bom_version_id", enableVersion.getId()));
                        for (PackBom packBom : packBoms1) {
                            if ("1".equals(packBom.getMainFlag())) {
                                orderBookDetailVo.setFabricState(packBom.getStatus());
                                // orderBookDetailVo.setFabricFactoryCode(packBom.getSupplierId());
                                // orderBookDetailVo.setFabricFactoryName(packBom.getSupplierName());
                                QueryWrapper<BasicsdatumMaterialColor> basicsdatumMaterialColorQueryWrapper = new QueryWrapper<>();
                                basicsdatumMaterialColorQueryWrapper.eq("material_code", packBom.getMaterialCode());
                                basicsdatumMaterialColorQueryWrapper.eq("color_code", packBom.getColorCode());
                                BasicsdatumMaterialColor basicsdatumMaterialColor = basicsdatumMaterialColorService.getOne(basicsdatumMaterialColorQueryWrapper);
//                                orderBookDetailVo.setFabricFactoryColorNumber(basicsdatumMaterialColor.getSupplierColorCode());
                                // orderBookDetailVo.setFabricCode(packBom.getMaterialCode());
                                // orderBookDetailVo.setFabricComposition(packBom.getIngredient());
                            }
                        }
                    }
                }
            }

            String orderBookChannel = orderBookList.stream().filter(it -> it.getId().equals(orderBookDetailVo.getOrderBookId())).findFirst().map(OrderBook::getChannel).orElse("");
            JSONObject jsonObject = JSON.parseObject(Opt.ofBlankAble(orderBookDetailVo.getCommissioningSize()).orElse(""));
            if (jsonObject!= null){
                Map<String, String> sizeModelMap = sizeList.stream().filter(it -> orderBookDetailVo.getSizeCodes().contains(it.getCode()))
                        .collect(Collectors.toMap(BasicsdatumSize::getInternalSize, BasicsdatumSize::getModel));
                sizeModelMap.forEach((key,value)-> {
                    for (OrderBookChannelType channel : OrderBookChannelType.values()) {
                        jsonObject.put(key+ channel.getFill() + "Size",value);
                    }
                });
                channelPageConfig.forEach((channel, pageConfig)-> {
                    List<String> sizeRange = pageConfig.getSizeRange();
                    if (jsonObject.keySet().stream().anyMatch(it-> it.contains(channel.ordinal()+""))) {
                        sizeRange.forEach(size-> {
                            String status = orderBookChannel.contains(channel.getCode()) && sizeModelMap.containsKey(size) ? "0" : "1";
                            jsonObject.put(size+ channel.getFill() + "Status", status);
                            jsonObject.put(size+ channel.getPercentageFill() + "Status", status);
                        });
                    };
                });
                orderBookDetailVo.setCommissioningSize(JSON.toJSONString(jsonObject));
            }
        }

        return orderBookDetailVos;
    }

    @Override
    public OrderBookDetailVo getDetailById(String id) {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tobl.id", id);
        List<OrderBookDetailVo> orderBookDetailVos = this.querylist(queryWrapper,null);
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
        // if (StringUtil.isNotEmpty(dto.getCommissioningDate())){
        //     queryWrapper.between("tobl.commissioning_date", dto.getCommissioningDate().split(","));
        // }commissioningDate
        queryWrapper.notEmptyLike("tobl.commissioning_date", dto.getCommissioningDate());
        queryWrapper.notEmptyIn("tobl.id",dto.getId());
        queryWrapper.notEmptyIn("tobl.id",dto.getIds());
        queryWrapper.notEmptyEq("tsc.devt_type_name",dto.getDevtTypeName());
        queryWrapper.likeList("ts.prod_category", dto.getCategoryCode());
        queryWrapper.likeList("tsc.band_name", dto.getBand());
        queryWrapper.likeList("tobl.designer_id", dto.getDesignerName());
        queryWrapper.likeList("tsc.style_no", dto.getBulkStyleNo());
        queryWrapper.notEmptyEq("tobl.company_code", dto.getCompanyCode());
        queryWrapper.notEmptyLike("ts.brand", dto.getBrandCode());
        queryWrapper.notEmptyLike("ts.positioning_name", dto.getPositioningName());
        queryWrapper.notEmptyLike("ts.plate_type_name",dto.getPatternPositioningName());
        queryWrapper.notEmptyLike("tsc.color_name",dto.getColorName());
        queryWrapper.notEmptyLike("tbcl.colour_code", dto.getSupplierColor());
        queryWrapper.notEmptyLike("tsc.tag_price", dto.getTagPrice());
        queryWrapper.notEmptyLike("tobl.total_production", dto.getTotalProduction());
        queryWrapper.notEmptyLike("tobl.coefficient_code", dto.getCoefficientCode());
        queryWrapper.notEmptyLike("tobl.status", dto.getStatus());
        queryWrapper.notNullEq("tobl.audit_status", dto.getAuditStatus());

        // queryWrapper.notEmptyLike("tobl.coefficient_code", dto.getCost());
        queryWrapper.notEmptyLike("tobl.target_time", dto.getTargetTime());
        queryWrapper.notEmptyLike("tobl.production_urgency_name", dto.getProductionUrgencyName());
        queryWrapper.notEmptyLike("tobl.remark", dto.getRemark());
        queryWrapper.notEmptyLike("tobl.other", dto.getOther());
        queryWrapper.notEmptyLike("tobl.fabric_state", dto.getFabricState());
        queryWrapper.notEmptyLike("tobl.continuation_point", dto.getContinuationPoint());
        queryWrapper.notEmptyLike("tobl.company_fabric_number", dto.getCompanyFabricNumber());
        queryWrapper.notEmptyLike("tobl.fabric_factory_code", dto.getFabricFactoryCode());
        queryWrapper.notEmptyLike("tobl.fabric_factory_name", dto.getFabricFactoryName());
        queryWrapper.notEmptyLike("tobl.fabric_composition", dto.getFabricComposition());
        queryWrapper.notEmptyLike("tobl.fabric_factory_color_number", dto.getFabricFactoryColorNumber());
        queryWrapper.notEmptyLike("tobl.fabric_type", dto.getFabricType());
        queryWrapper.notEmptyLike("tobl.unit_price", dto.getUnitPrice());
        queryWrapper.notEmptyLike("tobl.delivery_time", dto.getDeliveryTime());
        queryWrapper.notEmptyLike("tobl.inventory_fabric_meter", dto.getInventoryFabricMeter());
        queryWrapper.notEmptyLike("tobl.inventory_doable_number", dto.getInventoryDoableNumber());
        queryWrapper.notEmptyLike("tobl.fabric_remarks", dto.getFabricRemarks());
        queryWrapper.notEmptyLike("tobl.fabric_drop", dto.getFabricDrop());
        queryWrapper.notEmptyLike("ts.design_no", dto.getStyle());
        // queryWrapper.notEmptyLike("ts.prod_category2nd_name", dto.getProdCategory2ndName());
        queryWrapper.notEmptyLike("ts.old_design_no", dto.getOldDesignNo());
        queryWrapper.notEmptyLike("ts.registering_no", dto.getRegisteringNo());
        queryWrapper.notEmptyLike("tobl.suit_no", dto.getSuitNo());
        queryWrapper.notEmptyLike("ts.pattern_design_name", dto.getPatternDesignName());
        queryWrapper.notEmptyLike("ts.designer", dto.getStyleDesignerName());

        queryWrapper.notEmptyLike("tobl.offline_production", dto.getOfflineProduction());
        queryWrapper.notEmptyLike("tobl.online_production", dto.getOnlineProduction());
        queryWrapper.notEmptyLike("tobl.material", dto.getMaterial());
        queryWrapper.notEmptyLike("tobl.braiding", dto.getBraiding());
        queryWrapper.notEmptyLike("tobl.dimension_info", dto.getDimensionInfo());
        queryWrapper.notEmptyLike("tobl.gram_weight", dto.getGramWeight());

        // //有权限则查询全部数据
        // if (StringUtil.isEmpty(dto.getIsAll()) || "0".equals(dto.getIsAll())){
        //     queryWrapper.and(qw -> qw.eq("tobl.designer_id", dto.getUserId()).
        //             or().eq("tobl.business_id", dto.getUserId())
        //             .or().eq("tobl.create_id", dto.getUserId()));
        // }

        if(StrUtil.isNotBlank(dto.getPlanningSeasonId())){
            BaseQueryWrapper<OrderBook> baseQueryWrapper = new BaseQueryWrapper();
            baseQueryWrapper.eq("season_id",dto.getPlanningSeasonId());
            List<OrderBook> list = orderBookService.list(baseQueryWrapper);
            List<String> stringList = list.stream().map(OrderBook::getId).collect(Collectors.toList());
            if (!stringList.isEmpty()){
                queryWrapper.in("tobl.order_book_id",stringList);
            }else {
                ArrayList<Object> arr = new ArrayList<>();
                arr.add("0");
                queryWrapper.in("tobl.order_book_id",arr);
            }

        }

        queryWrapper.orderByDesc("tobl.serial_number");
        return queryWrapper;
    }

    /**
     * 设计师补充数据
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean designConfirm(OrderBookDetailSaveDto dto) {
        if(StrUtil.isEmpty(dto.getStyleColorId())){
            throw new RuntimeException("配色id为空");
        }
        OrderBookDetail orderBookDetail = this.getById(dto.getId());
//        if (orderBookDetail.getAuditStatus() != OrderBookDetailAuditStatusEnum.NOT_COMMIT) {
        if (orderBookDetail.getAuditStatus() == OrderBookDetailAuditStatusEnum.FINISH) {
            throw new OtherException("不允许修改已发起审批的数据");
        }
        dto.setDesignerConfirm("1");
        boolean isUpdate=false;
        /*查询配色数据*/
        StyleColor styleColor =  styleColorService.getById(dto.getStyleColorId());
        //修改厂家
        if (!StringUtils.equals(styleColor.getSupplierAbbreviation(),dto.getFobClothingFactoryName())
        || !StringUtils.equals(styleColor.getSupplierNo(),dto.getFobClothingFactoryCode())
        || !StringUtils.equals(styleColor.getSupplier(),dto.getFobSupplier())){

            styleColor.setSupplierAbbreviation(dto.getFobClothingFactoryName());
            styleColor.setSupplierNo(dto.getFobClothingFactoryCode());
            styleColor.setSupplier(dto.getFobSupplier());
            isUpdate=true;
        }
        //修改波段
        if (!StringUtils.equals(styleColor.getBandCode(),dto.getBandCode()) || !StringUtils.equals(styleColor.getBandName(),dto.getBandName())){
            styleColor.setBandCode(dto.getBandCode());
            styleColor.setBandName(dto.getBandName());
            isUpdate=true;
        }

        /*修改bom的颜色*/
        PackInfo packInfo = packInfoService.getByOne("style_no",styleColor.getStyleNo());
        if(!ObjectUtils.isEmpty(packInfo)){
            /*当颜色修改时同时修改配色和bom的颜色*/
            if(!StrUtil.equals(styleColor.getColorCode(),dto.getColorCode()) || !StrUtil.equals(styleColor.getColorName(),dto.getColorName())){
                isUpdate=true;
                styleColor.setColorName(dto.getColorName());
                styleColor.setColorCode(dto.getColorCode());
            }

            packInfo.setColorCode(styleColor.getColorCode());
            packInfo.setColor(styleColor.getColorName());
            packInfoService.updateById(packInfo);

            String packType = "0".equals(styleColor.getBomStatus()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS;
            PackBomVersion enableVersion = packBomVersionService.getEnableVersion(packInfo.getId(), packType);
            if (enableVersion != null) {
                PackBom packBom = packBomService.findOne(new QueryWrapper<PackBom>()
                        .eq("bom_version_id", enableVersion.getId())
                        .eq("main_flag", "1")
                );
                if (packBom != null ) {
                    if (StrUtil.isNotBlank(dto.getFabricState())) {
                        packBom.setStatus(dto.getFabricState());
                        packBomService.updateById(packBom);
                    }

                    if (StrUtil.isNotBlank(dto.getFabricFactoryColorNumber())) {
                        basicsdatumMaterialColorService.update(new UpdateWrapper<BasicsdatumMaterialColor>().set("supplier_color_code",dto.getFabricFactoryColorNumber())
                                .eq("material_code", packBom.getMaterialCode())
                                .eq("color_code", packBom.getColorCode())
                        );
                    }
                }
            }
        }

        if (isUpdate){
            /*修改*/
            styleColorService.updateById(styleColor);

            //触发下发
            smpService.goods(styleColor.getId().split(","));
        }

        baseMapper.updateById(dto);
        return true;
    }

    /**
     * 订货本详情-设计师分配
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignmentDesigner(List<OrderBookDetailSaveDto> dto) {
        List<String> ids = dto.stream().map(OrderBookDetailSaveDto::getId).collect(Collectors.toList());
        /*id分组*/
        Map<String, OrderBookDetailSaveDto> map = dto.stream().collect(Collectors.toMap(OrderBookDetailSaveDto::getId, Function.identity()));
        List<OrderBookDetail> orderBookDetails = baseMapper.selectBatchIds(ids);
        /*设置设计师*/
        orderBookDetails.forEach(o -> {
            if (o.getAuditStatus() != OrderBookDetailAuditStatusEnum.NOT_COMMIT)
                throw new OtherException("已经发起审核,无法分配设计师");
            OrderBookDetailSaveDto orderBookDetailSaveDto = map.get(o.getId());
            if (ObjectUtil.isNotEmpty(orderBookDetailSaveDto)) {
                o.setDesignerId(orderBookDetailSaveDto.getDesignerId());
                /*查询是否包含设计师编码*/
                if(orderBookDetailSaveDto.getDesignerName().contains(COMMA)){
                    List<String> stringList =  StringUtils.convertList(orderBookDetailSaveDto.getDesignerName());
                    o.setDesignerName(stringList.get(0));
                    o.setDesignerCode(stringList.get(1));
                }else {
                    o.setDesignerName(orderBookDetailSaveDto.getDesignerName());
                    o.setDesignerCode(orderBookDetailSaveDto.getDesignerCode());
                }

                o.setStatus(OrderBookDetailStatusEnum.DESIGNER);
            }
        });
        /*保存*/
        saveOrUpdateBatch(orderBookDetails);
        String userIds = orderBookDetails.stream().map(OrderBookDetail::getDesignerId).collect(Collectors.joining(","));
        // 发送通知消息给对应的人员
        messageUtils.sendCommonMessage(userIds, "您有新的订货本消息待处理", "/styleManagement/orderBook", stylePicUtils.getGroupUser());
        return true;
    }

    @Override
    public Map<String, Object> queryCount(OrderBookDetailQueryDto dto) {
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, "style_order_book", "tobl.");
        List<OrderBookDetailVo> querylistAll = this.getBaseMapper().queryPage(queryWrapper);
        HashMap<String, Object> hashMap =new HashMap<>();
        float materialSum = 0;
        float materialMoneySum = 0;
        float braidingSum = 0;
        float tagPriceSum = 0;
        float totalProductionSum = 0;
        float onlineProductionSum = 0;
        float offlineProductionSum = 0;
        for (OrderBookDetailVo orderBookDetailVo : querylistAll) {
            String commissioningSize = orderBookDetailVo.getCommissioningSize();
            if (StrUtil.isNotEmpty(commissioningSize)){
                JSONObject jsonObject = JSON.parseObject(commissioningSize);
                if (jsonObject!= null){
                    for (String sizeName : jsonObject.keySet()) {
                        Object o =  hashMap.get(sizeName);
                        try {
                            if (ObjectUtil.isNotEmpty(o)){
                                hashMap.put(sizeName,(Float)o+jsonObject.getFloat(sizeName));
                            }else {
                                hashMap.put(sizeName, jsonObject.getFloat(sizeName));
                            }
                        } catch (Exception ignored) {

                        }
                    }

                }
            }
            try {
                materialSum +=  Float.parseFloat(orderBookDetailVo.getMaterial());
            } catch (Exception ignored) {
            }
            try {
                materialMoneySum +=  Float.parseFloat(orderBookDetailVo.getTagPrice().multiply(new BigDecimal(orderBookDetailVo.getMaterial())).toString());
            } catch (Exception ignored) {
            }
            try {
                braidingSum +=  Float.parseFloat(orderBookDetailVo.getBraiding());
            } catch (Exception ignored) {
            }
            try {

                tagPriceSum +=  Float.parseFloat(orderBookDetailVo.getTagPrice().toString());
            } catch (Exception ignored) {
            }
            try {
                totalProductionSum +=  Float.parseFloat(orderBookDetailVo.getTotalProduction());
            } catch (Exception ignored) {
            }
            try {
                onlineProductionSum +=  Float.parseFloat(orderBookDetailVo.getOnlineProduction());
            } catch (Exception ignored) {
            }
            try {
                offlineProductionSum +=  Float.parseFloat(orderBookDetailVo.getOfflineProduction());
            } catch (Exception ignored) {
            }
        }
        hashMap.put("materialSum", materialSum);
        hashMap.put("materialMoneySum", materialMoneySum);
        hashMap.put("braidingSum", braidingSum);
        hashMap.put("tagPriceSum", tagPriceSum);
        hashMap.put("totalProductionSum", totalProductionSum);
        hashMap.put("onlineProductionSum", onlineProductionSum);
        hashMap.put("offlineProductionSum", offlineProductionSum);
        hashMap.put("total", querylistAll.size());
        return hashMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(OrderBookDetailSaveDto dto) {
        // OrderBookDetail orderBookDetail = orderBookDetailService.getById(dto.getId());
        String[] split = dto.getIds().split(",");
        List<OrderBookDetail> orderBookDetails = this.listByIds(Arrays.asList(split));
        if (CollUtil.isEmpty(orderBookDetails)) return;
        OrderBookDetailStatusEnum updateStatus = dto.getStatus();
        OrderBookDetailAuditStatusEnum updateAuditStatus;
        switch (updateStatus) {
            case NOT_AUDIT: updateAuditStatus = OrderBookDetailAuditStatusEnum.AWAIT; break;
            case AUDIT: updateAuditStatus = OrderBookDetailAuditStatusEnum.FINISH; break;
            default: updateAuditStatus = OrderBookDetailAuditStatusEnum.NOT_COMMIT; break;
        }
        for (OrderBookDetail orderBookDetail : orderBookDetails) {
            OrderBookDetailAuditStatusEnum auditStatus = orderBookDetail.getAuditStatus();

            orderBookDetail.setStatus(updateStatus);
            orderBookDetail.setAuditStatus(updateAuditStatus);

//            if (StrUtil.isBlank(orderBookDetail.getDesignerCode())) {
//                throw new OtherException("还未分配设计师");
//            }
//            if (StrUtil.isBlank(orderBookDetail.getBusinessId())) {
//                throw new OtherException("还未分配商企");
//            }
            String totalProduction = orderBookDetail.getTotalProduction();
            if (StringUtils.isEmpty(totalProduction) || "0".equals(totalProduction)) {
                throw new OtherException("下单数量不能为空或者0");
            }
            // 检查投产数是否一致
            JSONObject jsonObject = JSON.parseObject(orderBookDetail.getCommissioningSize());
            int sumProduction = jsonObject.keySet().stream()
                    .filter(it ->
                            it.endsWith(OrderBookChannelType.OFFLINE.getFill()) || it.endsWith(OrderBookChannelType.ONLINE.getFill())
                    ).mapToInt(jsonObject::getInteger)
                    .sum();
            if (sumProduction != Integer.parseInt(totalProduction)) {
                throw new OtherException("尺码总数量与投产数量不一致，请重新填写");
            }

            // 判断是否能下单
            if (auditStatus != updateAuditStatus) {
                if (auditStatus == OrderBookDetailAuditStatusEnum.FINISH){
                    throw new OtherException("已经完成审核,无法修改审核状态");
                }
            }else if (auditStatus != OrderBookDetailAuditStatusEnum.NOT_COMMIT){
                throw new OtherException("已经发起审核,请勿重复提交");
            }
        }
        this.updateBatchById(orderBookDetails);

        // 修改订货本状态为待确认
        List<String> orderBookIdList = orderBookDetails.stream().map(OrderBookDetail::getOrderBookId).distinct().collect(Collectors.toList());
        orderBookService.update(new LambdaUpdateWrapper<OrderBook>()
                .set(OrderBook::getStatus, OrderBookStatusEnum.NOT_CONFIRM)
                .set(OrderBook::getUpdateDate, new Date())
                .in(OrderBook::getId,orderBookIdList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void placeAnOrderReject(OrderBookDetailQueryDto dto) {
        if (StringUtils.isEmpty(dto.getIds())) {
            throw new OtherException("请选订货本");
        }
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        List<OrderBookDetailVo> orderBookDetails = this.querylist(queryWrapper, null);
        for (OrderBookDetailVo orderBookDetail :orderBookDetails) {
            if (OrderBookDetailAuditStatusEnum.NOT_COMMIT == orderBookDetail.getAuditStatus()){
                throw new OtherException(orderBookDetail.getBulkStyleNo()+"未提交审核，不能驳回审核");
            }
            orderBookDetail.setStatus(OrderBookDetailStatusEnum.AUDIT_SUSPEND);
            orderBookDetail.setAuditStatus(OrderBookDetailAuditStatusEnum.NOT_COMMIT);
            orderBookDetail.setIsLock(YesOrNoEnum.NO);
            orderBookDetail.setIsOrder(YesOrNoEnum.NO);
        }
        List<OrderBookDetail> orderBookDetails1 = BeanUtil.copyToList(orderBookDetails, OrderBookDetail.class);
        this.updateBatchById(orderBookDetails1);

        List<String> orderBookIdList = orderBookDetails1.stream().map(OrderBookDetail::getOrderBookId).distinct().collect(Collectors.toList());
        for (String orderBookId : orderBookIdList) {
            LambdaQueryWrapper<OrderBookDetail> ew = new LambdaQueryWrapper<OrderBookDetail>().eq(OrderBookDetail::getOrderBookId, orderBookId);
            long totalCount = this.count(ew);
            long rightStatusCount = this.count(ew.and(it-> it.eq(OrderBookDetail::getIsOrder, YesOrNoEnum.NO).or().isNull(OrderBookDetail::getIsOrder)));

            orderBookService.update(new LambdaUpdateWrapper<OrderBook>()
                    .set(OrderBook::getStatus,OrderBookStatusEnum.SUSPEND)
                    .set(OrderBook::getOrderStatus,totalCount == rightStatusCount ? OrderBookOrderStatusEnum.NOT_COMMIT : OrderBookOrderStatusEnum.PART_ORDER)
                    .eq(OrderBook::getId,orderBookId));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean placeAnOrder(OrderBookDetailQueryDto dto, String orderBookId) {
        if (StringUtils.isEmpty(orderBookId)) {
            throw new OtherException("请选订货本");
        }
        BaseQueryWrapper<OrderBookDetail> queryWrapper = this.buildQueryWrapper(dto);
        List<OrderBookDetailVo> orderBookDetails = this.querylist(queryWrapper, null);
        for (OrderBookDetailVo orderBookDetail :orderBookDetails) {
            if (orderBookDetail.getStatus() == OrderBookDetailStatusEnum.AUDIT){
                throw new OtherException(orderBookDetail.getBulkStyleNo()+ "已审核,请勿重复提交");
            } else if (orderBookDetail.getStatus() != OrderBookDetailStatusEnum.NOT_AUDIT){
                throw new OtherException(orderBookDetail.getBulkStyleNo()+ "未审核，不能下单");
            }
        }

        for (OrderBookDetailVo orderBookDetail : orderBookDetails) {
            orderBookDetail.setIsLock(YesOrNoEnum.YES);
            orderBookDetail.setIsOrder(YesOrNoEnum.YES);
            orderBookDetail.setStatus(OrderBookDetailStatusEnum.AUDIT);
            orderBookDetail.setAuditStatus(OrderBookDetailAuditStatusEnum.FINISH);
            orderBookDetail.setCommissioningDate(new Date());
        }
        List<OrderBookDetail> orderBookDetails1 = BeanUtil.copyToList(orderBookDetails, OrderBookDetail.class);
        boolean b = this.updateBatchById(orderBookDetails1);

        // 检查是否下属详情是否全部完成审核
        LambdaQueryWrapper<OrderBookDetail> ew = new LambdaQueryWrapper<OrderBookDetail>().eq(OrderBookDetail::getOrderBookId, dto.getOrderBookId());
        long totalCount = this.count(ew);
        long rightStatusCount = this.count(ew.eq(OrderBookDetail::getIsOrder, YesOrNoEnum.YES));

        OrderBook orderBook = orderBookService.getById(dto.getOrderBookId());
        if (totalCount == rightStatusCount) {
            orderBook.setStatus(OrderBookStatusEnum.CONFIRM);
            orderBook.setOrderStatus(OrderBookOrderStatusEnum.ORDER);
        }else {
            orderBook.setStatus(OrderBookStatusEnum.PART_CONFIRM);
            orderBook.setOrderStatus(OrderBookOrderStatusEnum.PART_ORDER);
        }
        orderBookService.updateById(orderBook);
        orderBookDetails1.forEach(orderBookDetail -> {
            PublicStyleColorDto colorDto = new PublicStyleColorDto();
            colorDto.setOrderFlag(YesOrNoEnum.YES.getValueStr());
            colorDto.setId(orderBookDetail.getStyleColorId());
            styleColorService.updateOrderFlag(colorDto);
        });
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPersonnel(OrderBookDetailSaveDto dto) {
        List<String> idList = StringUtils.convertList(dto.getIds());
        List<OrderBookDetail> orderBookDetailList = this.listByIds(idList);
        if (orderBookDetailList.size() < idList.size()){
            throw new OtherException("订货本详情不存在");
        }
        orderBookDetailList.forEach(orderBookDetail -> {
            if (orderBookDetail.getAuditStatus() != OrderBookDetailAuditStatusEnum.NOT_COMMIT)
                throw new OtherException("已经发起审核,无法分配商企");
            orderBookDetail.setBusinessId("1");
            orderBookDetail.setStatus(OrderBookDetailStatusEnum.BUSINESS);
        });

        boolean b = this.saveOrUpdateBatch(orderBookDetailList);
        if (b) {
            // 发送通知消息给对应的人员
            List<UserCompany> userCompanies = amcFeignService.getTeamUserListByPost(dto.getPlanningSeasonId(), "商企");
            if (!userCompanies.isEmpty()){
                List<String> list = userCompanies.stream().map(UserCompany::getUserId).collect(Collectors.toList());
                messageUtils.sendCommonMessage(StringUtils.join(list, ","), "您有新的订货本消息待处理", "/styleManagement/orderBook", stylePicUtils.getGroupUser());
            }
        }
        return b;
    }

    @Value("${orderBook.sizeRange:XXS,XS,S,M,L,XL,XXL,XXXL}")
    private String sizeRange;

    @Override
    public Map<OrderBookChannelType, OrderBookDetailPageConfigVo> pageConfig(OrderBookDetailQueryDto dto) {
//        List<String> sizeRange = sizeService.listOneField(new LambdaQueryWrapper<BasicsdatumSize>()
//                .eq(BasicsdatumSize::getStatus, "1")
//                .eq(BasicsdatumSize::getCompanyCode, dto.getCompanyCode())
//                .orderByAsc(BasicsdatumSize::getSort), BasicsdatumSize::getInternalSize);
//        Set<String> sizeRangeSet = sizeRange.stream()
//                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.toList()))
//                .keySet();
        // 仅线上线下
        return Arrays.stream(OrderBookChannelType.values()).map(channel-> {
            // 找对应渠道配置
            OrderBookDetailPageConfigVo vo = new OrderBookDetailPageConfigVo();
            vo.setChannel(channel);
            vo.setSizeRange(Arrays.asList(sizeRange.split(",")));
            vo.setSameDesignCount(1);
            return vo;
        }).collect(Collectors.toMap(OrderBookDetailPageConfigVo::getChannel, Function.identity()));
    }

    @Override
    public PageInfo<OrderBookSimilarStyleVo> similarStyleList(OrderBookDetailQueryDto queryDto) {
        long millis = System.currentTimeMillis();
        /* ----------------------------封装查询参数---------------------------- */
        if (CollUtil.isEmpty(queryDto.getChannel())) {
            throw new OtherException("相似款查询必须传入渠道条件");
        }
        List<String> channelList = queryDto.getChannel().stream().map(OrderBookChannelType::getText).collect(Collectors.toList());

        String category1stCode = queryDto.getCategory1stCode();
        List<String> searchBulkStyleNoList = new ArrayList<>();
        if (StrUtil.isNotBlank(category1stCode)) {
            // 根据大类获取款式
            List<String> styleIdList = styleService.listOneField(new LambdaQueryWrapper<Style>().eq(Style::getProdCategory1st, category1stCode), Style::getId);
            if (CollUtil.isNotEmpty(styleIdList)) {
                // 根据款式获取款号
                searchBulkStyleNoList.addAll(styleColorService.listOneField(new LambdaQueryWrapper<StyleColor>().in(StyleColor::getStyleId, styleIdList), StyleColor::getStyleNo));
            }
        }
        System.out.println("封装查询参数 time:" + (System.currentTimeMillis() - millis));

        /* ----------------------------查询分页款号数据---------------------------- */

        Page<Object> page = queryDto.startPage();
        BaseQueryWrapper<OrderBookDetail> qw = new BaseQueryWrapper<>();
        qw.notEmptyLike("T.PROD_CODE", queryDto.getBulkStyleNo());
        qw.notEmptyIn("T.PROD_CODE", searchBulkStyleNoList);
        qw.in("T.CHANNEL_TYPE", channelList);

        List<Map<String, Object>> totalMaps = getBaseMapper().queryStarRocksTotal(qw);
        List<OrderBookSimilarStyleVo> dtoList = ORDER_BOOK_CV.copyList2SimilarStyleVo(totalMaps);

        PageInfo<OrderBookSimilarStyleVo> result = CopyUtil.copy(page.toPageInfo(), dtoList);
        if (CollUtil.isEmpty(dtoList)) {
            return result;
        }
        System.out.println("查询分页款号数据 time:" + (System.currentTimeMillis() - millis));

        /* ----------------------------PDM款式配色装饰---------------------------- */

        List<String> bulkStyleNoList = dtoList.stream().map(OrderBookSimilarStyleVo::getBulkStyleNo).collect(Collectors.toList());
        // 查询PDM的款式配色
        List<StyleColor> styleColorList = styleColorService.list(new LambdaQueryWrapper<StyleColor>().in(StyleColor::getStyleNo, bulkStyleNoList));
        dtoList.forEach(dto-> {
            styleColorList.stream().filter(it-> it.getStyleNo().equals(dto.getBulkStyleNo())).findFirst().ifPresent(styleColor -> {
                dto.setStyleColorPic(styleColor.getStyleColorPic());
                dto.setStyleId(styleColor.getStyleId());
            });
        });
        // 设置款图
        stylePicUtils.setStylePic(dtoList, "styleColorPic",30);
        System.out.println("PDM款式配色装饰 time:" + (System.currentTimeMillis() - millis));

        /* ----------------------------装饰详细的投产销售---------------------------- */

        BaseQueryWrapper<OrderBookDetail> qw1 = new BaseQueryWrapper<>();
        qw1.in("T.PROD_CODE", bulkStyleNoList);
        qw.in("T.CHANNEL_TYPE", channelList);

        List<Map<String, Object>> detailMaps = getBaseMapper().queryStarRocksDetail(qw1);
        // 封装数据并转化Bean
        detailMaps.forEach(it-> it.put("sizeMap",new HashMap<>(it)));
        List<StyleSaleIntoDto> detailList = ORDER_BOOK_CV.copyList2StyleSaleInto(detailMaps);

        List<String> sizeNameList = StringUtils.convertList("XXS,XS,S,M,L,XL,XXL");
        // 构建基础模型
        Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> map = new HashMap<>(3);
        for (StyleSaleIntoCalculateResultType calculateResultType : StyleSaleIntoCalculateResultType.values()) {
            OrderBookSimilarStyleChannelVo channelVo = new OrderBookSimilarStyleChannelVo();
            channelVo.setResultType(calculateResultType);
            for (OrderBookChannelType channelType : OrderBookChannelType.values()) {
                OrderBookSimilarStyleSizeMapVo sizeMapVo = new OrderBookSimilarStyleSizeMapVo();
                sizeMapVo.setResultType(calculateResultType);
                sizeMapVo.setNumSizeMap(sizeNameList.stream().collect(Collectors.toMap(Function.identity(), (it) -> 0.0)));
                sizeMapVo.setPercentageSizeMap(sizeNameList.stream().collect(Collectors.toMap(Function.identity(), (it) -> 0.0)));
                channelVo.getChannelSizeMap().put(channelType, sizeMapVo);
            }
            map.put(calculateResultType, channelVo);
        }

        // 设置投产销售
        dtoList.forEach(dto-> {
            // 获取同品牌相同款号相同渠道的数据
            List<StyleSaleIntoDto> dtoDetailList = detailList.stream().filter(it ->
                    it.getBulkStyleNo().equals(dto.getBulkStyleNo()) &&
                    it.getBrand().equals(dto.getBrand())
            ).collect(Collectors.toList());

            dto.setChannelList(dtoDetailList.stream().map(StyleSaleIntoDto::getChannel).distinct().collect(Collectors.toList()));

            Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> calculateSizeMap = ORDER_BOOK_CV.copyResultTypeMyself(map);

            calculateSizeMap.forEach((calculateResultType, channelVo)-> {
                String code = calculateResultType.getCode();
                List<StyleSaleIntoDto> calculateDetailList = dtoDetailList.stream()
                        .filter(it -> it.getResultType().getCode().contains(code))
                        .collect(Collectors.toList());
                channelVo.getChannelSizeMap().forEach((channel, sizeMap)-> {
                    calculateDetailList.stream().filter(it -> it.getChannel() == channel).findFirst().ifPresent(detail-> {
                        detail.getSizeMap().forEach((size,num)-> {
                            if (sizeMap.getNumSizeMap().containsKey(size)) {
                                sizeMap.getNumSizeMap().put(size, num);
                            }
                        });
                    });
                });
            });
            calculateSizeMap.get(StyleSaleIntoCalculateResultType.SALE_INTO).getChannelSizeMap().forEach((channel, sizeMap)-> {
                sizeMap.getNumSizeMap().keySet().forEach(size-> {
                    double sale = calculateSizeMap.get(StyleSaleIntoCalculateResultType.SALE).getChannelSizeMap()
                            .get(channel).getNumSizeMap().get(size);
                    double into = calculateSizeMap.get(StyleSaleIntoCalculateResultType.INTO).getChannelSizeMap()
                            .get(channel).getNumSizeMap().get(size);
                    sizeMap.getNumSizeMap().put(size, BigDecimalUtil.dividePercentage(sale, into));
                });
            });

            dto.setCalculateSizeMap(calculateSizeMap);

            dto.setType(dtoDetailList.stream().filter(it-> it.getType() != null).min((it1, it2) ->
                    NumberUtil.compare(it1.getType().ordinal(), it2.getType().ordinal())
            ).map(StyleSaleIntoDto::getType).orElse(null));
        });
        System.out.println("装饰详细的投产销售 time:" + (System.currentTimeMillis() - millis));
        return result;
    }

    /**
     * 查询款式定价数据
     */

    private void queryStylePrice(List<OrderBookDetailVo> orderBookDetailVos,Integer openDataAuth) {
        if (orderBookDetailVos != null && !orderBookDetailVos.isEmpty()) {
            List<String> bulkStyleNos = orderBookDetailVos.stream().map(OrderBookDetailVo::getBulkStyleNo).collect(Collectors.toList());
            BaseQueryWrapper qw = new BaseQueryWrapper();
            qw.in("ssc.style_no", bulkStyleNos);
            if (null == openDataAuth) {
                dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_pricing.getK(), "sd.");
            }
            /*获取款式定价的列表*/
            List<StylePricingVO> stylePricingList = stylePricingServiceimpl.getBaseMapper().getStylePricingList(new StylePricingSearchDTO(), qw);
            // List<StylePricingVO> stylePricingVOS = BeanUtil.copyToList(stylePricingList, StylePricingVO.class);
            /*款式定价数据组装处理*/
            stylePricingServiceimpl.dataProcessing(stylePricingList, orderBookDetailVos.get(0).getCompanyCode(),false);
            /*按大货款号分类*/
            Map<String, StylePricingVO> map = Optional.of(stylePricingList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(StylePricingVO::getBulkStyleNo, v -> v, (a, b) -> b));

            for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {
                /*获取款式定价的数据*/
                StylePricingVO stylePricingVO = map.get(orderBookDetailVo.getBulkStyleNo());
                if (!ObjectUtil.isEmpty(stylePricingVO)) {
                    orderBookDetailVo.setStylePricingId(stylePricingVO.getStylePricingId());

                    if ("CMT".equals(orderBookDetailVo.getDevtTypeName())){
                        orderBookDetailVo.setCmtCost(stylePricingVO.getTotalCost());
                        orderBookDetailVo.setCmtCarpetCost(stylePricingVO.getSewingProcessingFee());
                        orderBookDetailVo.setCmtTotalCost(stylePricingVO.getTotalCost());


                        orderBookDetailVo.setFobCost(new BigDecimal(0));
                    }else {
                        orderBookDetailVo.setFobCost(stylePricingVO.getCoordinationProcessingFee());


                        orderBookDetailVo.setCmtCost(new BigDecimal(0));
                        orderBookDetailVo.setCmtCarpetCost(new BigDecimal(0));
                        orderBookDetailVo.setCmtTotalCost(new BigDecimal(0));
                    }
                    orderBookDetailVo.setCost(stylePricingVO.getTotalCost());

                    orderBookDetailVo.setRate(stylePricingVO.getPlanningRatio());

                    if (orderBookDetailVo.getRate() == null){
                        orderBookDetailVo.setRate(new BigDecimal(4));
                    }
                    orderBookDetailVo.setHonest(stylePricingVO.getPackagingFee());
                    /*产品风格*/
                    /*系数取款式定价中的产品风格 没有值时是D*/
                    orderBookDetailVo.setCoefficientCode(StrUtil.isEmpty(stylePricingVO.getProductStyle())?"D":stylePricingVO.getProductStyle());
                }

            }
        }

    }
}
