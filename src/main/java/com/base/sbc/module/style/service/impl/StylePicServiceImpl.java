/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.entity.StylePic;
import com.base.sbc.module.style.mapper.StylePicMapper;
import com.base.sbc.module.style.service.StylePicService;
import com.base.sbc.module.style.vo.StylePicVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 类描述：款式设计-设计款图 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.StylePicService
 * @email your email
 * @date 创建时间：2023-10-20 13:15:21
 */
@Service
public class StylePicServiceImpl extends BaseServiceImpl<StylePicMapper, StylePic> implements StylePicService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    StylePicUtils stylePicUtils;

    @Override
    public int getNextSort(String styleId) {
        List<Integer> sorts = baseMapper.getSorts(styleId);
        if (CollUtil.isEmpty(sorts)) {
            return 1;
        }
        CollUtil.sort(sorts, Integer::compareTo);
        int max = CollUtil.getLast(sorts);
        for (int i = 0; i < max; i++) {
            if ((i + 1) != sorts.get(i)) {
                return i + 1;
            }
        }
        return max + 1;
    }

    @Override
    public List<StylePicVo> listByStyleId(String id) {
        UserCompany userCompany = companyUserInfo.get();
        GroupUser userBy = new GroupUser();
        userBy.setUsername(userCompany.getUserId());
        userBy.setName(userCompany.getAliasUserName());
        QueryWrapper<StylePic> qw = new QueryWrapper<>();
        qw.lambda().eq(StylePic::getStyleId, id);
        qw.orderByDesc("main_pic_flag");
        qw.orderByAsc("sort");
        List<StylePic> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(stylePic -> {
                StylePicVo stylePicVo = BeanUtil.copyProperties(stylePic, StylePicVo.class);
                stylePicVo.setUrl(stylePicUtils.getStyleUrl(stylePic.getFileName()));
                return stylePicVo;
            }).collect(Collectors.toList());
        }
        return null;
    }

// 自定义方法区 不替换的区域【other_end】

}
