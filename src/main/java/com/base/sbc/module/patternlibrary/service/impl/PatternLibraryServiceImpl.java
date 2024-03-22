package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 版型库-主表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryServiceImpl extends ServiceImpl<PatternLibraryMapper, PatternLibrary> implements PatternLibraryService {

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    public IPage<PatternLibraryVO> listPages(PatternLibraryDTO patternLibraryDTO) {
        IPage<PatternLibraryVO> iPage = new Page<>(patternLibraryDTO.getPageNum(), patternLibraryDTO.getPageSize());
        LambdaQueryWrapper<PatternLibraryVO> queryWrapper = new LambdaQueryWrapper<>();
        // TODO：多选品牌筛选、多选涉及部件筛选
        queryWrapper
                // 版型编码
                .like(ObjectUtil.isNotEmpty(patternLibraryDTO.getCode())
                        , PatternLibraryVO::getCode, patternLibraryDTO.getCode())
                // 大类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory1st())
                        , PatternLibraryVO::getProdCategory1st, patternLibraryDTO.getProdCategory1st())
                // 品类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory())
                        , PatternLibraryVO::getProdCategory, patternLibraryDTO.getProdCategory())
                // 中类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory2nd())
                        , PatternLibraryVO::getProdCategory2nd, patternLibraryDTO.getProdCategory2nd())
                // 廓形编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getSilhouetteCode())
                        , PatternLibraryVO::getSilhouetteCode, patternLibraryDTO.getSilhouetteCode())
                // 所属版型库
                .in(ObjectUtil.isNotEmpty(patternLibraryDTO.getTemplateCode())
                        , PatternLibraryVO::getTemplateCode, Arrays.asList(patternLibraryDTO.getTemplateCode().split(",")))
                // 状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getStatus())
                        , PatternLibraryVO::getStatus, patternLibraryDTO.getStatus())
                // 启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getEnableFlag())
                        , PatternLibraryVO::getEnableFlag, patternLibraryDTO.getEnableFlag());
        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "");
        IPage<PatternLibraryVO> page = baseMapper.listPages(iPage, queryWrapper, patternLibraryDTO);
        return page;
    }

}
