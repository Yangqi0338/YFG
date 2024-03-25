package com.base.sbc.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public List<EscmMaterialCompnentInspectCompanyDto> getListByMaterialsNo(QueryWrapper<EscmMaterialCompnentInspectCompanyDto> queryWrapper) {
        /*查询检查报告*/
        List<EscmMaterialCompnentInspectCompanyDto> list = list(queryWrapper);
        /*按物料分组*/
        Map<String, List<EscmMaterialCompnentInspectCompanyDto>> map = list.stream()
                .collect(Collectors.groupingBy(EscmMaterialCompnentInspectCompanyDto::getMaterialsNo));

        List<EscmMaterialCompnentInspectCompanyDto> inspectCompanyDtoList = map.entrySet().stream()
                .map(entry -> {
                    List<EscmMaterialCompnentInspectCompanyDto> value = entry.getValue();
                    /*按年份获取最大的数据*/
                    EscmMaterialCompnentInspectCompanyDto maxYearItem = value.stream()
                            .max(Comparator.comparing(EscmMaterialCompnentInspectCompanyDto::getYear))
                            .orElse(null);

                    if (maxYearItem != null) {
                        /*整个分组放到集合返回前端*/
                        List<EscmMaterialCompnentInspectCompanyDto> companyDtoList = BeanUtil.copyToList(value,EscmMaterialCompnentInspectCompanyDto.class);
                        maxYearItem.setCompanyDtoList(companyDtoList);
                        maxYearItem.setInspectCompanyId(maxYearItem.getId());
                        maxYearItem.setId(null);
                    }
                    return maxYearItem;
                }).filter(Objects::nonNull).collect(Collectors.toList());
        return inspectCompanyDtoList;
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
