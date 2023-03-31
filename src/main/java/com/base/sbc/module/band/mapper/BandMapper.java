package com.base.sbc.module.band.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.band.entity.Band;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/18 17:53:34
 */
@Mapper
public interface BandMapper extends BaseMapper<Band> {
    List<Band> listQuery(Band band);

    Integer delByIds( @Param("ids")  String[] ids,@Param("updateName") String updateName,@Param("updateId") String updateId);

    Integer update(Band band);

    Integer bandStartStop(Band band);

}
