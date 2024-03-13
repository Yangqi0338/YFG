package com.base.sbc.module.basicsdatum.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.basicsdatum.dto.BasicProcessGalleryDto;
import com.base.sbc.module.basicsdatum.entity.BasicProcessGallery;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-03-11 11:11:14
 * @mail 247967116@qq.com
 */
public interface BasicProcessGalleryService extends BaseService<BasicProcessGallery> {
    PageInfo<BasicProcessGalleryVo> queryPage(BasicProcessGalleryDto basicProcessGalleryDto);

    List<BasicProcessGalleryVo> queryList(QueryWrapper<BasicProcessGallery> queryWrapper);

    QueryWrapper<BasicProcessGallery> buildQueryWrapper(BasicProcessGalleryDto basicProcessGalleryDto);

    List<BasicProcessGalleryVo> export(BasicProcessGalleryDto basicProcessGalleryDto);
}
