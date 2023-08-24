/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SaveUpdateFabricDetailedInformationDto;
import com.base.sbc.module.sample.entity.FabricDetailedInformation;
import com.base.sbc.module.sample.mapper.FabricBasicInformationMapper;
import com.base.sbc.module.sample.mapper.FabricDetailedInformationMapper;
import com.base.sbc.module.sample.service.FabricDetailedInformationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：面料详细信息 service类
 * @address com.base.sbc.module.sample.service.FabricDetailedInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:28
 * @version 1.0  
 */
@Service
public class FabricDetailedInformationServiceImpl extends BaseServiceImpl<FabricDetailedInformationMapper, FabricDetailedInformation> implements FabricDetailedInformationService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private FilesUtils filesUtils;

    @Autowired
    private MessageUtils messageUtils;

    @Override
    @Transactional(readOnly = false)
    public ApiResult saveUpdateFabricDetailed(SaveUpdateFabricDetailedInformationDto saveUpdateFabricBasicDto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("basic_information_id",saveUpdateFabricBasicDto.getBasicInformationId());
        FabricDetailedInformation fabricDetailedInformation = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(fabricDetailedInformation)) {
            fabricDetailedInformation = new FabricDetailedInformation();
        }
        BeanUtils.copyProperties(saveUpdateFabricBasicDto, fabricDetailedInformation);
        fabricDetailedInformation.setCompanyCode(baseController.getUserCompany());
        saveOrUpdate(fabricDetailedInformation);
        messageUtils.atactiformSendMessage("fabric","0",baseController.getUser());
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult uploadingReport(String id, MultipartFile file, HttpServletRequest request) throws Throwable {
        if (StringUtils.isEmpty(id)) {
            throw new OtherException("id为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("basic_information_id",id);
        queryWrapper.eq("is_draft", BaseGlobal.STATUS_NORMAL);
        FabricDetailedInformation fabricDetailedInformation = baseMapper.selectOne(queryWrapper);
        if(fabricDetailedInformation==null){
            throw new OtherException("辅料专员暂未提交面料详情");
        }
        Object o = filesUtils.uploadBigData(file, FilesUtils.PRODUCT, request).getData();
        String s = o.toString();
        fabricDetailedInformation.setReportName(file.getOriginalFilename());
        fabricDetailedInformation.setReportUrl(s);
        fabricDetailedInformation.updateInit();
        baseMapper.updateById(fabricDetailedInformation);
        return ApiResult.success("操作成功");
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
