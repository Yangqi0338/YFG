/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.hangTag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.dto.StylePricingStatusDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    public PageInfo<StylePricingVO> getStylePricingList(Principal user, @Valid @RequestBody StylePricingSearchDTO stylePricingSearchDTO) {
        return stylePricingService.getStylePricingList(user,stylePricingSearchDTO);
    }

    @ApiOperation(value = "通过资料包id获取")
    @GetMapping("/getByPackId")
    public ApiResult getByPackId(@Valid @NotBlank(message = "资料包id不可为空") String packId) {
        return selectSuccess(stylePricingService.getByPackId(packId, super.getUserCompany()));
    }

    @ApiOperation(value = "保存")
    @PostMapping("/insertOrUpdate")
    public ApiResult insertOrUpdate(@Valid @RequestBody StylePricingSaveDTO stylePricingSaveDTO) {
        stylePricingService.insertOrUpdate(stylePricingSaveDTO, super.getUserCompany());
        return updateSuccess("修改成功");
    }

    @ApiOperation(value = "批量保存")
    @PostMapping("/insertOrUpdateBatch")
    public ApiResult insertOrUpdateBatch(@Valid @RequestBody List<StylePricingSaveDTO> stylePricingSaveDTO) {
        stylePricingService.insertOrUpdateBatch(stylePricingSaveDTO, super.getUserCompany());
        return updateSuccess("修改成功");
    }
    @ApiOperation(value = "提交审核")
    @PostMapping("/updateStatus")
    public ApiResult updateStatus( @RequestBody StylePricingStatusDTO dto) {
        String[] split = dto.getIds().split(",");
        if (split.length == 0){
            throw new OtherException("存在为生成数据,请先点击修改保存");
        }
        List<StylePricing> stylePricings = stylePricingService.listByIds(Arrays.asList(split));
        for (StylePricing stylePricing : stylePricings) {

            if ("1".equals(dto.getControlConfirm()) && "1".equals(stylePricing.getProductHangtagConfirm()) && "1".equals(stylePricing.getControlHangtagConfirm())) {
                throw new OtherException("存在已经提交审核");
            }
            if ("1".equals(dto.getProductHangtagConfirm()) && "0".equals(stylePricing.getControlConfirm())){
                throw new OtherException("请先计控确认");
            }
            if ("1".equals(dto.getControlHangtagConfirm()) && ("0".equals(stylePricing.getProductHangtagConfirm())  || "0".equals(stylePricing.getControlConfirm()))){
                throw new OtherException("请先商品吊牌确认");
            }
            if (!StringUtils.isEmpty(dto.getControlConfirm())){
                if (dto.getControlConfirm().equals(stylePricing.getControlConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setControlConfirm(dto.getControlConfirm());
                stylePricing.setControlConfirmTime(new Date());
            }
            if (!StringUtils.isEmpty(dto.getProductHangtagConfirm())){
                if (dto.getProductHangtagConfirm().equals(stylePricing.getProductHangtagConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setProductHangtagConfirm(dto.getProductHangtagConfirm());
                stylePricing.setControlConfirmTime(new Date());
            }
            if (!StringUtils.isEmpty(dto.getControlHangtagConfirm())){
                if (dto.getControlHangtagConfirm().equals(stylePricing.getControlHangtagConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setControlHangtagConfirm(dto.getControlHangtagConfirm());
                stylePricing.setControlConfirmTime(new Date());
            }
        }
        stylePricingService.updateBatchById(stylePricings);
        return updateSuccess("提交成功");
    }


}































