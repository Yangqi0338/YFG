/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.dto.SampleCirculateSaveDto;
import com.base.sbc.module.sample.service.SampleCirculateItemService;
import com.base.sbc.module.sample.service.SampleCirculateService;
import com.base.sbc.module.sample.vo.SampleCirculateVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* 类描述：样衣借还 Controller类
* @address com.base.sbc.module.sample.web.SampleCirculateController
*/
@RestController
@Api(tags = "样衣借还相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleCirculate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleCirculateController {

	@Autowired
	private SampleCirculateService sampleCirulateService;
	@Autowired
	private SampleCirculateItemService sampleCirulateItemService;

	@ApiOperation(value = "借样")
	@PostMapping("/borrow")
	public SampleCirculateVo borrow(@RequestBody SampleCirculateSaveDto dto) {
		return sampleCirulateService.borrow(dto);
	}

	@ApiOperation(value = "还样")
	@PostMapping("/return")
	public SampleCirculateVo return1(@RequestBody SampleCirculateSaveDto dto) { return sampleCirulateService.return1(dto); }

	@ApiOperation(value = "分页查询")
	@GetMapping("/getList")
	public PageInfo getList(@Valid SampleCirculatePageDto dto){
		return sampleCirulateItemService.queryPageInfo(dto);
	}
}