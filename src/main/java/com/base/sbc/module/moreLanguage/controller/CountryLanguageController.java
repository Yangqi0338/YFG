package com.base.sbc.module.moreLanguage.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.LanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与国家语言相关的所有接口信息", tags = {"国家语言接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/country", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CountryLanguageController extends BaseController {

    private final CountryLanguageService countryLanguageService;
    
    private final CcmFeignService ccmFeignService;

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult<PageInfo<CountryLanguageDto>> listQuery(CountryQueryDto countryQueryDto) {

        countryQueryDto.setCache(YesOrNoEnum.NO.getValueStr());
        Page<CountryLanguageDto> startPage;
        List<CountryLanguageDto> list;
        if (YesOrNoEnum.YES.getValueStr().equals(countryQueryDto.getCodeGroup())) {
            countryQueryDto.setPageSize(Integer.MAX_VALUE);
            startPage = countryQueryDto.startPage();
            list = countryLanguageService.listQuery(countryQueryDto).stream()
                    .collect(Collectors.groupingBy(CountryLanguageDto::getCode))
                    .values().stream().map(it-> it.get(0)).collect(Collectors.toList());
        }else {
            startPage = countryQueryDto.startPage();
            list = countryLanguageService.listQuery(countryQueryDto);
        }
        PageInfo<CountryLanguageDto> pageInfo = startPage.toPageInfo();
        pageInfo.setList(list);
        return selectSuccess(pageInfo);
    }

    /**
     * 国家详情
     */
    @GetMapping("/detail")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "国家详情", notes = "国家详情")
    public ApiResult detail(@Valid MoreLanguageQueryDto queryDto) {
        return selectSuccess(countryLanguageService.detail(queryDto));
    }

    /**
     * 保存国家
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "保存国家", notes = "保存国家")
    @DuplicationCheck
    public ApiResult save(@Valid @RequestBody CountryTypeLanguageSaveDto countryTypeLanguageSaveDto) {
        String countryCode = countryLanguageService.save(countryTypeLanguageSaveDto, false);
        return countryTypeLanguageSaveDto.getCode() != null ? updateSuccess(countryCode) : insertSuccess(countryCode);
    }

    /**
     * 预览
     */
    @ApiOperation(value = "预览", notes = "预览")
    @PostMapping("/review")
    public ApiResult review(@Valid @RequestBody CountryTypeLanguageSaveDto countryTypeLanguageSaveDto) {
        try {
            String code = countryLanguageService.review(countryTypeLanguageSaveDto);
            return selectSuccess(code);
        }catch (RightException e) { return selectSuccess(e.getMessage()); }
    }

    /**
     * 语言查询
     */
    @ApiOperation(value = "语言查询", notes = "语言查询")
    @GetMapping("/languageQuery")
    public ApiResult languageQuery(@Valid LanguageQueryDto languageQueryDto) {
        String code = languageQueryDto.getCode();
        List<BasicBaseDict> dictList = ccmFeignService.getDictInfoToList("language");

        if (StrUtil.isNotBlank(code)) {
            dictList = dictList.stream().filter(it-> it.getValue().contains(code)).collect(Collectors.toList());
        }

        if (languageQueryDto.isInit()) {
            countryLanguageService.initLanguage(dictList);
        }

        Page<BasicBaseDict> startPage = languageQueryDto.startPage();
        ccmFeignService.setPage(startPage);
        return CollectionUtil.isNotEmpty(dictList) ? selectSuccess(ccmFeignService.clearPage(dictList)) : selectNotFound();
    }

    /**
     * 取消保存
     */
    @ApiOperation(value = "取消保存", notes = "取消保存")
    @GetMapping("/cancelSave")
    public ApiResult cancelSave(CountryQueryDto countryQueryDto) {
        return updateSuccess(countryLanguageService.cancelSave(countryQueryDto));
    }

}
