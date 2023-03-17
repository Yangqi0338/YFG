package com.base.sbc.pdm.mapper;

import com.base.sbc.pdm.entity.Planning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/17 11:30:17
 */
@Mapper
public interface PlanningMapper {

    @Select("select * from t_planning")
    List<Planning> getList();
}
