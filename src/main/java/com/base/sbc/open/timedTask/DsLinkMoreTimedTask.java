package com.base.sbc.open.timedTask;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.open.dto.OpenMaterialDto;
import com.base.sbc.open.dto.OpenStyleBomDto;
import com.base.sbc.open.dto.OpenStyleDto;
import com.base.sbc.open.service.OpenMaterialService;
import com.base.sbc.open.thirdToken.DsLinkMoreScm;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/18 10:58
 */
@EnableScheduling
@Component
public class DsLinkMoreTimedTask {

    protected final Logger logger = LoggerFactory.getLogger(DsLinkMoreTimedTask.class);

    @Autowired
    private OpenMaterialService materialService;
    @Autowired
    private PackInfoService packInfoService;
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
    private PackPricingProcessCostsService packPricingProcessCostsService;
    @Autowired
    private DsLinkMoreScm linkMoreScm;

    /**
     * 物料定时同步到领猫scm
     */
//    @Scheduled(cron = "0 0 * * * ?")
    public void materialTask(){
        List<OpenMaterialDto> purchaseMaterialList = materialService.getMaterialList("677447590605750272");
        logger.info("需要同步的物料 :{}", JSON.toJSONString(purchaseMaterialList));

        List<String> errorList = new ArrayList<>();
        for (OpenMaterialDto materialDto : purchaseMaterialList) {
            HttpResponse authTokenOrSign = linkMoreScm.sendToLinkMore("v1/BaseInfo/updatematerial", JSON.toJSONString(materialDto));
            String body = authTokenOrSign.body();
            logger.info(materialDto.getMtCode() + "物料上传领猫返回信息 :{}", body);
            errorList = linkMoreScm.checkAndReturn(errorList,body,materialDto.getMtCode());
        }
        if (errorList.size() > 0){
            logger.info("同步失败的物料 :{}", StringUtils.convertListToString(errorList));
        }
    }

    /**
     * 款式（基础信息）定时同步领猫scm
     */
//    @Scheduled(cron = "0 0 * * * ?")
    public void styleTask(){
        List<OpenStyleDto> styleDtoList = packInfoService.getStyleListForLinkMore("677447590605750272");

        List<String> errorList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        Map<String,OpenStyleDto> idMap = new HashMap<>();
        String id;
        Map<String, Map<String, String>> allSizeMap = new HashMap<>();
        for (OpenStyleDto style : styleDtoList) {
            id = style.getId();
            allSizeMap.put(style.getCode(),style.getSizeMap());
            style.setId(null);
            style.setSizeMap(null);
            upToLinkMore(JSONObject.toJSONString(style),
                    style.getCode(),
                    "v1/product/updateproduct",
                    "款式基础信息资料包",
                    errorList);

            if (errorList.size() == 0){
                idList.add(id);
                idMap.put(id,style);
            }else if (!errorList.get(errorList.size()-1).equals(style.getCode())){
                idList.add(id);
                idMap.put(id,style);
            }
        }
        if (errorList.size() > 0){
            logger.info("同步失败的款式 :{}", StringUtils.convertListToString(errorList));
        }
        if (idList.size() > 0){
            styleBomTask(idList,idMap,allSizeMap);
        }
    }

    /**
     * 款式资料包（Bom）同步领猫scm-推送款式时同步推送资料包，每次款式修改后都重新推送
     * @param idList        款式id集合
     * @param idMap         款式map
     * @param allSizeMap    款式尺码编码map
     */
    public void styleBomTask(List<String> idList,
                             Map<String,OpenStyleDto> idMap,
                             Map<String, Map<String, String>> allSizeMap){
        /*1.统一查询*/
        String packType = "packBigGoods";
        //图片附件表
        QueryWrapper<Attachment> attachmentQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(attachmentQw,null,idList,null);
        attachmentQw.eq("type","packBigGoods-图样附件");
        Map<String, List<Attachment>> attachmentMap = new HashMap<>();
        List<String> attachmentIds = new ArrayList<>();
        for (Attachment attachment : attachmentService.list(attachmentQw)) {
            attachmentIds.add(attachment.getFileId());
            if (attachmentMap.get(attachment.getForeignId()) != null){
                attachmentMap.get(attachment.getForeignId()).add(attachment);
            }else{
                List<Attachment> list = new ArrayList<>();
                list.add(attachment);
                attachmentMap.put(attachment.getForeignId(),list);
            }
        }
        Map<String, String> fileMap = new HashMap<>();
        if (attachmentIds.size()>0){
            fileMap = uploadFileService.listByIds(attachmentIds)
                    .stream().collect(Collectors.toMap(f -> f.getId(), f -> f.getUrl(), (f1, f2) -> f2));

        }
        //尺寸表
        QueryWrapper<PackSize> sizeQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(sizeQw,null,idList,packType);
        Map<String, List<PackSize>> sizeMap = packSizeService.list(sizeQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));

        //物料清单
        QueryWrapper<PackBom> bomQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(bomQw,null,idList,packType);
        Map<String, List<PackBom>> bomMap = new HashMap<>();
        List<String> bomCodes = new ArrayList<>();
        for (PackBom bom : packBomService.list(bomQw)) {
            bomCodes.add(bom.getMaterialCode());
            if (bomMap.get(bom.getForeignId()) != null){
                bomMap.get(bom.getForeignId()).add(bom);
            }else{
                List<PackBom> list = new ArrayList<>();
                list.add(bom);
                bomMap.put(bom.getForeignId(),list);
            }
        }
        //物料配色
        QueryWrapper<PackBomColor> bomColorQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(bomColorQw,null,idList,packType);
        Map<String, List<PackBomColor>> bomColorMap = packBomColorService.list(bomColorQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));
        //物料配码
        QueryWrapper<PackBomSize> bomSizeQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(bomSizeQw,null,idList,packType);
        Map<String, List<PackBomSize>> bomSizeMap = packBomSizeService.list(bomSizeQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));
        //物料档案
        Map<String, BasicsdatumMaterial> materialMap = new HashMap<>();
        if (bomCodes.size()>0){
            QueryWrapper<BasicsdatumMaterial> materialQW = new QueryWrapper<>();
            materialQW.in("material_code",bomCodes);
            materialMap = basicsdatumMaterialService.list(materialQW)
                    .stream().collect(Collectors.toMap(p -> p.getMaterialCode(), p -> p, (p1, p2) -> p2));
        }


        //工艺说明
        QueryWrapper<PackTechSpec> packTechSpecQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(packTechSpecQw,null,idList,packType);
        Map<String, List<PackTechSpec>> packTechSpecMap = packTechSpecService.list(packTechSpecQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));

        //工序工价
        QueryWrapper<PackProcessPrice> packProcessPriceQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(packProcessPriceQw,null,idList,packType);
        Map<String, List<PackProcessPrice>> packProcessPriceMap = packProcessPriceService.list(packProcessPriceQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));

        //核价信息
        QueryWrapper<PackPricingProcessCosts> processCostsQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(processCostsQw,null,idList,packType);
        QueryWrapper<PackPricingOtherCosts> otherCostsQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(otherCostsQw,null,idList,packType);
        QueryWrapper<PackPricing> costQw = new QueryWrapper<>();
        linkMoreScm.openCommonQw(costQw,null,idList,packType);
        Map<String, List<PackPricingProcessCosts>> processCostsMap = packPricingProcessCostsService.list(processCostsQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));
        Map<String,List<PackPricingOtherCosts>> otherCostsMap = packPricingOtherCostsService.list(otherCostsQw)
                .stream().collect(Collectors.groupingBy(p -> p.getForeignId()));
        Map<String, List<PackPricing>> packPricingMap = new HashMap<>();

        List<PackPricing> pricingList = packPricingService.list(costQw);
        List<String> colorList = new ArrayList<>();
        for (PackPricing p : pricingList) {
            colorList.add(p.getColorCode());
            if (packPricingMap.get(p.getForeignId()) != null){
                packPricingMap.get(p.getForeignId()).add(p);
            }else{
                List<PackPricing> list = new ArrayList<>();
                list.add(p);
                packPricingMap.put(p.getForeignId(),list);
            }
        }
        //查询每一个对应的物料
        PackCommonPageSearchDto searchDto = new PackCommonPageSearchDto();
        searchDto.setForeignId(StringUtils.convertListToString(idList));
        searchDto.setColorCode(StringUtils.convertListToString(colorList));
        searchDto.setPackType("packBigGoods");
        Map<String, List<PackBomVo>> costBomMap = packBomService.getPackBomVoList(searchDto).stream().collect(Collectors.groupingBy(p -> p.getForeignId()));

        /*2.统一组装*/
        List<OpenStyleBomDto> styleBomDtoList = new ArrayList<>();

        for (String s : idList) {
            OpenStyleBomDto dto = new OpenStyleBomDto();
            dto.setId(s);
            dto.setCode(idMap.get(s).getCode());

            //图片附件表
            if (attachmentMap.get(s) != null){
                dto.initAttachmentList(attachmentMap.get(s),fileMap);
            }
            //尺寸表
            if (sizeMap.get(s) != null){
                dto.initSizeList(sizeMap.get(s),allSizeMap.get(dto.getCode()));
            }
            //物料清单
            if (bomMap.get(s) != null){
                dto.initBomList(bomMap.get(s),bomColorMap.get(s),bomSizeMap.get(s),materialMap,allSizeMap.get(dto.getCode()));
            }
            //工艺说明
            if (packTechSpecMap.get(s) != null){
                dto.initTechSpecList(packTechSpecMap.get(s));
            }
            //工序工价
            if (packProcessPriceMap.get(s) != null){
                dto.initPackProcessPriceList(packProcessPriceMap.get(s));
            }
            //核价信息主表
            if (packPricingMap.get(s) != null){
                dto.initCostList(packPricingMap.get(s),otherCostsMap.get(s),costBomMap.get(s),materialMap,processCostsMap.get(s));
            }
            styleBomDtoList.add(dto);
        }

        /*3.统一上传*/
//        logger.info("需要同步的款式资料包(BOM)信息 :{}", JSON.toJSONString(styleBomDtoList));
        //图片附件表
        List<String> errorAttachmentList = new ArrayList<>();
        //尺寸表
        List<String> errorSizeList = new ArrayList<>();
        //物料清单
        List<String> errorBomList = new ArrayList<>();
        //物料清单
        List<String> errorPackTechSpecList = new ArrayList<>();
        //工序工价
        List<String> errorProcessPriceList = new ArrayList<>();
        //工序工价
        List<String> errorCostList = new ArrayList<>();
        for (OpenStyleBomDto style : styleBomDtoList) {
            //图片附件表
            upToLinkMore(JSONObject.toJSONString(style.getAttachment()),
                    style.getCode(),
                    "v1/product/standardbom/updateimgfiles",
                    "款式资料包(图片附件表)",
                    errorAttachmentList);

            //尺寸表
            upToLinkMore(JSONObject.toJSONString(style.getPackSize()),
                    style.getCode(),
                    "v1/product/standardbom/updatesizeguides",
                    "款式资料包(尺寸表)",
                    errorSizeList);

            //物料清单
            upToLinkMore(JSONObject.toJSONString(style.getPackBom()),
                    style.getCode(),
                    "v1/product/standardbom/updatematerials",
                    "款式资料包(物料清单)",
                    errorBomList);

            //工艺说明
            upToLinkMore(JSONObject.toJSONString(style.getPackTechSpec()),
                    style.getCode(),
                    "v1/product/standardbom/updateprocguides",
                    "款式资料包(工艺说明)",
                    errorPackTechSpecList);

            //工序工价
            upToLinkMore(JSONObject.toJSONString(style.getPackProcess()),
                    style.getCode(),
                    "v1/product/standardbom/updateprocedures",
                    "款式资料包(工序工价)",
                    errorProcessPriceList);

            //核价信息
            upToLinkMore(JSONObject.toJSONString(style.getPackCost()),
                    style.getCode(),
                    "v1/product/standardbom/updatepkgfees",
                    "款式资料包(核价信息)",
                    errorCostList);

        }


        /*4.错误日志打印*/
        if (errorAttachmentList.size() > 0){
            logger.info("同步失败的款式图片附件表 :{}", StringUtils.convertListToString(errorAttachmentList));
        }
        if (errorSizeList.size() > 0){
            logger.info("同步失败的款式尺寸表 :{}", StringUtils.convertListToString(errorSizeList));
        }
        if (errorBomList.size() > 0){
            logger.info("同步失败的款式物料清单 :{}", StringUtils.convertListToString(errorBomList));
        }
        if (errorPackTechSpecList.size() > 0){
            logger.info("同步失败的款式工艺说明 :{}", StringUtils.convertListToString(errorPackTechSpecList));
        }
        if (errorProcessPriceList.size() > 0){
            logger.info("同步失败的款式工序工价 :{}", StringUtils.convertListToString(errorProcessPriceList));
        }
        if (errorCostList.size() > 0){
            logger.info("同步失败的款式核价信息 :{}", StringUtils.convertListToString(errorCostList));
        }
    }

    /**
     * 上传领猫并打印日志
     * @param str               上传json
     * @param id                款式id
     * @param url               上传地址
     * @param msg               日志信息
     * @param errorList         错误信息
     * @return  errorList       错误信息
     */
    public List<String> upToLinkMore(String str, String id, String url, String msg, List<String> errorList){
        logger.info(id + msg +"上传领猫上传信息 :{}", str);
        HttpResponse response = linkMoreScm.sendToLinkMore(url, str);
        String body = response.body();
        logger.info(id + msg +"上传领猫返回信息 :{}", body);
        return linkMoreScm.checkAndReturn(errorList,body,id);
    }

}
