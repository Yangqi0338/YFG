/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackSampleReviewDto;
import com.base.sbc.module.pack.service.PackSampleReviewService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSampleReviewVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 类描述：资料包-样衣评审 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackSampleReviewController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 11:09:05
 */
@RestController
@Api(tags = "资料包-样衣评审")
@RequestMapping(value = BaseController.SAAS_URL + "/packSampleReview", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackSampleReviewController {

	@Autowired
	private PackSampleReviewService packSampleReviewService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<PackSampleReviewVo> page(@Valid PackCommonPageSearchDto dto) {
		return packSampleReviewService.pageInfo(dto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/detail")
	public PackSampleReviewVo getById(@Valid IdDto idDto) {
		return packSampleReviewService.getDetail(idDto.getId());
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping()
	public Boolean removeById(@Valid IdsDto ids) {
		return packSampleReviewService.removeByIds(StringUtils.convertList(ids.getId()));
	}

	@ApiOperation(value = "保存/修改", notes = "id为空新增、不为空修改")
	@PostMapping
	@OperaLog(value = "样衣评审", operationType = OperationType.INSERT_UPDATE, pathSpEL = PackUtils.pathSqEL, parentIdSpEl = "#p0.foreignId", service = PackSampleReviewService.class)
	public PackSampleReviewVo save(@RequestBody PackSampleReviewDto dto) {
		return packSampleReviewService.saveByDto(dto);
	}

}































