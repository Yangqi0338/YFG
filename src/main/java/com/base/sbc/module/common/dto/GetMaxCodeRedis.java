package com.base.sbc.module.common.dto;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author ZCYLGZ
 */
@Data
public class GetMaxCodeRedis {


    /**对应字段和值，例如: brand->sjkj */
    LinkedHashMap<String,Object> valueMap;

    /**
     * 远程请求的服务
     */
    private CcmService ccmService;

    public GetMaxCodeRedis() {
    }

    public GetMaxCodeRedis(CcmService ccmService) {
        this.ccmService = ccmService;
    }


    /**
     * 生成1个编码
     * 根据编码规则中配置的编码，获取编号
     * @param billCode
     * @param object
     * @return
     */
    public String genCode(String billCode, Object object) {
        //远程请求获取结果
        try {
            String apiResultStr = this.ccmService.getGenCodeByRedis(billCode,1, object);
            return CollUtil.getFirst(parse(apiResultStr));
        } catch (Exception e) {
            //未生成，或生成有异常
            return null;
        }
    }
    /**
     * 生成多个编码
     * 根据编码规则中配置的编码，获取编号
     * @param billCode
     * @param object
     * @return
     */
    public List<String> genCode(String billCode,int count, Object object) {
        //远程请求获取结果
        try {
            String apiResultStr = this.ccmService.getGenCodeByRedis(billCode,count, object);
            return parse(apiResultStr);
        } catch (Exception e) {
            //未生成，或生成有异常
            return null;
        }
    }

    /**
     * 生成1个编码
     * 根据编码规则中配置的编码，获取编号
     * @param billCode
     * @param object
     * @return
     */
    public String genCodeExists(String billCode,Object object) {
        //远程请求获取结果
        try {
            ApiResult apiResult = this.ccmService.getGenCodeExistsRedis(billCode,1, object);
            if(apiResult.getSuccess()){
                return ((ArrayList) apiResult.getData()).get(0).toString();
            }else{
                throw new RuntimeException(apiResult.getMessage());
            }
        } catch (Exception e) {
            //未生成，或生成有异常
            return null;
        }
    }

    /**
     * 解析返回
     * @param apiResultStr
     * @return
     */
    private List<String> parse(String apiResultStr){
        JSONObject jsonObject = JSONObject.parseObject(apiResultStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
           return jsonObject.getJSONArray(BaseConstant.DATA).toJavaList(String.class);
        }
        return null;
    }
}
