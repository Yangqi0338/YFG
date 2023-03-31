package com.base.sbc.pdm.service.material;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.pdm.entity.material.MaterialSize;
import com.base.sbc.pdm.mapper.material.MaterialSizeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/31 9:36:01
 */
@Service
public class MaterialSizeService {

    @Resource
    private MaterialSizeMapper materialSizeMapper;
    /**
     * 获取素材列表相关联的列表
     */
    public List<MaterialSize> getByMaterialIds(List<String> materialIds){
        QueryWrapper<MaterialSize> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",materialIds);
        return materialSizeMapper.selectList(queryWrapper);
    }

    /**
     * 根据id集合查询列表
     */
    public List<MaterialSize> getBySizeId(String sizeId){
        QueryWrapper<MaterialSize> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("size_id",sizeId);
        return materialSizeMapper.selectList(queryWrapper);
    }

    /**
     * 新增尺码关联
     */
    public Integer add(MaterialSize materialSize){
        return materialSizeMapper.insert(materialSize);
    }

    /**
     * 删除关联
     */
    public Integer del(String materialId){
        QueryWrapper<MaterialSize> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("material_id",materialId);
        return materialSizeMapper.delete(queryWrapper);
    }
}
