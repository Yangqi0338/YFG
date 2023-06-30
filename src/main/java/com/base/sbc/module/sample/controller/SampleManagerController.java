/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.entity.SampleItemLog;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 类描述：样衣管理 Controller类
* @address com.base.sbc.module.sample.web.SampleManagerController
*/
@RestController
@Api(tags = "样衣管理相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleManager", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleManagerController {

	@Autowired
	private SampleService sampleService;
	@Autowired
	private SampleItemService sampleItemService;
	@Autowired
	private SampleItemLogService sampleItemLogService;

	@ApiOperation(value = "保存")
	@PostMapping("/save")
	public SampleVo save(@RequestBody SampleSaveDto dto) {
		return sampleService.save(dto);
	}

	@ApiOperation(value = "分页查询-设计款号维度")
	@GetMapping("/getListByDesignNo")
	public PageInfo getListByDesignNo(@Valid SamplePageDto dto){
		return sampleService.queryPageInfo(dto);
	}

	@ApiOperation(value = "分页查询-样衣明细维度")
	@GetMapping("/getListBySampleItem")
	public PageInfo getListBySampleItem(@Valid SamplePageDto dto){
		return sampleItemService.queryPageInfo(dto);
	}

	@ApiOperation(value = "详情")
	@GetMapping("/{id}")
	public SampleVo getDetail(@PathVariable("id") String id) {
		return sampleService.getDetail(id);
	}

	@ApiOperation(value = "根据样衣明细ID获取日志")
	@GetMapping("getLogList/{id}")
	public List<SampleItemLog> getLogListBySampleItemId(@PathVariable("id") String id) {
		return sampleItemLogService.getListBySampleItemId(id);
	}
}