package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/5 9:24:20
 * @mail 247967116@qq.com
 */
@Mapper
public interface ProcessDatabaseMapper extends BaseMapper<ProcessDatabase> {
    List<String> getAllPatternPartsCode(@Param(Constants.WRAPPER) QueryWrapper<ProcessDatabase> qw);
}
