package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.basicsdatum.entity.BasicProcessGallery;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-03-11 11:10:52
 * @mail 247967116@qq.com
 */
@Mapper
public interface BasicProcessGalleryMapper extends BaseMapper<BasicProcessGallery> {
    List<BasicProcessGalleryVo> queryList(@Param("ew") QueryWrapper<BasicProcessGallery> ew);
}
