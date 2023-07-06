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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackSizeDto;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.mapper.PackSizeMapper;
import com.base.sbc.module.pack.service.PackSizeService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：资料包-尺寸表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 10:14:51
 */
@Service
public class PackSizeServiceImpl extends BaseServiceImpl<PackSizeMapper, PackSize> implements PackSizeService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackSizeVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackSize> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        if (StrUtil.isBlank(dto.getOrderBy())) {
            qw.orderByDesc("id");
        }
        Page<PackSize> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackSize> pageInfo = page.toPageInfo();
        PageInfo<PackSizeVo> voPageInfo = CopyUtil.copy(pageInfo, PackSizeVo.class);
        return voPageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackSizeVo saveByDto(PackSizeDto dto) {
        //新增
        if (StrUtil.isBlank(dto.getId()) || StrUtil.contains(dto.getId(), StrUtil.DASHED)) {
            PackSize pageData = BeanUtil.copyProperties(dto, PackSize.class);
            pageData.setId(null);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackSizeVo.class);
        }
        //修改
        else {
            PackSize dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            boolean b = updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackSizeVo.class);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean del(String id) {
        removeBatchByIds(StrUtil.split(id, CharUtil.COMMA));
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatchByDto(PackCommonSearchDto commonDto, List<PackSizeDto> dtoList) {
        List<PackSize> packSizes = BeanUtil.copyToList(dtoList, PackSize.class);
        if (CollUtil.isNotEmpty(packSizes)) {
            for (PackSize packSize : packSizes) {
                packSize.setForeignId(commonDto.getForeignId());
                packSize.setPackType(commonDto.getPackType());
            }
        }
        QueryWrapper<PackSize> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, commonDto);
        addAndUpdateAndDelList(packSizes, qw);
        return true;
    }

// 自定义方法区 不替换的区域【other_end】

}

