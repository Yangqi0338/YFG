/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.purchase.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.purchase.dto.PurchasePageDTO;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.entity.PurchaseRequest;
import com.base.sbc.module.purchase.entity.PurchaseRequestDetail;
import com.base.sbc.module.purchase.service.PurchaseRequestDetailService;
import com.base.sbc.module.purchase.service.PurchaseRequestService;
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
* 类描述：采购申请单 Controller类
* @address com.base.sbc.module.purchase.web.PurchaseRequestController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-9-18 15:59:21
* @version 1.0
*/
@RestController
@Api(tags = "采购申请单")
@RequestMapping(value = BaseController.SAAS_URL + "/purchaseRequest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PurchaseRequestController extends BaseController{

	@Autowired
	private PurchaseRequestService purchaseRequestService;

	@Autowired
	private PurchaseRequestDetailService purchaseRequestDetailService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@Autowired
	private FlowableService flowableService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, PurchasePageDTO page) {
		QueryWrapper<PurchaseRequest> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getOrderStatus()),"order_status", page.getOrderStatus());
		qc.eq(StringUtils.isNotBlank(page.getStatus()),"status", page.getStatus());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("code", page.getSearch())
					.or()
					.like("order_name", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<PurchaseRequest> purchaseDemandPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			purchaseRequestService.list(qc);
			PageInfo<PurchaseRequest> pages = purchaseDemandPage.toPageInfo();
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
		PurchaseRequest purchaseRequest = purchaseRequestService.getById(id);
		if(purchaseRequest != null){
			QueryWrapper<PurchaseRequestDetail> detailQw = new QueryWrapper<>();
			detailQw.eq("request_id", id);
			List<PurchaseRequestDetail> purchaseRequestDetailList = purchaseRequestDetailService.list(detailQw);
			purchaseRequest.setDetailList(purchaseRequestDetailList);
			return selectSuccess(purchaseRequest);
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
		boolean result = purchaseRequestService.removeByIds(idList);
		if(result) {
			return ApiResult.success("删除成功！");
		}
		return deleteNotFound();
	}

	@ApiOperation(value = "新增")
	@PostMapping
	public ApiResult add(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody PurchaseRequest purchaseRequest) {
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return purchaseRequestService.addPurchaseRequest(companyCode, userInfo, purchaseRequest);
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody PurchaseRequest purchaseRequest) {
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return purchaseRequestService.updatePurchaseRequest(companyCode, userInfo, purchaseRequest);
	}

	@ApiOperation(value = "作废-通过id查询,多个逗号分开")
	@DeleteMapping("/cancel")
	public ApiResult cancel(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids) {
		if(StringUtils.isBlank(ids)){
			return updateAttributeNotRequirements("ids");
		}
		return purchaseRequestService.cancel(userCompany, ids);
	}

	@ApiOperation(value = "提交")
	@GetMapping("/submit")
	public ApiResult submit(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("id") String id) {
		if(StringUtils.isBlank(id)){
			return selectAttributeNotRequirements("id");
		}
		PurchaseRequest purchaseRequest = purchaseRequestService.getById(id);
		if(purchaseRequest != null){
			if(!StringUtils.equals(purchaseRequest.getStatus(), "0") || !StringUtils.equals(purchaseRequest.getOrderStatus(), "0")){
				return ApiResult.error("请选择草稿状态的单据！", 500);
			}

			Boolean result = flowableService.start("单号:"+purchaseRequest.getCode(), flowableService.PURCHASE_REQUEST, purchaseRequest.getId(),
					"/pdm/api/saas/purchaseRequest/examine", "/pdm/api/saas/purchaseRequest/examine",
					"/pdm/api/saas/purchaseRequest/examine",null, BeanUtil.beanToMap(purchaseRequest));
			if(result){
				purchaseRequest.setStatus("1");
				purchaseRequestService.updateById(purchaseRequest);
				return ApiResult.success("提交成功！", result);
			}
		}
		return ApiResult.error("提交失败！", 500);
	}

	@ApiOperation(value = "审核")
	@PostMapping("/examine")
	public void examinePass(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody AnswerDto dto) {
		UserCompany userInfo = userCompanyUtils.getCompanyUser();
		if(StringUtils.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
			purchaseRequestService.examinePass(userInfo, dto);
		}else if(StringUtils.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)){
			purchaseRequestService.examineNoPass(userInfo, dto);
		}else{
			purchaseRequestService.cancelExamine(userInfo, dto);
		}
	}
}































