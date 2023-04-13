package com.base.sbc.module.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.material.entity.MaterialSize;
import com.base.sbc.module.material.mapper.MaterialSizeMapper;
import com.base.sbc.module.material.service.MaterialSizeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/1 10:02:59
 */
@Service
public class MaterialSizeServiceImpl extends ServicePlusImpl<MaterialSizeMapper,MaterialSize> implements MaterialSizeService {

    @Resource
    private MaterialSizeMapper materialSizeMapper;
    /**
     * 获取素材列表相关联的列表
     *
     * @param materialIds
     */
    @Override
    public List<MaterialSize> getByMaterialIds(List<String> materialIds) {
        QueryWrapper<MaterialSize> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",materialIds);
        return materialSizeMapper.selectList(queryWrapper);
    }

    /**
     * 根据id集合查询列表
     *
     * @param sizeId
     */
    @Override
    public List<MaterialSize> getBySizeId(String sizeId){
        QueryWrapper<MaterialSize> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("size_id",sizeId);
        return materialSizeMapper.selectList(queryWrapper);
    }
}
