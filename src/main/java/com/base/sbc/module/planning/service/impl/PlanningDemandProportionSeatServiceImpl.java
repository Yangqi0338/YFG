/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.enums.SeatMatchFlagEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.mapper.PlanningDemandProportionSeatMapper;
import com.base.sbc.module.planning.entity.PlanningDemandProportionSeat;
import com.base.sbc.module.planning.service.PlanningDemandProportionSeatService;
import com.base.sbc.module.style.vo.DemandOrderSkcVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 类描述：企划-需求占比坑位表 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDemandProportionSeatService
 * @email your email
 * @date 创建时间：2023-9-14 11:16:44
 */
@Service
public class PlanningDemandProportionSeatServiceImpl extends BaseServiceImpl<PlanningDemandProportionSeatMapper, PlanningDemandProportionSeat> implements PlanningDemandProportionSeatService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void createByDemand(List<PlanningDemandProportionData> dataList) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        List<String> pids = new ArrayList<>();
        List<PlanningDemandProportionSeat> addList = new ArrayList<>();
        for (PlanningDemandProportionData p : dataList) {
            pids.add(p.getId());
            if (p.getNum() != null) {
                for (Integer i = 0; i < p.getNum(); i++) {
                    PlanningDemandProportionSeat seat = new PlanningDemandProportionSeat();
                    seat.setDemandId(p.getDemandId());
                    seat.setPlanningDemandProportionDataId(p.getId());
                    seat.setMatchFlag(SeatMatchFlagEnum.NO.getValue());
                    addList.add(seat);
                }
            }

        }
        //删除之前的
        delByPid(pids);
        saveBatch(addList);

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delByPid(List<String> pid) {
        QueryWrapper delQw = new QueryWrapper();
        delQw.in("planning_demand_proportion_data_id", pid);
        remove(delQw);
    }

    @Override
    public List<PlanningDemandProportionSeat> findByPid(Collection<String> pdIds) {
        QueryWrapper qw = new QueryWrapper();
        qw.in("planning_demand_proportion_data_id", pdIds);
        qw.orderByAsc("match_flag");
        return list(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean restSeatMatch(String id) {
        UpdateWrapper<PlanningDemandProportionSeat> uw = new UpdateWrapper<>();
        uw.lambda().set(PlanningDemandProportionSeat::getMatchFlag, SeatMatchFlagEnum.NO.getValue())
                .set(PlanningDemandProportionSeat::getStyleColorId, "")
                .in(PlanningDemandProportionSeat::getId, StrUtil.split(id, CharUtil.COMMA));
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean seatMatch(DemandOrderSkcVo bean) {
        PlanningDemandProportionSeat byId = getById(bean.getPlanningDemandProportionSeatId());
        if (byId == null) {
            throw new OtherException("无坑位信息");
        }
        SeatMatchFlagEnum seatMatchFlagEnum = SeatMatchFlagEnum.NO;
        String styleColorId = "";
        if (StrUtil.isNotBlank(bean.getStyleColorId())) {
            seatMatchFlagEnum = SeatMatchFlagEnum.CUSTOM_SEAT;
            styleColorId = bean.getStyleColorId();
        }
        UpdateWrapper<PlanningDemandProportionSeat> uw = new UpdateWrapper<>();
        uw.lambda().set(PlanningDemandProportionSeat::getMatchFlag, seatMatchFlagEnum.getValue())
                .set(PlanningDemandProportionSeat::getStyleColorId, styleColorId)
                .eq(PlanningDemandProportionSeat::getId, byId.getId());
        update(uw);
        return true;
    }

// 自定义方法区 不替换的区域【other_end】

}

