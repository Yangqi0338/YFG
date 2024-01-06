package com.base.sbc.module.operalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operalog.dto.OperaLogDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.mapper.OperaLogMapper;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/19 15:48:45
 * @mail 247967116@qq.com
 */
@Service
public class OperaLogServiceImpl extends BaseServiceImpl<OperaLogMapper, OperaLogEntity> implements OperaLogService {
    @Override
    public PageInfo<OperaLogEntity> listPage(OperaLogDto operaLogDto) {
        LambdaQueryWrapper<OperaLogEntity> queryWrapper = new BaseLambdaQueryWrapper<OperaLogEntity>()
                .notEmptyLike(OperaLogEntity::getDocumentId,operaLogDto.getDocumentId())
                .notEmptyLike(OperaLogEntity::getDocumentName,operaLogDto.getDocumentName())
                .notEmptyLike(OperaLogEntity::getDocumentCode,operaLogDto.getDocumentCode())
                .notEmptyIn(OperaLogEntity::getDocumentCode,operaLogDto.getDocumentCodeList())
                .notEmptyLike(OperaLogEntity::getParentId,operaLogDto.getParentId())
                .notEmptyLike(OperaLogEntity::getType,operaLogDto.getType())
                .notEmptyLike(OperaLogEntity::getPath,operaLogDto.getPath())
                .notEmptyLike(OperaLogEntity::getName,operaLogDto.getName())
                .notEmptyLike(OperaLogEntity::getCreateName,operaLogDto.getCreateName())
                .between(OperaLogEntity::getCreateDate,operaLogDto.getCreateDate())
                .eq(OperaLogEntity::getCompanyCode,operaLogDto.getUserCompany())
                .orderByDesc(OperaLogEntity::getCreateDate);
        Page<OperaLogEntity> startPage = PageHelper.startPage(operaLogDto);
        this.list(queryWrapper);
        return startPage.toPageInfo();
    }
}
