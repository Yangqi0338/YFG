/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.purchase.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.vo.BandQueryReturnVo;
import com.base.sbc.module.purchase.dto.DemandPageDTO;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
* 类描述：采购-采购需求表 Controller类
* @address com.base.sbc.module.purchase.web.PurchaseDemandController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-2 14:29:52
* @version 1.0
*/
@RestController
@Api(tags = "采购-采购需求表")
@RequestMapping(value = BaseController.SAAS_URL + "/purchaseDemand", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PurchaseDemandController extends BaseController{

	@Autowired
	private PurchaseDemandService purchaseDemandService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, DemandPageDTO page) {
		QueryWrapper<PurchaseDemand> qc = new QueryWrapper<>();
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
			com.github.pagehelper.Page<PurchaseDemand> purchaseDemandPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			purchaseDemandService.list(qc);
			PageInfo<PurchaseDemand> pages = purchaseDemandPage.toPageInfo();
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
		PurchaseDemand purchaseDemand = purchaseDemandService.getById(id);
		if(purchaseDemand != null){
			return selectSuccess(purchaseDemand);
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
		boolean result = purchaseDemandService.removeByIds(idList);
		if(result) {
			return ApiResult.success("删除成功！");
		}
		return deleteNotFound();
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public ApiResult update(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody PurchaseDemand purchaseDemand) {
		if(StringUtils.isBlank(purchaseDemand.getId())){
			return updateNotFound();
		}
		boolean b = purchaseDemandService.updateById(purchaseDemand);
		if (b) {
			return updateSuccess(purchaseDemand);
		}
		return updateNotFound();
	}

	@ApiOperation(value = "作废-通过id查询,多个逗号分开")
	@DeleteMapping("/cancel")
	public ApiResult cancel(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids) {
		if(StringUtils.isBlank(ids)){
			return updateAttributeNotRequirements("ids");
		}
		return purchaseDemandService.cancel(userCompany, ids);
	}

	@ApiOperation(value = "批量修改")
	@PostMapping("/batchUpdate")
	public ApiResult batchUpdate(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody List<PurchaseDemand> purchaseDemandList) {
		if(CollectionUtils.isEmpty(purchaseDemandList)){
			return updateAttributeNotRequirements("purchaseDemandList");
		}

		boolean result = purchaseDemandService.updateBatchById(purchaseDemandList);
		if (result) {
			return updateSuccess(purchaseDemandList);
		}
		return updateNotFound();
	}

	@ApiOperation(value = "根据设计资料包 生成采购需求单")
	@GetMapping("/generatePurchaseDemand")
	public ApiResult generatePurchaseDemand(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String id) {
		if(StringUtils.isBlank(id)){
			return selectAttributeNotRequirements("id");
		}
		UserCompany userInfo = userCompanyUtils.getCompanyUser();
		purchaseDemandService.generatePurchaseDemand(userInfo, userCompany, id);
		return selectSuccess("生成成功！");
	}

	@ApiOperation(value = "采购需求生成采购单")
	@PostMapping("/generatePurchaseOrder")
	public ApiResult generatePurchaseOrder(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody List<PurchaseDemand> purchaseDemandList) {
		if(CollectionUtils.isEmpty(purchaseDemandList)){
			return updateAttributeNotRequirements("purchaseDemandList");
		}
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return purchaseDemandService.generatePurchaseOrder(userInfo, userCompany, purchaseDemandList);
	}
}































