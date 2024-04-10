package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.pack.entity.PackingDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PackingDictionaryMapper extends BaseMapper<PackingDictionary> {
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

    PackingDictionary queryPacking(@Param("parentId")String parentId,@Param("name")String name);
    /**
     * 查询是否存在
     */
    PackingDictionary queryPackingDictionary(@Param("dto") PackingDictionary dto);

    /**
     * 修改
     * @param dto
     * @return
     */
    int updatePacking(@Param("dto") PackingDictionary dto);
}
