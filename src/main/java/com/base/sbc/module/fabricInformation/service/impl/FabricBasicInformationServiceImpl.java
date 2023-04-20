/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricInformation.service.impl;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.fabricInformation.dto.QueryFabricInformationDto;
import com.base.sbc.module.fabricInformation.dto.SaveUpdateFabricBasicInformationDto;
import com.base.sbc.module.fabricInformation.entity.FabricPost;
import com.base.sbc.module.fabricInformation.mapper.FabricBasicInformationMapper;
import com.base.sbc.module.fabricInformation.entity.FabricBasicInformation;
import com.base.sbc.module.fabricInformation.mapper.FabricPostMapper;
import com.base.sbc.module.fabricInformation.service.FabricBasicInformationService;
import com.base.sbc.module.fabricInformation.vo.FabricInformationVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：面料基本信息 service类
 * @address com.base.sbc.module.fabricInformation.service.FabricBasicInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:26
 * @version 1.0  
 */
@Service
public class FabricBasicInformationServiceImpl extends ServicePlusImpl<FabricBasicInformationMapper, FabricBasicInformation> implements FabricBasicInformationService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private FabricPostMapper fabricPostMapper;

    @Override
    public PageInfo<FabricInformationVo> getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto) {
        PageHelper.startPage(queryFabricInformationDto);
        queryFabricInformationDto.setCompanyCode(baseController.getUserCompany());
        List<FabricInformationVo> list = baseMapper.getFabricInformationList(queryFabricInformationDto);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(readOnly = false)
    public ApiResult saveUpdateFabricBasic(SaveUpdateFabricBasicInformationDto saveUpdateFabricBasicDto) {
        FabricBasicInformation fabricBasicInformation = new FabricBasicInformation();
        if (StringUtils.isNotBlank(saveUpdateFabricBasicDto.getId())) {
            /*调整*/
            fabricBasicInformation=baseMapper.selectById(saveUpdateFabricBasicDto.getId());
            BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricBasicInformation );
            fabricBasicInformation.updateInit();
            baseMapper.updateById(fabricBasicInformation);
        } else {
            /*新增*/
            BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricBasicInformation );
            fabricBasicInformation.insertInit();
            fabricBasicInformation.setStatus(BaseGlobal.STATUS_NORMAL);
            fabricBasicInformation.setCompanyCode(baseController.getUserCompany());
            fabricBasicInformation.setRegisterDate(new Date());
            fabricBasicInformation.setRemark("");
            /*面料详细id暂时为空*/
            fabricBasicInformation.setFabricDetailedId("");
            baseMapper.insert(fabricBasicInformation);
           /*添加到接受岗位*/
            FabricPost fabricPost=new FabricPost();
            fabricPost.setFabricBasicId(fabricBasicInformation.getId());
            List<FabricPost> fabricPostList=new ArrayList<>();
            fabricPostList.add(fabricPost);
//            fabricPostMapper.batchInsert(fabricPostList);

        }

        return ApiResult.success("操作成功");
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
