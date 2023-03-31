package com.base.sbc.module.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.material.dao.MaterialAllDto;
import com.base.sbc.module.material.entity.Material;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 19:07:58
 */
@Mapper
public interface MaterialMapper extends BaseMapper<Material> {
    /**
     * 多条件关联查询
     */
    List<MaterialAllDto> listQuery(MaterialAllDto materialAllDto);
}
