/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialIngredient;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.open.dto.BasicsdatumGarmentInspectionDetailDto;
import com.base.sbc.open.dto.BasicsdatumGarmentInspectionDto;
import com.base.sbc.open.entity.BasicsdatumGarmentInspectionDetail;
import com.base.sbc.open.mapper.BasicsdatumGarmentInspectionMapper;
import com.base.sbc.open.entity.BasicsdatumGarmentInspection;
import com.base.sbc.open.service.BasicsdatumGarmentInspectionDetailService;
import com.base.sbc.open.service.BasicsdatumGarmentInspectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：成衣成分送检 service类
 * @address com.base.sbc.open.service.BasicsdatumGarmentInspectionService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-17 15:18:54
 * @version 1.0  
 */
@Service
public class BasicsdatumGarmentInspectionServiceImpl extends BaseServiceImpl<BasicsdatumGarmentInspectionMapper, BasicsdatumGarmentInspection> implements BasicsdatumGarmentInspectionService {

    @Resource
    private BasicsdatumGarmentInspectionDetailService basicsdatumGarmentInspectionDetailService;

    @Override
    public void saveGarmentInspection(BasicsdatumGarmentInspectionDto garmentInspectionDto) {
        String year = garmentInspectionDto.getYear();
        String styleNo = garmentInspectionDto.getStyleNo();
        List<BasicsdatumGarmentInspectionDetailDto> detailDtoList = garmentInspectionDto.getGarmentInspectionDetailDtoList();
        if (StrUtil.isBlank(styleNo) ||  StrUtil.isBlank(year)) {
            throw new OtherException("大货款号或年份数据不能为空！");
        }
        if (CollUtil.isEmpty(detailDtoList)) {
            throw new OtherException("明细数据不能为空！");
        }

        QueryWrapper inspectionQueryWrapper = new QueryWrapper<BasicsdatumGarmentInspection>()
                .eq("style_no", garmentInspectionDto.getStyleNo())
                .eq("year", garmentInspectionDto);
        this.saveOrUpdate(garmentInspectionDto,inspectionQueryWrapper);


        QueryWrapper inspectionDetailQueryWrapper = new QueryWrapper<BasicsdatumGarmentInspectionDetail>()
                .eq("style_no", garmentInspectionDto.getStyleNo())
                .eq("year", garmentInspectionDto);

        basicsdatumGarmentInspectionDetailService.remove(inspectionDetailQueryWrapper);

        List<BasicsdatumGarmentInspectionDetail> basicsdatumGarmentInspectionDetails = BeanUtil.copyToList(detailDtoList, BasicsdatumGarmentInspectionDetail.class);

        basicsdatumGarmentInspectionDetailService.saveBatch(basicsdatumGarmentInspectionDetails);
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
