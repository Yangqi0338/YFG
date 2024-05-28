package com.base.sbc.module.smp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
@DS("starRocks")
public interface SaleProductIntoMapper extends BaseMapper {

     List<Map<String, Object>> querySaleIntoPage1(@Param("ew") QueryWrapper<?> queryWrapper, @Param("channel") String channel, @Param("total") Integer total);

     List<Map<String, Object>> querySaleIntoPage(@Param("ew") QueryWrapper<?> queryWrapper, @Param("total") Integer total);

     List<Map<String, Object>> queryYearProductionOrSalesStatistics(@Param("ew") QueryWrapper<?> queryWrapper);
}
