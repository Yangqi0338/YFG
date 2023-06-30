/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleWarehousePageDto;
import com.base.sbc.module.sample.service.SampleWarehouseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
* 类描述：样衣仓库 Controller类
* @address com.base.sbc.module.sample.web.SampleWarehouseController
*/
@RestController
@Api(tags = "样衣仓库相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleWarehouse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleWarehouseController {

	@Autowired
	private SampleWarehouseService sampleWarehouseService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getList")
	public PageInfo getList(@Valid SampleWarehousePageDto dto){
		return sampleWarehouseService.queryPageInfo(dto);
	}
}