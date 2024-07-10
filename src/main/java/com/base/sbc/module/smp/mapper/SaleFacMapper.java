package com.base.sbc.module.smp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.smp.entity.SaleFac;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
@DS("starRocks")
public interface SaleFacMapper extends BaseMapper<SaleFac> {

    List<Map<String, Object>> findSizeMap(@Param(Constants.WRAPPER) AbstractWrapper queryWrapper);
}
