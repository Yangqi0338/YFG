package com.base.sbc.module.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.material.entity.MaterialCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/4/1 11:13:28
 */
@Mapper
public interface MaterialCollectMapper extends BaseMapper<MaterialCollect> {
    /**
     * 根据传入的素材id列表查询对应收藏的数量
     */
    List<Map<String,Integer>> numList(@Param("materialIds")List<String> materialIds);

    Long getCollectFileCount(@Param("userId")String userId, @Param("fileIds")List<String> byAllFileIds);
}
