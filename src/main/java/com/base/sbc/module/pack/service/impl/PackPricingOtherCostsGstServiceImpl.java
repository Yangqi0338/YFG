/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.pack.dto.OtherCostsPageDto;
import com.base.sbc.module.pack.entity.PackPricingOtherCostsGst;
import com.base.sbc.module.pack.mapper.PackPricingOtherCostsGstMapper;
import com.base.sbc.module.pack.service.PackPricingOtherCostsGstService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingOtherCostsVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：资料包-核价信息-其他费用 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingOtherCostsService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:18
 */
@Service
public class PackPricingOtherCostsGstServiceImpl extends AbstractPackBaseServiceImpl<PackPricingOtherCostsGstMapper, PackPricingOtherCostsGst> implements PackPricingOtherCostsGstService {

// 自定义方法区 不替换的区域【other_start】
@Autowired
private MinioUtils minioUtils;

    @Override
    public PageInfo<PackPricingOtherCostsVo> pageInfo(OtherCostsPageDto dto) {
        Page<PackPricingOtherCostsGst> page = PageHelper.startPage(dto);
        BaseQueryWrapper<PackPricingOtherCostsGst> qw = new BaseQueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        List<PackPricingOtherCostsGst> list = list(qw);
        list.forEach(PackPricingOtherCostsGst::build);
        PageInfo<PackPricingOtherCostsGst> pageInfo = page.toPageInfo();
        list.forEach(it -> it.setPicUrl(minioUtils.getObjectUrl(it.getPicUrl())));
        return CopyUtil.copy(pageInfo, PackPricingOtherCostsVo.class);
    }

    @Override
    String getModeName() {
        return "其他费用";
    }

// 自定义方法区 不替换的区域【other_end】

}
