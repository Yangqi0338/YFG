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
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.LanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.StyleCountryPrintRecordDto;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryPrintRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与款式国家打印记录相关的所有接口信息", tags = {"款式国家打印记录"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/printRecord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StyleCountryPrintRecordController extends BaseController {

    private final StyleCountryPrintRecordService printRecordService;

    /**
     * 查询列表
     */
    @ApiOperation(value = "根据款号获取打印记录", notes = "根据款号获取打印记录")
    @GetMapping("/findPrintRecordByStyleNo")
    public ApiResult<StyleCountryPrintRecordDto> findPrintRecordByStyleNo(@Valid HangTagMoreLanguageDTO languageDTO) {
        languageDTO.setUserCompany(super.getUserCompany());
        return selectSuccess(printRecordService.findPrintRecordByStyleNo(languageDTO));
    }

}
