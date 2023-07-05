/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackSampleReviewDto;
import com.base.sbc.module.pack.entity.PackSampleReview;
import com.base.sbc.module.pack.mapper.PackSampleReviewMapper;
import com.base.sbc.module.pack.service.PackSampleReviewService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSampleReviewVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：资料包-样衣评审 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSampleReviewService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 11:09:05
 */
@Service
public class PackSampleReviewServiceImpl extends BaseServiceImpl<PackSampleReviewMapper, PackSampleReview> implements PackSampleReviewService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackSampleReviewVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackSampleReview> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        Page<PackSampleReview> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackSampleReview> pageInfo = page.toPageInfo();
        PageInfo<PackSampleReviewVo> voPageInfo = CopyUtil.copy(pageInfo, PackSampleReviewVo.class);
        return voPageInfo;
    }

    @Override
    public PackSampleReviewVo getDetail(String id) {
        PackSampleReview byId = getById(id);
        return BeanUtil.copyProperties(byId, PackSampleReviewVo.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackSampleReviewVo saveByDto(PackSampleReviewDto dto) {
        PackSampleReview pageData = BeanUtil.copyProperties(dto, PackSampleReview.class);
        if (CommonUtils.isInitId(pageData.getId())) {
            pageData.setId(null);
            save(pageData);
            dto.setId(pageData.getId());
        } else {
            PackSampleReview dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
        }
        return getDetail(dto.getId());
    }


// 自定义方法区 不替换的区域【other_end】

}
