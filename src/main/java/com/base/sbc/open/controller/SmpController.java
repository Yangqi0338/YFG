package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.open.dto.MtBpReqDto;
import com.base.sbc.open.dto.SmpDeptDto;
import com.base.sbc.open.dto.SmpPostDto;
import com.base.sbc.open.dto.SmpUserDto;
import com.base.sbc.open.entity.MtBqReqEntity;
import com.base.sbc.open.service.MtBqReqService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/10 9:27:47
 * @mail 247967116@qq.com
 * smp开放接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/smp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmpController extends BaseController {

    private final MtBqReqService mtBqReqService;

    private final BasicsdatumSupplierService basicsdatumSupplierService;

    private final AmcService amcService;

    /**
     * bp供应商
     */
    @PostMapping("/supplierSave")
    @ApiOperation(value = "bp供应商新增或者修改", notes = "bp供应商新增或者修改")
    @Transactional(rollbackFor = {Throwable.class})
    public ApiResult supplierSave(@RequestBody JSONObject jsonObject) {
        MtBpReqDto mtBpReqDto = JSONObject.parseObject(jsonObject.toJSONString(), MtBpReqDto.class);

        //先保存传过来的数据对象
        MtBqReqEntity mtBqReqEntity = mtBpReqDto.toMtBqReqEntity();
        mtBqReqService.saveOrUpdate(mtBqReqEntity, new QueryWrapper<MtBqReqEntity>().eq("partner", mtBqReqEntity.getPartner()));
        //再存入供应商
        BasicsdatumSupplier basicsdatumSupplier = mtBqReqEntity.toBasicsdatumSupplier();
        basicsdatumSupplierService.saveOrUpdate(basicsdatumSupplier, new QueryWrapper<BasicsdatumSupplier>().eq("supplier_code", basicsdatumSupplier.getSupplierCode()));
        return insertSuccess(null);
    }

    /**
     * hr-人员
     */
    @PostMapping("/hrUserSave")
    @ApiOperation(value = "hr-人员新增或者修改", notes = "hr-人员新增或者修改")
    public ApiResult hrSave(@RequestBody JSONObject jsonObject) {
        SmpUserDto smpUserDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpUserDto.class);
        smpUserDto.preInsert();
        smpUserDto.setCreateName("smp请求");
        smpUserDto.setUpdateName("smp请求");
        smpUserDto.setCompanyCode(smpUserDto.getCompanyId());
        amcService.hrUserSave(smpUserDto);
        return insertSuccess(null);
    }

    /**
     * hr-部门
     */
    @PostMapping("/hrDeptSave")
    @ApiOperation(value = "hr-部门新增或者修改", notes = "hr-部门新增或者修改")
    public ApiResult hrDeptSave(@RequestBody JSONObject jsonObject) {
        SmpDeptDto smpDeptDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpDeptDto.class);
        System.out.println(smpDeptDto);
        return insertSuccess(null);
    }

    /**
     * hr-岗位
     */
    @PostMapping("/hrPostSave")
    @ApiOperation(value = "hr-岗位新增或者修改", notes = "hr-岗位新增或者修改")
    public ApiResult hrPostSave(@RequestBody JSONObject jsonObject) {
        SmpPostDto smpPostDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpPostDto.class);
        System.out.println(smpPostDto);
        return insertSuccess(null);
    }


    /**
     * smp-样衣
     */
    @PostMapping("/smpSampleSave")
    @ApiOperation(value = "smp-样衣新增或者修改", notes = "smp-样衣新增或者修改")
    public ApiResult smpSampleSave(@RequestBody JSONObject jsonObject) {
        SmpSampleDto smpSampleDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpSampleDto.class);
        System.out.println(smpSampleDto);
        return insertSuccess(null);
    }


    /**
     * 吊牌打印
     */
    @GetMapping("/TagPrinting")
    @ApiOperation(value = "吊牌打印获取", notes = "吊牌打印获取")
    public ApiResult tagPrinting(String id, Boolean bl) {
        List<TagPrinting> tagPrintings = new ArrayList<>();
        TagPrinting tagPrinting = new TagPrinting();
        tagPrinting.setColorwayWareCode("ABC123");
        tagPrinting.setIsGift("No");
        tagPrinting.setColorDescription("Red");
        tagPrinting.setColorCode("001");
        tagPrinting.setStyleCode("STYLE001");
        tagPrinting.setColorwayBatchNo("BATCH001");
        tagPrinting.setMerchApproved(true);
        tagPrinting.setSecCode("SEC001");
        tagPrinting.setMainCode("MAIN001");
        tagPrinting.setColorwaySalesPrice("99.99");
        tagPrinting.setIsAccessories(true);
        tagPrinting.setColorwaySeries("Series A");
        tagPrinting.setActive(true);
        tagPrinting.setColorwaySaleType("Online");
        tagPrinting.setSeasonBrand("Brand A");
        tagPrinting.setCollectionProdCategory("Category A");
        tagPrinting.setTheme("Theme A");
        tagPrinting.setStyle3rdCategory("Category 3");
        tagPrinting.setStyle2ndCategory("Category 2");
        tagPrinting.setSizeRangeCode("SR001");
        tagPrinting.setSizeRangeName("Size Range A");
        tagPrinting.setProductType("Type A");
        tagPrinting.setFirstProdCategory("Category 1");
        tagPrinting.setSizeRangeDimensionType("Type B");
        tagPrinting.setComposition("Cotton");
        tagPrinting.setCareSymbols("Wash with care");
        tagPrinting.setQualityClass("Class A");
        tagPrinting.setProductName("Product A");
        tagPrinting.setSaftyType("Type A");
        tagPrinting.setOpStandard("Standard A");
        tagPrinting.setApproved(true);
        tagPrinting.setAttention("Attention A");
        tagPrinting.setTechApproved(true);
        tagPrinting.setSaftyTitle("Title A");
        tagPrinting.setAppBomComment("BOM Comment A");
        tagPrinting.setSeriesDesc("Description A");
        tagPrinting.setFrontFabricColor("Red");
        tagPrinting.setBackFabricColor("Blue");
        tagPrinting.setAppHem("Hem A");
        tagPrinting.setAppRemark("Remark A");
        tagPrinting.setColorwayRuleName("Rule A");
        tagPrinting.setColorwayRuleValue("Value A");
        tagPrinting.setDyeingComposition("Cotton");
        tagPrinting.setTechType("Type A");
        tagPrinting.setSaftyClass("Class A");
        tagPrinting.setSeason("Summer");
        tagPrinting.setIsMainMaterial(true);
        tagPrinting.setAccessoriesColorCode("001");
        tagPrinting.setAccessoriesColorDescription("Red");
        tagPrinting.setStyleCodeEN("STYLE001");
        tagPrinting.setSizeIDEN("M");
        tagPrinting.setPromoType("Discount");
        tagPrinting.setPromoDesc("Promotion A");
        tagPrinting.setIsFirstBuy(true);
        tagPrinting.setIsSample(true);
        tagPrinting.setSampleCode("SAMPLE001");
        tagPrinting.setIsSampleCard(true);
        tagPrinting.setSampleCardCode("CARD001");
        tagPrinting.setSampleCardGrade("Grade A");

        // 设置尺码信息
        List<TagPrinting.Size> sizeInfo = new ArrayList<>();
        TagPrinting.Size size1 = new TagPrinting.Size();
        size1.setSizeCode("S");
        size1.setSortCode("1");
        size1.setSizeName("Small");
        size1.setSizeID("S001");
        size1.setExtSizeCode("ES001");
        size1.setShowIntSize("Yes");
        size1.setEuropeCode("EU001");
        size1.setSkuFiller("100g");
        size1.setSpecialSpec("None");
        sizeInfo.add(size1);

        TagPrinting.Size size2 = new TagPrinting.Size();
        size2.setSizeCode("M");
        size2.setSortCode("2");
        size2.setSizeName("Medium");
        size2.setSizeID("M001");
        size2.setExtSizeCode("EM001");
        size2.setShowIntSize("Yes");
        size2.setEuropeCode("EU002");
        size2.setSkuFiller("150g");
        size2.setSpecialSpec("None");
        sizeInfo.add(size2);
        tagPrinting.setSize(sizeInfo);


        TagPrinting tagPrinting2 = new TagPrinting();
        tagPrinting2.setColorwayWareCode("XYZ789");
        tagPrinting2.setIsGift("Yes");
        tagPrinting2.setColorDescription("Blue");
        tagPrinting2.setColorCode("002");
        tagPrinting2.setStyleCode("STYLE002");
        tagPrinting2.setColorwayBatchNo("BATCH002");
        tagPrinting2.setMerchApproved(true);
        tagPrinting2.setSecCode("SEC002");
        tagPrinting2.setMainCode("MAIN002");
        tagPrinting2.setColorwaySalesPrice("49.99");
        tagPrinting2.setIsAccessories(true);
        tagPrinting2.setColorwaySeries("Series B");
        tagPrinting2.setActive(true);
        tagPrinting2.setColorwaySaleType("Offline");
        tagPrinting2.setSeasonBrand("Brand B");
        tagPrinting2.setCollectionProdCategory("Category B");
        tagPrinting2.setTheme("Theme B");
        tagPrinting2.setStyle3rdCategory("Category 4");
        tagPrinting2.setStyle2ndCategory("Category 3");
        tagPrinting2.setSizeRangeCode("SR002");
        tagPrinting2.setSizeRangeName("Size Range B");
        tagPrinting2.setProductType("Type B");
        tagPrinting2.setFirstProdCategory("Category 2");
        tagPrinting2.setSizeRangeDimensionType("Type C");
        tagPrinting2.setComposition("Polyester");
        tagPrinting2.setCareSymbols("Dry clean only");
        tagPrinting2.setQualityClass("Class B");
        tagPrinting2.setProductName("Product B");
        tagPrinting2.setSaftyType("Type B");
        tagPrinting2.setOpStandard("Standard B");
        tagPrinting2.setApproved(true);
        tagPrinting2.setAttention("Attention B");
        tagPrinting2.setTechApproved(true);
        tagPrinting2.setSaftyTitle("Title B");
        tagPrinting2.setAppBomComment("BOM Comment B");
        tagPrinting2.setSeriesDesc("Description B");
        tagPrinting2.setFrontFabricColor("Blue");
        tagPrinting2.setBackFabricColor("Black");
        tagPrinting2.setAppHem("Hem B");
        tagPrinting2.setAppRemark("Remark B");
        tagPrinting2.setColorwayRuleName("Rule B");
        tagPrinting2.setColorwayRuleValue("Value B");
        tagPrinting2.setDyeingComposition("Polyester");
        tagPrinting2.setTechType("Type B");
        tagPrinting2.setSaftyClass("Class B");
        tagPrinting2.setSeason("Winter");
        tagPrinting2.setIsMainMaterial(true);
        tagPrinting2.setAccessoriesColorCode("002");
        tagPrinting2.setAccessoriesColorDescription("Blue");
        tagPrinting2.setStyleCodeEN("STYLE002");
        tagPrinting2.setSizeIDEN("L");
        tagPrinting2.setPromoType("Free gift");
        tagPrinting2.setPromoDesc("Promotion B");
        tagPrinting2.setIsFirstBuy(true);
        tagPrinting2.setIsSample(true);
        tagPrinting2.setSampleCode("SAMPLE002");
        tagPrinting2.setIsSampleCard(true);
        tagPrinting2.setSampleCardCode("CARD002");
        tagPrinting2.setSampleCardGrade("Grade B");

        // 设置尺码信息
        List<TagPrinting.Size> sizeInfo2 = new ArrayList<>();
        TagPrinting.Size size3 = new TagPrinting.Size();
        size1.setSizeCode("L");
        size1.setSortCode("3");
        size1.setSizeName("Large");
        size1.setSizeID("L002");
        size1.setExtSizeCode("EL002");
        size1.setShowIntSize("Yes");
        size1.setEuropeCode("EU003");
        size1.setSkuFiller("200g");
        size1.setSpecialSpec("None");
        sizeInfo2.add(size3);

        TagPrinting.Size size4 = new TagPrinting.Size();
        size2.setSizeCode("XL");
        size2.setSortCode("4");
        size2.setSizeName("Extra Large");
        size2.setSizeID("XL002");
        size2.setExtSizeCode("EXL002");
        size2.setShowIntSize("Yes");
        size2.setEuropeCode("EU004");
        size2.setSkuFiller("250g");
        size2.setSpecialSpec("None");
        sizeInfo2.add(size4);

        tagPrinting2.setSize(sizeInfo2);

        tagPrintings.add(tagPrinting);
        tagPrintings.add(tagPrinting2);
        return selectSuccess(tagPrintings);
    }
}
