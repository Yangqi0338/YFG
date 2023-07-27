/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumWashIconDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumWashIcon;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumWashIconMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumWashIconService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumWashIconVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：基础资料-洗涤图标 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumWashIconService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-27 17:27:54
 * @version 1.0  
 */
@Service
public class BasicsdatumWashIconServiceImpl extends BaseServiceImpl<BasicsdatumWashIconMapper, BasicsdatumWashIcon> implements BasicsdatumWashIconService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 基础资料-洗涤图标分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<BasicsdatumWashIconVo> getBasicsdatumWashIconList(QueryDto queryDto) {
            /*分页*/
            QueryWrapper<BasicsdatumWashIcon> queryWrapper = new QueryWrapper<>();
            if (queryDto.getPageNum() != 0 && queryDto.getPageSize() != 0) {
                PageHelper.startPage(queryDto);
            }else {
                queryWrapper.eq("status", BaseGlobal.STATUS_NORMAL);
            }
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.like(StringUtils.isNotBlank(queryDto.getName()),"name",queryDto.getName());
            queryWrapper.like(StringUtils.isNotBlank(queryDto.getName()),"code",queryDto.getCode());
            /*查询基础资料-洗涤图标数据*/
            List<BasicsdatumWashIconVo> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumWashIconVo.class);
            PageInfo<BasicsdatumWashIconVo> pageInfo = new PageInfo<>(list);
            return pageInfo;
        }





        /**
        * 方法描述：新增修改基础资料-洗涤图标
        *
        * @param addRevampBasicsdatumWashIconDto 基础资料-洗涤图标Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumWashIcon(AddRevampBasicsdatumWashIconDto addRevampBasicsdatumWashIconDto) {
                BasicsdatumWashIcon basicsdatumWashIcon = new BasicsdatumWashIcon();
            if (StringUtils.isEmpty(addRevampBasicsdatumWashIconDto.getId())) {
                QueryWrapper<BasicsdatumWashIcon> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampBasicsdatumWashIconDto, basicsdatumWashIcon);
                basicsdatumWashIcon.setCompanyCode(baseController.getUserCompany());
                basicsdatumWashIcon.insertInit();
                baseMapper.insert(basicsdatumWashIcon);
           } else {
                /*修改*/
                basicsdatumWashIcon = baseMapper.selectById(addRevampBasicsdatumWashIconDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumWashIcon)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumWashIconDto, basicsdatumWashIcon);
                basicsdatumWashIcon.updateInit();
                baseMapper.updateById(basicsdatumWashIcon);
                }
                return true;
         }


         /**
         * 方法描述：删除基础资料-洗涤图标
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delBasicsdatumWashIcon(String id) {
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
          public Boolean startStopBasicsdatumWashIcon(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumWashIcon> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
