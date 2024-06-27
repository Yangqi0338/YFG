package com.base.sbc.module.storageSpace.controller;

import com.base.sbc.config.common.base.BaseController;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * @Create : 2024/6/27 10:28
 **/
@RestController
@Api(value = "与素材库相关的所有接口信息", tags = {"素材库接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/storageSpace", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StorageSpaceController {
}
