package com.base.sbc.module.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.common.service.impl.IServiceImpl;
import com.base.sbc.module.material.entity.MaterialColor;
import com.base.sbc.module.material.mapper.MaterialColorMapper;
import com.base.sbc.module.material.service.MaterialColorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/1 11:20:07
 */
@Service
public class MaterialColorServiceImpl extends IServiceImpl<MaterialColorMapper,MaterialColor> implements MaterialColorService {
    @Resource
    private MaterialColorMapper materialColorMapper;
    /**
     * 获取素材列表相关联的列表
     *
     * @param materialIds
     */
    @Override
    public List<MaterialColor> getByMaterialIds(List<String> materialIds) {
        QueryWrapper<MaterialColor> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",materialIds);
        return materialColorMapper.selectList(queryWrapper);
    }

    /**
     * 根据id集合查询列表
     *
     * @param colorId
     */
    @Override
    public List<MaterialColor> getColorId(String colorId) {
        QueryWrapper<MaterialColor> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("color_id",colorId);
        return materialColorMapper.selectList(queryWrapper);
    }


}
