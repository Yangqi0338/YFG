package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/6/27 17:23
 * @mail 247967116@qq.com
 */
@Mapper
public interface SpecificationGroupMapper extends BaseMapper<SpecificationGroup> {
    List<Map<String, String>> listIdName();
}
