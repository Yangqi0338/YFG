/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.datasource.SaveOrUpdateBatch;
import com.base.sbc.module.hangtag.vo.HangTagIngredientVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：基础加强mapper
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.common.mapper.BaseEnhanceMapper
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:55
 */
@Mapper
public interface BaseEnhanceMapper<T> extends BaseMapper<T> {

    int saveOrUpdateBatch(Collection<T> list);
//    int saveOrUpdateBatch(Collection<String> list);

}

