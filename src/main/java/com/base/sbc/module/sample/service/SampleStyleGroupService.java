/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import java.util.List;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleStyleGroupQueryDto;
import com.base.sbc.module.sample.dto.SampleStyleGroupSaveDto;
import com.base.sbc.module.sample.entity.SampleStyleGroup;
import com.base.sbc.module.sample.vo.SampleStyleGroupPageVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：款式管理-款式搭配 service类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:23
 * @version 1.0
 */
public interface SampleStyleGroupService extends BaseService<SampleStyleGroup>{

	PageInfo<SampleStyleGroupPageVo> getStyleGroupList(SampleStyleGroupQueryDto dto);

	Boolean delSampleStyleGroup(String id);

	SampleStyleGroupPageVo saveSampleStyleGroup(SampleStyleGroupSaveDto dto);

	SampleStyleGroupPageVo getSampleStyleGroup(String id);

	List<SampleStyleGroupPageVo> getStyleGroupItemByGroupCode(String groupCode);

	Boolean delSampleStyleGroupItem(String id);
	
}
