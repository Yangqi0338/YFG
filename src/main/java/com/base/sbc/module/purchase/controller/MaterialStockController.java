/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.purchase.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.service.MaterialStockService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 类描述：物料库存 Controller类
* @address com.base.sbc.module.purchase.web.MaterialStockController
* @author tzy
* @email 974849633@qq.com
* @date 创建时间：2023-9-13 15:44:13
* @version 1.0
*/
@RestController
@Api(tags = "物料库存")
@RequestMapping(value = BaseController.SAAS_URL + "/materialStock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class MaterialStockController{

	@Autowired
	private MaterialStockService materialStockService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<MaterialStock> page(Page page) {
		PageHelper.startPage(page);
		List<MaterialStock> list = materialStockService.list();
		return new PageInfo<>(list);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public MaterialStock getById(@PathVariable("id") String id) {
		return materialStockService.getById(id);
	}

}































