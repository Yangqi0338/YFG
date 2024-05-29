package com.base.sbc.open.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/21 10:44:18
 * @mail 247967116@qq.com
 */
public interface EscmMaterialCompnentInspectCompanyService extends BaseService<EscmMaterialCompnentInspectCompanyDto> {


    /**
     * 查询最新的检测报告
     * @param queryWrapper
     * @return
     */
    List<EscmMaterialCompnentInspectCompanyDto> getListByMaterialsNo(QueryWrapper<EscmMaterialCompnentInspectCompanyDto> queryWrapper, List<String> existsIdList);


    /**
     * 查询吊牌中的检查报告
     * @param queryWrapper
     * @return
     */
    List<EscmMaterialCompnentInspectCompanyDto>  getList(QueryWrapper<EscmMaterialCompnentInspectCompanyDto> queryWrapper);

}
