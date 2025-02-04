/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：款式管理-面料汇总 dao类
 * @address com.base.sbc.module.fabricsummary.dao.FabricSummaryDao
 * @author your name  
 * @email  your email
 * @date 创建时间：2024-3-28 15:25:40 
 * @version 1.0  
 */
@Mapper
public interface FabricSummaryMapper extends BaseMapper<FabricSummary> {
    List<String> fabricSummaryIdList(@Param(Constants.WRAPPER)BaseQueryWrapper<String> qw);

    List<FabricSummaryInfoVo> fabricSummaryInfoVoList( @Param(Constants.WRAPPER) BaseQueryWrapper<FabricSummaryInfoVo> qw);


    int getSerialNumberMax(@Param("groupId")String groupId);

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
}