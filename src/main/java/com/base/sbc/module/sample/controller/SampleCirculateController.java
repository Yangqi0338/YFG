/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleBorrowDto;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.dto.SampleReturnDTO;
import com.base.sbc.module.sample.service.SampleCirculateItemService;
import com.base.sbc.module.sample.service.SampleCirculateService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：样衣借还 Controller类
 *
 * @address com.base.sbc.module.sample.web.SampleCirculateController
 */
@RestController
@Api(tags = "样衣借还相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleCirculate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleCirculateController extends BaseController {

    @Autowired
    private SampleCirculateService sampleCirulateService;
    @Autowired
    private SampleCirculateItemService sampleCirulateItemService;

    @ApiOperation(value = "借样")
    @PostMapping("/borrow")
    public ApiResult borrow(@Valid @RequestBody SampleBorrowDto dto) {
        return updateSuccess(sampleCirulateService.borrow(dto));
    }

    @ApiOperation(value = "获取归还样衣明细")
    @PostMapping("/getSampleReturnDetailsVO")
    public ApiResult getSampleReturnDetailsVO(@Valid @NotNull(message = "样衣明细id不可为空") @RequestBody List<String> sampleItemIds) {
        return selectSuccess(sampleCirulateService.getSampleReturnDetailsVO(sampleItemIds));
    }

    @ApiOperation(value = "还样")
    @PostMapping("/sampleReturn")
    public ApiResult sampleReturn(@RequestBody SampleReturnDTO dto) {
        sampleCirulateService.sampleReturn(dto);
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/getList")
    public PageInfo getList(@Valid SampleCirculatePageDto dto) {
        return sampleCirulateItemService.queryPageInfo(dto);
    }
}