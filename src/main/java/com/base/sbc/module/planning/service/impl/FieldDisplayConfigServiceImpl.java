/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.annotation.FieldDisplay;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.FormType;
import com.base.sbc.module.formtype.mapper.FieldManagementMapper;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FormTypeService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.FieldDisplaySaveDto;
import com.base.sbc.module.planning.entity.FieldDisplayConfig;
import com.base.sbc.module.planning.mapper.FieldDisplayConfigMapper;
import com.base.sbc.module.planning.service.FieldDisplayConfigService;
import com.base.sbc.module.planning.vo.FieldDisplayVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private BaseController baseController;

    @Autowired
    private FormTypeService formTypeService;

    @Autowired
    private FieldManagementMapper fieldManagementMapper;

    /**
     * 表单字段类型名称
     * 维度系数
     */
    private final String FORM_TYPE_NAME = "维度系数";

    @Override
    public List<FieldDisplayVo> getConfig(String type) {
        Map<String, List<FieldDisplayVo>> result = new HashMap<>(8);
        //获取默认配置
        List<FieldDisplayVo> defaultList = new ArrayList<>(16);
        Class clzz = null;
        if (StrUtil.equals(type, STYLE_BOARD)) {
            clzz = PlanningSummaryDetailVo.class;
        } else if (StrUtil.equals(type, PLANNING_BOARD)) {
            clzz = PlanningSummaryDetailVo.class;
        }
        if (clzz == null) {
            throw new OtherException("无匹配类型");
        }
        Field[] fields = clzz.getDeclaredFields();
        // 初始化排序开始值 因为开发已经使用了这个字段 设置成 字符串类型了 使用的时候要转换类型
        String sort = "1";
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
                fd.setSort(sort);
                sort = String.valueOf(Integer.parseInt(sort) + 1);
                fd.setDynamicFields(false);
                defaultList.add(fd);
            }
        }
        // 查询「维度系数」所有列表数据
        List<FieldManagementVo> allFieldManagementList = getAllFieldManagement();
        if (ObjectUtil.isNotEmpty(allFieldManagementList)) {
            // 如果动态的维度数据不为空 那么加入默认的用户配置字段中
            for (FieldManagementVo fieldManagementVo : allFieldManagementList) {
                FieldDisplayVo fd = new FieldDisplayVo();
                fd.setField(fieldManagementVo.getFieldName());
                fd.setName(fieldManagementVo.getFieldExplain());
                // 默认不选中
                fd.setDisplay(false);
                fd.setSort(sort);
                sort = String.valueOf(Integer.parseInt(sort) + 1);
                // 设置动态字段
                fd.setDynamicFields(true);
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
                if (ObjectUtil.isEmpty(userList.size())) {
                    userList = null;
                } else {
                    // 由于维度系数会动态修改 所以此时需要判断之前存的用户配置 是否需要修改
                    // 初始化最终的配置集合
                    List<FieldDisplayVo> finalList = new ArrayList<>();
                    Map<String, FieldDisplayVo> userDataMap = userList
                            .stream()
                            .filter(FieldDisplayVo::isDynamicFields)
                            .collect(Collectors.toMap(FieldDisplayVo::getField, item -> item));
                    // 初始化新增字段的排序 新增字段设置大一点放最后面
                    String newSort = "999";
                    for (FieldManagementVo fieldManagementVo : allFieldManagementList) {
                        FieldDisplayVo fieldDisplayVo = userDataMap.get(fieldManagementVo.getFieldName());
                        // 下面添加的是根据现有「维度系数」字段新增或者添加之前的，那之前的字段不在现有「维度系数」中，就自动排除了
                        if (ObjectUtil.isNotEmpty(fieldDisplayVo)) {
                            finalList.add(fieldDisplayVo);
                        } else {
                            // 如果之前的配置没有 那么新增一个
                            FieldDisplayVo newFieldDisplayVo = new FieldDisplayVo();
                            newFieldDisplayVo.setField(fieldManagementVo.getFieldName());
                            newFieldDisplayVo.setName(fieldManagementVo.getFieldExplain());
                            // 默认不选中
                            newFieldDisplayVo.setDisplay(false);
                            newFieldDisplayVo.setSort(newSort);
                            newSort = String.valueOf(Integer.parseInt(newSort) + 1);
                            // 设置动态字段
                            newFieldDisplayVo.setDynamicFields(true);
                            finalList.add(newFieldDisplayVo);
                        }
                    }
                    // 非动态字段
                    userList = userList
                            .stream()
                            .filter(item -> !item.isDynamicFields())
                            .collect(Collectors.toList());
                    // 添加上动态字段 组成最终字段
                    userList.addAll(finalList);

                }
            } catch (Exception e) {
                log.error("解析字段配置异常", e);
            }
        }
        return Opt.ofEmptyAble(userList).orElse(defaultList);
    }

    /**
     * 获取「维度系数」的所有数据
     * @return 维度系数全部列表
     */
    public List<FieldManagementVo> getAllFieldManagement() {
        QueryFieldManagementDto queryFieldManagementDto = new QueryFieldManagementDto();
        queryFieldManagementDto.setCompanyCode(baseController.getUserCompany());
        FormType formType = formTypeService.getByOne("name", FORM_TYPE_NAME);
        if (ObjectUtil.isEmpty(formType)) {
            return new ArrayList<>();
        }
        queryFieldManagementDto.setFormTypeId(formType.getId());
        return fieldManagementMapper.getFieldManagementList(queryFieldManagementDto);
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
