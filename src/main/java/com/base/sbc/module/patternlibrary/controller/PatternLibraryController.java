package com.base.sbc.module.patternlibrary.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.AuditsDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.dto.UseStyleDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.EverGreenVO;
import com.base.sbc.module.patternlibrary.vo.FilterCriteriaVO;
import com.base.sbc.module.patternlibrary.vo.UseStyleVO;
import com.base.sbc.module.style.entity.Style;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ApiResult<PageInfo<PatternLibrary>> listPages(@RequestBody PatternLibraryPageDTO patternLibraryPageDTO) {
        PageInfo<PatternLibrary> patternLibraryIPage = patternLibraryService.listPages(patternLibraryPageDTO);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibraryIPage);
    }

    @ApiOperation(value = "版型库详情")
    @GetMapping("/getDetail")
    public ApiResult<PatternLibrary> getDetail(String patternLibraryId) {
        PatternLibrary patternLibrary = patternLibraryService.getDetail(patternLibraryId);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibrary);
    }

    @ApiOperation(value = "使用款记录")
    @GetMapping("/listUseStyle")
    public ApiResult<PageInfo<UseStyleVO>> listUseStyle(UseStyleDTO useStyleDTO) {
        PageHelper.startPage(useStyleDTO.getPageNum(), useStyleDTO.getPageSize());
        List<UseStyleVO> useStyleVOList = patternLibraryService.listUseStyle(useStyleDTO);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, new PageInfo<>(useStyleVOList));
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

    @ApiOperation(value = "版型库子表数据新增/编辑")
    @PostMapping("/saveOrUpdateItemDetail")
    public ApiResult<String> saveOrUpdateItemDetail(@RequestBody PatternLibraryDTO patternLibraryDTO) {
        Boolean resultFlag = patternLibraryService.saveOrUpdateItemDetail(patternLibraryDTO);
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
    public ApiResult<String> removeDetail(String patternLibraryId) {
        Boolean resultFlag = patternLibraryService.removeDetail(patternLibraryId);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库批量审核")
    @PostMapping("/updateAudits")
    public ApiResult<String> updateAudits(@RequestBody AuditsDTO auditsDTO) {
        Boolean resultFlag = patternLibraryService.updateAudits(auditsDTO);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库启用/禁用")
    @PostMapping("/updateEnableFlag")
    public ApiResult<String> updateEnableFlag(@RequestBody PatternLibraryDTO patternLibraryDTO) {
        Boolean resultFlag = patternLibraryService.updateEnableFlag(patternLibraryDTO);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库 Excel 导入")
    @PostMapping("/excelImport")
    public ApiResult<String> excelImport(@RequestParam("file") MultipartFile file) {
        Boolean resultFlag = patternLibraryService.excelImport(file);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "版型库 Excel 导出")
    @GetMapping("/excelExport")
    public ApiResult<String> excelExport(PatternLibraryPageDTO patternLibraryPageDTO, HttpServletResponse response) {
        Boolean resultFlag = patternLibraryService.excelExport(patternLibraryPageDTO, response);
        if (resultFlag) {
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
        } else {
            return ApiResult.error(ResultConstant.OPERATION_FAILED, 400);
        }
    }

    @ApiOperation(value = "查询设计款号数据信息")
    @GetMapping("/listStyle")
    public ApiResult<List<String>> listStyle(@RequestParam(value = "search", required = false) String search) {
        List<Style> styleList = patternLibraryService.listStyle(search, null);
        if (ObjectUtil.isNotEmpty(styleList)) {
            List<String> styleNoList = styleList.stream().map(Style::getDesignNo).collect(Collectors.toList());
            return ApiResult.success(ResultConstant.OPERATION_SUCCESS, styleNoList);
        }
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, new ArrayList<>());
    }

    @ApiOperation(value = "根据设计款号查询相关数据")
    @GetMapping("/getInfoByDesignNo")
    public ApiResult<PatternLibrary> getInfoByDesignNo(String styleNo) {
        PatternLibrary patternLibrary = patternLibraryService.getInfoByDesignNo(styleNo);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, patternLibrary);
    }

    @ApiOperation(value = "查询已开款的设计款号数据信息（转版型库数据）")
    @PostMapping("/listStyleToPatternLibrary")
    public ApiResult<PageInfo<PatternLibrary>> listStyleToPatternLibrary(@RequestBody PatternLibraryDTO patternLibraryDTO) {
        PageInfo<PatternLibrary> patternLibraryPageInfo = patternLibraryService.listStyleToPatternLibrary(patternLibraryDTO);
        return ApiResult.success("操作成功", patternLibraryPageInfo);
    }

    @ApiOperation(value = "款的使用款记录")
    @GetMapping("/listUseStyleByStyle")
    public ApiResult<PageInfo<UseStyleVO>> listUseStyleByStyle(UseStyleDTO useStyleDTO) {
        PageHelper.startPage(useStyleDTO.getPageNum(), useStyleDTO.getPageSize());
        List<UseStyleVO> useStyleVOList = patternLibraryService.listUseStyleByStyle(useStyleDTO);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, new PageInfo<>(useStyleVOList));
    }

    @ApiOperation(value = "查询已开款的设计款号数据信息（转版型库数据）-廓形及代码")
    @GetMapping("/listStyleToPatternLibrarySilhouette")
    public ApiResult<List<FieldVal>> listStyleToPatternLibrarySilhouette() {
        List<FieldVal> fieldValList = patternLibraryService.listStyleToPatternLibrarySilhouette();
        return ApiResult.success("操作成功", fieldValList);
    }

    @ApiOperation(value = "查询已开款的设计款号数据信息（转版型库数据）-品类")
    @GetMapping("/listStyleToPatternLibraryProdCategory")
    public ApiResult<List<Style>> listStyleToPatternLibraryProdCategory() {
        List<Style> styleList = patternLibraryService.listStyleToPatternLibraryProdCategory();
        return ApiResult.success("操作成功", styleList);
    }

    @ApiOperation(value = "获取大类的类型")
    @GetMapping("/getCategoriesType")
    public ApiResult<CategoriesTypeVO> getCategoriesType() {
        CategoriesTypeVO categoriesType = patternLibraryService.getCategoriesType();
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, categoriesType);
    }

    @ApiOperation(value = "获取所有的筛选条件")
    @GetMapping("/getAllFilterCriteria")
    public ApiResult<List<FilterCriteriaVO>> getAllFilterCriteria(
            @ApiParam(
                    name = "type",
                    value = "筛选条件类型（1-版型编码 2-品牌 3-所属品类 4-廓形 5-所属版型库 6-涉及部件 7-审核状态 8-是否启用）"
            ) Integer type) {
        List<FilterCriteriaVO> list = patternLibraryService.getAllFilterCriteria(type);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, list);
    }

    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        PatternLibrary patternLibrary = patternLibraryService.getOne(new LambdaQueryWrapper<PatternLibrary>().eq(PatternLibrary::getId, dto.getBusinessKey()));
        if (BaseConstant.APPROVAL_PASS.equals(dto.getApprovalType())) {
            // 审核通过
            patternLibrary.setStatus(PatternLibraryStatusEnum.REVIEWED.getCode());
        } else {
            // 审核不通过
            if ("1".equals(dto.getRecallFlag())) {
                // 撤回
                patternLibrary.setStatus(PatternLibraryStatusEnum.NO_SUBMIT.getCode());

            } else {
                // 驳回
                patternLibrary.setStatus(PatternLibraryStatusEnum.REJECTED.getCode());
            }
        }
        return patternLibraryService.updateById(patternLibrary);
    }

    @ApiOperation(value = "根据版型库 ID 生成版型库对应的常青原版树")
    @GetMapping("/listEverGreenTree")
    public ApiResult<EverGreenVO> listEverGreenTree(String patternLibraryId) {
        EverGreenVO everGreenVO = patternLibraryService.listEverGreenTree(patternLibraryId);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, everGreenVO);
    }

    @ApiOperation(value = "删除常青原版树中节点")
    @PostMapping("/removeEverGreenTreeNode")
    public ApiResult<String> removeEverGreenTreeNode(String patternLibraryId) {
        patternLibraryService.removeEverGreenTreeNode(patternLibraryId);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
    }

    @ApiOperation(value = "新增青原版树中节点")
    @PostMapping("/newEverGreenTreeNode")
    public ApiResult<String> newEverGreenTreeNode(String patternLibraryId) {
        patternLibraryService.newEverGreenTreeNode(patternLibraryId);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS);
    }


}
