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
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.process.dto.AddRevampProcessStageDto;
import com.base.sbc.module.process.dto.QueryStageDto;
import com.base.sbc.module.process.entity.ProcessStage;
import com.base.sbc.module.process.mapper.ProcessStageMapper;
import com.base.sbc.module.process.service.ProcessStageService;
import com.base.sbc.module.process.vo.ProcessStageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-阶段 service类
 * @address com.base.sbc.module.process.service.ProcessStageService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-27 14:47:10
 * @version 1.0  
 */
@Service
public class ProcessStageServiceImpl extends BaseServiceImpl<ProcessStageMapper, ProcessStage> implements ProcessStageService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-阶段分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<ProcessStageVo> getProcessStageList(QueryStageDto queryDto) {
            /*分页*/
            if(queryDto.getPageSize()!=0 && queryDto.getPageNum()!=0){
                PageHelper.startPage(queryDto);
            }
            PageHelper.startPage(queryDto);
            QueryWrapper<ProcessStage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.like(StringUtils.isNotBlank(queryDto.getStageName()),"stage_name",queryDto.getStageName());
            queryWrapper.orderByAsc("sort");
            /*查询流程配置-阶段数据*/
            List<ProcessStage> processStageList = baseMapper.selectList(queryWrapper);
            PageInfo<ProcessStage> pageInfo = new PageInfo<>(processStageList);
            /*转换vo*/
            List<ProcessStageVo> list = BeanUtil.copyToList(processStageList, ProcessStageVo.class);
            PageInfo<ProcessStageVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改流程配置-阶段
        *
        * @param addRevampProcessStageDto 流程配置-阶段Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessStage(AddRevampProcessStageDto addRevampProcessStageDto) {
                ProcessStage processStage = new ProcessStage();
            if (StringUtils.isEmpty(addRevampProcessStageDto.getId())) {
                QueryWrapper<ProcessStage> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("stage_name",addRevampProcessStageDto.getStageName());
               List<ProcessStage> processStageList = baseMapper.selectList(queryWrapper);
               if(!CollectionUtils.isEmpty(processStageList)){
                   throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
               }
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessStageDto, processStage);
                processStage.setCompanyCode(baseController.getUserCompany());
                processStage.insertInit();
                baseMapper.insert(processStage);
           } else {
                /*修改*/
                processStage = baseMapper.selectById(addRevampProcessStageDto.getId());
                if (ObjectUtils.isEmpty(processStage)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessStageDto, processStage);
                processStage.updateInit();
                baseMapper.updateById(processStage);
                }
                return true;
         }


         /**
         * 方法描述：删除流程配置-阶段
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessStage(String id) {
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
          public Boolean startStopProcessStage(StartStopDto startStopDto) {
            UpdateWrapper<ProcessStage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
