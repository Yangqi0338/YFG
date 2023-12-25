/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.style.controller;
import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.dto.StyleSpecFabricDto;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.entity.StyleSpecFabric;
import com.base.sbc.module.style.service.StyleSpecFabricService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 类描述：款式BOM指定面料表 Controller类
* @address com.base.sbc.module.style.web.StyleSpecFabricController
* @author your name
* @email your email
* @date 创建时间：2023-12-25 16:04:37
* @version 1.0
*/
@RestController
@Api(tags = "款式BOM指定面料表")
@RequestMapping(value = BaseController.SAAS_URL + "/styleSpecFabric", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleSpecFabricController{

	@Autowired
	private StyleSpecFabricService styleSpecFabricService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<StyleSpecFabric> page(Page page) {
		PageHelper.startPage(page);
		List<StyleSpecFabric> list = styleSpecFabricService.list();
		return new PageInfo<>(list);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public StyleSpecFabric getById(@PathVariable("id") String id) {
		return styleSpecFabricService.getById(id);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		StyleSpecFabric styleSpecFabric = new StyleSpecFabric();
		styleSpecFabric.setId(id);
		styleSpecFabric.setDelFlag("1");
		return styleSpecFabricService.updateById(styleSpecFabric);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public StyleSpecFabric save(@Valid @RequestBody StyleSpecFabricDto styleSpecFabricDto) {
		StyleSpecFabric styleSpecFabric = BeanUtil.copyProperties(styleSpecFabricDto, StyleSpecFabric.class);
		styleSpecFabricService.save(styleSpecFabric);
		return styleSpecFabric;
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public StyleSpecFabric update(@Valid @RequestBody StyleSpecFabricDto styleSpecFabricDto) {
		StyleSpecFabric styleSpecFabric = BeanUtil.copyProperties(styleSpecFabricDto, StyleSpecFabric.class);
		styleSpecFabricService.updateById(styleSpecFabric);
		return styleSpecFabric;
	}

}































