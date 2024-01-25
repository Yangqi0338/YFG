package com.base.sbc.module.orderbook.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookOrderStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.common.dto.BasePageInfo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailSaveDto;
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
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.dto.PublicStyleColorDto;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

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

    private final MessageUtils messageUtils;
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


        /*设置图片分辨路*/
        stylePicUtils.setStylePic(orderBookDetailVos, "stylePic",30);
        stylePicUtils.setStylePic(orderBookDetailVos, "styleColorPic",30);
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

        for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {


            // /*版型定位字段*/
            // FieldVal fieldValList = map.get(orderBookDetailVo.getStyleColorId());
            // if(!ObjectUtil.isEmpty(fieldValList)){
            //     orderBookDetailVo.setPatternPositioningCode(fieldValList.getVal());
            //     orderBookDetailVo.setPatternPositioningName(fieldValList.getValName());
            // }
            String unitFabricDosageIds = orderBookDetailVo.getUnitFabricDosageIds();
            if (StringUtil.isNotEmpty(unitFabricDosageIds)){
                StringBuilder names=new StringBuilder();
                List<PackBom> packBoms = packBomService.listByIds(Arrays.asList(unitFabricDosageIds.split(",")));

                for (int i = 0; i < packBoms.size(); i++) {
                    StringBuilder unitFabricDosage= new StringBuilder();
                    unitFabricDosage.append(packBoms.get(i).getCollocationName()).append(":").append(Objects.equals(packBoms.get(i).getPackType(), "packDesign") ? packBoms.get(i).getDesignUnitUse().setScale(2, RoundingMode.HALF_UP) : packBoms.get(i).getBulkUnitUse().setScale(2, RoundingMode.HALF_UP));
                    unitFabricDosage.append(packBoms.get(i).getStockUnitName());
                    if (i != 0) {
                        unitFabricDosage.insert(0,"\n");
                    }
                    names.append(unitFabricDosage);
                }
                orderBookDetailVo.setUnitFabricDosage(names.toString());
            }

            String unitDosageIds = orderBookDetailVo.getUnitDosageIds();
            if (StringUtil.isNotEmpty(unitDosageIds)){
                StringBuilder names=new StringBuilder();
                List<PackBom> packBoms = packBomService.listByIds(Arrays.asList(unitDosageIds.split(",")));
                for (int i = 0; i < packBoms.size(); i++) {
                    StringBuilder unitDosage= new StringBuilder();
                    unitDosage.append(packBoms.get(i).getCollocationName()).append(":").append(Objects.equals(packBoms.get(i).getPackType(), "packDesign") ? packBoms.get(i).getDesignUnitUse().setScale(2, RoundingMode.HALF_UP): packBoms.get(i).getBulkUnitUse().setScale(2, RoundingMode.HALF_UP));
                    unitDosage.append(packBoms.get(i).getStockUnitName());
                    if (i != 0) {
                        unitDosage.insert(0,"\n");
                    }
                    names.append(unitDosage);
                }
                orderBookDetailVo.setUnitDosage(names.toString());
            }


            if ("CMT".equals(orderBookDetailVo.getDevtTypeName())){
                // orderBookDetailVo.setSupplierNo("");
                // orderBookDetailVo.setSupplierColor("");
                // orderBookDetailVo.setSupplierAbbreviation("");
                // /*"FOB取配色 cmt设置为空*/
                // orderBookDetailVo.setFobClothingFactoryName("");
                // orderBookDetailVo.setFobClothingFactoryCode("");

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
                                // orderBookDetailVo.setFabricFactoryCode(packBom.getSupplierId());
                                // orderBookDetailVo.setFabricFactoryName(packBom.getSupplierName());
                                QueryWrapper<BasicsdatumMaterialColor> basicsdatumMaterialColorQueryWrapper = new QueryWrapper<>();
                                basicsdatumMaterialColorQueryWrapper.eq("material_code", packBom.getMaterialCode());
                                basicsdatumMaterialColorQueryWrapper.eq("color_code", packBom.getColorCode());
                                BasicsdatumMaterialColor basicsdatumMaterialColor = basicsdatumMaterialColorService.getOne(basicsdatumMaterialColorQueryWrapper);
                                orderBookDetailVo.setFabricFactoryColorNumber(basicsdatumMaterialColor.getSupplierColorCode());
                                // orderBookDetailVo.setFabricCode(packBom.getMaterialCode());
                                // orderBookDetailVo.setFabricComposition(packBom.getIngredient());
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
    @Transactional
    public boolean designConfirm(OrderBookDetailSaveDto dto) {
        if(StrUtil.isEmpty(dto.getStyleColorId())){
            throw new RuntimeException("配色id为空");
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

        /*当颜色修改时同时修改配色和bom的颜色*/
        if(!StrUtil.equals(styleColor.getColorCode(),dto.getColorCode()) || !StrUtil.equals(styleColor.getColorName(),dto.getColorName())){
            isUpdate=true;
            styleColor.setColorName(dto.getColorName());
            styleColor.setColorCode(dto.getColorCode());
            /*修改bom的颜色*/
            PackInfo packInfo = packInfoService.getByOne("style_no",styleColor.getStyleNo());
            if(!ObjectUtils.isEmpty(packInfo)){
                packInfo.setColorCode(styleColor.getColorCode());
                packInfo.setColor(styleColor.getColorName());
                packInfoService.updateById(packInfo);
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
        for (OrderBookDetail orderBookDetail : orderBookDetails) {
            // 判断是否能下单
            if (orderBookDetail.getAuditStatus() != OrderBookDetailAuditStatusEnum.NOT_COMMIT){
                throw new OtherException("已经发起审核,请勿重复提交");
            }
            if (StrUtil.isBlank(orderBookDetail.getDesignerCode())) {
                throw new OtherException("还未分配设计师");
            }
            if (StrUtil.isBlank(orderBookDetail.getBusinessId())) {
                throw new OtherException("还未分配商企");
            }
            String totalProduction = orderBookDetail.getTotalProduction();
            if (StringUtils.isEmpty(totalProduction) || "0".equals(totalProduction)) {
                throw new OtherException("下单数量不能为空或者0");
            }
            orderBookDetail.setStatus(OrderBookDetailStatusEnum.NOT_AUDIT);
            orderBookDetail.setAuditStatus(OrderBookDetailAuditStatusEnum.AWAIT);
        }
        this.updateBatchById(orderBookDetails);

        // 修改订货本状态为待确认
        List<String> orderBookIdList = orderBookDetails.stream().map(OrderBookDetail::getOrderBookId).distinct().collect(Collectors.toList());
        orderBookService.update(new LambdaUpdateWrapper<OrderBook>()
                .set(OrderBook::getStatus, OrderBookStatusEnum.NOT_CONFIRM)
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
                    .set(OrderBook::getStatus,OrderBookStatusEnum.NOT_COMMIT)
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
            orderBook.setStatus(OrderBookStatusEnum.ORDER);
            orderBook.setOrderStatus(OrderBookOrderStatusEnum.ORDER);
        }else {
            orderBook.setStatus(OrderBookStatusEnum.CONFIRM);
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
                    orderBookDetailVo.setStylePricingId(stylePricingVO.getId());

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
