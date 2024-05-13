package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicProcessGalleryDto;
import com.base.sbc.module.basicsdatum.entity.BasicProcessGallery;
import com.base.sbc.module.basicsdatum.mapper.BasicProcessGalleryMapper;
import com.base.sbc.module.basicsdatum.service.BasicProcessGalleryService;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-03-11 11:11:33
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BasicProcessGalleryServiceImpl extends BaseServiceImpl<BasicProcessGalleryMapper,BasicProcessGallery> implements BasicProcessGalleryService {
    private final MinioUtils minioUtils;
    @Override
    public PageInfo<BasicProcessGalleryVo> queryPage(BasicProcessGalleryDto basicProcessGalleryDto) {
        PageHelper.startPage(basicProcessGalleryDto);
        QueryWrapper<BasicProcessGallery> queryWrapper = this.buildQueryWrapper(basicProcessGalleryDto);
        return new PageInfo<>(this.queryList(queryWrapper));
    }


    @Override
    public List<BasicProcessGalleryVo> queryList(QueryWrapper<BasicProcessGallery> queryWrapper) {
        List<BasicProcessGalleryVo> basicProcessGalleryVos = this.baseMapper.queryList(queryWrapper);
        minioUtils.setObjectUrlToList(basicProcessGalleryVos,"image");
        return basicProcessGalleryVos;
    }

    @Override
    public QueryWrapper<BasicProcessGallery> buildQueryWrapper(BasicProcessGalleryDto basicProcessGalleryDto) {
        BaseQueryWrapper<BasicProcessGallery> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("code", basicProcessGalleryDto.getCode());
        queryWrapper.notEmptyEq("name", basicProcessGalleryDto.getName());
        queryWrapper.notEmptyEq("type_name", basicProcessGalleryDto.getTypeName());
        queryWrapper.notEmptyEq("type_code", basicProcessGalleryDto.getTypeCode());
        queryWrapper.notEmptyLike("remarks", basicProcessGalleryDto.getRemarks());
        if (StringUtils.isNotBlank(basicProcessGalleryDto.getCreateDate())){
            queryWrapper.between("create_date", basicProcessGalleryDto.getCreateDate().split(","));
        }

        queryWrapper.notEmptyEq("status", basicProcessGalleryDto.getStatus());
        queryWrapper.notEmptyEq("id", basicProcessGalleryDto.getId());
        return queryWrapper;
    }

    @Override
    public List<BasicProcessGalleryVo> export(BasicProcessGalleryDto basicProcessGalleryDto) {
        QueryWrapper<BasicProcessGallery> queryWrapper = this.buildQueryWrapper(basicProcessGalleryDto);
        return this.queryList(queryWrapper);
    }
}
