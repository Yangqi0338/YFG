/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.review.controller;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.review.entity.ReviewDimension;
import com.base.sbc.module.review.service.ReviewDimensionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* 类描述：评审会-评审维度配置 Controller类
* @address com.base.sbc.module.review.web.ReviewDimensionController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-14 15:40:34
* @version 1.0
*/
@RestController
@Api(tags = "评审会-评审维度配置")
@RequestMapping(value = BaseController.SAAS_URL + "/reviewDimension", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ReviewDimensionController extends BaseController{

	@Autowired
	private ReviewDimensionService reviewDimensionService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value = "查询所有数据", notes = "查询所有")
	@GetMapping
	public ApiResult getAll(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany){
		List<ReviewDimension> reviewDimensionList = reviewDimensionService.list();
		if(reviewDimensionList != null){
			return selectSuccess(reviewDimensionList);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "新增评审维度配置")
	@PostMapping
	public ApiResult add(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewDimension reviewDimension) {
		return reviewDimensionService.addReviewDimension(companyCode, userCompanyUtils.getCompanyUser(user), reviewDimension);
	}

	@ApiOperation(value = "修改评审维度配置")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewDimension reviewDimension) {
		return reviewDimensionService.updateReviewDimension(userCompanyUtils.getCompanyUser(user), reviewDimension);
	}

	@ApiOperation(value = "根据id查询明细", notes = "查询明细")
	@GetMapping("/{id}")
	public ApiResult getById(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @PathVariable("id") String id){
		ReviewDimension reviewDimension = reviewDimensionService.getById(id);
		if(reviewDimension != null){
			return selectSuccess(reviewDimension);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "删除")
	@DeleteMapping
	public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestParam("ids") String ids) throws Exception {
		if(StringUtils.isBlank(ids)){
			return deleteAttributeNotRequirements("ids");
		}
		return reviewDimensionService.batchDelete(companyCode, ids);
	}

	@ApiOperation(value = "根据部门id查询评审维度配置", notes = "查询明细")
	@GetMapping("/getByDepartment")
	public ApiResult getByDepartment(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String ids){
		QueryWrapper<ReviewDimension> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.in("department_id", StringUtils.convertList(ids));
		List<ReviewDimension> dimensionList = reviewDimensionService.list(qc);
		if(CollectionUtil.isNotEmpty(dimensionList)){
			Set<String> resultSet = new HashSet<>();
			for(ReviewDimension reviewDimension : dimensionList){
				resultSet.add(reviewDimension.getReviewDimension());
			}
			return selectSuccess(resultSet.toArray());
		}
		return selectNotFound();
	}

}































