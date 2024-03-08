/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibraryAgent;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourLibraryAgentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：基础资料-颜色库 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumColourLibraryAgentDao
 * @email your email
 * @date 创建时间：2024-2-28 16:13:32
 */
@Mapper
public interface BasicsdatumColourLibraryAgentMapper extends BaseMapper<BasicsdatumColourLibraryAgent> {
    List<BasicsdatumColourLibraryAgentVo> findList(@Param(Constants.WRAPPER) BaseQueryWrapper<BasicsdatumColourLibraryAgent> queryWrapper);

}