/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.common.mapper.BaseEnhanceMapper;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 类描述：品名多语言属性值配置表 dao类
 * @address com.base.sbc.module.moreLanguage.dao.StandardColumnCountryTranslateMapper
 * @author KC  
 * @email  KC
 * @date 创建时间：2023-11-30 15:07:41 
 * @version 1.0  
 */
@Mapper
public interface StandardColumnCountryTranslateMapper extends BaseEnhanceMapper<StandardColumnCountryTranslate> {
    List<Map<String, Object>> listAllByTable(@Param("fields") String fields, @Param("tableName") String tableName, @Param("where") String where);
// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
}