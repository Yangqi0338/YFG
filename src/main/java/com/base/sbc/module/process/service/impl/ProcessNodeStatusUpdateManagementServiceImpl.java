/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusUpdateManagementDto;
import com.base.sbc.module.process.entity.ProcessNodeStatusUpdateManagement;
import com.base.sbc.module.process.mapper.ProcessNodeStatusUpdateManagementMapper;
import com.base.sbc.module.process.service.ProcessNodeStatusUpdateManagementService;
import com.base.sbc.module.process.vo.ProcessNodeStatusUpdateManagementVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-节点状态修改字段 service类
 * @address com.base.sbc.module.process.service.ProcessNodeStatusUpdateManagementService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-16 14:00:08
 * @version 1.0  
 */
@Service
public class ProcessNodeStatusUpdateManagementServiceImpl extends ServicePlusImpl<ProcessNodeStatusUpdateManagementMapper, ProcessNodeStatusUpdateManagement> implements ProcessNodeStatusUpdateManagementService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-节点状态修改字段分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<ProcessNodeStatusUpdateManagementVo> getProcessNodeStatusUpdateManagementList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<ProcessNodeStatusUpdateManagement> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询流程配置-节点状态修改字段数据*/
            List<ProcessNodeStatusUpdateManagement> processNodeStatusUpdateManagementList = baseMapper.selectList(queryWrapper);
            PageInfo<ProcessNodeStatusUpdateManagement> pageInfo = new PageInfo<>(processNodeStatusUpdateManagementList);
            /*转换vo*/
            List<ProcessNodeStatusUpdateManagementVo> list = BeanUtil.copyToList(processNodeStatusUpdateManagementList, ProcessNodeStatusUpdateManagementVo.class);
            PageInfo<ProcessNodeStatusUpdateManagementVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改流程配置-节点状态修改字段
        *
        * @param addRevampProcessNodeStatusUpdateManagementDto 流程配置-节点状态修改字段Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessNodeStatusUpdateManagement(AddRevampProcessNodeStatusUpdateManagementDto addRevampProcessNodeStatusUpdateManagementDto) {
                ProcessNodeStatusUpdateManagement processNodeStatusUpdateManagement = new ProcessNodeStatusUpdateManagement();
            if (StringUtils.isEmpty(addRevampProcessNodeStatusUpdateManagementDto.getId())) {
                QueryWrapper<ProcessNodeStatusUpdateManagement> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessNodeStatusUpdateManagementDto, processNodeStatusUpdateManagement);
                processNodeStatusUpdateManagement.setCompanyCode(baseController.getUserCompany());
                processNodeStatusUpdateManagement.insertInit();
                baseMapper.insert(processNodeStatusUpdateManagement);
           } else {
                /*修改*/
                processNodeStatusUpdateManagement = baseMapper.selectById(addRevampProcessNodeStatusUpdateManagementDto.getId());
                if (ObjectUtils.isEmpty(processNodeStatusUpdateManagement)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessNodeStatusUpdateManagementDto, processNodeStatusUpdateManagement);
                processNodeStatusUpdateManagement.updateInit();
                baseMapper.updateById(processNodeStatusUpdateManagement);
                }
                return true;
         }

    /**
     * 方法描述：批量新增修改流程配置-节点动作
     *
     * @param AddRevampProcessNodeStatusUpdateManagementDto 部件Dto类
     * @return boolean
     */
    @Override
    public Boolean batchAddRevampProcessNodeAction(List<AddRevampProcessNodeStatusUpdateManagementDto> AddRevampProcessNodeStatusUpdateManagementDto) {
        List<ProcessNodeStatusUpdateManagement>  processNodeActionList = BeanUtil.copyToList(AddRevampProcessNodeStatusUpdateManagementDto, ProcessNodeStatusUpdateManagement.class);
        saveOrUpdateBatch(processNodeActionList);
        return true;
    }


    /**
         * 方法描述：删除流程配置-节点状态修改字段
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessNodeStatusUpdateManagement(String id) {
         List<String> ids = StringUtils.convertList(id);
           /*批量删除*/
           baseMapper.deleteBatchIds(ids);
           return true;
         }


         /**
         * 方法描述：启用停止
         *
         * @param startStopDto 启用停止Dto类
         * @return boolean
         */
          @Override
          public Boolean startStopProcessNodeStatusUpdateManagement(StartStopDto startStopDto) {
            UpdateWrapper<ProcessNodeStatusUpdateManagement> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
