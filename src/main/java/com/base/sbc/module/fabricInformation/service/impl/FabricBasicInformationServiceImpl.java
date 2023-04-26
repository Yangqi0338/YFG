/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricInformation.service.impl;

import com.base.sbc.client.amc.entity.Job;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.amc.utils.AmcUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.fabricInformation.dto.QueryDetailFabricDto;
import com.base.sbc.module.fabricInformation.dto.QueryFabricInformationDto;
import com.base.sbc.module.fabricInformation.dto.SaveUpdateFabricBasicInformationDto;
import com.base.sbc.module.fabricInformation.entity.FabricDetailedInformation;
import com.base.sbc.module.fabricInformation.entity.FabricJob;
import com.base.sbc.module.fabricInformation.mapper.FabricBasicInformationMapper;
import com.base.sbc.module.fabricInformation.entity.FabricBasicInformation;
import com.base.sbc.module.fabricInformation.mapper.FabricDetailedInformationMapper;
import com.base.sbc.module.fabricInformation.mapper.FabricJobMapper;
import com.base.sbc.module.fabricInformation.service.FabricBasicInformationService;
import com.base.sbc.module.fabricInformation.vo.FabricInformationVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private FabricJobMapper fabricJobMapper;
    @Autowired
    private FabricDetailedInformationMapper fabricDetailedInformationMapper;

    @Autowired
    private AmcService amcService;


    @Override
    public PageInfo<FabricInformationVo> getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto) {
        PageHelper.startPage(queryFabricInformationDto);
        queryFabricInformationDto.setCompanyCode(baseController.getUserCompany());
        String postString=  amcService.getJobByUserIdOrJobName(baseController.getUserId(),null);
        List<Job> jobList= AmcUtils.parseStrTopostIdList(postString);
        if(!CollectionUtils.isEmpty(jobList)){
            queryFabricInformationDto.setJobIdList(jobList.stream().map(Job::getId).collect(Collectors.toList()));
        }else {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
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
            /*暂时这么写*/
            String postString=  amcService.getJobByUserIdOrJobName(null,"面辅料专员,设计师,理化实验室,设计师助理");
            List<Job> jobList= AmcUtils.parseStrTopostIdList(postString);
            for (Job job : jobList) {
                FabricJob fabricJob=new FabricJob();
                fabricJob.setFabricBasicId(fabricBasicInformation.getId());
                fabricJob.setUserJobId(job.getId());
                fabricJob.setCompanyCode(baseController.getUserCompany());
                fabricJob.setRemark("");
                if(job.getName().equals("设计师") || job.getName().equals("设计师助理")){
                    fabricJob.setSponsorStatus("0");
                }else {
                    fabricJob.setSponsorStatus("1");
                }
                fabricJob.insertInit();
                fabricJobMapper.insert(fabricJob);
            }
        }
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult delFabric(String id) {
        List<String> ids = StringUtils.convertList(id);
        List<FabricBasicInformation> list = baseMapper.selectBatchIds(ids);
        List<String> basicIdList=  list.stream().filter(f -> StringUtils.isBlank(f.getFabricDetailedId())).map(FabricBasicInformation::getId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(basicIdList)){
            throw new OtherException("存在面料详细无法删除");
        }
        baseMapper.deleteBatchIds(basicIdList);
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult getById(QueryDetailFabricDto queryDetailFabricDto) {
        FabricBasicInformation fabricBasicInformation=   baseMapper.selectById(queryDetailFabricDto.getId());
        FabricInformationVo fabricInformationVo=new FabricInformationVo();
        BeanUtils.copyProperties(fabricBasicInformation,fabricInformationVo );
        if(StringUtils.isNotBlank(fabricBasicInformation.getFabricDetailedId())){
           FabricDetailedInformation fabricDetailedInformation= fabricDetailedInformationMapper.selectById(fabricBasicInformation.getFabricDetailedId());
            if (queryDetailFabricDto.getType().equals("update") ||   fabricDetailedInformation.getIsDraft().equals(BaseGlobal.STOCK_STATUS_WAIT_CHECK)) {
                BeanUtils.copyProperties(fabricDetailedInformation,fabricInformationVo );
            }
        }
        return ApiResult.success("查询成功",fabricInformationVo);
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
