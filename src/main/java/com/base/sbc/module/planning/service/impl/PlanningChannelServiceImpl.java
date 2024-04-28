/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.CountVo;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.dto.PlanningChannelDto;
import com.base.sbc.module.planning.dto.PlanningChannelSearchDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningChannelMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：企划-渠道 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningChannelService
 * @email your email
 * @date 创建时间：2023-7-21 16:00:35
 */
@Service
public class PlanningChannelServiceImpl extends BaseServiceImpl<PlanningChannelMapper, PlanningChannel> implements PlanningChannelService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    PlanningSeasonService planningSeasonService;
    @Autowired
    PlanningCategoryItemService planningCategoryItemService;
    @Autowired
    StyleService styleService;
    @Autowired
    AmcFeignService amcFeignService;

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PlanningChannelVo saveByDto(PlanningChannelDto dto) {
//        checkedRepeat(dto);
        if (CommonUtils.isInitId(dto.getId())) {
            PlanningSeason planningSeason = planningSeasonService.getById(dto.getPlanningSeasonId());
            if (planningSeason == null) {
                throw new OtherException("产品季信息为空");
            }

            QueryWrapper<PlanningChannel> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("planning_season_id",dto.getPlanningSeasonId());
            queryWrapper.eq("channel",dto.getChannel());
            long count = this.count(queryWrapper);
            if(count>0){
                throw new OtherException("该渠道已存在");
            }

            PlanningChannel planningChannel = BeanUtil.copyProperties(dto, PlanningChannel.class);
            BeanUtil.copyProperties(planningSeason, planningChannel);
            CommonUtils.resetCreateUpdate(planningChannel);
            planningChannel.setId(null);
            Integer maxSort = super.getBaseMapper().getMaxSort(dto.getPlanningSeasonId());
            planningChannel.setSort(Objects.isNull(maxSort) ? 1 : maxSort + 1);
            save(planningChannel);
            return BeanUtil.copyProperties(planningChannel, PlanningChannelVo.class);
        } else {
            PlanningChannel planningChannel = getById(dto.getId());
            if (planningChannel == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }

            QueryWrapper<PlanningChannel> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("planning_season_id",dto.getPlanningSeasonId());
            queryWrapper.eq("channel",dto.getChannel());
            queryWrapper.ne("id",dto.getId());
            long count = this.count(queryWrapper);
            if(count>0){
                throw new OtherException("该渠道已存在");
            }

            planningChannel.setChannel(dto.getChannel());
            planningChannel.setChannelName(dto.getChannelName());
            planningChannel.setSex(dto.getSex());
            planningChannel.setSexName(dto.getSexName());
            planningChannel.setProductLine(dto.getProductLine());
            planningChannel.setProductLineName(dto.getProductLineName());

            updateById(planningChannel);
            //修改对应的坑位信息
            planningCategoryItemService.updateByChannelChange(planningChannel);
            styleService.updateByChannelChange(planningChannel);
            return BeanUtil.copyProperties(planningChannel, PlanningChannelVo.class);
        }
    }

    @Override
    public void checkedRepeat(PlanningChannelDto dto) {
        QueryWrapper<PlanningChannel> qw = new QueryWrapper();
        qw.lambda().eq(PlanningChannel::getPlanningSeasonId, dto.getPlanningSeasonId())
                .eq(PlanningChannel::getChannel, dto.getChannel())
                .eq(PlanningChannel::getSex, dto.getSex())
        ;
        if (!CommonUtils.isInitId(dto.getId())) {
            qw.ne("id", dto.getId());
        }
        long count = count(qw);
        if (count > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
    }

    @Override
    public PageInfo<PlanningChannelVo> channelPageInfo(PlanningChannelSearchDto dto) {
        BaseQueryWrapper<PlanningChannel> qw = new BaseQueryWrapper<>();
        qw.lambda().eq(StrUtil.isNotBlank(dto.getPlanningSeasonId()), PlanningChannel::getPlanningSeasonId, dto.getPlanningSeasonId());
        qw.eq("c.del_flag", BaseGlobal.NO);
        qw.notEmptyEq("s.brand", dto.getBrand());
        qw.notEmptyEq("s.year", dto.getYear());
        qw.notEmptyEq("s.season", dto.getSeason());
        qw.notEmptyEq("c.channel", dto.getChannel());
        qw.notEmptyLike("c.sex", dto.getSex());
        qw.orderByDesc("id");
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PlanningChannel.getK());
        Page<PlanningChannelVo> page = PageHelper.startPage(dto);
        List<PlanningChannelVo> list = getBaseMapper().list(qw);
        PageInfo<PlanningChannelVo> pageInfo = page.toPageInfo();
        if (CollUtil.isNotEmpty(list)) {
            amcFeignService.setUserAvatarToList(list);
            //统计skc
            List<String> channelIds = list.stream().map(PlanningChannelVo::getId).collect(Collectors.toList());
            Map<String, Long> channelTotal = planningCategoryItemService.totalSkcByChannel(channelIds);
            for (PlanningChannelVo planningChannelVo : list) {
                planningChannelVo.setSkcCount(channelTotal.getOrDefault(planningChannelVo.getId(), 0L));
            }
        }


        return pageInfo;
    }

    @Override
    public boolean checkHasSub(String id) {
        BaseQueryWrapper<PlanningCategoryItem> countQw = new BaseQueryWrapper<>();
        countQw.eq("planning_channel_id", id);
        countQw.eq("del_flag", BaseGlobal.NO);
        long count = planningCategoryItemService.count(countQw);
        return count > 0;
    }

    @Override
    public boolean delChannel(String id) {
        return removeBatchByIds(StrUtil.split(id, CharUtil.COMMA));
    }

    @Override
    public Map<String, Long> countByPlanningSeason() {
        QueryWrapper qw = new QueryWrapper();
        qw.select("planning_season_id as label,count(1) as `count`");
        qw.eq("del_flag", BaseGlobal.NO);
        qw.groupBy("planning_season_id");

        Map<String, Long> result = new HashMap<>(16);
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.PlanningChannel.getK());
        List<CountVo> list = getBaseMapper().selectIdCount(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (CountVo countVo : list) {
                result.put(countVo.getLabel(), countVo.getCount());
            }
        }
        return result;
    }

    @Override
    public List<SelectOptionsVo> channelClassifSelection(String planningSeasonId) {
        return super.getBaseMapper().channelClassifSelection(planningSeasonId);
    }


// 自定义方法区 不替换的区域【other_end】

}
