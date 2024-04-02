package com.base.sbc.module.patternlibrary.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplateDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplatePageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 版型库-模板表
 *
 * @author XHTE
 * @create 2024-03-22
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "版型库-模板表")
@RequestMapping(value = BaseController.SAAS_URL + "/patternLibraryTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PatternLibraryTemplateController {

    @Autowired
    private PatternLibraryTemplateService patternLibraryTemplateService;

    @ApiOperation(value = "版型库-模板表列表")
    @PostMapping("/listPages")
    public ApiResult<PageInfo<PatternLibraryTemplate>> listPages(
            @RequestBody PatternLibraryTemplatePageDTO patternLibraryTemplatePageDTO) {
        PageInfo<PatternLibraryTemplate> patternLibraryTemplatePageInfo = patternLibraryTemplateService
                .listPages(patternLibraryTemplatePageDTO);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibraryTemplatePageInfo);
    }

    @ApiOperation(value = "版型库-模板表详情")
    @GetMapping("/getDetail")
    public ApiResult<PatternLibraryTemplate> getDetail(String patternLibraryTemplateId) {
        PatternLibraryTemplate patternLibraryTemplate = patternLibraryTemplateService.getDetail(patternLibraryTemplateId);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibraryTemplate);
    }

    @ApiOperation(value = "版型库-模板表新增/编辑")
    @PostMapping("/saveOrUpdateDetail")
    public ApiResult<String> saveOrUpdateDetail(@RequestBody PatternLibraryTemplate patternLibraryTemplate) {
        Boolean resultFlag = patternLibraryTemplateService.saveOrUpdateDetail(patternLibraryTemplate);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库-模板表删除")
    @PostMapping("/removeDetail")
    public ApiResult<String> removeDetail(String patternLibraryTemplateId) {
        Boolean resultFlag = patternLibraryTemplateService.removeDetail(patternLibraryTemplateId);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库-模板表启用/禁用")
    @PostMapping("/updateEnableFlag")
    public ApiResult<String> updateEnableFlag(@RequestBody PatternLibraryTemplateDTO patternLibraryTemplateDTO) {
        Boolean resultFlag = patternLibraryTemplateService.updateEnableFlag(patternLibraryTemplateDTO);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

}
