/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryGroup;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：款式管理-面料汇总-组管理 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryGroupService
 * @author your name
 * @email your email
 * @date 创建时间：2024-4-26 15:06:06
 * @version 1.0  
 */
public interface FabricSummaryGroupService extends BaseService<FabricSummaryGroup>{
    PageInfo<FabricSummaryGroupVo> getGroupList(FabricSummaryStyleMaterialDto dto);

    boolean deleteByIds(List<String> ids);

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}
