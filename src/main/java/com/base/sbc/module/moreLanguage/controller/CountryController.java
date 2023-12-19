package com.base.sbc.module.moreLanguage.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与国家语言相关的所有接口信息", tags = {"国家语言接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/country", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CountryController extends BaseController {

    private final CountryService countryService;

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(CountryQueryDto countryQueryDto) {
        return selectSuccess(countryService.listQuery(countryQueryDto));
    }

}
