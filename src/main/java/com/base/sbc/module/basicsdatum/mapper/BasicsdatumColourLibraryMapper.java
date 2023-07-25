/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumColourLibraryExcelDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：基础资料-颜色库 dao类
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumColourLibraryDao
 * @author mengfanjiang  
 * @email  2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:02 
 * @version 1.0
 */
@Mapper
public interface BasicsdatumColourLibraryMapper extends BaseMapper<BasicsdatumColourLibrary> {
/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/
    List<BasicsdatumColourLibraryExcelDto> selectColourLibrary(@Param(Constants.WRAPPER) QueryWrapper<BasicsdatumColourLibraryExcelDto> wrapper);

    List<SelectOptionsVo> getAllColourSpecification(@Param(Constants.WRAPPER) QueryWrapper<BasicsdatumColourLibrary> qw);

/** 自定义方法区 不替换的区域【other_end】 **/
}