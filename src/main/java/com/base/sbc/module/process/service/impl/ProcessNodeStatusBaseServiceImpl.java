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
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusDto;
import com.base.sbc.module.process.dto.QueryNodeStatusDto;
import com.base.sbc.module.process.entity.ProcessNodeStatus;
import com.base.sbc.module.process.mapper.ProcessNodeStatusMapper;
import com.base.sbc.module.process.service.ProcessNodeStatusService;
import com.base.sbc.module.process.vo.ProcessNodeStatusVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-节点状态 service类
 * @address com.base.sbc.module.process.service.ProcessNodeStatusService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0  
 */
@Service
public class ProcessNodeStatusBaseServiceImpl extends BaseServiceImpl<ProcessNodeStatusMapper, ProcessNodeStatus> implements ProcessNodeStatusService {

        @Autowired
        private BaseController baseController;


/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-节点状态分页查询
        *
        * @param queryNodeStatusDto
        * @return
        */
        @Override
        public PageInfo<ProcessNodeStatusVo> getProcessNodeStatusList(QueryNodeStatusDto queryNodeStatusDto) {
            /*分页*/
            if(queryNodeStatusDto.getPageNum()!=0 && queryNodeStatusDto.getPageSize()!=0){
                PageHelper.startPage(queryNodeStatusDto);
            }
            QueryWrapper<ProcessNodeStatus> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.eq("node_id", queryNodeStatusDto.getNodeId());
            queryWrapper.orderByAsc("sort");
            /*查询流程配置-节点状态数据*/
            List<ProcessNodeStatus> processNodeStatusList = baseMapper.selectList(queryWrapper);
            PageInfo<ProcessNodeStatus> pageInfo = new PageInfo<>(processNodeStatusList);
            /*转换vo*/
            List<ProcessNodeStatusVo> list = BeanUtil.copyToList(processNodeStatusList, ProcessNodeStatusVo.class);
            PageInfo<ProcessNodeStatusVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改流程配置-节点状态
        *
        * @param addRevampProcessNodeStatusDto 流程配置-节点状态Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessNodeStatus(AddRevampProcessNodeStatusDto addRevampProcessNodeStatusDto) {
                ProcessNodeStatus processNodeStatus = new ProcessNodeStatus();
            if (StringUtils.isEmpty(addRevampProcessNodeStatusDto.getId())) {
                QueryWrapper<ProcessNodeStatus> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessNodeStatusDto, processNodeStatus);
                processNodeStatus.setCompanyCode(baseController.getUserCompany());
                processNodeStatus.insertInit();
                baseMapper.insert(processNodeStatus);
           } else {
                /*修改*/
                processNodeStatus = baseMapper.selectById(addRevampProcessNodeStatusDto.getId());
                if (ObjectUtils.isEmpty(processNodeStatus)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessNodeStatusDto, processNodeStatus);
                processNodeStatus.updateInit();
                baseMapper.updateById(processNodeStatus);
                }
                return true;
         }

    /**
     * 方法描述：批量新增修改流程配置-节点状态
     *
     * @param addRevampProcessNodeStatusDto 部件Dto类
     * @return boolean
     */
    @Override
    @Transactional(readOnly = false)
    public List<ProcessNodeStatusVo>  batchAddRevamp(List<AddRevampProcessNodeStatusDto> addRevampProcessNodeStatusDto) {
        /*重新给排序下标 添加节点条件流转*/
        for (int i = 0; i < addRevampProcessNodeStatusDto.size(); i++) {
            addRevampProcessNodeStatusDto.get(i).setSort(i+1);
        }
        List<ProcessNodeStatus>  processNodeStatusList = BeanUtil.copyToList(addRevampProcessNodeStatusDto, ProcessNodeStatus.class);
        saveOrUpdateBatch(processNodeStatusList);
        List<ProcessNodeStatusVo>  processNodeStatusvoList = BeanUtil.copyToList(processNodeStatusList, ProcessNodeStatusVo.class);
        return processNodeStatusvoList;
    }


    /**
         * 方法描述：删除流程配置-节点状态
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessNodeStatus(String id) {
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
          public Boolean startStopProcessNodeStatus(StartStopDto startStopDto) {
            UpdateWrapper<ProcessNodeStatus> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
