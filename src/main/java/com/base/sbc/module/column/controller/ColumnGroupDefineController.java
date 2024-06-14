/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.column.dto.ColumnGroupDefineDto;
import com.base.sbc.module.column.service.ColumnGroupDefineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述： Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.web.ColumnGroupDefineController
 * @email your email
 * @date 创建时间：2023-12-11 10:55:06
 */
@RestController
@Api(tags = "")
@RequestMapping(value = BaseController.SAAS_URL + "/columnGroupDefine", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ColumnGroupDefineController {

    @Autowired
    private ColumnGroupDefineService columnGroupDefineService;

    @ApiOperation("明细-通过tableCode查询")
    @GetMapping({"/{tableCode}/{userGroupId}"})
    public ApiResult getById(@PathVariable("tableCode") String tableCode, @PathVariable("userGroupId") String userGroupId) {
        return ApiResult.success("查询成功", this.columnGroupDefineService.findDetail(tableCode,"", userGroupId));
    }

    @ApiOperation("保存")
    @PostMapping("/saveMain")
    public ApiResult save(@RequestBody ColumnGroupDefineDto dto) {
        this.columnGroupDefineService.saveMain(dto);
        return ApiResult.success("保存成功");
    }

}































