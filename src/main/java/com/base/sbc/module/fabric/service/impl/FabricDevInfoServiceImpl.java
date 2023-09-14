/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.entity.FabricDevInfo;
import com.base.sbc.module.fabric.mapper.FabricDevInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevInfoService;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：面料开发信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevInfoService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:39
 */
@Service
public class FabricDevInfoServiceImpl extends BaseServiceImpl<FabricDevInfoMapper, FabricDevInfo> implements FabricDevInfoService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public PageInfo<FabricDevConfigInfoVO> getByDevApplyCode(String devApplyCode, Integer pageNum, Integer pageSize) {
        if (StringUtils.isEmpty(devApplyCode)) {
            return new PageInfo<>();
        }
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(super.getBaseMapper().getByDevApplyCode(devApplyCode));
    }

    @Override
    public PageInfo<FabricDevConfigInfoVO> getByDevMainId(String devMainId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FabricDevConfigInfoVO> fabricDevConfigInfoVOS = super.getBaseMapper().getByDevMainId(devMainId);
        return new PageInfo<>(fabricDevConfigInfoVOS);
    }

    @Override
    public void saveDevInfo(String devConfigId, Date expectStartDate, Date expectEndDate, String devMainId) {
        FabricDevInfo fabricDevInfo = new FabricDevInfo();
        fabricDevInfo.setId(new IdGen().nextIdStr());
        fabricDevInfo.setCompanyCode(super.getCompanyCode());
        fabricDevInfo.setDevMainId(devMainId);
        fabricDevInfo.setDevConfigId(devConfigId);
        fabricDevInfo.setExpectStartDate(expectStartDate);
        fabricDevInfo.setExpectEndDate(expectEndDate);
        fabricDevInfo.insertInit();
        super.save(fabricDevInfo);
    }

    @Override
    public void updateFile(String id, String attachmentUrl) {
        FabricDevInfo devInfo = super.getById(id);
        FabricDevInfo fabricDevInfo = new FabricDevInfo();
        fabricDevInfo.setId(id);
        fabricDevInfo.updateInit();
        fabricDevInfo.setAttachmentUrl(attachmentUrl);
        if (Objects.isNull(devInfo.getPracticalStartDate())) {
            fabricDevInfo.setPracticalStartDate(new Date());
        }
        fabricDevInfo.setOperator(super.getUserName());
        fabricDevInfo.setOperatorId(super.getUserId());
        super.updateById(fabricDevInfo);
    }

    @Override
    public void updateStatus(String devId, String status) {
        FabricDevInfo fabricDevInfo = new FabricDevInfo();
        fabricDevInfo.setId(devId);
        fabricDevInfo.updateInit();
        fabricDevInfo.setStatus(status);
        fabricDevInfo.setPracticalEndDate(new Date());
        fabricDevInfo.setOperator(super.getUserName());
        fabricDevInfo.setOperatorId(super.getUserId());
        super.updateById(fabricDevInfo);
    }

    public List<FabricDevInfo> getAllPass(String devMainId) {
        LambdaQueryWrapper<FabricDevInfo> qw = new QueryWrapper<FabricDevInfo>().lambda()
                .eq(FabricDevInfo::getDevMainId, devMainId)
                .eq(FabricDevInfo::getDelFlag, "0")
                .select(FabricDevInfo::getId, FabricDevInfo::getStatus, FabricDevInfo::getPracticalStartDate);
        return super.list(qw);

    }


// 自定义方法区 不替换的区域【other_end】

}
