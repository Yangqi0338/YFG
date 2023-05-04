/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.dto.SaveUpdateDemandProportionDataDto;
import com.base.sbc.module.planning.mapper.PlanningDemandProportionDataMapper;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.service.PlanningDemandProportionDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
public class PlanningDemandProportionDataServiceImpl extends ServicePlusImpl<PlanningDemandProportionDataMapper, PlanningDemandProportionData> implements PlanningDemandProportionDataService {
    @Autowired
    private BaseController baseController;

    @Override
    public ApiResult saveUpdate(SaveUpdateDemandProportionDataDto saveUpdateDemandDimensionalityDataDto) {
        PlanningDemandProportionData planningDemandDimensionalityData = new PlanningDemandProportionData();
        QueryWrapper<PlanningDemandProportionData> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("demand_id",saveUpdateDemandDimensionalityDataDto.getDemandId())
        .eq("classify",saveUpdateDemandDimensionalityDataDto.getClassify());
        List<PlanningDemandProportionData> list = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            throw new OtherException("数据重复");
        }
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

    @Override
    public ApiResult del(String id) {
        List<String> ids = StringUtils.convertList(id);
        baseMapper.deleteBatchIds(ids);
        return ApiResult.success("操作成功");
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
