package com.base.sbc.module.moreLanguage.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageGroupDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.LanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.SqlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

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
    public ApiResult<?> listQuery(CountryQueryDto countryQueryDto) {
        // 肯定不查缓存
        countryQueryDto.setCache(YesOrNoEnum.NO.getValueStr());
        countryQueryDto.setDecorateLanguageName(true);
        // 是否进行国家码分组
        if (YesOrNoEnum.YES.getValueStr().equals(countryQueryDto.getCodeGroup())) {
            List<CountryLanguageGroupDto> list = new ArrayList<>();
            // 查询国家并根据编码分组
           countryLanguageService.listQuery(countryQueryDto)
                    .stream().collect(Collectors.groupingBy(CountryLanguageDto::getCode)).forEach((code, sameCodeList)-> {
                       CountryLanguageGroupDto countryLanguageDto = MORE_LANGUAGE_CV.copy2Group(sameCodeList.get(0));
                       countryLanguageDto.setLanguageList(MORE_LANGUAGE_CV.copyList2Group(sameCodeList));
                       list.add(countryLanguageDto);
                    });
            return selectSuccess(list);
        }else {
            // 正常分页查询
            Page<CountryLanguageDto> startPage = countryQueryDto.startPage();
            List<CountryLanguageDto> list = countryLanguageService.listQuery(countryQueryDto);
            PageInfo<CountryLanguageDto> pageInfo = startPage.toPageInfo();
            pageInfo.setList(list);
            return selectSuccess(pageInfo);
        }
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
            // 删除之前的旧缓存,防止循环点击上一步下一步导致的数据残留
            cancelSave(countryTypeLanguageSaveDto.getCode());
            // 获取保存返回的缓存国家编码
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
        // 查询语言数据字典
        String code = languageQueryDto.getCode();
        List<BasicBaseDict> dictList = ccmFeignService.getDictInfoToList(MoreLanguageProperties.languageDictCode);

        if (StrUtil.isNotBlank(code)) {
            // 过滤对应的编码
            dictList = dictList.stream().filter(it-> it.getValue().contains(code)).collect(Collectors.toList());
        }

        // 手动分页
        Page<BasicBaseDict> startPage = languageQueryDto.startPage();
        ccmFeignService.setPage(startPage);
        PageInfo<BasicBaseDict> pageInfo = ccmFeignService.clearPage(dictList);
        List<BasicBaseDict> subDictList = pageInfo.getList();

        SqlUtil.clearLocalPage();
        // 初始化语言对应的国家兼容数据
        countryLanguageService.initLanguage(subDictList);

        return CollectionUtil.isNotEmpty(subDictList) ? selectSuccess(pageInfo) : selectNotFound();
    }

    /**
     * 取消保存
     */
    @ApiOperation(value = "取消保存", notes = "取消保存")
    @GetMapping("/cancelSave")
    public ApiResult cancelSave(String code) {
        return updateSuccess(countryLanguageService.cancelSave(code));
    }

    /**
     * 获取国家
     */
    @ApiOperation(value = "获取所有创建的国家", notes = "获取国家")
    @GetMapping("/getAllCountry")
    public ApiResult getAllCountry(String code) {
        return selectSuccess(countryLanguageService.getAllCountry(code));
    }

}
