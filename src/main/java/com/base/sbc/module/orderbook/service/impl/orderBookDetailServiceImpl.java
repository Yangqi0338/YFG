package com.base.sbc.module.orderbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
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
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
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

    private final OrderBookService orderBookService;

    private final FieldValService fieldValService;

    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    private final StyleColorService styleColorService;

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
        //导出
        ExcelUtils.executorExportExcel(orderBookDetailExportVos, OrderBookDetailExportVo.class,"订货本详情",dto.getImgFlag(),3000,response,"stylePic");

    }

    @Override
    public List<OrderBookDetailVo> querylist(QueryWrapper<OrderBookDetail> queryWrapper) {
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
                                QueryWrapper<BasicsdatumMaterialColor> basicsdatumMaterialColorQueryWrapper = new QueryWrapper<>();
                                basicsdatumMaterialColorQueryWrapper.eq("material_code", packBom.getMaterialCode());
                                basicsdatumMaterialColorQueryWrapper.eq("color_code", packBom.getColorCode());
                                BasicsdatumMaterialColor basicsdatumMaterialColor = basicsdatumMaterialColorService.getOne(basicsdatumMaterialColorQueryWrapper);
                                orderBookDetailVo.setFabricFactoryColorNumber(basicsdatumMaterialColor.getSupplierColorCode());
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


        return queryWrapper;
    }

    /**
     * 设计师补充数据
     *
     * @param dto
     * @return
     */
    @Override
    public boolean designConfirm(OrderBookDetailSaveDto dto) {
        if(StrUtil.isEmpty(dto.getStyleColorId())){
            throw new RuntimeException("配色id为空");
        }
        dto.setDesignerConfirm("1");
        //修改厂家
        /*查询配色数据*/
        StyleColor styleColor =  styleColorService.getById(dto.getStyleColorId());
        styleColor.setSupplierAbbreviation(dto.getFobClothingFactoryName());
        styleColor.setSupplierCode(dto.getFobClothingFactoryCode());
        styleColor.setSupplier(dto.getFobSupplier());
        /*当颜色修改时同时修改配色和bom的颜色*/
        if(!StrUtil.equals(styleColor.getColorCode(),dto.getColorCode())){
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
        /*xiu*/
        styleColorService.updateById(styleColor);
        baseMapper.updateById(dto);
        return true;
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
                    /*产品风格*/
                    /*系数取款式定价中的产品风格 没有值时是D*/
                    orderBookDetailVo.setCoefficientCode(StrUtil.isEmpty(stylePricingVO.getProductStyle())?"D":stylePricingVO.getProductStyle());
                }

            }
        }

    }
}
