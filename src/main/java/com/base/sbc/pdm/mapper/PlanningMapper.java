package com.base.sbc.pdm.mapper;

import com.base.sbc.pdm.entity.Band;
import com.base.sbc.pdm.entity.Planning;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/17 11:30:17
 */
@Mapper
public interface PlanningMapper {
    List<Planning> listQuery(Planning planning);

    Integer delByIds(String[] ids);

    Integer update(Planning planning);
}
