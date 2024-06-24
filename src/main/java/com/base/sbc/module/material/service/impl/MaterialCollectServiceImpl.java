package com.base.sbc.module.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.material.entity.MaterialCollect;
import com.base.sbc.module.material.mapper.MaterialCollectMapper;
import com.base.sbc.module.material.service.MaterialCollectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;

/**
 * @author 卞康
 * @date 2023/4/1 11:12:47
 */
@Service
public class MaterialCollectServiceImpl extends ServiceImpl<MaterialCollectMapper, MaterialCollect> implements MaterialCollectService {
    /**
     * 根据传入的素材id列表查询对应收藏的数量
     *
     * @param materialIds
     */
    @Override
    public List<Map<String, Integer>> numList(List<String> materialIds) {
        return this.getBaseMapper().numList(materialIds);
    }

    @Override
    public Boolean checkFolderRelation(List<String> ids) {
        if (CollUtil.isEmpty(ids)){
            return true;
        }
        QueryWrapper<MaterialCollect> qw = new QueryWrapper<>();
        qw.lambda().in(MaterialCollect::getFolderId,ids);
        qw.lambda().eq(MaterialCollect::getDelFlag,"0");
        return count(qw) > 0;
    }

    @Override
    public void mergeFolderReplace(String id, List<String> byMergeFolderIds) {
        UpdateWrapper<MaterialCollect> uw = new UpdateWrapper<>();
        uw.lambda().in(MaterialCollect::getFolderId,byMergeFolderIds);
        uw.lambda().set(MaterialCollect::getFolderId,id);
        update(uw);
    }
}
