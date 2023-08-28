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
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.dto.StyleInfoColorDto;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.base.sbc.module.style.vo.StyleInfoColorVo;
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
* 类描述：款式设计详情颜色表 Controller类
* @address com.base.sbc.module.style.web.StyleInfoColorController
* @author LiZan
* @email 2682766618@qq.com
* @date 创建时间：2023-8-24 15:21:29
* @version 1.0
*/
@RestController
@Api(tags = "款式设计详情颜色表")
@RequestMapping(value = BaseController.SAAS_URL + "/styleInfoColor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleInfoColorController extends BaseController{

	@Autowired
	private StyleInfoColorService styleInfoColorService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<StyleInfoColorVo> page(@Valid StyleInfoColorDto styleInfoColorDto) {
		return styleInfoColorService.pageList(styleInfoColorDto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public StyleInfoColor getById(@PathVariable("id") String id) {
		return styleInfoColorService.getById(id);
	}


	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return styleInfoColorService.removeByIds(ids);
	}
	@ApiOperation(value = "删除-通过颜色code查询,多个逗号分开")
	@DeleteMapping("/removeByCode")
	public ApiResult removeByCode(@RequestParam("codes") String codes) {
		if (StringUtils.isBlank(codes)) {
			return deleteNotFound("删除失败，颜色code不能为空");
		}
		styleInfoColorService.delStyleInfoColorById(codes, getUserCompany());
		return deleteSuccess("删除成功");
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public StyleInfoColor save(@RequestBody StyleInfoColor styleInfoColor) {
		styleInfoColorService.save(styleInfoColor);
		return styleInfoColor;
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public ApiResult update(@RequestBody @Validated(GroupUpdate.class) StyleInfoColorDto styleInfoColorDto) {
		styleInfoColorService.updateStyleInfoColorById(styleInfoColorDto);
		return updateSuccess("修改成功");
	}

}































