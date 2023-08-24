/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.dto.ScoreConfigSearchDto;
import com.base.sbc.module.patternmaking.entity.ScoreConfig;
import com.base.sbc.module.patternmaking.mapper.ScoreConfigMapper;
import com.base.sbc.module.patternmaking.service.ScoreConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：默认评分配置 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.ScoreConfigService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-24 13:37:10
 */
@Service
public class ScoreConfigServiceImpl extends BaseServiceImpl<ScoreConfigMapper, ScoreConfig> implements ScoreConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<ScoreConfig> pageByDto(ScoreConfigSearchDto dto) {
        ScoreConfig search = BeanUtil.copyProperties(dto, ScoreConfig.class);
        BaseQueryWrapper<ScoreConfig> qw = new BaseQueryWrapper<>();
        commonQw(search, qw);
        qw.andLike(dto.getSearch(), "prod_category1st_name", "prod_category_name", "prod_category2nd_name");
        Page<ScoreConfig> objects = PageHelper.startPage(dto);
        List<ScoreConfig> list = list(qw);
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ScoreConfig saveBy(ScoreConfig scoreConfig) {
        if (CommonUtils.isInitId(scoreConfig.getId())) {
            checkRepeat(scoreConfig);
            save(scoreConfig);
        } else {
            updateScore(scoreConfig);

        }
        return scoreConfig;
    }

    @Override
    public void checkRepeat(ScoreConfig search) {
        BaseQueryWrapper<ScoreConfig> qw = new BaseQueryWrapper<>();
        commonQw(search, qw);
        long count = count(qw);
        if (count > 0) {
            throw new OtherException("有重复配置");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateScore(ScoreConfig scoreConfig) {
        ScoreConfig update = new ScoreConfig();
        update.setPatternDefaultScore(scoreConfig.getPatternDefaultScore());
        UpdateWrapper<ScoreConfig> uw = new UpdateWrapper<>();
        uw.eq("id", scoreConfig.getId());
        update(update, uw);
        return true;
    }

    @Override
    public ScoreConfig findOne(ScoreConfigSearchDto dto) {
        ScoreConfig search = BeanUtil.copyProperties(dto, ScoreConfig.class);
        BaseQueryWrapper<ScoreConfig> qw = new BaseQueryWrapper<>();
        commonQw(search, qw);
        qw.last("limit 1");
        return getOne(qw);
    }

    private void commonQw(ScoreConfig search, BaseQueryWrapper<ScoreConfig> qw) {
        qw.lambda().eq(ScoreConfig::getCompanyCode, getCompanyCode());
        qw.notEmptyEq("prod_category1st", search.getProdCategory1st());
        qw.notEmptyEq("prod_category", search.getProdCategory());
        qw.notEmptyEq("prod_category2nd", search.getProdCategory2nd());
    }

// 自定义方法区 不替换的区域【other_end】

}
