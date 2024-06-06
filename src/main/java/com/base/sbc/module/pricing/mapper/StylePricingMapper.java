/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.style.dto.StyleColorsDto;
import com.base.sbc.module.style.vo.StyleColorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：款式定价 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.dao.StylePricingDao
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 */
@Mapper
public interface StylePricingMapper extends BaseMapper<StylePricing> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取款式定价列表
     *
     * @param dto
     * @return
     */
    List<StylePricingVO> getStylePricingList(@Param("dto") StylePricingSearchDTO dto,
                                             @Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<StyleColorVo> getByStyleList(@Param(Constants.WRAPPER) QueryWrapper qw, @Param("dto") StyleColorsDto dto);

    List<StylePricingVO> getStylePricingByLine(@Param("dto") StylePricingSearchDTO dto,
                                               @Param(Constants.WRAPPER) QueryWrapper qw);

    Long getStylePricingByLine_COUNT(@Param("dto") StylePricingSearchDTO dto,
                                     @Param(Constants.WRAPPER) QueryWrapper qw);

// 自定义方法区 不替换的区域【other_end】


    List<StylePricingVO> getStylePricingList_COUNT(@Param("dto") StylePricingSearchDTO dto,
                                                   @Param(Constants.WRAPPER) BaseQueryWrapper qw);
}