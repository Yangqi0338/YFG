package com.base.sbc.pdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.pdm.entity.Band;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/18 17:53:34
 */
@Mapper
public interface BandMapper extends BaseMapper<Band> {
    List<Band> listQuery(Band band);

    Integer delByIds(String[] ids);

    Integer update(Band band);
}
