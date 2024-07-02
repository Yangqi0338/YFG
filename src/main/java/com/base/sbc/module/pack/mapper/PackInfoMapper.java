/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.pack.dto.PricingSelectSearchDTO;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.PricingSelectListVO;
import com.base.sbc.module.pricing.vo.PricingVO;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：资料包 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.dao.PackInfoDao
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@Mapper
public interface PackInfoMapper extends BaseMapper<PackInfo> {
// 自定义方法区 不替换的区域【other_start】

    List<PackInfoListVo> queryByQw(@Param(Constants.WRAPPER) QueryWrapper<PackInfo> qw);


    List<PackInfoListVo> queryByListQw(@Param(Constants.WRAPPER) QueryWrapper<PackInfo> qw);
    List<PackInfoListVo> queryByListQw_COUNT(@Param(Constants.WRAPPER) QueryWrapper<PackInfo> qw);

    /**
     * 核价管理选择制版单列表
     * @param search
     * @return
     */
    List<PricingSelectListVO> pricingSelectList(@Param("dto") PricingSelectSearchDTO dto);

    /**
     * 通过id获取核价对象
     *
     * @param id
     * @return
     */
    PricingVO getPricingVoById(@Param("id") String id);

    long countByQw(@Param(Constants.WRAPPER) QueryWrapper codeQw);

    List<FabricSummaryInfoVo> selectFabricSummaryStyle(@Param("dto") FabricSummaryV2Dto dto,@Param(Constants.WRAPPER)  BaseQueryWrapper qw);

// 自定义方法区 不替换的区域【other_end】
}