package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.hangTag.service.HangTagService;
import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.open.dto.MtBpReqDto;
import com.base.sbc.open.dto.SmpOpenMaterialDto;
import com.base.sbc.open.entity.*;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
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
public class OpenSmpController extends BaseController {

    private final MtBqReqService mtBqReqService;

    private final BasicsdatumSupplierService basicsdatumSupplierService;

    private final AmcService amcService;

    private final HangTagService hangTagService;

    private final BasicsdatumMaterialService basicsdatumMaterialService;

    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    private final BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    private final BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;

    private final EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;

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
     * smp-物料
     */
    @PostMapping("/smpMaterial")
    @ApiOperation(value = "smp-物料", notes = "smp-物料")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult smpMaterial(@RequestBody JSONObject jsonObject) {
        //初步逻辑：关联编码的，先去查询编码是否存在，如果不存在则返回错误，字段不存在。
        SmpOpenMaterialDto smpOpenMaterialDto = jsonObject.toJavaObject(SmpOpenMaterialDto.class);

        JSONObject images = jsonObject.getJSONObject("images");
        if (images!=null){
            JSONArray imagesChildren = images.getJSONArray("imagesChildren");
            if (imagesChildren!=null){
                List<String> list =new ArrayList<>();
                for (Object imagesChild : imagesChildren) {
                    JSONObject imagesChild1 = (JSONObject) JSONObject.toJSON(imagesChild);
                    String string = imagesChild1.getString("filename");
                    list.add(string);
                }
                smpOpenMaterialDto.setImages(list);
            }
        }

        JSONObject quotItems = jsonObject.getJSONObject("quotItems");
        if (quotItems != null) {
            JSONArray quotItemsChildren = quotItems.getJSONArray("quotItemsChildren");
            if (quotItemsChildren != null) {
                List<SmpOpenMaterialDto.QuotItem> list = new ArrayList<>();
                for (Object quotItemsChild: quotItemsChildren) {
                    JSONObject json = (JSONObject) JSON.toJSON(quotItemsChild);
                    SmpOpenMaterialDto.QuotItem quotItem = JSON.toJavaObject(json, SmpOpenMaterialDto.QuotItem.class);
                    quotItem.setC8_SupplierItemRev_MLeadTime(json.getBigDecimal("c8SupplierItemRevMLeadTime"));
                    quotItem.setLeadTime(json.getBigDecimal("leadTime"));

                    list.add(quotItem);
                }
                smpOpenMaterialDto.setQuotItems(list);
            }
        }
        JSONObject mODELITEMS = jsonObject.getJSONObject("mODELITEMS");
        if (mODELITEMS != null) {
            JSONArray mODELITEMSNANAnnotation = mODELITEMS.getJSONArray("mODELITEMSChildren");
            if (mODELITEMSNANAnnotation != null) {
                List<SmpOpenMaterialDto.ModelItem> list = new ArrayList<>();
                for (Object object : mODELITEMSNANAnnotation) {
                    JSONObject json = (JSONObject) JSON.toJSON(object);
                    SmpOpenMaterialDto.ModelItem modelItem = JSON.toJavaObject(json, SmpOpenMaterialDto.ModelItem.class);
                    list.add(modelItem);
                }
                smpOpenMaterialDto.setMODELITEMS(list);
            }
        }


        JSONObject cOLORITEMS = jsonObject.getJSONObject("cOLORITEMS");
         if (cOLORITEMS != null) {
             JSONArray cOLORITEMSNANAnnotation = cOLORITEMS.getJSONArray("cOLORITEMSChildren");
             if (cOLORITEMSNANAnnotation != null) {
                 List<SmpOpenMaterialDto.ColorItem> list = new ArrayList<>();
                 for (Object object : cOLORITEMSNANAnnotation) {
                     JSONObject json = (JSONObject) JSON.toJSON(object);
                     SmpOpenMaterialDto.ColorItem colorItem = JSON.toJavaObject(json, SmpOpenMaterialDto.ColorItem.class);
                     colorItem.setSmpColor(json.getString("supplierColorNo"));
                     list.add(colorItem);
                 }
                 smpOpenMaterialDto.setCOLORITEMS(list);
             }

         }

        BasicsdatumMaterial basicsdatumMaterial = smpOpenMaterialDto.toBasicsdatumMaterial();
            basicsdatumMaterial.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);

        //if (true){
        //    return null;
        //}
        basicsdatumMaterialService.saveOrUpdate(basicsdatumMaterial, new QueryWrapper<BasicsdatumMaterial>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        if (!smpOpenMaterialDto.getCOLORITEMS().isEmpty()) {
            List<BasicsdatumMaterialColor> basicsdatumMaterialColors = new ArrayList<>();
            //转成颜色集合
            smpOpenMaterialDto.getCOLORITEMS().forEach(colorItem -> {
                BasicsdatumMaterialColor basicsdatumMaterialColor = new BasicsdatumMaterialColor();
                basicsdatumMaterialColor.setColorCode(colorItem.getColorCode());
                basicsdatumMaterialColor.setStatus(colorItem.isActive() ? "0" : "1");
                basicsdatumMaterialColor.setSupplierColorCode(colorItem.getSmpColor());
                basicsdatumMaterialColor.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialColors.add(basicsdatumMaterialColor);
            });
            basicsdatumMaterialColorService.addAndUpdateAndDelList(basicsdatumMaterialColors, new QueryWrapper<BasicsdatumMaterialColor>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }

        if (!smpOpenMaterialDto.getMODELITEMS().isEmpty()) {
            List<BasicsdatumMaterialWidth> basicsdatumMaterialWidths = new ArrayList<>();
            smpOpenMaterialDto.getMODELITEMS().forEach(modelItem -> {
                BasicsdatumMaterialWidth basicsdatumMaterialWidth = new BasicsdatumMaterialWidth();
                basicsdatumMaterialWidth.setStatus(modelItem.isActive() ? "0" : "1");
                basicsdatumMaterialWidth.setWidthCode(modelItem.getCODE());
                basicsdatumMaterialWidth.setName(modelItem.getSIZENAME());
                basicsdatumMaterialWidth.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialWidths.add(basicsdatumMaterialWidth);
            });
            basicsdatumMaterialWidthService.addAndUpdateAndDelList(basicsdatumMaterialWidths, new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }

        if (!smpOpenMaterialDto.getQuotItems().isEmpty()) {
            List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = new ArrayList<>();
            smpOpenMaterialDto.getQuotItems().forEach(quotItem -> {
                BasicsdatumMaterialPrice basicsdatumMaterialPrice = new BasicsdatumMaterialPrice();
                basicsdatumMaterialPrice.setWidth(quotItem.getSUPPLIERSIZE());
                basicsdatumMaterialPrice.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialPrice.setSupplierId(quotItem.getSupplierCode());
                basicsdatumMaterialPrice.setSupplierName(quotItem.getSupplierName());
                basicsdatumMaterialPrice.setColor(quotItem.getSUPPLIERCOLORID());
                basicsdatumMaterialPrice.setQuotationPrice(quotItem.getFOBFullPrice());
                basicsdatumMaterialPrice.setOrderDay(quotItem.getLeadTime());
                basicsdatumMaterialPrice.setProductionDay(quotItem.getC8_SupplierItemRev_MLeadTime());
                basicsdatumMaterialPrice.setMinimumOrderQuantity(quotItem.getMOQInitial());
                basicsdatumMaterialPrice.setColorName(quotItem.getSUPPLIERCOLORNAME());
                basicsdatumMaterialPrice.setWidthName(quotItem.getSUPPLIERSIZE());
                basicsdatumMaterialPrice.setSupplierMaterialCode(quotItem.getSupplierMaterial());
                basicsdatumMaterialPrices.add(basicsdatumMaterialPrice);

            });
            basicsdatumMaterialPriceService.addAndUpdateAndDelList(basicsdatumMaterialPrices, new QueryWrapper<BasicsdatumMaterialPrice>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }

        return insertSuccess(null);
    }


    /**
     * 吊牌打印
     */
    @GetMapping("/TagPrinting")
    @ApiOperation(value = "吊牌打印获取", notes = "吊牌打印获取")
    public ApiResult tagPrinting(String id, Boolean bl) {
        List<TagPrinting> tagPrintings1 = hangTagService.hangTagPrinting(id, bl, super.getUserCompany());
        return selectSuccess(tagPrintings1);
    }


    /**
     * 面料成分检测数据接口
     */
    @GetMapping("/escmMaterialCompnentInspectCompany")
    public ApiResult EscmMaterialCompnentInspectCompanyDto(@RequestBody JSONObject jsonObject){
        EscmMaterialCompnentInspectCompanyDto escmMaterialCompnentInspectCompanyDto = jsonObject.toJavaObject(EscmMaterialCompnentInspectCompanyDto.class);
        escmMaterialCompnentInspectCompanyService.saveOrUpdate(escmMaterialCompnentInspectCompanyDto);
        return insertSuccess(null);
    }
}
