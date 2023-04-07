package com.base.sbc.module.common.dto;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Data
public class GetMaxCodeRegexp {


    /**对应字段和值，例如: brand->sjkj */
    LinkedHashMap<String,Object> valueMap;

    /**
     * 远程请求的服务
     */
    private CcmService ccmService;

    public GetMaxCodeRegexp() {
    }

    public GetMaxCodeRegexp(CcmService ccmService) {
        this.ccmService = ccmService;
    }


    /**
     * 根据编码规则中配置的编码，获取编号
     *
     * @param billCode
     * @param object
     * @return
     */
    public String genCode(String billCode, Object object) {
        //远程请求获取结果
        try {
            String apiResultStr = this.ccmService.getGenCodeByRegexp(billCode, object);
            JSONObject jsonObject = JSONObject.parseObject(apiResultStr);
            if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
                return jsonObject.getString(BaseConstant.DATA);
            }
        } catch (Exception e) {
            //未生成，或生成有异常
            return null;
        }
        //未生成，或生成有异常
        return null;
    }
}
