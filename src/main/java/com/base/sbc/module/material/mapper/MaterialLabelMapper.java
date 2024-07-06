package com.base.sbc.module.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.vo.MaterialChildren;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/29 16:19:11
 */
@Mapper
public interface MaterialLabelMapper extends BaseMapper<MaterialLabel> {

    List<MaterialChildren> linkageQuery(@Param("search") String search, @Param("materialCategoryIds") List<String> materialCategoryIds);
}
