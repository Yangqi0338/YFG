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

    int addPacking(@Param("dto") PackingDictionary dto);
}
