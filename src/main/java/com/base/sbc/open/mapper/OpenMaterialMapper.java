package com.base.sbc.open.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.open.dto.OpenMaterialDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/15 10:07
 */
@Mapper
public interface OpenMaterialMapper extends BaseMapper<OpenMaterialDto> {

    List<OpenMaterialDto> getMaterialList(String companyCode);
}
