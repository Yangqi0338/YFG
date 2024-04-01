package com.base.sbc.module.patternlibrary.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.AuditsDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.base.sbc.module.style.entity.Style;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public ApiResult<PageInfo<PatternLibraryVO>> listPages(@RequestBody PatternLibraryPageDTO patternLibraryPageDTO) {
        PageInfo<PatternLibraryVO> patternLibraryIPage = patternLibraryService.listPages(patternLibraryPageDTO);
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
    public ApiResult<String> saveOrUpdateDetail(@RequestBody PatternLibraryDTO patternLibraryDTO) {
        Boolean resultFlag = patternLibraryService.saveOrUpdateDetails(patternLibraryDTO);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库批量编辑")
    @PostMapping("/updateDetails")
    public ApiResult<String> updateDetails(@RequestBody List<PatternLibraryDTO> patternLibraryDTOList) {
        Boolean resultFlag = patternLibraryService.updateDetails(patternLibraryDTOList);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
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

    /**
     * 审批回调
     */
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        PatternLibrary patternLibrary = patternLibraryService.getOne(new LambdaQueryWrapper<PatternLibrary>().eq(PatternLibrary::getId, dto.getBusinessKey()));
        if (BaseConstant.APPROVAL_PASS.equals(dto.getApprovalType())) {
            //审核通过
            patternLibrary.setStatus(PatternLibraryStatusEnum.REVIEWED.getCode());
        } else {
            //审核不通过
            patternLibrary.setStatus(PatternLibraryStatusEnum.REJECTED.getCode());
        }
        return patternLibraryService.updateById(patternLibrary);
    }

    @ApiOperation(value = "版型库批量审核通过")
    @PostMapping("/updateAuditsPass")
    public ApiResult<String> updateAuditsPass(@RequestBody AuditsDTO auditsDTO) {
        Boolean resultFlag = patternLibraryService.updateAuditsPass(auditsDTO);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库批量审核驳回")
    @PostMapping("/updateAuditsReject")
    public ApiResult<String> updateAuditsReject(@RequestBody AuditsDTO auditsDTO) {
        Boolean resultFlag = patternLibraryService.updateAuditsReject(auditsDTO);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "查询已开款的设计款号数据信息")
    @GetMapping("/listStyle")
    public ApiResult<List<Style>> listStyle(String search) {
        List<Style> styleList = patternLibraryService.listStyle(search);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, styleList);
    }

    @ApiOperation(value = "根据设计款号查询相关数据")
    @GetMapping("/getInfoByDesignNo")
    public ApiResult<PatternLibraryVO> getInfoByDesignNo(String styleNo) {
        PatternLibraryVO patternLibraryVO = patternLibraryService.getInfoByDesignNo(styleNo);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibraryVO);
    }

    @ApiOperation(value = "获取大类的类型")
    @GetMapping("/getCategoriesType")
    public ApiResult<CategoriesTypeVO> getCategoriesType() {
        CategoriesTypeVO categoriesType = patternLibraryService.getCategoriesType();
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, categoriesType);
    }


}
