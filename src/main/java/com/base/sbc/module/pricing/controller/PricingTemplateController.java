/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.pricing.dto.*;
import com.base.sbc.module.pricing.service.PricingTemplateService;
import com.base.sbc.module.pricing.vo.PricingTemplateVO;
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
 * 类描述：核价模板 Controller类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.web.PricingTemplateController
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:21:48
 */
@RestController
@Api(tags = "核价模板")
@RequestMapping(value = BaseController.SAAS_URL + "/pricingTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PricingTemplateController extends BaseController {

    @Autowired
    private PricingTemplateService pricingTemplateService;

    /**
     * 分页获取
     *
     * @param pricingTemplateSearchDTO
     * @return
     */
    @ApiOperation(value = "分页获取模板")
    @PostMapping(value = "/queryPageInfo")
    public PageInfo<PricingTemplateVO> queryPageInfo(@Valid @RequestBody PricingTemplateSearchDTO pricingTemplateSearchDTO) {
        return pricingTemplateService.queryPageInfo(pricingTemplateSearchDTO, super.getUserCompany());
    }
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/getDetailsById")
    public ApiResult getDetailsById(@Valid @NotBlank(message = "核价模板id不可为空") String id) {
        return selectSuccess(pricingTemplateService.getDetailsById(id, super.getUserCompany()));
    }

    /**
     * 保存
     *
     * @param pricingTemplateDTO
     * @return
     */
    @ApiOperation(value = "保存")
    @PostMapping(value = "/save")
    public ApiResult save(@Valid @RequestBody PricingTemplateDTO pricingTemplateDTO) {
        return updateSuccess(pricingTemplateService.save(pricingTemplateDTO, super.getUserCompany()));
    }

    /**
     * 删除
     *
     * @param pricingDelDTO
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delById")
    public ApiResult delById(@Valid @RequestBody PricingDelDTO pricingDelDTO) {
        pricingTemplateService.delById(pricingDelDTO);
        return deleteSuccess("操作成功");
    }

    /**
     * 默认设置
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "默认设置")
    @GetMapping("/defaultSetting")
    public ApiResult defaultSetting(@Valid @NotBlank(message = "核价模板id不可为空") String id) {
        pricingTemplateService.defaultSetting(id, super.getUserCompany());
        return updateSuccess("操作成功");
    }


    /**
     * 修改状态
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "修改状态")
    @PostMapping(value = "/updateStatus")
    public ApiResult updateStatus(@Valid @RequestBody PricingUpdateStatusDTO dto) {
        pricingTemplateService.updateStatus(dto);
        return updateSuccess("操作成功");
    }

    /**
     * 核价公式模板计算
     *
     * @param formulaCountDTO
     * @return
     */
    @ApiOperation(value = "核价公式模板计算")
    @PostMapping(value = "/formulaCount")
    public ApiResult formulaCount(@Valid @RequestBody FormulaCountDTO formulaCountDTO) {
        return selectSuccess(pricingTemplateService.formulaCount(formulaCountDTO, super.getUserCompany()));
    }

    @ApiOperation(value = "获取默认模板")
    @GetMapping("/getDefaultPricingTemplate")
    public PricingTemplateVO  getDefaultPricingTemplate(@RequestParam String brand, @RequestParam String devtType){
        return pricingTemplateService.getDefaultPricingTemplate(brand, devtType ,super.getUserCompany());
    }
}