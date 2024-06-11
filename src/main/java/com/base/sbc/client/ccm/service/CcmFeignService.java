package com.base.sbc.client.ccm.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.entity.*;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public static final ThreadLocal<Page<BasicBaseDict>> pageLocal = new ThreadLocal<>();

    public void setPage(Page<BasicBaseDict> page) {
        pageLocal.set(page);
    }

    public PageInfo<BasicBaseDict> clearPage(List<BasicBaseDict> baseDictList) {
        Page<BasicBaseDict> page = pageLocal.get();
        if (page != null) {
            PageInfo<BasicBaseDict> pageInfo = page.toPageInfo();
            int pageNum = pageInfo.getPageNum();
            int pageSize = pageInfo.getPageSize();
            int maxSize = baseDictList.size();
            if (CollectionUtil.isNotEmpty(baseDictList)) {
                int formPage = Math.max(pageNum - 1, 0);
                int fromIndex = Math.min(formPage * pageSize, maxSize-1);
                int toIndex = Math.min((formPage + 1) * pageSize, maxSize);
                pageInfo.setList(baseDictList.subList(fromIndex, toIndex));
            }
            pageInfo.setTotal(maxSize);
            pageLocal.remove();
            return pageInfo;
        }
        return new PageInfo<>();
    }

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

    public List<BasicStructureTreeVo> findStructureTreeByCodes(String codes) {
        String str = ccmService.findStructureTreeByCodes(codes);
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


    public List<BasicStructureTreeVo> queryBasicStructureNextLevelList(String structureCode, String treeCode, Integer level) {
        String str = ccmService.queryBasicStructureNextLevelList(structureCode, treeCode, level);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            try {
                List<BasicStructureTreeVo> data = jsonObject.getJSONArray("data").toJavaList(BasicStructureTreeVo.class);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<BasicStructureTreeVo> basicStructureTreeByCode(String code, String hasRoot, String levels) {
        String str = ccmService.basicStructureTreeByCode(code, hasRoot, levels);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            try {
                List<BasicStructureTreeVo> data = jsonObject.getJSONObject("data").getJSONArray("dataTree").toJavaList(BasicStructureTreeVo.class);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, BasicStructureTreeVo> findStructureTreeByCategoryIdsToMap(String categoryIds) {
        List<BasicStructureTreeVo> list = findStructureTreeByCategoryIds(categoryIds);
        return Opt.ofNullable(list)
                .map(e -> e.stream().collect(Collectors.toMap(BasicStructureTreeVo::getId, v -> v, (a, b) -> b)))
                .orElse(MapUtil.empty());
    }

    /**
     * 通过id 获取结构树名称
     *
     * @param categoryIds
     * @return
     */
    public Map<String, String> findStructureTreeNameByCategoryIds(String categoryIds, String structureCode) {
        BasicStructureSearchDto dto = new BasicStructureSearchDto();
        dto.setCategoryIds(categoryIds);
        dto.setStructureCode(structureCode);
        String str = ccmService.findStructureTreeNameByCategoryIds(dto);
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
     * 通过code 获取结构树名称
     *
     * @param codes
     * @return
     */
    public Map<String, String> findStructureTreeNameByCodes(String codes, String structureCode) {
        BasicStructureSearchDto dto = new BasicStructureSearchDto();
        dto.setCodes(codes);
        dto.setStructureCode(structureCode);
        String str = ccmService.findStructureTreeNameByCodes(dto);
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
        String dictInfo = ccmService.getDictInfo(types, null);
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
     * ccm 查询字典
     *
     * @param types
     * @return
     */
    public Map<String, Map<String, String>> getDictInfoToMapTurnOver(String types) {
        Map<String, Map<String, String>> result = new LinkedHashMap<>(16);
        String dictInfo = ccmService.getDictInfo(types, null);
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
                    dictMap.put(dict.getName(), dict.getValue());
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
            ids = jsonObject.get("data").toString();
        }
        return ids;
    }

    /**
     * 查询 品类集合
     */
    public List<BasicCategoryDot> getCategorySByNameAndLevel(String structureName, String names, String level) {
        String dictInfo = ccmService.getCategorySByNameAndLevel(structureName, names, level);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicCategoryDot> data = jsonObject.getJSONArray("data").toJavaList(BasicCategoryDot.class);
            return data;
        }
        return null;
    }

    /**
     * 查询 品类集合
     */
    public List<BasicCategoryDot> getTreeByNamelList(String structureName, String level) {
        String dictInfo = ccmService.treeByName(structureName, null, level);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicCategoryDot> data = JSON.parseArray((JSON.parseObject(jsonObject.get("data").toString()).get("dataTree")).toString(), BasicCategoryDot.class);
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
            Map<String, String> idNameMap = findStructureTreeNameByCategoryIds(CollUtil.join(ids, StrUtil.COMMA), "品类");
            for (Object o : list) {
                String id = BeanUtil.getProperty(o, idkey);
                BeanUtil.setProperty(o, nameKey, Optional.ofNullable(idNameMap.get(id)).orElse(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BasicBaseDict> basicDictDependsByTypes(String isLeaf, String type, String dependDictType, String dependCode) {
        String str = ccmService.basicDictDependsByTypes(isLeaf, type, dependDictType, dependCode);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicBaseDict> data = jsonObject.getJSONArray("data").toJavaList(BasicBaseDict.class);
            return data;
        }
        return null;
    }

    public List<BasicStructureTree> appointNextLevelList(String structureCode, String level) {
        String str = ccmService.appointNextLevelList(structureCode, level);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicStructureTree> data = jsonObject.getJSONArray("data").toJavaList(BasicStructureTree.class);
            return data;
        }
        return null;
    }

    /**
     * 通过编码获取开关是否开启或关闭
     * @param code
     * @return
     */
    public Boolean getSwitchByCode(String code) {
        String resultStr = ccmService.getCompanySettingData(code);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        if (Objects.isNull(jsonObject.getJSONObject("data")) || !jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            return Boolean.FALSE;
        }
        return !YesOrNoEnum.YES.getValueStr().equals(jsonObject.getJSONObject("data").getString("value"));
    }

    /**
     * 通过编码获取开关是否开启或关闭
     * @param code
     * @return
     */
    public YesOrNoEnum inSettingOptions(String code, String... value) {
        List<String> valueList = Arrays.stream(value).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        if (CollUtil.isEmpty(valueList)) return YesOrNoEnum.NO;
        String resultStr = ccmService.getCompanySettingData(code);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        JSONObject data = jsonObject.getJSONObject("data");
        if (Objects.isNull(data) || !jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            return YesOrNoEnum.NO;
        }
        String settingValue = data.getString("value");
        return YesOrNoEnum.findByValue(Arrays.stream(value).allMatch(settingValue::contains));
    }

    /**
     * 查询所有单位列表，可根据类型筛选
     * @param type 类型
     *
     */
    public List<BasicUnitConfig> getAllUnitConfigList(String type) {
        String str = ccmService.getAllUnitConfigList(type);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            List<BasicUnitConfig> data = jsonObject.getJSONArray("data").toJavaList(BasicUnitConfig.class);
            return data;
        }
        return null;
    }

    /**
     * ccm 查询字典
     *
     * @param types
     * @return
     */
    public List<BasicBaseDict> getDictInfoToList(String types) {
        List<BasicBaseDict> list = new ArrayList<>();
        String dictInfo = ccmService.getDictInfo(types, null);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            list = jsonObject.getJSONArray("data").toJavaList(BasicBaseDict.class);
        }
        return list;
    }

    /**
     * ccm 查询字典
     *
     * @param types
     * @return
     */
    public List<BasicBaseDict> getAllDictInfoToList(String types) {
        List<BasicBaseDict> list = new ArrayList<>();
        String dictInfo = ccmService.getDictInfo(types, "0,1");
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            list = jsonObject.getJSONArray("data").toJavaList(BasicBaseDict.class);
        }
        return list;
    }

    /**
     * ccm 查询字典依赖
     *
     */
    public List<BasicDictDepend> getDictDependsList( BasicDictDependsQueryDto basicDictDependsQueryDto) {
        List<BasicDictDepend> list = new ArrayList<>();
        String dictDependsList = ccmService.getDictDependsList(basicDictDependsQueryDto.getDictTypeName(), basicDictDependsQueryDto.getPageNum(), basicDictDependsQueryDto.getPageSize());
        JSONObject jsonObject = JSON.parseObject(dictDependsList);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            list = jsonObject.getJSONObject("data").getJSONArray("list").toJavaList(BasicDictDepend.class);
        }
        return list;
    }
}
