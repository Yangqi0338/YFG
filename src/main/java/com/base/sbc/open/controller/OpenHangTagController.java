/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ValidationUtil;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageCheckDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.moreLanguage.dto.StyleCountryPrintRecordDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryPrintRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.OPEN_CV;

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

    private final StyleCountryPrintRecordService printRecordService;

    private final CountryLanguageService countryLanguageService;

    @ApiOperation(value = "查询详情多语言")
    @GetMapping("/getMoreLanguageDetailsByBulkStyleNo")
    public ApiResult getMoreLanguageDetailsByBulkStyleNo(@Valid HangTagMoreLanguageSystemDTO hangTagMoreLanguageDTO) {
        if (hangTagMoreLanguageDTO.getType() == null) throw new OtherException("吊牌类型不能为空");
        HangTagMoreLanguageDTO languageDTO = OPEN_CV.copy2MoreLanguageDTO(hangTagMoreLanguageDTO);
        languageDTO.setCode(countryLanguageService.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                .eq(CountryLanguage::getCountryCode, hangTagMoreLanguageDTO.getCountryCode()), CountryLanguage::getCode));
        if (StrUtil.isBlank(languageDTO.getCode())) throw new OtherException("PDM未创建" + Opt.ofNullable(hangTagMoreLanguageDTO.getCountryName()).orElse("") + "国家语言翻译");
        languageDTO.setUserCompany(super.getUserCompany());
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(languageDTO, false, true));
    }

    @ApiOperation(value = "查询详情多语言")
    @PostMapping("/getMoreLanguageCheckByBulkStyleNo")
//    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestParam @NotEmpty(message = "检查参数列表不能为空") List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList) {
    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestParam @NotBlank(message = "检查参数不能为空") String jsonParams) {
        List<HangTagMoreLanguageSystemDTO> hangTagMoreLanguageSystemDTOList = JSONUtil.toList(jsonParams, HangTagMoreLanguageSystemDTO.class);
        ValidationUtil.validate(hangTagMoreLanguageSystemDTOList);

        // 通过名字获取编码
        List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList = hangTagMoreLanguageSystemDTOList.stream().map(hangTagMoreLanguageSystemDTO-> {
            HangTagMoreLanguageCheckDTO languageCheckDTO = OPEN_CV.copy2Check(hangTagMoreLanguageSystemDTO);
            languageCheckDTO.setCode(countryLanguageService.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                    .eq(CountryLanguage::getCountryCode, hangTagMoreLanguageSystemDTO.getCountryCode()), CountryLanguage::getCode));
            if (StrUtil.isBlank(languageCheckDTO.getCode())) throw new OtherException("PDM未创建" + Opt.ofNullable(hangTagMoreLanguageSystemDTO.getCountryName()).orElse("") + "国家语言翻译");
            return languageCheckDTO;
        }).collect(Collectors.toList());

        HangTagMoreLanguageDTO hangTagMoreLanguageDTO = new HangTagMoreLanguageDTO();
        hangTagMoreLanguageDTO.setUserCompany(super.getUserCompany());
        hangTagMoreLanguageDTO.setHangTagMoreLanguageCheckDTOList(hangTagMoreLanguageCheckDTOList);
        hangTagMoreLanguageDTO.setBulkStyleNo(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getBulkStyleNo).collect(Collectors.joining(",")));
        hangTagMoreLanguageDTO.setCode(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getCode).collect(Collectors.joining(",")));
        hangTagMoreLanguageDTO.setSource(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getSource).findFirst().orElse(SystemSource.BCS));
        return selectSuccess(JSONUtil.toJsonStr(hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO, false, true)));
    }

    /**
     * 保存打印记录
     */
    @ApiOperation(value = "保存打印记录", notes = "保存打印记录")
    @PostMapping("/savePrintRecord")
    public ApiResult savePrintRecord(@Valid HangTagMoreLanguageSystemDTO languageDTO) {
        printRecordService.savePrintRecord(languageDTO);
        return updateSuccess(true);
    }

}
