/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.purchase.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.purchase.dto.PurchasePageDTO;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.mapper.PurchaseOrderMapper;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
* 类描述：采购-采购单 Controller类
* @address com.base.sbc.module.purchase.web.PurchaseOrderController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-4 9:43:16
* @version 1.0
*/
@RestController
@Api(tags = "采购-采购单")
@RequestMapping(value = BaseController.SAAS_URL + "/purchaseOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PurchaseOrderController extends BaseController{

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;

	@Autowired
	private PurchaseOrderMapper purchaseOrderMapper;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, PurchasePageDTO page) {
		QueryWrapper<PurchaseOrder> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getOrderStatus()),"order_status", page.getOrderStatus());
		qc.eq(StringUtils.isNotBlank(page.getStatus()),"status", page.getStatus());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("design_style_code", page.getSearch())
					.or()
					.like("plate_bill_code", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<PurchaseOrder> purchaseDemandPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			purchaseOrderService.list(qc);
			PageInfo<PurchaseOrder> pages = purchaseDemandPage.toPageInfo();
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ApiResult getById(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @PathVariable("id") String id) {
		if(StringUtils.isBlank(id)){
			return selectAttributeNotRequirements("id");
		}
		PurchaseOrder purchaseOrder = purchaseOrderService.getById(id);
		if(purchaseOrder != null){
			QueryWrapper<PurchaseOrderDetail> detailQw = new QueryWrapper<>();
			detailQw.eq("purchase_order_id", id);
			List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrderDetailService.list(detailQw);
			purchaseOrder.setPurchaseOrderDetailList(purchaseOrderDetailList);
			return selectSuccess(purchaseOrder);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping
	public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids) {
		if(StringUtils.isBlank(ids)){
			return deleteAttributeNotRequirements("ids");
		}
		List<String> idList = StringUtils.convertList(ids);
		boolean result = purchaseOrderService.removeByIds(idList);
		if(result) {
			return ApiResult.success("删除成功！");
		}
		return deleteNotFound();
	}


	@ApiOperation(value = "新增")
	@PostMapping
	public ApiResult add(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody PurchaseOrder purchaseOrder) {
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return purchaseOrderService.addPurchaseOrder(userInfo, userCompany, purchaseOrder);
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody PurchaseOrder purchaseOrder) {
		if(StringUtils.isBlank(purchaseOrder.getId())){
			return updateNotFound();
		}

		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return purchaseOrderService.updatePurchaseOrder(userInfo, userCompany, purchaseOrder);
	}

	@ApiOperation(value = "作废-通过id查询,多个逗号分开")
	@DeleteMapping("/cancel")
	public ApiResult cancel(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids) {
		if(StringUtils.isBlank(ids)){
			return updateAttributeNotRequirements("ids");
		}
		return purchaseOrderService.cancel(userCompany, ids);
	}

	@ApiOperation(value = "入库单选择采购单使用的接口")
	@GetMapping("/noticePage")
	public ApiResult noticePage(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, PurchasePageDTO page) {
		BaseQueryWrapper<PurchaseOrder> qc = new BaseQueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getOrderStatus()),"warehouse_status", page.getWarehouseStatus());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("code", page.getSearch())
					.or()
					.like("supplier_name", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<PurchaseOrder> purchaseDemandPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			purchaseOrderMapper.purchaseRelationoNotice(qc);
			PageInfo<PurchaseOrder> pages = purchaseDemandPage.toPageInfo();
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}
}































