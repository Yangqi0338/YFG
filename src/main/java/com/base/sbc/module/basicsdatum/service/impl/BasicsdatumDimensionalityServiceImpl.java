/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumDimensionalityDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumDimensionalityMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumDimensionalityVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.CheckMutexDto;
import com.base.sbc.module.planning.service.PlanningDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-纬度系数表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0  
 */
@Service
public class BasicsdatumDimensionalityServiceImpl extends BaseServiceImpl<BasicsdatumDimensionalityMapper, BasicsdatumDimensionality> implements BasicsdatumDimensionalityService {

    @Autowired
    private PlanningDemandService planningDemandService;
    /**
     * 获取维度数据
     *
     * @param dto
     * @return
     */
    @Override
    public Map getDimensionality(BasicsdatumDimensionalityDto dto) {
        if(StrUtil.isEmpty(dto.getProdCategory())|| StrUtil.isEmpty(dto.getCoefficientTemplateId())){
            throw new OtherException("品类或模板id不能为空");
        }
        BaseQueryWrapper<BasicsdatumDimensionality> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tbd.prod_category",dto.getProdCategory());
        queryWrapper.eq(StrUtil.isNotBlank(dto.getProdCategory2nd()),"tbd.prod_category2nd",dto.getProdCategory2nd());
        queryWrapper.eq("tbd.del_flag",BaseGlobal.NO);
        queryWrapper.eq("tbd.coefficient_template_id",dto.getCoefficientTemplateId());
        queryWrapper.orderByAsc("tbd.sort");
        List<BasicsdatumDimensionalityVo> dimensionalityList = baseMapper.getDimensionality(queryWrapper);

        if(CollUtil.isEmpty(dimensionalityList)){
            return new HashMap();
        }
        return dimensionalityList.stream().collect(Collectors.groupingBy(p -> p.getGroupName()));
    }

    /**
     * 保存/编辑维度标签
     *
     * @param dtoList
     * @return
     */
    @Override
    public boolean batchSaveDimensionality(List<BasicsdatumDimensionalityDto> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            throw new OtherException("系数列表数据为空");
        }
        List<BasicsdatumDimensionalityDto> dtoList1 = dtoList.stream().filter(d -> StrUtil.isEmpty(d.getCoefficientTemplateId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(dtoList1)) {
            throw new OtherException("列表数据缺少模板id");
        }

        CheckMutexDto checkMutexDto = new CheckMutexDto();
        checkMutexDto.setChannel(dtoList.get(0).getChannel());
        checkMutexDto.setCoefficientTemplateId(dtoList.get(0).getCoefficientTemplateId());
        checkMutexDto.setProdCategory(dtoList.get(0).getProdCategory());
        checkMutexDto.setProdCategory2nd(dtoList.get(0).getProdCategory2nd());
        checkMutex(checkMutexDto);
        List<BasicsdatumDimensionality> list = BeanUtil.copyToList(dtoList, BasicsdatumDimensionality.class);
        list.forEach(p -> {
            if (CommonUtils.isInitId(p.getId())) {
                p.setId(null);
            }
        });
        saveOrUpdateBatch(list);
        return true;
    }

    /**
     * 增加检查互斥品类和中类互斥
     *
     * @param checkMutexDto
     */
    @Override
    public void checkMutex(CheckMutexDto checkMutexDto) {
        //品类和中类互斥,当前如果是中类,查询是否存在品类,如果是品类,查询是否存在中类
        QueryWrapper<BasicsdatumDimensionality> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coefficient_template_id",checkMutexDto.getCoefficientTemplateId());
        queryWrapper.eq("channel", checkMutexDto.getChannel());
        if (StrUtil.isNotBlank(checkMutexDto.getProdCategory2nd())) {
            queryWrapper.eq("prod_category", checkMutexDto.getProdCategory());
            long count = count(queryWrapper);
            if (count>0) {
                throw new OtherException("已存在品类维度");
            }
        } else {
            queryWrapper.isNotNull("prod_category2nd");
            queryWrapper.ne("prod_category2nd", "");
            long count = count(queryWrapper);
            if (count>0) {
                throw new OtherException("已存在中类维度");
            }
        }
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
