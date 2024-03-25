package com.base.sbc.module.patternlibrary.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.service.PatternLibraryItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 版型库-主表
 *
 * @author XHTE
 * @create 2024-03-22
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "版型库-主表")
@RequestMapping(value = BaseController.SAAS_URL + "/patternLibrary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PatternLibraryController {

    @Autowired
    private PatternLibraryService patternLibraryService;

    @ApiOperation(value = "版型库列表")
    @PostMapping("/listPages")
    public ApiResult<PageInfo<PatternLibraryVO>> listPages(@RequestBody PatternLibraryDTO patternLibraryDTO) {
        PageInfo<PatternLibraryVO> patternLibraryIPage = patternLibraryService.listPages(patternLibraryDTO);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibraryIPage);
    }

    @ApiOperation(value = "版型库详情")
    @GetMapping("/getDetail")
    public ApiResult<PatternLibraryVO> getDetail(String patternLibraryId) {
        PatternLibraryVO patternLibraryVO = patternLibraryService.getDetail(patternLibraryId);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibraryVO);
    }

    @ApiOperation(value = "版型库新增/编辑")
    @PostMapping("/saveOrUpdateDetail")
    public ApiResult<PatternLibraryVO> saveOrUpdateDetail(PatternLibrary patternLibrary) {
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, null);
    }

    @ApiOperation(value = "版型库删除")
    @PostMapping("/removeDetail")
    public ApiResult<String> removeDetail(@RequestBody String patternLibraryId) {
        Boolean resultFlag = patternLibraryService.removeDetail(patternLibraryId);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库批量审核")
    @PostMapping("/updateAudits")
    public ApiResult<String> updateAudits(List<String> patternLibraryIdList) {
        Boolean resultFlag = patternLibraryService.updateAudits(patternLibraryIdList);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }


}
