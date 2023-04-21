/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricInformation.service.impl;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.fabricInformation.dto.SaveUpdateFabricDetailedInformationDto;
import com.base.sbc.module.fabricInformation.entity.FabricBasicInformation;
import com.base.sbc.module.fabricInformation.mapper.FabricBasicInformationMapper;
import com.base.sbc.module.fabricInformation.mapper.FabricDetailedInformationMapper;
import com.base.sbc.module.fabricInformation.entity.FabricDetailedInformation;
import com.base.sbc.module.fabricInformation.service.FabricDetailedInformationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：面料详细信息 service类
 * @address com.base.sbc.module.fabricInformation.service.FabricDetailedInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:28
 * @version 1.0  
 */
@Service
public class FabricDetailedInformationServiceImpl extends ServicePlusImpl<FabricDetailedInformationMapper, FabricDetailedInformation> implements FabricDetailedInformationService {
    @Autowired
    private FabricBasicInformationMapper fabricBasicInformationMapper;
    @Autowired
    private BaseController baseController;



    @Override
    @Transactional(readOnly = false)
    public ApiResult saveUpdateFabricDetailed(SaveUpdateFabricDetailedInformationDto saveUpdateFabricBasicDto) {
        FabricDetailedInformation fabricDetailedInformation=new FabricDetailedInformation();
        if(StringUtils.isNotBlank(saveUpdateFabricBasicDto.getFabricDetailedId())){
            /*调整*/
            fabricDetailedInformation=baseMapper.selectById(saveUpdateFabricBasicDto.getFabricDetailedId());
            BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricDetailedInformation);
            fabricDetailedInformation.updateInit();
            fabricDetailedInformation.setId(saveUpdateFabricBasicDto.getFabricDetailedId());
            baseMapper.updateById(fabricDetailedInformation);
        }else {
            FabricBasicInformation fabricBasicInformation= fabricBasicInformationMapper.selectById(saveUpdateFabricBasicDto.getFabricBasicId());
            BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricDetailedInformation);
            fabricDetailedInformation.setCompanyCode(baseController.getUserCompany());
            fabricDetailedInformation.setRemark("");
            fabricDetailedInformation.insertInit();
            baseMapper.insert(fabricDetailedInformation);
            fabricBasicInformation.setFabricDetailedId(fabricDetailedInformation.getId());
            fabricBasicInformationMapper.updateById(fabricBasicInformation);
        }
        return ApiResult.success("操作成功");
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
