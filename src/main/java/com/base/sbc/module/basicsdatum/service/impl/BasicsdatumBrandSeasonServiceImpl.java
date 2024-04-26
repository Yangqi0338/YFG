/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumBrandSeasonDto;
import com.base.sbc.module.basicsdatum.enums.MonthEnum;
import com.base.sbc.module.basicsdatum.enums.SeasonEnum;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBrandSeasonVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBrandSeasonMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBrandSeason;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBrandSeasonService;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 类描述：品牌-季度表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBrandSeasonService
 * @author 谭博文
 * @email your email
 * @date 创建时间：2024-4-9 9:42:49
 * @version 1.0  
 */
@Service
public class BasicsdatumBrandSeasonServiceImpl extends BaseServiceImpl<BasicsdatumBrandSeasonMapper, BasicsdatumBrandSeason> implements BasicsdatumBrandSeasonService {

// 自定义方法区 不替换的区域【other_start】
    @Override
    public ApiResult addAndUpdateBasicsdatumBrandSeason(BasicsdatumBrandSeason basicsdatumBrandSeason) {
        if (null == basicsdatumBrandSeason) {
            return ApiResult.error("参数为空", 500);
        }

        ApiResult apiResult = checkParam(basicsdatumBrandSeason);
        if (!apiResult.getSuccess()) {
            return apiResult;
        }
        if (StringUtils.isBlank(basicsdatumBrandSeason.getId())){
            save(basicsdatumBrandSeason);
            return ApiResult.success("操作完成");
        }
        updateById(basicsdatumBrandSeason);
        return ApiResult.success("操作完成");
    }

    @Override
    public PageInfo queryPage(BasicsdatumBrandSeasonDto basicsdatumBrandSeasonDto) {
        if (null == basicsdatumBrandSeasonDto) {
            return null;
        }
        BaseQueryWrapper<BasicsdatumBrandSeason> queryWrapper = new BaseQueryWrapper<>();
        if (StringUtils.isNotBlank(basicsdatumBrandSeasonDto.getBrand())) {
            queryWrapper.eq("brand", basicsdatumBrandSeasonDto.getBrand());
        }
        if (StringUtils.isNotBlank(basicsdatumBrandSeasonDto.getSeason())) {
            queryWrapper.eq("season", basicsdatumBrandSeasonDto.getSeason());
        }
        if (StringUtils.isNotBlank(basicsdatumBrandSeasonDto.getMonth())) {
            queryWrapper.eq("month", basicsdatumBrandSeasonDto.getMonth());
        }
        Page<BasicsdatumBrandSeason> page = PageHelper.startPage(basicsdatumBrandSeasonDto);
        getBaseMapper().selectList(queryWrapper);
        return page.toPageInfo();
    }


    // 自定义方法区 不替换的区域【other_end】
	private ApiResult checkParam(BasicsdatumBrandSeason basicsdatumBrandSeason) {
        if (StringUtils.isBlank(basicsdatumBrandSeason.getBrand())) {
            return ApiResult.error("品牌字段为空", 500);
        }
        if (StringUtils.isBlank(basicsdatumBrandSeason.getSeason()) || null == SeasonEnum.getBycode(basicsdatumBrandSeason.getSeason())) {
            return ApiResult.error("季节字段为空或者不存在的季节code", 500);
        }
        if (StringUtils.isBlank(basicsdatumBrandSeason.getMonth()) || null == MonthEnum.getBycode(basicsdatumBrandSeason.getMonth())) {
            return ApiResult.error("月份字段为空或者不存在的月份code", 500);
        }

        // 校验品牌-季度-月份是否存在数据库
        QueryWrapper<BasicsdatumBrandSeason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand", basicsdatumBrandSeason.getBrand());
        List<BasicsdatumBrandSeason> basicsdatumBrandSeasons = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(basicsdatumBrandSeasons)) {
            return ApiResult.success();
        }
        // 是否已经添加该月份
        BasicsdatumBrandSeason monthBrandSeason = basicsdatumBrandSeasons.stream().filter(b ->StringUtils.equals(b.getMonth(), basicsdatumBrandSeason.getMonth())).findFirst().orElse(null);
        if (null != monthBrandSeason) {
            if (StringUtils.isBlank(basicsdatumBrandSeason.getId())) {
                return ApiResult.error("该月份已存在", 500);
            }
            if (!StringUtils.equals(monthBrandSeason.getId(), basicsdatumBrandSeason.getId())) {
                return ApiResult.error("该月份已存在", 500);
            }
        }
        return ApiResult.success();
    }


}
