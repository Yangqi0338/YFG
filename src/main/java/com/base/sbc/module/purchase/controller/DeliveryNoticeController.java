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
import com.base.sbc.module.purchase.entity.DeliveryNotice;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.service.DeliveryNoticeService;
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
* 类描述：送货通知单 Controller类
* @address com.base.sbc.module.purchase.web.DeliveryNoticeController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-8 16:38:37
* @version 1.0
*/
@RestController
@Api(tags = "送货通知单")
@RequestMapping(value = BaseController.SAAS_URL + "/deliveryNotice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class DeliveryNoticeController extends BaseController{

	@Autowired
	private DeliveryNoticeService deliveryNoticeService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, Page page) {
		QueryWrapper<DeliveryNotice> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getStatus()),"status", page.getStatus());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("design_style_code", page.getSearch())
					.or()
					.like("plate_bill_code", page.getSearch())
					.or()
					.like("purchase_code", page.getSearch())
					.or()
					.like("material_code", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<DeliveryNotice> deliveryNoticePage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			deliveryNoticeService.list(qc);
			PageInfo<DeliveryNotice> pages = deliveryNoticePage.toPageInfo();
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public DeliveryNotice getById(@PathVariable("id") String id) {
		return deliveryNoticeService.getById(id);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return deliveryNoticeService.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public DeliveryNotice save(@RequestBody DeliveryNotice deliveryNotice) {
		deliveryNoticeService.save(deliveryNotice);
		return deliveryNotice;
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public DeliveryNotice update(@RequestBody DeliveryNotice deliveryNotice) {
		boolean b = deliveryNoticeService.updateById(deliveryNotice);
		if (!b) {
			//影响行数为0（数据未改变或者数据不存在）
			//返回影响行数需要配置jdbcURL参数useAffectedRows=true
		}
		return deliveryNotice;
	}

	@ApiOperation(value = "生成收货通知单")
	@GetMapping("/generateNotice")
	public ApiResult generateNotice(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, String ids) {
		if(StringUtils.isBlank(ids)){
			return insertAttributeNotRequirements("ids");
		}

		UserCompany userCompany = userCompanyUtils.getCompanyUser();
		return deliveryNoticeService.generateNotice(userCompany, companyCode, ids);
	}
}































