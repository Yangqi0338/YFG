/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumModelTypeDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
* 类描述：基础资料-号型类型 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumModelTypeController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-20 9:31:14
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-号型类型")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumModelType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumModelTypeController{

	@Autowired
	private BasicsdatumModelTypeService basicsdatumModelTypeService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumModelTypeList")
	public PageInfo<BasicsdatumModelTypeVo> getBasicsdatumModelTypeList(QueryDto queryDto) {
		return  basicsdatumModelTypeService.getBasicsdatumModelTypeList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumModelTypeImportExcel")
	public Boolean basicsdatumModelTypeImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumModelTypeService.basicsdatumModelTypeImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumModelTypeDeriveExcel")
	public void basicsdatumModelTypeDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumModelTypeService.basicsdatumModelTypeDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumModelType")
	public Boolean startStopBasicsdatumModelType(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumModelTypeService.startStopBasicsdatumModelType(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-号型类型")
	@PostMapping("/addRevampBasicsdatumModelType")
	public Boolean addRevampBasicsdatumModelType(@Valid @RequestBody AddRevampBasicsdatumModelTypeDto addRevampBasicsdatumModelTypeDto) {
	return basicsdatumModelTypeService.addRevampBasicsdatumModelType(addRevampBasicsdatumModelTypeDto);
	}

	@ApiOperation(value = "删除基础资料-号型类型")
	@DeleteMapping("/delBasicsdatumModelType")
	public Boolean delBasicsdatumModelType(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumModelTypeService.delBasicsdatumModelType(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumModelType getById(@PathVariable("id") String id) {
		return basicsdatumModelTypeService.getById(id);
	}


}































