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
import com.base.sbc.module.review.entity.ReviewMeetingType;
import com.base.sbc.module.review.service.ReviewMeetingTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

/**
* 类描述：评审会-会议类型表 Controller类
* @address com.base.sbc.module.review.web.ReviewMeetingTypeController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-14 10:11:24
* @version 1.0
*/
@RestController
@Api(tags = "评审会-会议类型表")
@RequestMapping(value = BaseController.SAAS_URL + "/reviewMeetingType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ReviewMeetingTypeController extends BaseController{

	@Autowired
	private ReviewMeetingTypeService reviewMeetingTypeService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value = "查询所有数据", notes = "查询所有")
	@GetMapping
	public ApiResult getAll(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String status){
		QueryWrapper<ReviewMeetingType> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		if(StringUtils.isNotBlank(status)) {
			qc.eq("status", status);
		}
		List<ReviewMeetingType> reviewMeetingTypeList = reviewMeetingTypeService.list(qc);
		if(CollectionUtil.isNotEmpty(reviewMeetingTypeList)){
			return selectSuccess(reviewMeetingTypeList);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "查询所有一级菜单数据", notes = "查询所有")
	@GetMapping("/topLevel")
	public ApiResult getOneAll(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany){
		QueryWrapper<ReviewMeetingType> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq("is_leaf", "0");
		List<ReviewMeetingType> reviewMeetingTypeList = reviewMeetingTypeService.list(qc);
		if(CollectionUtil.isNotEmpty(reviewMeetingTypeList)){
			return selectSuccess(reviewMeetingTypeList);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "新增会议类型")
	@PostMapping
	public ApiResult add(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeetingType reviewMeetingType) {
		return reviewMeetingTypeService.addReviewMeetingType(companyCode, userCompanyUtils.getCompanyUser(user), reviewMeetingType);
	}

	@ApiOperation(value = "修改会议类型")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeetingType reviewMeetingType) {
		return reviewMeetingTypeService.updateReviewMeetingType(userCompanyUtils.getCompanyUser(user), reviewMeetingType);
	}

	@ApiOperation(value = "根据id查询明细", notes = "查询明细")
	@GetMapping("/{id}")
	public ApiResult getById(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @PathVariable("id") String id){
		ReviewMeetingType reviewMeetingType = reviewMeetingTypeService.getById(id);
		if(reviewMeetingType != null){
			return selectSuccess(reviewMeetingType);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "删除")
	@DeleteMapping
	public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @NotNull @RequestParam("ids") String ids) throws Exception {
		if(StringUtils.isBlank(ids)){
			return deleteAttributeNotRequirements("ids");
		}
		return reviewMeetingTypeService.batchDelete(companyCode, ids);
	}

}































