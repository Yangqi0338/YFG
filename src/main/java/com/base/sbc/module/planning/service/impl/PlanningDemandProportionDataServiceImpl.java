/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumLavationReminderExcelDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.SaveUpdateDemandProportionDataDto;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.mapper.PlanningDemandProportionDataMapper;
import com.base.sbc.module.planning.service.PlanningDemandProportionDataService;
import com.base.sbc.module.planning.service.PlanningDemandProportionSeatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：企划-需求维度数据表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDemandDimensionalityDataService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:22
 */
@Service
public class PlanningDemandProportionDataServiceImpl extends BaseServiceImpl<PlanningDemandProportionDataMapper, PlanningDemandProportionData> implements PlanningDemandProportionDataService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private PlanningDemandProportionSeatService planningDemandProportionSeatService;

    @Override
    public ApiResult saveUpdate(SaveUpdateDemandProportionDataDto saveUpdateDemandDimensionalityDataDto) {
        PlanningDemandProportionData planningDemandDimensionalityData = new PlanningDemandProportionData();
        if (!StringUtils.isEmpty(saveUpdateDemandDimensionalityDataDto.getId())) {
            /*修改*/
            planningDemandDimensionalityData = baseMapper.selectById(saveUpdateDemandDimensionalityDataDto.getId());
            BeanUtils.copyProperties(saveUpdateDemandDimensionalityDataDto, planningDemandDimensionalityData);
            planningDemandDimensionalityData.updateInit();
            baseMapper.updateById(planningDemandDimensionalityData);
        } else {
            /*新增*/
            BeanUtils.copyProperties(saveUpdateDemandDimensionalityDataDto, planningDemandDimensionalityData);
            planningDemandDimensionalityData.setCompanyCode(baseController.getUserCompany());
            planningDemandDimensionalityData.insertInit();
            baseMapper.insert(planningDemandDimensionalityData);
        }
        return ApiResult.success("操作成功");
    }

    /**
     * 批量新增修改需求占比数据表
     *
     * @param list
     * @return
     */
    @Override
    public List<PlanningDemandProportionData> batchSaveUpdate(List<SaveUpdateDemandProportionDataDto> list) {
        List<PlanningDemandProportionData> dataList = BeanUtil.copyToList(list, PlanningDemandProportionData.class);
        dataList.forEach(p -> {
            if (CommonUtils.isInitId(p.getId())) {
                p.setId(null);
            }
        });
        saveOrUpdateBatch(dataList);
        //创建维度位置信息
        planningDemandProportionSeatService.createByDemand(dataList);
        countProportion(dataList.get(0).getChannel(),dataList.get(0).getDemandId());
        return dataList;
    }

    @Override
    public ApiResult del(String id) {
        List<String> ids = StringUtils.convertList(id);

        /*重新计算占比*/
        List<PlanningDemandProportionData> dataList = baseMapper.selectBatchIds(ids);
        baseMapper.deleteBatchIds(ids);
        if(CollUtil.isNotEmpty(dataList)){
            countProportion(dataList.get(0).getChannel(),dataList.get(0).getDemandId());
        }
        return ApiResult.success("操作成功");
    }

    /**
     * 计算占比
     * @param channel
     * @param demandId
     */
    public void  countProportion(String channel,String demandId){
        /*查询全部需求占比*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("channel", channel);
        queryWrapper.eq("demand_id", demandId);
        List<PlanningDemandProportionData> list1 = baseMapper.selectList(queryWrapper);
        /*重新算zhanb*/
        List<Integer> collect = list1.stream().map(PlanningDemandProportionData::getNum).collect(Collectors.toList());
        /*去掉%*/

        /*总数*/
        int sum = collect.stream().reduce(0, Integer::sum);
        for (PlanningDemandProportionData planningDemandProportionData : list1) {
            double v2 = (double) planningDemandProportionData.getNum() / sum * 100;
            String str = String.format("%.2f", v2);
            double four = Double.parseDouble(str);
            planningDemandProportionData.setProportion(four + "%");
        }
        saveOrUpdateBatch(list1);
    }


/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
