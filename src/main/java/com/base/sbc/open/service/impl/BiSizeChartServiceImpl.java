//package com.base.sbc.open.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.base.sbc.module.common.service.impl.BaseServiceImpl;
//import com.base.sbc.module.pack.entity.PackInfo;
//import com.base.sbc.module.pack.entity.PackSize;
//import com.base.sbc.module.pack.service.PackInfoService;
//import com.base.sbc.module.pack.service.PackSizeService;
//import com.base.sbc.module.style.entity.Style;
//import com.base.sbc.module.style.service.StyleService;
//import com.base.sbc.open.entity.BiSizeChart;
//import com.base.sbc.open.mapper.BiSizeChartMapper;
//import com.base.sbc.open.service.BiSizeChartService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author 卞康
// * @date 2023/7/24 10:19:06
// * @mail 247967116@qq.com
// */
//@Service
//@RequiredArgsConstructor
//public class BiSizeChartServiceImpl extends ServiceImpl<BiSizeChartMapper,BiSizeChart> implements BiSizeChartService{
//
//    private final StyleService styleService;
//
//    private final PackSizeService packSizeService;
//
//    private final PackInfoService packInfoService;
//
//    public void sizeChart() {
//        List<BiSizeChart> biSizeCharts = new ArrayList<>();
//        List<Style> list = styleService.list();
//        for (Style style : list) {
//            List<PackInfo> packInfos = packInfoService.list(new QueryWrapper<PackInfo>().eq("foreign_id", style.getId()));
//            for (PackInfo packInfo : packInfos) {
//                List<PackSize> packSizes = packSizeService.list(new QueryWrapper<PackSize>().eq("foreign_id", packInfo.getId()));
//                for (PackSize packSize : packSizes) {
//                    String[] sizeNames = packSize.getSize().split(",");
//                    for (String sizeName : sizeNames) {
//                        BiSizeChart biSize = new BiSizeChart();
//                        //biSize.setSizeCode(String.valueOf(packSize.getSort()));
//                        biSize.setSizechartName(style.getSizeRange());//号型类型编码？
//                        biSize.setSizerangeUrl(style.getSizeRange());
//                        biSize.setSizerangeName(style.getSizeRangeName()); //号型类型名称？
//                        biSize.setBaseSize(style.getDefaultSize());
//                        biSize.setSubrangeIncrement(packSize.getPartName());//未确认
//                        try {
//                            biSize.setSizechartGroup(style.getProdCategoryName());
//                            String[] split = sizeName.split("\\(");
//                            biSize.setProductSizeType(split[1].replace(")", ""));
//                            biSize.setProductSize(split[0]);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        biSize.setSizechartState("DRAFT");
//                        biSize.setCreator(packSize.getCreateName());
//                        biSize.setCreateTime(packSize.getCreateDate());
//                        biSize.setUpdateTime(packSize.getUpdateDate());
//                        biSize.setUpdator(packSize.getUpdateName());
//                        biSize.setDimensionName(packSize.getPartName());
//                        biSize.setDimensionDescription(packSize.getMethod());
//                        biSize.setDimensionSizeChart(sizeName);
//                        biSize.setIsBaseSize(sizeName.equals(style.getDefaultSize()));
//
//                        JSONObject jsonObject = JSON.parseObject(packSize.getStandard());
//                        biSize.setIncrementsCode(jsonObject.getString("template" + sizeName));
//                        biSize.setPatternCode(jsonObject.getString("garment" + sizeName));
//                        biSize.setShrinkageCode(jsonObject.getString("washing" + sizeName));
//
//                        biSize.setDimensionId(packSize.getPartCode());//测量点编码
//                        biSize.setStyleUrl(style.getDesignNo());
//
//                        biSizeCharts.add(biSize);
//                    }
//                }
//            }
//        }
//        this.remove(null);
//        this.saveBatch(biSizeCharts);
//    }
//}
