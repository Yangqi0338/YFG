/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.StyleGroupItemSaveDto;
import com.base.sbc.module.style.dto.StyleGroupQueryDto;
import com.base.sbc.module.style.dto.StyleGroupSaveDto;
import com.base.sbc.module.style.entity.StyleGroup;
import com.base.sbc.module.style.vo.StyleGroupPageVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：款式管理-款式搭配 service类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:23
 * @version 1.0
 */
public interface StyleGroupService extends BaseService<StyleGroup>{

	PageInfo<StyleGroupPageVo> getStyleGroupList(StyleGroupQueryDto dto);

	Boolean delSampleStyleGroup(String id);

	StyleGroupPageVo saveSampleStyleGroup(StyleGroupSaveDto dto);

	StyleGroupPageVo getSampleStyleGroup(String id);

	PageInfo<StyleGroupPageVo> getStyleGroupItemByGroupCode(StyleGroupQueryDto dto);

	Boolean delSampleStyleGroupItem(String id);

	Boolean saveSampleStyleGroupItem(StyleGroupItemSaveDto dto);
	
}
