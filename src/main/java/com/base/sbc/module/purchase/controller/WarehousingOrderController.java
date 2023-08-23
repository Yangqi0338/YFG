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
import com.base.sbc.module.purchase.dto.WarehouseingPageDTO;
import com.base.sbc.module.purchase.entity.*;
import com.base.sbc.module.purchase.service.WarehousingOrderDetailService;
import com.base.sbc.module.purchase.service.WarehousingOrderService;
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
* 类描述：入库单 Controller类
* @address com.base.sbc.module.purchase.web.WarehousingOrderController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-9 16:21:43
* @version 1.0
*/
@RestController
@Api(tags = "入库单")
@RequestMapping(value = BaseController.SAAS_URL + "/warehousingOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class WarehousingOrderController extends BaseController{

	@Autowired
	private WarehousingOrderService warehousingOrderService;

	@Autowired
	private WarehousingOrderDetailService warehousingOrderDetailService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@Autowired
	private FlowableService flowableService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, WarehouseingPageDTO page) {
		QueryWrapper<WarehousingOrder> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getStatus()),"status", page.getStatus());
		qc.eq(StringUtils.isNotBlank(page.getOrderStatus()), "order_status", page.getOrderStatus());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("code", page.getSearch())
					.or()
					.like("source_code", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<WarehousingOrder> warehousingOrderPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			warehousingOrderService.list(qc);
			PageInfo<WarehousingOrder> pages = warehousingOrderPage.toPageInfo();
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
		WarehousingOrder warehousingOrder = warehousingOrderService.getById(id);
		if(warehousingOrder != null){
			QueryWrapper<WarehousingOrderDetail> detailQw = new QueryWrapper<>();
			detailQw.eq("warehouse_order_id", id);
			List<WarehousingOrderDetail> orderDetailList = warehousingOrderDetailService.list(detailQw);
			warehousingOrder.setOrderDetailList(orderDetailList);
			return selectSuccess(warehousingOrder);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return warehousingOrderService.removeByIds(ids);
	}

	@ApiOperation(value = "新增")
	@PostMapping
	public ApiResult save(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody WarehousingOrder warehousingOrder) {
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return warehousingOrderService.addWarehousing(userInfo, userCompany, warehousingOrder);
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody WarehousingOrder warehousingOrder) {
		if(StringUtils.isBlank(warehousingOrder.getId())){
			return updateNotFound();
		}

		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return warehousingOrderService.updateWarehousing(userInfo, userCompany, warehousingOrder);
	}

	@ApiOperation(value = "作废-通过id查询,多个逗号分开")
	@DeleteMapping("/cancel")
	public ApiResult cancel(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids) {
		if(StringUtils.isBlank(ids)){
			return updateAttributeNotRequirements("ids");
		}
		return warehousingOrderService.cancel(userCompany, ids);
	}

	@ApiOperation(value = "提交")
	@GetMapping("/submit")
	public ApiResult submit(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,@RequestParam("id") String id) {
		if(StringUtils.isBlank(id)){
			return selectAttributeNotRequirements("id");
		}
		WarehousingOrder warehousingOrder = warehousingOrderService.getById(id);
		if(warehousingOrder != null){
			if(!StringUtils.equals(warehousingOrder.getStatus(), "0") || !StringUtils.equals(warehousingOrder.getOrderStatus(), "0")){
				return ApiResult.error("请选择草稿状态的单据！", 500);
			}

			Boolean result = flowableService.start("单号:"+ warehousingOrder.getCode(), flowableService.WAREHOUSING_ORDER, warehousingOrder.getId(),
					"/pdm/api/saas/warehousingOrder/examine", "/pdm/api/saas/warehousingOrder/examine",
					"/pdm/api/saas/warehousingOrder/examine", null, BeanUtil.beanToMap(warehousingOrder));
			if(result){
				warehousingOrder.setStatus("1");
				warehousingOrderService.updateById(warehousingOrder);
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
			warehousingOrderService.examinePass(userInfo, dto);
		}else if(StringUtils.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)){
			warehousingOrderService.examineNoPass(userInfo, dto);
		}else{
			warehousingOrderService.cancelExamine(userInfo, dto);
		}
	}
}































