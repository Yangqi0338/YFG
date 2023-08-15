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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.review.dto.ReviewMeetingPageDTO;
import com.base.sbc.module.review.entity.ReviewMeeting;
import com.base.sbc.module.review.mapper.ReviewMeetingMapper;
import com.base.sbc.module.review.service.ReviewMeetingService;
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
* 类描述：评审会 Controller类
* @address com.base.sbc.module.review.web.ReviewMeetingController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-8-14 17:06:30
* @version 1.0
*/
@RestController
@Api(tags = "评审会")
@RequestMapping(value = BaseController.SAAS_URL + "/reviewMeeting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ReviewMeetingController extends BaseController{
	@Autowired
	private ReviewMeetingService reviewMeetingService;

	@Autowired
	private ReviewMeetingMapper reviewMeetingMapper;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value="分页查询数据", notes="")
	@GetMapping
	public ApiResult selectAll(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, ReviewMeetingPageDTO page, String typeIds) {
		com.github.pagehelper.Page<ReviewMeeting> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
		BaseQueryWrapper<ReviewMeeting> qc = new BaseQueryWrapper<>();
		qc.eq("m.company_code", companyCode);
		if(StringUtils.isNoneBlank(typeIds)) {
			qc.in("m.meeting_type", StringUtils.convertList(typeIds));
		}
		if (page.getStartDate() != null && page.getEndDate() != null) {
			qc.between("m.meeting_date", page.getStartDate(), page.getEndDate());
		}

		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("m.create_date");
		}
		reviewMeetingMapper.selectMeetingRelation(qc);
		PageInfo<ReviewMeeting> pageList = pages.toPageInfo();
		if (CollectionUtil.isNotEmpty(pageList.getList())) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}

	@ApiOperation(value = "根据id查询明细", notes = "查询明细")
	@GetMapping("/{id}")
	public ApiResult getById(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @PathVariable("id") String id){
		return reviewMeetingService.reviewMeetingHandleData(userCompany, id);
	}

	@ApiOperation(value = "新增评审会")
	@PostMapping
	public ApiResult add(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeeting reviewMeeting) {
		return reviewMeetingService.addReviewMeeting(companyCode, userCompanyUtils.getCompanyUser(user), reviewMeeting, true);
	}

	@ApiOperation(value = "修改评审会")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeeting reviewMeeting) {
		return reviewMeetingService.updateReviewMeeting(companyCode, userCompanyUtils.getCompanyUser(user), reviewMeeting, false);
	}

	@ApiOperation(value = "删除")
	@DeleteMapping
	public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestParam("ids") String ids) throws Exception {
		if(StringUtils.isBlank(ids)){
			return deleteAttributeNotRequirements("ids");
		}
		return reviewMeetingService.deleteOrder(companyCode, ids);
	}
}































