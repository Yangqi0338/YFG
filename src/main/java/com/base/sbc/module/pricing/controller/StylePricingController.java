/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 类描述：款式定价 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.web.StylePricingController
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 */
@RestController
@Api(tags = "款式定价")
@RequestMapping(value = BaseController.SAAS_URL + "/stylePricing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StylePricingController extends BaseController {

    @Autowired
    private StylePricingService stylePricingService;

    @ApiOperation(value = "获取款式定价列表")
    @PostMapping("/getStylePricingList")
    public PageInfo<StylePricingVO> getStylePricingList(@Valid @RequestBody StylePricingSearchDTO stylePricingSearchDTO) {
        return stylePricingService.getStylePricingList(stylePricingSearchDTO);
    }

    @ApiOperation(value = "通过资料包id获取")
    @GetMapping("/getByPackId")
    public ApiResult getByPackId(@Valid @NotBlank(message = "资料包id不可为空") String packId) {
        return selectSuccess(stylePricingService.getByPackId(packId, super.getUserCompany()));
    }

}































