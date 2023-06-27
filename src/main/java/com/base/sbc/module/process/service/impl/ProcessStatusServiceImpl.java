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
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.process.dto.AddRevampProcessStatusDto;
import com.base.sbc.module.process.dto.QueryStatusDto;
import com.base.sbc.module.process.entity.ProcessStatus;
import com.base.sbc.module.process.mapper.ProcessStatusMapper;
import com.base.sbc.module.process.service.ProcessStatusService;
import com.base.sbc.module.process.vo.ProcessStatusVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-状态定义 service类
 * @address com.base.sbc.module.process.service.ProcessStatusService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:16
 * @version 1.0  
 */
@Service
public class ProcessStatusServiceImpl extends BaseServiceImpl<ProcessStatusMapper, ProcessStatus> implements ProcessStatusService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-状态定义分页查询
        *
        * @param queryStatusDto
        * @return
        */
        @Override
        public PageInfo<ProcessStatusVo> getProcessStatusList(QueryStatusDto queryStatusDto) {
            /*分页*/
            if(queryStatusDto.getPageNum()!=0 && queryStatusDto.getPageSize() !=0){
                PageHelper.startPage(queryStatusDto);
            }
            QueryWrapper<ProcessStatus> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.like(StringUtils.isNotBlank(queryStatusDto.getStatusName()),"status_name",queryStatusDto.getStatusName());
            /*查询流程配置-状态定义数据*/
            List<ProcessStatus> processStatusList = baseMapper.selectList(queryWrapper);
            PageInfo<ProcessStatus> pageInfo = new PageInfo<>(processStatusList);
            /*转换vo*/
            List<ProcessStatusVo> list = BeanUtil.copyToList(processStatusList, ProcessStatusVo.class);
            PageInfo<ProcessStatusVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改流程配置-状态定义
        *
        * @param addRevampProcessStatusDto 流程配置-状态定义Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessStatus(AddRevampProcessStatusDto addRevampProcessStatusDto) {
                ProcessStatus processStatus = new ProcessStatus();
            if (StringUtils.isEmpty(addRevampProcessStatusDto.getId())) {
                QueryWrapper<ProcessStatus> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessStatusDto, processStatus);
                processStatus.setCompanyCode(baseController.getUserCompany());
                processStatus.insertInit();
                baseMapper.insert(processStatus);
           } else {
                /*修改*/
                processStatus = baseMapper.selectById(addRevampProcessStatusDto.getId());
                if (ObjectUtils.isEmpty(processStatus)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessStatusDto, processStatus);
                processStatus.updateInit();
                baseMapper.updateById(processStatus);
                }
                return true;
         }


         /**
         * 方法描述：删除流程配置-状态定义
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessStatus(String id) {
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
          public Boolean startStopProcessStatus(StartStopDto startStopDto) {
            UpdateWrapper<ProcessStatus> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
