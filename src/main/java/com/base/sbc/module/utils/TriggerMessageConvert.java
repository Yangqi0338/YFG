package com.base.sbc.module.utils;

import static com.base.sbc.config.utils.DateUtils.FORMAT_SECOND;

import static org.apache.commons.lang3.time.DateUtils.isSameDay;

import com.alibaba.fastjson.JSON;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
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
        triggerParams.put("pic_url",material.getPicUrl());
        if (null != groupUser){
            triggerParams.put(groupUserKey, JSON.toJSONString(groupUser));
        }
        objectMap.put(paramsKey,triggerParams);
        return objectMap;
    }

    public static Map<String, Object> styleDesignApproveConvert(Map<String, Object> variables, Style style, GroupUser user, StylePicUtils stylePicUtils) {
        if (null != user){
            variables.put(groupUserKey, JSON.toJSONString(user));
        }
        Map<String,String> messageObjects = Maps.newHashMap();
        messageObjects.put("brand",style.getBrand());
        messageObjects.put("brand_name",style.getBrandName());
        messageObjects.put("designer",style.getDesigner());
        messageObjects.put("design_no",style.getDesignNo());
        messageObjects.put("prod_category1st_name",style.getProdCategory1stName());
        messageObjects.put("prod_category_name",style.getProdCategoryName());
        messageObjects.put("prod_category2nd_name",style.getProdCategory2ndName());
        messageObjects.put("prod_category3rd_name",style.getProdCategory3rdName());
        messageObjects.put("planning_finish_date", DateUtils.formatDate(style.getPlanningFinishDate(),FORMAT_SECOND));
        messageObjects.put("demand_finish_date",DateUtils.formatDate(style.getPlanningFinishDate(),FORMAT_SECOND));
        messageObjects.put("task_level_name",style.getTaskLevelName());
        messageObjects.put("style_pic",stylePicUtils.getStyleUrl(style.getStylePic()));
        variables.put(paramsKey,messageObjects);
        return variables;
    }

    public static void main(String[] args) {
        IdGen idGen = new IdGen();
        for (int i = 0; i < 10; i++) {
            System.out.println(idGen.nextIdStr());
        }

    }

    public static Map<String, String> stylePricingConvert(StyleColor styleColor,Style style, String triggerActionCode) {
        Map<String,String> messageObjects = Maps.newHashMap();
        if (StringUtils.isNotBlank(triggerActionCode)){
            messageObjects.put("triggerActionCode",triggerActionCode);
        }
        messageObjects.put("brand",style.getBrand());
        messageObjects.put("design_no",style.getStyleNo());
        messageObjects.put("product_name",styleColor.getProductName());
        messageObjects.put("style_no",styleColor.getStyleNo());
        messageObjects.put("devt_type_name",styleColor.getDevtTypeName());
        messageObjects.put("defective_no",styleColor.getDefectiveNo());
        return messageObjects;
    }
}
