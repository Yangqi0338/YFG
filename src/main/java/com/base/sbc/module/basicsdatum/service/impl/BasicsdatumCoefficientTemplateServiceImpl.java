/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddUpdateCoefficientTemplateDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumCoefficientTemplateDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplate;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBomTemplateVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCoefficientTemplateVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumCoefficientTemplateMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 类描述：基础资料-纬度系数模板 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0  
 */
@Service
public class BasicsdatumCoefficientTemplateServiceImpl extends BaseServiceImpl<BasicsdatumCoefficientTemplateMapper, BasicsdatumCoefficientTemplate> implements BasicsdatumCoefficientTemplateService {
    /**
     * 获取模板列表
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo getCoefficientTemplateList(BasicsdatumCoefficientTemplateDto dto) {
        QueryWrapper<BasicsdatumCoefficientTemplate> queryWrapper = new QueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()),"name",dto.getName());
        queryWrapper.in(StringUtils.isNotBlank(dto.getChannel()),"channel",StringUtils.convertList(dto.getChannel()));
        /*查询*/
        Page<BasicsdatumCoefficientTemplateVo> objects = PageHelper.startPage(dto);
        baseMapper.selectList(queryWrapper);
        return objects.toPageInfo();
    }

    /**
     * 新增保存
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean addUpdateCoefficientTemplate(AddUpdateCoefficientTemplateDto dto) {
        BasicsdatumCoefficientTemplate basicsdatumCoefficientTemplate = new BasicsdatumCoefficientTemplate();
        /*判断新增修改*/
        if (StringUtils.isNotBlank(dto.getId())) {
            /*修改*/
            basicsdatumCoefficientTemplate = baseMapper.selectById(dto.getId());
            if (!StrUtil.equals(dto.getName(), basicsdatumCoefficientTemplate.getName())) {
                //            校验名称重复
                QueryWrapper<BasicsdatumCoefficientTemplate> queryWrapper = new QueryWrapper();
                queryWrapper.eq("name", dto.getName());
                if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                    throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
                }
            }
            BeanUtils.copyProperties(dto, basicsdatumCoefficientTemplate);
            baseMapper.updateById(basicsdatumCoefficientTemplate);
        } else {
//            校验名称重复
            QueryWrapper<BasicsdatumCoefficientTemplate> queryWrapper = new QueryWrapper();
            queryWrapper.eq("name", dto.getName());
            if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            BeanUtils.copyProperties(dto, basicsdatumCoefficientTemplate);
            baseMapper.insert(basicsdatumCoefficientTemplate);
        }
        return true;
    }

    /**
     * 停用启用
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean startStopCoefficientTemplate(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumCoefficientTemplate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
