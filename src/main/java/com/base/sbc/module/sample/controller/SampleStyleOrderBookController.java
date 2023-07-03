/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.service.SampleStyleOrderBookService;

import io.swagger.annotations.Api;

/**
 * 类描述：样衣款式订货本相关接口
 */
@RestController
@Api(tags = "样衣款式订货本相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/styleOrderBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleStyleOrderBookController {

	@Autowired
	private SampleStyleOrderBookService sampleStyleOrderBookService;

}