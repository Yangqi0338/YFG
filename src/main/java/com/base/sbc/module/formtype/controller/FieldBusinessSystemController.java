/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.formtype.dto.FieldBusinessSystemQueryDto;
import com.base.sbc.module.formtype.entity.FieldBusinessSystem;
import com.base.sbc.module.formtype.service.FieldBusinessSystemService;
import com.base.sbc.module.formtype.vo.FieldBusinessSystemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类描述：字段对应下游系统关系 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.formtype.web.FieldBusinessSystemController
 * @email your email
 * @date 创建时间：2024-5-31 10:35:28
 */
@RestController
@Api(tags = "字段对应下游系统关系")
@RequestMapping(value = BaseController.SAAS_URL + "/fieldBusinessSystem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FieldBusinessSystemController {

    @Autowired
    private FieldBusinessSystemService fieldBusinessSystemService;

    @ApiOperation(value = "查询")
    @GetMapping
    public List<FieldBusinessSystemVo> list(FieldBusinessSystemQueryDto dto) {
        return fieldBusinessSystemService.findList(dto);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/del}")
    public ApiResult removeById(String[] ids) {
        fieldBusinessSystemService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return ApiResult.success("删除成功");
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public ApiResult save(@RequestBody List<FieldBusinessSystem> list) {
        fieldBusinessSystemService.saveBatch(list);
        return ApiResult.success("保存成功");
    }

}































