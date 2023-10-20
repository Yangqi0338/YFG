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
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.review.dto.ReviewMeetingPageDTO;
import com.base.sbc.module.review.entity.ReviewDimension;
import com.base.sbc.module.review.entity.ReviewMeeting;
import com.base.sbc.module.review.entity.ReviewMeetingDepartment;
import com.base.sbc.module.review.entity.ReviewMeetingPeople;
import com.base.sbc.module.review.mapper.ReviewMeetingMapper;
import com.base.sbc.module.review.service.ReviewDimensionService;
import com.base.sbc.module.review.service.ReviewMeetingDepartmentService;
import com.base.sbc.module.review.service.ReviewMeetingPeopleService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	private ReviewMeetingDepartmentService departmentService;

	@Autowired
	private ReviewDimensionService dimensionService;

	@Autowired
	private ReviewMeetingPeopleService peopleService;

	@Autowired
	private UserCompanyUtils userCompanyUtils;

	@ApiOperation(value="分页查询数据", notes="")
	@GetMapping
	public ApiResult selectAll(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, ReviewMeetingPageDTO page, String typeIds) {
		com.github.pagehelper.Page<ReviewMeeting> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
		BaseQueryWrapper<ReviewMeeting> qc = new BaseQueryWrapper<>();
		qc.eq("m.company_code", companyCode);
		if(StringUtils.isNoneBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("m.code", page.getSearch())
					.or()
					.like("m.meeting_name", page.getSearch())
					.or()
					.like("m.style_no", page.getSearch())
					.or()
					.like("m.plate_bill_code", page.getSearch()));
		}
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
			for(ReviewMeeting reviewMeeting : pageList.getList()){
				QueryWrapper<ReviewMeetingDepartment> qw = new QueryWrapper<>();
				qw.eq("company_code", companyCode);
				qw.eq("meeting_id", reviewMeeting.getId());
				List<ReviewMeetingDepartment> departmentList = departmentService.list(qw);
				if(CollectionUtil.isNotEmpty(departmentList)){
					List<String> idList = departmentList.stream().map(ReviewMeetingDepartment::getDepartmentId).collect(Collectors.toList());
					QueryWrapper<ReviewDimension> dimensionQw = new QueryWrapper<>();
					dimensionQw.eq("company_code", companyCode);
					dimensionQw.in("department_id", idList);
					List<ReviewDimension> dimensionList = dimensionService.list(dimensionQw);
					if(CollectionUtil.isNotEmpty(dimensionList)){
						Set<String> resultSet = new HashSet<>();
						for(ReviewDimension reviewDimension : dimensionList){
							resultSet.add(reviewDimension.getReviewDimension());
						}

						reviewMeeting.setDimensionList(new ArrayList<>(resultSet));
					}
				}
			}
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}

	@ApiOperation(value="查询该用户参与的会议", notes="")
	@GetMapping("/selectNeedMeMeeting")
	public ApiResult selectNeedMeMeeting(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, ReviewMeetingPageDTO page, String typeIds, String type) {
		UserCompany userCompany = userCompanyUtils.getCompanyUser(user);

		com.github.pagehelper.Page<ReviewMeeting> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
		BaseQueryWrapper<ReviewMeeting> qc = new BaseQueryWrapper<>();
		qc.eq("m.company_code", companyCode);
		if(StringUtils.isNoneBlank(typeIds)) {
			qc.in("m.meeting_type", StringUtils.convertList(typeIds));
		}
		if (page.getStartDate() != null && page.getEndDate() != null) {
			qc.between("m.meeting_date", page.getStartDate(), page.getEndDate());
		}
		if(StringUtils.isNoneBlank(page.getSearch())){
			qc.and(wrapper -> wrapper.like("m.code", page.getSearch())
					.or()
					.like("m.meeting_name", page.getSearch())
					.or()
					.like("m.style_no", page.getSearch())
					.or()
					.like("m.plate_bill_code", page.getSearch()));
		}

		if(userCompany != null){
			qc.eq("d.staff_id", userCompany.getUserId());
		}

		if (!StringUtils.isEmpty(page.getOrderBy())){
			qc.orderByAsc(page.getOrderBy());
		}else {
			qc.orderByDesc("m.create_date");
		}
		reviewMeetingMapper.selectNeedMeMeeting(qc);
		PageInfo<ReviewMeeting> pageList = pages.toPageInfo();
		if (CollectionUtil.isNotEmpty(pageList.getList())) {
			if(StringUtils.equals(type, "1")) {
				//查询员工是否已经参加此次会议
				QueryWrapper<ReviewMeetingPeople> qw;
				for(ReviewMeeting reviewMeeting : pageList.getList()){
					qw = new QueryWrapper<>();
					qw.eq("company_code", companyCode);
					qw.eq("meeting_id", reviewMeeting.getId());
					qw.eq("create_id", userCompany.getUserId());
					List<ReviewMeetingPeople> peopleList = peopleService.list(qw);
					if(CollectionUtil.isNotEmpty(peopleList)){
						reviewMeeting.setParticipation("已上");
					}else{
						reviewMeeting.setParticipation("未上");
					}

					QueryWrapper<ReviewMeetingDepartment> dw = new QueryWrapper<>();
					dw.eq("company_code", companyCode);
					dw.eq("meeting_id", reviewMeeting.getId());
					List<ReviewMeetingDepartment> departmentList = departmentService.list(dw);
					if(CollectionUtil.isNotEmpty(departmentList)){
						List<String> idList = departmentList.stream().map(ReviewMeetingDepartment::getDepartmentId).collect(Collectors.toList());
						QueryWrapper<ReviewDimension> dimensionQw = new QueryWrapper<>();
						dimensionQw.eq("company_code", companyCode);
						dimensionQw.in("department_id", idList);
						List<ReviewDimension> dimensionList = dimensionService.list(dimensionQw);
						if(CollectionUtil.isNotEmpty(dimensionList)){
							Set<String> resultSet = new HashSet<>();
							for(ReviewDimension reviewDimension : dimensionList){
								resultSet.add(reviewDimension.getReviewDimension());
							}

							reviewMeeting.setDimensionList(new ArrayList<>(resultSet));
						}
					}
				}
			}
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

	@ApiOperation(value = "批量新增评审会")
	@PostMapping("/batchAdd")
	public ApiResult batchAdd(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeeting reviewMeeting) {
		return reviewMeetingService.batchAddReviewMeeting(companyCode, userCompanyUtils.getCompanyUser(user), reviewMeeting);
	}

	@ApiOperation(value = "修改评审会")
	@PostMapping("/update")
	public ApiResult update(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeeting reviewMeeting) {
		return reviewMeetingService.updateReviewMeeting(companyCode, userCompanyUtils.getCompanyUser(user), reviewMeeting, false);
	}

	@ApiOperation(value = "员工上传会议记录")
	@PostMapping("/staffUpdate")
	public ApiResult staffUpdate(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody ReviewMeeting reviewMeeting) {
		return reviewMeetingService.staffUpdate(companyCode, userCompanyUtils.getCompanyUser(user), reviewMeeting, false);
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































