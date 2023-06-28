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
import com.base.sbc.module.process.dto.AddRevampProcessStageDto;
import com.base.sbc.module.process.dto.QueryStageDto;
import com.base.sbc.module.process.entity.ProcessStage;
import com.base.sbc.module.process.service.ProcessStageService;
import com.base.sbc.module.process.vo.ProcessStageVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* 类描述：流程配置-阶段 Controller类
* @address com.base.sbc.module.process.web.ProcessStageController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-6-27 14:47:10
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-阶段")
@RequestMapping(value = BaseController.SAAS_URL + "/processStage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessStageController{

	@Autowired
	private ProcessStageService processStageService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessStageList")
	public PageInfo<ProcessStageVo> getProcessStageList(QueryStageDto queryStageDto) {
		return  processStageService.getProcessStageList(queryStageDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessStage")
	public Boolean startStopProcessStage(@Valid @RequestBody StartStopDto startStopDto) {
	return processStageService.startStopProcessStage(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-阶段")
	@PostMapping("/addRevampProcessStage")
	public Boolean addRevampProcessStage(@Valid @RequestBody AddRevampProcessStageDto addRevampProcessStageDto) {
	return processStageService.addRevampProcessStage(addRevampProcessStageDto);
	}

	@ApiOperation(value = "删除流程配置-阶段")
	@DeleteMapping("/delProcessStage")
	public Boolean delProcessStage(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processStageService.delProcessStage(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessStage getById(@PathVariable("id") String id) {
		return processStageService.getById(id);
	}


}































