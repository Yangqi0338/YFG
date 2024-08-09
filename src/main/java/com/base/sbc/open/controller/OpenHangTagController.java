/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageCheckDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBCSVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;
import static com.base.sbc.module.common.convert.ConvertContext.HANG_TAG_CV;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;
import static com.base.sbc.module.common.convert.ConvertContext.OPEN_CV;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.enableFlagFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.languageCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.modelLanguageCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.singleLanguageFlagFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.typeFunc;

/**
 * 类描述：吊牌表 Controller类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.web.HangTagController
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:53
 */
@RestController
@Api(tags = "吊牌表")
@RequestMapping(value = BaseController.OPEN_URL + "/hangTag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequiredArgsConstructor
public class OpenHangTagController extends BaseController {

    private final HangTagService hangTagService;

    private final StyleCountryStatusService styleCountryStatusService;

    private final CountryLanguageService countryLanguageService;

    @ApiOperation(value = "查询详情多语言")
    @GetMapping("/getMoreLanguageDetailsByBulkStyleNo")
    public ApiResult getMoreLanguageDetailsByBulkStyleNo(@Valid HangTagMoreLanguageSystemDTO hangTagMoreLanguageDTO) {
        if (hangTagMoreLanguageDTO.getType() == null) throw new OtherException(MoreLanguageProperties.getMsg(NOT_EXIST_HANG_TAG_TYPE));
        HangTagMoreLanguageDTO languageDTO = OPEN_CV.copy2MoreLanguageDTO(hangTagMoreLanguageDTO);
        // 找PDM这边DB中的国家语言唯一编码
        languageDTO.setCode(countryLanguageService.findOneField(new BaseLambdaQueryWrapper<CountryLanguage>()
                .notEmptyEq(modelLanguageCodeFunc, OPEN_CV.getModelLanguageCode(hangTagMoreLanguageDTO.getModelLanguageCode()))
                .notEmptyEq(languageCodeFunc, hangTagMoreLanguageDTO.getLanguageCode())
                .eq(singleLanguageFlagFunc, YesOrNoEnum.NO)
                .eq(typeFunc, languageDTO.getType())
                , CountryLanguage::getCode)
        );
        if (StrUtil.isBlank(languageDTO.getCode())) throw new OtherException(MoreLanguageProperties.getMsg(NOT_INSERT));
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(languageDTO));
    }

    @ApiOperation(value = "查询详情多语言")
    @PostMapping("/getMoreLanguageCheckByBulkStyleNo")
//    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestParam @NotEmpty(message = "检查参数列表不能为空") List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList) {
    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestBody List<HangTagMoreLanguageSystemDTO> hangTagMoreLanguageSystemDTOList) {
        // 通过名字获取编码
        List<HangTagMoreLanguageBCSVO> resultList = new ArrayList<>();
        List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList = new ArrayList<>();
        // 根据国家分组
        hangTagMoreLanguageSystemDTOList.stream().collect(Collectors.groupingBy(it->
                CommonUtils.saftyStrJoin("-",
                                OPEN_CV.getTypeByCsvIndex(it.getType()).getCode(),
                                it.getLanguageCode(),
                                OPEN_CV.getModelLanguageCode(it.getModelLanguageCode())
                        ).toString()
        )).forEach((key, sameKeyList)-> {
            // 获取内部编码
            String code = countryLanguageService.findOneField(new BaseLambdaQueryWrapper<CountryLanguage>()
                            .notEmptyEq(languageCodeFunc, key.split("-")[1])
                            .notEmptyEq(modelLanguageCodeFunc, key.split("-")[2])
                            .eq(singleLanguageFlagFunc, YesOrNoEnum.NO)
                            .eq(typeFunc, key.split("-")[0])
                            , CountryLanguage::getCode);
            List<HangTagMoreLanguageBaseVO> baseList = new ArrayList<>();
            sameKeyList.forEach(hangTagMoreLanguageSystemDTO->{
                // 如果没有编码,进行去重,并封装对应的结果
                if (StrUtil.isBlank(code)) {
                    baseList.add(OPEN_CV.copy2MoreLanguageVO(hangTagMoreLanguageSystemDTO));
                }else {
                    HangTagMoreLanguageCheckDTO languageCheckDTO = OPEN_CV.copy2Check(hangTagMoreLanguageSystemDTO);
                    languageCheckDTO.setCode(code);
                    hangTagMoreLanguageCheckDTOList.add(languageCheckDTO);
                }
            });
            if (CollUtil.isNotEmpty(baseList)) {
                resultList.add(new HangTagMoreLanguageBCSVO(HANG_TAG_CV.copyList2Bcs(baseList)));
            }
        });

        // 已经没有可处理的国家就直接结束了
        if (CollUtil.isNotEmpty(hangTagMoreLanguageCheckDTOList))  {
            HangTagMoreLanguageDTO hangTagMoreLanguageDTO = new HangTagMoreLanguageDTO();
            hangTagMoreLanguageDTO.setHangTagMoreLanguageCheckDTOList(hangTagMoreLanguageCheckDTOList);
            hangTagMoreLanguageDTO.setBulkStyleNo(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getBulkStyleNo).distinct().collect(Collectors.joining(COMMA)));
            hangTagMoreLanguageDTO.setCode(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getCode).distinct().collect(Collectors.joining(COMMA)));
            hangTagMoreLanguageDTO.setSource(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getSource).findFirst().orElse(SystemSource.BCS));
            hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getType).findFirst().ifPresent(hangTagMoreLanguageDTO::setType);
            hangTagMoreLanguageDTO.setMergeWarnMsg(true);
            resultList.addAll((List<HangTagMoreLanguageBCSVO>) hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO));
        }
        return selectSuccess(resultList);
    }

    /**
     * 保存打印记录
     */
    @ApiOperation(value = "保存打印记录", notes = "保存打印记录")
    @PostMapping("/savePrintRecord")
    public ApiResult savePrintRecord(@RequestBody HangTagMoreLanguageSystemDTO languageDTO) {
        styleCountryStatusService.savePrintRecord(languageDTO);
        return updateSuccess(true);
    }

}
