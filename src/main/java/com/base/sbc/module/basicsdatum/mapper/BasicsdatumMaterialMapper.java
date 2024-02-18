/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.basicsdatum.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
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
                                                                         @Param("materialCode") String materialCode,
                                                                         @Param("status") String status
    );


	List<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorListQw(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(@Param("companyCode") String companyCode,
                                                                         @Param("materialCode") String materialCode,
                                                                         @Param("status") String status
    );

	List<BasicsdatumMaterialColorSelectVo> getBasicsdatumMaterialColorSelect(@Param("companyCode") String companyCode,
																			 @Param("materialCode") String materialCode);

	List<BasicsdatumMaterialWidthSelectVo> getBasicsdatumMaterialWidthSelect(@Param("companyCode") String companyCode,
																			 @Param("materialCode") String materialCode);

	List<BomSelMaterialVo> getBomSelMaterialList(@Param(Constants.WRAPPER) BaseQueryWrapper qw,
												 @Param("source") String source);

	List<BasicsdatumMaterialOldPageVo> getBasicsdatumMaterialOldPage(@Param("companyCode") String companyCode,
			@Param("materialCode") String materialCode);

	List<WarehouseMaterialVo> getPurchaseMaterialList(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

	String selectMaxMaterialCode(@Param(Constants.WRAPPER) QueryWrapper qc);

    List<BasicsdatumMaterialPageVo> listSku(@Param(Constants.WRAPPER) BaseQueryWrapper<BasicsdatumMaterial> qw);

	/**
	 * 物料被bom使用清单
	 * @return
	 */
	List<BasicsdatumMaterialPageAndStyleVo> getBasicsdatumMaterialAndStyleList(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
// 自定义方法区 不替换的区域【other_end】
}

