/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumIngredientDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumIngredientDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumIngredient;
import com.base.sbc.module.basicsdatum.service.BasicsdatumIngredientService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumIngredientVo;
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
* 类描述：基础资料-材料成分 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumIngredientController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-19 19:15:00
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-材料成分")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumIngredient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumIngredientController {

	@Autowired
	private BasicsdatumIngredientService basicsdatumIngredientService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumIngredientList")
	public PageInfo<BasicsdatumIngredientVo> getBasicsdatumIngredientList(BasicsdatumIngredientDto queryDto) {
		return  basicsdatumIngredientService.getBasicsdatumIngredientList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumIngredientImportExcel")
	public ApiResult basicsdatumIngredientImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return ApiResult.success("操作成功", basicsdatumIngredientService.basicsdatumIngredientImportExcel(file)) ;
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumIngredientDeriveExcel")
	public void basicsdatumIngredientDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumIngredientService.basicsdatumIngredientDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumIngredient")
	public Boolean startStopBasicsdatumIngredient(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumIngredientService.startStopBasicsdatumIngredient(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-材料成分")
	@PostMapping("/addRevampBasicsdatumIngredient")
	public Boolean addRevampBasicsdatumIngredient(@Valid @RequestBody AddRevampBasicsdatumIngredientDto addRevampBasicsdatumIngredientDto) {
	return basicsdatumIngredientService.addRevampBasicsdatumIngredient(addRevampBasicsdatumIngredientDto);
	}

	@ApiOperation(value = "删除基础资料-材料成分")
	@DeleteMapping("/delBasicsdatumIngredient")
	public Boolean delBasicsdatumIngredient(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumIngredientService.delBasicsdatumIngredient(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumIngredient getById(@PathVariable("id") String id) {
		return basicsdatumIngredientService.getById(id);
	}


}































