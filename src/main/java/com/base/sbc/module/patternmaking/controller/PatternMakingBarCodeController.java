/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.patternmaking.dto.PatternMakingBarCodeQueryDto;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import com.base.sbc.module.patternmaking.service.PatternMakingBarCodeService;
import com.base.sbc.module.patternmaking.vo.PatternMakingBarCodeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述： Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.PatternMakingBarCodeController
 * @email your email
 * @date 创建时间：2024-7-3 15:56:26
 */
@RestController
@Api(tags = "")
@RequestMapping(value = BaseController.SAAS_URL + "/patternMakingBarCode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequiredArgsConstructor
public class PatternMakingBarCodeController {

    @Autowired
    private PatternMakingBarCodeService patternMakingBarCodeService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public ApiResult<PageInfo<PatternMakingBarCodeVo>> page(PatternMakingBarCodeQueryDto dto) {
        return ApiResult.success("查询成功", patternMakingBarCodeService.findPage(dto));
    }

    @ApiOperation(value = "明细-通过barCode查询")
    @GetMapping("/get")
    public ApiResult getById(@RequestParam("barCode") String barCode) {
        PatternMakingBarCodeVo byBarCode = patternMakingBarCodeService.getByBarCode(barCode);
        return ApiResult.success("查询成功", byBarCode);
    }

    @ApiOperation(value = "解绑-通过barCode查询")
    @DeleteMapping("/remove")
    public ApiResult removeById(@RequestParam("barCode") String barCode) {
        patternMakingBarCodeService.removeByBarCode(barCode);
        return ApiResult.success("解绑成功");
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public ApiResult save(@RequestBody PatternMakingBarCode patternMakingBarCode) {
        patternMakingBarCodeService.saveMain(patternMakingBarCode);
        return ApiResult.success("保存成功");
    }

    @ApiOperation(value = "明细-通过barCode查询")
    @GetMapping("/check")
    public ApiResult<List<PatternMakingBarCode>> check(@RequestParam("headId") String headId, @RequestParam("pitSite") Integer pitSite) {
        List<PatternMakingBarCode> byBarCode = patternMakingBarCodeService.listByCode(headId, pitSite);
        return ApiResult.success("查询成功", byBarCode);
    }

    @ApiOperation(value = "状态")
    @PostMapping("/status")
    public ApiResult status(@RequestBody PatternMakingBarCode patternMakingBarCode) {
        patternMakingBarCodeService.updateById(patternMakingBarCode);
        return ApiResult.success("保存成功");
    }

}