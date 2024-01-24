/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：正确样管理 dao类
 * @address com.base.sbc.module.style.dao.StyleColorCorrectInfoDao
 * @author your name  
 * @email  your email
 * @date 创建时间：2023-12-26 10:13:31 
 * @version 1.0  
 */
@Mapper
public interface StyleColorCorrectInfoMapper extends BaseMapper<StyleColorCorrectInfo> {

    List<StyleColorCorrectInfoVo> findList(@Param(Constants.WRAPPER) QueryWrapper qw );

}