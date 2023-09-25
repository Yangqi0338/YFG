/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.purchase.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.purchase.dto.DemandPageDTO;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
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

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
					.like("plate_bill_code", page.getSearch())
					.or()
					.like("supplier_name", page.getSearch())
					.or()
					.like("material_code", page.getSearch())
					.or()
					.like("material_name", page.getSearch()));
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

	@ApiOperation(value = "明细-通过ids查询多条数据")
	@GetMapping("/byIdList")
	public ApiResult byIdList(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids) {
		if(StringUtils.isBlank(ids)){
			return selectAttributeNotRequirements("ids");
		}
		List<String> idList = StringUtils.convertList(ids);
		QueryWrapper<PurchaseDemand> qw = new QueryWrapper<>();
		qw.eq("company_code", userCompany);
		qw.in("id", idList);
		List<PurchaseDemand> list = purchaseDemandService.list(qw);
		if(CollectionUtil.isNotEmpty(list)){
			return selectSuccess(list);
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
	public ApiResult generatePurchaseDemand(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String id, String materialIds, String colors) {
		if(StringUtils.isBlank(id)){
			return selectAttributeNotRequirements("id");
		}
		return purchaseDemandService.generatePurchaseDemand(userCompany, id, materialIds, colors);
	}

//	@ApiOperation(value = "根据设计资料包 删除")
//	@GetMapping("/deletePurchaseDemand")
//	public ApiResult deletePurchaseDemand(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String id) {
//		if(StringUtils.isBlank(id)){
//			return selectAttributeNotRequirements("id");
//		}
//		purchaseDemandService.deleteRelationData(userCompany, id);
//		return selectSuccess("生成成功！");
//	}

	@ApiOperation(value = "采购需求生成采购单")
	@PostMapping("/generatePurchaseOrder")
	public ApiResult generatePurchaseOrder(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestBody List<PurchaseDemand> purchaseDemandList) {
		if(CollectionUtils.isEmpty(purchaseDemandList)){
			return updateAttributeNotRequirements("purchaseDemandList");
		}

		UserCompany userInfo = userCompanyUtils.getCompanyUser(user);
		return purchaseDemandService.generatePurchaseOrder(userInfo, userCompany, purchaseDemandList);
	}

	@ApiOperation(value = "出库单选择采购需求列表")
	@GetMapping("/outboundOrderPage")
	public ApiResult outboundOrderPage(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, DemandPageDTO page) {
		QueryWrapper<PurchaseDemand> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("design_style_code", page.getSearch())
					.or()
					.like("plate_bill_code", page.getSearch()));
		}
		qc.groupBy("design_style_code", "plate_bill_code", "style_bom", "version_no");
		qc.orderByAsc("need_date");

		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			com.github.pagehelper.Page<PurchaseDemand> purchaseDemandPage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
			purchaseDemandService.list(qc);
			PageInfo<PurchaseDemand> pages = purchaseDemandPage.toPageInfo();

			for(PurchaseDemand item : pages.getList()){
				QueryWrapper<PurchaseDemand> qw = new QueryWrapper<>();
				qw.eq("company_code", userCompany);
				qw.eq("design_style_code", item.getDesignStyleCode());
				qw.eq(StringUtils.isNotBlank(item.getPlateBillCode()), "plate_bill_code", item.getPlateBillCode());
				qw.eq("style_bom", item.getStyleBom());
				qw.eq("version_no", item.getVersionNo());
				List<PurchaseDemand> list = purchaseDemandService.list(qw);
				item.setDetailList(list);
			}
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "物料齐套分页列表查询")
	@GetMapping("/materialKittingPage")
	public ApiResult materialKittingPage(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, DemandPageDTO page) {
		QueryWrapper<PurchaseDemand> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		if(StringUtils.isNotBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("design_style_code", page.getSearch())
					.or()
					.like("style_name", page.getSearch()));
		}
		qc.groupBy("design_style_code", "plate_bill_code", "style_bom", "version_no");
		qc.orderByAsc("need_date");

		if (page.getPageNum() != 0 && page.getPageSize() != 0) {
			List<PurchaseDemand> purchaseDemandList = purchaseDemandService.list(qc);

			for(PurchaseDemand item : purchaseDemandList){
				QueryWrapper<PurchaseDemand> qw = new QueryWrapper<>();
				qw.eq("company_code", userCompany);
				qw.eq("design_style_code", item.getDesignStyleCode());
				qw.eq(StringUtils.isNotBlank(item.getPlateBillCode()), "plate_bill_code", item.getPlateBillCode());
				qw.eq("style_bom", item.getStyleBom());
				qw.eq("version_no", item.getVersionNo());
				List<PurchaseDemand> list = purchaseDemandService.list(qw);
				String isComplete = "齐料";
				BigDecimal completeNum = BigDecimal.ZERO;
				if(CollectionUtil.isNotEmpty(list)) {
					for (PurchaseDemand demand : list) {
						if (demand.getReadyNum().compareTo(demand.getNeedNum()) == -1) {
							isComplete = "未齐料";
						} else {
							completeNum = completeNum.add(BigDecimal.ONE);
						}
					}
				}else{
					isComplete = "未齐料";
				}

				BigDecimal proportion = BigDecimalUtil.equalZero(completeNum) ? BigDecimal.ZERO : completeNum.divide(new BigDecimal(list.size()), 2 , BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100.0));
				item.setProportion(proportion);
				item.setIsComplete(isComplete);
				item.setDetailList(list);
			}

			if(StringUtils.isNotBlank(page.getIsKitting())){
				purchaseDemandList = purchaseDemandList.stream().filter(demand -> StringUtils.equals(demand.getIsComplete(), page.getIsKitting())).collect(Collectors.toList());
			}

			Integer currentStrat = (page.getPageNum() -1 ) * page.getPageSize();
			Integer currentEnd = page.getPageNum() * page.getPageSize() > purchaseDemandList.size() ? purchaseDemandList.size() : page.getPageNum() * page.getPageSize();
			List pageList = purchaseDemandList.subList(currentStrat, currentEnd);
			PageInfo<PurchaseDemand> pages = new PageInfo<>(purchaseDemandList,1);
			pages.setList(pageList);
			pages.setPageSize(page.getPageSize());
			pages.setPageNum(page.getPageNum());
			return ApiResult.success("success", pages);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "根据 设计款号+制版号+需求日期 查询 采购需求单")
	@GetMapping("/outboundOrderUse")
	public ApiResult outboundOrderUse(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String designStyleCode, String plateBillCode, String needDate) {
		if(StringUtils.isBlank(designStyleCode) || StringUtils.isBlank(plateBillCode) || StringUtils.isBlank(needDate)){
			return selectAttributeNotRequirements("designStyleCode, plateBillCode, needDate");
		}

		QueryWrapper<PurchaseDemand> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq("design_style_code", designStyleCode);
		qc.eq("plate_bill_code", plateBillCode);
		qc.eq("date_format(need_date, '%Y-%m-%d')", needDate);
		List<PurchaseDemand> list = purchaseDemandService.list(qc);

		if (!CollectionUtils.isEmpty(list)) {
			return ApiResult.success("success", list);
		}
		return selectNotFound();
	}
}































