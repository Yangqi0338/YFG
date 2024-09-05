/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.difference.entity.Difference;
import com.base.sbc.module.difference.service.DifferenceService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackSizeConfigDto;
import com.base.sbc.module.pack.dto.PackSizeConfigSearchDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.entity.PackSizeConfig;
import com.base.sbc.module.pack.mapper.PackSizeConfigMapper;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackSizeConfigService;
import com.base.sbc.module.pack.service.PackSizeService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-尺寸表配置 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeConfigService
 * @email your email
 * @date 创建时间：2023-9-1 14:07:14
 */
@Service
public class PackSizeConfigServiceImpl extends AbstractPackBaseServiceImpl<PackSizeConfigMapper, PackSizeConfig> implements PackSizeConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    @Lazy
    private StyleService styleService;

    @Autowired
    private DifferenceService differenceService;

    @Autowired
    private PackSizeService packSizeService;

    @Override
    String getModeName() {
        return "尺寸表配置";
    }


    @Override
    public PackSizeConfigVo getConfig(String foreignId, String packType) {
        PackSizeConfig packSizeConfig = get(foreignId, packType);
        if (packSizeConfig == null) {
            PackInfo packInfo = packInfoService.getById(foreignId);
            Style style = styleService.getById(packInfo.getForeignId());
            packSizeConfig = createByStyle(foreignId, packType, style);
        }

        return BeanUtil.copyProperties(packSizeConfig, PackSizeConfigVo.class);
    }

    @Override
    public PackSizeConfig createByStyle(String foreignId, String packType, Style style) {
        PackSizeConfig packSizeConfig = BeanUtil.copyProperties(style, PackSizeConfig.class);
        CommonUtils.resetCreateUpdate(packSizeConfig);
        packSizeConfig.setId(null);
        packSizeConfig.setForeignId(foreignId);
        packSizeConfig.setPackType(packType);
        packSizeConfig.setActiveSizes(style.getProductSizes());
        save(packSizeConfig);
        return packSizeConfig;
    }

    @Override
    public PageInfo<PackSizeConfigVo> pageInfo(PackSizeConfigSearchDto dto) {
        BaseQueryWrapper<PackSizeConfig> qw = new BaseQueryWrapper<>();
        qw.eq("tpsc.company_code", getCompanyCode());
        qw.eq("tpsc.pack_type", PackUtils.PACK_TYPE_BIG_GOODS);
        qw.eq("tpsc.del_flag", BaseGlobal.STATUS_NORMAL);
        qw.isNotNull("tpi.style_no");
        qw.ne("tpi.style_no","");
        qw.andLike(dto.getSearch(), "product_sizes", "size_range_name", "size_codes", "size_range","tpi.style_no");
        Page<PackSizeConfigVo> objects = PageHelper.startPage(dto);
        baseMapper.sizeConfigList(qw);
        PageInfo<PackSizeConfigVo> packSizeConfigPageInfo = objects.toPageInfo();
        return packSizeConfigPageInfo;
    }

    /**
     * 获取测量部位的当差设置
     *
     * @param dto
     * @return
     */
    @Override
    public Difference gatPartDifference(PackSizeConfigDto dto) {
        /*查配置*/
        PackSizeConfigVo config = getConfig(dto.getForeignId(), dto.getPackType());
        if(StringUtils.isNotBlank(config.getDifferenceCode())){
            //查询当差下的测量点
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("range_difference_id", config.getDifferenceId());
            queryWrapper.eq("part_code",dto.getPartCoed());
            List<Difference> differenceList = differenceService.list(queryWrapper);
            if(CollUtil.isNotEmpty(differenceList)){
                return differenceList.get(0);
            }
        }
        return new Difference();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackSizeConfigVo saveConfig(PackSizeConfigDto dto) {
        PackSizeConfigVo config = getConfig(dto.getForeignId(), dto.getPackType());
        saveOrUpdateOperaLog(dto, config, genOperaLogEntity(config, "修改"));

        /*查看是否引用当差及是否改变当差设置*/
        if (StringUtils.isNotBlank(dto.getDifferenceCode()) && !StringUtils.equals(dto.getDifferenceCode(), config.getDifferenceCode())) {
            /*获取尺码表的数据*/
            PackCommonPageSearchDto packCommonPageSearchDto = new PackCommonPageSearchDto();
            packCommonPageSearchDto.setForeignId(dto.getForeignId());
            packCommonPageSearchDto.setPackType(dto.getPackType());
            List<PackSizeVo> packSizeVoList = packSizeService.pageInfo(packCommonPageSearchDto).getList();
            if (CollUtil.isNotEmpty(packSizeVoList)) {
                //查询当差下的测量点
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("range_difference_id", dto.getDifferenceId());
                List<Difference> differenceList = differenceService.list(queryWrapper);
                if (CollUtil.isNotEmpty(differenceList)) {
                    /*测量部位下的当差*/
                    Map<String, List<Difference>> differenceMap = differenceList.stream().collect(Collectors.groupingBy(p -> p.getPartCode()));
                    /*循环匹配测量部位*/
                    for (PackSizeVo packSizeVo : packSizeVoList) {
                        if (StringUtils.isNotBlank(packSizeVo.getPartCode())) {
                            List<Difference> differenceList1 = differenceMap.get(packSizeVo.getPartCode());
                            if (CollUtil.isNotEmpty(differenceList1)) {
                                BeanUtils.copyProperties(differenceList1.get(0), packSizeVo, "id");
                                //设置当差 （只放入设置中的尺码）
                                if (StringUtils.isNotBlank(differenceList1.get(0).getCodeErrorSetting())) {
                                    /*模板中的档差设置*/
                                    JSONArray jsonArray = JSON.parseArray(differenceList1.get(0).getCodeErrorSetting());
                                    Map<String, JSONObject> sizeMap = jsonArray.stream().collect(Collectors.toMap(k -> ((JSONObject) k).getString("size"), v -> (JSONObject) v, (a, b) -> a));
                                    List<Map<String, String>> mapList = new ArrayList<>();
                                    /*页面设置中选中的数据*/
                                    for (String s : dto.getActiveSizesList()) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("size", s);
                                        JSONObject jsonObject = sizeMap.get(s);
                                        if (!ObjectUtils.isEmpty(jsonObject)) {
                                            map.put("difference", jsonObject.get("difference").toString());
                                        } else {
                                            map.put("difference", "");
                                        }
                                        mapList.add(map);
                                    }
                                    /*使用当差模板中的数据*/
                                    packSizeVo.setCodeErrorSetting(JSON.toJSON(mapList).toString());
                                }
                            }
                        }
                    }
                    List<PackSize> packSizeList = BeanUtil.copyToList(packSizeVoList, PackSize.class);
                    packSizeService.saveOrUpdateBatch(packSizeList);

                }
            }
        }
        BeanUtil.copyProperties(dto, config, "id");
        updateById(config);
        return BeanUtil.copyProperties(config, PackSizeConfigVo.class);
    }

// 自定义方法区 不替换的区域【other_end】

}

