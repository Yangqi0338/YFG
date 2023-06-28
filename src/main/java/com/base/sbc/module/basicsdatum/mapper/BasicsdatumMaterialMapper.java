/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
/** 
 * 类描述：基础资料-物料档案 dao类
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumMaterialDao
 * @author shenzhixiong  
 * @email  731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:17 
 * @version 1.0  
 */
@Mapper
public interface BasicsdatumMaterialMapper extends BaseMapper<BasicsdatumMaterial> {
// 自定义方法区 不替换的区域【other_start】

	List<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(@Param("companyCode") String companyCode,
			@Param("materialCode") String materialCode);

	List<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(@Param("companyCode") String companyCode,
			@Param("materialCode") String materialCode);
// 自定义方法区 不替换的区域【other_end】
}

