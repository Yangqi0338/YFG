package com.base.sbc.module.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.material.entity.MaterialCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/4/1 11:12:47
 */
public interface MaterialCollectService extends IService<MaterialCollect> {
    /**
     * 根据传入的素材id列表查询对应收藏的数量
     */
    List<Map<String,Integer>> numList(@Param("materialIds")List<String> materialIds);

    Boolean checkFolderRelation(List<String> ids);

    void mergeFolderReplace(String id, List<String> byMergeFolderIds);

    Long getCollectFileCount(String userId, List<String> byAllFileIds);
}
