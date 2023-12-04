/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackPricing;
import com.base.sbc.module.pack.mapper.PackPricingMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingVo;
import com.base.sbc.module.pricing.service.PricingTemplateService;
import com.base.sbc.module.pricing.vo.PricingTemplateItemVO;
import org.apache.commons.lang3.StringUtils;
import org.nfunk.jep.JEP;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    PackInfoService packInfoService;


    @Override
    public PackPricingVo getDetail(PackCommonSearchDto dto) {
        PackPricing one = get(dto.getForeignId(), dto.getPackType());
        PackPricingVo packPricingVo = BeanUtil.copyProperties(one, PackPricingVo.class);
        return packPricingVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
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
        }
        return BeanUtil.copyProperties(one, PackPricingVo.class);
    }

    /**
     * 计算总价格
     * 默认查大货 flag=1 资料包什么阶段就查什么阶段
     * @param packInfoId
     * @param flag
     * @return
     */
    @Override
    public BigDecimal countTotalPrice(String packInfoId,String flag) {
/*默认查大货 */
        String packType = PackUtils.PACK_TYPE_BIG_GOODS;
        if(StrUtil.equals(flag,BaseGlobal.YES)){
 /*           PackInfoStatus packInfoStatus = packInfoStatusService.getByOne("foreign_id", packInfoId);
            packType =StrUtil.equals(packInfoStatus.getBomStatus(),BaseGlobal.NO)? PackUtils.PACK_TYPE_DESIGN:PackUtils.PACK_TYPE_BIG_GOODS;*/
             packType =   PackUtils.PACK_TYPE_DESIGN;
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
        BigDecimal formula = formula(collect.get(0).getExpressionShow().replaceAll(",",""), hashMap);
        return  formula;
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
     * @param visitParam
     * @return
     */
    @Override
    public Map getPricingRoute(String styleNo) {
        if (StrUtil.isEmpty(styleNo)) {
            throw new OtherException("参数为空");
        }
        styleNo= com.base.sbc.config.utils.StringUtils.convertList(styleNo).get(1);
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
        stringMap.put("packType", PackUtils.PACK_TYPE_DESIGN);
        return stringMap;
    }



    @Override
    public Map<String, BigDecimal>  calculateCosts(PackCommonSearchDto dto) {
        Map<String, BigDecimal> temp = new HashMap<>(16);
        //其他费用统计
        Map<String, BigDecimal> otherStatistics = packPricingOtherCostsService.statistics(dto);
        temp.putAll(otherStatistics);
        //物料费用统计
        temp.put("物料费", packBomService.calculateCosts(dto));
        //统计加工费用
        temp.put("加工费", packPricingProcessCostsService.calculateCosts(dto));
        //统计二次加工费用
        temp.put("二次加工费", packPricingCraftCostsService.calculateCosts(dto));

        BigDecimal otherCosts = new BigDecimal(0);
        for (String s : otherStatistics.keySet()) {
            otherCosts = otherCosts.add(otherStatistics.get(s));
        }
        temp.put("其他费",otherCosts);

        Map<String, BigDecimal> result = new HashMap<>(16);
        for (Map.Entry<String, BigDecimal> a : temp.entrySet()) {
            if (a.getValue() != null) {
                result.put(a.getKey(), a.getValue().setScale(3, RoundingMode.HALF_UP));
            }
        }
        return result;
    }

    @Override
    public BigDecimal formula(String formula, Map<String, Object> itemVal) {
        try {
            JEP jep = new JEP();
            for (Map.Entry<String, Object> item : itemVal.entrySet()) {
                if (NumberUtil.isNumber(String.valueOf(item.getValue()))) {
                    jep.addVariable(item.getKey(), Double.parseDouble(item.getValue().toString()));
                }
            }
            jep.parseExpression(formula);
            double value = jep.getValue();
            BigDecimal b = new BigDecimal(value);
            return b.setScale(2, RoundingMode.HALF_UP);
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
