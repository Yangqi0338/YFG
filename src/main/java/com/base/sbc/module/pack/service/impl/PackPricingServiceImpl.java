/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.annotation.DistributedLock;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.pack.dto.PackBomPageSearchDto;
import com.base.sbc.config.resttemplate.RestTemplateService;
import com.base.sbc.module.basicsdatum.dto.SupplierDetailPriceDto;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingDto;
import com.base.sbc.module.pack.dto.SyncPricingBomDto;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.mapper.PackPricingMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pack.vo.PackPricingVo;
import com.base.sbc.module.pricing.dto.QueryContractPriceDTO;
import com.base.sbc.module.pricing.entity.PricingTemplate;
import com.base.sbc.module.pricing.entity.PricingTemplateItem;
import com.base.sbc.module.pricing.service.PricingTemplateItemService;
import com.base.sbc.module.pricing.service.PricingTemplateService;
import com.base.sbc.module.pricing.vo.PricingTemplateItemVO;
import com.base.sbc.module.smp.dto.HttpResp;
import com.base.sbc.module.pricing.vo.PricingTemplateVO;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import org.apache.commons.lang3.StringUtils;
import org.nfunk.jep.JEP;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-核价信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:16
 */
@Service
public class PackPricingServiceImpl extends AbstractPackBaseServiceImpl<PackPricingMapper, PackPricing> implements PackPricingService {


// 自定义方法区 不替换的区域【other_start】
    @Autowired
    private RestTemplateService restTemplateService;
    @Resource
    PackPricingOtherCostsService packPricingOtherCostsService;
    @Resource
    PackBomService packBomService;
    @Resource
    PackPricingProcessCostsService packPricingProcessCostsService;
    @Resource
    PackPricingCraftCostsService packPricingCraftCostsService;
    @Resource
    PricingTemplateService pricingTemplateService;
    @Resource
    BaseController baseController;
    @Resource
    PackInfoStatusService packInfoStatusService;

    @Resource
    StyleService styleService;

    @Resource
    PackInfoService packInfoService;

    @Resource
    PricingTemplateItemService pricingTemplateItemService;

    @Value("${interface.scmUrl:http://10.8.250.100:1980/escm-app/bill/readyWearContractBill}")
    private String SCM_URL;

    @Resource
    PackPricingBomService packPricingBomService;

    @Resource
    BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;
    @Resource
    PackBomVersionService packBomVersionService;

    @Override
    public ApiResult getContractPrice(QueryContractPriceDTO dto) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("data", Arrays.asList(dto.getStyleNo()));
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/getUnitPrice", paramJson.toJSONString(),
                Pair.of("moduleName","scm"),
                Pair.of("functionName","获取成衣合同价")
        );
        if (!httpResp.isSuccess()) {
            throw new OtherException(httpResp.getMessage());
        }
        // 保存日志
        if (dto.getWriteLog()) {
            saveOperaLog(genOperaLogEntity(dto, "同步成衣合同价"));
        }
        // 无值
        List<Map<String, Object>> respData = new ArrayList<>();
        if (StrUtil.isBlank(httpResp.getData()) || StrUtil.equals(httpResp.getData(), "[]")) {
            Map<String, Object> unitPrice = new HashMap<>();
            unitPrice.put("styleNo", dto.getStyleNo());
            unitPrice.put("unitPrice", null);
            respData.add(unitPrice);
            return ApiResult.success("查询成功", respData);
        }
        respData = (List<Map<String, Object>>) JSONArray.parse(httpResp.getData());
        return ApiResult.success("查询成功", respData);
    }

    @Override
    public PackPricingVo getDetail(PackCommonSearchDto dto) {
        PackPricing one = get(dto.getForeignId(), dto.getPackType());
        PackPricingVo packPricingVo = BeanUtil.copyProperties(one, PackPricingVo.class);
        return packPricingVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @DistributedLock(key = "'saveByDto'+#dto.foreignId")
    public PackPricingVo saveByDto(PackPricingDto dto) {
        PackPricing one = get(dto.getForeignId(), dto.getPackType());
        if (one == null) {
            one = new PackPricing();
            BeanUtil.copyProperties(dto, one);
            save(one);

        } else {
            saveOrUpdateOperaLog(dto, one, genOperaLogEntity(one, "修改"));
            BeanUtil.copyProperties(dto, one, "id");
            updateById(one);
            Map<String,Object> map = new HashMap<>();
            /*获取前端传入的税率计算*/
            if (StrUtil.isNotBlank(one.getCalcItemVal())) {
                JSONObject jsonObject = JSONObject.parseObject(one.getCalcItemVal());
                map.put("税率",jsonObject.get("税率"));
                map.put("pricingTemplateId",one.getPricingTemplateId());
            }
            calculatePricingJson(one.getForeignId(),one.getPackType(),map);
        }
        return BeanUtil.copyProperties(one, PackPricingVo.class);
    }

    /**
     * 计算总价格
     * 默认查大货 默认查大货 flag=1 查询设计  flag=2 当前阶段   ；decimal 保留小数据点
     * @param packInfoId
     * @param flag
     * @param decimal
     * @return
     */
    @Override
    public BigDecimal countTotalPrice(String packInfoId,String flag,Integer decimal) {
/*默认查大货 */
        String packType = PackUtils.PACK_TYPE_BIG_GOODS;
        if(StrUtil.equals(flag,BaseGlobal.YES)){
             packType =   PackUtils.PACK_TYPE_DESIGN;
        }
        if(StrUtil.equals(flag,BaseGlobal.STOCK_STATUS_CHECKED)){
            /*直接查询设计阶段的bom状态*/
            PackInfoStatus packInfoStatus = packInfoStatusService.get(packInfoId, PackUtils.PACK_TYPE_DESIGN);
            packType =StrUtil.equals(packInfoStatus.getBomStatus(),BaseGlobal.NO)? PackUtils.PACK_TYPE_DESIGN:PackUtils.PACK_TYPE_BIG_GOODS;
        }

        PackCommonSearchDto packCommonSearchDto = new PackCommonSearchDto();
        packCommonSearchDto.setPackType(packType);
        packCommonSearchDto.setForeignId(packInfoId);
        /*获取全部成本*/
        Map<String, BigDecimal> otherStatistics = calculateCosts(packCommonSearchDto);
        PackPricingVo detail = getDetail(packCommonSearchDto);
        if(ObjectUtil.isEmpty(detail)){
            return BigDecimal.ZERO;
        }
        /*没有核价模板时*/
        if(StrUtil.isEmpty(detail.getPricingTemplateId())){
            return BigDecimal.ZERO;
        }
        List<PricingTemplateItemVO> pricingTemplateItems = pricingTemplateService.getDetailsById(detail.getPricingTemplateId(), baseController.getUserCompany()).getPricingTemplateItems();

        List<PricingTemplateItemVO> collect = pricingTemplateItems.stream()
                .filter(i -> i.getSort() != null && StringUtils.equals(i.getShowFlag(), BaseGlobal.STATUS_CLOSE))
                .sorted(Comparator.comparing(PricingTemplateItemVO::getSort)).collect(Collectors.toList());

        String jsonMap = JSONUtil.toJsonStr(detail.getCalcItemVal());
        Map<String, Object>  hashMap = JSONUtil.parseObj(jsonMap).toBean(HashMap.class);
        for(String key:otherStatistics.keySet()){
            hashMap.put(key,otherStatistics.get(key));
        }
        if(CollUtil.isNotEmpty(collect)){
            BigDecimal formula = formula(collect.get(0).getExpressionShow().replaceAll(",",""), hashMap,decimal);
            return  formula;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 同步设计BOM核价到大货BOM
     * @param foreignId 资料包id
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void asyncCost(String foreignId) {
        // PackPricingVo detail = getDetail(new PackCommonSearchDto(foreignId, PackUtils.PACK_TYPE_DESIGN));
        // if(ObjectUtil.isEmpty(detail)){
        //     throw new OtherException("设计BOM核价信息不存在");
        // }
        // PackInfo packInfo = packInfoService.getById(foreignId);
        // if(ObjectUtil.isEmpty(packInfo)){
        //     throw new OtherException("资料包信息不存在");
        // }
        // PackPricingVo bigGoodsDetail = getDetail(new PackCommonSearchDto(foreignId, PackUtils.PACK_TYPE_BIG_GOODS));
        // if(ObjectUtil.isEmpty(bigGoodsDetail)){
        //     bigGoodsDetail = new PackPricingVo();
        //     bigGoodsDetail.setForeignId(foreignId);
        //     bigGoodsDetail.setPackType(PackUtils.PACK_TYPE_BIG_GOODS);
        // }
        // //旧数据的物料费不变
        // JSONObject jsonObject = JSON.parseObject(detail.getCalcItemVal());
        // if (jsonObject!=null){
        //     jsonObject.remove("物料费");
        //     bigGoodsDetail.setCalcItemVal(jsonObject.toJSONString());
        //     JSONObject jsonObject1 = JSON.parseObject(bigGoodsDetail.getCalcItemVal());
        //     if (jsonObject1!=null){
        //         String string = jsonObject1.getString("物料费");
        //         jsonObject.put("物料费",string);
        //     }
        // }else {
        //     bigGoodsDetail.setCalcItemVal("");
        // }
        //
        // bigGoodsDetail.setPricingTemplateId(detail.getPricingTemplateId());
        //
        // saveByDto(BeanUtil.copyProperties(bigGoodsDetail,PackPricingDto.class));

        //同步其他费用
        packPricingOtherCostsService.copy(foreignId,PackUtils.PACK_TYPE_DESIGN,foreignId,PackUtils.PACK_TYPE_BIG_GOODS,BaseGlobal.YES);
        //同步加工费用
        packPricingProcessCostsService.copy(foreignId,PackUtils.PACK_TYPE_DESIGN,foreignId,PackUtils.PACK_TYPE_BIG_GOODS,BaseGlobal.YES);
        //同步二次加工费用
        packPricingCraftCostsService.copy(foreignId,PackUtils.PACK_TYPE_DESIGN,foreignId,PackUtils.PACK_TYPE_BIG_GOODS,BaseGlobal.YES);


    }

    /**
     * 获取核价信息中的路由信息
     *
     * @param styleNo
     * @return
     */
    @Override
    public Map getPricingRoute(String styleNo) {
        if (StrUtil.isEmpty(styleNo)) {
            throw new OtherException("参数为空");
        }
        styleNo= com.base.sbc.config.utils.StringUtils.convertList(URLDecoder.decode(styleNo)).get(1);
        Map<String, String> stringMap = new HashMap<>();
        QueryWrapper<PackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("style_no", styleNo);
        PackInfo packInfo = packInfoService.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(packInfo)) {
            throw new OtherException(styleNo + "无资料包信息");
        }
        stringMap.put("id", packInfo.getId());
        stringMap.put("styleId", packInfo.getStyleId());
        stringMap.put("style", packInfo.getStyleNo());
        stringMap.put("packType", PackUtils.PACK_TYPE_BIG_GOODS);
        return stringMap;
    }

    /**
     * 生成核价信息
     *
     * @param styleId
     * @param foreignId
     * @return
     */
    @Override
    public PackPricing createPackPricing(String styleId, String foreignId) {
        /*获取到款式的生产类型再去查询核价模板新增核价信息*/
        Style style = styleService.getById(styleId);
        if (ObjectUtil.isEmpty(style)) {
            throw new OtherException("没有款式信息");
        }
//        查询核价模板
        QueryWrapper<PricingTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("devt_type", style.getDevtType());
        queryWrapper.eq("brand", style.getBrand());
        queryWrapper.eq("default_flag", BaseGlobal.YES);
        List<PricingTemplate> list = pricingTemplateService.list(queryWrapper);
        PackPricing packPricing = new PackPricing();
        if (!CollUtil.isEmpty(list)) {
            PricingTemplate pricingTemplate = list.get(0);
            Map<String, Object> map = new HashMap<>();
            /*获取核价的字段*/
            List<PricingTemplateItem> itemServiceByList = pricingTemplateItemService.list(new QueryWrapper<PricingTemplateItem>().eq("pricing_template_id", pricingTemplate.getId()));
            itemServiceByList.forEach(f -> {
                map.put(f.getName(), StrUtil.isNotBlank(f.getDefaultNum()) ? f.getDefaultNum() : 0);
            });
            map.put("currencyCode", "");
            map.put("pricingTemplateId", pricingTemplate.getId());
            map.put("pricingTemplateName", pricingTemplate.getTemplateName());
            map.put("costPrice", 0);
            packPricing.setPackType(PackUtils.PACK_TYPE_DESIGN);
            packPricing.setForeignId(foreignId);
            packPricing.setCalcItemVal(JSON.toJSONString(map));
            packPricing.setPricingTemplateId(pricingTemplate.getId());
        }else {
            throw new OtherException("品牌："+style.getBrandName()+"下无"+style.getDevtTypeName()+"生产类型模板");
        }

        baseMapper.insert(packPricing);
        return packPricing;
    }

    @Override
    public PackPricing getByForeignIdOne(String foreignId, String packType) {
        return get(foreignId,packType);
    }

    /**
     * 重新计算核价中的JSON
     *
     * @param foreignId
     * @param packType
     * @return
     */
    @Override
    public boolean calculatePricingJson(String foreignId, String packType) {
        return calculatePricingJson(foreignId,packType,new HashMap<>());
    }

    /**
     * 重新计算核价中的JSON
     * @param foreignId
     * @param packType
     * * @param fields 可修改字段
     * @return
     */
    @Override
//    @Async
    public boolean calculatePricingJson(String foreignId, String packType,Map<String,Object> fieldsMap) {
        PackPricing packPricing = get(foreignId, packType);
        if (StrUtil.isEmpty(packPricing.getPricingTemplateId())) {
            throw new OtherException("请选择一个模板");
        }
        /*修改模板*/
        if(fieldsMap.containsKey("pricingTemplateId")){
            packPricing.setPricingTemplateId(fieldsMap.get("pricingTemplateId").toString());
        }
        PricingTemplateVO pricingTemplateVO = pricingTemplateService.getDetailsById(packPricing.getPricingTemplateId(),getCompanyCode());

        List<PricingTemplateItemVO> pricingTemplateItems = pricingTemplateVO.getPricingTemplateItems();
        Map<String, Object> resultMap = new HashMap<>();
        /*拿模板中的字段数据*/
        if (CollUtil.isNotEmpty(pricingTemplateItems)) {
            resultMap = pricingTemplateItems.stream()
                    .collect(Collectors.toMap(PricingTemplateItemVO::getName, PricingTemplateItemVO::getDefaultNum));
        }
        PackCommonSearchDto packCommonSearchDto = new PackCommonSearchDto();
        packCommonSearchDto.setPackType(packType);
        packCommonSearchDto.setForeignId(foreignId);
        /*原有数据库的数据*/
       if(StrUtil.isNotBlank(packPricing.getCalcItemVal())){
           resultMap.putAll(JSONObject.parseObject(packPricing.getCalcItemVal(), Map.class));
       }
        /*替换json中的数据*/
        if(CollUtil.isNotEmpty(fieldsMap)){
            resultMap.putAll(fieldsMap);
        }
        /*计算各项的值*/
        Map<String, BigDecimal> map = calculateCosts(packCommonSearchDto);
        /*生成新的json map*/
        resultMap.putAll(map);
/*        PackBomPageSearchDto packBomPageSearchDto = new PackBomPageSearchDto();
        packBomPageSearchDto.setPackType(packType);
        packBomPageSearchDto.setForeignId(foreignId);
        List<PackBomVo> list = packBomService.pageInfo(packBomPageSearchDto).getList();
        if(CollUtil.isNotEmpty(list)){
            List<BigDecimal> collect = list.stream().map(PackBomVo::getCost).collect(Collectors.toList());
            resultMap.put("costPrice", collect.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }*/
        resultMap.put("costPrice", map.get("物料费"));
        resultMap.put("pricingTemplateId", packPricing.getPricingTemplateId());
        resultMap.put("pricingTemplateName", pricingTemplateVO.getTemplateName());
        resultMap.put("currencyCode", "");
        resultMap.put("成本价", countTotalPrice(foreignId, StrUtil.equals(packType, PackUtils.PACK_TYPE_DESIGN) ? BaseGlobal.YES : "", 2));
        packPricing.setCalcItemVal(JSONObject.toJSONString(resultMap));
        baseMapper.updateById(packPricing);
        return true;
    }
    /**
     * 从物料清单同步数据到核价
     *
     * @param dto
     * @return
     */
    @Override
    public boolean syncPricingBom(SyncPricingBomDto dto) {
        //行id不为空着 同步单行数据
        if(StrUtil.isNotBlank(dto.getPricingBomId())){
            /*同步单个物料*/
            PackBom one = packBomService.getById(dto.getBomId());

            PackPricingBom pricingBom = packPricingBomService.getById(dto.getPricingBomId());
            if(one != null&&pricingBom!=null){
                /*重新获取供应商报价信息*/
                SupplierDetailPriceDto supplierDetailPriceDto = new SupplierDetailPriceDto();
                supplierDetailPriceDto.setWidth(one.getTranslateCode());
                supplierDetailPriceDto.setSupplierId(one.getSupplierId());
                supplierDetailPriceDto.setMaterialCode(one.getMaterialCode());
                supplierDetailPriceDto.setColor(one.getColorCode());
                BigDecimal price = basicsdatumMaterialPriceDetailService.gatSupplierPrice(supplierDetailPriceDto).getQuotationPrice();
                //获取单件用量
                BigDecimal unitUse=one.getUnitUse();
                UpdateWrapper<PackPricingBom> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda()
                        .eq(PackPricingBom::getForeignId,dto.getForeignId())
                        .eq(PackPricingBom::getPackType,dto.getPackType())
                        .eq(PackPricingBom::getId,dto.getPricingBomId())
                        .set(PackPricingBom::getPlanningLoossRate,one.getPlanningLoossRate())
                        .set(PackPricingBom::getBulkPrice,one.getBulkPrice())
                        .set(PackPricingBom::getDesignUnitUse,one.getDesignUnitUse())
                        .set(PackPricingBom::getDesignPrice,one.getDesignPrice())
                        .set(PackPricingBom::getPrice,price)
                        .set(PackPricingBom::getGramWeight,one.getGramWeight())
                        .set(PackPricingBom::getPartCode,one.getPartCode())
                        .set(PackPricingBom::getPartName,one.getPartName())
                        .set(PackPricingBom::getLossRate,one.getLossRate())
                        .set(PackPricingBom::getCost,one.getCost())
                        .set(PackPricingBom::getStockUnitName,one.getStockUnitName())
                        .set(PackPricingBom::getStockUnitCode,one.getStockUnitCode())
                        .set(PackPricingBom::getTranslate,one.getTranslate())
                        .set(PackPricingBom::getCollocationCode,one.getCollocationCode())
                        .set(PackPricingBom::getCollocationName,one.getCollocationName())
                        .set(PackPricingBom::getUnitUse,unitUse);
                packPricingBomService.update(updateWrapper);
            }else{
                throw new OtherException("物料清单不存在");
            }
        }else{
            //全量同步
            //删除之前的（物理删除）
            BaseQueryWrapper<PackPricingBom> pricingBomQw = new BaseQueryWrapper<>();
            PackUtils.commonQw(pricingBomQw,dto);
            pricingBomQw.isNotNull("bom_id");
            packPricingBomService.physicalDeleteQWrap(pricingBomQw);

            List<PackBomVo> enableVersionBomList = packBomVersionService.getEnableVersionBomList(dto.getForeignId(), dto.getPackType());
            if(CollUtil.isNotEmpty(enableVersionBomList)){
//                packBomService.querySubList(enableVersionBomList);
                /*用与新增*/
                List<PackPricingBom> packPricingBoms=new ArrayList<>();
                for (PackBomVo packBomVo : enableVersionBomList) {
                    PackPricingBom pbom=BeanUtil.copyProperties(packBomVo, PackPricingBom.class);
                    pbom.insertInit();
                    pbom.setBomId(packBomVo.getId());
                    pbom.setId(null);
                    /*重新获取报价*/
                  /*  SupplierDetailPriceDto supplierDetailPriceDto = new SupplierDetailPriceDto();
                    supplierDetailPriceDto.setWidth(packBomVo.getTranslateCode());
                    supplierDetailPriceDto.setSupplierId(packBomVo.getSupplierId());
                    supplierDetailPriceDto.setMaterialCode(packBomVo.getMaterialCode());
                    supplierDetailPriceDto.setColor(packBomVo.getColorCode());
                    BigDecimal price = basicsdatumMaterialPriceDetailService.gatSupplierPrice(supplierDetailPriceDto).getQuotationPrice();
                    pbom.setPrice(price);*/
                    packPricingBoms.add(pbom);
                }
                if(CollUtil.isNotEmpty(packPricingBoms)){
                    packPricingBomService.saveBatch(packPricingBoms);
                }
            }
        }
        /*重新计算核价*/
        calculatePricingJson(dto.getForeignId(),dto.getPackType());
        return true;
    }


    @Override
    public Map<String, BigDecimal>  calculateCosts(PackCommonSearchDto dto) {
        Map<String, BigDecimal> temp = new HashMap<>(16);
        //其他费用统计
        Map<String, BigDecimal> otherStatistics = packPricingOtherCostsService.statistics(dto);
        temp.putAll(otherStatistics);
        //物料费用统计
        temp.put("物料费", packPricingBomService.calculateCosts(dto));
        //统计加工费用
        temp.put("加工费", packPricingProcessCostsService.calculateCosts(dto));
        //统计二次加工费用
        temp.put("二次加工费", packPricingCraftCostsService.calculateCosts(dto));
        BigDecimal otherCosts = new BigDecimal(0);
        for (String s : otherStatistics.keySet()) {
            otherCosts = otherCosts.add(otherStatistics.get(s));
        }
//        temp.put("其他费",otherCosts);

        Map<String, BigDecimal> result = new HashMap<>(16);
        for (Map.Entry<String, BigDecimal> a : temp.entrySet()) {
            if (a.getValue() != null) {
                result.put(a.getKey(), a.getValue().setScale(3, RoundingMode.HALF_UP));
            }
        }
        return result;
    }

    /**
     * 计算价格
     * @param formula
     * @param itemVal
     * @param decimal 保留小数点
     * @return
     */
    @Override
    public BigDecimal formula(String formula, Map<String, Object> itemVal,Integer decimal) {
        try {
            JEP jep = new JEP();
            for (Map.Entry<String, Object> item : itemVal.entrySet()) {
                if (NumberUtil.isNumber(String.valueOf(item.getValue()))) {
                    jep.addVariable(item.getKey(), Double.parseDouble(  ObjectUtil.isEmpty(item.getValue())?"0":item.getValue().toString()));
                }
            }
            jep.parseExpression(formula);
            double value = jep.getValue();
            BigDecimal b = new BigDecimal(value);
            return b.setScale(decimal, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new OtherException("计算异常,请检查公式是否有误");
        }
    }


    @Override
    String getModeName() {
        return "核价信息";
    }

// 自定义方法区 不替换的区域【other_end】

}
