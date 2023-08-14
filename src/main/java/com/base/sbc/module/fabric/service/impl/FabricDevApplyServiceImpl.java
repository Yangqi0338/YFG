/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricDevApplySaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevApplySearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevApply;
import com.base.sbc.module.fabric.mapper.FabricDevApplyMapper;
import com.base.sbc.module.fabric.service.*;
import com.base.sbc.module.fabric.vo.FabricDevApplyListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：面料开发申请（主表） service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevApplyService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:20
 */
@Service
public class FabricDevApplyServiceImpl extends BaseServiceImpl<FabricDevApplyMapper, FabricDevApply> implements FabricDevApplyService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private CodeGen codeGen;

    @Autowired
    private FabricDevBasicInfoService fabricDevBasicInfoService;
    @Autowired
    private FabricDevMaterialInfoService fabricDevMaterialInfoService;
    @Autowired
    private FabricDevOtherInfoService fabricDevOtherInfoService;
    @Autowired
    private FabricDevColorInfoService fabricDevColorInfoService;

    @Override
    public PageInfo<FabricDevApplyListVO> getDevApplyList(FabricDevApplySearchDTO dto) {
        PageHelper.startPage(dto);
        List<FabricDevApplyListVO> devApplyList = super.getBaseMapper().getDevApplyList(dto);
        return new PageInfo<>(devApplyList);
    }


    @Override
    public String devApplySave(FabricDevApplySaveDTO dto) {
        String companyCode = super.getCompanyCode();
        FabricDevApply fabricDevApply = this.getFabricDevApply(dto, companyCode);
        super.saveOrUpdate(fabricDevApply);
        String id = fabricDevApply.getId();
        fabricDevBasicInfoService.devBasicInfoSave(dto.getFabricDevBasicInfoSave(), id, companyCode);
        fabricDevMaterialInfoService.devMaterialInfoSave(dto.getFabricDevMaterialInfoSave(), id, companyCode);
        fabricDevOtherInfoService.devOtherInfoSave(dto.getFabricDevOtherInfoSave(), id, companyCode);
        return null;
    }

    private FabricDevApply getFabricDevApply(FabricDevApplySaveDTO dto, String companyCode) {
        FabricDevApply fabricDevApply = new FabricDevApply();
        if (StringUtils.isNotEmpty(dto.getId())) {
            fabricDevApply.setRequiredArrivalDate(dto.getRequiredArrivalDate());
            fabricDevApply.updateInit();
            return fabricDevApply;
        }
        IdGen idGen = new IdGen();
        fabricDevApply.setDevApplyCode(codeGen.getCode("K", "devApply", companyCode));
        fabricDevApply.setCompanyCode(companyCode);
        fabricDevApply.insertInit();
        fabricDevApply.setId(idGen.nextIdStr());
        fabricDevApply.setRequiredArrivalDate(dto.getRequiredArrivalDate());
        return fabricDevApply;
    }


// 自定义方法区 不替换的区域【other_end】

}
