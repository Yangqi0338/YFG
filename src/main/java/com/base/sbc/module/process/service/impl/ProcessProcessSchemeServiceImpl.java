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
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.process.dto.AddRevampProcessProcessSchemeDto;
import com.base.sbc.module.process.entity.ProcessProcessScheme;
import com.base.sbc.module.process.mapper.ProcessProcessSchemeMapper;
import com.base.sbc.module.process.service.ProcessProcessSchemeService;
import com.base.sbc.module.process.vo.ProcessProcessSchemeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 类描述：流程配置-流程方案 service类
 * @address com.base.sbc.module.process.service.ProcessProcessSchemeService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0  
 */
@Service
public class ProcessProcessSchemeServiceImpl extends ServicePlusImpl<ProcessProcessSchemeMapper, ProcessProcessScheme> implements ProcessProcessSchemeService {

        @Autowired
        private BaseController baseController;

    @Autowired
    private AmcFeignService amcFeignService;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 流程配置-流程方案分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public List<ProcessProcessSchemeVo> getProcessProcessSchemeList(QueryDto queryDto) {
            QueryWrapper<ProcessProcessScheme> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询流程配置-流程方案数据*/
            List<ProcessProcessScheme> processProcessSchemeList = baseMapper.selectList(queryWrapper);
            /*转换vo*/
            List<ProcessProcessSchemeVo> list = BeanUtil.copyToList(processProcessSchemeList, ProcessProcessSchemeVo.class);
            return list;
        }





        /**
        * 方法描述：新增修改流程配置-流程方案
        *
        * @param addRevampProcessProcessSchemeDto 流程配置-流程方案Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampProcessProcessScheme(AddRevampProcessProcessSchemeDto addRevampProcessProcessSchemeDto) {
                ProcessProcessScheme processProcessScheme = new ProcessProcessScheme();
            if (StringUtils.isEmpty(addRevampProcessProcessSchemeDto.getId())) {
                Map<String,String> map = amcFeignService.getUserAvatar(baseController.getUserId());
                addRevampProcessProcessSchemeDto.setCreatePicture(map.get(baseController.getUserId()));
                /*新增*/
                BeanUtils.copyProperties(addRevampProcessProcessSchemeDto, processProcessScheme);
                processProcessScheme.setCompanyCode(baseController.getUserCompany());
                processProcessScheme.insertInit();
                baseMapper.insert(processProcessScheme);
           } else {
                /*修改*/
                processProcessScheme = baseMapper.selectById(addRevampProcessProcessSchemeDto.getId());
                if (ObjectUtils.isEmpty(processProcessScheme)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampProcessProcessSchemeDto, processProcessScheme);
                processProcessScheme.updateInit();
                baseMapper.updateById(processProcessScheme);
                }
                return true;
         }


         /**
         * 方法描述：删除流程配置-流程方案
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delProcessProcessScheme(String id) {
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
          public Boolean startStopProcessProcessScheme(StartStopDto startStopDto) {
            UpdateWrapper<ProcessProcessScheme> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
