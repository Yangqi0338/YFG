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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchase.dto.MaterialStockDTO;
import com.base.sbc.module.purchase.dto.WarehouseingPageDTO;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.entity.MaterialStockLog;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.excel.MaterialStockExcel;
import com.base.sbc.module.purchase.excel.MaterialStockLogExcel;
import com.base.sbc.module.purchase.excel.PurchaseOrderExcel;
import com.base.sbc.module.purchase.mapper.MaterialStockLogMapper;
import com.base.sbc.module.purchase.service.MaterialStockLogService;
import com.base.sbc.module.purchase.service.MaterialStockService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
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
	@Lazy
	private MaterialStockService materialStockService;

	@Autowired
	private MaterialStockLogService materialStockLogService;

	@Autowired
	private MaterialStockLogMapper materialStockLogMapper;

	@ApiOperation(value = "物料库存分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, MaterialStockDTO page) {
		QueryWrapper<MaterialStock> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getWarehouseId()), "warehouse_id", page.getWarehouseId());
		qc.eq(StringUtils.isNotBlank(page.getDefaultSupplierId()), "default_supplier_id", page.getDefaultSupplierId());
		if(StringUtils.isNotBlank(page.getMaterialColor())) {
			qc.and(wrapper -> wrapper.like("material_color", page.getMaterialColor())
					.or().
					like("material_color_code", page.getMaterialColor()));
		}
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
	public ApiResult byMaterialSKU(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String SkuList, String warehouseId) {
		if(StringUtils.isBlank(SkuList) || StringUtils.isBlank(warehouseId)){
			return selectAttributeNotRequirements("SKU, warehouseId");
		}

		QueryWrapper<MaterialStock> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq("warehouse_id", warehouseId);
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
		BaseQueryWrapper<MaterialStockLog> qc = new BaseQueryWrapper<>();
		qc.eq("l.company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getMaterialWarehouseId()),"l.material_warehouse_id", page.getMaterialWarehouseId());
		qc.eq(StringUtils.isNotBlank(page.getType()), "l.type", page.getType());
		qc.eq(StringUtils.isNotBlank(page.getWarehouseId()), "l.warehouse_id", page.getWarehouseId());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("l.relation_code", page.getSearch())
					.or()
					.like("l.material_code", page.getSearch())
					.or()
					.like("s.material_name", page.getSearch()));
		}
		if(StringUtils.isNotBlank(page.getMaterialColor())) {
			qc.and(wrapper -> wrapper.like("s.material_color", page.getMaterialColor())
					.or().
					like("s.material_color_code", page.getMaterialColor()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<MaterialStockLog> materialStockPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			materialStockLogMapper.selectRelationStock(qc);
			PageInfo<MaterialStockLog> pages = materialStockPage.toPageInfo();
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "导出库存日志")
	@GetMapping(value = "exportMaterialStockLogExcel")
	public void exportMaterialStockLogExcel(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,
										 HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {
		BaseQueryWrapper<MaterialStockLog> qw = new BaseQueryWrapper<>();
		qw.in("l.id", StringUtils.convertList(ids));
		List<MaterialStockLog> materialStockLogList = materialStockLogMapper.selectRelationStock(qw);

		// 生成文件名称
		String time = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");// 格式化当前系统日期
		String strFileName = "库存日志_" + time + ".xlsx";
		try (OutputStream objStream = response.getOutputStream()) {
			response.reset();
			// 设置文件类型
			response.setContentType("application/x-msdownload");// 下载
			request.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(strFileName, "UTF-8"));

			MaterialStockLogExcel excel = new MaterialStockLogExcel();
			XSSFWorkbook objWb = excel.createWorkBook(materialStockLogList);
			objWb.write(objStream);
			objStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "导出库存明细")
	@GetMapping(value = "exportMaterialStockExcel")
	public void exportMaterialStockExcel(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,
											HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {
		BaseQueryWrapper<MaterialStock> qw = new BaseQueryWrapper<>();
		qw.in("id", StringUtils.convertList(ids));
		List<MaterialStock> materialStockList = materialStockService.list(qw);

		// 生成文件名称
		String time = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");// 格式化当前系统日期
		String strFileName = "库存明细_" + time + ".xlsx";
		try (OutputStream objStream = response.getOutputStream()) {
			response.reset();
			// 设置文件类型
			response.setContentType("application/x-msdownload");// 下载
			request.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(strFileName, "UTF-8"));

			MaterialStockExcel excel = new MaterialStockExcel();
			XSSFWorkbook objWb = excel.createWorkBook(materialStockList);
			objWb.write(objStream);
			objStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}































