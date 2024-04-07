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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：基础资料-物料档案-供应商报价 dao类
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumMaterialPriceDao
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:25
 */
@Mapper
public interface BasicsdatumMaterialPriceMapper extends BaseMapper<BasicsdatumMaterialPrice> {
// 自定义方法区 不替换的区域【other_start】

    List<BomSelMaterialVo> findDefaultToBomSel(@Param(Constants.WRAPPER) QueryWrapper<BasicsdatumMaterialPrice> qw);

    String supplierAbbreviation(@Param(Constants.WRAPPER)QueryWrapper qw);


// 自定义方法区 不替换的区域【other_end】
}

