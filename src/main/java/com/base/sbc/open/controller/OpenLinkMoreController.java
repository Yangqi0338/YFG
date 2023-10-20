package com.base.sbc.open.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.purchase.service.DeliveryNoticeService;
import com.base.sbc.open.dto.OpenMaterialNoticeDto;
import com.base.sbc.open.dto.OpenStyleBomDto;
import com.base.sbc.open.dto.OpenStyleDto;
import com.base.sbc.open.service.OpenMaterialService;
import com.base.sbc.open.thirdtoken.DsLinkMoreScm;
import com.base.sbc.open.timedtask.DsLinkMoreTimedTask;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 领猫对接接口
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/15 10:06
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/openDs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OpenLinkMoreController extends BaseController{

    @Autowired
    private OpenMaterialService materialService;
    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private BasicsdatumSupplierService supplierService;
    @Autowired
    private DeliveryNoticeService deliveryNoticeService;

    @Autowired
    private PackSizeService packSizeService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private PackBomService packBomService;
    @Autowired
    private PackBomColorService packBomColorService;
    @Autowired
    private PackBomSizeService packBomSizeService;
    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;
    @Autowired
    private PackTechSpecService packTechSpecService;
    @Autowired
    private PackProcessPriceService packProcessPriceService;
    @Autowired
    private PackPricingService packPricingService;
    @Autowired
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Autowired
    private CcmService ccmService;
    @Autowired
    private DsLinkMoreScm linkMoreScm;
    @Autowired
    private DsLinkMoreTimedTask timedTask;

    @ApiOperation(value = "物料推送领猫scm测试接口(已通过测试)")
    @GetMapping("/getMaterialList")
    public ApiResult getMaterialList(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        timedTask.materialTask();
        return selectSuccess("ok");
    }

    @ApiOperation(value = "款式基础信息推送领猫scm测试接口(已通过测试)")
    @GetMapping("/getStyleListForLinkMore")
    public ApiResult getStyleListForLinkMore(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        List<OpenStyleDto> styleList = packInfoService.getStyleListForLinkMore("677447590605750272");
        if (styleList == null){
            return null;
        }
        for (OpenStyleDto dto : styleList) {
            dto.setDesigner("风音");
            String arges = JSON.toJSONString(dto);
            System.out.println(arges);
//            HttpResponse authTokenOrSign = linkMoreScm.sendToLinkMore("v1/product/updateproduct", arges);
//            String body = authTokenOrSign.body();
//            System.out.println("========================"+body);
        }
        return selectSuccess(styleList);
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口")
    @GetMapping("/getStyleBomForLinkMore")
    public ApiResult getStyleBomForLinkMore(@RequestHeader(BaseConstant.USER_COMPANY) String idStr) {
        timedTask.styleTask();
        return null;
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口-附件(已通过测试)")
    @GetMapping("/getStyleBomForLinkMoreFile")
    public ApiResult getStyleBomForLinkMoreFile(){
        //图片附件表
        QueryWrapper<Attachment> attachmentQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(attachmentQw,"1698881540366368768",null,null);
        attachmentQw.eq("type","packBigGoods-图样附件");
        List<Attachment> list = attachmentService.list(attachmentQw);
        List<String> ids = list.stream().map(a -> a.getFileId()).collect(Collectors.toList());
        List<UploadFile> fileList = uploadFileService.listByIds(ids);
        Map<String, String> fileMap = fileList.stream().collect(Collectors.toMap(f -> f.getId(), f -> f.getUrl(), (f1, f2) -> f2));
        OpenStyleBomDto bomDto = new OpenStyleBomDto();
        bomDto.initAttachmentList(list,fileMap);
//        for (OpenStyleBomDto.OpenAttachmentDto dto : bomDto.getAttachmentList()) {
//            dto.setCode("822310301");
//        }

        String args = JSON.toJSONString(bomDto.getAttachment());
        logger.info("款式资料包(工序工价)上传领猫上传信息 :{}", args);
        HttpResponse attachment = linkMoreScm.sendToLinkMore("v1/product/standardbom/updateimgfiles", args);
        logger.info("款式资料包(工序工价)上传领猫返回信息 :{}", attachment.body());
        return null;
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口-尺寸表(已通过测试)")
    @GetMapping("/getStyleBomForLinkMoreSize")
    public ApiResult getStyleBomForLinkMoreSize(){
        //尺寸表
        QueryWrapper<PackSize> sizeQw = new QueryWrapper<>();
//        linkMoreScm.openCommonQw(sizeQw,"1698881540366368768",null,"packBigGoods");
        linkMoreScm.openCommonQw(sizeQw,"1701774217148821504",null,"packBigGoods");
        List<PackSize> list = packSizeService.list(sizeQw);

        OpenStyleBomDto bomDto = new OpenStyleBomDto();
        bomDto.setCode("922410201");
        bomDto.initSizeList(list,null);

        String args =JSON.toJSONString(bomDto.getPackSize());

        logger.info("款式资料包(尺码表)上传领猫上传信息 :{}", args);
        HttpResponse attachment = linkMoreScm.sendToLinkMore("v1/product/standardbom/updatesizeguides", args);

        logger.info("款式资料包(尺码表)上传领猫返回信息 :{}", attachment.body());
        return null;
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口-物料清单(已通过测试)")
    @GetMapping("/getStyleBomForLinkMoreMaterial")
    public ApiResult getStyleBomForLinkMoreMaterial(){
        //物料清单
        QueryWrapper<PackBom> bomQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(bomQw,"1698881540366368768",null,"packBigGoods");
        List<PackBom> bomList = packBomService.list(bomQw);
        List<String> bomCodes = new ArrayList<>();
        for (PackBom bom : packBomService.list(bomQw)) {
            bomCodes.add(bom.getMaterialCode());
        }
        //物料档案
        QueryWrapper<BasicsdatumMaterial> materialQW = new QueryWrapper<>();
        materialQW.in("material_code",bomCodes);
        Map<String, BasicsdatumMaterial> materialMap = basicsdatumMaterialService.list(materialQW)
                .stream().collect(Collectors.toMap(p -> p.getMaterialCode(), p -> p, (p1, p2) -> p2));
        //物料配色
        QueryWrapper<PackBomColor> bomColorQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(bomColorQw,"1698881540366368768",null,"packBigGoods");
        Map<String, List<PackBomColor>> bomColorMap = packBomColorService.list(bomColorQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));
        //物料配码
        QueryWrapper<PackBomSize> bomSizeQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(bomSizeQw,"1698881540366368768",null,"packBigGoods");
        Map<String, List<PackBomSize>> bomSizeMap = packBomSizeService.list(bomSizeQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));

        OpenStyleBomDto bomDto = new OpenStyleBomDto();
        bomDto.initBomList(bomList,bomColorMap.get("1698881540366368768"),bomSizeMap.get("1698881540366368768"),materialMap, null);
        String args =JSON.toJSONString(bomDto.getPackBom());

        logger.info("款式资料包(物料表)上传领猫上传信息 :{}", args);
        HttpResponse attachment = linkMoreScm.sendToLinkMore("v1/product/standardbom/updatematerials", args);
        logger.info("款式资料包(物料表)上传领猫返回信息 :{}", attachment.body());
        return null;
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口-工艺说明(已通过测试)")
    @GetMapping("/getStyleBomForLinkMoreSay")
    public ApiResult getStyleBomForLinkMoreSay(){
        //工艺说明
        QueryWrapper<PackTechSpec> packTechSpecQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(packTechSpecQw,"1698881540366368768",null,"packBigGoods");
        List<PackTechSpec> packTechSpecList = packTechSpecService.list(packTechSpecQw);

        OpenStyleBomDto bomDto = new OpenStyleBomDto();
        bomDto.initTechSpecList(packTechSpecList);
        String args =JSON.toJSONString(bomDto.getPackTechSpec());

        logger.info("款式资料包(工艺说明表)上传领猫上传信息 :{}", args);
        HttpResponse attachment = linkMoreScm.sendToLinkMore("v1/product/standardbom/updateprocguides", args);
        logger.info("款式资料包(工艺说明表)上传领猫返回信息 :{}", attachment.body());
        return null;
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口-工序工价(已通过测试)")
    @GetMapping("/getStyleBomForLinkMorePrice")
    public ApiResult getStyleBomForLinkMorePrice(){
        //工序工价
        QueryWrapper<PackProcessPrice> packProcessPriceQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(packProcessPriceQw,"1698881540366368768",null,"packBigGoods");
        List<PackProcessPrice> priceList = packProcessPriceService.list(packProcessPriceQw);

        OpenStyleBomDto bomDto = new OpenStyleBomDto();
        bomDto.initPackProcessPriceList(priceList);
        String args =JSON.toJSONString(bomDto.getPackProcess());

        logger.info("款式资料包(工序工价)上传领猫上传信息 :{}", args);
        HttpResponse attachment = linkMoreScm.sendToLinkMore("v1/product/standardbom/updateprocedures", args);
        logger.info("款式资料包(工序工价)上传领猫返回信息 :{}", attachment.body());
        return null;
    }

    @ApiOperation(value = "款式资料包推送领猫scm测试接口-核价信息(------未通过测试)")
    @GetMapping("/getStyleBomForLinkMoreCost")
    public ApiResult getStyleBomForLinkMoreCost(){
        //核价信息
        QueryWrapper<PackPricingProcessCosts> processCostsQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(processCostsQw,"1701774217148821504",null,"packBigGoods");
        QueryWrapper<PackPricingOtherCosts> otherCostsQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(otherCostsQw,"1701774217148821504",null,"packBigGoods");
        otherCostsQw.eq("costs_item","其它费");
        QueryWrapper<PackPricing> costQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(costQw,"1701774217148821504",null,"packBigGoods");

        Map<String,List<PackPricingOtherCosts>> otherCostsMap = packPricingOtherCostsService.list(otherCostsQw)
                .stream().collect(Collectors.groupingBy(p -> p.getColorCode()));
        List<PackPricing> pricingList = packPricingService.list(costQw);
        if (CollectionUtil.isEmpty(pricingList)){
            return null;
        }
        //查询每一个对应的物料
        List<String> colorList = pricingList.stream().map(p -> p.getColorCode()).collect(Collectors.toList());
        PackCommonPageSearchDto dto = new PackCommonPageSearchDto();
        dto.setForeignId("1701774217148821504");
        dto.setColorCode(StringUtils.convertListToString(colorList));
        dto.setPackType("packBigGoods");
        List<PackBomVo> bomCostList = packBomService.getPackBomVoList(dto);

        OpenStyleBomDto bomDto = new OpenStyleBomDto();
//        bomDto.initCostList(pricingList,otherCostsMap,bomCostList);
        bomDto.initCostList(pricingList,null,bomCostList,null, null);

//        String args =JSON.toJSONString(bomDto.getCostList().get(0));

//        String ttt = "{\"operator\":\"\",\"extra\":\"\",\"isPush\":true,\"isNotice\":true,\"isForce\":true,\"code\":\"\",\"list\":\"123\"}";
////        logger.info("款式资料包(核价信息)上传领猫上传信息 :{}", args);
//        HttpResponse attachment = linkMoreScm.sendToLinkMore("v1/product/standardbom/updateprocguides", ttt);
//        logger.info("款式资料包(核价信息)上传领猫返回信息 :{}", attachment.body());
        return null;
    }

    @ApiOperation(value = "领猫scm推送供应商保存")
    @PostMapping("/saveSupplierLinkMore")
    public ApiResult saveSupplierLinkMore(@RequestHeader(BaseConstant.USER_COMPANY)String companyCode,@RequestBody List<AddRevampBasicsdatumSupplierDto> addDtoList){
        //校验数据 字典（结算方式、供应商类型、开发年份）
        String dictInfo = ccmService.getOpenDictInfo(companyCode, "C8_Year,TradeTerm,C8_Sync_DataStatus");
        JSONArray data = JSONObject.parseObject(dictInfo).getJSONArray("data");
        Map<String,Map<String,String>> dictMap = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject obj = data.getJSONObject(i);
            if (StringUtils.isNotBlank(obj.getString("value"))
                    && StringUtils.isNotBlank(obj.getString("type"))
                    && StringUtils.isNotBlank(obj.getString("name"))) {
                if (dictMap.get(obj.getString("type"))!=null){
                    dictMap.get(obj.getString("type")).put(obj.getString("name"),obj.getString("value"));
                }else{
                    Map<String,String> map = new HashMap<>();
                    map.put(obj.getString("name"),obj.getString("value"));
                    dictMap.put(obj.getString("type"),map);
                }
            }
        }
        //新增数据
        return supplierService.addSupplierBatch(addDtoList,dictMap);
    }

    @ApiOperation(value = "领猫scm推送送货通知单保存")
    @PostMapping("/saveMaterialNotice")
    public ApiResult saveMaterialNotice(@RequestBody List<OpenMaterialNoticeDto> noticeDtoList){
        return deliveryNoticeService.saveNoticeList(noticeDtoList);
    }
}
