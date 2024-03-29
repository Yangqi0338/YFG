/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.esorderbook.service.EsOrderBookItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：ES订货本明细 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.web.EsOrderBookItemController
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@RestController
@Api(tags = "ES订货本明细")
@RequestMapping(value = BaseController.SAAS_URL + "/esOrderBookItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class EsOrderBookItemController {

    @Autowired
    private EsOrderBookItemService esOrderBookItemService;


}