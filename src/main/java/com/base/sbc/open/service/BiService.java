package com.base.sbc.open.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackSizeService;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.open.entity.BiSizeChart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/24 10:19:06
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BiService {

    private final SampleDesignService sampleDesignService;

    private final PackSizeService packSizeService;

    private final PackInfoService packInfoService;

    public List<BiSizeChart> sizeChart() {
        List<BiSizeChart> biSizeCharts = new ArrayList<>();
        // TODO: 2023/7/24 字段未确认
        List<SampleDesign> list = sampleDesignService.list();
        for (SampleDesign sampleDesign : list) {
            List<PackInfo> packInfos = packInfoService.list(new QueryWrapper<PackInfo>().eq("foreign_id", sampleDesign.getId()));
            for (PackInfo packInfo : packInfos) {
                List<PackSize> packSizes = packSizeService.list(new QueryWrapper<PackSize>().eq("foreign_id", packInfo.getId()));
                for (PackSize packSize : packSizes) {
                    String[] sizeNames = packSize.getSize().split(",");
                    for (String sizeName : sizeNames) {
                        BiSizeChart biSize = new BiSizeChart();
                        biSize.setSizechartName(sampleDesign.getSizeRange());//号型类型编码？
                        biSize.setSizerangeName(null); //号型类型名称？
                        biSize.setBaseSize(sampleDesign.getDefaultSize());
                        biSize.setSubrangeIncrement(null);//未确认
                        try {
                            biSize.setSizechartGroup(sampleDesign.getProdCategoryName());
                            String[] split = sizeName.split("\\(");
                            biSize.setProductSizeType(split[1].replace(")", ""));
                            biSize.setProductSize(split[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        biSize.setSizechartState("DRAFT");
                        biSize.setCreator(packSize.getCreateName());
                        biSize.setCreateTime(packSize.getCreateDate());
                        biSize.setUpdateTime(packSize.getUpdateDate());
                        biSize.setUpdator(packSize.getUpdateName());
                        biSize.setDimensionName(packSize.getPartName());
                        biSize.setDimensionDescription(packSize.getMethod());
                        biSize.setDimensionSizeChart(sizeName);
                        biSize.setIsBaseSize(sizeName.equals(sampleDesign.getDefaultSize()));

                        JSONObject jsonObject = JSON.parseObject(packSize.getStandard());
                        biSize.setIncrementsCode(jsonObject.getString("template" + sizeName));
                        biSize.setPatternCode(jsonObject.getString("garment" + sizeName));
                        biSize.setShrinkageCode(jsonObject.getString("washing" + sizeName));

                        biSize.setDimensionId(null);//测量点编码
                        biSize.setStyleUrl(sampleDesign.getStyleNo());

                        biSizeCharts.add(biSize);
                    }
                }
            }

        }
        return biSizeCharts;
    }
}
