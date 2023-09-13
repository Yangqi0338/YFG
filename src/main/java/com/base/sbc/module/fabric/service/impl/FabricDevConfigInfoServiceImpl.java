/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.alibaba.fastjson2.JSON;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.DelDTO;
import com.base.sbc.module.fabric.dto.EnableOrDeactivateDTO;
import com.base.sbc.module.fabric.dto.FabricDevConfigInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevConfigInfo;
import com.base.sbc.module.fabric.mapper.FabricDevConfigInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevConfigInfoService;
import com.base.sbc.module.fabric.service.FabricDevInfoService;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoListVO;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：面料开发配置信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevConfigInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:33
 */
@Service
public class FabricDevConfigInfoServiceImpl extends BaseServiceImpl<FabricDevConfigInfoMapper, FabricDevConfigInfo> implements FabricDevConfigInfoService {
    private static final Logger logger = LoggerFactory.getLogger(FabricDevConfigInfoService.class);
    @Autowired
    private FabricDevInfoService fabricDevInfoService;

    @Override
    public PageInfo<FabricDevConfigInfoListVO> getDevConfigList(Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<FabricDevConfigInfoListVO> devConfigList = super.baseMapper.getDevConfigList(super.getCompanyCode(), page.getStatus());
        return new PageInfo<>(devConfigList);
    }

    @Override
    public String devConfigSave(FabricDevConfigInfoSaveDTO dto) {
        logger.info("FabricDevConfigInfoService#devConfigSave 保存开发配置信息 dto:{}", JSON.toJSONString(dto));
        FabricDevConfigInfo fabricDevConfigInfo = CopyUtil.copy(dto, FabricDevConfigInfo.class);
        if (StringUtils.isEmpty(fabricDevConfigInfo.getId())) {
            IdGen idGen = new IdGen();
            fabricDevConfigInfo.setId(idGen.nextIdStr());
            fabricDevConfigInfo.setDevCode("DC" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
            fabricDevConfigInfo.setCompanyCode(super.getCompanyCode());
            fabricDevConfigInfo.insertInit();
        } else {
            fabricDevConfigInfo.updateInit();
        }
        super.saveOrUpdate(fabricDevConfigInfo);
        return fabricDevConfigInfo.getId();
    }

    @Override
    public void enableOrDeactivate(EnableOrDeactivateDTO dto) {
        logger.info("FabricDevConfigInfoService#enableOrDeactivate 启用或停用 dto:{}", JSON.toJSONString(dto));

        List<FabricDevConfigInfo> fabricDevConfigInfos = dto.getIds().stream()
                .map(id -> {
                    FabricDevConfigInfo fabricDevConfigInfo = new FabricDevConfigInfo();
                    fabricDevConfigInfo.setId(id);
                    fabricDevConfigInfo.setStatus(dto.getStatus());
                    fabricDevConfigInfo.updateInit();
                    return fabricDevConfigInfo;
                }).collect(Collectors.toList());
        super.updateBatchById(fabricDevConfigInfos);
    }

    @Override
    public void del(DelDTO dto) {
        logger.info("FabricDevConfigInfoService#del 删除 dto:{}", JSON.toJSONString(dto));
        super.getBaseMapper().deleteBatchIds(dto.getIds());
    }

    @Override
    public PageInfo<FabricDevConfigInfoVO> getDevConfigList(String devCode, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return null;
    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
