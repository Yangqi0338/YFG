/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.controller;

import cn.hutool.json.JSONUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageCheckDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.service.HangTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private HangTagService hangTagService;

    @ApiOperation(value = "查询详情多语言")
    @GetMapping("/getMoreLanguageDetailsByBulkStyleNo")
    public ApiResult getMoreLanguageDetailsByBulkStyleNo(@Valid HangTagMoreLanguageDTO hangTagMoreLanguageDTO) {
        hangTagMoreLanguageDTO.setUserCompany(super.getUserCompany());
        if (hangTagMoreLanguageDTO.getType() == null) throw new OtherException("吊牌类型不能为空");
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO, false, true));
    }

    @ApiOperation(value = "查询详情多语言")
    @GetMapping("/getMoreLanguageCheckByBulkStyleNo")
//    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestParam @NotEmpty(message = "检查参数列表不能为空") List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList) {
    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestParam @NotBlank(message = "检查参数不能为空") String jsonParams) {
        List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList = JSONUtil.toList(jsonParams, HangTagMoreLanguageCheckDTO.class);
        Validation.buildDefaultValidatorFactory().getValidator().validate(hangTagMoreLanguageCheckDTOList, Default.class);

        // 通过名字获取编码

        HangTagMoreLanguageDTO hangTagMoreLanguageDTO = new HangTagMoreLanguageDTO();
        hangTagMoreLanguageDTO.setUserCompany(super.getUserCompany());
        hangTagMoreLanguageDTO.setHangTagMoreLanguageCheckDTOList(hangTagMoreLanguageCheckDTOList);
        hangTagMoreLanguageDTO.setBulkStyleNo(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getBulkStyleNo).collect(Collectors.joining(",")));
        hangTagMoreLanguageDTO.setCode(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getCode).collect(Collectors.joining(",")));
        hangTagMoreLanguageDTO.setSource(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getSource).findFirst().orElse(SystemSource.BCS));
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO, false, true));
    }

}
