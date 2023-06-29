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
import com.base.sbc.open.entity.SmpDept;
import com.base.sbc.open.entity.SmpPost;
import com.base.sbc.open.entity.SmpUser;
import com.base.sbc.open.service.MtBqReqService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
        SmpUser smpUser = JSONObject.parseObject(jsonObject.toJSONString(), SmpUser.class);
        smpUser.preInsert();
        smpUser.setCreateName("smp请求");
        smpUser.setUpdateName("smp请求");
        smpUser.setCompanyCode(smpUser.getCompanyId());
        amcService.hrUserSave(smpUser);
        return insertSuccess(null);
    }

    /**
     * hr-部门
     */
    @PostMapping("/hrDeptSave")
    @ApiOperation(value = "hr-部门新增或者修改", notes = "hr-部门新增或者修改")
    public ApiResult hrDeptSave(@RequestBody JSONObject jsonObject) {
        SmpDept smpDept = JSONObject.parseObject(jsonObject.toJSONString(), SmpDept.class);
        smpDept.preInsert();
        smpDept.setCreateName("smp请求");
        smpDept.setUpdateName("smp请求");
        amcService.hrDeptSave(smpDept);
        return insertSuccess(null);
    }

    /**
     * hr-岗位
     */
    @PostMapping("/hrPostSave")
    @ApiOperation(value = "hr-岗位新增或者修改", notes = "hr-岗位新增或者修改")
    public ApiResult hrPostSave(@RequestBody JSONObject jsonObject) {
        SmpPost smpPost = JSONObject.parseObject(jsonObject.toJSONString(), SmpPost.class);
        amcService.hrPostSave(smpPost);
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
        TagPrinting tagPrinting = new TagPrinting();

        tagPrinting.setC8_Colorway_WareCode("ABC123");
        tagPrinting.setIsGift(false);
        tagPrinting.setColorDescription("Blue");
        tagPrinting.setColorCode("001");
        tagPrinting.setStyleCode("12345");
        tagPrinting.setC8_Colorway_BatchNo("BATCH001");
        tagPrinting.setMerchApproved(true);
        tagPrinting.setSecCode("SEC001");
        tagPrinting.setMainCode("MAIN001");
        tagPrinting.setC8_Colorway_SalesPrice("49.99");
        tagPrinting.setIsAccessories(false);
        tagPrinting.setC8_Colorway_Series("Series A");
        tagPrinting.setActive(true);
        tagPrinting.setC8_Colorway_SaleType("Retail");
        tagPrinting.setC8_Season_Brand("Brand X");
        tagPrinting.setC8_Collection_ProdCategory("123");
        tagPrinting.setTheme("Summer");
        tagPrinting.setC8_Style_3rdCategory("456");
        tagPrinting.setC8_Style_2ndCategory("789");
        tagPrinting.setSizeRangeCode("XL");
        tagPrinting.setSizeRangeName("Extra Large");
        tagPrinting.setProductType("T-Shirt");
        tagPrinting.setC8_1stProdCategory("Clothing");
        tagPrinting.setSizeRangeDimensionType("Dimensions");
        tagPrinting.setComposition("Cotton");
        tagPrinting.setCareSymbols("Wash with cold water");
        tagPrinting.setQualityClass("Class A");
        tagPrinting.setProductName("T-Shirt");
        tagPrinting.setSaftyType("Type 1");
        tagPrinting.setOPStandard("Standard X");
        tagPrinting.setApproved(true);
        tagPrinting.setAttention("Handle with care");
        tagPrinting.setTechApproved(true);
        tagPrinting.setSaftyTitle("Safety First");
        tagPrinting.setC8_APPBOM_Comment("Handle with care");
        tagPrinting.setStorageRequirement("Dry place");

// 创建尺码明细对象
        TagPrinting.Size size = new TagPrinting.Size();
        size.setSIZECODE("XXXL");
        size.setSORTCODE("0");
        size.setSIZENAME("175/80A");
        size.setSizeID("10270");
        size.setEXTSIZECODE("");
        size.setShowIntSize(true);
        size.setEuropeCode("");
        size.setSKUFiller("");
        size.setSpecialSpec("");

// 设置尺码明细
        ArrayList<TagPrinting.Size> sizes =new ArrayList<>();
        tagPrinting.setSize(sizes);
        ArrayList<TagPrinting> tagPrintings = new ArrayList<>();
        tagPrintings.add(tagPrinting);
        return selectSuccess(tagPrintings);
    }
}
