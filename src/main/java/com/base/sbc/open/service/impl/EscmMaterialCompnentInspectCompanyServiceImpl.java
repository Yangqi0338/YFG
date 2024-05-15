package com.base.sbc.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.mapper.EscmMaterialCompnentInspectCompanyMapper;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/7/21 10:45:15
 * @mail 247967116@qq.com
 */
@Service
public class EscmMaterialCompnentInspectCompanyServiceImpl extends BaseServiceImpl<EscmMaterialCompnentInspectCompanyMapper,EscmMaterialCompnentInspectCompanyDto> implements EscmMaterialCompnentInspectCompanyService {
    /**
     * 查询最新的检测报告
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public List<EscmMaterialCompnentInspectCompanyDto> getListByMaterialsNo(QueryWrapper<EscmMaterialCompnentInspectCompanyDto> queryWrapper, List<String> existsIdList) {
        /*查询检查报告*/
        List<EscmMaterialCompnentInspectCompanyDto> list = list(queryWrapper);
        /*按年份和到货时间排序*/
        list.sort(CommonUtils.nullLastComparing(EscmMaterialCompnentInspectCompanyDto::getYear)
                .thenComparing(CommonUtils.nullLastComparing(EscmMaterialCompnentInspectCompanyDto::getArriveDate)).reversed());

        /*按物料分组*/
        Map<String, List<EscmMaterialCompnentInspectCompanyDto>> map = list.stream()
                .collect(CommonUtils.groupingBy(EscmMaterialCompnentInspectCompanyDto::getMaterialsNo));

        return map.values().stream()
                .map(value -> {
                    // 获取中间表数据
                    Optional<EscmMaterialCompnentInspectCompanyDto> inspectCompanyDtoOpt = value.stream().filter(it -> existsIdList.contains(it.getId())).findFirst();
                    EscmMaterialCompnentInspectCompanyDto inspectCompanyDto;
                    if (inspectCompanyDtoOpt.isPresent()) {
                        inspectCompanyDto = inspectCompanyDtoOpt.get();
                    }else {
                        inspectCompanyDto = value.get(0);
                        inspectCompanyDto.setId(null);
                    }
                    List<EscmMaterialCompnentInspectCompanyDto> companyDtoList = BeanUtil.copyToList(value, EscmMaterialCompnentInspectCompanyDto.class);
                    inspectCompanyDto.setCompanyDtoList(companyDtoList);

                    return inspectCompanyDto;
                }).collect(Collectors.toList());
    }

    /**
     * 查询吊牌中的检查报告
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public List<EscmMaterialCompnentInspectCompanyDto> getList(QueryWrapper<EscmMaterialCompnentInspectCompanyDto> queryWrapper) {
        /*查询检查报告*/
        return  baseMapper.getList(queryWrapper);
    }
}
