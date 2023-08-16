/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialOldQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialOldSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthGroupSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthsSaveDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.vo.*;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：基础资料-物料档案 service类
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:17
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

	Map<String, Object> getBasicsdatumMaterialPriceColorWidthSelect(String materialCode);

	void exportBasicsdatumMaterial(HttpServletResponse response, BasicsdatumMaterialQueryDto dto) throws IOException;

	PageInfo<BomSelMaterialVo> getBomSelMaterialList(BasicsdatumMaterialQueryDto dto);

	Boolean saveBasicsdatumMaterialWidthGroup(BasicsdatumMaterialWidthGroupSaveDto dto);

	Boolean saveBasicsdatumMaterialWidths(BasicsdatumMaterialWidthsSaveDto dto);

	PageInfo<BasicsdatumMaterialOldPageVo> getBasicsdatumMaterialOldList(BasicsdatumMaterialOldQueryDto dto);

	Boolean saveBasicsdatumMaterialOld(BasicsdatumMaterialOldSaveDto dto);

	Boolean delBasicsdatumMaterialOld(String id);

	PageInfo<WarehouseMaterialVo> getPurchaseMaterialList(BasicsdatumMaterialQueryDto dto);

	/**
	 * 修改物料询价编号、货期数据
	 * @param dto 物料档案主表保存实体
	 * @return 是否成功
	 */
	Boolean updateInquiryNumberDeliveryName(BasicsdatumMaterialSaveDto dto);
}

