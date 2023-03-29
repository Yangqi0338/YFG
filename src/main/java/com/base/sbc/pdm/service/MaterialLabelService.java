package com.base.sbc.pdm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.pdm.entity.MaterialLabel;
import com.base.sbc.pdm.mapper.MaterialLabelMapper;
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
    public List<MaterialLabel> list(List<String> ids){
        QueryWrapper<MaterialLabel> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",ids);
        return materialLabelMapper.selectList(queryWrapper);
    }
}
