package com.base.sbc.open.service.impl;

import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.mapper.EscmMaterialCompnentInspectCompanyMapper;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param materialsNos
     * @return
     */
    @Override
    public List<EscmMaterialCompnentInspectCompanyDto> getListByMaterialsNo(List<String> materialsNos) {
        return baseMapper.getListByMaterialsNo(materialsNos);
    }
}
