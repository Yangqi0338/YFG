/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.formtype.vo.FieldBusinessSystemVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.formtype.entity.FieldBusinessSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：字段对应下游系统关系 dao类
 * @address com.base.sbc.module.formtype.dao.FieldBusinessSystemDao
 * @author your name  
 * @email  your email
 * @date 创建时间：2024-5-31 10:35:28 
 * @version 1.0  
 */
@Mapper
public interface FieldBusinessSystemMapper extends BaseMapper<FieldBusinessSystem> {

    List<FieldBusinessSystemVo> findPage(@Param(Constants.WRAPPER) BaseQueryWrapper<FieldBusinessSystem> qw);

}