/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.purchase.controller;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchase.dto.MaterialStockDTO;
import com.base.sbc.module.purchase.dto.WarehouseingPageDTO;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.entity.MaterialStockLog;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.service.MaterialStockLogService;
import com.base.sbc.module.purchase.service.MaterialStockService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 类描述：物料库存 Controller类
* @address com.base.sbc.module.purchase.web.MaterialStockController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-9-13 15:44:13
* @version 1.0
*/
@RestController
@Api(tags = "物料库存")
@RequestMapping(value = BaseController.SAAS_URL + "/materialStock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class MaterialStockController extends BaseController{

	@Autowired
	private MaterialStockService materialStockService;

	@Autowired
	private MaterialStockLogService materialStockLogService;

	@ApiOperation(value = "物料库存分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, MaterialStockDTO page) {
		QueryWrapper<MaterialStock> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getWarehouseId()), "warehouse_id", page.getWarehouseId());
		qc.eq(StringUtils.isNotBlank(page.getDefaultSupplierId()), "default_supplier_id", page.getDefaultSupplierId());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("material_code", page.getSearch())
					.or()
					.like("material_sku", page.getSearch())
					.or()
					.like("material_name", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<MaterialStock> materialStockPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			materialStockService.list(qc);
			PageInfo<MaterialStock> pages = materialStockPage.toPageInfo();
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "根据物料SKU查询物料库存数据")
	@GetMapping("/byMaterialSKU")
	public ApiResult byMaterialSKU(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String SkuList) {
		if(StringUtils.isBlank(SkuList)){
			return selectAttributeNotRequirements("SKU");
		}

		QueryWrapper<MaterialStock> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.in("material_sku", StringUtils.convertList(SkuList));
		List<MaterialStock> materialStockList = materialStockService.list(qc);
		if(CollectionUtil.isNotEmpty(materialStockList)){
			return selectSuccess(materialStockList);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "物料库存日志分页查询")
	@GetMapping("/materialStockLogPage")
	public ApiResult materialStockLogPage(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, MaterialStockDTO page) {
		QueryWrapper<MaterialStockLog> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq("material_warehouse_id", page.getMaterialWarehouseId());
		qc.eq(StringUtils.isNotBlank(page.getType()), "type", page.getType());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("relation_code", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<MaterialStockLog> materialStockPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			materialStockLogService.list(qc);
			PageInfo<MaterialStockLog> pages = materialStockPage.toPageInfo();
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

}































