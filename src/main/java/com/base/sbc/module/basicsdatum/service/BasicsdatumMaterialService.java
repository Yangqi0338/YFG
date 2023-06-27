/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthSaveDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：基础资料-物料档案 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:17
 * @version 1.0  
 */
public interface BasicsdatumMaterialService extends BaseService<BasicsdatumMaterial> {

	PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(BasicsdatumMaterialQueryDto dto);

	BasicsdatumMaterialVo saveBasicsdatumMaterial(BasicsdatumMaterialSaveDto dto);

	Boolean startStopBasicsdatumMaterial(StartStopDto dto);

	Boolean delBasicsdatumMaterial(String id);

	BasicsdatumMaterialVo getBasicsdatumMaterial(String id);

	PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(BasicsdatumMaterialWidthQueryDto dto);

	Boolean saveBasicsdatumMaterialWidth(BasicsdatumMaterialWidthSaveDto dto);

	Boolean startStopBasicsdatumMaterialWidth(StartStopDto dto);

	Boolean delBasicsdatumMaterialWidth(String id);

	PageInfo<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(BasicsdatumMaterialColorQueryDto dto);

	Boolean saveBasicsdatumMaterialColor(BasicsdatumMaterialColorSaveDto dto);

	Boolean delBasicsdatumMaterialColor(String id);

	Boolean startStopBasicsdatumMaterialColor(StartStopDto dto);

	PageInfo<BasicsdatumMaterialPricePageVo> getBasicsdatumMaterialPriceList(BasicsdatumMaterialPriceQueryDto dto);

	Boolean saveBasicsdatumMaterialPrice(BasicsdatumMaterialPriceSaveDto dto);

	Boolean startStopBasicsdatumMaterialPrice(StartStopDto dto);

	Boolean delBasicsdatumMaterialPrice(String id);
	
}

