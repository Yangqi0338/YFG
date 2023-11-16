/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.vo.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包-核价信息 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackPricingController
 * @email your email
 * @date 创建时间：2023-7-10 13:35:16
 */
@RestController
@Api(tags = "资料包-核价信息")
@RequestMapping(value = BaseController.SAAS_URL + "/packPricing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackPricingController {

    @Autowired
    private PackPricingService packPricingService;

    @Autowired
    private PackBomService packBomService;
    @Autowired
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Autowired
    private PackPricingProcessCostsService packPricingProcessCostsService;

    @Autowired
    private PackPricingCraftCostsService packPricingCraftCostsService;

    @ApiOperation(value = "获取明细")
    @GetMapping()
    public PackPricingVo getDetail(@Valid PackCommonSearchDto dto) {
        return packPricingService.getDetail(dto);
    }

   @ApiOperation(value = "设计BOM核价同步到大货BOM", notes = "设计BOM核价同步到大货BOM,不同步物料费用")
    @PostMapping("/asyncCost")
    public ApiResult asyncCost(@Valid @NotEmpty(message = "主id不可为空") String foreignId){
        packPricingService.asyncCost(foreignId);
        return ApiResult.success();
    }

    @ApiOperation(value = "保存")
    @PostMapping()
    public PackPricingVo savePackPricing(@Valid @RequestBody PackPricingDto dto) {
        return packPricingService.saveByDto(dto);
    }

    @ApiOperation(value = "物料费用-分页查询")
    @GetMapping("/materialCosts")
    public PageInfo<PackBomVo> materialCosts(PackCommonPageSearchDto dto) {
        return packBomService.pageInfo(dto);
    }

    @ApiOperation(value = "其他费用-分页查询", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @GetMapping("/otherCosts")
    public PageInfo<PackPricingOtherCostsVo> otherCostsPageInfo(OtherCostsPageDto dto) {
        return packPricingOtherCostsService.pageInfo(dto);
    }

    @ApiOperation(value = "其他费用-保存/修改", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @PostMapping("/otherCosts")
    public PackPricingOtherCostsVo saveOtherCosts(@Valid @RequestBody PackPricingOtherCostsDto dto) {
        return packPricingOtherCostsService.saveByDto(dto);
    }

    @ApiOperation(value = "批量其他费用-保存/修改", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @PostMapping("/batchOtherCosts")
    public Boolean batchOtherCosts(@Valid @RequestBody List<PackPricingOtherCostsDto> dto) {
        return packPricingOtherCostsService.batchOtherCosts(dto);
    }

    @ApiOperation(value = "其他费用-删除", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @DeleteMapping("/otherCosts")
    public boolean delOtherCosts(@Valid IdsDto idsDto) {
        return packPricingOtherCostsService.delByIds(idsDto.getId());
    }

    @ApiOperation(value = "加工费用-分页查询")
    @GetMapping("/processCosts")
    public PageInfo<PackPricingProcessCostsVo> processCostsPageInfo(PackCommonPageSearchDto dto) {
        return packPricingProcessCostsService.pageInfo(dto);
    }

    @ApiOperation(value = "加工费用-保存/修改")
    @PostMapping("/processCosts")
    public PackPricingProcessCostsVo saveProcessCosts(@Valid @RequestBody PackPricingProcessCostsDto dto) {
        return packPricingProcessCostsService.saveByDto(dto);
    }

    @ApiOperation(value = "导入工序工价")
    @GetMapping("/importProcessPrice")
    public ApiResult importProcessPrice(@Valid @NotEmpty(message = "主id不可为空") String foreignId,
                                        @NotEmpty(message = "类型不可为空") String packType,
                                        @NotEmpty(message = "颜色编码不可为空") String colorCode,
                                        @NotEmpty(message = "颜色名称不可为空") String colorName) {
        packPricingProcessCostsService.importProcessPrice(foreignId, packType, colorCode, colorName);
        return ApiResult.success();
    }

    @ApiOperation(value = "加工费用-删除")
    @DeleteMapping("/processCosts")
    public boolean delProcessCosts(@Valid IdsDto idsDto) {
        return packPricingProcessCostsService.delByIds(idsDto.getId());
    }

    @ApiOperation(value = "二次加工费-分页查询")
    @GetMapping("/craftCosts")
    public PageInfo<PackPricingCraftCostsVo> craftCostsPageInfo(PackCommonPageSearchDto dto) {
        return packPricingCraftCostsService.pageInfo(dto);
    }

    @ApiOperation(value = "二次加工费-保存/修改")
    @PostMapping("/craftCosts")
    public PackPricingCraftCostsVo saveCraftCosts(@Valid @RequestBody PackPricingCraftCostsDto dto) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return packPricingCraftCostsService.saveByDto(dto);
    }

    @ApiOperation(value = "二次加工费-删除")
    @DeleteMapping("/craftCosts")
    public boolean delCraftCosts(@Valid IdsDto idsDto) {
        return packPricingCraftCostsService.delByIds(idsDto.getId());
    }

    @ApiOperation(value = "计算各项的值", notes = "物料费用/加工费用/二次加工费用/包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @GetMapping("/calculateCosts")
    public Map<String, BigDecimal> calculateCosts(@Valid PackCommonSearchDto dto) {
        return packPricingService.calculateCosts(dto);
    }

    @ApiOperation(value = "公式计算")
    @PostMapping("/formula")
    public BigDecimal formula(@Valid @RequestBody FormulaDto dto) {
        return packPricingService.formula(dto.getFormula(), dto.getItemVal());
    }
}































