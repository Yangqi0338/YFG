/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.process.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.process.dto.AddRevampProcessProcessSchemeDto;
import com.base.sbc.module.process.entity.ProcessProcessScheme;
import com.base.sbc.module.process.service.ProcessProcessSchemeService;
import com.base.sbc.module.process.vo.ProcessProcessSchemeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 类描述：流程配置-流程方案 Controller类
* @address com.base.sbc.module.process.web.ProcessProcessSchemeController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-2 20:15:15
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-流程方案")
@RequestMapping(value = BaseController.SAAS_URL + "/processProcessScheme", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessProcessSchemeController{

	@Autowired
	private ProcessProcessSchemeService processProcessSchemeService;

	@ApiOperation(value = "查询流程方案")
	@GetMapping("/getProcessProcessSchemeList")
	public List<ProcessProcessSchemeVo> getProcessProcessSchemeList(QueryDto queryDto) {
		return  processProcessSchemeService.getProcessProcessSchemeList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessProcessScheme")
	public Boolean startStopProcessProcessScheme(@Valid @RequestBody StartStopDto startStopDto) {
	return processProcessSchemeService.startStopProcessProcessScheme(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-流程方案")
	@PostMapping("/addRevampProcessProcessScheme")
	public Boolean addRevampProcessProcessScheme(@Valid @RequestBody AddRevampProcessProcessSchemeDto addRevampProcessProcessSchemeDto) {
	return processProcessSchemeService.addRevampProcessProcessScheme(addRevampProcessProcessSchemeDto);
	}

	@ApiOperation(value = "删除流程配置-流程方案")
	@DeleteMapping("/delProcessProcessScheme")
	public Boolean delProcessProcessScheme(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processProcessSchemeService.delProcessProcessScheme(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessProcessScheme getById(@PathVariable("id") String id) {
		return processProcessSchemeService.getById(id);
	}


}































