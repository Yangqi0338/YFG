/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.style.dto.QueryStyleColorCorrectDto;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.service.StyleColorCorrectInfoService;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：正确样管理 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.web.StyleColorCorrectInfoController
 * @email your email
 * @date 创建时间：2023-12-26 10:13:31
 */
@RestController
@Api(tags = "正确样管理")
@RequestMapping(value = BaseController.SAAS_URL + "/styleColorCorrectInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleColorCorrectInfoController {

    @Autowired
    private StyleColorCorrectInfoService styleColorCorrectInfoService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public PageInfo<StyleColorCorrectInfoVo> page(QueryStyleColorCorrectDto page) {
		return styleColorCorrectInfoService.findList(page);
    }

    @ApiOperation(value = "删除-通过id查询")
    @PostMapping("/delete")
    public ApiResult removeById(@RequestBody StyleColorCorrectInfo styleColorCorrectInfo) {
        styleColorCorrectInfoService.deleteMain(styleColorCorrectInfo);
        return ApiResult.success();
    }

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@RequestBody StyleColorCorrectInfo styleColorCorrectInfo) {
        String id = styleColorCorrectInfoService.saveMain(styleColorCorrectInfo);
        return ApiResult.success("成功",id);
    }


}































