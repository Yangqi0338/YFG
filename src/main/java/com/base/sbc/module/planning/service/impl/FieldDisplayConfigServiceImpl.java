/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.annotation.FieldDisplay;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.FieldDisplaySaveDto;
import com.base.sbc.module.planning.entity.FieldDisplayConfig;
import com.base.sbc.module.planning.mapper.FieldDisplayConfigMapper;
import com.base.sbc.module.planning.service.FieldDisplayConfigService;
import com.base.sbc.module.planning.vo.FieldDisplayVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：字段显示隐藏配置 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.FieldDisplayConfigService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-28 14:22:42
 */
@Service
public class FieldDisplayConfigServiceImpl extends BaseServiceImpl<FieldDisplayConfigMapper, FieldDisplayConfig> implements FieldDisplayConfigService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<FieldDisplayVo> getConfig(String type) {
        Map<String, List<FieldDisplayVo>> result = new HashMap<>(8);
        //获取默认配置
        List<FieldDisplayVo> defaultList = new ArrayList<>(16);
        Class clzz = null;
        if (StrUtil.equals(type, styleBoard)) {
            clzz = PlanningSummaryDetailVo.class;
        } else if (StrUtil.equals(type, planningBoard)) {
            clzz = PlanningSummaryDetailVo.class;
        }
        if (clzz == null) {
            throw new OtherException("无匹配类型");
        }
        Field[] fields = clzz.getDeclaredFields();
        // 遍历字段
        for (Field field : fields) {
            // 检查字段上是否存在UserAvatar的注解
            if (field.isAnnotationPresent(FieldDisplay.class)) {
                // 获取字段上的注解对象
                FieldDisplay annotation = field.getAnnotation(FieldDisplay.class);
                FieldDisplayVo fd = new FieldDisplayVo();
                fd.setField(field.getName());
                fd.setName(annotation.value());
                fd.setDisplay(annotation.display());
                defaultList.add(fd);
            }
        }
        // 查询用户配置
        QueryWrapper<FieldDisplayConfig> qw = new QueryWrapper<>();
        qw.eq("type", type);
        qw.eq("user_id", getUserId());
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.last("limit 1");
        FieldDisplayConfig userConfig = getOne(qw);
        List<FieldDisplayVo> userList = null;
        if (userConfig != null && StrUtil.isNotBlank(userConfig.getConfig())) {
            try {
                userList = JSONArray.parseArray(userConfig.getConfig()).toJavaList(FieldDisplayVo.class);
                if (userList.size() != defaultList.size()) {
                    userList = null;
                }
            } catch (Exception e) {
                log.error("解析字段配置异常", e);
            }
        }
        return Opt.ofEmptyAble(userList).orElse(defaultList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveConfig(FieldDisplaySaveDto dto) {
        //查询
        QueryWrapper<FieldDisplayConfig> qw = new QueryWrapper<>();
        qw.eq("type", dto.getType());
        qw.in("user_id", getUserId());
        qw.last("limit 1");
        FieldDisplayConfig one = getOne(qw);
        if (one == null) {
            FieldDisplayConfig save = new FieldDisplayConfig();
            save.setUserId(getUserId());
            save.setType(dto.getType());
            save.setConfig(JSON.toJSONString(dto.getConfig()));
            save(save);
        } else {
            one.setConfig(JSON.toJSONString(dto.getConfig()));
            updateById(one);
        }
        return true;
    }

// 自定义方法区 不替换的区域【other_end】

}
