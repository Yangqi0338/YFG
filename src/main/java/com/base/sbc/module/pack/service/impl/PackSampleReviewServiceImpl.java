/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
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

import static com.base.sbc.module.common.convert.ConvertContext.PACK_CV;

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
public class PackSampleReviewServiceImpl extends AbstractPackBaseServiceImpl<PackSampleReviewMapper, PackSampleReview> implements PackSampleReviewService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackSampleReviewVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackSampleReview> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        if (StrUtil.isBlank(dto.getOrderBy())) {
            qw.orderByDesc("id");
        }
        Page<PackSampleReview> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackSampleReview> pageInfo = page.toPageInfo();
        return CopyUtil.copy(pageInfo, PackSampleReviewVo.class);
    }

    @Override
    public PackSampleReviewVo getDetail(String id) {
        return PACK_CV.copy2Vo(getById(id));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackSampleReviewVo saveByDto(PackSampleReviewDto dto) {
        if (CommonUtils.isInitId(dto.getId())) {
            dto.insertInit();
            PackSampleReview pageData = PACK_CV.copy2Entity(dto);
            save(pageData);
            dto.setId(pageData.getId());
        } else {
            PackSampleReview dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            saveOrUpdateOperaLog(dto, dbData, genOperaLogEntity(dbData, "修改"));
            PACK_CV.copy2Entity(dto, dbData);
            updateById(dbData);
        }
        return getDetail(dto.getId());
    }

    @Override
    String getModeName() {
        return "样衣评审";
    }


// 自定义方法区 不替换的区域【other_end】

}
