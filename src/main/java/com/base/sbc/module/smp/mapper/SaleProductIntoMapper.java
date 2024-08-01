package com.base.sbc.module.smp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.smp.entity.SalesData;
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

     /**
      * 根据大货款查询合并款
      * @param goodsNoList 款号集合
      * @return 款号-合并款号
      */
     List<SalesData> queryMergeGoodsNoByGoodsNo(List<String> goodsNoList);

     /**
      * 根据合并款查询大货款
      * @param mergeGoodsNoList 合并款号集合
      * @return 款号-合并款号
      */
     List<SalesData> queryGoodsNoByMergeGoodsNo(List<String> mergeGoodsNoList);

     /**
      * 根据大货款查询销售量和投产量
      * @param goodsNoList 大货款号集合
      * @return 大货款号（prodCode） 销售量（salesNum） 投产量（productionNum）
      */
     List<SalesData> querySalesNumAndProductionNumByGoodsNos(List<String> goodsNoList);

}
