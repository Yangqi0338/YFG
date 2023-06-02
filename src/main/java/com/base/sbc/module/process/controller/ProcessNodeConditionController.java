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
import com.base.sbc.module.process.dto.AddRevampProcessNodeConditionDto;
import com.base.sbc.module.process.entity.ProcessNodeCondition;
import com.base.sbc.module.process.service.ProcessNodeConditionService;
import com.base.sbc.module.process.vo.ProcessNodeConditionVo;
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
* 类描述：流程配置-节点条件 Controller类
* @address com.base.sbc.module.process.web.ProcessNodeConditionController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-2 20:15:15
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-节点条件")
@RequestMapping(value = BaseController.SAAS_URL + "/processNodeCondition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessNodeConditionController{

	@Autowired
	private ProcessNodeConditionService processNodeConditionService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessNodeConditionList")
	public PageInfo<ProcessNodeConditionVo> getProcessNodeConditionList(QueryDto queryDto) {
		return  processNodeConditionService.getProcessNodeConditionList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessNodeCondition")
	public Boolean startStopProcessNodeCondition(@Valid @RequestBody StartStopDto startStopDto) {
	return processNodeConditionService.startStopProcessNodeCondition(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-节点条件")
	@PostMapping("/addRevampProcessNodeCondition")
	public Boolean addRevampProcessNodeCondition(@Valid @RequestBody AddRevampProcessNodeConditionDto addRevampProcessNodeConditionDto) {
	return processNodeConditionService.addRevampProcessNodeCondition(addRevampProcessNodeConditionDto);
	}

	@ApiOperation(value = "删除流程配置-节点条件")
	@DeleteMapping("/delProcessNodeCondition")
	public Boolean delProcessNodeCondition(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processNodeConditionService.delProcessNodeCondition(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessNodeCondition getById(@PathVariable("id") String id) {
		return processNodeConditionService.getById(id);
	}


}































