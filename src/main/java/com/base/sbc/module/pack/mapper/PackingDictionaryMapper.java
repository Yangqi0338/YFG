package com.base.sbc.module.pack.mapper;

import com.base.sbc.module.pack.entity.PackingDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PackingDictionaryMapper {
    /**
     * 列表查询
     * @return
     */
    List<PackingDictionary> listPackingDictionary();

    /**
     * 新增
     * @param dto
     * @return
     */
    int addPacking(@Param("dto") PackingDictionary dto);
    /**
     * 查询是否存在
     */
    PackingDictionary queryPackingDictionary(@Param("dto") PackingDictionary dto);

    int updatePacking(@Param("dto") PackingDictionary dto);
}
