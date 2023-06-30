/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleSalePageDto;
import com.base.sbc.module.sample.dto.SampleSaleSaveDto;
import com.base.sbc.module.sample.service.SampleSaleService;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* 类描述：样衣销售 Controller类
* @address com.base.sbc.module.sample.web.SampleSaleController
*/
@RestController
@Api(tags = "样衣销售相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleSale", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleSaleController {

	@Autowired
	private SampleSaleService sampleSaleService;

	@ApiOperation(value = "保存")
	@PostMapping("/save")
	public SampleSaleVo save(@RequestBody SampleSaleSaveDto dto) {
		return sampleSaleService.save(dto);
	}

	@ApiOperation(value = "分页查询")
	@GetMapping("/getList")
	public PageInfo getList(@Valid SampleSalePageDto dto){
		return sampleSaleService.queryPageInfo(dto);
	}

	@ApiOperation(value = "详情")
	@GetMapping("/{id}")
	public SampleSaleVo getDetail(@PathVariable("id") String id) {
		return sampleSaleService.getDetail(id);
	}
}