package com.base.sbc.open.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPriceDetail;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.service.SpecificationService;
import com.base.sbc.open.dto.SmpOpenMaterialDto;
import com.base.sbc.open.dto.SmpOpenMaterialDto.QuotItem;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 卞康
 * @date 2023/7/23 14:30:40
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenSmpService {

	private final SpecificationService SpecificationService;

    private final BasicsdatumMaterialService basicsdatumMaterialService;

    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    private final BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    private final BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;

    private final BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;

    private final CcmService ccmService;
    private final BasicsdatumSupplierService basicsdatumSupplierService;

    @Value("${minio.endpoint}")
    private String url;


    @Transactional(rollbackFor = Exception.class)
    public void smpMaterial(JSONObject smpOpenMaterialDtoJson) {
        JSONObject jsonObject1 = smpOpenMaterialDtoJson.getJSONObject("params");
        JSONArray jSON = jsonObject1.getJSONArray("jSON");
        JSONObject jsonObject = jSON.getJSONObject(0);
        SmpOpenMaterialDto smpOpenMaterialDto = jsonObject.toJavaObject(SmpOpenMaterialDto.class);
        //初步逻辑：关联编码的，先去查询编码是否存在，如果不存在则返回错误，字段不存在。
        JSONObject images = jsonObject.getJSONObject("images");
        if (images != null) {
            JSONArray imagesChildren = images.getJSONArray("imagesChildren");
            if (imagesChildren != null) {
                List<String> list = new ArrayList<>();
                for (Object imagesChild : imagesChildren) {
                    JSONObject imagesChild1 = (JSONObject) JSONObject.toJSON(imagesChild);
                    String string = imagesChild1.getString("filename");

                    try {
                        //转换图片地址
                        //String objectName = System.currentTimeMillis() + "." + FileUtil.extName(string);
                        //String contentType = "image/jpeg";
                        //InputStream inputStream = OpenSmpFtpUtils.download(string);
                        //String s = minioUtils.uploadFile(inputStream, objectName, contentType);

                        String[] split = string.split("\\.");

                        list.add(url+"/pdm/Material/" +smpOpenMaterialDto.getC8_Season_Year()+"/"+smpOpenMaterialDto.getC8_Season_Quarter()+"/"+ smpOpenMaterialDto.getCode() + "." + split[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                smpOpenMaterialDto.setImages(list);
            }
        }
        //if (true){
        //    return;
        //}

        JSONObject quotItems = jsonObject.getJSONObject("quotItems");
        List<QuotItem> quotItemsList = new ArrayList<>();
        if (quotItems != null) {
            JSONArray quotItemsChildren = quotItems.getJSONArray("quotItemsChildren");
            if (quotItemsChildren != null) {

                for (Object quotItemsChild : quotItemsChildren) {
                    JSONObject json = (JSONObject) JSON.toJSON(quotItemsChild);
                    QuotItem quotItem = JSON.toJavaObject(json, QuotItem.class);
                    quotItem.setC8_SupplierItemRev_MLeadTime(json.getString("c8SupplierItemRevMLeadTime"));
                    quotItem.setLeadTime(json.getString("leadTime"));
                    quotItemsList.add(quotItem);
                }


                smpOpenMaterialDto.setQuotItems(quotItemsList);
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
                    list.add(colorItem);
                }
                smpOpenMaterialDto.setCOLORITEMS(list);
            }

        }

        BasicsdatumMaterial basicsdatumMaterial = smpOpenMaterialDto.toBasicsdatumMaterial();
        basicsdatumMaterial.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
        basicsdatumMaterial.setUpdateName("外部系统推送");
        basicsdatumMaterial.setDistribute("1");

        String c8ProcMode = ccmService.getOpenDictInfo(BaseConstant.DEF_COMPANY_CODE, "C8_ProcMode");
        JSONArray data = JSONObject.parseObject(c8ProcMode).getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject obj = data.getJSONObject(i);
            if (obj.getString("value").equals(basicsdatumMaterial.getProcMode())) {
                basicsdatumMaterial.setProcModeName(obj.getString("name"));
            }
        }


        String c8MaterialUom = ccmService.getOpenDictInfo(BaseConstant.DEF_COMPANY_CODE, "C8_Material_UOM");
        JSONArray data1 = JSONObject.parseObject(c8MaterialUom).getJSONArray("data");
        for (int i = 0; i < data1.size(); i++) {
            JSONObject obj = data1.getJSONObject(i);
            if (obj.getString("value").equals(basicsdatumMaterial.getStockUnitCode())) {
                basicsdatumMaterial.setStockUnitName(obj.getString("name"));
            }
        }


        String c8PickingMethod = ccmService.getOpenDictInfo(BaseConstant.DEF_COMPANY_CODE, "C8_PickingMethod");
        JSONArray data2 = JSONObject.parseObject(c8PickingMethod).getJSONArray("data");
        for (int i = 0; i < data2.size(); i++) {
            JSONObject obj = data2.getJSONObject(i);
            if (obj.getString("value").equals(basicsdatumMaterial.getPickingMethod())) {
                basicsdatumMaterial.setPickingMethodName(obj.getString("name"));
            }
        }


        try {
            String categorySByNameAndLevel = ccmService.getOpenCategorySByNameAndLevel("材料", smpOpenMaterialDto.getC8_Material_2ndCategory(), "1");
            String str2 = JSON.parseObject(categorySByNameAndLevel).getJSONArray("data").getJSONObject(0).getString("name");
            basicsdatumMaterial.setCategoryName(basicsdatumMaterial.getCategoryName() + "-" + str2);
            basicsdatumMaterial.setCategory2Name(str2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String categorySByNameAndLevel = ccmService.getOpenCategorySByNameAndLevel("材料", smpOpenMaterialDto.getC8_Material_3rdCategory(), "2");
            String str3 = JSON.parseObject(categorySByNameAndLevel).getJSONArray("data").getJSONObject(0).getString("name");
            basicsdatumMaterial.setCategoryName(basicsdatumMaterial.getCategoryName() + "-" + str3);
            basicsdatumMaterial.setCategory3Name(str3);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //    if (true){
        //        return ;
        //}


        if (!smpOpenMaterialDto.getCOLORITEMS().isEmpty()) {
            List<BasicsdatumMaterialColor> basicsdatumMaterialColors = new ArrayList<>();
            //转成颜色集合
            smpOpenMaterialDto.getCOLORITEMS().forEach(colorItem -> {
                BasicsdatumMaterialColor basicsdatumMaterialColor = new BasicsdatumMaterialColor();
                basicsdatumMaterialColor.setColorCode(colorItem.getColorCode());
                basicsdatumMaterialColor.setColorName(colorItem.getColorName());
                basicsdatumMaterialColor.setStatus(colorItem.isActive() ? "0" : "1");
                basicsdatumMaterialColor.setSupplierColorCode(colorItem.getMatColor4Supplier());
                basicsdatumMaterialColor.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialColor.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialColor.setUpdateName("外部系统推送");
                basicsdatumMaterialColor.setScmStatus("1");
                basicsdatumMaterialColors.add(basicsdatumMaterialColor);
            });
            basicsdatumMaterialColorService.remove(new QueryWrapper<BasicsdatumMaterialColor>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
            basicsdatumMaterialColorService.saveBatch(basicsdatumMaterialColors);

        }

        if (!smpOpenMaterialDto.getMODELITEMS().isEmpty()) {
			// 查询 规格信息 清理逻辑： name 原S修改为 165/80(S) 这种格式，按规格门幅配置通过
			// width_code（类似：C541077）获取，如果取到则覆盖
			List<Specification> list = SpecificationService
					.list(new QueryWrapper<Specification>().select("code,name,hangtags")
							.eq("company_code", BaseConstant.DEF_COMPANY_CODE));
			Map<String, String> map = list != null ? list.stream().filter(e -> StringUtils.isNotBlank(e.getCode()))
					.collect(Collectors.toMap(e -> e.getCode(), e -> e.getHangtags(), (e1, e2) -> e1)) : null;

            List<BasicsdatumMaterialWidth> basicsdatumMaterialWidths = new ArrayList<>();
            List<String> codes = new ArrayList<>();
            smpOpenMaterialDto.getMODELITEMS().forEach(modelItem -> {
                BasicsdatumMaterialWidth basicsdatumMaterialWidth = new BasicsdatumMaterialWidth();
				basicsdatumMaterialWidth.setSortCode(modelItem.getSORTCODE());
				basicsdatumMaterialWidth.setSizeName(modelItem.getSIZENAME());
				basicsdatumMaterialWidth.setCode(modelItem.getCODE());
                basicsdatumMaterialWidth.setWidthCode(modelItem.getSizeURL());
                //basicsdatumMaterialWidth.setName(modelItem.getSIZECODE().replaceAll(",", "<->"));
                //不用替换，影响接口下发下游
                basicsdatumMaterialWidth.setName(modelItem.getSIZECODE());
				basicsdatumMaterialWidth.setStatus(modelItem.isActive() ? "0" : "1");
                basicsdatumMaterialWidth.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialWidth.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialWidth.setScmStatus("1");
                basicsdatumMaterialWidth.setUpdateName("外部系统推送");

				// 清理逻辑： name 原S修改为 165/80(S) 这种格式，按规格门幅配置通过 width_code（类似：C541077）获取，如果取到则覆盖
				if (map != null && map.containsKey(basicsdatumMaterialWidth.getWidthCode())) {
					String name = map.get(basicsdatumMaterialWidth.getWidthCode());
					basicsdatumMaterialWidth.setName(name);
				}

                basicsdatumMaterialWidths.add(basicsdatumMaterialWidth);
                codes.add(basicsdatumMaterialWidth.getWidthCode());
            });

            //List<SpecificationGroup> specifications = specificationGroupService.list(new QueryWrapper<SpecificationGroup>().eq("specification_ids", String.join(",", codes)));
            //if (specifications != null && specifications.size() > 0) {
            //    basicsdatumMaterial.setWidthGroup(specifications.get(0).getCode());
            //    basicsdatumMaterial.setWidthGroupName(specifications.get(0).getName());
            //}
            basicsdatumMaterialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
            basicsdatumMaterialWidthService.saveBatch(basicsdatumMaterialWidths);

        }

        if (!smpOpenMaterialDto.getQuotItems().isEmpty()) {
            List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = new ArrayList<>();
            AtomicInteger index = new AtomicInteger();
            smpOpenMaterialDto.getQuotItems().forEach(quotItem -> {
                BasicsdatumMaterialPrice basicsdatumMaterialPrice = new BasicsdatumMaterialPrice();

                basicsdatumMaterialPrice.setWidthName(quotItem.getSUPPLIERSIZE());

                if (StringUtils.isEmpty(quotItem.getMat_SizeURL()) && !smpOpenMaterialDto.getMODELITEMS().isEmpty()) {
                    for (SmpOpenMaterialDto.ModelItem modelitem : smpOpenMaterialDto.getMODELITEMS()) {
                        if (basicsdatumMaterialPrice.getWidthName().equals(modelitem.getSIZECODE())) {
                            basicsdatumMaterialPrice.setWidth(modelitem.getSizeURL());
                        }
                    }
                } else {
                    basicsdatumMaterialPrice.setWidth(quotItem.getMat_SizeURL());
                }


                basicsdatumMaterialPrice.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialPrice.setSupplierId(quotItem.getSupplierCode());
                BasicsdatumSupplier basicsdatumSupplier = basicsdatumSupplierService.getOne(new QueryWrapper<BasicsdatumSupplier>().eq("supplier_code", quotItem.getSupplierCode()));
                if (basicsdatumSupplier == null) {
                    throw new RuntimeException("供应商编码:" + quotItem.getSupplierCode() + ",不存在");
                }
                basicsdatumMaterialPrice.setSupplierName(basicsdatumSupplier.getSupplier());
                basicsdatumMaterialPrice.setColor(quotItem.getSUPPLIERCOLORID());
                basicsdatumMaterialPrice.setQuotationPrice(quotItem.getFOBFullPrice());
                basicsdatumMaterialPrice.setOrderDay(quotItem.getLeadTime());
                basicsdatumMaterialPrice.setProductionDay(quotItem.getC8_SupplierItemRev_MLeadTime());
                basicsdatumMaterialPrice.setMinimumOrderQuantity(quotItem.getMOQInitial());
                basicsdatumMaterialPrice.setColorName(quotItem.getSUPPLIERCOLORNAME());

                basicsdatumMaterialPrice.setSupplierMaterialCode(quotItem.getSupplierMaterial());
                basicsdatumMaterialPrice.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialPrice.setSelectFlag(quotItem.getDefaultQuote());
                basicsdatumMaterialPrice.setUpdateName("外部系统推送");
                basicsdatumMaterialPrice.setRemarks(quotItem.getComment());
                basicsdatumMaterialPrice.setIndex(String.valueOf(index.get()));
                basicsdatumMaterialPrice.setScmStatus("1");
                basicsdatumMaterialPrices.add(basicsdatumMaterialPrice);
                index.getAndIncrement();


                if (quotItem.getDefaultQuote()) {
                    basicsdatumMaterial.setSupplierName(basicsdatumSupplier.getSupplier());
                    basicsdatumMaterial.setSupplierId(quotItem.getSupplierCode());
                    basicsdatumMaterial.setSupplierFabricCode(quotItem.getSupplierMaterial());
                    basicsdatumMaterial.setSupplierQuotationPrice(quotItem.getFOBFullPrice());
                }

            });

            List<BasicsdatumMaterialPrice> basicsdatumMaterialPriceList = BeanUtil.copyToList(basicsdatumMaterialPrices, BasicsdatumMaterialPrice.class);
            List<BasicsdatumMaterialPrice> list = this.merge(basicsdatumMaterialPrices);

            basicsdatumMaterialPriceService.remove(new QueryWrapper<BasicsdatumMaterialPrice>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
            basicsdatumMaterialPriceService.saveBatch(list);
            List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialPriceDetails = new ArrayList<>();
            for (BasicsdatumMaterialPrice basicsdatumMaterialPrice : list) {

                for (BasicsdatumMaterialPrice materialPrice : basicsdatumMaterialPriceList) {
                    if (basicsdatumMaterialPrice.getIndexList().contains(materialPrice.getIndex())) {
                        BasicsdatumMaterialPriceDetail basicsdatumMaterialPriceDetail = new BasicsdatumMaterialPriceDetail();
                        BeanUtil.copyProperties(materialPrice, basicsdatumMaterialPriceDetail);
                        basicsdatumMaterialPriceDetail.setPriceId(basicsdatumMaterialPrice.getId());
                        basicsdatumMaterialPriceDetails.add(basicsdatumMaterialPriceDetail);
                    }
                }
                //basicsdatumMaterialPriceDetailService.addAndUpdateAndDelList(basicsdatumMaterialPriceDetails, new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("price_id", basicsdatumMaterialPrice.getId()));
            }
            basicsdatumMaterialPriceDetailService.remove(new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
            basicsdatumMaterialPriceDetailService.saveBatch(basicsdatumMaterialPriceDetails);

        }
        basicsdatumMaterialService.saveOrUpdate(basicsdatumMaterial, new QueryWrapper<BasicsdatumMaterial>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
    }

    //供应商合并
    private List<BasicsdatumMaterialPrice> merge(List<BasicsdatumMaterialPrice> list) {
        ////排除第一轮
        Map<String, BasicsdatumMaterialPrice> map = new HashMap<>();
        for (BasicsdatumMaterialPrice item : list) {
            String key = item.getSupplierName() + item.getQuotationPrice() + item.getColorName();
            if (map.containsKey(key)) {
                BasicsdatumMaterialPrice existingItem = map.get(key);

                //规格
                existingItem.setWidthName(existingItem.getWidthName() + "," + item.getWidthName());
                HashSet<String> hashSet = new HashSet<>(Arrays.asList(existingItem.getWidthName().split(",")));
                existingItem.setWidthName(String.join(",", hashSet));

                //规格
                existingItem.setWidth(existingItem.getWidth() + "," + item.getWidth());
                HashSet<String> hashSet1 = new HashSet<>(Arrays.asList(existingItem.getWidth().split(",")));
                existingItem.setWidth(String.join(",", hashSet1));

                //颜色id
                existingItem.setColor(existingItem.getColor() + "," + item.getColor());
                HashSet<String> hashSet2 = new HashSet<>(Arrays.asList(existingItem.getColor().split(",")));
                existingItem.setColor(String.join(",", hashSet2));

                //颜色名称
                existingItem.setColorName(existingItem.getColorName() + "," + item.getColorName());
                HashSet<String> hashSet3 = new HashSet<>(Arrays.asList(existingItem.getColorName().split(",")));
                existingItem.setColorName(String.join(",", hashSet3));


                //索引
                if (existingItem.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(existingItem.getIndex());
                    existingItem.setIndexList(set);
                } else {
                    existingItem.getIndexList().add(item.getIndex());
                    existingItem.getIndexList().add(existingItem.getIndex());
                }

                map.put(key, existingItem);
            } else {
                if (item.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(item.getIndex());
                    item.setIndexList(set);
                } else {
                    item.getIndexList().add(item.getIndex());
                    item.getIndexList().add(item.getIndex());
                }

                map.put(key, item);
            }
        }

        List<BasicsdatumMaterialPrice> mergedList = new ArrayList<>();
        for (Map.Entry<String, BasicsdatumMaterialPrice> entry : map.entrySet()) {
            mergedList.add(entry.getValue());
        }
        //Set<String> set1=new HashSet<>();
        //排除第二轮
        Map<String, BasicsdatumMaterialPrice> map1 = new HashMap<>();
        for (BasicsdatumMaterialPrice item : mergedList) {
            String key = item.getSupplierName() + item.getWidthName() + item.getQuotationPrice();
            if (map1.containsKey(key)) {
                BasicsdatumMaterialPrice existingItem = map1.get(key);

                //颜色id
                existingItem.setColor(existingItem.getColor() + "," + item.getColor());
                HashSet<String> hashSet2 = new HashSet<>(Arrays.asList(existingItem.getColor().split(",")));
                existingItem.setColor(String.join(",", hashSet2));

                //颜色名称
                existingItem.setColorName(existingItem.getColorName() + "," + item.getColorName());
                HashSet<String> hashSet3 = new HashSet<>(Arrays.asList(existingItem.getColorName().split(",")));
                existingItem.setColorName(String.join(",", hashSet3));


                //索引
                if (existingItem.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(existingItem.getIndex());
                    set.addAll(existingItem.getIndexList());
                    existingItem.setIndexList(set);
                } else {
                    existingItem.getIndexList().add(item.getIndex());
                    existingItem.getIndexList().addAll(existingItem.getIndexList());
                    existingItem.getIndexList().addAll(item.getIndexList());
                    existingItem.getIndexList().add(existingItem.getIndex());
                }

                map1.put(key, existingItem);
            } else {
                if (item.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(item.getIndex());
                    set.addAll(item.getIndexList());
                    item.setIndexList(set);
                } else {
                    item.getIndexList().add(item.getIndex());
                    item.getIndexList().addAll(item.getIndexList());
                    item.getIndexList().add(item.getIndex());
                }
                map1.put(key, item);
            }
        }
        List<BasicsdatumMaterialPrice> mergedList1 = new ArrayList<>();
        for (Map.Entry<String, BasicsdatumMaterialPrice> entry : map1.entrySet()) {
            BasicsdatumMaterialPrice value = entry.getValue();
            mergedList1.add(value);
        }

        return mergedList1;
    }

}
