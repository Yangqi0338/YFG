/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleAllocatePageDto;
import com.base.sbc.module.sample.dto.SampleAllocateSaveDto;
import com.base.sbc.module.sample.service.SampleAllocateService;
import com.base.sbc.module.sample.vo.SampleAllocateVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* 类描述：样衣调拨相关接口 Controller类
* @address com.base.sbc.module.sample.web.SampleAllocateController
*/
@RestController
@Api(tags = "样衣调拨相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleAllocate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleAllocateController {

	@Autowired
	private SampleAllocateService sampleAllocateService;

	@ApiOperation(value = "保存")
	@PostMapping("/save")
	public ApiResult save(@RequestBody SampleAllocateSaveDto dto) {
		sampleAllocateService.save(dto);
		return ApiResult.success("ok");
	}

	@ApiOperation(value = "分页查询")
	@GetMapping("/getList")
	public PageInfo getList(@Valid SampleAllocatePageDto dto){
		return sampleAllocateService.queryPageInfo(dto);
	}

	@ApiOperation(value = "详情")
	@GetMapping("/{id}")
	public SampleAllocateVo getDetail(@PathVariable("id") String id) {
		return sampleAllocateService.getDetail(id);
	}
}