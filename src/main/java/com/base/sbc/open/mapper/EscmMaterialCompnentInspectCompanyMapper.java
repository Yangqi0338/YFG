package com.base.sbc.open.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/21 10:44:26
 * @mail 247967116@qq.com
 */
@Mapper
public interface EscmMaterialCompnentInspectCompanyMapper extends BaseMapper<EscmMaterialCompnentInspectCompanyDto> {

    /**
     * 查询最新的检测报告
     * @param materialsNos
     * @return
     */
    List<EscmMaterialCompnentInspectCompanyDto> getListByMaterialsNo(@Param("materialsNos") List<String> materialsNos);



    /**
     * 查询最新的检测报告
     * @param qw
     * @return
     */
    List<EscmMaterialCompnentInspectCompanyDto> getList(@Param(Constants.WRAPPER) QueryWrapper qw);
}
