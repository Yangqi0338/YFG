package com.base.sbc.material.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.material.entity.MaterialColor;
import com.base.sbc.material.mapper.MaterialColorMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/31 16:08:52
 */
@Service
public class MaterialColorService {
    @Resource
    private MaterialColorMapper materialColorMapper;
    /**
     * 获取素材列表相关联的列表
     */
    public List<MaterialColor> getByMaterialIds(List<String> materialIds){
        QueryWrapper<MaterialColor> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",materialIds);
        return materialColorMapper.selectList(queryWrapper);
    }

    /**
     * 根据id集合查询列表
     */
    public List<MaterialColor> getColorId(String colorId){
        QueryWrapper<MaterialColor> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("color_id",colorId);
        return materialColorMapper.selectList(queryWrapper);
    }

    /**
     * 新增尺码关联
     */
    public Integer add(MaterialColor materialColor){
        return materialColorMapper.insert(materialColor);
    }

    /**
     * 删除关联
     */
    public Integer del(String materialId){
        QueryWrapper<MaterialColor> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("material_id",materialId);
        return materialColorMapper.delete(queryWrapper);
    }
}
