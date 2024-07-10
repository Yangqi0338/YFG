/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.controller;

import com.base.sbc.config.common.base.BaseController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务列表详情 Controller
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Api(tags = "任务列表详情")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/taskListDetail")
@RequiredArgsConstructor
public class TaskListDetailController {

}
