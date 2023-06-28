/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

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
import com.base.sbc.module.sample.dto.AddRevampSampleStyleColorDto;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.mapper.SampleStyleColorMapper;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：样衣-款式配色 service类
 * @address com.base.sbc.module.sample.service.SampleStyleColorService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 * @version 1.0  
 */
@Service
public class SampleStyleColorServiceImpl extends BaseServiceImpl<SampleStyleColorMapper, SampleStyleColor> implements SampleStyleColorService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 样衣-款式配色分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<SampleStyleColorVo> getSampleStyleColorList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<SampleStyleColor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询样衣-款式配色数据*/
            List<SampleStyleColor> sampleStyleColorList = baseMapper.selectList(queryWrapper);
            PageInfo<SampleStyleColor> pageInfo = new PageInfo<>(sampleStyleColorList);
            /*转换vo*/
            List<SampleStyleColorVo> list = BeanUtil.copyToList(sampleStyleColorList, SampleStyleColorVo.class);
            PageInfo<SampleStyleColorVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改样衣-款式配色
        *
        * @param addRevampSampleStyleColorDto 样衣-款式配色Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampSampleStyleColor(AddRevampSampleStyleColorDto addRevampSampleStyleColorDto) {
                SampleStyleColor sampleStyleColor = new SampleStyleColor();
            if (StringUtils.isEmpty(addRevampSampleStyleColorDto.getId())) {
                QueryWrapper<SampleStyleColor> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampSampleStyleColorDto, sampleStyleColor);
                sampleStyleColor.setCompanyCode(baseController.getUserCompany());
                sampleStyleColor.insertInit();
                baseMapper.insert(sampleStyleColor);
           } else {
                /*修改*/
                sampleStyleColor = baseMapper.selectById(addRevampSampleStyleColorDto.getId());
                if (ObjectUtils.isEmpty(sampleStyleColor)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampSampleStyleColorDto, sampleStyleColor);
                sampleStyleColor.updateInit();
                baseMapper.updateById(sampleStyleColor);
                }
                return true;
         }


         /**
         * 方法描述：删除样衣-款式配色
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delSampleStyleColor(String id) {
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
          public Boolean startStopSampleStyleColor(StartStopDto startStopDto) {
            UpdateWrapper<SampleStyleColor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
