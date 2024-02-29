/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryAgentDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibraryAgent;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryAgentService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourLibraryAgentVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 类描述：基础资料-颜色库 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.web.BasicsdatumColourLibraryAgentController
 * @email your email
 * @date 创建时间：2024-2-28 16:13:32
 */
@RestController
@Api(tags = "基础资料-颜色库")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumColourLibraryAgent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumColourLibraryAgentController {

    @Autowired
    private BasicsdatumColourLibraryAgentService basicsdatumColourLibraryAgentService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<BasicsdatumColourLibraryAgentVo> page(QueryBasicsdatumColourLibraryAgentDto dto) {
        return basicsdatumColourLibraryAgentService.findPage(dto);
    }

    @ApiOperation(value = "/导入")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        return basicsdatumColourLibraryAgentService.importExcel(file);
    }

    @ApiOperation(value = "/导出")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, QueryBasicsdatumColourLibraryAgentDto dto) throws IOException {
        basicsdatumColourLibraryAgentService.exportExcel(response, dto);
    }

    @ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
    @PostMapping("/startStopBasicsdatumColourLibrary")
    public ApiResult startStopBasicsdatumColourLibrary(@Valid @RequestBody StartStopDto startStopDto) {
        basicsdatumColourLibraryAgentService.statusUpdate(startStopDto);
        return ApiResult.success();
    }

    @ApiOperation(value = "保存")
    @PostMapping("/saveOrUpdate")
    public ApiResult saveOrUpdate(@RequestBody BasicsdatumColourLibraryAgent basicsdatumColourLibraryAgent) {
        basicsdatumColourLibraryAgentService.saveOrUpdateMian(basicsdatumColourLibraryAgent);
        return ApiResult.success();
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public ApiResult removeById(@PathVariable("id") String id) {
        basicsdatumColourLibraryAgentService.del(id);
        return ApiResult.success();
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public BasicsdatumColourLibraryAgent getById(@PathVariable("id") String id) {
        return basicsdatumColourLibraryAgentService.getById(id);
    }

}































