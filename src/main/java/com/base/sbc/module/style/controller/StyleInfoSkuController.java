/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.style.controller;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.GroupUpdate;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.dto.StyleInfoSkuDto;
import com.base.sbc.module.style.entity.StyleInfoSku;
import com.base.sbc.module.style.service.StyleInfoSkuService;
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
* 类描述：款式设计SKU表 Controller类
* @address com.base.sbc.module.style.web.StyleInfoSkuController
* @author LiZan
* @email 2682766618@qq.com
* @date 创建时间：2023-8-24 15:21:34
* @version 1.0
*/
@RestController
@Api(tags = "款式设计SKU表")
@RequestMapping(value = BaseController.SAAS_URL + "/styleInfoSku", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleInfoSkuController {

	@Autowired
	private StyleInfoSkuService styleInfoSkuService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<StyleInfoSku> page(Page page) {
		PageHelper.startPage(page);
		List<StyleInfoSku> list = styleInfoSkuService.list();
		return new PageInfo<>(list);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public StyleInfoSku getById(@PathVariable("id") String id) {
		return styleInfoSkuService.getById(id);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return styleInfoSkuService.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public StyleInfoSku save(@RequestBody StyleInfoSku styleInfoSku) {
		styleInfoSkuService.save(styleInfoSku);
		return styleInfoSku;
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public ApiResult updateStyleInfoSkuById(@RequestBody @Validated(GroupUpdate.class) StyleInfoSkuDto styleInfoSkuDto) {
		styleInfoSkuService.updateStyleInfoSkuById(styleInfoSkuDto);
		return ApiResult.success("修改成功");
	}

}































