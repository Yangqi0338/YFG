package com.base.sbc.module.smp;

import com.base.sbc.config.restTemplate.RestTemplateUtils;
import com.base.sbc.module.smp.dto.*;
import org.springframework.stereotype.Service;


/**
 * @author 卞康
 * @date 2023/5/8 15:27:43
 * @mail 247967116@qq.com
 * 对接下发Smp主数据
 */
@Service
public class SmpService {


    //private  static final String URL ="http://10.88.34.25:7006/pdm";
    private static final String URL = "http://smp-i.eifini.com/service-manager/pdm";


    /**
     * 商品主数据下发
     */
    public Boolean goods(SmpGoodsDto smpGoodsDto) {
        return RestTemplateUtils.spmPost(URL + "/goods", smpGoodsDto);
    }

    /**
     * 物料主数据下发
     */
    public Boolean materials(SmpMaterialDto smpMaterialDto) {
        return RestTemplateUtils.spmPost(URL + "/materials", smpMaterialDto);
    }

    /**
     * bom下发
     */
    public Boolean bom(SmpBomDto smpBomDto) {
        return RestTemplateUtils.spmPost(URL + "/bom", smpBomDto);
    }

    /**
     * 颜色主数据下发
     */
    public Boolean color(SmpColorDto smpColorDto) {
        return RestTemplateUtils.spmPost(URL + "/color", smpColorDto);
    }

    /**
     * 工艺单下发
     */
    public Boolean processSheet(SmpProcessSheetDto smpProcessSheetDto) {
        return RestTemplateUtils.spmPost(URL + "/processSheet", smpProcessSheetDto);
    }

    /**
     * 样品下发
     */
    public Boolean sample(SmpSampleDto smpSampleDto) {
        return RestTemplateUtils.spmPost(URL + "/sample", smpSampleDto);
    }

    /**
     * 修改尺码的时候验证
     */
    public Boolean style(PlmStyleSizeParam param) {
        return RestTemplateUtils.spmPost("http://10.8.250.100:1980/escm-app/information/plm/style", param);
    }


    public static void main(String[] args) {
        SmpGoodsDto goodsDto = new SmpGoodsDto();
        goodsDto.setDesigner("4564");
        goodsDto.setMainPush(true);
        goodsDto.setBrandName("4564");
        goodsDto.setDesignNumber("545564");
        goodsDto.setDesignScore(156465);
        goodsDto.setRegion("sdf");
        goodsDto.setPlanningRate(4564.0);
        goodsDto.setSeason("sdf");
        goodsDto.setProductType("Sdf");
        Boolean post = RestTemplateUtils.spmPost(URL + "/goods", goodsDto);
        System.out.println(post);
    }

}

