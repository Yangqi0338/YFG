package com.base.sbc.module.material.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.material.service.MaterialDetailsService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 卞康
 * @date 2023/3/22 15:52:46
 */
@RestController
@Api(tags = "1.2 SAAS接口[标签]")
@RequestMapping(value = BaseController.SAAS_URL + "/materialDetails", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialDetailsController extends BaseController {
    @Resource
    private MaterialDetailsService materialDetailsService;
}
