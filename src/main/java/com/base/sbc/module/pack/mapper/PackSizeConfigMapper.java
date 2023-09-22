/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.pack.entity.PackSizeConfig;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：资料包-尺寸表配置 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.dao.PackSizeConfigDao
 * @email your email
 * @date 创建时间：2023-9-1 14:07:14
 */
@Mapper
public interface PackSizeConfigMapper extends BaseMapper<PackSizeConfig> {
// 自定义方法区 不替换的区域【other_start】


   List<PackSizeConfigVo> sizeConfigList(@Param(Constants.WRAPPER) QueryWrapper qw);

// 自定义方法区 不替换的区域【other_end】
}

