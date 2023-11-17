package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.common.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:58
 * @mail 247967116@qq.com
 * 企划看板坑位
 */
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/planningProjectPlank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class PlanningProjectPlankController extends BaseController {
}
