/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.fabricsummary.mapper.FabricSummaryMapper;
import com.base.sbc.module.fabricsummary.service.FabricSummaryService;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：款式管理-面料汇总 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryService
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-28 15:25:40
 * @version 1.0  
 */
@Service
public class FabricSummaryServiceImpl extends BaseServiceImpl<FabricSummaryMapper, FabricSummary> implements FabricSummaryService {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private StylePicUtils stylePicUtils;
    @Override
    public  PageInfo<String> fabricSummaryIdList(FabricSummaryV2Dto dto) {
        Page<String> page  = PageHelper.startPage(dto);
        BaseQueryWrapper<String> qw = new BaseQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getId())){
            qw.eq("tfs.id",dto.getId());
        }
        qw.eq("tfs.company_code",dto.getCompanyCode());
        QueryGenerator.initQueryWrapperByMap(qw, dto);
        baseMapper.fabricSummaryIdList(qw);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<FabricSummaryInfoVo> fabricSummaryInfoVoList(FabricSummaryV2Dto dto) {

        BaseQueryWrapper<FabricSummaryInfoVo> qw = new BaseQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getId())){
            qw.eq("tfs.id",dto.getId());
        }
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        qw.eq("tfs.company_code",dto.getCompanyCode());
        qw.orderByDesc("tfs.create_date");
        Page<FabricSummaryInfoVo> page = PageHelper.startPage(dto);
        List<FabricSummaryInfoVo> list = getBaseMapper().fabricSummaryInfoVoList(qw);
        if (CollectionUtils.isNotEmpty(list)){
            list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        PageInfo<FabricSummaryInfoVo> pageInfo = page.toPageInfo();
        if (isColumnHeard){
            pageInfo.setList(list);
            return pageInfo;
        }
        minioUtils.setObjectUrlToList(list, "imageUrl");
        stylePicUtils.setStylePic(list, "stylePic");

        Map<String, List<FabricSummaryInfoVo>> map = list.stream().collect(Collectors.groupingBy(FabricSummaryInfoVo::getId));

        List<FabricSummaryInfoVo> result = new ArrayList<>();
        for (String s : map.keySet()) {
            List<FabricSummaryInfoVo> fabricSummaryInfoVoList = map.get(s);
            if (fabricSummaryInfoVoList.size() > 1){
                fabricSummaryInfoVoList =  fabricSummaryInfoVoList.stream().sorted(Comparator.comparing(FabricSummaryInfoVo::getSort)).collect(Collectors.toList());
            }
            result.addAll(fabricSummaryInfoVoList);
        }
        pageInfo.setList(result.stream().sorted(Comparator.comparing(FabricSummaryInfoVo::getCreateDate).reversed()).collect(Collectors.toList()));
        return pageInfo;


    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
