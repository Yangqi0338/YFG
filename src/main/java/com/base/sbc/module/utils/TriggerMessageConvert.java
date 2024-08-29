package com.base.sbc.module.utils;

import static com.base.sbc.config.utils.DateUtils.FORMAT_SECOND;

import com.alibaba.fastjson.JSON;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.module.material.entity.Material;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;

/**
 *  触发配置转换
 **/
public class TriggerMessageConvert {
    private static final String paramsKey = "triggerParams";

    private static final String groupUserKey = "groupUser";

    /**
     * 素材库审批业务对象转换
     * @param material
     */
    public static Map<String, Object>  materialApproveConvert(Material material, GroupUser groupUser){
        Map<String, Object> objectMap = BeanUtil.beanToMap(material);
        Map<String, String> triggerParams = Maps.newHashMap();
        triggerParams.put("brand",material.getBrand());
        triggerParams.put("brand_name",material.getBrandName());
        triggerParams.put("create_name",material.getCreateName());
        triggerParams.put("material_name",material.getMaterialName());
        triggerParams.put("materialCode",material.getMaterialCode());
        triggerParams.put("material_category_name",material.getMaterialCategoryName());
        triggerParams.put("update_date", DateUtils.getDate(FORMAT_SECOND));
        if (null != groupUser){
            triggerParams.put(groupUserKey, JSON.toJSONString(groupUser));
        }
        objectMap.put(paramsKey,triggerParams);
        return objectMap;
    }

}
