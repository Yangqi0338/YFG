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
import com.base.sbc.module.purchase.entity.MaterialWarehouse;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.service.MaterialWarehouseService;
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
* 类描述：物料仓库 Controller类
* @address com.base.sbc.module.purchase.web.MaterialWarehouseController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-4 14:46:11
* @version 1.0
*/
@RestController
@Api(tags = "物料仓库")
@RequestMapping(value = BaseController.SAAS_URL + "/materialWarehouse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class MaterialWarehouseController extends BaseController{

	@Autowired
	private MaterialWarehouseService materialWarehouseService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public ApiResult page(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, Page page) {
		QueryWrapper<MaterialWarehouse> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq(StringUtils.isNotBlank(page.getStatus()),"status", page.getStatus());
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("warehouse_name", page.getSearch())
					.or()
					.like("contacts", page.getSearch())
					.or()
					.like("phone", page.getSearch()));
		}
		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("create_date");
		}
		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<MaterialWarehouse> purchaseDemandPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			materialWarehouseService.list(qc);
			PageInfo<MaterialWarehouse> pages = purchaseDemandPage.toPageInfo();
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
		MaterialWarehouse warehouse = materialWarehouseService.getById(id);
		if(warehouse != null){
			return selectSuccess(warehouse);
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
		QueryWrapper<MaterialWarehouse> qw = new QueryWrapper<>();
		qw.eq("company_code", userCompany);
		qw.in("id", idList);
		List<MaterialWarehouse> materialWarehouseList = materialWarehouseService.list(qw);
		for(MaterialWarehouse materialWarehouse : materialWarehouseList){
			if("1".equals(materialWarehouse.getStatus())){
				return ApiResult.error("请选择禁用状态的数据！", 500);
			}
		}

		boolean result = materialWarehouseService.removeByIds(idList);
		if(result) {
			return ApiResult.success("删除成功！");
		}
		return deleteNotFound();
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public ApiResult save(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody MaterialWarehouse materialWarehouse) {
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return materialWarehouseService.addWarehouse(userInfo, userCompany, materialWarehouse);
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody MaterialWarehouse materialWarehouse) {
		if(StringUtils.isBlank(materialWarehouse.getId())){
			return updateNotFound();
		}
		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return materialWarehouseService.updateWarehouse(userInfo, userCompany, materialWarehouse);
	}

}































