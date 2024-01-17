/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessTemplateDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate;
import com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
* 类描述：款式研发进度模板 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumResearchProcessTemplateController
* @author your name
* @email your email
* @date 创建时间：2024-1-2 17:47:24
* @version 1.0
*/
@RestController
@Api(tags = "款式研发进度模板")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumResearchProcessTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumResearchProcessTemplateController{

	@Autowired
	private BasicsdatumResearchProcessTemplateService basicsdatumResearchProcessTemplateService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<BasicsdatumResearchProcessTemplate> page(BasicsdatumResearchProcessTemplateDto templateDto) {
		return basicsdatumResearchProcessTemplateService.getTemplatePageInfo(templateDto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumResearchProcessTemplate getById(@PathVariable("id") String id) {
		return basicsdatumResearchProcessTemplateService.getTemplateById(id);
	}

	@ApiOperation(value = "启用停用")
	@PostMapping("updateStatus")
	public Boolean updateStatus(@RequestBody BasicsdatumResearchProcessTemplateDto processTemplateDto) {
		return basicsdatumResearchProcessTemplateService.updateEnableFlagById(processTemplateDto);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("delete/{id}")
	public Boolean updateStatus(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return basicsdatumResearchProcessTemplateService.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping("save")
	public BasicsdatumResearchProcessTemplate save(@Valid @RequestBody  BasicsdatumResearchProcessTemplateDto processTemplateDto) {
		BasicsdatumResearchProcessTemplate basicsdatumResearchProcessTemplate = basicsdatumResearchProcessTemplateService.saveTemplate(processTemplateDto);
		return basicsdatumResearchProcessTemplate;
	}

	@ApiOperation(value = "修改")
	@PostMapping("update")
	public BasicsdatumResearchProcessTemplate update(@RequestBody BasicsdatumResearchProcessTemplate basicsdatumResearchProcessTemplate) {
		boolean b = basicsdatumResearchProcessTemplateService.updateById(basicsdatumResearchProcessTemplate);
		if (!b) {
			//影响行数为0（数据未改变或者数据不存在）
			//返回影响行数需要配置jdbcURL参数useAffectedRows=true
		}
		return basicsdatumResearchProcessTemplate;
	}

}































