package com.base.sbc.client.amc.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * amc 帮助类
 */
public class AmcUtils {


    /**
     * 将 amc 返回的str 转换为 List<UserCompany>
     * @param str
     * @return
     */
    public static List<UserCompany> parseStrToList(String str){
        JSONObject jsonObject = JSON.parseObject(str);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            return data.toJavaList(UserCompany.class);
        }
        return new ArrayList<>();
    }

    /**
     * 将 amc 返回的str 转换为 Map<userId,UserCompany>
     * @param str
     * @return
     */
    public static Map<String,UserCompany> parseStrToMap(String str){
        List<UserCompany> userCompanies = parseStrToList(str);
        if(CollUtil.isNotEmpty(userCompanies)){
            return  userCompanies.stream().collect(Collectors.toMap(k->k.getUserId(),v->v,(a,b)->b));
        }
        return MapUtil.empty();
    }
}
