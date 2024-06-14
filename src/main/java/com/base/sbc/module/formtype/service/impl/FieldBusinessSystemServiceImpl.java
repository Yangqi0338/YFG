/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.FieldBusinessSystemQueryDto;
import com.base.sbc.module.formtype.entity.FieldBusinessSystem;
import com.base.sbc.module.formtype.mapper.FieldBusinessSystemMapper;
import com.base.sbc.module.formtype.service.FieldBusinessSystemService;
import com.base.sbc.module.formtype.vo.FieldBusinessSystemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：字段对应下游系统关系 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.formtype.service.FieldBusinessSystemService
 * @email your email
 * @date 创建时间：2024-5-31 10:35:28
 */
@Service
public class FieldBusinessSystemServiceImpl extends BaseServiceImpl<FieldBusinessSystemMapper, FieldBusinessSystem> implements FieldBusinessSystemService {


    @Override
    public List<FieldBusinessSystemVo> findList(FieldBusinessSystemQueryDto dto) {
        Page<Object> objects = PageHelper.startPage(dto);
        BaseQueryWrapper<FieldBusinessSystem> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("tfbs.business_type", dto.getBusinessType());
        qw.eq("tfbs.del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        List<FieldBusinessSystemVo> list = baseMapper.findPage(qw);
        return list;
    }


}
