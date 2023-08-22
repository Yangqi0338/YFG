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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：基础资料-BOM模板与物料档案中间表 dao类
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumBomTemplateMaterialDao
 * @author mengfanjiang  
 * @email  XX.com
 * @date 创建时间：2023-8-22 17:27:44 
 * @version 1.0  
 */
@Mapper
public interface BasicsdatumBomTemplateMaterialMapper extends BaseMapper<BasicsdatumBomTemplateMaterial> {
// 自定义方法区 不替换的区域【other_start】

  List<BasicsdatumMaterialPageVo>  getBomTemplateMateriaList(@Param(Constants.WRAPPER) QueryWrapper qw);


// 自定义方法区 不替换的区域【other_end】
}