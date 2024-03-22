package com.base.sbc.module.patternlibrary.controller;

import com.base.sbc.config.common.base.BaseController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版型库-主表
 *
 * @author XHTE
 * @create 2024-03-22
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "版型库-主表")
@RequestMapping(value = BaseController.SAAS_URL + "/patternLibrary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PatternLibraryController {

}
