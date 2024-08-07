/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingBomDto;
import com.base.sbc.module.pack.entity.PackPricingBom;
import com.base.sbc.module.pack.mapper.PackPricingBomMapper;
import com.base.sbc.module.pack.service.PackPricingBomService;
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingBomVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：资料包-物料清单 service类
 * @address com.base.sbc.module.pack.service.PackPricingBomService
 * @author 孟繁江
 * @email your email
 * @date 创建时间：2024-7-29 17:56:54
 * @version 1.0
 */
@Service
public class PackPricingBomServiceImpl extends AbstractPackBaseServiceImpl<PackPricingBomMapper, PackPricingBom> implements PackPricingBomService {


// 自定义方法区 不替换的区域【other_start】
        @Autowired
        @Lazy
        private PackPricingService packPricingService;

    /**
     * 查询核价清单
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo pageInfo(PackCommonPageSearchDto dto) {
        BaseQueryWrapper<PackPricingBom> queryWrapper = new BaseQueryWrapper<>();
        PackUtils.commonQw(queryWrapper, dto);
        Page<PackPricingBomVo> objects = PageHelper.startPage(dto);
        this.list(queryWrapper);
        return objects.toPageInfo();
    }

    /**
     * 计算核价总成本
     *
     * @param dto
     * @return
     */
    @Override
    public BigDecimal calculateCosts(PackCommonSearchDto dto) {

        BigDecimal result = BigDecimal.ZERO;
        BaseQueryWrapper<PackPricingBom> queryWrapper = new BaseQueryWrapper<>();
        PackUtils.commonQw(queryWrapper, dto);

        List<PackPricingBom> bomList = list(queryWrapper);
        if (CollUtil.isEmpty(bomList)) {
            return result;
        }
        /*设计还是大货*/
        Boolean loss = "packDesign".equals(dto.getPackType());
        //款式物料费用=款式物料用量*款式物料单价*（1+损耗率)
        //大货物料费用=物料大货用量*物料大货单价*（1+额定损耗率)
        return bomList.stream().map(packBom -> {
                    /*获取损耗率*/
            BigDecimal divide = BigDecimal.ONE.add(Optional.ofNullable(!loss ? packBom.getPlanningLoossRate() : packBom.getLossRate()).orElse(BigDecimal.ZERO).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP));
            BigDecimal mul = NumberUtil.mul(
                    !loss ? packBom.getBulkUnitUse() : packBom.getDesignUnitUse(),
                    !loss ? Optional.ofNullable(packBom.getPurchasePrice()).orElse(BigDecimal.ZERO) : packBom.getPrice(),
                    divide
                    );
                    return mul;
                }
        ).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).setScale(3, RoundingMode.HALF_UP);

    }

    /**
     * 复制
     *
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @param map             物料清单idmap
     * @return
     */
    @Override
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, Map<String, String> map) {
        //删除目标的数据
        physicsDel(targetForeignId, targetPackType);
        /*获取上阶段的物料费用*/
        List<PackPricingBom> list = list(sourceForeignId, sourcePackType);
        if (CollUtil.isNotEmpty(list)) {
            for (PackPricingBom packPricingBom : list) {
                packPricingBom.setBomId(map.get(packPricingBom.getBomId()));
                packPricingBom.setPackType(targetPackType);
                packPricingBom.setForeignId( targetForeignId);
                packPricingBom.setId(null);
                packPricingBom.insertInit();
                packPricingBom.updateInit();
            }
            saveBatch(list);
        }
        return true;
    }

    /**
     * 修改保存
     *
     * @param list
     * @return
     */
    @Override
    public boolean saveOrUpdate(List<PackPricingBomDto> list) {
        List<PackPricingBom> pricingBomList = BeanUtil.copyToList(list, PackPricingBom.class);
        for (PackPricingBom packPricingBom : pricingBomList) {
            packPricingBom.calculateCost();
        }
        this.saveOrUpdateBatch(pricingBomList);
        packPricingService.calculatePricingJson(list.get(0).getForeignId(),list.get(0).getPackType());
        return true;
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Override
    public boolean pricingBomDel(IdsDto ids) {
        List<PackPricingBom> packPricingBoms = baseMapper.selectBatchIds(StringUtils.convertList(ids.getId()));
        removeBatchByIds(StrUtil.split(ids.getId(), CharUtil.COMMA));
        packPricingService.calculatePricingJson(packPricingBoms.get(0).getForeignId(),packPricingBoms.get(0).getPackType());
        return true;
    }

    @Override
    String getModeName() {
        return "资料包-物料费用";
    }


// 自定义方法区 不替换的区域【other_end】

}
