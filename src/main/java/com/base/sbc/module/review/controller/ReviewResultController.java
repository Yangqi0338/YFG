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
import com.base.sbc.module.review.entity.ReviewResult;
import com.base.sbc.module.review.entity.ReviewResultDetail;
import com.base.sbc.module.review.service.ReviewResultDetailService;
import com.base.sbc.module.review.service.ReviewResultService;
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
* 类描述：评审会-评审结果配置 Controller类
* @address com.base.sbc.module.review.web.ReviewResultController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-14 16:00:19
* @version 1.0
*/
@RestController
@Api(tags = "评审会-评审结果配置")
@RequestMapping(value = BaseController.SAAS_URL + "/reviewResult", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ReviewResultController extends BaseController{

	@Autowired
	private ReviewResultService reviewResultService;

	@Autowired
	private ReviewResultDetailService resultDetailService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value="分页查询数据", notes="")
	@GetMapping
	public ApiResult selectAll(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, Page page, String typeIds) {
		return reviewResultService.ReviewResultPage(companyCode, page, typeIds);
	}

	@ApiOperation(value = "新增评审结果配置")
	@PostMapping
	public ApiResult add(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewResult reviewResult) {
		return reviewResultService.addReviewResult(companyCode, userCompanyUtils.getCompanyUser(user), reviewResult);
	}

	@ApiOperation(value = "修改评审结果配置")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewResult reviewResult) {
		return reviewResultService.updateReviewResult(companyCode, userCompanyUtils.getCompanyUser(user), reviewResult);
	}

	@ApiOperation(value = "根据id查询明细", notes = "查询明细")
	@GetMapping("/{id}")
	public ApiResult getById(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @PathVariable("id") String id){
		ReviewResult reviewResult = reviewResultService.getById(id);
		if(reviewResult != null){
			QueryWrapper<ReviewResultDetail> qc = new QueryWrapper<>();
			qc.eq("result_id", id);
			List<ReviewResultDetail> resultDetailList = resultDetailService.list(qc);
			reviewResult.setReviewResultDetailList(resultDetailList);
			return selectSuccess(reviewResult);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "根据会议类型id查询审核结果配置明细", notes = "查询明细")
	@GetMapping("/getByTypeId")
	public ApiResult getByTypeId(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("typeId") String typeId){
		QueryWrapper<ReviewResult> qc = new QueryWrapper<>();
		qc.eq("company_code", userCompany);
		qc.eq("meeting_type", typeId);
		List<ReviewResult> reviewResultList = reviewResultService.list(qc);
		if(CollectionUtil.isNotEmpty(reviewResultList)){
			QueryWrapper<ReviewResultDetail> qw = new QueryWrapper<>();
			qw.eq("result_id", reviewResultList.get(0).getId());
			List<ReviewResultDetail> resultDetailList = resultDetailService.list(qw);
			return selectSuccess(resultDetailList);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "删除")
	@DeleteMapping
	public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @NotNull @RequestParam("ids") String ids) throws Exception {
		if(StringUtils.isBlank(ids)){
			return deleteAttributeNotRequirements("ids");
		}
		return reviewResultService.deleteOrder(companyCode, ids);
	}

}































