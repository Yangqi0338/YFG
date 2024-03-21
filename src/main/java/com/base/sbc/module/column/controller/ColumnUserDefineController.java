/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.column.dto.ColumnUserDefineDto;
import com.base.sbc.module.column.service.ColumnUserDefineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：用户级列自定义头 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.web.ColumnUserDefineController
 * @email your email
 * @date 创建时间：2023-12-6 17:33:00
 */
@RestController
@Api(tags = "用户级列自定义头")
@RequestMapping(value = BaseController.SAAS_URL + "/columnUserDefine", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ColumnUserDefineController {

    @Autowired
    private ColumnUserDefineService columnUserDefineService;

    @ApiOperation("查询列表")
    @GetMapping({"/{tableCode}"})
    public ApiResult list(@PathVariable("tableCode") String tableCode) {
        return ApiResult.success("查询成功", this.columnUserDefineService.findList(tableCode));
    }

    @ApiOperation("配置明细")
    @GetMapping({"/{tableCode}/{id}"})
    public ApiResult findDetail(@PathVariable("tableCode") String tableCode, @PathVariable("id") String id) {
        return ApiResult.success("查询成功", this.columnUserDefineService.findDetail(tableCode, id));
    }

    @ApiOperation("获取默认配置")
    @GetMapping({"/findDefaultDetail/{tableCode}"})
    public ApiResult findDefaultDetail(@PathVariable("tableCode") String tableCode) {
        return ApiResult.success("查询成功", this.columnUserDefineService.findDefaultDetail(tableCode));
    }

    @ApiOperation("获取默认配置-并按照分组处理")
    @GetMapping({"/findDefaultDetailGroup/{tableCode}"})
    public ApiResult findDefaultDetailGroup(@PathVariable("tableCode") String tableCode) {
        return ApiResult.success("查询成功", this.columnUserDefineService.findDefaultDetailGroup(tableCode));
    }

    @ApiOperation("删除-通过id查询")
    @DeleteMapping({"/{id}"})
    public ApiResult removeById(@PathVariable("id") String id) {
        this.columnUserDefineService.delete(id);
        return ApiResult.success();
    }

    @ApiOperation("保存")
    @PostMapping
    public ApiResult save(@RequestBody ColumnUserDefineDto dto) {
        this.columnUserDefineService.saveMain(dto);
        return ApiResult.success();
    }

    @ApiOperation("设置默认模板")
    @GetMapping({"setDefault/{tableCode}/{id}"})
    public ApiResult setDefault(@PathVariable("tableCode") String tableCode, @PathVariable("id") String id) {
        this.columnUserDefineService.setDefault(tableCode, id);
        return ApiResult.success();
    }

}































