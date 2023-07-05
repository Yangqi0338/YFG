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
import com.base.sbc.module.pack.dto.PackBusinessOpinionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.entity.PackBusinessOpinion;
import com.base.sbc.module.pack.mapper.PackBusinessOpinionMapper;
import com.base.sbc.module.pack.service.PackBusinessOpinionService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBusinessOpinionVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：资料包-业务意见 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBusinessOpinionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 11:09:08
 */
@Service
public class PackBusinessOpinionServiceImpl extends BaseServiceImpl<PackBusinessOpinionMapper, PackBusinessOpinion> implements PackBusinessOpinionService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackBusinessOpinionVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackBusinessOpinion> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        Page<PackBusinessOpinion> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackBusinessOpinion> pageInfo = page.toPageInfo();
        PageInfo<PackBusinessOpinionVo> voPageInfo = CopyUtil.copy(pageInfo, PackBusinessOpinionVo.class);
        return voPageInfo;
    }

    @Override
    public PackBusinessOpinionVo getDetail(String id) {
        PackBusinessOpinion byId = getById(id);
        return BeanUtil.copyProperties(byId, PackBusinessOpinionVo.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackBusinessOpinionVo saveByDto(PackBusinessOpinionDto dto) {
        PackBusinessOpinion pageData = BeanUtil.copyProperties(dto, PackBusinessOpinion.class);
        if (CommonUtils.isInitId(pageData.getId())) {
            pageData.setId(null);
            save(pageData);
            dto.setId(pageData.getId());
        } else {
            PackBusinessOpinion dbData = getById(dto.getId());
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
