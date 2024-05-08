/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.vo.FabricStyleGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：款式管理-面料汇总 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryService
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-28 15:25:40
 * @version 1.0  
 */
public interface FabricSummaryService extends BaseService<FabricSummary>{
    PageInfo<String> fabricSummaryIdList(FabricSummaryV2Dto dto);

    PageInfo<FabricSummaryInfoVo> fabricSummaryInfoVoList(FabricSummaryV2Dto dto);

    boolean fabricSummaryGroupSaveOrUpdate(FabricStyleGroupVo fabricStyleGroupVo);

    boolean deleteFabricSummaryGroup(FabricStyleGroupVo fabricStyleGroupVo);

    PageInfo<FabricSummaryGroupVo> fabricSummaryGroup(FabricSummaryStyleMaterialDto dto);

    PageInfo<FabricSummaryInfoVo> selectFabricSummaryStyle(FabricSummaryV2Dto dto);

    Boolean saveFabricSummary(List<FabricSummaryInfoVo> dto);

    boolean fabricSummarySync(List<FabricSummaryV2Dto> dto);

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}
