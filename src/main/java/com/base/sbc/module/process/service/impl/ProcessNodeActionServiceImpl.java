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
import com.base.sbc.module.process.dto.AddRevampProcessNodeActionDto;
import com.base.sbc.module.process.entity.ProcessNodeAction;
import com.base.sbc.module.process.mapper.ProcessNodeActionMapper;
import com.base.sbc.module.process.service.ProcessNodeActionService;
import com.base.sbc.module.process.vo.ProcessNodeActionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-节点动作 service类
 * @address com.base.sbc.module.process.service.ProcessNodeActionService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:14
 * @version 1.0  
 */
@Service
public class ProcessNodeActionServiceImpl extends ServicePlusImpl<ProcessNodeActionMapper, ProcessNodeAction> implements ProcessNodeActionService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-节点动作分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<ProcessNodeActionVo> getProcessNodeActionList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<ProcessNodeAction> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询流程配置-节点动作数据*/
            List<ProcessNodeAction> processNodeActionList = baseMapper.selectList(queryWrapper);
            PageInfo<ProcessNodeAction> pageInfo = new PageInfo<>(processNodeActionList);
            /*转换vo*/
            List<ProcessNodeActionVo> list = BeanUtil.copyToList(processNodeActionList, ProcessNodeActionVo.class);
            PageInfo<ProcessNodeActionVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改流程配置-节点动作
        *
        * @param addRevampProcessNodeActionDto 流程配置-节点动作Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessNodeAction(AddRevampProcessNodeActionDto addRevampProcessNodeActionDto) {
                ProcessNodeAction processNodeAction = new ProcessNodeAction();
            if (StringUtils.isEmpty(addRevampProcessNodeActionDto.getId())) {
                QueryWrapper<ProcessNodeAction> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessNodeActionDto, processNodeAction);
                processNodeAction.setCompanyCode(baseController.getUserCompany());
                processNodeAction.insertInit();
                baseMapper.insert(processNodeAction);
           } else {
                /*修改*/
                processNodeAction = baseMapper.selectById(addRevampProcessNodeActionDto.getId());
                if (ObjectUtils.isEmpty(processNodeAction)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessNodeActionDto, processNodeAction);
                processNodeAction.updateInit();
                baseMapper.updateById(processNodeAction);
                }
                return true;
         }


         /**
         * 方法描述：删除流程配置-节点动作
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessNodeAction(String id) {
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
          public Boolean startStopProcessNodeAction(StartStopDto startStopDto) {
            UpdateWrapper<ProcessNodeAction> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
