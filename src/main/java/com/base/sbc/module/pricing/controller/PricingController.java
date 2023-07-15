/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.pricing.dto.PricingCountDTO;
import com.base.sbc.module.pricing.dto.PricingDTO;
import com.base.sbc.module.pricing.dto.PricingDelDTO;
import com.base.sbc.module.pricing.dto.PricingSearchDTO;
import com.base.sbc.module.pricing.service.PricingService;
import com.base.sbc.module.pricing.vo.PricingListVO;
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
 * 类描述：核价表 Controller类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.web.PricingController
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
@RestController
@Api(tags = "核价表")
@RequestMapping(value = BaseController.SAAS_URL + "/pricing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PricingController extends BaseController {

    @Autowired
    private PricingService pricingService;

    /**
     * 分页获取
     *
     * @param pricingSearchDTO
     * @return
     */
    @ApiOperation(value = "分页获取")
    @PostMapping(value = "/queryPageInfo")
    public PageInfo<PricingListVO> queryPageInfo(@Valid @RequestBody PricingSearchDTO pricingSearchDTO) {
        return pricingService.queryPageInfo(pricingSearchDTO, super.getUserCompany());
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/getDetailsById")
    public ApiResult getDetailsById(@Valid @NotBlank(message = "核价id不可为空") String id) {
        return selectSuccess(pricingService.getDetailsById(id, super.getUserCompany()));
    }

    /**
     * 保存
     *
     * @param pricingDTO
     * @return
     */
    @ApiOperation(value = "保存")
    @PostMapping(value = "/save")
    public ApiResult save(@Valid @RequestBody PricingDTO pricingDTO) {
        return updateSuccess(pricingService.save(pricingDTO, super.getUserCompany()));
    }

    /**
     * 删除
     *
     * @param pricingIdDTO
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delByIds")
    public ApiResult delByIds(@Valid @RequestBody PricingDelDTO pricingIdDTO) {
        pricingService.delByIds(pricingIdDTO, super.getUserCompany());
        return deleteSuccess("操作成功");
    }

    /**
     * 提交审批
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "提交审批")
    @GetMapping("/submitApprove")
    public ApiResult submitApprove(@Valid @NotBlank(message = "核价id不可为空") String id) {
        pricingService.submitApprove(id, super.getUserCompany());
        return updateSuccess("操作成功");
    }


    /**
     * 费用计算
     * @param pricingCountDTO
     * @return
     */
    @ApiOperation(value = "费用计算")
    @PostMapping("/costsCount")
    public ApiResult costsCount(@Valid @RequestBody PricingCountDTO pricingCountDTO) {
        return selectSuccess(pricingService.costsCount(pricingCountDTO));
    }
}
