/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.sample.dto.SampleDto;
import com.base.sbc.module.sample.entity.Technology;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类描述：样衣 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Service
public class SampleServiceImpl extends ServicePlusImpl<SampleMapper, Sample> implements SampleService {


    @Autowired
    private TechnologyService technologyService;

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/
    @Override
    public Sample saveSample(SampleDto dto) {
        Sample sample = getById(dto.getId());
        BeanUtil.copyProperties(dto, sample);
        save(sample);
        //保存工艺信息
        if(ObjectUtil.isNotEmpty(dto.getTechnology())){
            Technology technology=null;
            if(StrUtil.isNotBlank(dto.getId())){
                technology=technologyService.getById(dto.getId());
            }else{
                technology=BeanUtil.copyProperties(dto.getTechnology(),Technology.class);
            }
            technologyService.saveOrUpdate(technology);
        }
        return sample;
    }


/** 自定义方法区 不替换的区域【other_end】 **/

}

