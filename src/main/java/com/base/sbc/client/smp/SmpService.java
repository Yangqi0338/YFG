package com.base.sbc.client.smp;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.smp.dto.SmpGoodsDto;
import com.base.sbc.config.restTemplate.RestTemplateUtils;
import org.springframework.stereotype.Service;


/**
 * @author 卞康
 * @date 2023/5/8 15:27:43
 * @mail 247967116@qq.com
 */
@Service
public class SmpService {


    private  static final String URL ="http://10.88.34.25:7006/pdm";
    //private  static final String URL ="http://smp-i.eifini.com/service-manager/pdm";


    /**
     * 商品主数据下发
     */
    public Boolean goods(SmpGoodsDto goodsDto){
        return RestTemplateUtils.spmPost(URL + "/sample",goodsDto);

    }

    /**
     * 物料主数据下发
     */
    public Boolean materials(JSONObject jsonObject){
        return RestTemplateUtils.spmPost(URL + "/sample",jsonObject);

    }

    /**
     * bom下发
     */
    public Boolean bom(JSONObject jsonObject){
        return RestTemplateUtils.spmPost(URL + "/sample",jsonObject);
    }

    /**
     * 颜色主数据下发
     */
    public Boolean color(JSONObject jsonObject){
        return RestTemplateUtils.spmPost(URL + "/sample",jsonObject);

    }

    /**
     * 工艺单地址下发
     */
    public Boolean processSheet(JSONObject jsonObject){
        return RestTemplateUtils.spmPost(URL + "/sample",jsonObject);
    }

    /**
     * 样品下发
     */
    public Boolean sample(JSONObject jsonObject){
        return RestTemplateUtils.spmPost(URL + "/sample",jsonObject);
    }



    public static void main(String[] args) {
        SmpGoodsDto goodsDto =new SmpGoodsDto();
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

