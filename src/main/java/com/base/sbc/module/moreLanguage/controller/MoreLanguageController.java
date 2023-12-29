package com.base.sbc.module.moreLanguage.controller;

import com.alibaba.excel.EasyExcel;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.listener.MoreLanguageImportListener;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.groups.Default;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与多语言相关的所有接口信息", tags = {"多语言接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/moreLanguage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MoreLanguageController extends BaseController {

    private final MoreLanguageService moreLanguageService;

    private final UserUtils userUtils;

    private final FlowableService flowableService;

    private final RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MoreLanguageImportListener importListener;

    /**
     * 导入国家翻译
     */
    @SneakyThrows
    @PostMapping("/importExcel")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "导入国家翻译", notes = "导入国家翻译")
    @DuplicationCheck(type = 3)
    public ApiResult importExcel(@RequestPart MultipartFile file, @RequestParam String countryLanguageId) {
//        moreLanguageService.importExcel(excelQueryDto);
        importListener.setCountryLanguageId(countryLanguageId);
        EasyExcel.read(file.getInputStream(), importListener).headRowNumber(2).doReadAll();

        return selectSuccess("");
    }
    /**
     * 导出国家翻译
     */
    @GetMapping("/exportExcel")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "导出国家翻译", notes = "导出国家翻译")
    @DuplicationCheck
    public ApiResult exportExcel(@Valid MoreLanguageExcelQueryDto excelQueryDto) {
        moreLanguageService.exportExcel(excelQueryDto);

        return selectSuccess("");
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(@Validated MoreLanguageQueryDto moreLanguageQueryDto) {
        if (moreLanguageQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        return selectSuccess(moreLanguageService.listQuery(moreLanguageQueryDto));
    }

    /**
     * 查询国家拥有的标准表表头
     */
    @ApiOperation(value = "查询国家拥有的标准表表头", notes = "查询国家拥有的标准表表头")
    @GetMapping("/queryCountryTitle")
    public ApiResult  queryCountryTitle(@Validated({Default.class, CountryLanguageService.class}) MoreLanguageQueryDto moreLanguageQueryDto) {
        if (moreLanguageQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        return selectSuccess(moreLanguageService.queryCountryTitle(moreLanguageQueryDto));
    }
}
