/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.service.ColumnDefineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：系统级列自定义 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.web.ColumnDefineController
 * @email your email
 * @date 创建时间：2023-12-6 17:33:00
 */
@RestController
@Api(tags = "系统级列自定义")
@RequestMapping(value = BaseController.SAAS_URL + "/columnDefine", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ColumnDefineController {

    @Autowired
    private ColumnDefineService columnDefineService;

    @ApiOperation("明细-通过tableCode查询")
    @GetMapping({"/{tableCode}"})
    public ApiResult getById(@PathVariable("tableCode") String tableCode) {
        return ApiResult.success("查询成功", this.columnDefineService.getByTableCode(tableCode));
    }

    @ApiOperation("保存")
    @PostMapping
    public ApiResult save(@RequestBody List<ColumnDefine> list) {
        this.columnDefineService.saveMain(list);
        return ApiResult.success("保存成功");
    }

}































