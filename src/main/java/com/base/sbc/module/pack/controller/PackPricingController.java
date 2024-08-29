/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.FormulaDto;
import com.base.sbc.module.pack.dto.OtherCostsPageDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingCraftCostsDto;
import com.base.sbc.module.pack.dto.PackPricingDto;
import com.base.sbc.module.pack.dto.PackPricingOtherCostsDto;
import com.base.sbc.module.pack.dto.PackPricingOtherCostsGstDto;
import com.base.sbc.module.pack.dto.PackPricingProcessCostsDto;
import com.base.sbc.module.pack.entity.PackPricingOtherCostsGst;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackPricingCraftCostsService;
import com.base.sbc.module.pack.service.PackPricingOtherCostsGstService;
import com.base.sbc.module.pack.service.PackPricingOtherCostsService;
import com.base.sbc.module.pack.service.PackPricingProcessCostsService;
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pack.vo.PackPricingCraftCostsVo;
import com.base.sbc.module.pack.vo.PackPricingOtherCostsVo;
import com.base.sbc.module.pack.vo.PackPricingProcessCostsVo;
import com.base.sbc.module.pack.vo.PackPricingVo;
import com.base.sbc.module.pricing.dto.QueryContractPriceDTO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
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
public class PackPricingController extends BaseController {

    @Autowired
    private PackPricingService packPricingService;

    @Autowired
    private PackBomService packBomService;
    @Autowired
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Autowired
    private PackPricingOtherCostsGstService packPricingOtherCostsGstService;
    @Autowired
    private PackPricingProcessCostsService packPricingProcessCostsService;

    @Autowired
    private PackPricingCraftCostsService packPricingCraftCostsService;

    @ApiOperation(value = "获取合同价")
    @PostMapping("/getContractPrice")
    public ApiResult getContractPrice(@RequestBody QueryContractPriceDTO contractPriceDTO) {
        return packPricingService.getContractPrice(contractPriceDTO);
    }

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
    @DuplicationCheck
    public PackPricingVo    savePackPricing(@Valid @RequestBody PackPricingDto dto) {
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

    @ApiOperation(value = "其他费用-gst-分页查询", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @GetMapping("/otherCostsGst")
    public PageInfo<PackPricingOtherCostsVo> otherCostsGstPageInfo(OtherCostsPageDto dto) {
        return packPricingOtherCostsGstService.pageInfo(dto);
    }

    @ApiOperation(value = "其他费用-保存/修改", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @PostMapping("/otherCosts")
    public PackPricingOtherCostsVo saveOtherCosts(@Valid @RequestBody PackPricingOtherCostsDto dto) {
        packPricingOtherCostsService.batchOtherCosts(Arrays.asList(dto), true);
        return BeanUtil.copyProperties(dto, PackPricingOtherCostsVo.class);
    }

    @ApiOperation(value = "批量其他费用-保存/修改", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @PostMapping("/batchOtherCosts")
    public Boolean batchOtherCosts(@Valid @RequestBody List<PackPricingOtherCostsDto> dto) {
        packPricingOtherCostsService.batchOtherCosts(dto, true);
        return true;
    }

    @ApiOperation(value = "批量其他费用-gst-保存/修改", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @PostMapping("/batchOtherCostsGst")
    public Boolean batchOtherCostsGst(@Valid @RequestBody List<PackPricingOtherCostsGstDto> dto) {
        packPricingOtherCostsGstService.saveOrUpdateBatch(BeanUtil.copyToList(dto, PackPricingOtherCostsGst.class));
        return true;
    }

    @ApiOperation(value = "其他费用-删除", notes = "包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @DeleteMapping("/otherCosts")
    public boolean delOtherCosts(@Valid IdsDto idsDto) {
        return packPricingOtherCostsService.delByIds(idsDto.getId());
    }

    @ApiOperation(value = "导出外辅工艺PDF")
    @GetMapping("/exportWfgyPdf")
    public ApiResult exportExcel(OtherCostsPageDto dto) throws Exception {
        return selectSuccess(packPricingOtherCostsService.generateWfgyPdf(dto));
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
        return packPricingService.formula(dto.getFormula(), dto.getItemVal(),2);
    }


    @ApiOperation(value = "获取核价信息路由参数")
    @GetMapping("/getPricingRoute")
    public Map getPricingRoute(String styleNo) {
        return packPricingService.getPricingRoute(styleNo);
    }
}































