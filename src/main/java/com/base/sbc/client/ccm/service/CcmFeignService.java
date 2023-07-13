package com.base.sbc.client.ccm.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 类描述：ccm 远程调用
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.ccm.service.CcmFeignService
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 16:36
 */
@Service
public class CcmFeignService {

    @Autowired
    private CcmService ccmService;

    public List<BasicStructureTreeVo> findStructureTreeByCategoryIds(String categoryIds) {
        String str = ccmService.findStructureTreeByCategoryIds(categoryIds);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicStructureTreeVo> data = jsonObject.getJSONArray("data").toJavaList(BasicStructureTreeVo.class);
            return data;
        }
        return null;
    }

    /**
     * 通过id 获取结构树名称
     *
     * @param categoryIds
     * @return
     */
    public Map<String, String> findStructureTreeNameByCategoryIds(String categoryIds) {
        String str = ccmService.findStructureTreeNameByCategoryIds(categoryIds);
        Map<String, String> result = new HashMap<>(16);
        if (StrUtil.isBlank(str)) {
            return result;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            return (Map<String, String>) jsonObject.get("data");

        }
        return result;
    }

    /**
     * ccm 查询字典
     *
     * @param types
     * @return
     */
    public Map<String, Map<String, String>> getDictInfoToMap(String types) {
        Map<String, Map<String, String>> result = new LinkedHashMap<>(16);
        String dictInfo = ccmService.getDictInfo(types);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicBaseDict> data = jsonObject.getJSONArray("data").toJavaList(BasicBaseDict.class);
            if (CollUtil.isNotEmpty(data)) {
                for (BasicBaseDict dict : data) {
                    Map<String, String> dictMap = result.get(dict.getType());
                    if (dictMap == null) {
                        dictMap = new LinkedHashMap<>(16);
                        result.put(dict.getType(), dictMap);
                    }
                    dictMap.put(dict.getValue(), dict.getName());
                }
            }
        }
        return result;
    }

    /**
     * 查询 品类id
     */
    public String getIdsByNameAndLevel(String structureName, String names, String level) {
        String dictInfo = ccmService.getIdsByNameAndLevel(structureName, names, level);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        String ids = "";
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            ids =jsonObject.get("data").toString();
        }
        return ids;
    }

    /**
     * 查询 品类集合
     */
    public List<BasicCategoryDot>  getCategorySByNameAndLevel(String structureName, String names, String level) {
        String dictInfo = ccmService.getCategorySByNameAndLevel(structureName, names, level);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicCategoryDot> data = jsonObject.getJSONArray("data").toJavaList(BasicCategoryDot.class);
            return data;
        }
        return null;
    }

    public void setCategoryName(List list, String idkey, String nameKey) {
        try {
            if (CollUtil.isEmpty(list)) {
                return;
            }
            List<String> ids = new ArrayList<>();
            for (Object o : list) {
                String id = BeanUtil.getProperty(o, idkey);
                ids.add(id);
            }
            Map<String, String> idNameMap = findStructureTreeNameByCategoryIds(CollUtil.join(ids, StrUtil.COMMA));
            for (Object o : list) {
                String id = BeanUtil.getProperty(o, idkey);
                BeanUtil.setProperty(o, nameKey, Optional.ofNullable(idNameMap.get(id)).orElse(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
