/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.patternmaking.vo.PatternMakingBarCodeVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述： dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dao.PatternMakingBarCodeDao
 * @email your email
 * @date 创建时间：2024-7-3 15:56:26
 */
@Mapper
public interface PatternMakingBarCodeMapper extends BaseMapper<PatternMakingBarCode> {

    List<PatternMakingBarCodeVo> findPage(@Param(Constants.WRAPPER) BaseQueryWrapper<PatternMakingBarCode> qw);

}