/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialColorMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：基础资料-物料档案-物料颜色 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:22
 * @version 1.0  
 */
@Service
public class BasicsdatumMaterialColorServiceImpl
		extends BaseServiceImpl<BasicsdatumMaterialColorMapper, BasicsdatumMaterialColor>
		implements BasicsdatumMaterialColorService {

	// 自定义方法区 不替换的区域【other_start】
	@Override
	public void copyByMaterialCode(String materialCode, String newMaterialCode) {
		QueryWrapper<BasicsdatumMaterialColor> qw = new QueryWrapper<>();
		qw.eq("material_code", materialCode);
		qw.eq("company_code", getCompanyCode());
		List<BasicsdatumMaterialColor> basicsdatumMaterialOlds = super.list(qw);
		if (CollectionUtils.isNotEmpty(basicsdatumMaterialOlds)) {
			basicsdatumMaterialOlds.forEach(e -> {
				e.insertInit();
				e.setUpdateId(null);
				e.setUpdateName(null);
				e.setUpdateDate(null);
				e.setId(null);
				e.setMaterialCode(newMaterialCode);
			});
			super.saveBatch(basicsdatumMaterialOlds);
		}
	}

	@Override
	public void updateMaterialCode(String oldMaterialCode, String newMaterialCode) {
		LambdaUpdateWrapper<BasicsdatumMaterialColor> updateWrapper = new UpdateWrapper<BasicsdatumMaterialColor>()
				.lambda()
				.eq(BasicsdatumMaterialColor::getMaterialCode, oldMaterialCode)
				.set(BasicsdatumMaterialColor::getMaterialCode, newMaterialCode);
		super.update(updateWrapper);
	}

	@Override
	public List<BasicsdatumMaterialColor> getBasicsdatumMaterialColorCodeList(String materialCode, String colorCode) {
		QueryWrapper<BasicsdatumMaterialColor> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(BasicsdatumMaterialColor::getMaterialCode,materialCode);
		queryWrapper.lambda().eq(BasicsdatumMaterialColor::getColorCode,colorCode);
		queryWrapper.lambda().eq(BasicsdatumMaterialColor::getDelFlag,"0");
		queryWrapper.lambda().eq(BasicsdatumMaterialColor::getStatus,"0");
		return list(queryWrapper);
	}


// 自定义方法区 不替换的区域【other_end】

}

