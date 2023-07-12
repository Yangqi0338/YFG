/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookDesignUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookPlanUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookPriceUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookProductPlanUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookQueryDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookSaveDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookUserUpdateDto;
import com.base.sbc.module.sample.entity.SampleStyleOrderBook;
import com.base.sbc.module.sample.vo.SampleStyleOrderBookPageVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：款式管理-订货本 service类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:29
 * @version 1.0
 */
public interface SampleStyleOrderBookService extends BaseService<SampleStyleOrderBook>{

	PageInfo<SampleStyleOrderBookPageVo> getStyleOrderBookList(SampleStyleOrderBookQueryDto dto);

	Boolean saveSampleStyleOrderBook(SampleStyleOrderBookSaveDto dto);

	Boolean updateSampleStyleOrderBook(SampleStyleOrderBookUpdateDto dto);

	Boolean delSampleStyleOrderBookItem(String id);

	Boolean updateSampleStyleOrderBookPrice(SampleStyleOrderBookPriceUpdateDto dto);

	Boolean updateSampleStyleOrderBookUser(SampleStyleOrderBookUserUpdateDto dto);

	Boolean updateSampleStyleOrderBookDesign(SampleStyleOrderBookDesignUpdateDto dto);

	Boolean updateSampleStyleOrderBookPlan(SampleStyleOrderBookPlanUpdateDto dto);

	Boolean updateSampleStyleOrderBookProductPlan(SampleStyleOrderBookProductPlanUpdateDto dto);
	
}
