/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.style.dto.StyleOrderBookQueryDto;
import com.base.sbc.module.style.entity.StyleOrderBook;
import com.base.sbc.module.style.vo.StyleOrderBookPageVo;

/**
 * 类描述：款式管理-订货本 dao类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:29
 * @version 1.0
 */
@Mapper
public interface StyleOrderBookMapper extends BaseMapper<StyleOrderBook> {
	/**
	 * 订货本列表页面查询
	 * 
	 * @param companyCode
	 * @param dto
	 * @return
	 */
	List<StyleOrderBookPageVo> getStyleOrderBookList(StyleOrderBookQueryDto dto);
}