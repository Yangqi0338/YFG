/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.sample.vo.SamplePageVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.sample.entity.Sample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：样衣 dao类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.dao.SampleDao
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Mapper
public interface SampleMapper extends BaseMapper<Sample> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    List<SamplePageVo> selectByQw(@Param(Constants.WRAPPER) QueryWrapper<Sample> wrapper);

/** 自定义方法区 不替换的区域【other_end】 **/
}

