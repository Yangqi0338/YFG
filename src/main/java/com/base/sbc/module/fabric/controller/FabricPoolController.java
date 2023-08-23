/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.fabric.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.fabric.entity.FabricPool;
import com.base.sbc.module.fabric.service.FabricPoolService;
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
* 类描述：面料池 Controller类
* @address com.base.sbc.module.fabric.web.FabricPoolController
* @author your name
* @email your email
* @date 创建时间：2023-8-23 11:02:50
* @version 1.0
*/
@RestController
@Api(tags = "面料池")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricPool", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricPoolController{

	@Autowired
	private FabricPoolService fabricPoolService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<FabricPool> page(Page page) {
		PageHelper.startPage(page);
		List<FabricPool> list = fabricPoolService.list();
		return new PageInfo<>(list);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public FabricPool getById(@PathVariable("id") String id) {
		return fabricPoolService.getById(id);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return fabricPoolService.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public FabricPool save(@RequestBody FabricPool fabricPool) {
		fabricPoolService.save(fabricPool);
		return fabricPool;
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public FabricPool update(@RequestBody FabricPool fabricPool) {
		boolean b = fabricPoolService.updateById(fabricPool);
		if (!b) {
			//影响行数为0（数据未改变或者数据不存在）
			//返回影响行数需要配置jdbcURL参数useAffectedRows=true
		}
		return fabricPool;
	}

}































