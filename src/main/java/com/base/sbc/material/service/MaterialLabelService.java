package com.base.sbc.material.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.material.entity.MaterialLabel;
import com.base.sbc.material.mapper.MaterialLabelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/29 16:19:50
 */
@Service
public class MaterialLabelService {
    @Resource
    private MaterialLabelMapper materialLabelMapper;

    /**
     * 获取素材列表相关联的列表
     */
    public List<MaterialLabel> getByMaterialIds(List<String> materialIds){
        QueryWrapper<MaterialLabel> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",materialIds);
        return materialLabelMapper.selectList(queryWrapper);
    }

    /**
     * 根据id集合查询列表
     */
    public List<MaterialLabel> getByLabelIds(List<String> labelId){
        QueryWrapper<MaterialLabel> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("label_id",labelId);
        return materialLabelMapper.selectList(queryWrapper);
    }

    /**
     * 新增标签关连
     */
    public Integer add(MaterialLabel materialLabel){
        return materialLabelMapper.insert(materialLabel);
    }

    /**
     * 删除关联
     */
    public Integer del(String materialId){
        QueryWrapper<MaterialLabel> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("material_id",materialId);
        return materialLabelMapper.delete(queryWrapper);
    }
}
