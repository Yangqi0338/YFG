/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.SupplierDetailPriceDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPriceDetail;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialPriceDetailMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialPriceMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPriceDetailVo;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-物料档案-供应商报价- service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService
 * @email your email
 * @date 创建时间：2023-7-27 17:53:42
 */
@Service
public class BasicsdatumMaterialPriceDetailServiceImpl extends ServiceImpl<BasicsdatumMaterialPriceDetailMapper, BasicsdatumMaterialPriceDetail> implements BasicsdatumMaterialPriceDetailService {

    @Autowired
    private BasicsdatumMaterialPriceMapper basicsdatumMaterialPriceMapper;

    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void copyByMaterialCode(String materialCode, String newMaterialCode) {
        QueryWrapper<BasicsdatumMaterialPriceDetail> qw = new QueryWrapper<>();
        qw.eq("material_code", materialCode);
        List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialOlds = super.list(qw);
        if (CollectionUtils.isNotEmpty(basicsdatumMaterialOlds)) {
            basicsdatumMaterialOlds.forEach(e -> {
                e.setId(null);
                e.setMaterialCode(newMaterialCode);
            });
            super.saveBatch(basicsdatumMaterialOlds);
        }
    }

    /**
     * 获取供应商规格颜色
     *
     * @param supplierDetailPriceDto
     * @return
     */
    @Override
    public List<BasicsdatumMaterialPriceDetailVo> gatSupplierWidthColorList(SupplierDetailPriceDto supplierDetailPriceDto) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("material_code", supplierDetailPriceDto.getMaterialCode());
        queryWrapper.eq("supplier_id", supplierDetailPriceDto.getSupplierId());
        queryWrapper.eq("status", BaseGlobal.STATUS_NORMAL);
        List<BasicsdatumMaterialPriceDetailVo> list = new ArrayList<>();
        /*查询出供应商报价*/
        List<BasicsdatumMaterialPrice> pricelList = basicsdatumMaterialPriceMapper.selectList(queryWrapper);
        queryWrapper.clear();
        if (CollUtil.isNotEmpty(pricelList)) {
            /*查询供应商详情标价*/
            queryWrapper.in("price_id", pricelList.stream().map(BasicsdatumMaterialPrice::getId).collect(Collectors.toList()));
            List<BasicsdatumMaterialPriceDetail> priceDetailList = baseMapper.selectList(queryWrapper);
            if (!org.springframework.util.CollectionUtils.isEmpty(priceDetailList)) {
                /*用于去重*/
                boolean b = StringUtils.isNotBlank(supplierDetailPriceDto.getWidth());
                list = BeanUtil.copyToList(CollUtil.distinct(priceDetailList, b ? BasicsdatumMaterialPriceDetail::getWidth : BasicsdatumMaterialPriceDetail::getColor, true), BasicsdatumMaterialPriceDetailVo.class);
            }
        }
        return list;
    }

    /**
     * 查询供应商规格
     *
     * @param materialCodeList
     * @return
     */
    @Override
    public List<BomSelMaterialVo> querySupplierWidth(List<String> materialCodeList) {
        QueryWrapper qw = new QueryWrapper<>();
        qw.in("tbmpd.material_code", materialCodeList);
        qw.in("tbmp.select_flag", BaseGlobal.YES);
        return baseMapper.querySupplierWidth(qw);
    }

    /**
     * 获取供应商详情价格
     *
     * @param supplierDetailPriceDto
     * @return
     */
    @Override
    public BasicsdatumMaterialPriceDetailVo gatSupplierPrice(SupplierDetailPriceDto supplierDetailPriceDto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("supplier_id",supplierDetailPriceDto.getSupplierId());
        queryWrapper.eq("material_code",supplierDetailPriceDto.getMaterialCode());
        queryWrapper.eq("color",supplierDetailPriceDto.getColor());
        queryWrapper.eq("width",supplierDetailPriceDto.getWidth());
        BasicsdatumMaterialPriceDetailVo basicsdatumMaterialPriceDetailVo =new BasicsdatumMaterialPriceDetailVo();
        BasicsdatumMaterialPriceDetail basicsdatumMaterialPriceDetail =  baseMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(basicsdatumMaterialPriceDetail)){
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        BeanUtils.copyProperties(basicsdatumMaterialPriceDetail, basicsdatumMaterialPriceDetailVo);
        return basicsdatumMaterialPriceDetailVo;
    }

// 自定义方法区 不替换的区域【other_end】

}

