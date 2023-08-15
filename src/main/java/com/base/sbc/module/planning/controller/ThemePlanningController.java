/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.controller;

import com.base.sbc.config.common.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：主题企划 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.web.ThemePlanningController
 * @email your email
 * @date 创建时间：2023-8-15 13:58:35
 */
@RestController
@Api(tags = "主题企划")
@RequestMapping(value = BaseController.SAAS_URL + "/themePlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ThemePlanningController {
}































