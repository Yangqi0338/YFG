package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.base.BaseController;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2023/6/27 10:45
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@Api(tags = "基础资料-规格/门幅")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/specification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SpecificationController extends BaseController {
}
