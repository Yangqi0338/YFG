/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.mapper.BaseEnhanceMapper;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：国家款式状态表 dao类
 * @address com.base.sbc.module.moreLanguage.dao.StyleCountryStatusMapper
 * @author KC  
 * @email  KC
 * @date 创建时间：2023-11-30 15:07:37 
 * @version 1.0  
 */
@Mapper
public interface StyleCountryStatusMapper extends BaseEnhanceMapper<StyleCountryStatus> {
// 自定义方法区 不替换的区域【other_start】

    long countByBulkStyleNo(@Param("bulkStyleNoList") List<String> bulkStyleNoList);

    List<StyleCountryStatus> queryList(@Param(Constants.WRAPPER) QueryWrapper<StyleCountryStatus> qw);

// 自定义方法区 不替换的区域【other_end】
}