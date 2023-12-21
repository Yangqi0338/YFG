/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageCheckDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.service.HangTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO, false));
    }

    @ApiOperation(value = "查询详情多语言")
    @GetMapping("/getMoreLanguageCheckByBulkStyleNo")
    public ApiResult getMoreLanguageCheckByBulkStyleNo(@Valid @RequestBody @NotEmpty(message = "检查参数列表不能为空") List<HangTagMoreLanguageCheckDTO> hangTagMoreLanguageCheckDTOList) {
        HangTagMoreLanguageDTO hangTagMoreLanguageDTO = new HangTagMoreLanguageDTO();
        hangTagMoreLanguageDTO.setUserCompany(super.getUserCompany());
        hangTagMoreLanguageDTO.setHangTagMoreLanguageCheckDTOList(hangTagMoreLanguageCheckDTOList);
        hangTagMoreLanguageDTO.setBulkStyleNo(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getBulkStyleNo).collect(Collectors.joining(",")));
        hangTagMoreLanguageDTO.setCountryLanguageId(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getCountryLanguageId).collect(Collectors.joining(",")));
        hangTagMoreLanguageDTO.setSource(hangTagMoreLanguageCheckDTOList.stream().map(HangTagMoreLanguageCheckDTO::getSource).findFirst().orElse(SystemSource.BCS));
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO, false));
    }

}
