/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.style.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.style.dto.StyleSpecFabricDto;
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

	@ApiOperation(value = "根据配色id得到指定面料列表")
	@GetMapping("/list")
	public List<StyleSpecFabric> list(String styleColorId) {
		if (StrUtil.isEmpty(styleColorId)) {
			throw new OtherException("配色id不能为空");
		}
		QueryWrapper<StyleSpecFabric> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("style_color_id",styleColorId);
		List<StyleSpecFabric> list = styleSpecFabricService.list(queryWrapper);
		return list;
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		StyleSpecFabric styleSpecFabric = new StyleSpecFabric();
		styleSpecFabric.setId(id);
		styleSpecFabric.setDelFlag("1");
		return styleSpecFabricService.updateById(styleSpecFabric);
	}

	@ApiOperation(value = "批量保存")
	@PostMapping("/batchSave")
	public Boolean batchSave(@Valid @RequestBody List<StyleSpecFabricDto> styleSpecFabricDtoList) {
		return styleSpecFabricService.batchSaveAndClearHistoryData(styleSpecFabricDtoList);
	}
}































