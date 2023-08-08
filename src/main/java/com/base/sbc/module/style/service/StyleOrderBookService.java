/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.StyleOrderBookDesignUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookPlanUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookPriceUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookProductPlanUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookQueryDto;
import com.base.sbc.module.style.dto.StyleOrderBookSaveDto;
import com.base.sbc.module.style.dto.StyleOrderBookUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookUserUpdateDto;
import com.base.sbc.module.style.entity.StyleOrderBook;
import com.base.sbc.module.style.vo.StyleOrderBookPageVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：款式管理-订货本 service类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:29
 * @version 1.0
 */
public interface StyleOrderBookService extends BaseService<StyleOrderBook>{

	PageInfo<StyleOrderBookPageVo> getStyleOrderBookList(StyleOrderBookQueryDto dto);

	Boolean saveSampleStyleOrderBook(StyleOrderBookSaveDto dto);

	Boolean updateSampleStyleOrderBook(StyleOrderBookUpdateDto dto);

	Boolean delSampleStyleOrderBookItem(String id);

	Boolean updateSampleStyleOrderBookPrice(StyleOrderBookPriceUpdateDto dto);

	Boolean updateSampleStyleOrderBookUser(StyleOrderBookUserUpdateDto dto);

	Boolean updateSampleStyleOrderBookDesign(StyleOrderBookDesignUpdateDto dto);

	Boolean updateSampleStyleOrderBookPlan(StyleOrderBookPlanUpdateDto dto);

	Boolean updateSampleStyleOrderBookProductPlan(StyleOrderBookProductPlanUpdateDto dto);
	
}
