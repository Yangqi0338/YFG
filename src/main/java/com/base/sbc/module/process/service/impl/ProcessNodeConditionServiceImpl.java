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
import com.base.sbc.module.process.dto.AddRevampProcessNodeConditionDto;
import com.base.sbc.module.process.entity.ProcessNodeCondition;
import com.base.sbc.module.process.mapper.ProcessNodeConditionMapper;
import com.base.sbc.module.process.service.ProcessNodeConditionService;
import com.base.sbc.module.process.vo.ProcessNodeConditionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-节点条件 service类
 * @address com.base.sbc.module.process.service.ProcessNodeConditionService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0  
 */
@Service
public class ProcessNodeConditionServiceImpl extends ServicePlusImpl<ProcessNodeConditionMapper, ProcessNodeCondition> implements ProcessNodeConditionService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-节点条件分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<ProcessNodeConditionVo> getProcessNodeConditionList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<ProcessNodeCondition> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询流程配置-节点条件数据*/
            List<ProcessNodeCondition> processNodeConditionList = baseMapper.selectList(queryWrapper);
            PageInfo<ProcessNodeCondition> pageInfo = new PageInfo<>(processNodeConditionList);
            /*转换vo*/
            List<ProcessNodeConditionVo> list = BeanUtil.copyToList(processNodeConditionList, ProcessNodeConditionVo.class);
            PageInfo<ProcessNodeConditionVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改流程配置-节点条件
        *
        * @param addRevampProcessNodeConditionDto 流程配置-节点条件Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessNodeCondition(AddRevampProcessNodeConditionDto addRevampProcessNodeConditionDto) {
                ProcessNodeCondition processNodeCondition = new ProcessNodeCondition();
            if (StringUtils.isEmpty(addRevampProcessNodeConditionDto.getId())) {
                QueryWrapper<ProcessNodeCondition> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessNodeConditionDto, processNodeCondition);
                processNodeCondition.setCompanyCode(baseController.getUserCompany());
                processNodeCondition.insertInit();
                baseMapper.insert(processNodeCondition);
           } else {
                /*修改*/
                processNodeCondition = baseMapper.selectById(addRevampProcessNodeConditionDto.getId());
                if (ObjectUtils.isEmpty(processNodeCondition)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessNodeConditionDto, processNodeCondition);
                processNodeCondition.updateInit();
                baseMapper.updateById(processNodeCondition);
                }
                return true;
         }


         /**
         * 方法描述：删除流程配置-节点条件
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessNodeCondition(String id) {
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
          public Boolean startStopProcessNodeCondition(StartStopDto startStopDto) {
            UpdateWrapper<ProcessNodeCondition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
