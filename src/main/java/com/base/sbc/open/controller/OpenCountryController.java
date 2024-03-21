package com.base.sbc.open.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.open.dto.CountryOpenQueryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与国家语言相关的所有接口信息", tags = {"国家语言接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/country", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OpenCountryController extends BaseController {

    private final CountryLanguageService countryLanguageService;

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(CountryOpenQueryDto countryOpenQueryDto) {
        CountryQueryDto countryQueryDto = new CountryQueryDto();
        countryQueryDto.setCountryName(countryOpenQueryDto.getCountryName());
        countryQueryDto.setLanguageName(countryOpenQueryDto.getLanguageName());
        List<String> countryNameList = Arrays.asList(countryOpenQueryDto.getCountryName().split(COMMA));
        List<CountryLanguageDto> countryList = countryLanguageService.listQuery(countryQueryDto);
        List<String> notMatchCountryNameList = countryNameList.stream()
                .filter(countryName -> countryList.stream().noneMatch(it -> it.getCountryName().equals(countryName)))
                .collect(Collectors.toList());
        boolean isSuccess = CollectionUtil.isEmpty(notMatchCountryNameList);
        StringJoiner warnMsgJoiner = new StringJoiner("/");
        notMatchCountryNameList.forEach(it-> warnMsgJoiner.add(it+"不存在"));
        ApiResult result = new ApiResult();
        result.setSuccess(isSuccess);
        result.setMessage(isSuccess ? "查询成功": warnMsgJoiner.toString());
        result.setStatus(0);
        result.setData(countryList);
        switch (countryOpenQueryDto.getSource()) {
            case PDM:
                return selectSuccess("");
            case BCS:
            case ESCM:
            case PRINT:
                return result;
            default:
                throw new IllegalStateException("Unexpected value: " + countryOpenQueryDto.getSource());
        }
    }

}
