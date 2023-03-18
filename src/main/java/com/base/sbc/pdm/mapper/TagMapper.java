package com.base.sbc.pdm.mapper;

import com.base.sbc.pdm.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/18 10:29:04
 */
@Mapper
public interface TagMapper {
    /**
     * 条件查询标签
     * @param tag 条件封装
     * @return 结果集
     */
    List<Tag> listQuery(Tag tag);

    Integer del(Tag tag);

    Integer update(Tag tag);

    Integer add(Tag tag);

    /**
     * 根据名称和类别查询是否存在
     */
    Tag getByNameAndGroupId(Tag tag);
}
